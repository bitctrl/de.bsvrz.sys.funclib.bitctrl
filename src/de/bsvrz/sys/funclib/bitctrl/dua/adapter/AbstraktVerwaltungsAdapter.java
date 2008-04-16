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

package de.bsvrz.sys.funclib.bitctrl.dua.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.DatenFlussSteuerungsVersorger;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageState;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;

/**
 * Adapterklasse für Verwaltungsmodule.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public abstract class AbstraktVerwaltungsAdapter implements IVerwaltung {

	/**
	 * Die Objekte, die bearbeitet werden sollen.
	 */
	protected SystemObject[] objekte = null;

	/**
	 * Verbindung zum Datenverteiler.
	 */
	protected ClientDavInterface verbindung = null;

	/**
	 * Diese Klasse versendet Betriebsmeldungen.
	 */
	protected MessageSender nachrichtenSender = null;

	/**
	 * die Argumente der Kommandozeile.
	 */
	protected ArrayList<String> komArgumente = new ArrayList<String>();

	/**
	 * Die Konfigurationsbreiche, deren Objekte bearbeitet werden sollen.
	 */
	private Collection<ConfigurationArea> kBereiche = new HashSet<ConfigurationArea>();

	/**
	 * Verbindung zur Datenflusssteuerung.
	 */
	protected DatenFlussSteuerungsVersorger dfsHilfe = null;

	/**
	 * {@inheritDoc}
	 */
	public final Collection<ConfigurationArea> getKonfigurationsBereiche() {
		return this.kBereiche;
	}

	/**
	 * {@inheritDoc}
	 */
	public final SystemObject[] getSystemObjekte() {
		return this.objekte;
	}

	/**
	 * {@inheritDoc}
	 */
	public final ClientDavInterface getVerbindung() {
		return this.verbindung;
	}

	/**
	 * Erfragt eine Verbindung zum Versenden von Betriebsmeldungen.
	 *
	 * @return Betriebsmeldungs-Sender.
	 */
	public final MessageSender getBetriebsmeldungsSender() {
		return this.nachrichtenSender;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void sendeBetriebsMeldung(String id, MessageType typ,
			String nachrichtenTypErweiterung, MessageGrade klasse,
			MessageState status, String nachricht) {
		this.nachrichtenSender.sendMessage(id, typ, nachrichtenTypErweiterung,
				klasse, status, nachricht);
	}

	/**
	 * {@inheritDoc}
	 */
	public void initialize(ClientDavInterface dieVerbindung) throws Exception {
		try {

			this.verbindung = dieVerbindung;
			this.nachrichtenSender = MessageSender.getInstance();
			this.nachrichtenSender.setApplicationLabel(this.getSWETyp()
					.toString());
			if (this.komArgumente != null) {
				this.kBereiche = getKonfigurationsBereicheAlsObjekte(DUAUtensilien
						.getArgument(
								DUAKonstanten.ARG_KONFIGURATIONS_BEREICHS_PID,
								this.komArgumente));
				this.dfsHilfe = DatenFlussSteuerungsVersorger.getInstanz(this);
			} else {
				throw new DUAInitialisierungsException("Es wurden keine" + //$NON-NLS-1$
						" Kommandozeilenargumente übergeben"); //$NON-NLS-1$
			}

			/**
			 * Initialisiere das eigentliche Verwaltungsmodul
			 */
			this.initialisiere();

			Debug.getLogger().config(this.toString());

		} catch (DUAInitialisierungsException ex) {
			String fehler = "Initialisierung der Applikation " + //$NON-NLS-1$
					this.getSWETyp().toString() + " fehlgeschlagen"; //$NON-NLS-1$
			Debug.getLogger().error(fehler, ex);
			ex.printStackTrace();

			if (this.nachrichtenSender != null) {
				this.nachrichtenSender.sendMessage(
						MessageType.APPLICATION_DOMAIN, MessageGrade.ERROR,
						fehler);
			}

			if (this.verbindung != null) {
				verbindung.disconnect(true, fehler);
			} else {
				System.exit(0);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void parseArguments(ArgumentList argumente) throws Exception {
		Debug.init(this.getSWETyp().toString(), argumente);

		for (String s : argumente.getArgumentStrings()) {
			if (s != null) {
				this.komArgumente.add(s);
			}
		}

		argumente.fetchUnusedArguments();
	}

	/**
	 * Extrahiert aus einer Zeichenkette alle über Kommata getrennten
	 * Konfigurationsbereiche und gibt deren Systemobjekte zurück.
	 *
	 * @param kbString Zeichenkette mit den Konfigurationsbereichen
	 * @return (ggf. leere) <code>ConfigurationArea-Collection</code>
	 * mit allen extrahierten Konfigurationsbereichen.
	 */
	private Collection<ConfigurationArea> getKonfigurationsBereicheAlsObjekte(
			final String kbString) {
		List<String> resultListe = new ArrayList<String>();

		if (kbString != null) {
			String[] s = kbString.split(","); //$NON-NLS-1$
			for (String dummy : s) {
				if (dummy != null && dummy.length() > 0) {
					resultListe.add(dummy);
				}
			}
		}
		Collection<ConfigurationArea> kbListe = new HashSet<ConfigurationArea>();

		for (String kb : resultListe) {
			try {
				ConfigurationArea area = this.verbindung.getDataModel()
						.getConfigurationArea(kb);
				if (area != null) {
					kbListe.add(area);
				}
			} catch (UnsupportedOperationException ex) {
				Debug.getLogger().warning("Konfigurationsbereich " + kb + //$NON-NLS-1$
						" konnte nicht identifiziert werden.", ex); //$NON-NLS-1$
			}
		}

		return kbListe;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "SWE: " + this.getSWETyp() + "\n"; //$NON-NLS-1$ //$NON-NLS-2$

		String dummy = "---keine Konfigurationsbereiche angegeben---\n"; //$NON-NLS-1$
		if (kBereiche.size() > 0) {
			dummy = Constants.EMPTY_STRING;
			for (ConfigurationArea kb : kBereiche) {
				dummy += kb + "\n"; //$NON-NLS-1$
			}
		}

		return s + "Konfigurationsbereiche:\n" + dummy; //$NON-NLS-1$
	}

	/**
	 * {@inheritDoc}
	 */
	public String getArgument(String schluessel) {
		return DUAUtensilien.getArgument(schluessel, this.komArgumente);
	}

	/**
	 * Diese Methode wird zur Initialisierung aufgerufen,
	 * <b>nachdem</b> sowohl die Argumente der Kommandozeile,
	 * als auch die Datenverteilerverbindung übergeben wurden
	 * (also nach dem Aufruf der Methoden <code>parseArguments(..)</code>
	 * und <code>initialize(..)</code>).
	 *
	 * @throws DUAInitialisierungsException falls es Probleme bei der
	 * Initialisierung geben sollte
	 */
	protected abstract void initialisiere() throws DUAInitialisierungsException;

}
