package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Anzeige extends JPanel {

	JLabel lblWert;

	public Anzeige(String messwert, String einheit) {
		super(new FlowLayout());

		JLabel lblMesswert, lblEinheit;

		lblMesswert = new JLabel(messwert);
		lblWert = new JLabel(" - ");
		lblEinheit = new JLabel(einheit);

		add(lblMesswert);
		add(lblWert);
		add(lblEinheit);
	}

	public void setWert(Integer wert) {
		if (wert == null) {
			lblWert.setText(" - ");
		} else {
			lblWert.setText(" " + wert + " ");
		}
	}

}
