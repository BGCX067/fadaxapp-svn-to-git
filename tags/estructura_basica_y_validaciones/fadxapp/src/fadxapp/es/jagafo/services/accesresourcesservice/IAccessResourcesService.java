package fadxapp.es.jagafo.services.accesresourcesservice;

import java.io.File;
import java.io.InputStream;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import fadxapp.es.jagafo.services.IService;
import fadxapp.es.jagafo.services.ServiceImplAnnotation;

/**
 * 
 * Esta clase se encargará de leer todos los ficheros de la aplicación
 * 
 */
@ServiceImplAnnotation(serviceImpl=AccessResourcesService.class)
public interface IAccessResourcesService extends IService {
	/**
	 * Dado un recurso cuyo path se indica como parametro, devuelve el File
	 * asociado
	 * 
	 * @param nombreRecurso
	 * @return
	 */
	File getFile(String nombreRecurso);

	/**
	 * Dado un recurso (fichero de texto, foto, ...) que se quiera leer, se le
	 * indica como parámetro su nombre y devuelve un InputStream de su contenido
	 * 
	 * @param nombreRecurso
	 * @return devuelve un InputStream con el contenido del fichero
	 */
	InputStream getResource(String nombreRecurso);

	/**
	 * lee el fichero que se le pasa por argumento y devuelve su contenido
	 * 
	 * @param nombreFichero
	 *            nombre del fichero que queremos leer
	 * @return devuelve el contenido del fichero
	 */
	String readAllFile(String nombreFichero);

	/**
	 * lee el fichero que se le pasa por argumento y devuelve su contenido
	 * 
	 * @param filePath
	 * @return devuelve el contenido del fichero
	 */
	String readExternalFile(String filePath);

	/**
	 * Devuelve la imagen asociada a la clave que pasamos como valor, y además la guarda en el registro de imagenes de eclipse
	 * para una posterior carga rápida
	 * 
	 * @param imagePath Nombre de la ruta de la imagen
	 * @return Imagen SWT correspondiente a la clave
	 */
	Image getImageFromRegistry(String imagePath);
	
	/**
	 * Lee la imagen asociada a la ruta que pasamos como parametro. Por defecto
	 * la busca en 'icons/'. Si la imagen no existe se devuelve una imagen por
	 * defecto, que si no existe se devuelve una imagen interna de eclipse
	 * 
	 * @param nbImagen
	 *            Ruta del archivo a leer
	 * @return
	 */
	Image getImage(String nbImagen);

	/**
	 * Devuelve el imageDescriptor asociada a la clave que pasamos como valor y además la guarda en el registro de imagenes de eclipse
	 * para su carga rápida
	 * 
	 * @param imagePath Ruta de la imagen en la aplicacion
	 * @return ImageDescriptor
	 */
	ImageDescriptor getImageDescriptorFromRegistry(String imagePath);
	
	/**
	 * Lee el descriptor asociado a la ruta que pasamos como parametro. Por
	 * defecto la busca en 'icons/'. Si la imagen no existe se devuelve un
	 * descriptor por defecto, que si no existe se devuelve un descriptor
	 * interna de eclipse
	 * 
	 * @param nbImagen
	 *            Ruta del archivo a leer
	 * @return
	 */
	ImageDescriptor getImageDescriptor(String nbImagen);

	/**
	 * Comprueba si la imagen existe en nuestra aplicacion, es decir si esta
	 * dada de alta dentro del fichero xml de rutas de imagenes y que ademas el
	 * recurso existe en nuestra aplicacion
	 * @param imagePath Ruta de la imagen en la aplicacion
	 * @return True si existe, false en otro caso
	 */
	boolean imageExists(String imagePath);
	
	/**
	 * Limpiamos las imagenes registradas si es necesario
	 */
	void disposeImageRegistry();
}
