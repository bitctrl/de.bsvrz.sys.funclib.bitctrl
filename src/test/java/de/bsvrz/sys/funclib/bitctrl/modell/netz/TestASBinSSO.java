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

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Testfall Netzreferenzumrechnung ASB in Segment-Und-Offset.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */

@Ignore("Test überprüfen - NetzTest.init funktioniert nicht")
public class TestASBinSSO {

	/**
	 * .
	 *
	 * @throws java.lang.Exception
	 *             bei Ausnahmen
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Netzreferenzen-Prüfung");
		NetzTests.init();
	}

	/**
	 * .
	 *
	 * @throws java.lang.Exception
	 *             bei Ausnahmen
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUmrechnung() {
		try {
			NetzReferenzen.getInstanz().init(NetzTests.getVerbindung(), NetzTests.getKonfig().getString("Netz"));

			final String anfangsknoten = NetzTests.getKonfig().getString("Anfangsknoten");
			final String endknoten = NetzTests.getKonfig().getString("Endknoten");
			final long stationierung = Long.parseLong(NetzTests.getKonfig().getString("Stationierung"));
			final String richtung = NetzTests.getKonfig().getString("StationierungsRichtung");
			NetzInterface.ASBStationierungsRichtung asbRichtung = null;
			if (richtung.equalsIgnoreCase("IN_STATIONIERUNGSRICHTUNG")) {
				asbRichtung = NetzInterface.ASBStationierungsRichtung.IN_STATIONIERUNGSRICHTUNG;
			} else if (richtung.equalsIgnoreCase("GEGEN_STATIONIERUNGSRICHTUNG")) {
				asbRichtung = NetzInterface.ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG;
			} else {
				fail("Die ASB-Stationierungsrichtung '" + richtung + "' ist nicht zulässig");
			}

			System.out.println("Test Referenzierungsumrechnung ASB in Segment und Offset");
			System.out.println("Anfangsknoten: " + anfangsknoten);
			System.out.println("Endknoten:     " + endknoten);
			System.out.println("Stationierung: " + stationierung);
			System.out.println("Richtung:      " + asbRichtung);

			final AsbStationierungOrtsReferenz asbref = new AsbStationierungOrtsReferenz(anfangsknoten, endknoten,
					asbRichtung, stationierung);

			assertNotNull(asbref);

			final List<StrassenSegmentUndOffsetOrtsReferenzInterface> soref = asbref
					.ermittleOrtsReferenzStrassenSegmentUndOffset();

			assertNotNull(soref);

			for (final StrassenSegmentUndOffsetOrtsReferenzInterface ref : soref) {
				System.out.println("Ortsreferenz:");
				System.out.println("Segment: " + ref.getStrassenSegment().getPid());
				System.out.println("Offset:  " + ref.getStartOffset());
			}
		} catch (final Exception e) {
			System.err.println(e.getMessage());
			fail();
		}

	}
}
