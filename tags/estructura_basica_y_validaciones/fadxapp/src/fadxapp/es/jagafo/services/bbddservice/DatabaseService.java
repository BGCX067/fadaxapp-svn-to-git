package fadxapp.es.jagafo.services.bbddservice;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import fadxapp.es.jagafo.services.ConfigurationPaths;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.AccessResourcesService;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.logservice.ILogService;
import fadxapp.es.jagafo.services.logservice.LogService;
import fadxapp.es.jagafo.utils.DesktopCtes;

public class DatabaseService implements IDatabaseService  {
	static final String KEY_NAME = "bbdd.name";
	static final String KEY_DESTINATION_BBDD = "bbdd.destinationpath";
	
	private String databaseName;
	private String destinationPath;
	
	public DatabaseService (){
		init();
	}
	
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDestinationPath() {
		return destinationPath;
	}

	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	private void init() {
		IAccessResourcesService resourceService = 
				(IAccessResourcesService) ServiceLocator.getService(AccessResourcesService.class);
		Properties properties = new Properties();
		try {
			properties.load(resourceService.getResource(ConfigurationPaths.FICH_CONFIG_SQLITE));
			setDatabaseName(properties.getProperty(KEY_NAME));
			setDestinationPath(properties.getProperty(KEY_DESTINATION_BBDD));
		} catch (IOException e) {
			((ILogService)ServiceLocator.getService(LogService.class))
				.error(this.getClass().getSimpleName(),
						"Error cargando propiedades", e);
		}
	}

	public synchronized Connection createConnection(){
		Connection c = null;
		String path = DesktopCtes.USER_DIR+destinationPath+System.getProperty("file.separator")+databaseName+".db";		
		try{
			FileUtils.forceMkdir(new File(DesktopCtes.USER_DIR+destinationPath));
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+path);
			((ILogService)ServiceLocator.getService(LogService.class))
			.debug(this.getClass().getSimpleName(),
					"Se ha creado conexion a "+path);
		}catch(Exception e){
			((ILogService)ServiceLocator.getService(LogService.class))
			.error(this.getClass().getSimpleName(),
					"Error creando conexion a "+path, e);
		}
		return c;
	}
}
