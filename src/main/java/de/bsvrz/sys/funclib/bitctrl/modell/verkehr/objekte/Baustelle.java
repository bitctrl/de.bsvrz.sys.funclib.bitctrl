/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AnmeldeException;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensendeException;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdBaustellenSimulationStarten;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdBaustellenSimulationStartenMitMq;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter.PdBaustellenEigenschaften;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter.PdBaustellenVerantwortlicher;

/**
 * Repr&auml;sentiert eine Baustelle.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class Baustelle extends Situation {

	/**
	 * die Menge der Netze in denen die die Baustelle referenziert wird.
	 */
	private final Set<VerkehrModellNetz> netze = new HashSet<>();

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz einer Baustelle auf der Basis des
	 * übergebenen Systemobjekts.
	 *
	 * @param obj
	 *            das Systemobjekt, das die Baustelle definiert
	 */
	public Baustelle(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist keine Baustelle.");
		}
	}

	/**
	 * fügt der Baustelle eine Netzreferenz hinzu.
	 *
	 * @param netz
	 *            das Netz für das eine Referenz hinzugefügt wird.
	 */
	@Override
	public void addNetzReferenz(final VerkehrModellNetz netz) {
		netze.add(netz);
	}

	/**
	 * liefert den Datensatz der die Eigenschaften einer Baustelle definiert.
	 *
	 * @return den Datensatz
	 */
	public PdBaustellenEigenschaften getBaustellenEigenschaften() {
		return getParameterDatensatz(PdBaustellenEigenschaften.class);
	}

	/**
	 * liefert den Datensatz der den Verantwortlichen einer Baustelle definiert.
	 *
	 * @return den Datensatz
	 */
	public PdBaustellenVerantwortlicher getBaustellenVerantwortlicher() {
		return getParameterDatensatz(PdBaustellenVerantwortlicher.class);
	}

	/**
	 * liefert die Menge der Netze in denen die Baustelle refernziert wird.
	 *
	 * @return die Menge der Netze
	 */
	@Override
	public Set<VerkehrModellNetz> getNetze() {
		return netze;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.BAUSTELLE;
	}

	/**
	 * entfernt eine Netzreferenz von der Baustelle.
	 *
	 * @param netz
	 *            das Netz auf das die Referenz entfernt wird.
	 */
	@Override
	public void removeNetzReferenz(final VerkehrModellNetz netz) {
		netze.remove(netz);
	}

	/**
	 * die Funktion versendet eine Anforderung zum Simulieren einer Baustelle.
	 * Der Versand erfolgt mit einem Timeout von 1 Minute, d.h. wenn innerhalb
	 * einer Minute keine Empfangsbereitschaft durch die Baustellensimulation
	 * besteht wird der Versand abgebrochen.
	 *
	 * @param name
	 *            der Name des Auftraggebers
	 * @param bemerkung
	 *            eine Bemerkung zum Simulationsauftrag
	 * @throws AnmeldeException
	 *             die Anmeldung zum Versand der Auftragsdaten ist
	 *             fehlgeschlagen
	 * @throws DatensendeException
	 *             der Auftrag konnte innerhalb der vorgegebenen Zeit nicht
	 *             versendet werden
	 */
	public void simuliereBaustelle(final String name, final String bemerkung)
			throws AnmeldeException, DatensendeException {

		getOnlineDatensatz(OdBaustellenSimulationStarten.class).anmeldenSender(
				OdBaustellenSimulationStarten.Aspekte.Senden.getAspekt());
		final OdBaustellenSimulationStarten.Daten datum = getOnlineDatensatz(
				OdBaustellenSimulationStarten.class).erzeugeDatum();
		datum.setName(name);
		datum.setBemerkung(bemerkung);
		try {
			getOnlineDatensatz(OdBaustellenSimulationStarten.class).sendeDaten(
					OdBaustellenSimulationStarten.Aspekte.Senden.getAspekt(),
					datum, TimeUnit.MINUTES.toMillis(1));
		} catch (final DatensendeException e) {
			getOnlineDatensatz(OdBaustellenSimulationStarten.class)
			.abmeldenSender(OdBaustellenSimulationStarten.Aspekte.Senden
					.getAspekt());
			throw e;
		}

		getOnlineDatensatz(OdBaustellenSimulationStarten.class).abmeldenSender(
				OdBaustellenSimulationStarten.Aspekte.Senden.getAspekt());
	}

	/**
	 * die Funktion versendet eine Anforderung zum Simulieren einer Baustelle.
	 * Der Versand erfolgt mit einem Timeout von 1 Minute, d.h. wenn innerhalb
	 * einer Minute keine Empfangsbereitschaft durch die Baustellensimulation
	 * besteht wird der Versand abgebrochen.
	 *
	 * @param name
	 *            der Name des Auftraggebers
	 * @param bemerkung
	 *            eine Bemerkung zum Simulationsauftrag
	 * @param mq
	 *            MQ, der zur Berechnung des Simulation benutzt werden soll
	 * @throws AnmeldeException
	 *             die Anmeldung zum Versand der Auftragsdaten ist
	 *             fehlgeschlagen
	 * @throws DatensendeException
	 *             der Auftrag konnte innerhalb der vorgegebenen Zeit nicht
	 *             versendet werden
	 */
	public void simuliereBaustelleMitMq(final String name,
			final String bemerkung, final MessQuerschnitt mq)
					throws AnmeldeException, DatensendeException {

		getOnlineDatensatz(OdBaustellenSimulationStartenMitMq.class)
		.anmeldenSender(
				OdBaustellenSimulationStartenMitMq.Aspekte.Senden
				.getAspekt());
		final OdBaustellenSimulationStartenMitMq.Daten datum = getOnlineDatensatz(
				OdBaustellenSimulationStartenMitMq.class).erzeugeDatum();
		datum.setName(name);
		datum.setBemerkung(bemerkung);
		datum.setMq(mq);
		try {
			getOnlineDatensatz(OdBaustellenSimulationStartenMitMq.class)
			.sendeDaten(
					OdBaustellenSimulationStartenMitMq.Aspekte.Senden
					.getAspekt(),
					datum, TimeUnit.MINUTES.toMillis(1));
		} catch (final DatensendeException e) {
			getOnlineDatensatz(OdBaustellenSimulationStartenMitMq.class)
			.abmeldenSender(
					OdBaustellenSimulationStartenMitMq.Aspekte.Senden
					.getAspekt());
			throw e;
		}

		getOnlineDatensatz(OdBaustellenSimulationStartenMitMq.class)
		.abmeldenSender(
				OdBaustellenSimulationStartenMitMq.Aspekte.Senden
				.getAspekt());
	}
}
