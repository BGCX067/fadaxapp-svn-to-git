package fadxapp.es.jagafo.controller.jobs.uijobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

import fadxapp.es.jagafo.utils.RCPUtils;

/**
 * Job encargado de limpiar automaticamente el mensaje de la barra de estado
 * @author jagafo
 */
public class CleanStatusLineJob extends UIJob implements IJobChangeListener {
	private static final Long CLEAN_STATUS_LINE_DELAY = 7000L;
	private static final Long CLEAN_STATUS_LINE_ERROR_DELAY = CLEAN_STATUS_LINE_DELAY*2L;
	private boolean isError = false;

	public CleanStatusLineJob(boolean isError) {
		super("CleanJob ");
		this.isError = isError;
		setUser(false);
		setSystem(true);
	}

	public IStatus runInUIThread(IProgressMonitor monitor) {
		Runnable results = new Runnable() {
			public void run() {
				if (isError)
					RCPUtils.getStatusLineManager().setErrorMessage("");
				else
					RCPUtils.getStatusLineManager().setMessage("");
			}
		};
		Display.getDefault().asyncExec(results);
		return Status.OK_STATUS;
	}

	public void aboutToRun(IJobChangeEvent event) {
	}

	public void awake(IJobChangeEvent event) {
	}

	public void done(IJobChangeEvent event) {
		if (isError)
			schedule(CLEAN_STATUS_LINE_ERROR_DELAY);
		else
			schedule(CLEAN_STATUS_LINE_DELAY);
	}

	public void running(IJobChangeEvent event) {
	}

	public void scheduled(IJobChangeEvent event) {
	}

	public void sleeping(IJobChangeEvent event) {
	}
}
