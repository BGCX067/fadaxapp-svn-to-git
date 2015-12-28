package fadxapp.es.jagafo.services.startapplicationservice;

import fadxapp.es.jagafo.services.IService;
import fadxapp.es.jagafo.services.ServiceImplAnnotation;

/**
 * Servicio encargado de inicializar el entorno para que todas las clases estén
 * correctamente inicializadas antes de ejecutar la aplicación El primer
 * servicio que llame a ServiceProvider.getServicio(servicio) será el encargado
 * de inicializar este servicio. ServiceProvider tiene un método estático que se
 * encarga de cargar e inicializar los fichero paths.xml y services.xml
 * @author jagafo
 */
@ServiceImplAnnotation(serviceImpl=StartApplicationService.class)
public interface IStartApplicationService extends IService {

	boolean start();

	/**
	 * Libera el bloqueo sobre el fichero de control de ejecucion de la
	 * aplicacion y ademas lo elimina.
	 * 
	 * @throws Exception
	 */
	void release() throws Exception;
}