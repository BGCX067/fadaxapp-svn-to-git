package fadxapp.es.jagafo.utils;


public class DesktopCtes {

	static {
		// Path del workspace
		if (System.getProperty("os.name").contains("Linux"))
			USER_DIR = System.getProperty("user.dir")+"/workspace/";
		else
			USER_DIR = System.getProperty("user.dir")+"\\workspace\\";		
	}

	public static String USER_DIR;

	// Comandos introducidos para salvar a disco
	public static final String COMMANDS_TO_SAVE = "COMMANDS_TO_SAVE";

	// Ruta del fich de config de preferencias
	public static final String CONFIG_FILE = "configuration.properties";
}
