package fadxapp.es.jagafo.services;

import org.apache.commons.io.IOUtils;

public class ConfigurationPaths {
	
	public static final String PATH_XML_FILES = "ConfigurationFiles";

	public static final String FICH_CONFIG_PATHS = PATH_XML_FILES+IOUtils.DIR_SEPARATOR+"paths.xml";

	public static final String FICH_CONFIG_SERVICES = PATH_XML_FILES+IOUtils.DIR_SEPARATOR+"services.xml";

	public static final String FICH_CONFIG_LOGS = PATH_XML_FILES+IOUtils.DIR_SEPARATOR+"log4j2.xml";

	public static final String FICH_CONFIG_CACHE = PATH_XML_FILES+IOUtils.DIR_SEPARATOR+"Cache.properties";
	
	public static final String FICH_CONFIG_SQLITE = PATH_XML_FILES+IOUtils.DIR_SEPARATOR+"sqlite.properties";

	public static final String TAG_DOCUMENT_FACTORY = "documentfactory";
	
	public static final String FICH_CONFIG_PROPERTIES_NAME = "AppProperties.xml";
	
	public static final String FICH_CONFIG_PROPERTIES = PATH_XML_FILES+IOUtils.DIR_SEPARATOR+FICH_CONFIG_PROPERTIES_NAME;
}
