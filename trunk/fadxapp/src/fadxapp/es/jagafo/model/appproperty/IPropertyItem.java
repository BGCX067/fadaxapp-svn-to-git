package fadxapp.es.jagafo.model.appproperty;

/**
 * Interfaz que implementara el provedor de propiedades para la parte visual de
 * la aplicacion
 */
public interface IPropertyItem {
	static final String PROPERTY_NAME = "propiedad";
	static final String PROPERTY_KEY = "propertyKey";
	static final String LABEL = "label";
	static final String TYPE = "type";
	static final String DEBUG_END_KEY = "_debug";
	static final String DEBUG_KEY_VALUE = "debugModeValue";

	static final String TYPE_FDBROWSER = "folderBrowser";
	static final String TYPE_FIBROWSER = "fileBrowser";
	static final String TYPE_SIMPLE_TEXT = "simpleText";
	static final String TYPE_XPATH = "xpathText";
	static final String TYPE_BOOLEAN = "boolean";
	static final String TYPE_EXTENSION = "extensionText";
	static final String TYPE_FOLDER_NAME = "folderName";

	/**
	 * Devuelve la clave de la propiedad
	 * 
	 * @return
	 */
	public abstract String getKey();

	/**
	 * Establece la clave de la propiedad
	 * 
	 * @param key
	 */
	public abstract void setKey(String key);

	/**
	 * Devuelve la descripcion de la propiedad
	 * 
	 * @return
	 */
	public abstract String getLabel();

	/**
	 * Establece la descripción de la propiedad
	 * 
	 * @param label
	 */
	public abstract void setLabel(String label);

	/**
	 * Devuelve el valor de la propiedad
	 * 
	 * @return
	 */
	public abstract String getValue();

	/**
	 * Establece el valor de la propiedad
	 * 
	 * @param value
	 */
	public abstract void setValue(String value);

	/**
	 * Devuelve el tipo de la propiedad. Toma un valor de las constantes de
	 * {@link IPropertiesKeys}
	 * 
	 * @return
	 */
	public abstract String getType();

	/**
	 * Establece el tipo de propiedad Toma un valor las constantes de
	 * {@link IPropertiesKeys}
	 * 
	 * @param type
	 */
	public abstract void setType(String type);

	/**
	 * Devuelve si el antiguo valor de la propiedad es distinto del nuevo valor
	 * que se pasa como parametro
	 * 
	 * @param newValue
	 *            Valor con el que comparar el de la propiedad
	 * @return True si son distintos, false en otro caso
	 */
	public abstract boolean isDirty(String newValue);

	/**
	 * Devuelve la representacion en string de la propiedad
	 * 
	 * @return
	 */
	public abstract String toString();
}
