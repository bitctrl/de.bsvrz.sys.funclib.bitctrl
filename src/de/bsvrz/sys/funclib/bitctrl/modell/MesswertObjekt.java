package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;

/**
 * Erweitert ein Systemobjekt um einen oder mehrere Messwerte.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface MesswertObjekt extends SystemObjekt {

	/**
	 * Beschreibung eines Messwerts.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	class Messwert {

		/** Name des Wertes. */
		private final String name;

		/** Bezeichnung des Wertes. */
		private final String bezeichnung;

		/** PID der Attributgruppe mit dem Messwert. */
		private final String pidAtg;

		/** PID des Aspekts unter dem der Messwert gelesen wird. */
		private final String pidAsp;

		/**
		 * Erzeugt die Konstante.
		 * 
		 * @param name
		 *            Name des Messwerts
		 * @param bezeichnung
		 *            Bezeichnung des Wertes
		 * @param pidAtg
		 *            PID der Attributgruppe mit den Messwert. {@code null} ist
		 *            erlaubt und steht f&uuml;r einen virtuellen Meswert, der
		 *            im Datenkatalog nicht existiert.
		 * @param pidAsp
		 *            Aspekt unter dem der Messwert gelesen wird. {@code null}
		 *            ist erlaubt und steht f&uuml;r einen virtuellen Meswert,
		 *            der im Datenkatalog nicht existiert.
		 * @throws NullPointerException
		 *             Wenn {@code name} oder {@code bezeichnung} gleich
		 *             {@code null} sind
		 */
		public Messwert(String name, String bezeichnung, String pidAtg,
				String pidAsp) {
			if (name == null || bezeichnung == null) {
				throw new NullPointerException(
						"Name und Bezeichnung müssen ungleich null sein.");
			}

			this.name = name;
			this.bezeichnung = bezeichnung;
			this.pidAtg = pidAtg;
			this.pidAsp = pidAsp;
		}

		/**
		 * Gibt den Namen des Messwerts zur&uuml;ck.
		 * 
		 * @return Messwertname
		 */
		public String getName() {
			assert name != null;
			return name;
		}

		/**
		 * Gibt die Attributgruppe mit dem Messwert zur&uuml;ck;.
		 * 
		 * @return PID der Attributgruppe oder {@code null}, wenn der Messwert
		 *         im Datenkatalog nicht existiert. Letzteres kann der Fall
		 *         sein, wenn z.&nbsp;B. eine Anwendung den Messwert
		 *         unabh&auml;ngig vom Datenkatalog bestimmt.
		 */
		public String getAtgPID() {
			return pidAtg;
		}

		/**
		 * Gibt den Aspekt zur&uuml;ck; mit dem der Messwert gelesen wird.
		 * 
		 * @return PID der Attributgruppe oder {@code null}, wenn der Messwert
		 *         im Datenkatalog nicht existiert. Letzteres kann der Fall
		 *         sein, wenn z.&nbsp;B. eine Anwendung den Messwert
		 *         unabh&auml;ngig vom Datenkatalog bestimmt.
		 */
		public String getAspPID() {
			return pidAsp;
		}

		/**
		 * Zwei Messwerte sind gleich, wenn sie den selben Namen haben. Wichtig
		 * f&uuml;r das Hashing-Verhalten.
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Messwert) {
				Messwert mw;

				mw = (Messwert) obj;
				return name.equals(mw.name);
			}

			return false;
		}

		/**
		 * Gibt die Bezeichnung des Messwerts zur&uuml;ck.
		 */
		@Override
		public String toString() {
			assert bezeichnung != null;
			return bezeichnung;
		}

	}

	/**
	 * Gibt den Zeitpunkt der Datenerhebung zur&uuml;ck.
	 * 
	 * @return Zeitpunkt als Zeitstempel
	 */
	public abstract long getZeitstempel();

	/**
	 * Legt den Zeitstempel des aktuellen Messwerts fest.
	 * 
	 * @param zeitstempel
	 *            Ein Zeitstempel
	 */
	public void setZeitstempel(long zeitstempel);

	/**
	 * Gibt den Analysewert zur&uuml;ck, der zu dem Messwert geh&ouml;rt.
	 * 
	 * @param wert
	 *            Bezeichner eines Wertes
	 * @return Der Analysewert oder {@code null} f&uuml;r undefiniert
	 */
	Number getWert(Messwert wert);

	/**
	 * Legt einen Analysewert fest.
	 * 
	 * @param wert
	 *            Der Messwerts als Bezeichner des festzulegenden Wertes
	 * @param zahl
	 *            Zahlenwert des Analysewertes
	 */
	void setWert(Messwert wert, Integer zahl);

	/**
	 * Gibt den Messwert mit dem angegeben Namen zur&uuml;ck.
	 * 
	 * @param name
	 *            Name des gesuchten Messwerts
	 * @return Messwert
	 * @throws NoSuchElementException
	 *             Wenn der Name unbekannt ist
	 */
	Messwert getMesswert(String name);

	/**
	 * Gibt die Liste der Messwerte des Objekts zur&uuml;ck.
	 * 
	 * @return Liste von Wertbeschreibungen
	 */
	Collection<Messwert> getMesswerte();

	/**
	 * Extrahiert die Messwerte aus einem (passenden) Datensatz. Die Methode ist
	 * optional.
	 * 
	 * @param datensatz
	 *            Ein Datensatz
	 * @return Menge der aktualliserten Messwerte
	 * @throws UnsupportedOperationException
	 *             Wenn die Operation von der konkreten Implementation nicht
	 *             unterstützt wird.
	 */
	Set<Messwert> setWerte(ResultData datensatz);

	/**
	 * Gibt die Datenbeschreibung zu einem Messwert zur&uuml;ck.
	 * 
	 * @param mw
	 *            Ein Messwert
	 * @return Datenbeschreibung des Messwerts oder {@code null}, wenn der
	 *         Messwert nicht im Datenkatalog existiert.
	 */
	DataDescription getDbs(Messwert mw);

}
