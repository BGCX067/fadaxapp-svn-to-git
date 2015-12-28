package fadxapp.es.jagafo.controller.commands;

import java.sql.Connection;
import java.sql.Statement;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.bbddservice.DatabaseService;
import fadxapp.es.jagafo.services.bbddservice.IDatabaseService;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SQLLiteChecker extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public SQLLiteChecker() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Connection c = null;
		Statement stmt = null;
		try {
			c = ((IDatabaseService)(ServiceLocator.getService(DatabaseService.class))).createConnection();

			if (c != null){
				stmt = c.createStatement();
			}

			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			MessageDialog.openInformation(
					window.getShell(),
					"FadxApp",
					"Conexion satisfactoria!!!");
		} catch ( Exception e ) {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			MessageDialog.openInformation(
					window.getShell(),
					"FadxApp",
					"Conexion erronea!!!");
		}
		finally{
			try{
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return null;
	}
}
