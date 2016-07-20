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
package de.bsvrz.sys.funclib.bitctrl.text;

// import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ZeitAngabeTest {

	private static ZeitAngabe negative;
	private static ZeitAngabe nullZeit;
	private static ZeitAngabe normalZeit;
	private static ZeitAngabe normalDauer;
	private static ZeitAngabe normalDauer2;
	private static ZeitAngabe maxZeit;

	@BeforeClass
	public static void initCandidates() {
		//		final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CET"), Locale.GERMAN);
		final Calendar cal = Calendar.getInstance();

		cal.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
		cal.add(Calendar.DAY_OF_MONTH, -3);
		negative = new ZeitAngabe(cal.getTimeInMillis());
		nullZeit = new ZeitAngabe(0);

		cal.set(2005, Calendar.DECEMBER, 24, 0, 0, 0);
		normalZeit = new ZeitAngabe(cal.getTimeInMillis());

		normalDauer = new ZeitAngabe((3 * TimeUnit.DAYS.toMillis(1))
				+ (2 * TimeUnit.HOURS.toMillis(1))
				+ (10 * TimeUnit.MINUTES.toMillis(1))
				+ (33 * TimeUnit.SECONDS.toMillis(1)) + 22);
		normalDauer2 = new ZeitAngabe((3 * TimeUnit.DAYS.toMillis(1))
				+ (2 * TimeUnit.HOURS.toMillis(1))
				+ (0 * TimeUnit.MINUTES.toMillis(1))
				+ (33 * TimeUnit.SECONDS.toMillis(1)));

		maxZeit = new ZeitAngabe(Long.MAX_VALUE);
	}



	@Test
	public void testDauerAlsText() {
		Assert.assertEquals("ungültig", negative.dauerAlsText());
		Assert.assertEquals("0 Millisekunden", nullZeit.dauerAlsText());
		Assert.assertEquals(
				"3 Tage 2 Stunden 10 Minuten 33 Sekunden 22 Millisekunden",
				normalDauer.dauerAlsText());
		Assert.assertEquals("3 Tage 2 Stunden 33 Sekunden", normalDauer2
				.dauerAlsText());
		Assert.assertEquals("unbekannt", maxZeit.dauerAlsText());
	}

	@Test
	public void testDauerAlsTextString() {
		Assert.assertEquals("ungültig", negative.dauerAlsText("anders"));
		Assert.assertEquals("0 Millisekunden", nullZeit.dauerAlsText("anders"));
		Assert.assertEquals(
				"3 Tage 2 Stunden 10 Minuten 33 Sekunden 22 Millisekunden",
				normalDauer.dauerAlsText("anders"));
		Assert.assertEquals("3 Tage 2 Stunden 33 Sekunden", normalDauer2
				.dauerAlsText("anders"));
		Assert.assertEquals("anders", maxZeit.dauerAlsText("anders"));
	}

	@Test
	public void testZeitStempel() {
		Assert.assertEquals("29.12.1969 00:00:00", negative.zeitStempel());
		Assert.assertEquals("01.01.1970 01:00:00", nullZeit.zeitStempel());
		Assert.assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel());
		//	Assert.assertEquals("17.08.292278994 08:12:55", maxZeit.zeitStempel());
	}

	@Test
	public void testZeitStempelString() {
		Assert.assertEquals("29.12.1969 00:00:00", negative.zeitStempel(null));
		Assert.assertEquals("01.01.1970 01:00:00", nullZeit.zeitStempel(null));
		Assert.assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel(null));
		//		Assert.assertEquals("17.08.292278994 08:12:55", maxZeit.zeitStempel(null));

		Assert.assertEquals("29.12.1969 00:00:00", negative.zeitStempel("0Wert"));
		Assert.assertEquals("0Wert", nullZeit.zeitStempel("0Wert"));
		Assert.assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel("0Wert"));
		//		Assert.assertEquals("17.08.292278994 08:12:55", maxZeit.zeitStempel("0Wert"));
	}

	@Test
	public void testZeitStempelStringString() {
		Assert.assertEquals("29.12.1969 00:00:00", negative.zeitStempel(null, null));
		Assert.assertEquals("01.01.1970 01:00:00", nullZeit.zeitStempel(null, null));
		Assert.assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel(null, null));
		//		Assert.assertEquals("17.08.292278994 08:12:55", maxZeit
		//	.zeitStempel(null, null));

		Assert.assertEquals("29.12.1969 00:00:00", negative.zeitStempel("0Wert",
				"Ende"));
		Assert.assertEquals("0Wert", nullZeit.zeitStempel("0Wert", "Ende"));
		Assert.assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel("0Wert",
				"Ende"));
		Assert.assertEquals("Ende", maxZeit.zeitStempel("0Wert", "Ende"));
	}
}
