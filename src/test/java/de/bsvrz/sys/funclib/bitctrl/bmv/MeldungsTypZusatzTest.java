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
package de.bsvrz.sys.funclib.bitctrl.bmv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.text.ParseException;

import org.junit.Test;

/**
 * @author BitCtrl Systems GmbH, Falko
 *
 *         TODO Auch Fehlerf‰lle in Test einbeziehen.
 */
public class MeldungsTypZusatzTest {

	/**
	 * Test method for
	 * {@link de.bsvrz.sys.funclib.bitctrl.bmv.MeldungsTypZusatz#parse(java.lang.String)}
	 * .
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
	 * {@link de.bsvrz.sys.funclib.bitctrl.bmv.MeldungsTypZusatz#parse(java.lang.String)}
	 * .
	 */
	@Test
	public final void testParseWithParameter() {
		MeldungsTypZusatz mtz = null;

		try {
			mtz = MeldungsTypZusatz
					.parse("bitctrl.wvz::a:=3.7;;b:=test;;c:=true;;d:=245");
		} catch (final ParseException ex) {
			fail(ex.getLocalizedMessage());
		}

		assertEquals("bitctrl.wvz", mtz.getId());
		assertEquals(4, mtz.getParameter().size());
		assertEquals(3.7, mtz.getDouble("a"), 0);
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
