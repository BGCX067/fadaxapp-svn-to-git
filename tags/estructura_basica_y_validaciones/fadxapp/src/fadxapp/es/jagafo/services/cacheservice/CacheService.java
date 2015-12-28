package fadxapp.es.jagafo.services.cacheservice;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;
import org.apache.jcs.engine.control.CompositeCacheManager;

import fadxapp.es.jagafo.controller.exceptions.ServiceException;
import fadxapp.es.jagafo.model.ICacheableTimed;
import fadxapp.es.jagafo.services.ConfigurationPaths;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.logservice.ILogService;
import fadxapp.es.jagafo.utils.RCPUtils;

/**
 * Servicio que implementa la caché. Esta basado en la tecnologia JCS Archivo de
 * configuracion en /configFiles/
 * @author jagafo 
 * @see ICacheService
 */
public class CacheService implements ICacheService {
	/**
	 * Conjunto de regiones de cache que se ponen en funcionamiento y que están
	 * definidas en el fichero ConfiguracionCache.properties
	 */
	private static final List<String> regions;

	/**
	 * Define un mapa que contiene si los eventos de una region son invisibles a
	 * traves de la consola
	 */
	private static final Set<String> hidden;

	private static CompositeCacheManager ccm;

	static {
		regions = new ArrayList<String>();
		regions.add(CACHE_APP_OBJ);
		regions.add(CACHE_PROPERTIES);
		regions.add(CACHE_JDOM_DOCUMENTS);
		hidden = new HashSet<String>();
		hidden.add(CACHE_PROPERTIES);
		hidden.add(CACHE_JDOM_DOCUMENTS);
	};

	private HashMap<java.lang.String, JCS> cacheRegionsMap;
	private boolean isEnabled;
	private String cacheFilesPath;
	

	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	public CacheService() {
		try {
			ServiceLocator.getService(ILogService.class);
			ccm = CompositeCacheManager.getUnconfiguredInstance();
			// Lee el fichero de configuración de la caché y sus regiones
			/* Leemos el fichero de propiedades (si existe) */
			InputStream is = ((IAccessResourcesService) ServiceLocator
					.getService(IAccessResourcesService.class))
					.getResource(ConfigurationPaths.FICH_CONFIG_CACHE);
			Properties properties = new Properties();
			properties.load(is);
			String keyPath = "jcs.auxiliary.DC.attributes.DiskPath";
			String path = (String) properties.get(keyPath);
			cacheFilesPath = RCPUtils.USER_DIR + path;
			properties.put(keyPath, cacheFilesPath);

			ccm.configure(properties);
			String applicationsKey = "jcs.numberApplicationUsingMe";
			if (properties.get(applicationsKey) != null) {
				int num = Integer.parseInt((String) properties
						.get(applicationsKey));
				properties.setProperty(applicationsKey, ++num + "");
			} else {
				properties.setProperty(applicationsKey, 1 + "");
			}
			cacheRegionsMap = new HashMap<String, JCS>();
			for (int i = 0; i < regions.size(); i++) {
				try {
					JCS cache = JCS.getInstance(regions.get(i));
					cacheRegionsMap.put(regions.get(i), cache);
				} catch (Exception e) {
					new ServiceException(this,
							"Error en la inicialización de la region de la cache: "
									+ regions.get(i), e);
				}
			}
			isEnabled = true;
		} catch (Exception e) {
			new ServiceException(this,
					"No se ha podido cargar el fichero de configuración de la cache: "
							+ ConfigurationPaths.FICH_CONFIG_CACHE, e);
			isEnabled = false;
		}
	}
	
	protected void firePropertyChange(String prop, Object old, Object newValue) {
		listeners.firePropertyChange(prop, old, newValue);
	}

	protected void fireStructureChange(String prop, Object child) {
		listeners.firePropertyChange(prop, null, child);
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	/**
	 * Debe usarse el commando {@link es.hercules.repository.presentationlayer.commands.appconfiguration.ToogleStorageServiceCommand}
	 */
	public void setEnabled(boolean b) {
		if (isEnabled != b && cacheRegionsMap != null
				&& !cacheRegionsMap.isEmpty()) {
			firePropertyChange(STATUS_CHANGE_EVENT, isEnabled, b);
			isEnabled = b;
		}
	}
	
	public String getCacheFilesPath(){
		return cacheFilesPath;
	}
	
	public boolean cacheExists(String cacheID){
		return regions.contains(cacheID);
	}

	public List<String> getAvaliableCaches() {
		return regions;
	}

	public synchronized Object get(String cacheName, String key) {
		if (cacheRegionsMap.containsKey(cacheName)) {
			Object obj = cacheRegionsMap.get(cacheName).get(key);
			if (obj != null && !hidden.contains(cacheName))
				((ILogService) ServiceLocator
						.getService(ILogService.class)).debug(
						getClass().getSimpleName(), "Recuperado de la cache: "
								+ cacheName + " el elemento " + key);
			return obj;
		} else {
			if (!hidden.contains(cacheName))
				((ILogService) ServiceLocator
						.getService(ILogService.class)).debug(
						getClass().getSimpleName(),
						"No se encontró el elemento en la cache: " + cacheName);
			return null;
		}
	}

	public synchronized void put(String cacheName, String key, Object o) {
		if (cacheRegionsMap.containsKey(cacheName)) {
			try {
				// Esta nueva linea añadida controla que el evento putSafe se
				// ejecute correctamente.
				if (cacheRegionsMap.get(cacheName).get(key) != null) {
					if (!hidden.contains(cacheName))
						((ILogService) ServiceLocator
								.getService(ILogService.class)).debug(
								this.getClass().getSimpleName(),
								"Eliminando de la cache: " + cacheName
										+ " el elemento: " + key);
					cacheRegionsMap.get(cacheName).remove(key);
				}
				if (o instanceof ICacheableTimed)
					((ICacheableTimed)o).setCachedTime(Calendar.getInstance().getTimeInMillis());
				cacheRegionsMap.get(cacheName).putSafe(key, o);
//				if (cacheName.equalsIgnoreCase(CACHE_HERCULES_OBJ))
//					logKey(getLogFilePath(), key);
				if (!hidden.contains(cacheName))
					((ILogService) ServiceLocator
							.getService(ILogService.class)).debug(this
							.getClass().getSimpleName(),
							"Guardando en la cache: " + cacheName
									+ " el elemento: " + key);
			} catch (CacheException e1) {
				new ServiceException(this,
						"No se ha podido guardar un objeto en la cache con clave: "
								+ key + " en la region " + cacheName, e1);
			}
		} else {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).warn(getClass()
					.getSimpleName(), "No se encuentra la caché: " + cacheName);
		}
	}

	public synchronized void remove(String cacheName, String key) {
		if (cacheRegionsMap.containsKey(cacheName)) {
			try {
				cacheRegionsMap.get(cacheName).remove(key);
			} catch (CacheException e) {
				new ServiceException(this,
						"No se ha podido borrar un objeto en la cache con clave: "
								+ key + " en la tabla " + cacheName, e);
			}
		}
	}

	public synchronized void eraseCache(String cacheName) {
		if (cacheRegionsMap.containsKey(cacheName)) {
			try {
				cacheRegionsMap.get(cacheName).clear();
//				if (cacheName.equalsIgnoreCase(CACHE_HERCULES_OBJ))
//					clearKeyLogFile();
				if (!hidden.contains(cacheName))
					((ILogService) ServiceLocator
							.getService(ILogService.class)).info(
							getClass().getSimpleName(), "Limpiada la caché: "
									+ cacheName);
			} catch (Exception e) {
				new ServiceException(this,
						"No se ha podido borrar la region: " + cacheName, e);
			}
		}
	}

	public synchronized boolean saveToDisk(String cacheName) {
		if (cacheRegionsMap.containsKey(cacheName)) {
			cacheRegionsMap.get(cacheName).dispose();
			return true;
		} else {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).warn(getClass()
					.getSimpleName(),
					"No se encuentra la region que se quiere salvar a disco "
							+ cacheName);
			return false;
		}
	}

	public synchronized void saveAllCacheToDisk() {
		boolean ok = true;
		String failedSaveCaches = "";
		for (String cacheName : cacheRegionsMap.keySet()) {
			ok = saveToDisk(cacheName) & ok;
			if (!ok)
				failedSaveCaches += cacheName + ",";
		}
		if (failedSaveCaches.length() != 0)
			failedSaveCaches = failedSaveCaches.substring(0, failedSaveCaches
					.length() - 1);
		if (ok) {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).debug(getClass()
					.getSimpleName(),
					"Almacenada toda las regiones de cache en disco");
		} else {
			new ServiceException(this,
				"Fallo el guardado de las caches " + failedSaveCaches,new CacheException());
		}
	}

	public void reset() {
		ServiceLocator.removeService(ICacheService.class);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}
}