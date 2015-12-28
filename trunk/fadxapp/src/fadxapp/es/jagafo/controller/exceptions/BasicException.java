package fadxapp.es.jagafo.controller.exceptions;

import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.logservice.ILogService;

/**
 * Excepcion basica de la que heredaran todos las excepciones de la aplicacion
 * Cuando se produce automaticamente se loguea en el servicio {@link ILogService} 
 * @author jagafo
 */
public abstract class BasicException extends Exception {
	public static enum Type {
		ERROR, WARN, DEBUG
	};

	private static final long serialVersionUID = 1L;
	private Object classDispatcher = this;
	private static ILogService logService;

	protected BasicException() {
		super();
	}

	protected BasicException(String msg) {
		super(msg);
		log(msg, Type.ERROR);
	}

	protected BasicException(Throwable e) {
		super(e);
		log(e.getMessage(), Type.ERROR);
	}

	protected BasicException(Object classDispatcher, String msg) {
		this(classDispatcher, msg, Type.ERROR);
	}
	
	protected BasicException(Object classDispatcher, String msg,Throwable e) {
		this(classDispatcher, msg, e,Type.ERROR);
	}
	
	protected BasicException(Object classDispatcher, Throwable e) {
		this(classDispatcher,e,Type.ERROR);
	}

	protected BasicException(Object classDispatcher, String msg, Type type) {
		super(msg);
		this.classDispatcher = classDispatcher;
		log(msg, type);
	}
	
	protected BasicException(Object classDispatcher, String msg, Throwable e,Type type) {
		super(e);
		this.classDispatcher = classDispatcher;
		log(msg, type);
	}
	
	protected BasicException(Object classDispatcher, Throwable e, Type type) {
		super(e);
		this.classDispatcher = classDispatcher;
		log(null,e, type);
	}
	
	protected void log(String message, Type type){
		log(message,null,type);
	}
	
	protected void log(String message, Throwable e,Type type) {
		if (logService==null)
			logService = ((ILogService) ServiceLocator
				.getService(ILogService.class));
		if (logService != null) {
			if (type == Type.ERROR) {
				logService.error(classDispatcher.getClass().getSimpleName(),
						message==null?e.getMessage():message, e==null?this:e);
			} else if (type == Type.WARN) {
				logService.warn(classDispatcher.getClass().getSimpleName(),
						message==null?e.getMessage():message);
			} else {
				logService.debug(classDispatcher.getClass().getSimpleName(),
						message==null?e.getMessage():message);
			}
		}
	}
}