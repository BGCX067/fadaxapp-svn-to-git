package fadxapp.es.jagafo.controller.exceptions;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Excepcion critica de la aplicacion. Muestra un mensaje por pantalla y finaliza la aplicacion
 * @author jagafo
 */
public abstract class CriticalException extends BasicException {

	private static final long serialVersionUID = 83450677731151916L;

	protected CriticalException() {
	}
	
	public CriticalException(Object classDispatcher, String msg, Type type) {
		super(classDispatcher, msg, type);
		showMsg(msg);
	}

	public CriticalException(Object classDispatcher, String msg) {
		super(classDispatcher, msg);
		showMsg(msg);
	}

	public CriticalException(String msg) {
		super(msg);
		showMsg(msg);
	}

	public CriticalException(Throwable e) {
		super(e);
		showMsg(e.getMessage());
	}

	private void showMsg(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				MessageDialog.openError(new Shell(Display.getDefault()),
						"Error Critico", "Se cerrará la aplicacion:: "
								+ message);
				Runtime.getRuntime().exit(0);
			}
		});
	}
}
