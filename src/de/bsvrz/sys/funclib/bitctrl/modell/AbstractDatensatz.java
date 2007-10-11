package de.bsvrz.sys.funclib.bitctrl.modell;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.Data;

public abstract class AbstractDatensatz implements Datensatz {

	private final EventListenerList listeners = new EventListenerList();
	private boolean autoUpdate;
	private boolean valid;

	public void addUpdateListener(DatensatzUpdateListener listener) {
		listeners.add(DatensatzUpdateListener.class, listener);
	}

	protected abstract void fireAutoUpdate();

	protected void fireDatensatzAktualisiert() {
		DatensatzUpdateEvent event = new DatensatzUpdateEvent(getObjekt(), this);
		for (DatensatzUpdateListener listener : listeners
				.getListeners(DatensatzUpdateListener.class)) {
			listener.datensatzAktualisiert(event);
		}
	}

	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	public boolean isValid() {
		return valid;
	}

	public void removeUpdateListener(DatensatzUpdateListener listener) {
		listeners.remove(DatensatzUpdateListener.class, listener);
	}

	public void setAutoUpdate(boolean ein) {
		autoUpdate = ein;
		fireAutoUpdate();
	}

	protected void setValid(boolean valid) {
		this.valid = valid;
	}
}
