package de.bsvrz.sys.funclib.bitctrl.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TreePropertiesTest {

	@Test
	public void einfacherTest() {
		TreeProperties tree = new TreeProperties();

		tree.beginGroup("window");
		tree.setProperty("width", "640");
		tree.setProperty("height", "480");
		tree.endGroup("window");
		System.out.println(tree);
		assertEquals(2, tree.size());
		assertEquals("640", tree.get("window.width"));
		assertEquals("480", tree.get("window.height"));

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
		System.out.println(tree);
	}
}
