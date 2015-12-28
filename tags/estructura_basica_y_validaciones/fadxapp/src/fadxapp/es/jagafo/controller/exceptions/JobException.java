package fadxapp.es.jagafo.controller.exceptions;

/**
 * Cualquier error provocado por un job de la aplicacion se maneja mediante esta
 * clase
 */
public class JobException extends DisplayedException {

	private static final long serialVersionUID = -8127813535314468461L;

	protected JobException() {
		super();
	}

	public JobException(Object classDispatcher, String msg, Type type) {
		super(classDispatcher, msg, type);
	}

	public JobException(Object classDispatcher, String msg) {
		super(classDispatcher, msg);
	}
	
	public JobException(Object classDispatcher, String msg,Exception e) {
		super(classDispatcher, msg, e);
	}
	
	public JobException(Object classDispatcher, Exception e) {
		super(classDispatcher, e);
	}

	protected JobException(String msg) {
		super(msg);
	}

	protected JobException(Throwable e) {
		super(e);
	}
}
