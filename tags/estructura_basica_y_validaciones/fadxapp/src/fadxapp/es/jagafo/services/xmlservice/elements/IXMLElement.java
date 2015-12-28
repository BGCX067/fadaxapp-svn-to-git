package fadxapp.es.jagafo.services.xmlservice.elements;

import java.util.List;

/**
 * Elementos de un un documento {@link IXMLDocument}
 * @author jagafo
 */
public interface IXMLElement {
	String getName();

	List<IXMLElement> getChildren();

	String getAttributeValue(String name);

	void setDom(XMLDocument dom);

	@SuppressWarnings("rawtypes")
	List getAttributes();

	String getValue();
}
