package fadxapp.es.jagafo.services.finishapplicationservice;

import fadxapp.es.jagafo.services.IService;
import fadxapp.es.jagafo.services.ServiceImplAnnotation;

/**
 * Servicio encargado de finalizar el entorno correctamente
 * @author jagafo
 */
@ServiceImplAnnotation(serviceImpl=FinishApplicationService.class)
public interface IFinishApplicationService extends IService {

	/**
	 * Finaliza la aplicación RCP
	 * 
	 * @param returnCode
	 *            código de retorno que devuelven las aplicaciónes RCP una vez
	 *            que finalizan. Se controlará el valor de este código para
	 *            saber si la aplicación ha finalizado correctamente o no
	 * 
	 */
	void finish(int returnCode);
}