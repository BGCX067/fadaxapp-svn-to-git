package fadxapp.es.jagafo.services.xmlservice;

import org.jdom2.Element;

import fadxapp.es.jagafo.services.IService;
import fadxapp.es.jagafo.services.ServiceImplAnnotation;
import fadxapp.es.jagafo.services.xmlservice.elements.IXMLElement;

/**
 * Servicio encargado de parsear los elementos analizados a XML.
 * @jagafo
 */
@ServiceImplAnnotation(serviceImpl=XMLService.class)
public interface IXMLService extends IService {

	/**
	 * Dada una ruta de un fichero, lo convierte al formato interno IElement
	 * 
	 * @param path
	 * @return @throws Exception
	 */
	IXMLElement parseToNodes(String path) throws Exception;

	/**
	 * Transforma el objeto que se pasa como parametro a un string XML
	 * 
	 * @param o Objecto a serializar en XML
	 * @return Contenido XML del objeto, null si no se ha podido convertir
	 */
	String encodeToStr(Object o);
	
	/**
	 * Transforma el objeto que se pasa como parametro a un {@link Element}
	 * 
	 * @param o Objecto a serializar en XML
	 * @return Contenido XML del objeto, null si no se ha podido convertir
	 */
	Element encodeToElementDom(Object o);
	
	/**
	 * Transforma el contenido que se pasa como XML en un objeto
	 * @param xmlContent Contenido a parsear
	 * @return Objeto parseado, null si no ha sido posible parsearlo
	 */
	Object decode(String xmlContent);
}
