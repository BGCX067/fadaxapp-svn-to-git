package fadxapp.es.jagafo.controller.exceptions;

/**
 * Cualquier error provocado por el analisis o manejo de informacion XML se
 * maneja mediante esta excepcion
 * @author jagafo
 */
public class XMLException extends BasicException {

	private static final long serialVersionUID = -8127813535314468461L;

	public XMLException() {
		super();
	}

	public XMLException(Object classDispatcher, String msg, Type type) {
		super(classDispatcher, msg, type);
	}

	public XMLException(Object classDispatcher, String msg) {
		super(classDispatcher, msg);
	}

	public XMLException(String msg) {
		super(msg);
	}

	public XMLException(Throwable e) {
		super(e);
	}

	public XMLException(Object classDispatcher, String string, Exception e) {
		super(classDispatcher,string,e);
	}
}
