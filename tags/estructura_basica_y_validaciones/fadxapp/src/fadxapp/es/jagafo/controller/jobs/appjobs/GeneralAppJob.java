package fadxapp.es.jagafo.controller.jobs.appjobs;

import fadxapp.es.jagafo.controller.jobs.GeneralJob;
import fadxapp.es.jagafo.controller.jobs.uijobs.AppJobFinishListenerJob;

/**
 * Todo job de la aplicacion extendera de esta clase con el objeto de crear
 * automaticamente un job que escuche cuando termina este para escribir
 * informacion en la zona de status Line
 * @author jagafo
 */
public abstract class GeneralAppJob extends GeneralJob {
	/**
	 * MODIFICAMOS EL FAMILY para esta familia de jobs, machando el de {@link GeneralJob}
	 */
	public static final String FAMILY = "APP_JOB_FAMILY";

	public GeneralAppJob(String name) {
		super(name);
		setUser(false);
		setSystem(false);
		setFamily(FAMILY);
		addJobChangeListener(new AppJobFinishListenerJob());
	}
}
