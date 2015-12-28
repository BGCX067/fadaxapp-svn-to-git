package fadxapp.es.jagafo.controller.jobs;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.operation.IRunnableWithProgress;

/**
 * Interfaz que define cualquier job que lance la aplicacion
 * @jagafo
 */
public interface IGeneralJob {
	/**
	 * Identificador del parametro que contendrá una referencia al Job que ha lanzado a this
	 */
	static final String PARAM_PARENT_JOB = "PARENT_JOB";
	/**
	 * Identificador del parametro que contendrá un ruta o rutas que requiera un job para su funcionamiento
	 */
	static final String PARAM_PATH = "PATH";
	/**
	 * Identificador del parametro para los resultados que devuelva un job en su salida
	 */
	static final String PARAM_RESULTS = "RESULTS";
	
	/**
	 * Devuelve el monitor asociado al job
	 * @return
	 */
	public void setMonitor(IProgressMonitor monitor);
	
	/**
	 * Devuelve el monitor asociado al job
	 * @return
	 */
	public IProgressMonitor getMonitor();
	
	/**
	 * Devuelve true si el monitor esta cancelado o monitor es null, false en otro caso
	 * @return
	 */
	public boolean isMonitorCanceled();

	/**
	 * Establece si el trabajo del job es o no indetermindo
	 * @param b
	 */
	public void setIndeterminate(boolean b);
	
	/**
	 * Establece un porcentaje en el nombre del proceso
	 * @param current
	 * @param total
	 */
	public void setPercent(int current,int total);

	/**
	 * Establece como a terminado el job
	 * 
	 * @param b
	 *            True si termino correctamente, false en otro caso
	 */
	void setFinishOK(boolean b);

	/**
	 * comprueba como ha terminado el job
	 * 
	 * @return True bien, false otro caso
	 */
	boolean isFinishedOK();

	/**
	 * Establece el mensaje de como ha terminado el job
	 * 
	 * @param msg
	 */
	void setFinishMessage(String msg);

	/**
	 * Devuelve el mensaje de como ha terminado el job
	 * 
	 * @return
	 */
	String getFinishMessage();

	/**
	 * Establece los parametros de finalizacion
	 * 
	 * @param finishOk
	 *            True si ha terminado bien, false en otro caso
	 * @param msg
	 *            Mensaje
	 */
	void setFinishStatus(boolean finishOk, String msg);

	/**
	 * Establece los parametros que necesita el job para ejercer su trabajo
	 * 
	 * @param parameters
	 */
	void setParameters(Map<String, Object> parameters);

	/**
	 * Devuelve los parametros que usa el Job durante su ejecucion
	 * 
	 * @return
	 */
	Map<String, Object> getParameters();

	/**
	 * Establece un parametro para el proceso
	 */
	void setParameter(String key, Object value);

	/**
	 * Devuelve el parametro
	 * 
	 * @return Object Parametro, null si no lo encuentra
	 */
	Object getParameter(String key);

	/**
	 * Indica si el proceso contiene o no el parametro
	 * 
	 * @param key
	 *            Parametro a buscar
	 */
	boolean hasParameter(String key);

	/**
	 * Este método run pertenece a la interfaz {@link IRunnableWithProgress}. Es
	 * donde se realiza todo el peso de la accion del usuario. Se divide en dos
	 * grandes funciones: el prepareAction, encargado de realizar las consultas
	 * al diccionario y procesarlas y el doRun, encargado de visualizar las
	 * información procesada
	 * 
	 * @param monitor
	 *            Monitor de progreso asociado a la accion
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	IStatus run(final IProgressMonitor monitor);

	/**
	 * Establece el titulo principal del monitor
	 * 
	 * @param title
	 */
	public void setMainTaskTitle(String title);

	/**
	 * Establece el titulo secundario del monitor
	 * 
	 * @param title
	 */
	public void setSecondaryTaskTitle(String title);
	
	/**
	 * Aumenta el tamaño del trabajo total
	 * @param increment
	 */
	public void incTotalWork(int increment);
}
