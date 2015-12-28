package fadxapp.es.jagafo.controller.exceptions;

/**
 * Cualquier error de la aplicación producida por un comportamiento indebido de
 * la aplicación se maneja con este error <br>
 * <b>Esta excepcion finaliza la aplicación</b>
 * @author jagafo
 */
public class InternalErrorException extends CriticalException {
	private static final long serialVersionUID = -7897829305917647147L;

	public InternalErrorException() {
		super();
	}

	public InternalErrorException(Object classDispatcher, String msg, Type type) {
		super(classDispatcher, msg, type);
	}

	public InternalErrorException(Object classDispatcher, String msg) {
		super(classDispatcher, msg);
	}

	public InternalErrorException(String msg) {
		super(msg);
	}

	public InternalErrorException(Throwable e) {
		super(e);
	}
}
