package fadxapp.es.jagafo.services.xmlservice.core;

import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import fadxapp.es.jagafo.controller.exceptions.XMLException;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.logservice.ILogService;

/**
 * Clase encargada de sumninistrar las rutas de acceso a los servicios de la
 * aplicacion
 */
public class PathProvider {
	private Document dom;

	private static PathProvider path = null;

	private PathProvider() {
		super();
	}

	public static PathProvider getInstance() {
		if (path != null)
			return path;
		else {
			path = new PathProvider();
			return path;
		}
	}

	public void initialize(String fileName) {
		SAXBuilder builder = new SAXBuilder();
		try {
			InputStream is = ((IAccessResourcesService) ServiceLocator
					.getService(IAccessResourcesService.class))
					.getResource(fileName);
			setDom(builder.build(is));
		} catch (Exception e) {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).debug(getClass()
							.getName(), "Error JDOM en " + fileName + ":" + e);
		}
	}

	public String getPath(String directorio) {
		Element elemento = getDom().getRootElement().getChild(directorio);
		if (elemento != null) {
			return elemento.getAttributeValue("path");
		} else {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).debug(getClass()
							.getName(),
							"No se puede encontrar el path correspondiente a "
							+ directorio);
			return null;
		}
	}

	public String getType(String directory) {
		Element elemento = getDom().getRootElement().getChild(directory);

		if (elemento != null) {
			return elemento.getAttributeValue("tipo");
		} else {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).debug(getClass()
							.getName(),
							"No se puede encontrar el tipo correspondiente a "
							+ directory);
			return null;
		}
	}

	public InputStream getInputStream(String especie, String nombre) throws XMLException {
		String path = getPath(especie);
		String tipo = getType(especie);
		String nombreRecurso = new String("");
		if (path != null)
			nombreRecurso = path + nombre;
		else
			nombreRecurso = nombre;
		InputStream resultado = null;
		if (tipo.equals("fich"))
			resultado = getInputStreamFromFichero(nombreRecurso);
		else if (tipo.equals("url"))
			resultado = getInputStreamFromURL(nombreRecurso);
		else if (tipo.equals("jar"))
			resultado = getInputStreamFromJar(nombreRecurso);
		else
			resultado = null;

		if (resultado == null)
			throw new XMLException("No puedo encontrar el recurso : " + nombre);
		return resultado;
	}

	private InputStream getInputStreamFromFichero(String nombreRecurso) {
		try {
			return ((IAccessResourcesService) ServiceLocator
					.getService(IAccessResourcesService.class))
					.getResource(nombreRecurso);
		} catch (Exception e) {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).debug(getClass()
							.getName(), "No encuentro el fichero " + nombreRecurso
							+ "e: " + e);
			return null;
		}
	}

	private InputStream getInputStreamFromJar(String nombreRecurso) {
		try {
			nombreRecurso = nombreRecurso.replace('\\', '/');
			return getClass().getResourceAsStream(nombreRecurso);
		} catch (Exception e) {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).debug(getClass()
							.getName(), "No encuentro el fichero " + nombreRecurso
							+ " en el jar. e: " + e);
			return null;
		}
	}

	private InputStream getInputStreamFromURL(String nombreRecurso) {
		try {
			java.net.URL myURL = new java.net.URL(nombreRecurso);
			return myURL.openStream();
		} catch (Exception e) {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).debug(getClass()
							.getName(), "No encuentro el fichero " + nombreRecurso
							+ " en la URL. e: " + e);
			return null;
		}
	}

	public String getSetting(String directorio) {
		Element elemento = getDom().getRootElement().getChild(directorio);
		if (elemento != null) {
			return elemento.getText();
		} else {
			return null;
		}
	}

	private Document getDom() {
		return dom;
	}

	private void setDom(Document dom) {
		this.dom = dom;
	}
}
