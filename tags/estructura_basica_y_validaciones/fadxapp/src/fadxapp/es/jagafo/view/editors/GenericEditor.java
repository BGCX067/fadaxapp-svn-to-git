package fadxapp.es.jagafo.view.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.logservice.ILogService;

/**
 * Todo editor de la aplicación extenderá de este editor
 */
public abstract class GenericEditor extends EditorPart {
	protected static ILogService logService = ((ILogService) ServiceLocator
			.getService(ILogService.class));
	private boolean dirty = false;

	public GenericEditor() {
	}

	protected EditorPart getEditor() {
		return this;
	}

	protected void setDirty(boolean b) {
		dirty = b;
		super.firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		if (!(input instanceof GenericEditorInput))
			throw new PartInitException(
					"GenericEditor expects a GenerciEditorInput.  Actual input: "
							+ input);
		setSite(site);
		setInput(input);
	}

	public boolean isDirty() {
		return dirty;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}
	
	public void doSave(IProgressMonitor monitor) {
	}

	public void doSaveAs() {
	}

	public void setFocus() {
	}
}
