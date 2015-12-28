package fadxapp.es.jagafo.view.validation.length;

import org.eclipse.swt.widgets.Control;

import fadxapp.es.jagafo.nls.DialogMessages;
import fadxapp.es.jagafo.view.validation.MyControlDecoration;


/**
 * Se encarga de validar un valor a partir de una expresion regular
 * @author jagafo
 */
public class LengthValueValidation extends MyControlDecoration {
	private Integer minValue;
	private Integer maxValue;
	
	public LengthValueValidation(Control control, int minValue, int maxValue) {
		super(control);
		this.minValue = minValue;
		this.maxValue = maxValue;
		setDescriptionText(DialogMessages.bind(DialogMessages.GenericDialog_length_validation, minValue,maxValue));
	}
	
	@Override
	public boolean check() {
		return getControlValue().trim().length() >= minValue && getControlValue().trim().length() <= maxValue;
	}
}
