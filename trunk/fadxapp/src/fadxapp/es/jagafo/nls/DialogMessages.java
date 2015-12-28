package fadxapp.es.jagafo.nls;

import org.eclipse.osgi.util.NLS;

public class DialogMessages extends NLS {
	private static final String BUNDLE_NAME = "dialogs"; //$NON-NLS-1$
	
	static {
	    // initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, DialogMessages.class);
	}
	
	public static String LoginDialog_title;
	public static String LoginDialog_grpDatosUsuari_text;
	public static String LoginDialog_lblLlogonid_text;
	public static String LoginDialog_txtTlogonid_text;
	public static String LoginDialog_lblLemail_text;
	public static String LoginDialog_txtTemail_text;
	public static String LoginDialog_lblContrasea_text;
	public static String LoginDialog_txtPassword_text;
	public static String LoginDialog_txtPassword_toolTipText;
	public static String LoginDialog_btnInicioPorDefecto_text;
	public static String LoginDialog_btnRolAdministrador_text;
	public static String LoginDialog_other_Text;
	public static String LoginDialog_other_Text_1;
	public static String LoginDialog_createAdmin_warning;
	
	public static String Generic_invalid_input;
	public static String GenericDialog_length_validation;
	public static String GenericDialog_email_validation;
	public static String GenericDialog_pass_validation;

	private DialogMessages(){
	}
}
