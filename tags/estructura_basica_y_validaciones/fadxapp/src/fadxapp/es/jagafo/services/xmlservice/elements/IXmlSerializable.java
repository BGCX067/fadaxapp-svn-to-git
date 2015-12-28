package fadxapp.es.jagafo.services.xmlservice.elements;

import org.jdom2.Element;

import fadxapp.es.jagafo.controller.exceptions.XMLException;

/**
 * Cualquier elemento que pueda ser tranformado en XML o bien
 * pueda inicializarse desde un elemento XML implementara esta interface
 * @author jagafo
 */
public interface IXmlSerializable {
	/**
	 * Inicializa los valores del objeto a partir del elemento que se pasa como
	 * parametro
	 * @param value
	 * @return
	 * @throws XMLException 
	 */
	void decodeXML(Element value) throws XMLException;
	
	/**
	 * Se construye un {@link Element} que representa toda la información
	 * necesaria para definir de manera completa el objeto
	 * @return
	 */
	Element encodeToXML();
}
