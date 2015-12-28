package fadxapp.es.jagafo.controller.jobs.uijobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

import fadxapp.es.jagafo.controller.jobs.GeneralJob;
import fadxapp.es.jagafo.controller.jobs.appjobs.GeneralAppJob;
import fadxapp.es.jagafo.services.ServiceLocator;
import fadxapp.es.jagafo.services.accesresourcesservice.IAccessResourcesService;
import fadxapp.es.jagafo.utils.RCPUtils;

/**
 * Job encargado de mostrar imagenes en el tray utiliza una escheluding rule para acceder al valor
 * @author jagafo
 */
public class AnimateImageTrayUIJob extends GeneralJob {
	// Familia para controlar que no se ejecute otro job de esta familia
	public static final String FAMILY = "ANIMATE_TRAY_FAMILY";
	private static final long DELAY_REFRESH = 500;
	private ImageData[] imageDataArray;
	private ImageLoader loader;
	
	public AnimateImageTrayUIJob(String filePath){
		super(AnimateImageTrayUIJob.class.getSimpleName());
		setUser(false);
		setSystem(true);
		setFamily(FAMILY);
		loader = new ImageLoader();
		imageDataArray = loader.load(((IAccessResourcesService)ServiceLocator.getService(IAccessResourcesService.class)).getResource(filePath));
		setPriority(DECORATE);
	}
	
	public boolean belongsTo(Object family){
		return family.equals(FAMILY);
	}

	@Override
	public IStatus especificJobRun(IProgressMonitor monitor) {
		while (!monitor.isCanceled() && Job.getJobManager().find(GeneralAppJob.FAMILY).length!=0){
			for (int imageDataIndex=0; !monitor.isCanceled() && imageDataIndex < imageDataArray.length; imageDataIndex++){
				final ImageData imageData = imageDataArray[imageDataIndex];
				RCPUtils.changeTrayIcon(new Image(Display.getDefault(),imageData));
				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;
				try {
//					int ms = imageData.delayTime * 500;
//					if (ms < 20) ms += 30;
//					if (ms < 30) ms += 10;
					long i = 0;
					while (i < DELAY_REFRESH && !monitor.isCanceled()){
						Thread.sleep(1);
						i++;
					}
				} catch (InterruptedException e) {
				}
			}
		}
		return Status.OK_STATUS;
	}
}

