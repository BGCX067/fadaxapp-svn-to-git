package fadxapp.es.jagafo.view.dialogs.user.login;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fadxapp.es.jagafo.model.appproperty.IPropertiesKeys;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.apppropertiesservice.IAppPropertyService;
import fadxapp.es.jagafo.services.encryptionservice.IEncryptionService;

/**
 * Contiene los atributos que definen una conexión con el API del diccionario
 */
public class ConnectionDetails {
	private static final IEncryptionService encryptionService = ((IEncryptionService) ServiceLocator
			.getService(IEncryptionService.class));

	private String login;
	private String password;

	public ConnectionDetails() {
		this.login = "";
		this.password = "";
	}

	public ConnectionDetails(String login, String password, String ip_server,
			String port_server, String dbname) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * Carga los datos de la pantalla de login del usuario introducidos en un
	 * anterior logueo.
	 */
	public void loadConnectionDetails() {
		// Carga los valores guardados en las preferencias. Si no lo encuentra,
		// devuelve el segundo parámetro = valor por defecto
		String userId = ((IAppPropertyService) ServiceLocator
				.getService(IAppPropertyService.class)).loadPreference(
						IPropertiesKeys.USER_DDBB_KEY, System.getProperty("user.name"));
		this.login = userId;
		if (((IAppPropertyService) ServiceLocator
				.getService(IAppPropertyService.class)).loadPreference(
						IPropertiesKeys.SAVE_DDBB_PASS_KEY, "false").equals("true")
						&& ((IAppPropertyService) ServiceLocator
								.getService(IAppPropertyService.class))
								.loadPreference(IPropertiesKeys.PASS_DDBB_KEY, "")
								.length() != 0) {
			this.password = ((IAppPropertyService) ServiceLocator
					.getService(IAppPropertyService.class))
					.loadPreference(IPropertiesKeys.PASS_DDBB_KEY, "");
			if (password.trim().length() != 0
					&& encryptionService.isInitialize()) {
				// SecurableObjectHolder holder = new SecurableObjectHolder();
				// password =
				// holder.getSecurableObject().decryptPassword(password);
				password = encryptionService.decrypt(password);
			}
		}
	}

	/**
	 * Salva los detalles de conexión de un usuario de la pantalla de login en
	 * disco. Esta información la guarda cifrada.
	 * 
	 */
	public void saveConnectionDetails() {
		((IAppPropertyService) ServiceLocator
				.getService(IAppPropertyService.class)).savePreference(
						IPropertiesKeys.USER_DDBB_KEY, login);
		if (((IAppPropertyService) ServiceLocator
				.getService(IAppPropertyService.class)).loadPreference(
						IPropertiesKeys.SAVE_DDBB_PASS_KEY, "false").equals("true")) {
			Job loadDetailsJob = new Job("SavePassWordJob") {

				protected IStatus run(IProgressMonitor monitor) {
					// SecurableObjectHolder holder = new
					// SecurableObjectHolder();
					// password =
					// holder.getSecurableObject().encryptPassword(password);
					if (encryptionService.isInitialize()) {
						password = encryptionService.encrypt(password);
						((IAppPropertyService) ServiceLocator
								.getService(IAppPropertyService.class))
								.savePreference(IPropertiesKeys.PASS_DDBB_KEY,
										password);
					}
					return Status.OK_STATUS;
				}
			};
			loadDetailsJob.setSystem(true);
			loadDetailsJob.setPriority(Job.SHORT);
			loadDetailsJob.schedule();
		}
	}

	/**
	 * Borra los detalles de conexión de un usuario en disco
	 */
	public void removeConnectionDetails() {
		((IAppPropertyService) ServiceLocator
				.getService(IAppPropertyService.class)).savePreference(
						IPropertiesKeys.USER_DDBB_KEY, "");
		((IAppPropertyService) ServiceLocator
				.getService(IAppPropertyService.class)).savePreference(
						IPropertiesKeys.PASS_DDBB_KEY, "");
	}
}
