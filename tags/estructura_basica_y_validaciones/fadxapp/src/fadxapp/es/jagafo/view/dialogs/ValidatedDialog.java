package fadxapp.es.jagafo.view.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fadxapp.es.jagafo.view.validation.MyControlDecoration;

/**
 * Define un dialogo que implementa la logica de validacion cuando se termina de rellenar un dialogo
 * 
 * @author jagafo
 */
public abstract class ValidatedDialog extends Dialog {
	protected List<MyControlDecoration> validationList = new ArrayList<MyControlDecoration>(0);

	protected ValidatedDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected void addValidation(Control control,
			final MyControlDecoration validation) {
		validationList.add(validation);
		control.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!validation.check()){
					validation.show();
				}
				else{
					validation.hide();
				}
				checkOkButton();
			}
		});
		if (control instanceof Text){
			((Text)control).addModifyListener(new ModifyListener() {	
				public void modifyText(ModifyEvent e) {
					if (!validation.check()){
						validation.show();
					}
					else{
						validation.hide();
					}
					checkOkButton();	
				}
			});
		}
	}
	
	/**
	 * 
	 * @return Debe retornar el boton ok del dialogo
	 */
	protected abstract Button getOkButton();

	/**
	 * Comprueba el valor del boton ok del dialogo
	 */
	protected void checkOkButton() {
		Boolean enabled = Boolean.TRUE;
		for (MyControlDecoration validation: validationList){
			if (!validation.check()){
				validation.show();
				enabled = Boolean.FALSE;
				break;
			}
		}
		getOkButton().setEnabled(enabled);
	}
}
