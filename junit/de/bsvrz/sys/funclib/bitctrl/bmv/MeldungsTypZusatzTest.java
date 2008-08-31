/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.bmv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.junit.Test;

/**
 * @author BitCtrl Systems GmbH, Falko
 * @version $Id$
 * @todo Auch Fehlerfälle in Test einbeziehen.
 */
public class MeldungsTypZusatzTest {

	/**
	 * Test method for
	 * {@link de.bsvrz.sys.funclib.bitctrl.bmv.MeldungsTypZusatz#parse(java.lang.String)}.
	 */
	@Test
	public final void testParseWithoutParameter() {
		MeldungsTypZusatz mtz = null;

		try {
			mtz = MeldungsTypZusatz.parse("bitctrl.wvz");
		} catch (final ParseException ex) {
			fail(ex.getLocalizedMessage());
		}

		assertEquals("bitctrl.wvz", mtz.getId());
		assertEquals(0, mtz.getParameter().size());
	}

	/**
	 * Test method for
	 * {@link de.bsvrz.sys.funclib.bitctrl.bmv.MeldungsTypZusatz#parse(java.lang.String)}.
	 */
	@Test
	public final void testParseWithParameter() {
		MeldungsTypZusatz mtz = null;

		try {
			mtz = MeldungsTypZusatz
					.parse("bitctrl.wvz:a=3.7,b=test,c=true,d=245");
		} catch (final ParseException ex) {
			fail(ex.getLocalizedMessage());
		}

		assertEquals("bitctrl.wvz", mtz.getId());
		assertEquals(4, mtz.getParameter().size());
		assertEquals(3.7, mtz.getDouble("a"));
		assertEquals("test", mtz.getString("b"));
		assertEquals(true, mtz.getBoolean("c"));
		assertEquals(245, mtz.getInteger("d"));
	}

	/**
	 * Test method for
	 * {@link de.bsvrz.sys.funclib.bitctrl.bmv.MeldungsTypZusatz#compile()}.
	 */
	@Test
	public final void testCompile() {
		final MeldungsTypZusatz mtzExpected = new MeldungsTypZusatz(
				"bitctrl.wvz");

		mtzExpected.set("a", 123);
		mtzExpected.set("b", false);
		mtzExpected.set("c", 45.8);
		mtzExpected.set("d", "test");
		mtzExpected.set("e", 55L);
		assertEquals(5, mtzExpected.getParameter().size());

		final String compiled = mtzExpected.compile();
		MeldungsTypZusatz mtzActual = null;
		try {
			mtzActual = MeldungsTypZusatz.parse(compiled);
		} catch (final ParseException ex) {
			fail(ex.getLocalizedMessage());
		}
		assertEquals(mtzExpected, mtzActual);
	}
}
