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
package de.bsvrz.sys.funclib.bitctrl.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

/**
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class TreePropertiesTest {

	/**
	 * Ein einfacher Test, der eine Gruppe und ein Feld schreibt.
	 */
	@Test
	public void schreibLeseTest() {
		final TreeProperties tree = new TreeProperties();

		tree.beginGroup("window");
		tree.setProperty("width", 640);
		tree.setProperty("height", 480);
		tree.endGroup("window");

		assertEquals(2, tree.size());
		assertEquals(640, tree.getInt("window.width"));
		assertEquals(480, tree.getInt("window.height"));

		tree.beginWriteArray("people", 2);
		tree.setArrayIndex(0);
		tree.setProperty("name", "Hans");
		tree.setArrayIndex(1);
		tree.setProperty("name", "Wurst");
		tree.endArray("people");

		assertEquals(5, tree.size());
		assertEquals("2", tree.get("people.size"));
		assertEquals("Hans", tree.get("people.1.name"));
		assertEquals("Wurst", tree.get("people.2.name"));
	}

	/**
	 * Ein einfacher Test, der eine Gruppe und ein Feld schreibt.
	 */
	@Test
	public void schreibTest() {
		final TreeProperties tree = new TreeProperties();

		tree.beginGroup("window");
		tree.setProperty("width", 640);
		tree.setProperty("height", 480);
		tree.endGroup("window");

		assertEquals(2, tree.size());
		assertEquals(640, tree.getInt("window.width"));
		assertEquals(480, tree.getInt("window.height"));

		tree.beginWriteArray("people", 2);
		tree.setArrayIndex(0);
		tree.setProperty("name", "Hans");
		tree.setArrayIndex(1);
		tree.setProperty("name", "Wurst");
		tree.endArray("people");

		assertEquals(5, tree.size());
		assertEquals(2, tree.getInt("people.size"));
		assertEquals("Hans", tree.get("people.1.name"));
		assertEquals("Wurst", tree.get("people.2.name"));

		tree.beginGroup("window");
		assertEquals(640, tree.getInt("width"));
		assertEquals(480, tree.getInt("height"));
		tree.endGroup("window");
		final int size = tree.beginReadArray("people");
		assertEquals(2, size);
		tree.setArrayIndex(0);
		assertEquals("Hans", tree.getProperty("name"));
		tree.setArrayIndex(1);
		assertEquals("Wurst", tree.getProperty("name"));
		tree.endArray("people");
	}

	@Test
	public void testLoadUndStore() {
		final TreeProperties tree = new TreeProperties();

		tree.beginGroup("window");
		tree.setProperty("width", "640");
		tree.setProperty("height", "480");
		tree.endGroup("window");

		assertEquals(2, tree.size());
		assertEquals(640, tree.getInt("window.width"));
		assertEquals(480, tree.getInt("window.height"));

		tree.beginWriteArray("people", 2);
		tree.setArrayIndex(0);
		tree.setProperty("name", "Hans");
		tree.setArrayIndex(1);
		tree.setProperty("name", "Wurst");
		tree.endArray("people");

		try {
			tree.store(new FileOutputStream("target/test.properties"), "JUnit Test");
		} catch (final FileNotFoundException ex) {
			fail(ex.getLocalizedMessage());
		} catch (final IOException ex) {
			fail(ex.getLocalizedMessage());
		}

		final TreeProperties load = new TreeProperties();
		try {
			load.load(new FileInputStream("target/test.properties"));
		} catch (final FileNotFoundException ex) {
			fail(ex.getLocalizedMessage());
		} catch (final IOException ex) {
			fail(ex.getLocalizedMessage());
		}

		assertEquals(tree, load);
	}

}
