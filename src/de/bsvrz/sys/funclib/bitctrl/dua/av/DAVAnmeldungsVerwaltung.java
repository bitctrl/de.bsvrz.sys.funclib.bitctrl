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

package de.bsvrz.sys.funclib.bitctrl.dua.av;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Abstrakte Verwaltungsklasse für Datenanmeldungen.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public abstract class DAVAnmeldungsVerwaltung {

	/**
	 * produziert ausfuehrlichere Log-Meldungen.
	 */
	protected static final boolean DEBUG = false;

	/**
	 * Debug-Logger.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Baum der Datenanmeldungen, die im Moment aktuell sind (ggf. mit ihrem
	 * Status der Sendesteuerung).
	 */
	protected Map<DAVObjektAnmeldung, SendeStatus> aktuelleObjektAnmeldungen = new TreeMap<DAVObjektAnmeldung, SendeStatus>();

	/**
	 * Datenverteilerverbindung.
	 */
	protected ClientDavInterface dav = null;

	/**
	 * Standardkonstruktor.
	 * 
	 * @param dav
	 *            Datenverteilerverbindung
	 */
	protected DAVAnmeldungsVerwaltung(final ClientDavInterface dav) {
		this.dav = dav;
	}

	/**
	 * Modifiziert die hier verwalteten Objektanmeldungen dergestalt, dass nur
	 * die innerhalb der übergebenen Liste beschriebenen Anmeldungen bestehen
	 * bleiben.<br>
	 * D.h. insbesondere, dass eine übergebene leere Liste alle bereits
	 * durchgeführten Anmeldungen wieder rückgängig macht.
	 * 
	 * @param neueObjektAnmeldungen
	 *            die neue Liste mit Objektanmeldungen
	 */
	public final void modifiziereObjektAnmeldung(
			final Collection<DAVObjektAnmeldung> neueObjektAnmeldungen) {

		// Debug Anfang
		String info = Constants.EMPTY_STRING;
		if (DEBUG) {
			info = "Verlangte Anmeldungen (" + this.getInfo() + "): "; //$NON-NLS-1$ //$NON-NLS-2$
			if (neueObjektAnmeldungen.size() == 0) {
				info += "keine\n"; //$NON-NLS-1$
			} else {
				info += "\n"; //$NON-NLS-1$
			}
			for (DAVObjektAnmeldung neueObjektAnmeldung : neueObjektAnmeldungen) {
				info += neueObjektAnmeldung;
			}
			info += "Bisherige Anmeldungen (" + this.getInfo() + "): "; //$NON-NLS-1$ //$NON-NLS-2$
			if (aktuelleObjektAnmeldungen.size() == 0) {
				info += "keine\n"; //$NON-NLS-1$
			} else {
				info += "\n"; //$NON-NLS-1$
			}
			for (DAVObjektAnmeldung aktuelleObjektAnmeldung : aktuelleObjektAnmeldungen
					.keySet()) {
				info += aktuelleObjektAnmeldung;
			}
		}
		// Debug Ende

		synchronized (this) {
			Collection<DAVObjektAnmeldung> diffObjekteAnmeldungen = new TreeSet<DAVObjektAnmeldung>();
			for (DAVObjektAnmeldung neueAnmeldung : neueObjektAnmeldungen) {
				if (!aktuelleObjektAnmeldungen.containsKey(neueAnmeldung)) {
					diffObjekteAnmeldungen.add(neueAnmeldung);
				}
			}

			Collection<DAVObjektAnmeldung> diffObjekteAbmeldungen = new TreeSet<DAVObjektAnmeldung>();
			for (DAVObjektAnmeldung aktuelleAnmeldung : aktuelleObjektAnmeldungen
					.keySet()) {
				if (!neueObjektAnmeldungen.contains(aktuelleAnmeldung)) {
					diffObjekteAbmeldungen.add(aktuelleAnmeldung);
				}
			}

			if (DEBUG) {
				info += "--------\nABmeldungen: "; //$NON-NLS-1$
				info += abmelden(diffObjekteAbmeldungen);
				info += "ANmeldungen: "; //$NON-NLS-1$
				info += anmelden(diffObjekteAnmeldungen);
				LOGGER.config(info);
			} else {
				abmelden(diffObjekteAbmeldungen);
				anmelden(diffObjekteAnmeldungen);
			}
		}
	}

	/**
	 * Führt alle übergebenen Daten<b>ab</b>meldungen durch.
	 * 
	 * @param abmeldungen
	 *            durchzuführende Daten<b>ab</b>meldungen
	 * @return eine Liste aller <b>ab</b>gemeldeten Einzel-Anmeldungen als
	 *         Zeichenkette
	 */
	protected abstract String abmelden(
			final Collection<DAVObjektAnmeldung> abmeldungen);

	/**
	 * Führt alle übergebenen Daten<b>an</b>meldungen durch.
	 * 
	 * @param anmeldungen
	 *            durchzuführende Daten<b>an</b>meldungen
	 * @return eine Liste aller neu <b>an</b>gemeldeten Einzel-Anmeldungen als
	 *         Zeichenkette
	 */
	protected abstract String anmelden(
			final Collection<DAVObjektAnmeldung> anmeldungen);

	/**
	 * Erfragt Informationen zum Anmeldungsverhalten.
	 * 
	 * @return Informationen zum Anmeldungsverhalten
	 */
	protected abstract String getInfo();

	/**
	 * Der Zustand einer Datenbeschreibung bwzüglich der Sendesteuerung und der
	 * aktuell veroeffentlichten Daten.
	 * 
	 * @author BitCtrl Systems GmbH, Thierfelder
	 * 
	 */
	protected class SendeStatus {

		/**
		 * aktueller Zustand der Sendesteuerung.
		 */
		private byte status = ClientSenderInterface.STOP_SENDING;

		/**
		 * indiziert, ob die Datenbeschreibung im Moment auf
		 * <code>keine Daten</code> oder <code>keine Quelle</code> steht.
		 */
		private boolean imMomentKeineDaten = true;

		/**
		 * Standardkonstruktor.
		 */
		public SendeStatus() {
		}

		/**
		 * Standardkonstruktor.
		 * 
		 * @param status
		 *            aktueller Zustand der Sendesteuerung
		 * @param imMomentKeineDaten
		 *            indiziert, ob die Datenbeschreibung im Moment auf
		 *            <code>keine Daten</code> oder <code>keine Quelle</code>
		 *            steht
		 */
		public SendeStatus(byte status, boolean imMomentKeineDaten) {
			this.status = status;
			this.imMomentKeineDaten = imMomentKeineDaten;
		}

		/**
		 * Erfragt den aktuellen Zustand der Sendesteuerung.
		 * 
		 * @return aktueller Zustand der Sendesteuerung
		 */
		public final byte getStatus() {
			return this.status;
		}

		/**
		 * Erfragt ob die Datenbeschreibung im Moment auf
		 * <code>keine Daten</code> oder <code>keine Quelle</code> steht.
		 * 
		 * @return ob die Datenbeschreibung im Moment auf
		 *         <code>keine Daten</code> oder <code>keine Quelle</code>
		 *         steht
		 */
		public final boolean isImMomentKeineDaten() {
			return this.imMomentKeineDaten;
		}

	}

}
