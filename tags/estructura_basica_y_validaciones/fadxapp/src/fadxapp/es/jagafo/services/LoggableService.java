package fadxapp.es.jagafo.services;

import fadxapp.es.jagafo.services.logservice.ILogService;

/**
 * Dota de la funcionalidad de logueo al servicio que extienda de esta clase
 * @author jagafo
 *
 */
public class LoggableService implements ILogabbleService {

	@Override
	public ILogService getLogService() {
		return (ILogService)ServiceLocator.getService(ILogService.class);
	}

}
