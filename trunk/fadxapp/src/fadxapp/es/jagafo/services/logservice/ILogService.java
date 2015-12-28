package fadxapp.es.jagafo.services.logservice;

import fadxapp.es.jagafo.services.IService;
import fadxapp.es.jagafo.services.ServiceImplAnnotation;

/**
 * Servicio encargado de realizar los logueos de la aplicaci�n usando log4j
 * @author jagafo
 */
@ServiceImplAnnotation(serviceImpl=LogService.class)
public interface ILogService extends IService {

	/**
	 * Anotaci�n de debug en Log
	 * 
	 * @param puntoEntrada Donde se produce el fallo
	 * @param message Cadena a mostrar
	 * 
	 */
	void debug(Object puntoEntrada, Object message);

	/**
	 * Anotaci�n de Info en Log
	 * 
	 * @param puntoEntrada Donde se produce el fallo
	 * @param message Cadena a mostrar
	 * 
	 */
	void info(Object puntoEntrada, Object message);

	/**
	 * Anotaci�n de Warning en Log
	 * 
	 * @param puntoEntrada Donde se produce el fallo
	 * @param message Cadena a mostrar
	 * 
	 */
	void warn(Object puntoEntrada, Object message);

	/**
	 * Anotaci�n de Error en Log
	 * 
	 * @param puntoEntrada Donde se produce el fallo
	 * @param message Cadena a mostrar
	 * @param t
	 * 
	 */
	void error(Object puntoEntrada, Object message, Throwable t);

	/**
	 * Anotaci�n de Error Fatal en Log
	 * 
	 * @param puntoEntrada Donde se produce el fallo
	 * @param message Cadena a mostrar
	 * @param t Excepcion a lanzar hacia arriba
	 * 
	 */

	void fatal(Object puntoEntrada, Object message, Throwable t);
}