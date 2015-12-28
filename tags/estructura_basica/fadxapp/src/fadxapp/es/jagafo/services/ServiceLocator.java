package fadxapp.es.jagafo.services;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import fadxapp.es.jagafo.services.accesresourcesservice.AccessResourcesService;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.logservice.ILogService;
import fadxapp.es.jagafo.services.logservice.LogService;

public class ServiceLocator implements IServiceLocator {

	private static ConcurrentMap<Class<? extends IService>, IService> serviciosClass = new ConcurrentHashMap<Class <? extends IService>, IService>();

	private static boolean inicializado = false;

	private static IAccessResourcesService accesoRecursos;

	public static IService getService(Class<? extends IService> serviceClass) {
		IService result = null;
		try {
			ServiceImplAnnotation anotation = 
				(ServiceImplAnnotation) serviceClass.getAnnotation(
						ServiceImplAnnotation.class);
			if (anotation != null){
				serviceClass = anotation.serviceImpl();
			}	
			if (!inicializado){
				/**
				 * El localizador, para leer su propia configuración, necesita del
				 * servicio de acceso a recursos, por eso, cuando el localizador no
				 * está inicializado tiene que asumir alguna implementación por
				 * defecto para este servicio.
				 */
				if (accesoRecursos == null){
					accesoRecursos = AccessResourcesService.class.newInstance();
					serviciosClass.put(AccessResourcesService.class, accesoRecursos);
				}
			}
			result = serviciosClass.get(serviceClass);
			if (result == null){
				result = (IService)serviceClass.newInstance();
				serviciosClass.put(serviceClass, result);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			if (LogService.class.equals(serviceClass)){
				System.out.println("No se ha podido instanciar el servicio de loggin de la aplicacion");
				e.printStackTrace();
			}else{
				((LogService) ServiceLocator.getService(ILogService.class))
				.error("ServiceProvider",
						"No se han podido instanciar la clase "
								+ serviceClass, e);
			}
		}
		return result;
	}
	
	public static void removeService(Class <? extends IService> serviceClass){
		if (inicializado){
			serviciosClass.remove(serviceClass);
		}
	}
}
