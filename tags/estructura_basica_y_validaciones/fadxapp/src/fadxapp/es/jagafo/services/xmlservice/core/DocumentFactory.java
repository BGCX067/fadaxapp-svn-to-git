package fadxapp.es.jagafo.services.xmlservice.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import fadxapp.es.jagafo.controller.exceptions.XMLException;
import fadxapp.es.jagafo.model.ICacheable;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.services.cacheservice.ICacheService;
import fadxapp.es.jagafo.services.xmlservice.elements.IXMLDocument;
import fadxapp.es.jagafo.services.xmlservice.elements.XMLDocument;

/**
 * Define la factoria encarga de devolver instancias de documentos XML para
 * los servicios de analisis de la aplicacion, entre otros
 * @author jagafo
 */
public class DocumentFactory implements IDocumentFactory {
	public DocumentFactory() {
		super();
	}

	public IXMLDocument getDocument(String path, String nombre)
			throws XMLException {
		return getDocument(path, nombre, true);
	}

	public IXMLDocument getDocument(String path, String nombre, boolean allowCached)
			throws XMLException {
		if (allowCached) {
			ICacheService cacheService = ((ICacheService) ServiceLocator
					.getService(ICacheService.class));
			if (cacheService != null) {
				String key = "";
				if (path != null)
					key = path + nombre;
				else
					key = nombre;
				Object cachedDoc = cacheService.get(
						ICacheService.CACHE_JDOM_DOCUMENTS, key);
				if (cachedDoc != null && cachedDoc instanceof ICacheable
						&& ((ICacheable) cachedDoc).isUpdate())
					return (IXMLDocument) cachedDoc;
				else if (cachedDoc != null)
					cacheService
							.remove(ICacheService.CACHE_JDOM_DOCUMENTS, key);
			}
		}
		SAXBuilder parser = new SAXBuilder();
		try {
			Document doc = parser.build(PathProvider.getInstance()
					.getInputStream(path, nombre));
			XMLDocument BPdoc = new XMLDocument(doc);
			if (path != null)
				BPdoc.setFilePath(path + nombre);
			else
				BPdoc.setFilePath(nombre);
			if (allowCached
					&& ((ICacheService) ServiceLocator
							.getService(ICacheService.class)) != null
					&& BPdoc.isCacheable())
				((ICacheService) ServiceLocator
						.getService(ICacheService.class)).put(
						ICacheService.CACHE_JDOM_DOCUMENTS, BPdoc
								.getCacheKeyId(), BPdoc);
			return BPdoc;
		} catch (JDOMException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
	}

	public IXMLDocument getDocument(String nombreCompleto) throws XMLException {
		return getDocument(nombreCompleto, true);
	}

	public IXMLDocument getDocument(String nombreCompleto, boolean cacheable)
			throws XMLException {
		if (cacheable) {
			ICacheService cacheService = ((ICacheService) ServiceLocator
					.getService(ICacheService.class));
			if (cacheService != null) {
				Object cachedDoc = cacheService.get(
						ICacheService.CACHE_JDOM_DOCUMENTS, nombreCompleto);
				if (cachedDoc != null && cachedDoc instanceof ICacheable
						&& ((ICacheable) cachedDoc).isUpdate())
					return (IXMLDocument) cachedDoc;
				else if (cachedDoc != null)
					cacheService.remove(ICacheService.CACHE_JDOM_DOCUMENTS,
							nombreCompleto);
			}
		}
		InputStream is = ((IAccessResourcesService) ServiceLocator
				.getService(IAccessResourcesService.class))
				.getResource(nombreCompleto);
		try {
			SAXBuilder parser = new SAXBuilder();
			Document doc = parser.build(is);
			XMLDocument BPdoc = new XMLDocument(doc);
			BPdoc.setFilePath(nombreCompleto);
			if (cacheable
					&& ((ICacheService) ServiceLocator
							.getService(ICacheService.class)) != null
					&& BPdoc.isCacheable())
				((ICacheService) ServiceLocator
						.getService(ICacheService.class)).put(
						ICacheService.CACHE_JDOM_DOCUMENTS, BPdoc
								.getCacheKeyId(), BPdoc);
			return BPdoc;
		} catch (JDOMException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
	}

	public IXMLDocument createDocument(String xmlContent) throws XMLException {
		try {
			InputStream is = new ByteArrayInputStream(xmlContent
					.getBytes("UTF-8"));
			SAXBuilder parser = new SAXBuilder();
			return new XMLDocument(parser.build(is));
		} catch (JDOMException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
	}

	public IXMLDocument createDocument(InputStream inputStream)
			throws XMLException {
		try {
			SAXBuilder parser = new SAXBuilder();
			return new XMLDocument(parser.build(inputStream));
		} catch (JDOMException e) {
			throw new XMLException(e);
		} catch (IOException e) {
			throw new XMLException(e);
		}
	}

	public IXMLDocument createDocument(Element rootElement) {
		return new XMLDocument(new Document(rootElement));
	}

	public List<Element> getNodes(String xpath, String filePath)
			throws XMLException {
		return getNodes(xpath, filePath, true);
	}

	public List<Element> getNodes(String xpath, String filePath,
			boolean allowCached) throws XMLException {
		IXMLDocument jdomDoc = getDocument(filePath, allowCached);
		XPathExpression<Element> xp = XPathFactory.instance().
				compile(xpath, Filters.element());
		return xp.evaluate(jdomDoc.getDom());
	}
}
