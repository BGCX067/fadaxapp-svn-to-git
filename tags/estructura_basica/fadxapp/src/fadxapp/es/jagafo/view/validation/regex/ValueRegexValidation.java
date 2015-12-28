package fadxapp.es.jagafo.view.validation.regex;

import java.util.regex.Pattern;

import fadxapp.es.jagafo.view.validation.ValueValidation;

/**
 * Se encarga de validar un valor a partir de una expresion regular
 * @author jagafo
 *
 */
public abstract class ValueRegexValidation extends ValueValidation {
	
	public ValueRegexValidation(String errorMessage){
		super(errorMessage);
	}
	
	@Override
	public String validation(Object value) {
		Pattern  pattern = Pattern.compile(getRegEx());
		if (pattern.matcher(getStringValue(value)).matches()){
			return errorMessage;
		}
		return EMPTY; 
	}
	
	/**
	 * @return Devuelve la mascara con la que se comproborá el valor
	 */
	protected abstract String getRegEx();

}
