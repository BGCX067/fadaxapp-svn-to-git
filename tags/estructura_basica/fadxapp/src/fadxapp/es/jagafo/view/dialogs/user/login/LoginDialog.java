package fadxapp.es.jagafo.view.dialogs.user.login;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import fadxapp.es.jagafo.model.appproperty.IPropertiesKeys;
import fadxapp.es.jagafo.nls.DialogMessages;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.apppropertiesservice.IAppPropertyService;

/**
 * Login dialog, which prompts for the user's account info, and has Login and
 * Cancel buttons.
 * @author jagafo
 */
public class LoginDialog extends Dialog {
	private Text txtTlogonid;
	private Text txtTemail;
	private Text txtPassword;
	private Boolean chkAdminRole;
	private Boolean chkDefault;
	private Boolean chkAdminRoleEnabled = Boolean.FALSE;
	
	protected Boolean firstLaunch;

	/**
	 * Create the dialog.
	 * @param parentShell
	 * @param firstLaunch 
	 */
	public LoginDialog(Shell parentShell) {
		super(parentShell);
		this.firstLaunch = Boolean.parseBoolean(((IAppPropertyService) ServiceLocator
				.getService(IAppPropertyService.class))
				.loadPreference(IPropertiesKeys.FIRST_LAUNCH_KEY,
						"true"));;
	}
	
	public Text getTxtTlogonid() {
		return txtTlogonid;
	}

	public void setTxtTlogonid(Text txtTlogonid) {
		this.txtTlogonid = txtTlogonid;
	}

	public Text getTxtPassword() {
		return txtPassword;
	}

	public void setTxtPassword(Text txtPassword) {
		this.txtPassword = txtPassword;
	}

	public Boolean getChkAdminRole() {
		return chkAdminRole;
	}

	public void setChkAdminRole(Boolean chkAdminRole) {
		this.chkAdminRole = chkAdminRole;
	}

	public Boolean getChkDefault() {
		return chkDefault;
	}

	public void setChkDefault(Boolean chkDefault) {
		this.chkDefault = chkDefault;
	}

	public Boolean getChkAdminRoleEnabled() {
		return chkAdminRoleEnabled;
	}

	public void setChkAdminRoleEnabled(Boolean chkAdminRoleEnabled) {
		this.chkAdminRoleEnabled = chkAdminRoleEnabled;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.VERTICAL));
		
		Group grpDatosUsuari = new Group(container, SWT.NONE);
		grpDatosUsuari.setText(DialogMessages.LoginDialog_grpDatosUsuari_text);
		grpDatosUsuari.setLayout(new GridLayout(2, false));
		
		Label lblLlogonid = new Label(grpDatosUsuari, SWT.NONE);
		lblLlogonid.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLlogonid.setText(DialogMessages.LoginDialog_lblLlogonid_text);
		
		txtTlogonid = new Text(grpDatosUsuari, SWT.BORDER);
		txtTlogonid.setText(DialogMessages.LoginDialog_txtTlogonid_text);
		txtTlogonid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLemail = new Label(grpDatosUsuari, SWT.NONE);
		lblLemail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblLemail.setText(DialogMessages.LoginDialog_lblLemail_text);
		
		txtTemail = new Text(grpDatosUsuari, SWT.BORDER);
		txtTemail.setText(DialogMessages.LoginDialog_txtTemail_text);
		txtTemail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblContrasea = new Label(grpDatosUsuari, SWT.NONE);
		lblContrasea.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblContrasea.setText(DialogMessages.LoginDialog_lblContrasea_text);
		
		txtPassword = new Text(grpDatosUsuari, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setToolTipText(DialogMessages.LoginDialog_txtPassword_toolTipText);
		txtPassword.setText(DialogMessages.LoginDialog_txtPassword_text);
		GridData gd_txtPassword = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtPassword.widthHint = 156;
		txtPassword.setLayoutData(gd_txtPassword);
		new Label(grpDatosUsuari, SWT.NONE);
		
		if (firstLaunch){
			CLabel lblNewLabel = new CLabel(grpDatosUsuari, SWT.NONE);
			lblNewLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
			lblNewLabel.setImage(((IAccessResourcesService)ServiceLocator.getService(IAccessResourcesService.class))
					.getImageFromRegistry("interfaz/warning.gif"));
			lblNewLabel.setText(DialogMessages.LoginDialog_createAdmin_warning);
		}
		
		Button btnInicioPorDefecto = new Button(grpDatosUsuari, SWT.CHECK);
		btnInicioPorDefecto.setText(DialogMessages.LoginDialog_btnInicioPorDefecto_text);
		new Label(grpDatosUsuari, SWT.NONE);
		
		Button btnRolAdministrador = new Button(grpDatosUsuari, SWT.CHECK);
		btnRolAdministrador.setText(DialogMessages.LoginDialog_btnRolAdministrador_text);
		btnRolAdministrador.setEnabled(chkAdminRoleEnabled);
		if (firstLaunch){
			btnRolAdministrador.setSelection(Boolean.TRUE);
		}

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, DialogMessages.LoginDialog_other_Text,
				true);
		Button okButton = createButton(parent, IDialogConstants.OK_ID,
				DialogMessages.LoginDialog_other_Text_1, false);
		
		okButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				TODO: Validar los campos introducidos
			}
		});
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(341, 258);
	}
}
