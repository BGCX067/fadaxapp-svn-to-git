package fadxapp.es.jagafo.services.cacheservice;

import java.beans.PropertyChangeListener;
import java.util.List;

import fadxapp.es.jagafo.services.IService;
import fadxapp.es.jagafo.services.ServiceImplAnnotation;

/**
 * Servicio que da acceso a la caché. Para implementar la caché, se hace uso del
 * API JCS (Java Cache System) de Jakarta Tomcat. Este API permite definir
 * regiones (similar al concepto de tablas). Estas regiones se pueden configurar
 * para que sean almacenadas en disco, memoria, remotamente, ...
 * @author jagafo
 */
@ServiceImplAnnotation(serviceImpl=CacheService.class)
public interface ICacheService extends IService {

//	Definimos las regiones del fichero Cache.properties
	/** Almacena los objetos de dominio de la aplicacion */
	static final String CACHE_APP_OBJ = "appOBJ";
	/** Almacena las propiedades de la aplicacion */
	static final String CACHE_PROPERTIES = "propertiesReg";
	/** Almacena los documentos a parsear de la aplicacion */
	static final String CACHE_JDOM_DOCUMENTS = "jdomDocuments";
	
	static final String STATUS_CHANGE_EVENT = "cacheStatusChangeEvent";

	/**
	 * Añade listeners al servicio de cache
	 * @param l
	 */
	void addPropertyChangeListener(PropertyChangeListener l);

	/**
	 * Elimina los listeners del servicio de cache
	 * @param l
	 */
	void removePropertyChangeListener(PropertyChangeListener l);

	/**
	 * Dado el nombre de la cache, se almacena con la clave suministrada el objeto
	 * que se ha pasado también como parámetro
	 * <br><b>Usar la clase CommonUserFacadeFactory para acceso de la aplicacion</b>
	 * @param cacheName
	 *            nombre de la cache donde se va a almacenar la info. Nota: las
	 *            cachés disponibles son los atributos estáticos que ofrece esta
	 *            interfaz
	 * @param key
	 *            clave con la que se va a almacenar el objeto en la caché
	 * @param o
	 *            objeto que se va almacenar. Será buscado por la clave
	 *            introducida
	 * 
	 */
	void put(String cacheName, String key, Object o);

	/**
	 * Dada la caché seleccionada, se buscará en esa caché la clave introducida
	 * <br><b>Usar la clase CommonUserFacadeFactory para acceso de la aplicacion</b>
	 * @param cacheName
	 *            nombre de la cache de donde se va a sacar la info. Nota: las
	 *            cachés disponibles son los atributos estáticos que ofrece esta
	 *            interfaz
	 * @param key
	 *            clave a buscar en la cache
	 * @return devuelve el objeto a buscar si lo encuentra. Si no lo encuentra
	 *         devuelve null.
	 */
	Object get(String cacheName, String key);

	/**
	 * Borra de la cache el objeto de cache que coincide con la clave
	 * <br><b>Usar la clase CommonUserFacadeFactory para acceso de la aplicacion</b>
	 * @param cacheName
	 *            nombre de la cache sobre la que se va a borrar el dato
	 * @param key
	 *            clave a borrar de la cache
	 * 
	 */
	void remove(String cacheName, String key);

	/**
	 * Borra todos los datos de la cache
	 * 
	 * @param cacheName
	 *            nombre de la cache a salvar
	 * 
	 */
	void eraseCache(String cacheName);

	/**
	 * Guarda la caché a Disco
	 * 
	 * @param cacheName
	 *            nombre de la cache a salvar
	 * @return True si no hubo problema durante el almacenamiento, False en otro
	 *         caso
	 */
	boolean saveToDisk(String cacheName);

	/**
	 * Guarda toda los espacios de Cache a disco
	 */
	void saveAllCacheToDisk();

	/**
	 * Recarga la cache
	 */
	void reset();

	/**
	 * Devuelve las caches habilitadas para la aplicacion
	 * @return Lista de String con las caches activadas
	 */
	List<String> getAvaliableCaches();

	/**
	 * Establece si la cache esta habilitada
	 * @param b
	 */
	void setEnabled(boolean b);

	/**
	 * Devuelve true si la cache esta habilitada, false en otro caso
	 * @return
	 */
	boolean isEnabled();
	
	/**
	 * Devuelve el directorio donde estan almacenadas las caches
	 * @return
	 */
	String getCacheFilesPath();

	/**
	 * Comprueba si la cache que se pasa como id existe
	 * @param string
	 * @return True si existe, False en otro caso
	 */
	boolean cacheExists(String string);
}