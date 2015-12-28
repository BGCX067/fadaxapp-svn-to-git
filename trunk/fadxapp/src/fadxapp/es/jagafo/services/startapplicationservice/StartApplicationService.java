package fadxapp.es.jagafo.services.startapplicationservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.apppropertiesservice.IAppPropertyService;
import fadxapp.es.jagafo.services.logservice.ILogService;

/**
 * @see IStartApplicationService
 * @author jagafo
 */
public class StartApplicationService implements IStartApplicationService {
	private File controlFile;
	private FileLock lock;
	private FileChannel channel;

	public StartApplicationService() {
	}

	/**
	 * Esta funcion controla si ya hay una ejecucion activa de la aplicacion.
	 * Esto se realiza mediante el control de un fichero "lockeado".
	 * 
	 * @return True si hay una aplicacion activa, False en otro caso
	 */
	private boolean isAnotherInstanceRunning() {
		// Get a file channel for the file
		controlFile = new File("instanceControl");
		try {
			channel = new RandomAccessFile(controlFile, "rw").getChannel();
		} catch (FileNotFoundException e) {
			try {
				controlFile.createNewFile();
			} catch (IOException ex) {
				ex.printStackTrace();
				return false;
			}
			try {
				channel = new RandomAccessFile(controlFile, "rw").getChannel();
			} catch (FileNotFoundException ex) {
				// Por aqui no deberia entrar nunca para bloquear ya que nos
				// hemos encargado de que el fichero exista
			}
		}
		// Use the file channel to create a lock on the file.
		// This method blocks until it can retrieve the lock.
		try {
			lock = channel.tryLock();
			if (lock == null)
				return true;
		} catch (OverlappingFileLockException e) {
			// File is already locked in this thread or virtual machine
			return true;
		} catch (IOException e) {
			// Por aqui no deberia entrar tampoco
			e.printStackTrace();
		}
		return false;
	}

	public boolean start() {
		if (isAnotherInstanceRunning()) {
			MessageDialog.openError(new Shell(Display.getCurrent()), "Aviso",
					"Ya hay una instancia corriendo de la aplicacion");
			return false;
		}
		// Se inicia el servicio de propiedades
		ServiceLocator.getService(IAppPropertyService.class);
		// Obtiene la fecha y hora actual
		Date date = new Date();
		Format formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss");
		String s = formatter.format(date);

		((ILogService) ServiceLocator
				.getService(ILogService.class)).debug(getClass().getSimpleName(), "Arranca la aplicación" + " : "
				+ s);
		return true;
	}

	public void release() throws Exception {
		if (lock != null) {
			lock.release();
			channel.close();
			controlFile.delete();
		}
	}
}
