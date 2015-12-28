package fadxapp.es.jagafo.services.finishapplicationservice;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.ui.PlatformUI;

import fadxapp.es.jagafo.controller.exceptions.ServiceException;
import fadxapp.es.jagafo.model.appproperty.IPropertiesKeys;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.apppropertiesservice.IAppPropertyService;
import fadxapp.es.jagafo.services.cacheservice.ICacheService;
import fadxapp.es.jagafo.services.logservice.ILogService;
import fadxapp.es.jagafo.services.startapplicationservice.IStartApplicationService;
import fadxapp.es.jagafo.utils.RCPUtils;

/**
 * @see IFinishApplicationService
 * @author jagafo
 */
public class FinishApplicationService implements IFinishApplicationService {
	// Milisegundos a esperar para finalizar todos los jobs que se esten ejecutando
	private static final long CHECK_JOB_SLEEP = 250;
	private static final long CHECK_JOB_SLEEP_MAX = 1000;

	public FinishApplicationService() {
	}

	public void finish(int returnCode) {
		if (returnCode == IApplication.EXIT_OK){
			if (Boolean.parseBoolean(((IAppPropertyService) ServiceLocator
					.getService(IAppPropertyService.class))
					.loadPreference(IPropertiesKeys.FIRST_LAUNCH_KEY,
							"true"))){
				((IAppPropertyService) ServiceLocator
						.getService(IAppPropertyService.class))
						.savePreference(IPropertiesKeys.FIRST_LAUNCH_KEY,
								"false");
			}
		}
		if (returnCode != PlatformUI.RETURN_EMERGENCY_CLOSE){
			((ICacheService) ServiceLocator
					.getService(ICacheService.class))
					.saveAllCacheToDisk();

			RCPUtils.removeJobManagerListener();
			// Tratando de parar todos los jobs que se esten ejecutando
			int tryingCount = 1;
			int remainJob = Job.getJobManager().find(null).length;
			while (!Job.getJobManager().isIdle()){
				Job.getJobManager().cancel(null);
				try {
					// Esperaremos un tiempo incremental segun el numero de intentos
					Thread.sleep(CHECK_JOB_SLEEP*tryingCount<=CHECK_JOB_SLEEP_MAX?CHECK_JOB_SLEEP*tryingCount:CHECK_JOB_SLEEP_MAX);
					if (CHECK_JOB_SLEEP*tryingCount<=CHECK_JOB_SLEEP_MAX)
						tryingCount++;
					else{
						if (remainJob == Job.getJobManager().find(null).length)
							break;
						else
							remainJob = Job.getJobManager().find(null).length;
						tryingCount = 1;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				((IStartApplicationService) ServiceLocator
						.getService(IStartApplicationService.class))
						.release();
			} catch (Exception e) {
				new ServiceException(
						this,
						"No se ha podido desbloquear el fichero de control de incio",
						e);
			}

			//			TODO: Cerrar conexiones pendientes?
			//			try {
			//				((IAccesDDBBService) ServiceLocator
			//						.getService(ServiceLocator.DDBB_SERVICE)).closeConnection();
			//			} catch (DatabaseException e) {
			//			}

			// Obtiene la fecha y hora actual
			Date date = new Date();
			Format formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
			String s = formatter.format(date);
			((ILogService) ServiceLocator.getService(ILogService.class))
			.debug(getClass().getSimpleName(),
					"Finalizó la aplicación con el código = " + returnCode
					+ " : " + s);

			((IAccessResourcesService)ServiceLocator.getService(IAccessResourcesService.class)).disposeImageRegistry();
		}
	}
}
