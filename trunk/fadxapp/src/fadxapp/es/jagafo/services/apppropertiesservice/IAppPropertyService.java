package fadxapp.es.jagafo.services.apppropertiesservice;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;

import fadxapp.es.jagafo.model.appproperty.PropertyItem;
import fadxapp.es.jagafo.services.IService;
import fadxapp.es.jagafo.services.ServiceImplAnnotation;

/**
 * Interfaz que implementará la clase encargada de devolver las propiedades que
 * utiliza la aplicacion
 * @author jagafo
 */
@ServiceImplAnnotation(serviceImpl=AppPropertyService.class)
public interface IAppPropertyService extends IService {
	/**
	 * Devuelve la lista de propiedades cargadas en la aplicacion a través del
	 * fichero XML de propiedades.
	 * 
	 * @return Mapa de propertys leidas del fichero xml
	 */
	public abstract Map<String,PropertyItem> getProperties();
	
	/**
	 * Devuelve la coleccion de propiedades
	 * @return Coleccion de propiedades cargadas en la aplicacion
	 */
	public abstract Collection<PropertyItem> getPropertiesCollection();

	/**
	 * Limpia la lista de propiedades recogidas desde el fichero
	 */
	public abstract void clear();

	/**
	 * Comprueba si el fichero externo de propiedades ha sido modificado.
	 * Comprueba las fechas de modificacion o bien si el archivo de propiedades
	 * no existe
	 * 
	 * @return True si el fichero ha sido modificado, false en otro caso
	 */
	public abstract boolean filePropertiesChanged();

	/**
	 * La primera vez que se arranca la aplicación se leen todas las propiedades
	 * por defecto
	 */
	public abstract void loadDefaultProperties();

	/**
	 * Copia de nuevo el fichero de propiedades interno de la aplicacion y lo
	 * carga
	 */
	public abstract boolean resetPropertyFile();

	/**
	 * Carga la preferencia salvada en disco de este plugin. En caso de no
	 * encontrar la preferencia, devuelve el valor por defecto pasado por
	 * argumento
	 * 
	 * @param key
	 *            preferencia a buscar
	 * @param defaultValue
	 *            valor por defecto a devolver si no se encuentra la preferencia
	 * @param checkDebug
	 *            Si esta a true se comprueba si estamos en modo debug
	 * @return devuelve la preferencia salvada. Si no la encuentra, devuelve el
	 *         valor por defecto
	 * @throws FileNotFoundException
	 */
	public abstract String loadPreference(String key, String defaultValue,
			boolean checkDebug);

	/**
	 * No chequea si estamos en modo debug a la hora de leer la propiedad
	 * 
	 * @see #loadPreference(String, String, boolean)
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public abstract String loadPreference(String key, String defaultValue);

	/**
	 * Salva la preferencia introducida por el usuario con su clave y valor en
	 * el plugin actual En caso de que no se pueda salvar, se loguea esta
	 * información.
	 * 
	 * @param keyPreference
	 *            preferencia a salvar
	 * @param value
	 *            valor de la preferencia
	 * 
	 */
	public abstract void savePreference(String keyPreference, String value);
}