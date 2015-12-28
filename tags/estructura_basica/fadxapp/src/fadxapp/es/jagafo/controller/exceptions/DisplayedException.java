package fadxapp.es.jagafo.controller.exceptions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

/**
 * Toda excepcion que ademas de loguearse deba mostrar un mensaje por pantalla
 * hereda de este tipo
 * @author
 */
public class DisplayedException extends BasicException {

	private static final long serialVersionUID = 1L;

	public DisplayedException() {
		super();
	}

	public DisplayedException(Object classDispatcher, String msg, Type type) {
		super(classDispatcher, msg, type);
		showMessageException(classDispatcher);
	}

	public DisplayedException(Object classDispatcher, String msg) {
		super(classDispatcher, msg);
		showMessageException(classDispatcher);
	}

	public DisplayedException(String msg) {
		super(msg);
		showMessageException(null);
	}

	public DisplayedException(Throwable e) {
		super(e);
		showMessageException(null);
	}
	
	public DisplayedException(Object classDispatcher,Throwable e) {
		super(classDispatcher,e);
		showMessageException(classDispatcher);
	}
	
	public DisplayedException(Object classDispatcher,String msg,Throwable e) {
		super(classDispatcher,msg,e);
		showMessageException(classDispatcher);
	}	
	protected void showMessageException(final Object classDispatcher){
		UIJob showMessageException = new UIJob("showMessage") {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				String dispatcher = "";
				if (classDispatcher!=null)
					dispatcher = classDispatcher.toString();
				MessageDialog.openError(Display.getCurrent().getActiveShell(),"E R R O R",
						"Error durante la ejecucion de la tarea "+dispatcher+". Revise el Log o avise al administrador de la aplicacion."
				);
				return Status.OK_STATUS;
			}
		};
		showMessageException.setSystem(true);
		showMessageException.schedule();
	}
}
