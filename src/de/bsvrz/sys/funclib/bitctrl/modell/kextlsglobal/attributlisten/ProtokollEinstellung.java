/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.attributlisten;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.sys.funclib.bitctrl.modell.Attributliste;

/**
 * Paar bestehend aus Name und Wert, über das Eigenschaften definiert werden
 * (z.&nbsp;B. "seriell.rtsVorlauf", "20").
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class ProtokollEinstellung implements Attributliste {

	private String name = "";
	private String wert = "";

	public ProtokollEinstellung() {
		// nix
	}

	public ProtokollEinstellung(final String name, final String wert) {
		this.name = name;
		this.wert = wert;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the wert
	 */
	public String getWert() {
		return wert;
	}

	/**
	 * @param wert
	 *            the wert to set
	 */
	public void setWert(final String wert) {
		this.wert = wert;
	}

	/**
	 * {@inheritDoc}
	 */
	public void atl2Bean(final Data daten) {
		setName(daten.getTextValue("Name").getText());
		setWert(daten.getTextValue("Wert").getText());
	}

	/**
	 * {@inheritDoc}
	 */
	public void bean2Atl(final Data daten) {
		daten.getTextValue("Name").setText(name);
		daten.getTextValue("Wert").setText(wert);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProtokollEinstellung clone() {
		try {
			return (ProtokollEinstellung) super.clone();
		} catch (final CloneNotSupportedException ex) {
			throw new IllegalStateException(ex);
		}
	}

}
