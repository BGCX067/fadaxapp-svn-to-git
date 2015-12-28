package fadxapp.es.jagafo.services.bbddservice;

import java.sql.Connection;

import fadxapp.es.jagafo.services.IService;
import fadxapp.es.jagafo.services.ServiceImplAnnotation;

/**
 * Proporcina los metodos necesarios para trabajar con la bbdd
 * @author jagafo
 *
 */
@ServiceImplAnnotation(serviceImpl=DatabaseService.class)
public interface IDatabaseService extends IService {
	
	/**
	 * @return Crea la conexion a la bbdd de la aplicacion
	 */
	Connection createConnection();
}
