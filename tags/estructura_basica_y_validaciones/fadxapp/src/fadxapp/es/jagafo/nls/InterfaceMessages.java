package fadxapp.es.jagafo.nls;

import org.eclipse.osgi.util.NLS;

public class InterfaceMessages extends NLS {
	private static final String BUNDLE_NAME = "interface"; //$NON-NLS-1$
	
	static {
	    // initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, InterfaceMessages.class);
	}
	
	public static String application_title;
	public static String application_baloon_tooltip_title;
		public static String application_baloon_tooltip_message;
	private InterfaceMessages(){
	}
}
