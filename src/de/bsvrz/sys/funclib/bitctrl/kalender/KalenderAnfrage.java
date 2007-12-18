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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.kalender;

import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.ClientApplication;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.NetzBestandTeil;
import de.bsvrz.sys.funclib.bitctrl.util.Intervall;

/**
 * Repr&auml;sentiert eine Anfrage an den Ereigniskalender.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class KalenderAnfrage {

	/** Es werden alle Ereignistypen ber&uuml;cksichtigt. */
	public static final int OPT_ALLE = 0;

	/** Es werden nur die Ereignistypen ber&uuml;cksichtigt, die angeben sind. */
	public static final int OPT_NUR = 1;

	/**
	 * Es werden alle Ereignistypen ber&uuml;cksichtigt au&szlig;er denen, die
	 * angeben sind.
	 */
	public static final int OPT_NICHT = 2;

	/** Der Absender der Anfrage. */
	private ClientApplication absender;

	/** Das Absenderzeichen des Anfragers. */
	private String absenderZeichen;

	/** Das Zeitintervall, indem Ereignisse angefragt werden. */
	private Intervall intervall;

	/** Menge der Netzbestandteile, dessen Ereignisse angefragt werden. */
	private final Set<SystemObject> raeumlicheGueltigkeit;

	/**
	 * Auswahloption f&uuml;r die Liste der Ereignistypen. Standard ist
	 * {@link #OPT_ALLE}.
	 * 
	 * @see #ereignisTypen
	 */
	private int ereignisTypenOption = OPT_ALLE;

	/**
	 * Menge von Ereignistypen, die entsprechend der gesetzten Auswahloption
	 * ber&uuml;cksichtigt werden.
	 * 
	 * @see #ereignisTypenOption
	 */
	private final Set<SystemObject> ereignisTypen;

	/**
	 * Kreiert eine neue Kalenderanfrage. Als Ereignistypenoption wird
	 * {@link #OPT_ALLE} angenommen.
	 * 
	 * @param absender
	 *            der Sender der Anfrage.
	 * @param absenderZeichen
	 *            ein belieber Text.
	 * @param intervall
	 *            das Anfrageintervall.
	 */
	public KalenderAnfrage(ClientApplication absender, String absenderZeichen,
			Intervall intervall) {
		this.absender = absender;
		this.absenderZeichen = absenderZeichen;
		this.intervall = intervall;

		ereignisTypenOption = OPT_ALLE;
		ereignisTypen = new HashSet<SystemObject>();
		raeumlicheGueltigkeit = new HashSet<SystemObject>();
	}

	/**
	 * F&uuml;gt der Menge der Ereignistypen einen hinzu.
	 * 
	 * @param ereignisTyp
	 *            ein Systemobjekt, welches ein Ereignistyp sein muss.
	 */
	public void addEreignisTyp(SystemObject ereignisTyp) {
		ereignisTypen.add(ereignisTyp);
	}

	/**
	 * F&uuml;gt der Menge der Netzbestandteile der r&auml;umlichen
	 * G&uuml;ltigkeit einen hinzu.
	 * 
	 * @param netzBestandTeil
	 *            ein Systemobjekt, welches ein NetzBestandTeil sein muss.
	 */
	public void addNetzBestandTeil(NetzBestandTeil netzBestandTeil) {
		raeumlicheGueltigkeit.add(netzBestandTeil.getSystemObject());
	}

	/**
	 * F&uuml;gt der Menge der Netzbestandteile der r&auml;umlichen
	 * G&uuml;ltigkeit einen hinzu.
	 * 
	 * @param netzBestandTeil
	 *            ein Systemobjekt, welches ein NetzBestandTeil sein muss.
	 * @deprecated die Methode {@link #addNetzBestandTeil(NetzBestandTeil)} ist
	 *             besser geeignet, da sie typgepr&uuml;ft arbeitet.
	 */
	@Deprecated
	public void addNetzBestandTeil(SystemObject netzBestandTeil) {
		raeumlicheGueltigkeit.add(netzBestandTeil);
	}

	/**
	 * Gibt die anfragende Applikation zur&uuml;ck.
	 * 
	 * @return den Anfragenden.
	 */
	public ClientApplication getAbsender() {
		return absender;
	}

	/**
	 * Das Absenderzeichen der anfragenden Applikation.
	 * 
	 * @return ein beliebig vergebener Text.
	 */
	public String getAbsenderZeichen() {
		return absenderZeichen;
	}

	/**
	 * Baut aus den Informationen der Anfrage ein Datum.
	 * <p>
	 * Hinweis: Das Ergebnis wird auch im Parameter abgelegt!
	 * <p>
	 * <em>Hinweis:</em> Diese Methode ist nicht Teil der öffentlichen API und
	 * sollte nicht außerhalb der Ganglinie-API verwendet werden.
	 * 
	 * @param daten
	 *            ein Datum, welches eine (leere) Anfrage darstellt.
	 * @return das ausgef&uuml;llte Datum.
	 */
	public Data getDaten(Data daten) {
		Array feld;
		int i;

		daten.getReferenceValue("absenderId").setSystemObject(absender);
		daten.getTextValue("absenderZeichen").setText(absenderZeichen);
		daten.getTimeValue("Anfangszeitpunkt").setMillis(intervall.getStart());
		daten.getTimeValue("Endzeitpunkt").setMillis(intervall.getEnde());
		daten.getUnscaledValue("EreignisTypenOption").set(ereignisTypenOption);

		feld = daten.getArray("RäumlicheGültigkeit");
		feld.setLength(raeumlicheGueltigkeit.size());
		i = 0;
		for (SystemObject so : raeumlicheGueltigkeit) {
			feld.getItem(i++).asReferenceValue().setSystemObject(so);
		}

		feld = daten.getArray("EreignisTypReferenz");
		feld.setLength(ereignisTypen.size());
		i = 0;
		for (SystemObject so : ereignisTypen) {
			feld.getItem(i++).asReferenceValue().setSystemObject(so);
		}

		return daten;
	}

	/**
	 * Liste der Ereignistypen, die bei der Anfrage ber&uuml;cksichtigt werden.
	 * 
	 * @return Liste von Ereignistypen.
	 */
	public Set<SystemObject> getEreignisTypen() {
		return ereignisTypen;
	}

	/**
	 * Gibt die Art an, wie die Ereignistypen ber&uuml;cksichtigt werden.
	 * 
	 * @return die Filteroption.
	 * 
	 * @see #OPT_ALLE
	 * @see #OPT_NUR
	 * @see #OPT_NICHT
	 */
	public int getEreignisTypenOption() {
		return ereignisTypenOption;
	}

	/**
	 * Gibt das Intervall der Anfrage an.
	 * 
	 * @return Intervall indem die Ereignisse der ANtwort liegen m&uuml;ssen.
	 */
	public Intervall getIntervall() {
		return intervall;
	}

	/**
	 * Gibt die Menge der Netzbestandteile zur&uuml;ck, f&uuml;r die Ereignisse
	 * angefragt werden.
	 * 
	 * @return Menge von Netzbestandteilen.
	 */
	public Set<SystemObject> getRaeumlicheGueltigkeit() {
		return raeumlicheGueltigkeit;
	}

	/**
	 * &Uuml;bernimmt die Informationen aus dem Datum als inneren Zustand.
	 * <p>
	 * <em>Hinweis:</em> Diese Methode ist nicht Teil der öffentlichen API und
	 * sollte nicht außerhalb der Ganglinie-API verwendet werden.
	 * 
	 * @param daten
	 *            ein Datum, welches eine Anfrage darstellt.
	 */
	public void setDaten(Data daten) {
		Array feld;

		absender = (ClientApplication) daten.getReferenceValue("absenderId")
				.getSystemObject();
		absenderZeichen = daten.getTextValue("absenderZeichen").getText();
		intervall = new Intervall(daten.getTimeValue("Anfangszeitpunkt")
				.getMillis(), daten.getTimeValue("Endzeitpunkt").getMillis());
		ereignisTypenOption = daten.getUnscaledValue("EreignisTypenOption")
				.intValue();

		raeumlicheGueltigkeit.clear();
		feld = daten.getArray("RäumlicheGültigkeit");
		for (int i = 0; i < feld.getLength(); i++) {
			raeumlicheGueltigkeit.add(feld.getItem(i).asReferenceValue()
					.getSystemObject());
		}

		ereignisTypen.clear();
		feld = daten.getArray("EreignisTypReferenz");
		for (int i = 0; i < feld.getLength(); i++) {
			ereignisTypen.add(feld.getItem(i).asReferenceValue()
					.getSystemObject());
		}
	}

	/**
	 * Legt fest wie die angegebenen Ereignistypen ber&uuml;cksichtigt werden
	 * sollen.
	 * 
	 * @param ereignisTypenOption
	 *            die gew&uuml;nschte Option.
	 * @see #OPT_ALLE
	 * @see #OPT_NUR
	 * @see #OPT_NICHT
	 */
	public void setEreignisTypenOption(int ereignisTypenOption) {
		if (ereignisTypenOption != OPT_ALLE && ereignisTypenOption != OPT_NUR
				&& ereignisTypenOption != OPT_NICHT) {
			throw new IllegalArgumentException("Die Option ist nicht bekannt.");
		}
		this.ereignisTypenOption = ereignisTypenOption;
	}

}
