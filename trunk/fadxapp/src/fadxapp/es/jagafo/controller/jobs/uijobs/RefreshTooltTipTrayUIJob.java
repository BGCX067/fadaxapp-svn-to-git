package fadxapp.es.jagafo.controller.jobs.uijobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import fadxapp.es.jagafo.controller.jobs.GeneralJob;
import fadxapp.es.jagafo.controller.jobs.appjobs.GeneralAppJob;
import fadxapp.es.jagafo.utils.RCPUtils;

/**
 * Job encargado de mostrar imagenes en el tray utiliza una scheluding rule
 * para acceder al valor
 * @author jagafo
 */
public class RefreshTooltTipTrayUIJob extends GeneralJob {
	public static final String FAMILY = "REFRESH_TOOLTIP_FAMILY";
	private static final Long REFRESH_TIME = 7000L;
	
	public RefreshTooltTipTrayUIJob(){
		super(RefreshTooltTipTrayUIJob.class.getSimpleName());
		setUser(false);
		setSystem(true);
		setFamily(FAMILY);
		setPriority(DECORATE);
	}

	@Override
	public IStatus especificJobRun(IProgressMonitor monitor) {
		// Se lanza el refresh mientras haya jobs de app lanzados
		while (!monitor.isCanceled() && Job.getJobManager().find(GeneralAppJob.FAMILY).length!=0){
			RCPUtils.refreshToolTipTrayIcon();
			if (!monitor.isCanceled()){
				long i = 0;
				while (i < REFRESH_TIME && !monitor.isCanceled()){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i++;
				}
			}else
				return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}
}

