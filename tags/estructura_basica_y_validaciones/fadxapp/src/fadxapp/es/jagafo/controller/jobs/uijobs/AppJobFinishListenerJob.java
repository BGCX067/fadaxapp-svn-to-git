package fadxapp.es.jagafo.controller.jobs.uijobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;

import fadxapp.es.jagafo.controller.jobs.GeneralJob;
import fadxapp.es.jagafo.utils.RCPUtils;

/**
 * Job encargado de escuchar cuando termina un job de la aplicacion y escribir,
 * si es necesario, un mensaje en la barra de estado
 * @author jagafo
 */
public class AppJobFinishListenerJob extends GeneralJob implements 
		IJobChangeListener {
	private boolean isError;
	private String msg;

	public AppJobFinishListenerJob() {
		super("AppJobFinishListenerJob ");
		setUser(false);
		setSystem(true);
	}

	public IStatus runInUIThread(IProgressMonitor monitor) {
		RCPUtils.showMsgStatusLine(msg, isError);
		return Status.OK_STATUS;
	}
	
	@Override
	public IStatus especificJobRun(IProgressMonitor monitor) {
		RCPUtils.showMsgStatusLine(msg, isError);
		return Status.OK_STATUS;
	}

	public void aboutToRun(IJobChangeEvent event) {
	}

	public void awake(IJobChangeEvent event) {
	}

	public void done(IJobChangeEvent event) {
		if (event.getJob() instanceof GeneralJob) {
			isError = !((GeneralJob) event.getJob()).isFinishedOK();
			msg = ((GeneralJob) event.getJob()).getFinishMessage();
			if (msg.length() != 0) {
				addJobChangeListener(new CleanStatusLineJob(isError));
				schedule();
			}
		}
	}

	public void running(IJobChangeEvent event) {
	}

	public void scheduled(IJobChangeEvent event) {
	}

	public void sleeping(IJobChangeEvent event) {
	}
}
