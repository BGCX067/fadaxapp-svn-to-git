package fadxapp.es.jagafo.view.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;

/**
 * Define el ediotrInput para TODOS los editores de la aplicaicon
 */
public class GenericEditorInput implements IEditorInput {

	private String name;

	private String keyImagen;

	/**
	 * crea un editor Creates a chat editor input on the given session and name.
	 * 
	 * @param name
	 * @param nombreImagen
	 */
	public GenericEditorInput(String name, String keyImagen) {
		if (name == null)
			name = "Unknown";
		if (name.length() > 250) {
			name = name.substring(0, 250) + " ....";
		}
		this.name = name.trim();
		this.keyImagen = keyImagen;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		Image image = ((IAccessResourcesService) ServiceLocator
				.getService(IAccessResourcesService.class))
				.getImageFromRegistry(keyImagen);
		return ImageDescriptor.createFromImage(image);
	}

	public String getName() {
		return name;
	}

	public String getToolTipText() {
		return getName();
	}

	public IPersistableElement getPersistable() {
		// Not persistable between sessions
		return null;
	}

	@SuppressWarnings({ "rawtypes" })
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * Antes de abrir cualquier editor, se comprueba con los editores abiertos
	 * si ya hay alguno igual al que se pretende abrir. Se ha sobreescrito este
	 * método para realizar esta comprobación.
	 * 
	 * @param obj
	 * @return
	 */

	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (!(obj instanceof GenericEditorInput))
			return false;
//		if (name.equals(NewBuildEditor.ID) || name.equals(ResultsTreeEditor.ID) || name.equals(GraphEditor.ID)) 
//			return false;
		GenericEditorInput other = (GenericEditorInput) obj;
		return this.name.equals(other.name);
	}
}