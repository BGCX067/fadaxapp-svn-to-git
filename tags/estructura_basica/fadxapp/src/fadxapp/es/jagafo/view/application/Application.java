package fadxapp.es.jagafo.view.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.finishapplicationservice.IFinishApplicationService;
import fadxapp.es.jagafo.services.startapplicationservice.IStartApplicationService;
import fadxapp.es.jagafo.view.dialogs.user.login.LoginDialog;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) {
		Display display = PlatformUI.createDisplay();
		int returnCode =  IApplication.EXIT_OK;
		try {
			if (((IStartApplicationService) ServiceLocator
					.getService(IStartApplicationService.class))
					.start()) {
				if (authenticate(display)){
					returnCode = PlatformUI.createAndRunWorkbench(display,
							new ApplicationWorkbenchAdvisor());
				}
				else{
//					No se ha logueado el usuario
					returnCode = PlatformUI.RETURN_UNSTARTABLE;
				}
			} else{
//				Ya habia corriendo otra instancia de la aplicacion
				returnCode = PlatformUI.RETURN_EMERGENCY_CLOSE;
			}
		} finally {
			((IFinishApplicationService) ServiceLocator
					.getService(IFinishApplicationService.class))
					.finish(returnCode);
			display.dispose();
		}
		return returnCode;
	}

	private boolean authenticate(Display display) {
		LoginDialog loginDialog = new LoginDialog(display.getActiveShell());
		int response = loginDialog.open();{
			if (response == IDialogConstants.OK_ID){
//				TODO: Pendiente control de los datos de usuario
				return true;				
			}else{
				return false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
