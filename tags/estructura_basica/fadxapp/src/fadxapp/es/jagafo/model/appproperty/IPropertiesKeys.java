package fadxapp.es.jagafo.model.appproperty;

/**
 * Nombres de las constantes del fichero de propiedades y de las propiedades que
 * utilizará la aplicacion Si se modifica algun valor revisar los ficheros de
 * configurationFiles
 */
public interface IPropertiesKeys {
	// Propiedad que nos permite saber si es la primera vez que se arranca la
	// aplicacion
	static final String FIRST_LAUNCH_KEY = "firstLaunch";
	// Propiedades para el panel de conexion con la base de datos
	static final String USER_DDBB_KEY = "userBBDDKey";
	static final String PASS_DDBB_KEY = "pwdBBDDKey";
	static final String DEFAULT_BOOT_KEY = "defaultBootKey";
	static final String SAVE_DDBB_PASS_KEY = "saveddbbpass";

	static final String LOGVIEW_BUFFER = "logViewBufferKey";
	// Variable que guarda los directorios chekeados para la busqueda de codigo
	static final String CHECKED_SEARCH_FOLDERS = "checkedSearchFoldersKey";
	// Variable que guarda todos los directorios disponibles
	static final String SEARCH_FOLDERS = "searchFoldersKey";
	// Sirve para el dialogo de seleccion de extensiones a buscar
	static final String CHECKED_SEARCH_EXTENSION_KEY = "checkedSearchExtensionsKey";
	
	static final String TEXT_TRX_FILEPATH_KEY = "trxFilePathKey";
	static final String TEXT_TRX_FILEPATH = "D:\\eclipse\trx.xml";

	static final String PROPERTIES_FILE_DATA_CHANGED = "propertiesDateChanged";
	static final String PROPERTIES_FILE_HOUR_CHANGED = "propertiesHourChanged";
	
	static final String BOOL_CLEAN_TARGET_FOLDER_CODE = "cleanTargetFolderSourceCode";
	static final String BOOL_CLEAN_TARGET_FOLDER_XML = "cleanTargetFolderXML";
//	Nombre del equipo donde se está ejecutando la aplicacion
	static final String TEXT_COMPUTERNAME_KEY = "textCompurterName";
//	Directorio que se considerara de pruebas
	static final String FOLDER_DEVELOP_DRIVE_KEY = "FOLDER_DEVELOP_DRIVE_KEY";
	static final String FOLDER_DEVELOP_DRIVE = "H:\\";

}
