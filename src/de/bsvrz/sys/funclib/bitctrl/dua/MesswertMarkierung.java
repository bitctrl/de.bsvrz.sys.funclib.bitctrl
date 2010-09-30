/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua;

import com.bitctrl.Constants;

/**
 * Klasse, die alle Markierungen eines Messwertes speichert.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id: MesswertMarkierung.java 8054 2008-04-09 15:11:59Z tfelder $
 */
public class MesswertMarkierung implements Cloneable {

	/**
	 * der Wert von <code>*.Status.Erfassung.NichtErfasst</code>.
	 */
	protected boolean nichtErfasst = false;

	/**
	 * der Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>.
	 */
	protected boolean implausibel = false;

	/**
	 * der Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>.
	 */
	protected boolean interpoliert = false;

	/**
	 * der Wert von <code>*.Status.PlFormal.WertMax</code>.
	 */
	protected boolean formalMax = false;

	/**
	 * der Wert von <code>*.Status.PlFormal.WertMin</code>.
	 */
	protected boolean formalMin = false;

	/**
	 * der Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>.
	 */
	protected boolean logischMax = false;

	/**
	 * der Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>.
	 */
	protected boolean logischMin = false;

	/**
	 * zeigt an, ob eine der Setter-Methoden benutzt wurde.
	 */
	protected boolean veraendert = false;

	/**
	 * Erfragt den Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>.
	 * 
	 * @return der Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 */
	public final boolean isInterpoliert() {
		return this.interpoliert;
	}

	/**
	 * Setzt den Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>.
	 * 
	 * @param interpoliert
	 *            der Wert von
	 *            <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 */
	public final void setInterpoliert(boolean interpoliert) {
		this.veraendert = true;
		this.interpoliert = interpoliert;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>.
	 * 
	 * @return der Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>
	 */
	public final boolean isImplausibel() {
		return this.implausibel;
	}

	/**
	 * Setzt den Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>.
	 * 
	 * @param implausibel
	 *            der Wert von
	 *            <code>*.Status.MessWertErsetzung.Implausibel</code>
	 */
	public final void setImplausibel(boolean implausibel) {
		this.veraendert = true;
		this.implausibel = implausibel;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.Erfassung.NichtErfasst</code>.
	 * 
	 * @return der Wert von <code>*.Status.Erfassung.NichtErfasst</code>
	 */
	public final boolean isNichtErfasst() {
		return this.nichtErfasst;
	}

	/**
	 * Setzt den Wert von <code>*.Status.Erfassung.NichtErfasst</code>.
	 * 
	 * @param nichtErfasst
	 *            der Wert von <code>*.Status.Erfassung.NichtErfasst</code>
	 */
	public final void setNichtErfasst(boolean nichtErfasst) {
		this.veraendert = true;
		this.nichtErfasst = nichtErfasst;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.PlFormal.WertMax</code>.
	 * 
	 * @return den Wert von <code>*.Status.PlFormal.WertMax</code>
	 */
	public final boolean isFormalMax() {
		return formalMax;
	}

	/**
	 * Setzt den Wert von <code>*.Status.PlFormal.WertMax</code>.
	 * 
	 * @param formalMax
	 *            der Wert von <code>*.Status.PlFormal.WertMax</code>
	 */
	public final void setFormalMax(boolean formalMax) {
		this.veraendert = true;
		this.formalMax = formalMax;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.PlFormal.WertMin</code>.
	 * 
	 * @return den Wert von <code>*.Status.PlFormal.WertMin</code>
	 */
	public final boolean isFormalMin() {
		return formalMin;
	}

	/**
	 * Setzt den Wert von <code>*.Status.PlFormal.WertMin</code>.
	 * 
	 * @param formalMin
	 *            der Wert von <code>*.Status.PlFormal.WertMin</code>
	 */
	public final void setFormalMin(boolean formalMin) {
		this.veraendert = true;
		this.formalMin = formalMin;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>.
	 * 
	 * @return den Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>
	 */
	public final boolean isLogischMax() {
		return logischMax;
	}

	/**
	 * Setzt den Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>.
	 * 
	 * @param logischMax
	 *            der Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>
	 */
	public final void setLogischMax(boolean logischMax) {
		this.veraendert = true;
		this.logischMax = logischMax;
	}

	/**
	 * Erfragt den Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>.
	 * 
	 * @return der Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>
	 */
	public final boolean isLogischMin() {
		return logischMin;
	}

	/**
	 * Setzt den Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>.
	 * 
	 * @param logischMin
	 *            der Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>
	 */
	public final void setLogischMin(boolean logischMin) {
		this.veraendert = true;
		this.logischMin = logischMin;
	}

	/**
	 * Erfragt, ob dieser Wert veraendert wurde.
	 * 
	 * @return ob dieser Wert veraendert wurde
	 */
	public final boolean isVeraendert() {
		return this.veraendert;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		boolean gleich = false;

		if (obj instanceof MesswertMarkierung) {
			MesswertMarkierung that = (MesswertMarkierung) obj;
			gleich = this.nichtErfasst == that.nichtErfasst
					&& this.implausibel == that.implausibel
					&& this.interpoliert == that.interpoliert
					&& this.formalMax == that.formalMax
					&& this.formalMin == that.formalMin
					&& this.logischMax == that.logischMax
					&& this.logischMin == that.logischMin;
		}

		return gleich;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (this.nichtErfasst ? "nErf " : Constants.EMPTY_STRING) + //$NON-NLS-1$
				(this.formalMax ? "fMax " : Constants.EMPTY_STRING) + //$NON-NLS-1$
				(this.formalMin ? "fMin " : Constants.EMPTY_STRING) + //$NON-NLS-1$
				(this.logischMax ? "lMax " : Constants.EMPTY_STRING) + //$NON-NLS-1$
				(this.logischMin ? "lMin " : Constants.EMPTY_STRING) + //$NON-NLS-1$
				(this.implausibel ? "Impl " : Constants.EMPTY_STRING) + //$NON-NLS-1$
				(this.interpoliert ? "Intp " : Constants.EMPTY_STRING); //$NON-NLS-1$
	}
}
