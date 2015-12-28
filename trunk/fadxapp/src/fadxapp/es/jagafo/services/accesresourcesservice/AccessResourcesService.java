package fadxapp.es.jagafo.services.accesresourcesservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

import fadxapp.es.jagafo.controller.exceptions.ServiceException;
import fadxapp.es.jagafo.services.LoggableService;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.logservice.ILogService;
import fadxapp.es.jagafo.view.Activator;

/**
 * Servicio encargado de leer los ficheros de la aplicación
 * @author jagafo
 */
public class AccessResourcesService extends LoggableService implements IAccessResourcesService {
	
	private static final String DEFAULT_ICON_FOLDER = "icons/";
	private static final String DEFAULT_ICON_PATH = "defaultIcon.png";
	
	public AccessResourcesService(){
	}
	
	public File getFile(String recursoExterno) {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		try {
			URL urlResource = bundle.getEntry(recursoExterno);
			if (urlResource == null)
				if (new File(recursoExterno).exists())
					return new File(recursoExterno);
				else
					return null;
			URL urlFile = FileLocator.toFileURL(urlResource);
			if (urlFile != null)
				return new File(urlFile.toURI());
			else
				return null;
		} catch (URISyntaxException e) {
			new ServiceException(this,
					"Error accediendo al fichero " + recursoExterno, e);
			return null;
		} catch (IOException e) {
			new ServiceException(this,
					"Error accediendo al fichero " + recursoExterno, e);
			return null;
		}
	}

	public InputStream getResource(String recursoExterno) {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		InputStream is = null;
		try {
			is = bundle.getResource(recursoExterno).openStream();
		} catch (Exception e) {
			try {
				is = bundle.getResource(recursoExterno.toLowerCase())
						.openStream();
			} catch (Exception ex) {
				try {
					is = bundle.getResource(recursoExterno.toUpperCase())
							.openStream();
				} catch (Exception exc) {
					try {
						is = new FileInputStream(recursoExterno);
					} catch (Exception exce) {
						new ServiceException(this,
										"Error accediendo al recurso "
												+ recursoExterno, exc);
					}
				}
			}
		}
		return is;
	}

	public String readAllFile(String nombreFichero) {
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		try {
			InputStream is = bundle.getEntry(nombreFichero).openStream();
			StringBuffer sb = new StringBuffer();
			int chr;
			// lee hasta el final del stream
			while ((chr = is.read()) != -1)
				sb.append((char) chr);
			return sb.toString();
		} catch (IOException e) {
			new ServiceException(this,
				"Error al leer el fichero : " + nombreFichero,
				e);
			return null;
		}
	}

	public String readExternalFile(String nombreFichero) {
		StringBuilder contents = new StringBuilder();
		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(
					nombreFichero)));
			try {
				String line = null; // not declared within while loop
				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			new ServiceException(this,
				"Error al leer el fichero : " + nombreFichero,
				ex);
			return null;
		}
		return contents.toString();
	}

	public Image getImageFromRegistry(String pathImage) {
		Image result = null;
		if (pathImage != null && pathImage.trim().length()!=0)
			result = Activator.getDefault().getImageRegistry().get(pathImage);
		if (result == null) {
			if (pathImage == null || pathImage.trim().length() == 0) {
				result = Activator.getDefault().getImageRegistry().get(
						DEFAULT_ICON_FOLDER+DEFAULT_ICON_PATH);
				if (result == null) {
					result = getImage(DEFAULT_ICON_FOLDER
							+ DEFAULT_ICON_PATH);
					if (result != null)
						Activator.getDefault().getImageRegistry().put(
								DEFAULT_ICON_FOLDER+DEFAULT_ICON_PATH, result);
				}
				if (result == null) {
					getLogService().warn(
									this.getClass().getSimpleName(),
									"No se ha encontrado el icono de la clave "
									+ pathImage);
				}
			} else {
				result = getImage(pathImage);
				if (!result
						.equals(Activator.getDefault().getImageRegistry()
								.get(DEFAULT_ICON_FOLDER+DEFAULT_ICON_PATH)))
					Activator.getDefault().getImageRegistry().put(pathImage,
							result);
			}
		}
		return result;
	}

	public Image getImage(String nbImagen) {
		if (!nbImagen.startsWith(DEFAULT_ICON_FOLDER)
				&& !nbImagen.startsWith("/" + DEFAULT_ICON_FOLDER))
			nbImagen = DEFAULT_ICON_FOLDER + nbImagen;
		ImageDescriptor imageDesc = Activator.getImageDescriptor(nbImagen);
		if (imageDesc != null) {
			return imageDesc.createImage();
		} else {
			getLogService().warn(getClass()
					.getSimpleName(), "No se ha encontrado el icono "
					+ nbImagen);
			imageDesc = Activator.getImageDescriptor(DEFAULT_ICON_FOLDER
					+ DEFAULT_ICON_PATH);
			if (imageDesc != null) {
				return imageDesc.createImage();
			} else {
				getLogService().warn(
						getClass().getSimpleName(),
						"No se ha encontrado el icono por defecto "
								+ DEFAULT_ICON_FOLDER + DEFAULT_ICON_PATH);
				return PlatformUI.getWorkbench().getSharedImages().getImage(
						ISharedImages.IMG_OBJ_ELEMENT);
			}
		}
	}

	public ImageDescriptor getImageDescriptorFromRegistry(String pathImage) {
		ImageDescriptor result = null;
		if (pathImage != null && pathImage.trim().length()!=0)
		result = Activator.getDefault().getImageRegistry()
				.getDescriptor(pathImage);
		if (result == null) {
			if (pathImage == null || pathImage.trim().length() == 0) {
				result = Activator.getDefault().getImageRegistry()
				.getDescriptor(DEFAULT_ICON_FOLDER+DEFAULT_ICON_PATH);
				if (result == null) {
					result = getImageDescriptor(DEFAULT_ICON_FOLDER
							+ DEFAULT_ICON_PATH);
					if (result != null)
						Activator.getDefault().getImageRegistry().put(
								DEFAULT_ICON_FOLDER+DEFAULT_ICON_PATH, result);
				}
				if (result != null) {
					((ILogService) ServiceLocator
							.getService(ILogService.class)).warn(
									this.getClass().getSimpleName(),
									"No se ha encontrado el icono " + pathImage);
				}
			} else {
				result = getImageDescriptor(pathImage);
				if (!result
						.equals(Activator.getDefault().getImageRegistry()
								.get(DEFAULT_ICON_FOLDER+DEFAULT_ICON_PATH)))
					Activator.getDefault().getImageRegistry().put(pathImage,
							result);
			}
		}
		return result;
	}

	public ImageDescriptor getImageDescriptor(String nbImagen) {
		if (!nbImagen.startsWith(DEFAULT_ICON_FOLDER)
				&& !nbImagen.startsWith("/" + DEFAULT_ICON_FOLDER))
			nbImagen = DEFAULT_ICON_FOLDER + nbImagen;
		ImageDescriptor imageDesc = Activator.getImageDescriptor(nbImagen);
		if (imageDesc != null) {
			return imageDesc;
		} else {
			((ILogService) ServiceLocator
					.getService(ILogService.class)).warn(getClass()
					.getSimpleName(), "No se ha encontrado el icono "
					+ nbImagen);
			imageDesc = Activator.getImageDescriptor(DEFAULT_ICON_FOLDER
					+ DEFAULT_ICON_PATH);
			if (imageDesc != null) {
				return imageDesc;
			} else {
				((ILogService) ServiceLocator
						.getService(ILogService.class)).warn(this
						.getClass().getSimpleName(),
						"No se ha encontrado el icono por defecto "
								+ DEFAULT_ICON_FOLDER + DEFAULT_ICON_PATH);
				return PlatformUI.getWorkbench().getSharedImages()
						.getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT);
			}
		}
	}

	public boolean imageExists(String pathImage) {
		ImageDescriptor result = Activator.getDefault().getImageRegistry()
		.getDescriptor(pathImage);
		if (result != null)
			return true;
		if (pathImage == null || pathImage.trim().length() == 0)
			return false;
		if (!pathImage.startsWith(DEFAULT_ICON_FOLDER)
				&& !pathImage.startsWith("/" + DEFAULT_ICON_FOLDER))
			pathImage = DEFAULT_ICON_FOLDER + pathImage;
		if (Activator.getImageDescriptor(pathImage) == null)
			return false;
		return true;
	}
	
	public void disposeImageRegistry(){
		if (Activator.getDefault().getImageRegistry()!=null)
			Activator.getDefault().getImageRegistry().dispose();
	}
}
