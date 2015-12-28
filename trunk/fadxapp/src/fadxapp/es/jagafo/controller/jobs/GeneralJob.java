package fadxapp.es.jagafo.controller.jobs;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.progress.IProgressConstants;

import fadxapp.es.jagafo.controller.exceptions.JobException;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;

/**
 * Define el esqueleto general de cualquier Job que va a lanzar la aplicacion.
 * Cualquier objeto que necesite escuchar los cambios de un Job necesitara
 * implementar {@link IJobChangeListener}
 * @author jagafo
 */
public abstract class GeneralJob extends Job implements IGeneralJob {
	/**
	 * Identifica la familia de todos los jobs que extiendad de General Job
	 */
	public static final String FAMILY ="GeneralJobFamily"; 
	private String name;
	
	private String family = FAMILY;
	
	private String main_task_title; // mensaje de la tarea principal del monitor
									// durante la ejecucion
	private String secondary_task_title;// mensaje de la tarea secundaria del
										// monitor durante la ejecucion
	private boolean indeterminate; // indica si la barra de estado del monitor
									// sera indeterminada o no
	private IProgressMonitor monitor; // monitor asociado a la accion

	private Map<String, Object> parameters; // Informacion requerida por la
											// accion para ejecutarse
	private boolean finishOK;
	
	private String finishMsg;
	
	private int currentWorked;
	
	private int totalWork;

	public GeneralJob(String name) {
		super(name);
		this.name = name;
		indeterminate = true;
		finishOK = true;
		finishMsg = "";
		setUser(true);
		setPriority(Job.LONG);
		currentWorked = 0;
		totalWork = 0;
	}
	
	public boolean belongsTo(Object family) {
		return this.family.equals(family);
	}
	
	protected void setFamily(String family){
		this.family = family.toString();
	}
	
	protected String getPercent(int current, int total){
		if (total == 0)
			return "";
		if (current == 0)
			return "(0.00%)";
		double fraction = (((double)current)/((double)total));
		return "("+MessageFormat.format("{0,number,0.00%}", fraction)+")";
	}
	
	public void beginPercent(){
		currentWorked = 0;
		if (hasParameter(PARAM_PARENT_JOB)){
			((Job)getParameter(PARAM_PARENT_JOB)).setName(name+" "+getPercent(currentWorked, getTotalWork()));
		}else
			setName(name+" "+getPercent(currentWorked, getTotalWork()));
	}
	
	public void incPercent(){
		currentWorked++;
		if (hasParameter(PARAM_PARENT_JOB)){
			((GeneralJob)getParameter(PARAM_PARENT_JOB)).incPercent();
		}else
			setName(name+" "+getPercent(currentWorked, getTotalWork()));
	}
	
	public void setPercent(int current,int total){
		if (hasParameter(PARAM_PARENT_JOB)){
			((GeneralJob)getParameter(PARAM_PARENT_JOB)).setPercent(current, total);
		}else
			setName(name+" "+getPercent(current, total));
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public IProgressMonitor getMonitor() {
		return monitor;
	}
	
	public boolean isMonitorCanceled(){
		return monitor!=null && monitor.isCanceled();
	}

	public void setIndeterminate(boolean b) {
		indeterminate = b;
	}

	public void setFinishOK(boolean b) {
		finishOK = b;
	}

	public boolean isFinishedOK() {
		return finishOK;
	}

	public void setFinishMessage(String msg) {
		finishMsg = msg.trim();
	}

	public String getFinishMessage() {
		return finishMsg;
	}

	public void setFinishStatus(boolean finishOk, String msg) {
		setFinishOK(finishOk);
		setFinishMessage(msg);
	}

	public void setMainTaskTitle(String title) {
		main_task_title = title;
		if (monitor != null && title != null) {
			monitor.setTaskName(main_task_title);
		}
	}

	public void setSecondaryTaskTitle(String title) {
		secondary_task_title = title;
		if (monitor != null && secondary_task_title != null) {
			monitor.subTask(secondary_task_title);
		}
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameter(String key, Object value) {
		if (parameters == null)
			parameters = new HashMap<String, Object>();
		parameters.put(key, value);
	}

	public Object getParameter(String key) {
		return parameters.get(key);
	}

	public boolean hasParameter(String key) {
		return parameters!=null && parameters.containsKey(key);
	}

	public void setImage(String key) {
		setProperty(IProgressConstants.ICON_PROPERTY,
				((IAccessResourcesService) ServiceLocator
						.getService(IAccessResourcesService.class))
						.getImageDescriptorFromRegistry(key));
	}

	public IStatus run(final IProgressMonitor monitor) {
		setMonitor(monitor);
		int totalWork = IProgressMonitor.UNKNOWN;
		if (!indeterminate)
			totalWork = getTotalWork();
		monitor.beginTask(main_task_title, totalWork);
		try{
			IStatus result = especificJobRun(monitor);
			if (result == null)
				return Status.OK_STATUS;
			else
				return result;
		}catch(Exception e){
			new JobException(this,e);
			return Status.CANCEL_STATUS;
		}
	}
	
	public String toString(){
		return getName();
	}
	
	/**
	 * Establece el trabajo total que tendra el job
	 * @param totalWork
	 */
	protected void setTotalWork(int totalWork){
		this.totalWork = totalWork;
	}
	
	public void incTotalWork(int increment){
		if (hasParameter(PARAM_PARENT_JOB)){
			((GeneralJob)getParameter(PARAM_PARENT_JOB)).incTotalWork(increment);
		}else{
			this.totalWork = totalWork+increment;
			setPercent(currentWorked, totalWork);
		}
	}

	/**
	 * Devuelve la cantidad de trabajo que se realizara si la tarea es
	 * indeterminada
	 * 
	 * @return
	 */
	public int getTotalWork(){
		return totalWork;
	}

	/**
	 * En esta funcion se definira la funcion especifica del trabajo
	 * 
	 * @param monitor
	 * @return
	 */
	public abstract IStatus especificJobRun(IProgressMonitor monitor);
}
