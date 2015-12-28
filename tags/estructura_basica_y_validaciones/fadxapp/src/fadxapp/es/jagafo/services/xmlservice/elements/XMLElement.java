package fadxapp.es.jagafo.services.xmlservice.elements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

/**
 * Wraper sobre un IElement de jDom
 * @author jagafo
 */
public class XMLElement implements IXMLElement, Serializable {
	private static final long serialVersionUID = -4709107463199557344L;

	private List<IXMLElement> children = new ArrayList<IXMLElement>();

	private XMLDocument dom;

	private Element element;

	public XMLElement(Element element) {
		super();
		setElement(element);
		setDom(null);
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public void setDom(XMLDocument dom) {
		this.dom = dom;
	}

	public String getName() {
		return getElement().getName();
	}

	public String getValue() {
		return element.getValue();
	}

	public void iterarNodos() {
		@SuppressWarnings("rawtypes")
		List lista = getElement().getChildren();
		for (int i = 0, tope = lista.size(); i < tope; i++) {
			Element item = (Element) lista.get(i);
			XMLElement elementLuna = new XMLElement(item);
			getChildren().add(elementLuna);
			if (getDom() != null) {
				elementLuna.setDom(getDom());
			}
			elementLuna.iterarNodos();
		}
	}

	public IXMLElement buscarNodo(Element element) {
		if (element != null){
			if (getElement().equals(element)) {
				return this;
			} else {
				for (int i = 0, tope = getChildren().size(); i < tope; i++) {
					XMLElement nodo = (XMLElement) getChildren().get(i);
					if (nodo.getElement().equals(element)) {
						return nodo;
					} else {
						IXMLElement n = nodo.buscarNodo(element);
						if (n != null) {
							return n;
						}
					}
				}
			}
		}
		return null;
	}

	public XMLDocument getDom() {
		return dom;
	}

	@SuppressWarnings("unused")
	public String getAttributeValue(String name) {
		String result = getElement().getAttributeValue(name);
		if (result == null)
			result = getElement().getAttributeValue(name.toLowerCase());
		else if (result == null)
			result = getElement().getAttributeValue(name.toUpperCase());
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List getAttributes() {
		return element.getAttributes();
	}

	public List<IXMLElement> getChildren() {
		return children;
	}

	public IXMLElement getElement(String path) throws Exception {
		XPathExpression<Element> xp = XPathFactory.instance().
	               compile(path, Filters.element());
		return buscarNodo(xp.evaluateFirst(getElement()));
	}
}
