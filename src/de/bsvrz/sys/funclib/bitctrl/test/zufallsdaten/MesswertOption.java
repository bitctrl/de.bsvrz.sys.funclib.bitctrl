/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Kapselt einen Messwert und seine Optionen. Optionen sind derzeit nur Minimum
 * und Maximum des Wertebereichs in dem Testdaten generiert werden sollen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
@SuppressWarnings("serial")
public class MesswertOption extends JPanel implements ChangeListener {

	/** Liste angemeldeter Listener. */
	private final EventListenerList listeners = new EventListenerList();

	/** Stellt das Minimum ein. */
	private JSlider sldMin;

	/** Stellt das Maximum ein. */
	private JSlider sldMax;

	/** Zeigt den eingstellten Wert des Minimum. */
	private JLabel lblMinWert;

	/** Zeigt den eingstellten Wert des Maximum. */
	private JLabel lblMaxWert;

	/**
	 * Erzeugt einen Messwert.
	 * 
	 * @param name
	 *            Name des Messwerts
	 * @param min
	 *            Minimum des Wertes
	 * @param max
	 *            Maximum des Wertes
	 */
	public MesswertOption(String name, int min, int max) {
		if (name == null || name.equals("")) {
			throw new IllegalArgumentException(
					"Der Name darf weder noch null noch leer sein.");
		}
		if (min > max) {
			throw new IllegalArgumentException(
					"Das Minimum darf nicht größer als das Maximum sein.");
		}

		GridBagLayout layout;
		JLabel lblMin, lblMax;

		setName(name);
		layout = new GridBagLayout();
		setLayout(layout);

		setBorder(new TitledBorder(name));

		lblMin = new JLabel("Min");
		lblMinWert = new JLabel(String.valueOf(min));
		lblMax = new JLabel("Max");
		lblMaxWert = new JLabel(String.valueOf(max));

		sldMin = new JSlider(SwingConstants.VERTICAL, min, max, min);
		sldMin.setPaintTicks(true);
		sldMin.setMinorTickSpacing((max - min) / 10);
		sldMin.addChangeListener(this);

		sldMax = new JSlider(SwingConstants.VERTICAL, min, max, max);
		sldMax.setPaintTicks(true);
		sldMax.setMinorTickSpacing((max - min) / 10);
		sldMax.addChangeListener(this);

		add(layout, lblMin, 0, 0, 1, 1);
		add(layout, lblMax, 1, 0, 1, 1);
		add(layout, lblMinWert, 0, 1, 1, 1);
		add(layout, lblMaxWert, 1, 1, 1, 1);
		add(layout, sldMin, 0, 2, 1, 5);
		add(layout, sldMax, 1, 2, 1, 5);
	}

	/**
	 * Gibt das aktuell eingestellte Minimum zur&uuml;ck.
	 * 
	 * @return Das aktuelle Minimum
	 */
	public int getMin() {
		return sldMin.getValue();
	}

	/**
	 * Gibt das aktuell eingestellte Maximum zur&uuml;ck.
	 * 
	 * @return Das aktuelle Maximum
	 */
	public int getMax() {
		return sldMax.getValue();
	}

	/**
	 * Registriert einen Listener.
	 * 
	 * @param listener
	 *            Der neue Listener
	 */
	public void addMesswertOptionListener(MesswertOptionListener listener) {
		listeners.add(MesswertOptionListener.class, listener);
	}

	/**
	 * Entfernt einen Listener wieder aus der Liste registrierter Listener.
	 * 
	 * @param listener
	 *            Listener der abgemeldet werden soll
	 */
	public void removeMesswertOptionListener(MesswertOptionListener listener) {
		listeners.remove(MesswertOptionListener.class, listener);
	}

	/**
	 * Reagiert auf &Auml;nderung von Minimum und Maximum &uuml;ber die beiden
	 * Slider.
	 * <p>
	 * {@inheritDoc}
	 */
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() instanceof JSlider) {
			if (e.getSource() == sldMin) {
				lblMinWert.setText(String.valueOf(sldMin.getValue()));
				if (sldMin.getValue() > sldMax.getValue()) {
					sldMax.setValue(sldMin.getValue());
				}
				fireNeuesMinimum();
			}
			if (e.getSource() == sldMax) {
				lblMaxWert.setText(String.valueOf(sldMax.getValue()));
				if (sldMax.getValue() < sldMin.getValue()) {
					sldMin.setValue(sldMax.getValue());
				}
				fireNeuesMaximum();
			}
		}
	}

	/**
	 * Gibt den Namen des Werts und dessen eingestelltes Minimum und Maximum
	 * zur&uuml;ck.
	 */
	@Override
	public String toString() {
		return getName() + "[" + sldMin.getValue() + ", " + sldMax.getValue()
				+ "]";
	}

	/**
	 * Generiert ein Event, welches ein neues Minimum signalisiert.
	 */
	protected synchronized void fireNeuesMinimum() {
		MesswertOptionEvent event;

		event = new MesswertOptionEvent(this);

		for (MesswertOptionListener l : listeners
				.getListeners(MesswertOptionListener.class)) {
			l.minimumAktualisiert(event);
		}
	}

	/**
	 * Generiert ein Event, welches ein neues Maximum signalisiert.
	 */
	protected synchronized void fireNeuesMaximum() {
		MesswertOptionEvent event;

		event = new MesswertOptionEvent(this);

		for (MesswertOptionListener l : listeners
				.getListeners(MesswertOptionListener.class)) {
			l.maximumAktualisiert(event);
		}
	}

	/**
	 * Hilfsfunktion zum Umgang mit Constraints beim GridBagLayout.
	 * 
	 * @param layout
	 *            Das Layout, dem eine Komponente hinzugef&uuml;gt werden soll
	 * @param component
	 *            Neue Komponente die hinzuge&uuml;gt werden soll
	 * @param x
	 *            Die horizontale Position der Komponente
	 * @param y
	 *            Die vertikale Position der Komponente
	 * @param breite
	 *            Die Breite der Komponente
	 * @param hoehe
	 *            Die H&ouml;he der Komponente
	 */
	private void add(GridBagLayout layout, Component component, int x, int y,
			int breite, int hoehe) {
		GridBagConstraints constraints;

		constraints = new GridBagConstraints();
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = breite;
		constraints.gridheight = hoehe;

		layout.setConstraints(component, constraints);
		add(component);
	}

}
