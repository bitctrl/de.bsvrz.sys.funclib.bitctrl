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
 * @version $Id$
 */
public class TreePropertiesTest {

	/**
	 * Ein einfacher Test, der eine Gruppe und ein Feld schreibt.
	 */
	@Test
	public void schreibLeseTest() {
		TreeProperties tree = new TreeProperties();

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
		TreeProperties tree = new TreeProperties();

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
		int size = tree.beginReadArray("people");
		assertEquals(2, size);
		tree.setArrayIndex(0);
		assertEquals("Hans", tree.getProperty("name"));
		tree.setArrayIndex(1);
		assertEquals("Wurst", tree.getProperty("name"));
		tree.endArray("people");
	}

	@Test
	public void testLoadUndStore() {
		TreeProperties tree = new TreeProperties();

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
			tree.store(new FileOutputStream("test.properties"), "JUnit Test");
		} catch (FileNotFoundException ex) {
			fail(ex.getLocalizedMessage());
		} catch (IOException ex) {
			fail(ex.getLocalizedMessage());
		}

		TreeProperties load = new TreeProperties();
		try {
			load.load(new FileInputStream("test.properties"));
		} catch (FileNotFoundException ex) {
			fail(ex.getLocalizedMessage());
		} catch (IOException ex) {
			fail(ex.getLocalizedMessage());
		}

		assertEquals(tree, load);
	}

}
