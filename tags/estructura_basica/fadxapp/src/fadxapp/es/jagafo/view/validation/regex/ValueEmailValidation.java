package fadxapp.es.jagafo.view.validation.regex;

/**
 * Validacion de un email
 * @author jagafo
 *
 */
public class ValueEmailValidation extends ValueRegexValidation {

	public ValueEmailValidation(String errorMessage) {
		super(errorMessage);
	}

	@Override
	protected String getRegEx() {
		return "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	}

}
