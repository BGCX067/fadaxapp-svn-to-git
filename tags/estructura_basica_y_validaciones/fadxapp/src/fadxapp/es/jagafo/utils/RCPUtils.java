package fadxapp.es.jagafo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.RadioState;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.services.IEvaluationService;

import fadxapp.es.jagafo.controller.jobs.uijobs.CleanStatusLineJob;
import fadxapp.es.jagafo.controller.jobs.uijobs.ShowBalloonToolTipUIJob;
import fadxapp.es.jagafo.model.appproperty.IPropertiesKeys;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.apppropertiesservice.IAppPropertyService;
import fadxapp.es.jagafo.services.logservice.ILogService;
import fadxapp.es.jagafo.view.application.ApplicationWorkbenchWindowAdvisor;
import fadxapp.es.jagafo.view.editors.GenericEditorInput;

/**
 * Esta clase aglutina una serie de metodos estaticos que permite realizar
 * algunas acciones con la interfaz RCP y con servicios globales
 * 
 */
public class RCPUtils {
	/**
	 * Ruta hacia el workspace donde se esta ejecutando la aplicacion
	 */
	public static String USER_DIR;
	/**
	 * Contiene la ruta completa del directorio donde se guardaran todos los
	 * ficheros de configuracion de la aplicacion
	 */
	public static String CONFIG_DIR;

	static {
		// Path del workspace
		if (System.getProperty("os.name").contains("Linux")) {
			USER_DIR = System.getProperty("user.dir") + "/workspace/";
			CONFIG_DIR = System.getProperty("user.dir") + "/workspace/config/";
		} else {
			USER_DIR = System.getProperty("user.dir") + "\\workspace\\";
			CONFIG_DIR = System.getProperty("user.dir")
					+ "\\workspace\\config\\";
		}
	}
	private static boolean debugCalculated = false;
	private static boolean isDebugMode = false;

	private static IStatusLineManager defaultStatusLineManager = null;
	private static ApplicationWorkbenchWindowAdvisor appWorkbench = null;
	
	/**
	 * Indica si estamos trabajando sobre el entorno de trabajo configurado
	 * 
	 * @return
	 */
	public static boolean isDebugMode() {
		if (!debugCalculated) {
			debugCalculated = true;
			isDebugMode = !(new File(((IAppPropertyService) ServiceLocator
							.getService(IAppPropertyService.class))
							.loadPreference(
									IPropertiesKeys.FOLDER_DEVELOP_DRIVE_KEY,
									IPropertiesKeys.FOLDER_DEVELOP_DRIVE)).exists());
		}
		return isDebugMode;
	}

	/**
	 * Establece el manager por defecto para la aplicacion
	 * 
	 * @param statusLineManager
	 */
	public static void registerDefaultStatusLineManager(
			IStatusLineManager statusLineManager) {
		defaultStatusLineManager = statusLineManager;
	}

	/**
	 * Devuelve el manager de la linea de estatus de la aplicacion si encuentra
	 * el site apropiado, en otro caso devuelve null
	 * 
	 * @return IStatusLineManager si está disponible, <b>null</b> en otro caso
	 */
	public static IStatusLineManager getStatusLineManager() {
		try {
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
					&& PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage() != null
					&& PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().getActivePart() != null) {
				if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActivePart().getSite() instanceof IViewSite)
					return ((IViewSite) PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getActivePart().getSite()).getActionBars()
							.getStatusLineManager();
				else if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().getActivePart().getSite() instanceof IEditorSite)
					return ((IEditorSite) PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getActivePart().getSite()).getActionBars()
							.getStatusLineManager();
			} else if (defaultStatusLineManager != null)
				return defaultStatusLineManager;
		} catch (IllegalStateException e) {
			return null;
		}
		return null;
	}

	/**
	 * Escribe un mensaje en la barra de estado de la aplicacion rcp
	 * 
	 * @param line
	 *            Mensaje a escribir
	 * @param isError
	 *            Si es true se muestra el mensaje como error, si es false se
	 *            muestra el texto normal
	 */
	public static void showMsgStatusLine(final String line,
			final boolean isError) {
		if (getStatusLineManager() != null) {
			UIJob job = new UIJob("statusLineJob"
					+ Calendar.getInstance().getTimeInMillis()) {
				public IStatus runInUIThread(IProgressMonitor monitor) {
					if (PlatformUI.getWorkbench()!=null && PlatformUI.getWorkbench().isClosing() && 
							!PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().isDisposed() &&
							!PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().isVisible() ||
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getMinimized()){
						if (isError)
							scheludeBalloonToolTipTaryIcon(null,line,SWT.ICON_ERROR);
						else
							scheludeBalloonToolTipTaryIcon(null,line,SWT.ICON_INFORMATION);
					}
					if (isError)
						getStatusLineManager().setErrorMessage(line);
					else
						getStatusLineManager().setMessage(line);
					return Status.OK_STATUS;
				}
			};
			job.schedule();
			job.addJobChangeListener(new CleanStatusLineJob(isError));
		}
	}
	
	/**
	 * No usar esta funcion usar la funcion scheluded
	 * @see #scheludeBalloonToolTipTaryIcon(String, String, int)
	 */
	public static void showBalloonToolTipTaryIcon(final String title,final String toolTip,final int style){
		if (appWorkbench != null) {
			UIJob displayBalloonJob = new UIJob("DisplayBalloonToolTip") {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					appWorkbench.showBalloonToolTipOnTray(title,toolTip,style);
					return Status.OK_STATUS;
				}
			};
			displayBalloonJob.setUser(false);
			displayBalloonJob.setSystem(true);
			displayBalloonJob.schedule();
		}
	}
	
	/**
	 * @see ApplicationWorkbenchWindowAdvisor#showBalloonToolTipOnTray(String, int)
	 */
	public static void scheludeBalloonToolTipTaryIcon(final String title,final String toolTip,final int style){
		ShowBalloonToolTipUIJob job = new ShowBalloonToolTipUIJob(title, toolTip, style);
		job.schedule();
	}


	/**
	 * Devuelve la vista cuya id paramos como parametro
	 * 
	 * @param viewId
	 *            Id de la vista
	 * @return IViewPart si la encuentra, null en otro caso
	 */
	public static IViewPart getView(String viewId) {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage() != null)
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView(viewId);
		return null;
	}
	
	public static IViewPart openView(String viewId){
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()!= null &&
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage() != null){
			try {
				return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(viewId);
			} catch (PartInitException e) {
				((ILogService) ServiceLocator
						.getService(ILogService.class)).error(
						RCPUtils.class.getSimpleName(),
						"No se ha podido abrir la vista " + viewId, e);
			}
		}
		return null;
	}

	/**
	 * Devuelve el editor cuya id pasamos como parametro
	 * 
	 * @param editorID
	 *            ID del editor que queremos recuperar
	 * @param restore
	 *            Si True intenta restauralo, si False lo cierra
	 * @return Editor asociado a la id, null si no existe
	 */
	public static IEditorPart getEditor(String editorID, boolean restore) {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findEditors(null, editorID,
								IWorkbenchPage.MATCH_ID).length != 0)
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findEditors(null, editorID,
							IWorkbenchPage.MATCH_ID)[0].getEditor(restore);
		return null;
	}

	/**
	 * Devuelve el editor activo en la aplicacion rcp
	 * 
	 * @return Editor activo, null si no existe
	 */
	public static IEditorPart getActiveEditor() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
				&& PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage() != null)
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getActiveEditor();
		return null;
	}

	/**
	 * Abre el editor cuya id pasamos como parametro
	 * 
	 * @param idEditor
	 * @return
	 */
	public static IEditorPart openEditor(final String idEditor) {
		if (idEditor != null && idEditor.trim().length() != 0) {
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null
					&& PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage() != null) {
				try {
					return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().openEditor(
									new GenericEditorInput(idEditor, null),
									idEditor);
				} catch (PartInitException e) {
					((ILogService) ServiceLocator
							.getService(ILogService.class)).error(
							RCPUtils.class.getSimpleName(),
							"No se ha podido abrir el editor " + idEditor, e);
				}
			}
		}
		return null;
	}
	
	/**
	 * Abre un fichero en el editor que se le indica como parametro
	 * @param filePath
	 * @param isReadOnly
	 * @param editorId
	 */
	public static void showFileInEditor(final String filePath,final boolean isReadOnly,final String editorId){
		if (filePath != null) {
			UIJob job = new UIJob("ShowFileInEditor "+editorId) {

				public IStatus runInUIThread(IProgressMonitor monitor) {
					File fileToOpen = new File(filePath);
					if (fileToOpen.exists() && fileToOpen.isFile()) {
						IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						 
					    try {
					        IDE.openEditorOnFileStore( page, fileStore );
					    } catch ( PartInitException e ) {
					    	((ILogService) ServiceLocator
									.getService(ILogService.class))
									.error("RCPUtils",
											"Error al intentar abrir el fichero "
													+ filePath, e);
							showMsgStatusLine("Error al abrir el fichero log",
									true);
					    }
					} else {
						((ILogService) ServiceLocator
								.getService(ILogService.class)).error(
								"RCPUtils", "No se ha encontrado el fichero "
										+ filePath, new FileNotFoundException(
										filePath));
						showMsgStatusLine("No se ha encontrado el fichero "
								+ filePath, true);
					}
					return Status.OK_STATUS;
				}
			};
			job.schedule();
		} else {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).error("RCPUtils",
					"No se ha pasado correctamente el parametro de ruta",
					new FileNotFoundException(filePath));
		}
	}

	/**
	 * Abre en un editor de texto el fichero que pasamos como parametro
	 * 
	 * @param filePath
	 *            Ruta del fichero de texto a visualizar
	 * @param isReadOnly
	 *            Si es True se permite la modificacion del fichero, si es false
	 *            el fichero no es editable
	 */
	public static void showFileTextInEditor(String filePath,boolean isReadOnly) {
		showFileInEditor(filePath,isReadOnly,"org.eclipse.ui.DefaultTextEditor");
	}
	
	/**
	 * Abre en un editor de xml el fichero que pasamos como parametro
	 * 
	 * @param filePath
	 *            Ruta del fichero de XML a visualizar
	 * @param isReadOnly
	 *            Si es True se permite la modificacion del fichero, si es false
	 *            el fichero no es editable
	 */
	public static void showFileXMLInEditor(String filePath, boolean isReadOnly){
		showFileInEditor(filePath, isReadOnly, "org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart");
	}

	/**
	 * Registra el applicationWorckBenchWindowAdvisor, con el objetivo de hacer
	 * uso del TrayIcon
	 * 
	 * @param awwa
	 */
	public static void applicationWWARegister(
			ApplicationWorkbenchWindowAdvisor awwa) {
		appWorkbench = awwa;
	}

	/**
	 * Establece un toolTip en el icono de la aplicacion en la barra de tareas
	 * 
	 * @param toolTip
	 */
	public static void setToolTipTrayIcon(final String toolTip) {
		if (appWorkbench != null) {
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					appWorkbench.setToolTipTrayIcon(toolTip);
				}
			});
		}
	}
	
	/**
	 * Modifica el trayIcon de la aplicacion por la imagen que se pasa como parametro
	 * @param image
	 */
	public static void changeTrayIcon(final Image image) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (appWorkbench!=null && !image.isDisposed())
					appWorkbench.setImageTrayIcon(image);				
			}
		});
	}
	
	/**
	 * Provoca que se refresquen los comandos asociados al idTest que pasamos como parametro<br>
	 * p.e.: <code>es.hercules.repository.propertyTester.isBBDDok</code><br>
	 * si no tiene un "." se concatenará <code>es.hercules.repository.propertyTester</code>
	 * @param idtester
	 */
	public static void refreshTester(final String idTester){
		UIJob job = new UIJob(Display.getDefault(),"refreshTester") {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				IEvaluationService service = (IEvaluationService) PlatformUI
				.getWorkbench().getService(IEvaluationService.class);
				if (service != null)
					if (idTester.contains("."))
						service.requestEvaluation(idTester);
					else
						service.requestEvaluation("es.hercules.repository.propertyTester."+idTester);
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
	
	/**
	 * Devuelve el State asociado a una commando estilo check
	 * y que tiene un parametro org.eclipse.ui.commands.toggleState
	 * @param commandID
	 * @return
	 */
	public static State getToogleStateCommand(String commandID){
		ICommandService service = (ICommandService) PlatformUI
			.getWorkbench().getService(ICommandService.class);
		Command command = service
			.getCommand(commandID);
		return command.getState("org.eclipse.ui.commands.toggleState");
	}
	
	/**
	 * Devuelve el State asociado a una commando estilo radio
	 * y que tiene un parametro {@link RadioState}
	 * @param commandID
	 * @return
	 */
	public static State getRadioStateCommand(String commandID){
		ICommandService service = (ICommandService) PlatformUI
			.getWorkbench().getService(ICommandService.class);
		Command command = service
			.getCommand(commandID);
		return command.getState(RadioState.STATE_ID);
	}
	
	/**
	 * Provoca el refresco del toolTip de la aplicacion
	 */
	public static void refreshToolTipTrayIcon(){
		UIJob job = new UIJob("refreshToolTipTrayIcon") {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				if (appWorkbench!=null)
					appWorkbench.changeToolTip();
				return Status.OK_STATUS;
			}
		};
		job.setUser(false);
		job.setSystem(true);
		job.schedule();
	}
	
	/**
	 * Eliminar el lister de la aplicacion al jobManager
	 */
	public static void removeJobManagerListener(){
		if (appWorkbench!=null)
			appWorkbench.removeJobManagerListener();
	}
}