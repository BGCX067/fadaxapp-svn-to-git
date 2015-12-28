package fadxapp.es.jagafo.services.xmlservice.elements;

import java.io.FileNotFoundException;

import org.jdom2.Document;

/**
 * Entidad que representa un documento XML a cargar en la aplicacion 
 * @author jagafo
 */
public interface IXMLDocument {

	Document getDom();

	IXMLElement getRootElement();

	void grabarADiscoXML(String nombreFichero) throws FileNotFoundException;

	void setFilePath(String filePath);
}
