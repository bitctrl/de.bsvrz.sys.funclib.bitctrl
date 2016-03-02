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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.verkehr;

import static de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitMq.Daten.Werte.KB;
import static de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitMq.Daten.Werte.QKfz;
import static de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitMq.Daten.Werte.QLkw;
import static de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitMq.Daten.Werte.SKfz;
import static de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitMq.Daten.Werte.VLkw;
import static de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitMq.Daten.Werte.VPkw;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitMq;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.TestDatenGenerator;

/**
 * Testdatengenerator f&uuml;r Verkehrskurzzeitdaten am Messquerschnitt.
 *
 * TODO auch zuf‰llig null-Werte generieren
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class VerkehrsDatenKurzZeitMqGenerator
implements TestDatenGenerator<OdVerkehrsDatenKurzZeitMq.Daten> {

	/** Der Zufallszahlengenerator. */
	private final Random datenquelle = new Random();

	/** Sichert die List der Minimas. */
	private final Map<String, Integer> minimas = new HashMap<String, Integer>();

	/** Sichert die List der Maximas. */
	private final Map<String, Integer> maximas = new HashMap<String, Integer>();

	/**
	 * Initialisiert das Objekt.
	 *
	 */
	public VerkehrsDatenKurzZeitMqGenerator() {
		minimas.put(QKfz.name(), 0);
		minimas.put(QLkw.name(), 0);
		minimas.put(VPkw.name(), 0);
		minimas.put(VLkw.name(), 0);
		minimas.put(SKfz.name(), 0);
		minimas.put(KB.name(), 0);

		maximas.put(QKfz.name(), 10000);
		maximas.put(QLkw.name(), 10000);
		maximas.put(VPkw.name(), 254);
		maximas.put(VLkw.name(), 254);
		maximas.put(SKfz.name(), 254);
		maximas.put(KB.name(), 1000);
	}

	@Override
	public OdVerkehrsDatenKurzZeitMq.Daten generiere() {
		OdVerkehrsDatenKurzZeitMq.Daten datum;
		int bandbreite;
		int qLkw, qKfz;

		datum = new OdVerkehrsDatenKurzZeitMq.Daten();

		bandbreite = getMaxWert(QKfz.name()).intValue()
				- getMinWert(QKfz.name()).intValue();
		qKfz = getMinWert(QKfz.name()).intValue()
				+ (int) (datenquelle.nextDouble() * bandbreite);
		datum.setWert(QKfz.name(), qKfz);

		if (getMinWert(QLkw.name()).intValue() > getMaxWert(QKfz.name())
				.intValue()) {
			// Sonderfall, um Endlosschleife zu vermeiden, wenn Minimum(QLkw) >
			// Maximum(QKfz)
			qLkw = getMaxWert(QKfz.name()).intValue();
		} else {
			bandbreite = getMaxWert(QLkw.name()).intValue()
					- getMinWert(QLkw.name()).intValue();
			do {
				qLkw = getMinWert(QLkw.name()).intValue()
						+ (int) (datenquelle.nextDouble() * bandbreite);
			} while (qLkw > qKfz);
		}
		datum.setWert(QLkw.name(), qLkw);

		bandbreite = getMaxWert(VPkw.name()).intValue()
				- getMinWert(VPkw.name()).intValue();
		datum.setWert(VPkw.name(), getMinWert(VPkw.name()).intValue()
				+ (int) (datenquelle.nextDouble() * bandbreite));

		bandbreite = getMaxWert(VLkw.name()).intValue()
				- getMinWert(VLkw.name()).intValue();
		datum.setWert(VLkw.name(), getMinWert(VLkw.name()).intValue()
				+ (int) (datenquelle.nextDouble() * bandbreite));

		bandbreite = getMaxWert(SKfz.name()).intValue()
				- getMinWert(SKfz.name()).intValue();
		datum.setWert(SKfz.name(), getMinWert(SKfz.name()).intValue()
				+ (int) (datenquelle.nextDouble() * bandbreite));

		bandbreite = getMaxWert(KB.name()).intValue()
				- getMinWert(KB.name()).intValue();
		datum.setWert(KB.name(), getMinWert(KB.name()).intValue()
				+ (int) (datenquelle.nextDouble() * bandbreite));

		return datum;
	}

	@Override
	public Number getMaxWert(final String name) {
		return maximas.get(name);
	}

	@Override
	public Number getMinWert(final String name) {
		return minimas.get(name);
	}

	@Override
	public void setMaxWert(final String name, final Number max) {
		maximas.put(name, max.intValue());
	}

	@Override
	public void setMinWert(final String name, final Number min) {
		minimas.put(name, min.intValue());
	}

}
