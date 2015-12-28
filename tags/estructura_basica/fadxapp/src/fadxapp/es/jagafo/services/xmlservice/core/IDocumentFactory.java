package fadxapp.es.jagafo.services.xmlservice.core;

import java.io.InputStream;
import java.util.List;

import org.jdom2.Element;

import fadxapp.es.jagafo.controller.exceptions.XMLException;
import fadxapp.es.jagafo.services.xmlservice.elements.IXMLDocument;

/**
 * Define el comportamiento de la factoria de documentos XML con los que trabajara la aplicacion
 * @author jagafo
 */
public interface IDocumentFactory {

	/**
	 * Llama a {@link #getDocument(String, String, boolean)} con el parametro de
	 * allowCached a true por defecto
	 * 
	 * @param path
	 * @param nombre
	 *            del path (Registrado en paths.xml) donde se encuentra el
	 *            recurso
	 * @return
	 * @throws XMLException
	 */
	IXMLDocument getDocument(String path, String nombre) throws XMLException;

	/**
	 * Obtiene un IDocument de la zona del workspace donde están los recursos
	 * 
	 * @param path
	 * @param nombre
	 *            del path (Registrado en paths.xml) donde se encuentra el
	 *            recurso
	 * @param allowCached
	 *            Si True se permite almacenar en cache
	 * @return El IDocument construido.
	 * @throws XMLException
	 */
	IXMLDocument getDocument(String path, String nombre, boolean allowCached)
			throws XMLException;

	/**
	 * Llama a {@link #getDocument(String, boolean)} con el segundo parametro a
	 * true por defecto
	 * 
	 * @param nombreCompleto
	 *            Nombre del fichero
	 * @return Documento instanciado
	 * @throws XMLException
	 */
	IXMLDocument getDocument(String nombreCompleto) throws XMLException;

	/**
	 * Establece si el documento es o no cacheable
	 * 
	 * @param nombreCompleto
	 * @param cacheable
	 *            Indica si el documento es o no cacheable
	 * @return El IDocument construido.
	 * @throws XMLException
	 */
	IXMLDocument getDocument(String nombreCompleto, boolean cacheable)
			throws XMLException;

	/**
	 * Crea un IDocumento a partir del contenido XML que se le pasa como
	 * parametro
	 * 
	 * @param xmlContent
	 *            Contenido xml del documento a crear
	 * @return IDocument construido
	 * @throws XMLException
	 *             Devuelve error si no se puede leer el stream de texto o si
	 *             ocurre algun problema al parsear el XML.
	 */
	IXMLDocument createDocument(String xmlContent) throws XMLException;

	/**
	 * Crea un IDocumento a partir de un InputStream que se le pasa como
	 * parametro
	 * 
	 * @param inputStream
	 *            Stream hacia el fichero desde donde se lee el documento
	 * @return IDocument construido
	 * @throws XMLException
	 */
	IXMLDocument createDocument(InputStream inputStream) throws XMLException;

	/**
	 * Crea un IDocument a partir de un Element de jDom que se pasa como
	 * parametro
	 * 
	 * @param rootElement
	 * @return
	 */
	IXMLDocument createDocument(Element rootElement);

	/**
	 * Llama a {@link #getNodes(String, String, boolean)} con el parametro
	 * allowCached a True
	 * 
	 * @param xpath
	 *            Expresion de seleccion de nodos
	 * @param filePath
	 *            Ruta del documento xml donde seleccionar los nodos
	 * @return
	 * @throws XMLException
	 */
	List<Element> getNodes(String xpath, String filePath) throws XMLException;

	/**
	 * Devuelve la coleccion de nodos seleccionadas en el documento cuya ruta es
	 * filePath
	 * 
	 * @param xpath
	 *            Expresion de seleccion de nodos
	 * @param filePath
	 *            Ruta del documento xml donde seleccionar los nodos
	 * @param allowCached
	 *            Si True se permite cachear el documento sobre el que se hace
	 *            la consulta
	 * @return Lista de nodos seleccionados que cumplen la expresion xpath
	 * @throws XMLException
	 *             Si no se puede crear la expresion xpath o no se puede crear
	 *             el documento XML
	 */
	List<Element> getNodes(String xpath, String filePath, boolean allowCached)
			throws XMLException;
}
