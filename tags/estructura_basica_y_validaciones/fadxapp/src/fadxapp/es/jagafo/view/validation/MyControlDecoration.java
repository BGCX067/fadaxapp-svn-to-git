package fadxapp.es.jagafo.view.validation;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Define la clase propia para el control de mensajes introducidos
 * @author jagafo
 *
 */
public abstract class MyControlDecoration extends ControlDecoration {
	private static final String EMPTY_STRING = "";

	public MyControlDecoration(Control control) {
		super(control, SWT.CENTER);
	}
	
	/**
	 * Devuelve el valor del control asociado al decorator
	 * TODO: Implementar todas las equivalencias
	 */
	protected String getControlValue(){
		if (!getControl().isDisposed()){
			if (getControl() instanceof Text){
				return ((Text)getControl()).getText();
			}
		}
		return EMPTY_STRING;
	}
	
	public void show(){
		setImage(FieldDecorationRegistry.getDefault()
                .getFieldDecoration(FieldDecorationRegistry.DEC_ERROR)
                .getImage());
		super.show();
	}
	
	/**
	 * Se encarga de comprobar la validacion, y mostrar si corresponde el error
	 */
	public abstract boolean check();
}
