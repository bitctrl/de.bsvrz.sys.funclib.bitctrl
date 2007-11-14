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

package de.bsvrz.sys.funclib.bitctrl.dua.dfs;

import java.util.Collection;
import java.util.HashSet;

import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerungsListener;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Diese Klasse liest die Parameter der Datenflusssteuerung aus und meldet
 * �nderungen formatiert an andere Module des Typs
 * <code>IDatenFlussSteuerungsListener</code> weiter.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class DatenFlussSteuerungsVersorger implements ClientReceiverInterface {

	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Fehlermeldungstext
	 */
	private static final String STD_FEHLER = "Anmeldung auf Datenflusssteuerung fehlgeschlagen"; //$NON-NLS-1$

	/**
	 * die statische Instanz dieser Klasse
	 */
	protected static DatenFlussSteuerungsVersorger INSTANZ = null;

	/**
	 * Erfragt die statische Instanz dieser Klasse. Diese liest die Parameter
	 * der Datenflusssteuerung aus und meldet �nderungen formatiert an
	 * angemeldete Module des Typs <code>IDatenFlussSteuerungsListener</code>
	 * weiter.
	 * 
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 * @return die statische Instanz dieser Klasse
	 * @throws DUAInitialisierungsException
	 *             wird geworfen, wenn die �bergebene Verbindung fehlerhaft ist
	 *             (nicht die geforderten Informationen bereit h�lt), bzw. keine
	 *             Datenanmeldungen durchgef�hrt werden konnten
	 */
	public static DatenFlussSteuerungsVersorger getInstanz(
			final IVerwaltung verwaltung) throws DUAInitialisierungsException {
		if (INSTANZ == null) {
			/**
			 * Ermittlung des Objektes, das die Datenflusssteuerung f�r das
			 * �bergebene Verwaltungsmodul beschreibt
			 */
			final SystemObjectType typDFS = (SystemObjectType) verwaltung
					.getVerbindung().getDataModel()
					.getObject(DFSKonstanten.TYP);

			Collection<ConfigurationArea> kBereiche = verwaltung
					.getKonfigurationsBereiche();

			SystemObject[] dfsObjekte = new SystemObject[0];
			if (typDFS != null) {
				dfsObjekte = DUAUtensilien.getBasisInstanzen(typDFS,
						verwaltung.getVerbindung(), kBereiche).toArray(
						new SystemObject[0]);
			}

			SystemObject dfsObjekt = (dfsObjekte.length > 0 ? dfsObjekte[0]
					: null);

			if (dfsObjekte.length == 1) {
				LOGGER.fine("Es wurde genau ein Objekt vom Typ " + //$NON-NLS-1$
						DFSKonstanten.TYP + " identifiziert"); //$NON-NLS-1$
			} else if (dfsObjekte.length > 1) {
				LOGGER.warning("Es liegen mehrere Objekte vom Typ " + //$NON-NLS-1$
						DFSKonstanten.TYP + " vor"); //$NON-NLS-1$
			}

			INSTANZ = new DatenFlussSteuerungsVersorger(verwaltung, dfsObjekt);
		}
		return INSTANZ;
	}

	/**
	 * Menge aller Beobachter dieser Instanz
	 */
	private final Collection<IDatenFlussSteuerungsListener> listenerListe = new HashSet<IDatenFlussSteuerungsListener>();

	/**
	 * die aktuellen Parameter der Datenflusssteuerung
	 */
	private DatenFlussSteuerung letzteDfs = null;

	/**
	 * Verbindung zum Verwaltungsmodul
	 */
	private IVerwaltung verwaltung = null;

	/**
	 * Standardkonstruktor
	 * 
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 * @param dfsObjekt
	 *            das des Objektes, das die Datenflusssteuerung f�r das
	 *            �bergebene Verwaltungsmodul beschreibt
	 * @throws DUAInitialisierungsException
	 *             wird geworfen, wenn die �bergebene Verbindung fehlerhaft ist
	 *             (nicht die geforderten Informationen bereit h�lt), bzw. keine
	 *             Datenanmeldungen durchgef�hrt werden konnten
	 */
	protected DatenFlussSteuerungsVersorger(final IVerwaltung verwaltung,
			final SystemObject dfsObjekt) throws DUAInitialisierungsException {
		if (verwaltung == null) {
			throw new DUAInitialisierungsException(STD_FEHLER
					+ "\nKeine Verbindung zum Datenverteiler"); //$NON-NLS-1$
		}
		this.verwaltung = verwaltung;

		if (dfsObjekt != null) {
			try {
				DataDescription dd = new DataDescription(verwaltung
						.getVerbindung().getDataModel().getAttributeGroup(
								DFSKonstanten.ATG), verwaltung.getVerbindung()
						.getDataModel().getAspect(
								DaVKonstanten.ASP_PARAMETER_SOLL), (short) 0);

				verwaltung.getVerbindung().subscribeReceiver(this, dfsObjekt,
						dd, ReceiveOptions.normal(), ReceiverRole.receiver());

				LOGGER.config("F�r die Datenflusssteuerung" + //$NON-NLS-1$
						" wird das Objekt " + dfsObjekt + " verwendet."); //$NON-NLS-1$//$NON-NLS-2$
			} catch (Exception ex) {
				throw new DUAInitialisierungsException(STD_FEHLER, ex);
			}
		} else {
			LOGGER
					.warning("Die Datenflusssteuerung ist nicht zur Laufzeit steuerbar.\n" + //$NON-NLS-1$
							"Es wurde kein Objekt vom Typ "
							+ DFSKonstanten.TYP
							+ //$NON-NLS-1$
							" identifiziert."); //$NON-NLS-1$
		}
	}

	/**
	 * F�gt diesem Element einen neuen Beobachter hinzu. Jedes neue
	 * Beobachterobjekt wird sofort nach der Anmeldung mit den aktuellen Daten
	 * versorgt.
	 * 
	 * @param listener
	 *            der neue Beobachter
	 */
	public final void addListener(final IDatenFlussSteuerungsListener listener) {
		if (listener != null) {
			synchronized (this.listenerListe) {
				this.listenerListe.add(listener);
				if (letzteDfs != null) {
					listener.aktualisierePublikation(letzteDfs);
				}
			}
		} else {
			LOGGER.error("Listener kann nicht eingef�gt" + //$NON-NLS-1$
					" werden. Er ist " + DUAKonstanten.NULL); //$NON-NLS-1$
		}
	}

	/**
	 * L�scht ein Beobachterobjekt.
	 * 
	 * @param listener
	 *            das zu l�schende Beobachterobjekt
	 */
	public final void removeListener(
			final IDatenFlussSteuerungsListener listener) {
		if (listener != null) {
			synchronized (this.listenerListe) {
				this.listenerListe.remove(listener);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] resultate) {
		letzteDfs = new DatenFlussSteuerung();

		if (resultate != null && resultate.length > 0) {
			/**
			 * nur ein Objekt wird hier behandelt, d.h. dass nur ein Datensatz
			 * (der Letzte) interessiert
			 */
			final ResultData resultat = resultate[resultate.length - 1];

			if (resultat != null && resultat.isSourceAvailable()
					&& !resultat.isNoDataAvailable() && resultat.hasData()
					&& resultat.getData() != null) {

				Data.Array ps = resultat.getData().getArray(
						DFSKonstanten.ATL_PARA_SATZ);

				for (int i = 0; i < ps.getLength(); i++) {
					Data satz = ps.getItem(i);
					if (satz != null) {
						ParameterSatz dfParameterSatz = new ParameterSatz();

						final SWETyp swe = SWETyp.getZustand((int) satz
								.getUnscaledValue(DFSKonstanten.ATT_SWE)
								.getState().getValue());
						dfParameterSatz.setSwe(swe);

						/**
						 * Iteriere �ber alle Publikationszuordnungen innerhalb
						 * dieses Parametersatzes
						 */
						for (int j = 0; j < satz.getArray(
								DFSKonstanten.ATT_PUB_ZUORDNUNG).getLength(); j++) {
							Data paraZuordnung = satz.getArray(
									DFSKonstanten.ATT_PUB_ZUORDNUNG).getItem(j);
							PublikationsZuordung dfParaZuordnung;
							try {
								dfParaZuordnung = new PublikationsZuordung(
										paraZuordnung, verwaltung);
								dfParameterSatz.add(dfParaZuordnung);
							} catch (Exception e) {
								LOGGER.error("Eine Publikationszuordnung " + //$NON-NLS-1$
										"konnte nicht korrekt" + //$NON-NLS-1$
										" ausgelesen werden: " + paraZuordnung,
										e); //$NON-NLS-1$
							}
						}

						ParameterSatz dummy = letzteDfs
								.getParameterSatzFuerSWE(swe);

						if (dummy != null) {
							for (PublikationsZuordung neuePz : dfParameterSatz
									.getPubZuordnung()) {
								dummy.add(neuePz);
							}
						} else {
							letzteDfs.add(dfParameterSatz);
						}
					}
				}
			}
		}

		if (letzteDfs != null) {
			synchronized (this.listenerListe) {
				for (IDatenFlussSteuerungsListener listener : this.listenerListe) {
					listener.aktualisierePublikation(letzteDfs);
				}
			}
		}
	}
}