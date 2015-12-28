package fadxapp.es.jagafo.view.validation.regex;

import org.eclipse.swt.widgets.Control;

import fadxapp.es.jagafo.nls.DialogMessages;

/**
 * Validacion de un email
 * @author jagafo
 *
 */
public class EmailValidation extends ValueRegexValidation {

	public EmailValidation(Control control){
		super(control);
		setDescriptionText(DialogMessages.GenericDialog_email_validation);
	}

	@Override
	protected String getRegEx() {
		return "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	}

}
