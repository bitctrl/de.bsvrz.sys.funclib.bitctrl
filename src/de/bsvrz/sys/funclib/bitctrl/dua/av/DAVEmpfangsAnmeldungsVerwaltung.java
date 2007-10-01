/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.x
 * Copyright (C) 2007 BitCtrl Systems GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.av;

import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Verwaltungsklasse f�r Datenanmeldungen zum Empfangen von Daten.
 * �ber die Methode <code>modifiziereDatenAnmeldung(..)</code> lassen
 * sich Daten anmelden bzw. abmelden.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class DAVEmpfangsAnmeldungsVerwaltung
extends DAVAnmeldungsVerwaltung{

	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Rolle des Empf�ngers
	 */
	private ReceiverRole rolle = null;

	/**
	 * Optionen
	 */
	private ReceiveOptions optionen = null;

	/**
	 * der Empf�nger der Daten
	 */
	private ClientReceiverInterface empfaenger = null;


	/**
	 * Standardkonstruktor
	 *
	 * @param dav Datenverteilerverbindung
	 * @param rolle Rolle
	 * @param optionen Optionen
	 * @param empfaenger die Empf�nger-Klasse der Datenverteiler-
	 * Daten, f�r die diese Anmeldungs-Verwaltung arbeiten soll
	 */
	public DAVEmpfangsAnmeldungsVerwaltung(final ClientDavInterface dav,
										   final ReceiverRole rolle,
										   final ReceiveOptions optionen,
										   final ClientReceiverInterface empfaenger){
		super(dav);
		this.rolle = rolle;
		this.optionen = optionen;
		this.empfaenger = empfaenger;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String abmelden(final Collection
						<DAVObjektAnmeldung> abmeldungen) {
		String info = "keine\n"; //$NON-NLS-1$
		if(abmeldungen.size() > 0){
			info = "\n"; //$NON-NLS-1$
		}
		for(DAVObjektAnmeldung abmeldung:abmeldungen){
			try{
				this.dav.unsubscribeReceiver(this.empfaenger,
						abmeldung.getObjekt(), abmeldung.getDatenBeschreibung());
				this.aktuelleObjektAnmeldungen.remove(abmeldung);
				info += abmeldung;
			}catch(Exception ex){
				LOGGER.error("Probleme beim " + //$NON-NLS-1$
						"Abmelden ale Empf�nger/Senke", ex); //$NON-NLS-1$
			}
		}
		return info + "von [" + empfaenger + "]\n";  //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String anmelden(final Collection
						<DAVObjektAnmeldung> anmeldungen) {
		String info = "keine\n"; //$NON-NLS-1$
		if(anmeldungen.size() > 0){
			info = "\n"; //$NON-NLS-1$
		}
		for(DAVObjektAnmeldung anmeldung:anmeldungen){
			try{
				this.dav.subscribeReceiver(this.empfaenger,
					anmeldung.getObjekt(), anmeldung.getDatenBeschreibung(),
					this.optionen, this.rolle);
				this.aktuelleObjektAnmeldungen.put(anmeldung, null);
				info += anmeldung;
			}catch(Exception ex){
				LOGGER.error("Probleme beim " + //$NON-NLS-1$
						"Anmelden ale Empf�nger/Senke", ex); //$NON-NLS-1$
			}
		}
		return info + "f�r [" + empfaenger + "]\n";  //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getInfo() {
		return this.rolle + ", " + this.optionen;  //$NON-NLS-1$
	}
}
