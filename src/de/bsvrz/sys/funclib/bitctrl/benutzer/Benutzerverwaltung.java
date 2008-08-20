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

package de.bsvrz.sys.funclib.bitctrl.benutzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.event.EventListenerList;

import com.bitctrl.util.CollectionUtilities;
import com.bitctrl.util.Timestamp;

import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.ConfigurationTaskException;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.DynamicObjectType;
import de.bsvrz.dav.daf.main.config.InvalidationListener;
import de.bsvrz.dav.daf.main.config.DynamicObjectType.DynamicObjectCreatedListener;
import de.bsvrz.dav.daf.main.config.management.UserAdministration;
import de.bsvrz.sys.funclib.bitctrl.daf.DavTools;
import de.bsvrz.sys.funclib.bitctrl.modell.AnmeldeException;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensendeException;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.SystemModellGlobalTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Applikation;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Berechtigungsklasse;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Datenverteiler;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Region;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Rolle;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.onlinedaten.AngemeldeteApplikationen;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.onlinedaten.AngemeldeteApplikationen.Daten;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.onlinedaten.AngemeldeteApplikationen.Daten.AngemeldeteApplikation;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.parameter.PdBenutzerParameter;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.parameter.PdRollenRegionenPaareParameter;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.parameter.PdRollenRegionenPaareParameter.Daten.RolleRegionPaar;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Verwaltet die Benutzer des Datenverteilers.
 * 
 * @todo Falls nötig, mit verschiedenen Dav-Anmeldungen arbeiten.
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public final class Benutzerverwaltung {

	/** PID der Berechtigungsklasse ohne jede Zugriffsrechte. */
	public static final String PID_KEIN_ZUGRIFF = "berechtigungsklasse.keinZugriff";

	/** PID der Berechtigungsklasse mit allen Zugriffsrechten. */
	public static final String PID_ADMINISTRATOR = "berechtigungsklasse.administrator";

	/** PID der Zugriffsregion für alle Objekte. */
	public static final String PID_ROLLE_ALLES = "region.alles";

	/** Das Singleton der Klasse. */
	private static Benutzerverwaltung singleton;

	/**
	 * Gibt die einzige Instanz der Klasse zurück.
	 * 
	 * @return die Nutzerverwaltung als Singleton.
	 */
	public static Benutzerverwaltung getInstanz() {
		if (singleton == null) {
			singleton = new Benutzerverwaltung();
		}
		return singleton;
	}

	/**
	 * Kapselt den Empfang von Login/Logout-Meldungen.
	 */
	private class PrivateListener implements DatensatzUpdateListener,
			DynamicObjectCreatedListener, InvalidationListener {

		/** Das zuletzt empfangene Datum. */
		private AngemeldeteApplikationen.Daten letztesDatum = new AngemeldeteApplikationen.Daten();

		/**
		 * {@inheritDoc}
		 */
		public void datensatzAktualisiert(final DatensatzUpdateEvent event) {
			if (event.getDatum() instanceof AngemeldeteApplikationen.Daten) {
				final AngemeldeteApplikationen.Daten aktuellesDatum = (Daten) event
						.getDatum();

				for (final AngemeldeteApplikationen.Daten.AngemeldeteApplikation app : CollectionUtilities
						.difference(aktuellesDatum, letztesDatum)) {
					fireAngemeldet(app.getBenutzer(), app.getApplikation(), app
							.getSeit());
				}

				for (final AngemeldeteApplikationen.Daten.AngemeldeteApplikation app : CollectionUtilities
						.difference(letztesDatum, aktuellesDatum)) {
					fireAbgemeldet(app.getBenutzer(), app.getApplikation(), app
							.getSeit());
				}

				letztesDatum = aktuellesDatum;
			} else if (event.getDatum() instanceof PdBenutzerParameter.Daten) {
				final Benutzer benutzer = (Benutzer) event.getObjekt();
				final Berechtigungsklasse berechtigungsklasse = ((PdBenutzerParameter.Daten) event
						.getDatum()).getBerechtigungsklasse();
				fireBenutzerChanged(benutzer, berechtigungsklasse);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void objectCreated(final DynamicObject createdObject) {
			final ObjektFactory factory = ObjektFactory.getInstanz();
			final Benutzer benutzer = (Benutzer) factory
					.getModellobjekt(createdObject);
			final PdBenutzerParameter param = benutzer
					.getParameterDatensatz(PdBenutzerParameter.class);

			param.addUpdateListener(this);
			fireBenutzerAdded(benutzer);
		}

		/**
		 * {@inheritDoc}
		 */
		public void invalidObject(final DynamicObject dynamicObject) {
			final ObjektFactory factory = ObjektFactory.getInstanz();
			final Benutzer benutzer = (Benutzer) factory
					.getModellobjekt(dynamicObject);
			final PdBenutzerParameter param = benutzer
					.getParameterDatensatz(PdBenutzerParameter.class);

			param.removeUpdateListener(this);
			fireBenutzerRemoved(benutzer);
		}

	}

	/** Der Logger der Klasse. */
	private final Debug log;

	/** Die angemeldeten Listener der Klasse. */
	private final EventListenerList listeners;

	/**
	 * Inituialisierung.
	 */
	private Benutzerverwaltung() {
		log = Debug.getLogger();
		listeners = new EventListenerList();

		final PrivateListener privateListener = new PrivateListener();

		final ObjektFactory factory = ObjektFactory.getInstanz();
		final Datenverteiler dav = (Datenverteiler) factory
				.getModellobjekt(factory.getVerbindung().getLocalDav());
		final AngemeldeteApplikationen datensatz = dav
				.getOnlineDatensatz(AngemeldeteApplikationen.class);

		// Listener auf An- und Abmelden
		datensatz.addUpdateListener(AngemeldeteApplikationen.Aspekte.Standard
				.getAspekt(), privateListener);

		// Listener auf Änderungen der Menge der Benutzer
		final DynamicObjectType typ = (DynamicObjectType) factory
				.getModellobjekt(SystemModellGlobalTypen.Benutzer.getPid())
				.getSystemObject().getType();
		typ.addObjectCreationListener(privateListener);
		typ.addInvalidationListener(privateListener);

		// Listener auf Änderungen der Berechtigungsklasse der Nutzer
		for (final SystemObjekt so : factory
				.bestimmeModellobjekte(SystemModellGlobalTypen.Benutzer
						.getPid())) {
			if (so instanceof Benutzer) {
				final Benutzer benutzer = (Benutzer) so;
				benutzer.getParameterDatensatz(PdBenutzerParameter.class)
						.addUpdateListener(privateListener);
			}
		}

		log.info("Schnittstelle zur Nutzerverwaltung initialisiert.");
	}

	/**
	 * Registriert einen Listener an.
	 * 
	 * @param l
	 *            ein Listener.
	 */
	public void addLoginListener(final LoginListener l) {
		listeners.add(LoginListener.class, l);
	}

	/**
	 * Meldet einen Listener wieder ab.
	 * 
	 * @param l
	 *            ein Listener.
	 */
	public void removeLoginListener(final LoginListener l) {
		listeners.remove(LoginListener.class, l);
	}

	/**
	 * Benachrichtigt die angmeldeten Listener über die Anmeldung eines
	 * Benutzers.
	 * 
	 * @param benutzer
	 *            der betroffene Benutzer.
	 * @param applikation
	 *            die betroffene Applikation.
	 * @param anmeldezeit
	 *            der Anmeldezeitpunkt.
	 */
	protected synchronized void fireAngemeldet(final Benutzer benutzer,
			final Applikation applikation, final Timestamp anmeldezeit) {
		final LoginEvent e = new LoginEvent(this, benutzer, applikation,
				anmeldezeit);

		for (final LoginListener l : listeners
				.getListeners(LoginListener.class)) {
			l.angemeldet(e);
		}
	}

	/**
	 * Benachrichtigt die angmeldeten Listener darüber, dass sich ein Benutzer
	 * abgemeldet hat.
	 * 
	 * @param benutzer
	 *            der betroffene Benutzer.
	 * @param applikation
	 *            die betroffene Applikation.
	 * @param anmeldezeit
	 *            der Anmeldezeitpunkt.
	 */
	protected synchronized void fireAbgemeldet(final Benutzer benutzer,
			final Applikation applikation, final Timestamp anmeldezeit) {
		final LoginEvent e = new LoginEvent(this, benutzer, applikation,
				anmeldezeit);

		for (final LoginListener l : listeners
				.getListeners(LoginListener.class)) {
			l.abgemeldet(e);
		}
	}

	/**
	 * Registriert einen Listener an.
	 * 
	 * @param l
	 *            ein Listener.
	 */
	public void addBenutzerListener(final BenutzerListener l) {
		listeners.add(BenutzerListener.class, l);
	}

	/**
	 * Meldet einen Listener wieder ab.
	 * 
	 * @param l
	 *            ein Listener.
	 */
	public void removeBenutzerListener(final BenutzerListener l) {
		listeners.remove(BenutzerListener.class, l);
	}

	/**
	 * Benachrichtigt die angmeldeten Listener darüber, dass ein neuer Benutzer
	 * angelegt wurde.
	 * 
	 * @param benutzer
	 *            der betroffene Benutzer.
	 */
	protected synchronized void fireBenutzerAdded(final Benutzer benutzer) {
		final BenutzerEvent e = new BenutzerEvent(this, benutzer, null);

		for (final BenutzerListener l : listeners
				.getListeners(BenutzerListener.class)) {
			l.benutzerAdded(e);
		}
	}

	/**
	 * Benachrichtigt die angmeldeten Listener darüber, dass ein Benutzer
	 * gelöscht wurde.
	 * 
	 * @param benutzer
	 *            der betroffene Benutzer.
	 */
	protected synchronized void fireBenutzerRemoved(final Benutzer benutzer) {
		final BenutzerEvent e = new BenutzerEvent(this, benutzer, null);

		for (final BenutzerListener l : listeners
				.getListeners(BenutzerListener.class)) {
			l.benutzerRemoved(e);
		}
	}

	/**
	 * Benachrichtigt die angmeldeten Listener darüber, dass die
	 * Berechtigungsklasse eines Benutzer geändert wurde.
	 * 
	 * @param benutzer
	 *            der betroffene Benutzer.
	 * @param berechtigungsklasse
	 *            die Berechtigungsklasse des Benutzers.
	 */
	protected synchronized void fireBenutzerChanged(final Benutzer benutzer,
			final Berechtigungsklasse berechtigungsklasse) {
		final BenutzerEvent e = new BenutzerEvent(this, benutzer,
				berechtigungsklasse);

		for (final BenutzerListener l : listeners
				.getListeners(BenutzerListener.class)) {
			l.benutzerChanged(e);
		}
	}

	/**
	 * Gibt die Liste aller aktuell gültigen Anmeldungen eines Benutzers zurück.
	 * 
	 * @param benutzer
	 *            ein Benutzer.
	 * @return die Liste der Anmeldungen des Benutzers.
	 */
	public List<AngemeldeteApplikation> getAnmeldungen(final Benutzer benutzer) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		final Datenverteiler dav = factory.getDatenverteiler();
		final AngemeldeteApplikationen.Daten datum = dav.getOnlineDatensatz(
				AngemeldeteApplikationen.class).abrufenDatum(
				AngemeldeteApplikationen.Aspekte.Standard.getAspekt());
		final List<AngemeldeteApplikation> apps = new ArrayList<AngemeldeteApplikation>();

		for (final AngemeldeteApplikation app : datum) {
			if (benutzer.equals(app.getBenutzer())) {
				apps.add(app);
			}
		}

		return datum;
	}

	/**
	 * Gibt die Anmeldung zu einer Applikation zurück. Der Rückgabewert kann
	 * {@code null} sein, wenn es keine Anmeldung mehr zu dieser Applikation
	 * gibt, dass heißt sie wurde beendet.
	 * 
	 * @param applikation
	 *            eine Applikation.
	 * @return die aktuelle Anmeldung der Applikation.
	 */
	public AngemeldeteApplikation getAnmeldungen(final Applikation applikation) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		final Datenverteiler dav = factory.getDatenverteiler();
		final AngemeldeteApplikationen.Daten datum = dav.getOnlineDatensatz(
				AngemeldeteApplikationen.class).abrufenDatum(
				AngemeldeteApplikationen.Aspekte.Standard.getAspekt());

		for (final AngemeldeteApplikation app : datum) {
			if (applikation.equals(app.getApplikation())) {
				return app;
			}
		}

		return null;
	}

	/**
	 * Gibt den lokal angemeldeten Benutzer zurück.
	 * 
	 * @return der angemeldete Benutzer.
	 */
	public Benutzer getAngemeldetenBenutzer() {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		return (Benutzer) factory.getModellobjekt(factory.getVerbindung()
				.getLocalUser());
	}

	/**
	 * Gibt eine unveränderbare Liste aller Benutzer im System zurück.
	 * 
	 * @return die Benutzerliste.
	 */
	public List<Benutzer> getBenutzer() {
		final List<Benutzer> benutzer = new ArrayList<Benutzer>();
		final ObjektFactory factory = ObjektFactory.getInstanz();

		for (final SystemObjekt so : factory
				.bestimmeModellobjekte(SystemModellGlobalTypen.Benutzer
						.getPid())) {
			benutzer.add((Benutzer) so);
		}

		return Collections.unmodifiableList(benutzer);
	}

	/**
	 * Gibt eine unveränderbare Liste aller Berechtigungsklassen im System
	 * zurück.
	 * 
	 * @return die Liste der Berechtigungsklassen.
	 */
	public List<Berechtigungsklasse> getBerechtigungsklasse() {
		final List<Berechtigungsklasse> klassen = new ArrayList<Berechtigungsklasse>();
		final ObjektFactory factory = ObjektFactory.getInstanz();

		for (final SystemObjekt so : factory
				.bestimmeModellobjekte(SystemModellGlobalTypen.Berechtigungsklasse
						.getPid())) {
			klassen.add((Berechtigungsklasse) so);
		}

		return Collections.unmodifiableList(klassen);
	}

	/**
	 * Gibt eine Zusammenstellung aller Benutzer im System und deren
	 * Benutzerklasse zurück.
	 * 
	 * @return die aktuelle Rechteverteilung.
	 */
	public Map<Benutzer, Berechtigungsklasse> getBenutzerUndKlassen() {
		final Map<Benutzer, Berechtigungsklasse> rechte = new HashMap<Benutzer, Berechtigungsklasse>();

		for (final Benutzer benutzer : getBenutzer()) {
			rechte.put(benutzer, getBerechtigungsklasse(benutzer));
		}

		return rechte;
	}

	/**
	 * Prüft, ob ein Bennutzer für Administratoraufgaben festgelegt wurde und
	 * dieser über ausreichend Rechte verfügt.
	 * 
	 * @param loginname
	 *            ein beliebiger Nutzername
	 * @return {@code true}, wenn ein Bennutzer für Administratoraufgaben
	 *         angegeben ist.
	 */
	public boolean checkAdmin(final String loginname) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		final Benutzer benutzer = (Benutzer) factory.getModellobjekt(DavTools
				.generierePID(loginname, Benutzer.PRAEFIX_PID));
		final PdBenutzerParameter parameter = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datum = parameter.abrufenDatum();

		if (datum.isValid()) {
			return datum.getBerechtigungsklasse().getPid().equals(
					PID_ADMINISTRATOR);
		}

		return false;
	}

	/**
	 * Prüft ob ein bestimmter Benutzer zu einer bestimmten Berechtigungsklasse
	 * gehört.
	 * 
	 * @param benutzer
	 *            ein Benutzer.
	 * @param klasse
	 *            eine Berechtuigungsklasse.
	 * @return {@code true}, wenn der Benutzer zu der Berechtigungsklasse
	 *         gehört.
	 */
	public boolean isBerechtigungsklasse(final Benutzer benutzer,
			final Berechtigungsklasse klasse) {
		return klasse.equals(getBerechtigungsklasse(benutzer));
	}

	/**
	 * Bestimmt die aktuelle Berechtigungsklasse des Benutzers.
	 * 
	 * @param benutzer
	 *            ein Benutzer.
	 * @return die Berechtigungsklasse des Benutzers.
	 */
	public Berechtigungsklasse getBerechtigungsklasse(final Benutzer benutzer) {
		final PdBenutzerParameter parameter = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datum = parameter.abrufenDatum();

		if (datum.isValid()) {
			return datum.getBerechtigungsklasse();
		}

		return null;
	}

	/**
	 * Prüft ob ein bestimmter Benutzer eine bestimmte Rolle in einer Region
	 * hat.
	 * 
	 * @param benutzer
	 *            ein Benutzer.
	 * @param rolle
	 *            eine Zugriffsrolle.
	 * @param region
	 *            eine Zugriffsregion.
	 * @return {@code true}, wenn der Benutzer in der Region die angegebene
	 *         Rolle hat.
	 */
	public boolean isRolleUndRegion(final Benutzer benutzer, final Rolle rolle,
			final Region region) {
		final PdBenutzerParameter paramBenutzer = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datumBenutzer = paramBenutzer
				.abrufenDatum();
		final Berechtigungsklasse klasse = datumBenutzer
				.getBerechtigungsklasse();
		final PdRollenRegionenPaareParameter paramPaare = klasse
				.getParameterDatensatz(PdRollenRegionenPaareParameter.class);
		final PdRollenRegionenPaareParameter.Daten datumPaare = paramPaare
				.abrufenDatum();

		for (final RolleRegionPaar paar : datumPaare) {
			if (region.equals(paar.getRegion())
					&& rolle.equals(paar.getRolle())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Suchte eine bestimmte Berechtigungsklasse.
	 * 
	 * @param pid
	 *            die PID einer Berechtigungsklasse.
	 * @return die Berechtigungsklasse oder {@code null}, wenn zu der PID keine
	 *         existiert.
	 */
	public Berechtigungsklasse getBerechtigungsklasse(final String pid) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		return (Berechtigungsklasse) factory.getModellobjekt(pid);
	}

	/**
	 * Suchte eine bestimmte Zugriffsrolle.
	 * 
	 * @param pid
	 *            die PID einer Zugriffsrolle.
	 * @return die Zugriffsrolle oder {@code null}, wenn zu der PID keine
	 *         existiert.
	 */
	public Rolle getRolle(final String pid) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		return (Rolle) factory.getModellobjekt(pid);
	}

	/**
	 * Suchte eine bestimmte Zugriffsregion.
	 * 
	 * @param pid
	 *            die PID einer Zugriffsregion.
	 * @return die Zugriffsregion oder {@code null}, wenn zu der PID keine
	 *         existiert.
	 */
	public Region getRegion(final String pid) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		return (Region) factory.getModellobjekt(pid);
	}

	/**
	 * Prüft ob ein bestimmter Benutzer existiert und gibt ihn zurück.
	 * 
	 * @param loginname
	 *            der eindeutige Benutzername (Loginname).
	 * @return der Benutzer oder {@code null}, wenn kein Benutzer mit dem
	 *         angegebenen Namen existiert.
	 */
	public Benutzer getBenutzer(final String loginname) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		final String pid = DavTools.generierePID(loginname,
				Benutzer.PRAEFIX_PID);
		Benutzer benutzer = (Benutzer) factory.getModellobjekt(pid);

		if (benutzer != null) {
			return benutzer;
		}

		// Suche per PID erfolglos, weiter nach Namen suchen
		for (final SystemObjekt so : factory
				.bestimmeModellobjekte(SystemModellGlobalTypen.Benutzer
						.getPid())) {
			benutzer = (Benutzer) so;

			if (benutzer.getName().equals(loginname)) {
				break;
			}
		}

		log.fine("Die PID des Benutzers " + benutzer
				+ " beginnt nicht mit dem Standardprefix "
				+ Benutzer.PRAEFIX_PID);
		return benutzer;
	}

	/**
	 * Sucht alle Benutzer auf die bestimmte Kriterien zutreffen. Die
	 * Suchkriterien dürfen auch {@code null} sein, dies wird als Wildcard
	 * "alle" gedeutet. Die einzelnen Kriterien werden "oder"-verknüpft.
	 * 
	 * @param nachname
	 *            der Nachname der gesuchten Benutzer.
	 * @param vorname
	 *            der Vorname der gesuchten Benutzer.
	 * @param zweiterVorname
	 *            der zweite Vorname der gesuchten Benutzer.
	 * @param organisation
	 *            die Organisation der gesuchten Benutzer.
	 * @param email
	 *            die E-Mail-Adresse der gesuchten Benutzer.
	 * @return die Liste der gefundenen Benutzer, niemals {@code null}.
	 */
	public List<Benutzer> sucheBenutzer(final String nachname,
			final String vorname, final String zweiterVorname,
			final String organisation, final String email) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		final List<Benutzer> benutzer = new ArrayList<Benutzer>();

		for (final SystemObjekt so : factory
				.bestimmeModellobjekte(SystemModellGlobalTypen.Benutzer
						.getPid())) {
			final Benutzer b = (Benutzer) so;
			boolean ok = false;

			if (nachname == null || nachname.equals(b.getNachname())) {
				ok = true;
			} else if (vorname == null || vorname.equals(b.getVorname())) {
				ok = true;
			} else if (organisation == null
					|| zweiterVorname.equals(b.getZweiterVorname())) {
				ok = true;
			} else if (organisation == null
					|| organisation.equals(b.getOrganisation())) {
				ok = true;
			} else if (email == null || email.equals(b.getEmailAdresse())) {
				ok = true;
			}

			if (ok) {
				benutzer.add(b);
			}
		}

		return benutzer;
	}

	/**
	 * Legt einen neuen Benutzer an.
	 * <p>
	 * <em>Hinweis:</em> Vor dem Aufruf dieser Methode muss mit
	 * {@link #setAdmin(String, String)} ein berechtigter Administrator
	 * festgelegt werden. Am Ende der Methode wird die Anmeldung als
	 * Administrator wieder aufgehoben.
	 * 
	 * @param adminLoginname
	 *            der Name des Administrators der die Aktion ausführt.
	 * @param adminPasswort
	 *            das Anmeldekennwort des Administrators der die Aktion
	 *            ausführt.
	 * @param benutzerInfo
	 *            die Eigenschaften des neuen Benutzers.
	 * @return der neue Benutzer
	 * @throws KeineRechteException
	 *             wenn kein Administrator festgelegt wurde oder dieser nicht
	 *             über genügend Rechte verfügt.
	 * @throws BenutzerChangeException
	 *             wenn beim Anlegen des Benutzers ein Fehler eintrat.
	 * @see #setAdmin(String, String)
	 */
	public Benutzer anlegenBenutzer(final String adminLoginname,
			final String adminPasswort, final BenutzerInfo benutzerInfo)
			throws KeineRechteException, BenutzerChangeException {
		if (!checkAdmin(adminLoginname)) {
			throw new KeineRechteException(
					"Zum Anlegen eines neuen Benutzer, müssen Sie sich als Administrator anmelden und über ausreichend Rechte verfügen.");
		}

		final String pid;
		Benutzer benutzer;

		pid = DavTools.generierePID(benutzerInfo.getLoginname(),
				Benutzer.PRAEFIX_PID);
		try {
			benutzer = Benutzer.anlegen(pid, benutzerInfo.getLoginname(),
					benutzerInfo.getVorname(),
					benutzerInfo.getZweiterVorname(), benutzerInfo
							.getNachname(), benutzerInfo.getOrganisation(),
					benutzerInfo.getEmailAdresse());

			final ObjektFactory factory = ObjektFactory.getInstanz();
			final DataModel modell = factory.getVerbindung().getDataModel();
			final UserAdministration userAdmin = modell.getUserAdministration();
			userAdmin.createNewUser(adminLoginname, adminPasswort, benutzerInfo
					.getLoginname(), pid, benutzerInfo.getPasswort(), false,
					factory.getVerbindung().getLocalConfigurationAuthority()
							.getConfigurationArea().getPid());
		} catch (final ConfigurationChangeException ex) {
			throw new BenutzerChangeException("Der Benutzer "
					+ benutzerInfo.getLoginname()
					+ " konnte nicht angelegt werden.", ex);
		} catch (final ConfigurationTaskException ex) {
			throw new BenutzerChangeException("Der Benutzer "
					+ benutzerInfo.getLoginname()
					+ " konnte nicht angelegt werden.", ex);
		}

		return benutzer;
	}

	/**
	 * Ändert die Berechtigungsklasse eines Benutzer.
	 * 
	 * @param adminLoginname
	 *            der Name des Administrators der die Aktion ausführt.
	 * @param adminPasswort
	 *            das Anmeldekennwort des Administrators der die Aktion
	 *            ausführt.
	 * @param benutzer
	 *            der zu deaktivierende Benutzers.
	 * @param klasse
	 *            die zu setzende Berechtigungsklasse.
	 * @throws KeineRechteException
	 *             wenn kein Administrator festgelegt wurde oder dieser nicht
	 *             über genügend Rechte verfügt.
	 * @throws BenutzerChangeException
	 *             wenn beim Ändern der Benutzerrechte ein Fehler eintrat.
	 */
	public void setBerechtigungsklasse(final String adminLoginname,
			final String adminPasswort, final Benutzer benutzer,
			final Berechtigungsklasse klasse) throws KeineRechteException,
			BenutzerChangeException {
		if (!checkAdmin(adminLoginname)) {
			throw new KeineRechteException(
					"Zum Anlegen eines neuen Benutzer, müssen Sie sich als Administrator anmelden und überausreichend Rechte verfügen.");
		}

		final PdBenutzerParameter parameter = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datum = parameter.erzeugeDatum();
		datum.setBerechtigungsklasse(klasse);
		try {
			parameter.anmeldenSender();
			parameter.sendeDaten(datum);

			try {
				if (PID_ADMINISTRATOR.equals(klasse.getPid())) {
					final DataModel modell = ObjektFactory.getInstanz()
							.getVerbindung().getDataModel();
					final UserAdministration userAdmin = modell
							.getUserAdministration();
					userAdmin.changeUserRights(adminLoginname, adminPasswort,
							benutzer.getName(), true);
				} else {
					final DataModel modell = ObjektFactory.getInstanz()
							.getVerbindung().getDataModel();
					final UserAdministration userAdmin = modell
							.getUserAdministration();
					userAdmin.changeUserRights(adminLoginname, adminPasswort,
							benutzer.getName(), false);
				}
			} catch (final ConfigurationTaskException ex) {
				throw new BenutzerChangeException(
						"Die Adminrechte für den Benutzer " + benutzer
								+ " konnten nicht geändert werden.", ex);
			}
		} catch (final AnmeldeException ex) {
			throw new BenutzerChangeException(
					"Fehler beim Anmelden auf Parameter für Benutzer "
							+ benutzer + ".", ex);
		} catch (final DatensendeException ex) {
			throw new BenutzerChangeException(
					"Fehler beim Senden des Parameters für Benutzer "
							+ benutzer + ".", ex);
		} finally {
			parameter.abmeldenSender();
		}
	}

	/**
	 * Deaktiviert einen Benutzer. Seine Berechtigungsklasse wird auf "Kein
	 * Zugriff" gesetzt.
	 * 
	 * @param adminLoginname
	 *            der Name des Administrators der die Aktion ausführt.
	 * @param adminPasswort
	 *            das Anmeldekennwort des Administrators der die Aktion
	 *            ausführt.
	 * @param benutzer
	 *            der zu deaktivierende Benutzers.
	 * @throws KeineRechteException
	 *             wenn kein Administrator festgelegt wurde oder dieser nicht
	 *             über genügend Rechte verfügt.
	 * @throws BenutzerChangeException
	 *             wenn es beim deaktivieren einen Fehler gab.
	 */
	public void deaktiviereBenutzer(final String adminLoginname,
			final String adminPasswort, final Benutzer benutzer)
			throws KeineRechteException, BenutzerChangeException {
		if (!checkAdmin(adminLoginname)) {
			throw new KeineRechteException(
					"Zum Anlegen eines neuen Benutzer, müssen Sie sich als Administrator anmelden und überausreichend Rechte verfügen.");
		}

		final ObjektFactory factory = ObjektFactory.getInstanz();
		final Berechtigungsklasse klasse = (Berechtigungsklasse) factory
				.getModellobjekt(PID_KEIN_ZUGRIFF);
		final PdBenutzerParameter parameter = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datum = parameter.erzeugeDatum();
		datum.setBerechtigungsklasse(klasse);
		try {
			parameter.anmeldenSender();
			parameter.sendeDaten(datum);
		} catch (final AnmeldeException ex) {
			throw new BenutzerChangeException(
					"Fehler beim Anmelden auf Parameter für Benutzer "
							+ benutzer + ".", ex);
		} catch (final DatensendeException ex) {
			throw new BenutzerChangeException(
					"Fehler beim Senden des Parameters für Benutzer "
							+ benutzer + ".", ex);
		} finally {
			parameter.abmeldenSender();
		}
	}

}
