package fadxapp.es.jagafo.view.validation;

import org.eclipse.swt.widgets.Text;

/**
 * Define la estructura de una valor a validar
 * @author jagafo
 *
 */
public abstract class ValueValidation implements IValueValidation {
	protected static final String EMPTY = "";
	protected String errorMessage;
	
	public ValueValidation(String errorMessage){
		this.errorMessage = errorMessage;
	}
	
	public String getStringValue(Object value){
		if (value instanceof Text){
			return ((Text)value).getText();
		}
		else if (value instanceof String){
			return value.toString();
		}
		return EMPTY;
	}
}
