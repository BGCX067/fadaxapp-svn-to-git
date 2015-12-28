package fadxapp.es.jagafo.controller.commands.appconfiguration;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Comando para controlar el estado de los menus que contienen un check
 */
public class WindowTrayControlCommand implements IHandler {
	public static final String ID = "es.hercules.repository.commandTrayControl";

	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	public void dispose() {
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		HandlerUtil.toggleCommandState(event.getCommand());
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isHandled() {
		return true;
	}

	public void removeHandlerListener(IHandlerListener handlerListener) {
	}
}
