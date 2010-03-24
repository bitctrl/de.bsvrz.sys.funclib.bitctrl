package de.bsvrz.sys.funclib.bitctrl.util.monitor;

import com.bitctrl.util.monitor.AbstractMonitor;

import de.bsvrz.sys.funclib.debug.Debug;

public class DebugMonitor extends AbstractMonitor {

	private final Debug log = Debug.getLogger();
	private String name;
	private boolean canceled;
	private int leftWork;
	private double totalWork;
	private double allWork = 0.0;

	public void beginTask(final String name, final int totalWork) {
		this.name = name;
		log.info("DebugMonitor : " + name + " : beginTask(" + totalWork + ")");
		leftWork = totalWork;
		this.totalWork = totalWork;
	}

	public void done() {
		log.info("DebugMonitor : " + name + " : done");
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(final boolean canceled) {
		this.canceled = canceled;
		log.info("DebugMonitor : " + name + " : setCanceled(" + canceled + ")");
	}

	public void setTaskName(final String name) {
		log
				.info("DebugMonitor : " + this.name + " : setTaskName(" + name
						+ ")");
		this.name = name;

	}

	public void subTask(final String name) {
		log.fine("DebugMonitor : " + this.name + " : subTask(" + name + ")");
	}

	public void worked(final int work) {
		leftWork -= work;
		log.fine("DebugMonitor : " + name + " : worked(" + work + ") : "
				+ leftWork + " left");
		if (totalWork > 0) {
			allWork += work;
			notifyMonitorListeners(allWork / totalWork);
		}
	}

}
