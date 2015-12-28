package fadxapp.es.jagafo.view.validation.regex;

import org.eclipse.swt.widgets.Control;

import fadxapp.es.jagafo.nls.DialogMessages;

/**
 * Validacion de una pass que contenga un caracter en mayuscula, otro en minuscula y un numero
 * @author jagafo
 *
 */
public class PasswordValidation extends ValueRegexValidation {
	private Integer minLength;
	private Integer maxLength;

	public PasswordValidation(Control control,Integer minLength, Integer maxLength){
		super(control);
		setDescriptionText(DialogMessages.bind(DialogMessages.GenericDialog_pass_validation,minLength,maxLength));
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	@Override
	protected String getRegEx() {
		return "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{"+minLength+","+maxLength+"})";
	}

}
