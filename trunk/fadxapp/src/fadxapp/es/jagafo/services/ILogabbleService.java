package fadxapp.es.jagafo.services;

import fadxapp.es.jagafo.services.logservice.ILogService;


/**
 * Cualquier servicio que intente loguear informacion
 * @author luisjgf
 *
 */
public interface ILogabbleService extends IService {
	/**
	 * 
	 * @return Servicio de logueo
	 */
	ILogService getLogService();
}
