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

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.Collection;
import java.util.Comparator;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Schnittstelle f&uuml;r alle modellierten Systemobjekte. Am wichtigsten ist
 * die Methode {@link #getSystemObject()}, die das gekapselte Systemobjekt des
 * Datenverteilsers zur&uuml;ck gibt. Die anderen sollen lediglich die
 * Schreibweise verk&uuml;rzen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * 
 */
public interface SystemObjekt {

	/**
	 * Klasse zum Vergleich zweier Systemobjekte per PID.
	 * 
	 * @author BitCtrl Systems GmbH, Uwe Peuker
	 * @version $Id$
	 */
	public class PidComparator implements Comparator<SystemObjekt> {

		/**
		 * vergleicht zwei Systemobjekte bezüglich ihrer PID.
		 * 
		 * @param o1
		 *            erstes Objekt
		 * @param o2
		 *            zweites Objekt
		 * @return &lt;0, wenn o1 kleiner als o2 ist; 0, &gt;0, wenn o1 grösser
		 *         als o2 ist; 0 sonst.
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(SystemObjekt o1, SystemObjekt o2) {

			int result = 0;

			if (o1 == null) {
				result = -1;
			} else if (o2 == null) {
				result = 1;
			} else {
				result = o1.getPid().compareTo(o2.getPid());
			}

			return result;
		}
	}

	/**
	 * Gibt die ID des Systemobjekts zur&uuml;ck.
	 * 
	 * @see SystemObject#getId()
	 * @return Die ID
	 */
	long getId();

	/**
	 * Gibt den Namen des Systemobjekts zur&uuml;ck.
	 * 
	 * @see SystemObject#getName()
	 * @return Der Systemobjektname
	 */
	String getName();

	/**
	 * Gibt alle aktuell verwendeten Onlinedatens&auml;tze des Systemobjekts
	 * zur&uuml;ck.
	 * <p>
	 * <em>Hinweis:</em> Die zur&uuml;ckgegebene Menge ist echte Teilmenge (!)
	 * oder identisch mit der Menge der erlaubten Onlinedatens&auml;tze am
	 * Systemobjekt.
	 * 
	 * @return die Menge der aktuell verwendeten Onlinedatens&auml;tze.
	 */

	/**
	 * Gibt alle aktuell verwendeten Onlinedatens&auml;tze des Systemobjekts
	 * zur&uuml;ck.
	 * <p>
	 * <em>Hinweis:</em> Die zur&uuml;ckgegebene Menge ist echte Teilmenge (!)
	 * oder identisch mit der Menge der erlaubten Onlinedatens&auml;tze am
	 * Systemobjekt.
	 * 
	 * @return die Menge der aktuell verwendeten Onlinedatens&auml;tze.
	 */
	Collection<? extends OnlineDatensatz<? extends Datum>> getOnlineDatensatz();

	/**
	 * Gibt einen bestimmten Onlinedatensatz zur&uuml;ck. Es wird gepr&uuml;ft,
	 * ob der Datensatz am Systemobjekt verwendet werden darf.
	 * 
	 * @param typ
	 *            der Typ des Datensatzes.
	 * @param <O>
	 *            Der Typ des Onlinedatensatzes.
	 * @return der Datensatz.
	 */
	<O extends OnlineDatensatz<? extends Datum>> O getOnlineDatensatz(
			Class<O> typ);

	/**
	 * Gibt alle aktuell verwendeten Parameterdatens&auml;tze des Systemobjekts
	 * zur&uuml;ck.
	 * <p>
	 * <em>Hinweis:</em> Die zur&uuml;ckgegebene Menge ist echte Teilmenge (!)
	 * oder identisch mit der Menge der erlaubten Parameterdatens&auml;tze am
	 * Systemobjekt.
	 * 
	 * @return die Menge der aktuell verwendeten Onlinedatens&auml;tze.
	 */
	Collection<? extends ParameterDatensatz<? extends Datum>> getParameterDatensatz();

	/**
	 * Gibt einen bestimmten Parameterdatensatz zur&uuml;ck. Es wird
	 * gepr&uuml;ft, ob der Datensatz am Systemobjekt verwendet werden darf.
	 * 
	 * @param typ
	 *            der Typ des Datensatzes.
	 * @param <D>
	 *            Der Typ des Parameterdatensatzes.
	 * @return der Datensatz.
	 */
	<D extends ParameterDatensatz<? extends Datum>> D getParameterDatensatz(
			Class<D> typ);

	/**
	 * Gibt die PID des Systemobjekts zur&uuml;ck.
	 * 
	 * @see SystemObject#getPid()
	 * @return Die PID als String
	 */
	String getPid();

	/**
	 * Gibt das gekapselte Systemobjekt des Datenverteilers zur&uuml;ck.
	 * 
	 * @see SystemObject#getId()
	 * @return Das Datenverteilersystemobjekt
	 */
	SystemObject getSystemObject();

	/**
	 * Gibt den Typ des Systemobjekts zur&uuml;ck.
	 * 
	 * @return den Typ
	 */
	SystemObjektTyp getTyp();

	/**
	 * Pr&uuml;ft ob das Systemobjekt einen bestimmten Onlinedatensatz besitzt.
	 * 
	 * @param typ
	 *            der Typ des Onlinedatensatzes.
	 * @return {@code true}, wenn der Onlinedatensatz am Systemobjekt verwendet
	 *         werden kann.
	 */
	boolean hasOnlineDatensatz(
			Class<? extends OnlineDatensatz<? extends Datum>> typ);

	/**
	 * Pr&uuml;ft ob das Systemobjekt einen bestimmten Parameterdatensatz
	 * besitzt.
	 * 
	 * @param typ
	 *            der Typ des Parameterdatensatzes.
	 * @return {@code true}, wenn der Parameterdatensatz am Systemobjekt
	 *         verwendet werden kann.
	 */
	boolean hasParameterDatensatz(
			Class<? extends ParameterDatensatz<? extends Datum>> typ);

}
