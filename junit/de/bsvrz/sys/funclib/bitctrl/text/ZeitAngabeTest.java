package de.bsvrz.sys.funclib.bitctrl.text;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.junit.BeforeClass;

import com.bitctrl.Constants;

public class ZeitAngabeTest {

	ZeitAngabe negative = new ZeitAngabe(new GregorianCalendar(1970,
			Calendar.JANUARY, 1).getTimeInMillis()
			- (3 * Constants.MILLIS_PER_DAY));
	ZeitAngabe nullZeit = new ZeitAngabe(0);
	ZeitAngabe normalZeit = new ZeitAngabe(new GregorianCalendar(2005,
			Calendar.DECEMBER, 24).getTimeInMillis());
	ZeitAngabe normalDauer = new ZeitAngabe((3 * Constants.MILLIS_PER_DAY)
			+ (2 * Constants.MILLIS_PER_HOUR)
			+ (10 * Constants.MILLIS_PER_MINUTE)
			+ (33 * Constants.MILLIS_PER_SECOND) + 22);
	ZeitAngabe normalDauer2 = new ZeitAngabe((3 * Constants.MILLIS_PER_DAY)
			+ (2 * Constants.MILLIS_PER_HOUR)
			+ (0 * Constants.MILLIS_PER_MINUTE)
			+ (33 * Constants.MILLIS_PER_SECOND));
	ZeitAngabe maxZeit = new ZeitAngabe(Long.MAX_VALUE);

	@Test
	public void testDauerAlsText() {
		assertEquals("ungültig", negative.dauerAlsText());
		assertEquals("0 Millisekunden", nullZeit.dauerAlsText());
		assertEquals(
				"3 Tage 2 Stunden 10 Minuten 33 Sekunden 22 Millisekunden",
				normalDauer.dauerAlsText());
		assertEquals("3 Tage 2 Stunden 33 Sekunden", normalDauer2
				.dauerAlsText());
		assertEquals("unbekannt", maxZeit.dauerAlsText());
	}

	@Test
	public void testDauerAlsTextString() {
		assertEquals("ungültig", negative.dauerAlsText("anders"));
		assertEquals("0 Millisekunden", nullZeit.dauerAlsText("anders"));
		assertEquals(
				"3 Tage 2 Stunden 10 Minuten 33 Sekunden 22 Millisekunden",
				normalDauer.dauerAlsText("anders"));
		assertEquals("3 Tage 2 Stunden 33 Sekunden", normalDauer2
				.dauerAlsText("anders"));
		assertEquals("anders", maxZeit.dauerAlsText("anders"));
	}

	@Test
	public void testZeitStempel() {
		assertEquals("29.12.1969 00:00:00", negative.zeitStempel());
		assertEquals("01.01.1970 01:00:00", nullZeit.zeitStempel());
		assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel());
		assertEquals("17.08.292278994 08:12:55", maxZeit.zeitStempel());
	}

	@Test
	public void testZeitStempelString() {
		assertEquals("29.12.1969 00:00:00", negative.zeitStempel(null));
		assertEquals("01.01.1970 01:00:00", nullZeit.zeitStempel(null));
		assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel(null));
		assertEquals("17.08.292278994 08:12:55", maxZeit.zeitStempel(null));

		assertEquals("29.12.1969 00:00:00", negative.zeitStempel("0Wert"));
		assertEquals("0Wert", nullZeit.zeitStempel("0Wert"));
		assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel("0Wert"));
		assertEquals("17.08.292278994 08:12:55", maxZeit.zeitStempel("0Wert"));
	}

	@Test
	public void testZeitStempelStringString() {
		assertEquals("29.12.1969 00:00:00", negative.zeitStempel(null, null));
		assertEquals("01.01.1970 01:00:00", nullZeit.zeitStempel(null, null));
		assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel(null, null));
		assertEquals("17.08.292278994 08:12:55", maxZeit
				.zeitStempel(null, null));

		assertEquals("29.12.1969 00:00:00", negative.zeitStempel("0Wert",
				"Ende"));
		assertEquals("0Wert", nullZeit.zeitStempel("0Wert", "Ende"));
		assertEquals("24.12.2005 00:00:00", normalZeit.zeitStempel("0Wert",
				"Ende"));
		assertEquals("Ende", maxZeit.zeitStempel("0Wert", "Ende"));
	}
}
