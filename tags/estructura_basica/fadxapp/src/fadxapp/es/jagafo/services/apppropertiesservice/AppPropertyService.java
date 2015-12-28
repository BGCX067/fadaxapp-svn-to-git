package fadxapp.es.jagafo.services.apppropertiesservice;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.jdom2.Element;

import fadxapp.es.jagafo.controller.exceptions.DisplayedException;
import fadxapp.es.jagafo.controller.exceptions.InternalErrorException;
import fadxapp.es.jagafo.controller.exceptions.ServiceException;
import fadxapp.es.jagafo.controller.exceptions.XMLException;
import fadxapp.es.jagafo.model.appproperty.IPropertiesKeys;
import fadxapp.es.jagafo.model.appproperty.IPropertyItem;
import fadxapp.es.jagafo.model.appproperty.PropertyItem;
import fadxapp.es.jagafo.services.ConfigurationPaths;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.cacheservice.ICacheService;
import fadxapp.es.jagafo.services.logservice.ILogService;
import fadxapp.es.jagafo.services.xmlservice.core.DocumentFactoryFacade;
import fadxapp.es.jagafo.utils.RCPUtils;

/**
 * Devuelve una lista con las propiedades utilizadas por la aplicacion
 * @author jagafo
 */
public class AppPropertyService implements IAppPropertyService {
	private static final String XPATH_PROPIEDAD = "//" + IPropertyItem.PROPERTY_NAME;
	private static ILogService logService = ((ILogService) ServiceLocator
			.getService(ILogService.class));
	public static final String CONFIG_FILE = "configuration.properties";
	
	private Map<String,PropertyItem> items;
	private boolean isPropertiesLoaded = false;
	private Properties properties = null;
	
	public AppPropertyService() {
		Job loadPropertiesJob = new Job("loadPropertiesJob") {

			protected IStatus run(IProgressMonitor monitor) {
				// Si es la primera vez que arranca la aplicacion o bien se ha modificado el fichero XML propiedades
				// se recargan las propiedades
				if (Boolean.parseBoolean(((IAppPropertyService) ServiceLocator
						.getService(IAppPropertyService.class))
						.loadPreference(IPropertiesKeys.FIRST_LAUNCH_KEY,
								"true"))){
					loadDefaultProperties();
				}
				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;
				if (filePropertiesChanged()) 
					loadDefaultProperties();
				else
					checkXMLPropertiesFileCopy();
				isPropertiesLoaded = true;
				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;	
				return Status.OK_STATUS;
			}
		};
		loadPropertiesJob.setPriority(Job.SHORT);
		loadPropertiesJob.setUser(false);
		loadPropertiesJob.setSystem(false);
		loadPropertiesJob.schedule();
	}
	
	/**
	 * Lee el fichero xml de propiedades y devuelve una lista de
	 * {@link PropertyItem}
	 * 
	 * @param String
	 *            ruta del fichero desde donde leer propiedades
	 * @return Lista de propertys leidas del fichero xml
	 */
	private Map<String, PropertyItem> getProperties(String filePath) {
		items = new HashMap<String, PropertyItem>();
		List<Element> domElements = null;
		try {
			domElements = DocumentFactoryFacade.getInstance().getNodes(
					XPATH_PROPIEDAD, filePath, false);
		} catch (XMLException e) {
			new DisplayedException("No se han podido cargar las propiedades de la aplicacion");
		}
		// Para una entrada carga su valor normal y crea otra propiedad si tiene
		// un valor para modo debug
		if (domElements != null) {
			for (int i = 0; i < domElements.size(); i++) {
				PropertyItem property = new PropertyItem(domElements.get(i));
				items.put(property.getKey(), property);
				if (RCPUtils.isDebugMode()
						&& (domElements.get(i))
								.getAttributeValue(IPropertyItem.DEBUG_KEY_VALUE) != null){
					property = new PropertyItem(domElements.get(i),true);
					items.put(property.getKey(), property);
				}
					
			}
		}
		return items;
	}

	private boolean isPropertiesLoaded() {
		return isPropertiesLoaded;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seees.hercules.repository.modellayer.properties.IFactoryPropertyItems#
	 * getProperties()
	 */
	public synchronized Map<String,PropertyItem> getProperties() {
		if (items == null) {
			if (!new File(RCPUtils.CONFIG_DIR
					+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME).exists())
				return getProperties(ConfigurationPaths.FICH_CONFIG_PROPERTIES);
			else
				return getProperties(RCPUtils.CONFIG_DIR
						+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME);
		}
		return items;
	}
	
	public Collection<PropertyItem> getPropertiesCollection(){
		return getProperties().values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.hercules.repository.modellayer.properties.IFactoryPropertyItems#clear
	 * ()
	 */
	public void clear() {
		// Se elimina la cache de propiedades
		((ICacheService) ServiceLocator
				.getService(ICacheService.class))
				.eraseCache(ICacheService.CACHE_PROPERTIES);
		if (items != null) {
			items.clear();
			items = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seees.hercules.repository.modellayer.properties.IFactoryPropertyItems#
	 * filePropertiesChanged()
	 */
	public boolean filePropertiesChanged() {
		boolean changed = false;
		String dataChanged = loadPreference(
				IPropertiesKeys.PROPERTIES_FILE_DATA_CHANGED, "").trim();
		String hourChaned = loadPreference(
				IPropertiesKeys.PROPERTIES_FILE_HOUR_CHANGED, "").trim();
		if (dataChanged.length() != 0 && hourChaned.length() != 0) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd/MM/yyyy.HH:mm:ss");
			try {
				Date date = formatter.parse(dataChanged + "." + hourChaned);

				File f = ((IAccessResourcesService) ServiceLocator
						.getService(IAccessResourcesService.class))
						.getFile(RCPUtils.CONFIG_DIR
								+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME);
				if (f == null || !f.exists()) {
					logService
							.warn(
									this.getClass().getSimpleName(),
									"No se ha encontrado el fichero de propiedades donde se esperaba: "
											+ RCPUtils.CONFIG_DIR
											+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME);
					// Fuerza la copia del fichero desde el plugin
					changed = true;
				} else {
					Date fModified = formatter.parse(formatter.format(f
							.lastModified()));
					if (fModified.after(date)) {
						SimpleDateFormat dateFormatter = new SimpleDateFormat(
								"dd/MM/yyyy");
						SimpleDateFormat timeFormatter = new SimpleDateFormat(
								"HH:mm:ss");
						savePreference(
								IPropertiesKeys.PROPERTIES_FILE_DATA_CHANGED,
								dateFormatter.format(f.lastModified()));
						savePreference(
								IPropertiesKeys.PROPERTIES_FILE_HOUR_CHANGED,
								timeFormatter.format(f.lastModified()));
						logService
								.debug(getClass().getSimpleName(),
										"El fichero de propiedades ha cambiado. Cargando propiedades");
						changed = true;
					}
				}
			} catch (ParseException e) {
				new ServiceException(
					this,
					"No se ha podido parsear la fecha de la ultima actualizacion",
					e);
				changed = true;
			}
		} else {
			File f = ((IAccessResourcesService) ServiceLocator
					.getService(IAccessResourcesService.class))
					.getFile(RCPUtils.CONFIG_DIR
							+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME);
			if (f == null || !f.exists()) {
				logService
						.warn(
								this.getClass().getSimpleName(),
								"No se ha podido encontrar el fichero de configuracion de propiedades en "
										+ RCPUtils.CONFIG_DIR
										+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME);
				// Intentamos provocar la copia y carga del fichero de
				// propiedades
				changed = true;
			} else {
				// Se ha eliminado las propiedades de cache hay que recargarlas
				changed = true;
				SimpleDateFormat dateFormatter = new SimpleDateFormat(
						"dd/MM/yyyy");
				SimpleDateFormat timeFormatter = new SimpleDateFormat(
						"HH:mm:ss");
				savePreference(IPropertiesKeys.PROPERTIES_FILE_DATA_CHANGED,
						dateFormatter.format(f.lastModified()));
				savePreference(IPropertiesKeys.PROPERTIES_FILE_HOUR_CHANGED,
						timeFormatter.format(f.lastModified()));
			}
		}
		return changed;
	}
	
	private void checkXMLPropertiesFileCopy(){
		// Se intenta realizar una copia del fichero xml en el directorio de
		// instalacion
		if (!new File(RCPUtils.CONFIG_DIR
				+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME).exists()) {
			InputStream inputStream = ((IAccessResourcesService) ServiceLocator
					.getService(IAccessResourcesService.class))
					.getResource(ConfigurationPaths.FICH_CONFIG_PROPERTIES);
			if (inputStream != null) {
				try {
					FileUtils.copyInputStreamToFile(
									inputStream,
									new File(RCPUtils.CONFIG_DIR
											+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME));
					logService
							.debug(
									this.getClass().getSimpleName(),
									"Copiado el fichero de propiedades en "
											+ RCPUtils.CONFIG_DIR
											+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME);
				} catch (Exception e) {
					logService
							.warn(this.getClass().getSimpleName(),
									"No se ha podido copiar el fichero de configuracion de propiedades");
				}
			}
		}
		// Las propiedades ya estan cargadas en memoria en el servicio
		// Ahora las vamos a guardar en cache y en el fichero properties. correspondiente
		// Primero creamos el fichero de properties en la aplicacion
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(
					RCPUtils.CONFIG_DIR + CONFIG_FILE));
			properties = new Properties();
			properties.load(is);
		} catch (FileNotFoundException e1) {
			try {
				new File(RCPUtils.CONFIG_DIR).mkdirs();
				new File(RCPUtils.CONFIG_DIR + CONFIG_FILE).createNewFile();
				((ILogService) ServiceLocator
						.getService(ILogService.class)).debug(
						"RCPUtils", "Creando fichero de propiedades en "
								+ RCPUtils.CONFIG_DIR + CONFIG_FILE);
				InputStream is = new BufferedInputStream(new FileInputStream(
						RCPUtils.CONFIG_DIR + CONFIG_FILE));
				properties = new Properties();
				properties.load(is);
			} catch (IOException e) {
				new InternalErrorException(getClass(),"Error al crear el fichero de propiedades");
			}
		} catch (IOException e) {
			new ServiceException(
				this,
				"Error al cargar el fichero de propiedades",
				e);
		}
	}
	
	public synchronized void loadDefaultProperties() {
		checkXMLPropertiesFileCopy();
		isPropertiesLoaded = true;
		for (Iterator<PropertyItem> it = getProperties().values().iterator(); it.hasNext(); ) {
			PropertyItem current = (PropertyItem)it.next();
			savePreference(current.getKey(),current.getValue());
		}
		logService.debug(getClass().getSimpleName(),
				"Cargadas las propiedades por defecto");
		RCPUtils.showMsgStatusLine("Cargadas las propiedades por defecto",false);

		File f = ((IAccessResourcesService) ServiceLocator
				.getService(IAccessResourcesService.class))
				.getFile(RCPUtils.CONFIG_DIR
						+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME);
		if (f != null) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
			savePreference(IPropertiesKeys.PROPERTIES_FILE_DATA_CHANGED,
					dateFormatter.format(f.lastModified()));
			savePreference(IPropertiesKeys.PROPERTIES_FILE_HOUR_CHANGED,
					timeFormatter.format(f.lastModified()));
		}
	}

	public String loadPreference(String key, String defaultValue) {
		return loadPreference(key, defaultValue, false);
	}
	
	public String loadPreference(String key, String defaultValue,
			boolean checkDebug) {
		if (checkDebug && RCPUtils.isDebugMode())
			key = key + IPropertyItem.DEBUG_END_KEY;
		if (((ICacheService) ServiceLocator
				.getService(ICacheService.class)).isEnabled()
				&& ((ICacheService) ServiceLocator
						.getService(ICacheService.class)).get(
						ICacheService.CACHE_PROPERTIES, key) != null)
			return ((ICacheService) ServiceLocator
					.getService(ICacheService.class)).get(
					ICacheService.CACHE_PROPERTIES, key).toString();
		if (isPropertiesLoaded()) {
			Object value = properties.get(key);
			if (value != null) {
				if (((ICacheService) ServiceLocator
						.getService(ICacheService.class)).isEnabled())
					((ICacheService) ServiceLocator
							.getService(ICacheService.class)).put(
							ICacheService.CACHE_PROPERTIES, key, value);
				return value.toString();
			}
			return defaultValue;
		}else{
			// Todavia no estan cargadas las propiedades solo tenemos cargado el fichero XML
			if (!getProperties().containsKey(key)){
				return defaultValue;
			}else{
				return getProperties().get(key).toString();
			}
		}
	}

	public void savePreference(String keyPreference, String value) {
		if (isPropertiesLoaded()) {
			properties.setProperty(keyPreference, value);
			if (((ICacheService) ServiceLocator
					.getService(ICacheService.class)).isEnabled())
				((ICacheService) ServiceLocator
						.getService(ICacheService.class)).put(
						ICacheService.CACHE_PROPERTIES, keyPreference, value);
			OutputStream os;
			try {
				os = new BufferedOutputStream(new FileOutputStream(
						RCPUtils.CONFIG_DIR + CONFIG_FILE));
				properties.store(os,value);
				os.close();
			} catch (FileNotFoundException e) {
				new ServiceException(
					this,
					"Error al guardar el fichero de propiedades", e);
			} catch (IOException e1) {
				new ServiceException(
					this,
					"Error al guardar el fichero de propiedades", e1);
			}
			if (getProperties().containsKey(keyPreference))
				getProperties().get(keyPreference).setValue(value);
		}else{
			// Todavia no estan cargadas las propiedades solo tenemos cargado el fichero XML
			if (!getProperties().containsKey(keyPreference))
				getProperties().put(keyPreference, new PropertyItem(keyPreference, value, IPropertyItem.TYPE_SIMPLE_TEXT));
			else
				getProperties().get(keyPreference).setValue(value);
		}
	}

	public boolean resetPropertyFile() {
		if (new File(RCPUtils.CONFIG_DIR
				+ ConfigurationPaths.FICH_CONFIG_PROPERTIES_NAME).delete()) {
			clear();
			isPropertiesLoaded = false;
			loadDefaultProperties();
			return true;
		}
		return false;
	}
}
