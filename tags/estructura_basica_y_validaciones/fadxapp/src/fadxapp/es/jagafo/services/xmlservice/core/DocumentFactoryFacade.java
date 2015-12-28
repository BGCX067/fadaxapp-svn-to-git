package fadxapp.es.jagafo.services.xmlservice.core;

import fadxapp.es.jagafo.controller.exceptions.ServiceException;

/**
 * Define la factoria de documentos de la aplicacion
 * @author karsaorlong
 *
 */
public class DocumentFactoryFacade {
	static public IDocumentFactory instance;

	@SuppressWarnings({ "rawtypes" })
	public static IDocumentFactory getInstance() {
		if (instance == null) {
			try {
				Class f = DocumentFactory.class;
				instance = (IDocumentFactory) f.newInstance();
			} catch (Exception e) {
				new ServiceException(DocumentFactoryFacade.class,"Error instanciando: ",e);
			}
		}
		return instance;
	}
}
