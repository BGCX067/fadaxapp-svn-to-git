package fadxapp.es.jagafo.controller.jobs;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;

/**
 * Interfaz que implementara cualquier listener que necesite actuar cuando un job
 * termine correctamente. Debe implementar <b>doneOKevent()</b> que se ejecutará
 * al terminar el job ok.
 * @author jagafo
 */
public abstract class JobOKDoneChangeListener implements IJobChangeListener {
	GeneralJob job = null;
	
	public GeneralJob getGeneralJob(){
		return job;
	}

	public void aboutToRun(IJobChangeEvent event) {
	}

	public void awake(IJobChangeEvent event) {
	}

	public void done(IJobChangeEvent event) {
		if (event.getResult().equals(Status.OK_STATUS)){
			if (event.getJob() instanceof GeneralJob)
				job = (GeneralJob)event.getJob();
			doneOKevent();
		}
	}

	public void running(IJobChangeEvent event) {
	}

	public void scheduled(IJobChangeEvent event) {
	}

	public void sleeping(IJobChangeEvent event) {
	}
	
	/**
	 * Codigo que se ejecutara cuando el job termine y dé un resultado OK
	 */
	public abstract void doneOKevent();
}
