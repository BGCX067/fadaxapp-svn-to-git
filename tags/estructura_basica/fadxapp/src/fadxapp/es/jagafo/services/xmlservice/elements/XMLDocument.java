package fadxapp.es.jagafo.services.xmlservice.elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import fadxapp.es.jagafo.controller.exceptions.XMLException;
import fadxapp.es.jagafo.model.ICacheable;

/**
 * Wrapper sobre la clase IDocument de jDom
 * @author jagafo
 */
public class XMLDocument implements IXMLDocument, ICacheable {
	private static final long serialVersionUID = -7457273432308226999L;

	private IXMLElement rootElement;
	private Document dom;
	private boolean isCacheable = false;
	private String filePath = "";
	private long dateModified = -1;

	protected XMLDocument(String name) {
		super();
		setDom(new Document(new Element(name)));
		rootElement = new XMLElement(dom.getRootElement());
		rootElement.setDom(this);
		((XMLElement) rootElement).iterarNodos();
	}

	public XMLDocument(Document dom) {
		super();
		setDom(dom);
		rootElement = new XMLElement(dom.getRootElement());
		rootElement.setDom(this);
		((XMLElement) rootElement).iterarNodos();
	}

	public Document getDom() {
		return dom;
	}

	protected void setDom(Document dom) {
		this.dom = dom;
	}

	public IXMLElement getRootElement() {
		return rootElement;
	}

	public void setFilePath(String filePath) {
		if (filePath != null)
			this.filePath = filePath;
		try {
			if (new File(filePath).exists()) {
				setCacheable(true);
				dateModified = new File(filePath).lastModified();
			}
		} catch (Exception e) {
		}
		;
	}

	public void grabarADiscoXML(String nombreFichero)
			throws FileNotFoundException {
		Format format = Format.getPrettyFormat();
		format.setIndent("	");
		XMLOutputter outputter = new XMLOutputter(format);
		try {
			FileOutputStream fos = new FileOutputStream(nombreFichero);
			Writer out = new OutputStreamWriter(fos, "UTF8");
			outputter.output(getDom(), out);
			out.close();
			fos.close();
		} catch (Exception e) {
			new XMLException(this, "No es posible grabar fichero " + nombreFichero, e);
			throw new FileNotFoundException(nombreFichero);
		}
	}

	public String toString() {
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		return outputter.outputString(getDom());
	}

	public IXMLElement getElement(String path) throws Exception {
		XPathExpression<Element> xp = XPathFactory.instance().
	               compile(path, Filters.element());
		Element element = xp.evaluateFirst(getDom()); 
		return ((XMLElement) getRootElement()).buscarNodo(element);
	}

	public List<Element> getNodes(String xpath) throws XMLException {
		XPathExpression<Element> xp = XPathFactory.instance().
				compile(xpath, Filters.element());
		return xp.evaluate(getDom());
	}

	public String getCacheKeyId() {
		return filePath;
	}

	public boolean isCacheable() {
		try {
			return isCacheable && filePath.length() != 0
					&& new File(filePath).exists();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isUpdate() {
		try {
			if (filePath.length() > 0 && new File(filePath).exists()) {
				return new File(filePath).lastModified() == dateModified;
			} else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public void setCacheable(boolean b) {
		isCacheable = b;
	}
}
