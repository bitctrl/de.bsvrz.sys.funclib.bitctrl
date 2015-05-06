/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2009 BitCtrl Systems GmbH 
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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua;

import de.bsvrz.dav.daf.main.Data;

/**
 * Messwert <b>für ein Attribut</b> mit Plausibilisierungsinformationen.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id: AbstraktMesswert.java 8054 2008-04-09 15:11:59Z tfelder $
 */
public abstract class AbstraktMesswert extends MesswertMarkierung
implements Comparable<AbstraktMesswert> {

	/**
	 * Der Attributname dieses Messwertes.
	 */
	private String attName = null;

	/**
	 * der Messwert als <code>double</code>.
	 */
	private double wertSkaliert = -4;

	/**
	 * der Messwert als <code>long</code>.
	 */
	private long wertUnskaliert = -4;

	/**
	 * der Guete-Index.
	 */
	private GanzZahl guete = GanzZahl.getGueteIndex();

	/**
	 * das Guete-Verfahren.
	 */
	private int verfahren = 0;

	/**
	 * Standardkonstruktor.
	 *
	 * @param attName
	 *            der Attributname dieses Messwertes
	 * @param datum
	 *            das Datum aus dem der Messwert ausgelesen werden soll
	 */
	public AbstraktMesswert(final String attName, final Data datum) {
		if (attName == null) {
			throw new NullPointerException("Der Attributname ist <<null>>"); //$NON-NLS-1$
		}
		if (datum == null) {
			throw new NullPointerException("Das Datum ist <<null>>"); //$NON-NLS-1$
		}
		this.attName = attName;

		if (!isSkaliert()) {
			wertUnskaliert = datum.getItem(attName).getUnscaledValue("Wert") //$NON-NLS-1$
					.longValue();
		}

		nichtErfasst = datum.getItem(attName).getItem("Status") //$NON-NLS-1$
				.getItem("Erfassung").//$NON-NLS-1$
				getUnscaledValue("NichtErfasst").intValue() == DUAKonstanten.JA; //$NON-NLS-1$
		formalMax = datum.getItem(attName).getItem("Status").getItem("PlFormal") //$NON-NLS-1$ //$NON-NLS-2$
				.getUnscaledValue("WertMax").intValue() == DUAKonstanten.JA; //$NON-NLS-1$
		formalMin = datum.getItem(attName).getItem("Status").getItem("PlFormal") //$NON-NLS-1$ //$NON-NLS-2$
				.getUnscaledValue("WertMin").intValue() == DUAKonstanten.JA; //$NON-NLS-1$

		logischMax = datum.getItem(attName).getItem("Status") //$NON-NLS-1$
				.getItem("PlLogisch").//$NON-NLS-1$
				getUnscaledValue("WertMaxLogisch") //$NON-NLS-1$
				.intValue() == DUAKonstanten.JA;
		logischMin = datum.getItem(attName).getItem("Status") //$NON-NLS-1$
				.getItem("PlLogisch").//$NON-NLS-1$
				getUnscaledValue("WertMinLogisch") //$NON-NLS-1$
				.intValue() == DUAKonstanten.JA;

		implausibel = datum.getItem(attName).getItem("Status") //$NON-NLS-1$
				.getItem("MessWertErsetzung").//$NON-NLS-1$
				getUnscaledValue("Implausibel").intValue() == DUAKonstanten.JA; //$NON-NLS-1$
		interpoliert = datum.getItem(attName).getItem("Status") //$NON-NLS-1$
				.getItem("MessWertErsetzung").//$NON-NLS-1$
				getUnscaledValue("Interpoliert").intValue() == DUAKonstanten.JA; //$NON-NLS-1$

		guete.setWert(datum.getItem(attName).getItem("Güte") //$NON-NLS-1$
				.getUnscaledValue("Index").longValue()); //$NON-NLS-1$
		verfahren = datum.getItem(attName).getItem("Güte") //$NON-NLS-1$
				.getUnscaledValue("Verfahren").intValue(); //$NON-NLS-1$
	}

	/**
	 * Standardkonstruktor.
	 *
	 * @param attName
	 *            der Attributname dieses Messwertes
	 */
	public AbstraktMesswert(final String attName) {
		this.attName = attName;
	}

	/**
	 * Erfragt, ob es sich um einen Wert handelt, der skaliert gelesen bzw.
	 * geschrieben werden soll
	 *
	 * @return ob es sich um einen Wert handelt, der skaliert gelesen bzw.
	 *         geschrieben werden soll
	 */
	public abstract boolean isSkaliert();

	/**
	 * Erfragt die Guete dieses Attributwertes.
	 *
	 * @return die Guete dieses Attributwertes
	 */
	public final GanzZahl getGueteIndex() {
		return guete;
	}

	/**
	 * Setzte die Guete dieses Attributwertes.
	 *
	 * @param guete1
	 *            die Guete dieses Attributwertes
	 */
	public final void setGueteIndex(final GanzZahl guete1) {
		guete = guete1;
	}

	/**
	 * Erfragt das Gueteverfahren.
	 *
	 * @return das Gueteverfahren
	 */
	public final int getVerfahren() {
		return verfahren;
	}

	/**
	 * Setzt das Gueteverfahren.
	 *
	 * @param verfahren
	 *            das Gueteverfahren
	 */
	public final void setVerfahren(final int verfahren) {
		this.verfahren = verfahren;
	}

	/**
	 * Setzt den skalierten Attributwert.
	 *
	 * @param wert
	 *            der skalierte Attributwert
	 */
	public final void setWertSkaliert(final double wert) {
		wertSkaliert = wert;
	}

	/**
	 * Erfragt den skalierten Attributwert.
	 *
	 * @return den skalierten Attributwert
	 */
	public final double getWertSkaliert() {
		return wertSkaliert;
	}

	/**
	 * Setzt den unskalierte Attributwert.
	 *
	 * @param wert
	 *            der unskalierte Attributwert
	 */
	public final void setWertUnskaliert(final long wert) {
		wertUnskaliert = wert;
	}

	/**
	 * Erfragt den unskalierten Attributwert.
	 *
	 * @return der unskalierte Attributwert
	 */
	public final long getWertUnskaliert() {
		return wertUnskaliert;
	}

	/**
	 * Erfragt, ob dieser Messwert entweder <code>fehlerhaft</code>,
	 * <code>nicht ermittelbar/fehlerhaft</code> oder <code>implausibel</code>
	 * ist.
	 *
	 * @return ob dieser Messwert entweder <code>fehlerhaft</code>,
	 *         <code>nicht ermittelbar/fehlerhaft</code> oder
	 *         <code>implausibel</code> ist
	 */
	public final boolean isFehlerhaftBzwImplausibel() {
		return wertUnskaliert == DUAKonstanten.FEHLERHAFT
				|| wertUnskaliert == DUAKonstanten.NICHT_ERMITTELBAR_BZW_FEHLERHAFT
				|| implausibel;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(final AbstraktMesswert that) {
		return isSkaliert()
				? new Double(getWertSkaliert())
						.compareTo(that.getWertSkaliert())
						: new Long(getWertUnskaliert())
						.compareTo(that.getWertUnskaliert());
	}

	/**
	 * Kopiert den Inhalt dieses Objektes in das übergebene Datum.
	 *
	 * @param datum
	 *            ein veränderbares Datum
	 */
	public final void kopiereInhaltNach(final Data datum) {
		if (isSkaliert()) {
			datum.getItem(attName).getScaledValue("Wert").set(wertSkaliert); //$NON-NLS-1$
		} else {
			if (DUAUtensilien.isWertInWerteBereich(
					datum.getItem(attName).getItem("Wert"), wertUnskaliert)) { //$NON-NLS-1$
				datum.getItem(attName).getUnscaledValue("Wert") //$NON-NLS-1$
				.set(wertUnskaliert);
			} else {
				datum.getItem(attName).getUnscaledValue("Wert") //$NON-NLS-1$
				.set(DUAKonstanten.NICHT_ERMITTELBAR_BZW_FEHLERHAFT);
			}
		}

		datum.getItem(attName).getItem("Status").getItem("Erfassung").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("NichtErfasst") //$NON-NLS-1$
		.set(nichtErfasst ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("PlFormal").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMax") //$NON-NLS-1$
		.set(formalMax ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("PlFormal").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMin") //$NON-NLS-1$
		.set(formalMin ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("PlLogisch").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMaxLogisch") //$NON-NLS-1$
		.set(logischMax ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("PlLogisch").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMinLogisch") //$NON-NLS-1$
		.set(logischMin ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("Implausibel") //$NON-NLS-1$
		.set(implausibel ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("Interpoliert") //$NON-NLS-1$
		.set(interpoliert ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Güte").getUnscaledValue("Index") //$NON-NLS-1$ //$NON-NLS-2$
		.set(guete.getWert());
		datum.getItem(attName).getItem("Güte").getUnscaledValue("Verfahren") //$NON-NLS-1$ //$NON-NLS-2$
		.set(verfahren);
	}

	/**
	 * Kopiert den Inhalt dieses Objektes in das übergebene Datum.
	 *
	 * @param datum
	 *            ein veränderbares Datum
	 */
	public final void kopiereInhaltNachModifiziereIndex(final Data datum) {
		if (isSkaliert()) {
			datum.getItem(attName).getScaledValue("Wert").set(wertSkaliert); //$NON-NLS-1$
		} else {
			if (DUAUtensilien.isWertInWerteBereich(
					datum.getItem(attName).getItem("Wert"), wertUnskaliert)) { //$NON-NLS-1$
				datum.getItem(attName).getUnscaledValue("Wert") //$NON-NLS-1$
				.set(wertUnskaliert);
			} else {
				datum.getItem(attName).getUnscaledValue("Wert") //$NON-NLS-1$
				.set(DUAKonstanten.NICHT_ERMITTELBAR_BZW_FEHLERHAFT);
			}
		}

		datum.getItem(attName).getItem("Status").getItem("Erfassung").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("NichtErfasst") //$NON-NLS-1$
		.set(nichtErfasst ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("PlFormal").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMax") //$NON-NLS-1$
		.set(formalMax ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("PlFormal").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMin") //$NON-NLS-1$
		.set(formalMin ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("PlLogisch").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMaxLogisch") //$NON-NLS-1$
		.set(logischMax ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("PlLogisch").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("WertMinLogisch") //$NON-NLS-1$
		.set(logischMin ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("Implausibel") //$NON-NLS-1$
		.set(implausibel ? DUAKonstanten.JA : DUAKonstanten.NEIN);
		datum.getItem(attName).getItem("Status").getItem("MessWertErsetzung").//$NON-NLS-1$ //$NON-NLS-2$
				getUnscaledValue("Interpoliert") //$NON-NLS-1$
		.set(interpoliert ? DUAKonstanten.JA : DUAKonstanten.NEIN);

		if (datum.getItem(attName).getUnscaledValue("Wert").longValue() < 0) { //$NON-NLS-1$
			datum.getItem(attName).getItem("Güte").getUnscaledValue("Index") //$NON-NLS-1$ //$NON-NLS-2$
			.set(0);
		} else {
			datum.getItem(attName).getItem("Güte").getUnscaledValue("Index") //$NON-NLS-1$ //$NON-NLS-2$
			.set(guete.getWert());
		}
		datum.getItem(attName).getItem("Güte").getUnscaledValue("Verfahren") //$NON-NLS-1$ //$NON-NLS-2$
		.set(verfahren);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		boolean gleich = false;

		if (obj instanceof AbstraktMesswert) {
			final AbstraktMesswert that = (AbstraktMesswert) obj;

			gleich = super.equals(obj)
					&& getWertUnskaliert() == that.getWertUnskaliert()
					&& guete.equals(that.guete);
		}

		return gleich;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return (isSkaliert() ? getWertSkaliert() : getWertUnskaliert()) + " " //$NON-NLS-1$
				+ super.toString() + " " + guete.getSkaliertenWert() + //$NON-NLS-1$
				" (" + verfahren + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Erfragt den Namen dieses Messwertes.
	 *
	 * @return der Name dieses Messwertes
	 */
	public final String getName() {
		return attName;
	}

}
