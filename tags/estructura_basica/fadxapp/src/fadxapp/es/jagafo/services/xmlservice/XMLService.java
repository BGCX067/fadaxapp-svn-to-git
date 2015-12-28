package fadxapp.es.jagafo.services.xmlservice;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jdom2.Element;

import fadxapp.es.jagafo.controller.exceptions.ServiceException;
import fadxapp.es.jagafo.controller.exceptions.XMLException;
import fadxapp.es.jagafo.services.xmlservice.core.DocumentFactoryFacade;
import fadxapp.es.jagafo.services.xmlservice.elements.IXMLElement;
import fadxapp.es.jagafo.services.xmlservice.elements.IXmlSerializable;

/**
 * @author jagafo
 * @see IXMLService
 */
public class XMLService implements IXMLService {
	private static final String ENCODE_FILENAME = "Encode.xml";

	public IXMLElement parseToNodes(String path) throws Exception {
		return DocumentFactoryFacade.getInstance().getDocument(path)
				.getRootElement();
	}
	
	public String encodeToStr(Object o){
		if (o==null)
			return null;
		try {
			XMLEncoder encoder = new XMLEncoder(new FileOutputStream(System.getProperty("user.dir")+IOUtils.DIR_SEPARATOR+ENCODE_FILENAME));
			encoder.writeObject(o);
			encoder.close();
			return FileUtils.readFileToString(new File(System.getProperty("user.dir")+IOUtils.DIR_SEPARATOR+ENCODE_FILENAME));
		} catch (FileNotFoundException e) {
			new XMLException(e);
		} catch (IOException e) {
			new XMLException(e);
		}finally{
			File temp = new File(System.getProperty("user.dir")+IOUtils.DIR_SEPARATOR+ENCODE_FILENAME);
			if (temp.exists())
				temp.delete();
		}
		return null;
	}
	
	public Element encodeToElementDom(Object o){
		if (o==null)
			return null;
		if (o instanceof IXmlSerializable)
			return ((IXmlSerializable)o).encodeToXML();
		else
			new ServiceException(this,"El tipo "+o.getClass()+" no esta soportado");
		return null;
	}
	
	public Object decode(String xmlContent){
		if (xmlContent==null)
			return null;
		try {
			File encoded = new File(System.getProperty("user.dir")+IOUtils.DIR_SEPARATOR+ENCODE_FILENAME);
			FileUtils.writeStringToFile(encoded,xmlContent);
			XMLDecoder decoder = new XMLDecoder(new FileInputStream(encoded));
			Object result = decoder.readObject();
			decoder.close();
			return result;
		} catch (IOException e) {
			new XMLException(e);
		}finally{
			File encoded = new File(System.getProperty("user.dir")+IOUtils.DIR_SEPARATOR+ENCODE_FILENAME);
			if (encoded.exists())
				encoded.delete();
		}
		return null;
	}
}
