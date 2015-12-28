package fadxapp.es.jagafo.view.validation.regex;

import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Control;

import fadxapp.es.jagafo.view.validation.MyControlDecoration;

/**
 * Se encarga de validar un valor a partir de una expresion regular
 * @author jagafo
 *
 */
public abstract class ValueRegexValidation extends MyControlDecoration {
	
	public ValueRegexValidation(Control control){
		super(control);
	}
	
	@Override
	public boolean check() {
		Pattern  pattern = Pattern.compile(getRegEx());
		return pattern.matcher(getControlValue()).matches();
	}
	
	/**
	 * @return Devuelve la mascara con la que se comproborá el valor
	 */
	protected abstract String getRegEx();

}
