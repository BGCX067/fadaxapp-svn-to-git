package fadxapp.es.jagafo.view.validation.regex;

/**
 * Validacion de una pass que contenga un caracter en mayuscula, otro en minuscula y un numero
 * @author jagafo
 *
 */
public class ValuePasswordValidation extends ValueRegexValidation {

	public ValuePasswordValidation(String errorMessage) {
		super(errorMessage);
	}

	@Override
	protected String getRegEx() {
		return "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]))";
	}

}
