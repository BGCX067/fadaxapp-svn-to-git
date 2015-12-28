package fadxapp.es.jagafo.controller.jobs.uijobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import fadxapp.es.jagafo.controller.jobs.GeneralJob;
import fadxapp.es.jagafo.utils.RCPUtils;

/**
 * Job encargado de mostrar imagenes en el tray utiliza una escheluding rule
 * para acceder al valor
 * @author jagafo
 */
public class ShowBalloonToolTipUIJob extends GeneralJob {
	private static final Long BLOCK_TIME = 7000L;
	
	private static ISchedulingRule rule = new MutexRule();
	private String title;
	private String toolTip;
	private int style;
	
	public ShowBalloonToolTipUIJob(String title,String toolTip,int style){
		super(ShowBalloonToolTipUIJob.class.getSimpleName());
		setRule(rule);
		setUser(false);
		setSystem(true);
		this.title = title;
		this.toolTip = toolTip;
		this.style = style;
	}

	@Override
	public IStatus especificJobRun(IProgressMonitor monitor) {
		RCPUtils.showBalloonToolTipTaryIcon(title, toolTip, style);
		try {
			Thread.sleep(BLOCK_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Status.OK_STATUS;
	}
}
class MutexRule implements ISchedulingRule{

	public boolean contains(ISchedulingRule rule) {
		return this.equals(rule);
	}

	public boolean isConflicting(ISchedulingRule rule) {
		return this.equals(rule);
	}
}

