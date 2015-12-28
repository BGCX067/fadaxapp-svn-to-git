package fadxapp.es.jagafo.controller.exceptions;

/**
 * Cualquier excepcion que produzca un servicio de la aplicacion
 * @author jagafo
 */
public class ServiceException extends BasicException {

	private static final long serialVersionUID = -8127813535314468461L;

	public ServiceException() {
		super();
	}

	public ServiceException(Object classDispatcher, String msg, Type type) {
		super(classDispatcher, msg, type);
	}

	public ServiceException(Object classDispatcher, String msg) {
		super(classDispatcher, msg);
	}

	public ServiceException(Object classDispatcher, String msg,Throwable e) {
		super(classDispatcher, msg,e);
	}
	
	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Throwable e) {
		super(e);
	}
}
