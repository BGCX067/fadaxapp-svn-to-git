package fadxapp.es.jagafo.services.logservice;

import org.apache.logging.log4j.LogManager;


public class LogService implements ILogService {

	public LogService() {
	}
	
	private String getPuntoEntrada(Object value){
		if (value instanceof String){
			return value.toString();
		}else{
			return value.getClass().getSimpleName();
		}
	}

	public void debug(Object puntoEntrada, Object message) {
		LogManager.getLogger(getPuntoEntrada(puntoEntrada)).debug(message);
	}

	public void info(Object puntoEntrada, Object message) {
		LogManager.getLogger(getPuntoEntrada(puntoEntrada)).info(message);
	};

	public void warn(Object puntoEntrada, Object message) {
		LogManager.getLogger(getPuntoEntrada(puntoEntrada)).warn(message);
	};

	public void error(Object puntoEntrada, Object message, Throwable t) {
		LogManager.getLogger(getPuntoEntrada(puntoEntrada)).error(message,t);
	};

	public void fatal(Object puntoEntrada, Object message, Throwable t) {
		LogManager.getLogger(getPuntoEntrada(puntoEntrada)).fatal(message,t);
	}
}