package fadxapp.es.jagafo.view.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Define un dialogo que implementa la logica de validacion cuando se termina de rellenar un dialogo
 * 
 * @author jagafo
 */
public abstract class ValidatedDialog extends Dialog {

	protected ValidatedDialog(Shell parentShell) {
		super(parentShell);
	}
	
	/**
	 * 	
	 * @return True si se ha validado corretamente el dialogo, false en otro caso
	 */
	protected boolean validate(){
		Boolean isValid = Boolean.TRUE;
		
		
		return isValid;
	}
}
