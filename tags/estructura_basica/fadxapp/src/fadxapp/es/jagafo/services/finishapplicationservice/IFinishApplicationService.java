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
	 * Finaliza la aplicaci�n RCP
	 * 
	 * @param returnCode
	 *            c�digo de retorno que devuelven las aplicaci�nes RCP una vez
	 *            que finalizan. Se controlar� el valor de este c�digo para
	 *            saber si la aplicaci�n ha finalizado correctamente o no
	 * 
	 */
	void finish(int returnCode);
}