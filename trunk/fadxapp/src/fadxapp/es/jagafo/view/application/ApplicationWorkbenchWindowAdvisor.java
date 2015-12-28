package fadxapp.es.jagafo.view.application;

import java.net.UnknownHostException;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.State;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;

import fadxapp.es.jagafo.controller.commands.appconfiguration.WindowTrayControlCommand;
import fadxapp.es.jagafo.controller.jobs.appjobs.GeneralAppJob;
import fadxapp.es.jagafo.controller.jobs.uijobs.AnimateImageTrayUIJob;
import fadxapp.es.jagafo.controller.jobs.uijobs.RefreshTooltTipTrayUIJob;
import fadxapp.es.jagafo.model.appproperty.IPropertiesKeys;
import fadxapp.es.jagafo.nls.InterfaceMessages;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.apppropertiesservice.IAppPropertyService;
import fadxapp.es.jagafo.utils.RCPUtils;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private IWorkbenchWindow window;
	private TrayItem trayItem;
	private Image trayImage;
	private Image trayImageBusy;
	private IJobChangeListener jobChangeListener;

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
        RCPUtils.applicationWWARegister(this);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(700, 550));
        configurer.setTitle(InterfaceMessages.application_title);
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
    }
    
    public void postWindowOpen() {
		super.postWindowOpen();
		if (Boolean.parseBoolean(((IAppPropertyService) ServiceLocator
				.getService(IAppPropertyService.class))
				.loadPreference(IPropertiesKeys.FIRST_LAUNCH_KEY,
						"true"))){
			RCPUtils.scheludeBalloonToolTipTaryIcon(InterfaceMessages.application_baloon_tooltip_title,
					InterfaceMessages.application_baloon_tooltip_message,
					SWT.ICON_INFORMATION);
		}
		// Se introduce en el postWindowOpen para que se muestre el job ejecutandose
		addIconTray(getWindowConfigurer().getWindow());
		Job checkLocationChange = new Job("checkLocationchange"){
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				String localNameSystem = ((IAppPropertyService)ServiceLocator.getService(IAppPropertyService.class))
						.loadPreference(IPropertiesKeys.TEXT_COMPUTERNAME_KEY,"");
				String currentName;
				try {
					currentName = java.net.InetAddress.getLocalHost().getHostName();
				} catch (UnknownHostException e1) {
					currentName = System.getProperty("user.name");
				}
				if (!localNameSystem.equalsIgnoreCase(currentName)){
					RCPUtils.scheludeBalloonToolTipTaryIcon(InterfaceMessages.application_baloon_tooltip_title,
							InterfaceMessages.application_baloon_tooltip_message,
							SWT.ICON_INFORMATION);
				}
				return Status.OK_STATUS;
			}
		};
		checkLocationChange.setSystem(true);
		checkLocationChange.schedule(1500);
	}
    
    public void dispose() {
		if (trayImage != null) {
			trayImage.dispose();
		}
		if (trayImageBusy != null){
			trayImageBusy.dispose();
		}
		if (trayItem != null) {
			trayItem.dispose();
		}
	}

	public void setToolTipTrayIcon(String toolTip) {
		if (trayItem != null && !trayItem.isDisposed() && trayItem.getImage()!=null && !trayItem.getImage().isDisposed()) {
			if (toolTip == null || toolTip.trim().length() == 0)
				trayItem.setToolTipText(InterfaceMessages.application_title);
			else
				trayItem.setToolTipText(toolTip.trim());
		}
	}
	
	public void setImageTrayIcon(Image image) {
		if (trayItem != null && !trayItem.isDisposed() && !image.isDisposed())
			trayItem.setImage(image);
	}
	
	/**
	 * Muestra un mensaje de aviso en el toolTip de la aplicacion
	 * @param title Titulo del toolTip, si null se coge un titulo segun el parametro <b>style</b>
	 * @param toolTipText Mensaje a mostrar
	 * @param style Icono con el que se mostrará el mensaje, soporta SWT.ICON_INFORMATION, SWT.ICON_WARNING, SWT.ICON_ERROR
	 * @see RCPUtils#showBalloonToolTipTaryIcon(String, int)
	 */
	public void showBalloonToolTipOnTray(String title,String toolTipText,int style){
		if (trayItem != null){
			if (trayItem.getToolTip()!=null)
				trayItem.getToolTip().dispose();
			if (style!= SWT.ICON_INFORMATION && style != SWT.ICON_WARNING && style != SWT.ICON_ERROR)
				style = SWT.ICON_INFORMATION;
			ToolTip tip = new ToolTip(getWindowConfigurer().getWindow().getShell(), SWT.BALLOON | style);
			if (title==null || title.trim().length()==0){
				switch (style) {
					case SWT.ICON_WARNING: tip.setText("Advertencia");break;
					case SWT.ICON_ERROR: tip.setText("Error");break;
					default:tip.setText("Información");	break;
				}
			}else{
				tip.setText(title);
			}
			tip.setAutoHide(false);
			tip.setMessage(toolTipText);
			tip.setLocation(trayItem.getDisplay().getBounds().x,trayItem.getDisplay().getBounds().y);
			trayItem.setToolTip(tip);
			tip.setVisible(true);
		}
	}

	/**
	 * Añade a la aplicacion un icono en la barra de tareas
	 * 
	 * @param window
	 */
	private void addIconTray(IWorkbenchWindow window) {
		this.window = window;
		trayItem = initTrayItem(window);
		if (trayItem != null) {
			minimizeBehavior();
			hookPopupMenu();
			addJobManagerListener();
		}
	}
	
	private TrayItem initTrayItem(IWorkbenchWindow window) {
		final Tray tray = window.getShell().getDisplay().getSystemTray();
		trayItem = new TrayItem(tray, SWT.NONE);
		trayImage = ((IAccessResourcesService) ServiceLocator
				.getService(IAccessResourcesService.class))
				.getImageFromRegistry("icons/builder_app.gif");
		trayImageBusy = ((IAccessResourcesService) ServiceLocator
				.getService(IAccessResourcesService.class))
				.getImageFromRegistry("icons/builder_app_busy.gif");
		trayItem.setImage(trayImage);
		trayItem.setToolTipText(InterfaceMessages.application_title);
		return trayItem;
	}

	private void minimizeBehavior() {
		window.getShell().addShellListener(new ShellAdapter() {

			public void shellIconified(ShellEvent e) {
				ICommandService service = (ICommandService) PlatformUI
						.getWorkbench().getService(ICommandService.class);
				Command command = service
						.getCommand(WindowTrayControlCommand.ID);
				State state = command
						.getState("org.eclipse.ui.commands.toggleState");
				window.getShell().setVisible(
						!Boolean.parseBoolean(state.getValue().toString()));
			}
		});
		trayItem.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event event) {
				Shell shell = window.getShell();
				if (!shell.isVisible()) {
					window.getShell().setMinimized(false);
					shell.setVisible(true);
				}
				shell.forceFocus();
			}
		});
	}

	// We hook up on menu entry which allows to close the application
	private void hookPopupMenu() {
		trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				final State state = RCPUtils.getToogleStateCommand(WindowTrayControlCommand.ID);

				Menu menu = new Menu(window.getShell(), SWT.POP_UP);
				if (window.getShell().getMinimized()) {
					MenuItem minimize = new MenuItem(menu, SWT.NONE);
					minimize.setText("Restaurar");
					minimize.addListener(SWT.Selection, new Listener() {

						public void handleEvent(Event event) {
							window.getShell().setMinimized(false);
							if (!window.getShell().isVisible())
								window.getShell().setVisible(true);
						}
					});
				} else if (!window.getShell().getMinimized()) {
					MenuItem minimize = new MenuItem(menu, SWT.NONE);
					minimize.setText("Minimizar");
					minimize.addListener(SWT.Selection, new Listener() {

						public void handleEvent(Event event) {
							window.getShell().setMinimized(true);
							if (Boolean.parseBoolean(state.getValue()
									.toString()))
								window.getShell().setVisible(false);
						}
					});
				}
				MenuItem minimize = new MenuItem(menu, SWT.CHECK);
				minimize.setSelection(Boolean.parseBoolean(state.getValue()
						.toString()));
				minimize.setText("Ocultar al minimizar");
				minimize.addListener(SWT.Selection, new Listener() {

					public void handleEvent(Event event) {
						IHandlerService handlerService = (IHandlerService) window
								.getService(IHandlerService.class);
						try {
							handlerService.executeCommand(
									WindowTrayControlCommand.ID, null);
						} catch (Exception ex) {
							throw new RuntimeException(
									WindowTrayControlCommand.ID);
						}
					}
				});

				new MenuItem(menu, SWT.SEPARATOR);
				
				MenuItem about = new MenuItem(menu,SWT.NONE);
				about.setText("Acerca del autor...");
				about.addListener(SWT.Selection, new Listener(){
					public void handleEvent(Event event) {
						// Lets call our command
						IHandlerService handlerService = (IHandlerService) window
								.getService(IHandlerService.class);
						try {
							handlerService.executeCommand(
									"org.eclipse.ui.help.aboutAction"
									, null);
						} catch (Exception ex) {
							throw new RuntimeException(
									"org.eclipse.ui.help.aboutAction");
						}
					}
				});
				// Creates a new menu item that terminates the program when
				// selected
				MenuItem exit = new MenuItem(menu, SWT.NONE);
				exit.setText("Cerrar");
				exit.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						// Lets call our command
						IHandlerService handlerService = (IHandlerService) window
								.getService(IHandlerService.class);
						try {
							handlerService.executeCommand(
									"org.eclipse.ui.file.exit", null);
						} catch (Exception ex) {
							throw new RuntimeException(
									"org.eclipse.ui.file.exit");
						}
					}
				});
				menu.setVisible(true);
			}
		});
	}
	
	private void addJobManagerListener() {
		jobChangeListener = new IJobChangeListener() {
			
			public void sleeping(IJobChangeEvent event) {
			}
			
			public void scheduled(IJobChangeEvent event) {
			}
			
			public void running(IJobChangeEvent event) {
				if (event.getJob().belongsTo(GeneralAppJob.FAMILY))
					changeToolTip();
			}
			
			public void done(IJobChangeEvent event) {
				if (event.getJob().belongsTo(GeneralAppJob.FAMILY))
					changeToolTip();
			}
			
			public void awake(IJobChangeEvent event) {
			}
			
			public void aboutToRun(IJobChangeEvent event) {
			}
		};
		Job.getJobManager().addJobChangeListener(jobChangeListener);
		RCPUtils.refreshToolTipTrayIcon();
	}
	
	public void removeJobManagerListener(){
		Job.getJobManager().removeJobChangeListener(jobChangeListener);
	}
	
	public void changeToolTip(){
		Job[] appJobs = Job.getJobManager().find(GeneralAppJob.FAMILY);
		if (appJobs.length!=0){
			String jobsToolTip = InterfaceMessages.application_title+".Ejecutándose:";
			for (int i=0; i < appJobs.length && i < 3; i++){
				jobsToolTip += "\n"+appJobs[i].getName();
			}
			if (appJobs.length>3){
				jobsToolTip += "\n"+"...y "+(appJobs.length-3)+" operaciones más...";
			}
			final String finalToolTip = jobsToolTip;
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					trayItem.setToolTipText(finalToolTip);
					if (trayImageBusy!= null && trayItem.getImage().equals(trayImage)){
						if (Job.getJobManager().find(AnimateImageTrayUIJob.FAMILY).length==0)
							new AnimateImageTrayUIJob("icons/builder_app_busy2.gif").schedule();
					}
				}
			});
			if (Job.getJobManager().find(RefreshTooltTipTrayUIJob.FAMILY).length==0)
				new RefreshTooltTipTrayUIJob().schedule();
		}else{
			if (Job.getJobManager().find(RefreshTooltTipTrayUIJob.FAMILY).length!=0)
				Job.getJobManager().find(RefreshTooltTipTrayUIJob.FAMILY)[0].cancel();
			if (Job.getJobManager().find(AnimateImageTrayUIJob.FAMILY).length!=0){
				Job.getJobManager().find(AnimateImageTrayUIJob.FAMILY)[0].addJobChangeListener(new IJobChangeListener() {
					public void sleeping(IJobChangeEvent event) {
					}
					
					public void scheduled(IJobChangeEvent event) {
					}
					
					public void running(IJobChangeEvent event) {
					}
					
					public void done(IJobChangeEvent event) {
						Display.getDefault().asyncExec(new Runnable() {
							public void run() {
								trayItem.setToolTipText(InterfaceMessages.application_title);
								if (trayImage!= null && !trayItem.getImage().equals(trayImage))
									trayItem.setImage(trayImage);
							}
						});
					}
					
					public void awake(IJobChangeEvent event) {
					}
					
					public void aboutToRun(IJobChangeEvent event) {
					}
				});
				Job.getJobManager().find(AnimateImageTrayUIJob.FAMILY)[0].cancel();
			}
		}
	}
}
