/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.x 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */
package de.bsvrz.sys.funclib.bitctrl.dua;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import de.bsvrz.sys.funclib.bitctrl.app.Pause;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IObjektWeckerListener;
import de.bsvrz.sys.funclib.bitctrl.dua.test.DAVTest;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Testet die Klasse <code>ObjektWecker</code>
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class TestObjektWecker {

	/**
	 * der getestete Wecker
	 */
	protected ObjektWecker wecker = new ObjektWecker();
	
	/**
	 * Menge der geweckten Objekte mit der Anzahl, wie oft sie geweckt wurden
	 */
	protected HashMap<TestWeckObjekt, Integer> geweckteObjekte = new HashMap<TestWeckObjekt, Integer>();
	
	/**
	 * Menge der zu weckenden Objekte mit der Anzahl, wie oft sie geweckt werden sollen
	 */
	protected HashMap<TestWeckObjekt, Integer> zuWeckendeObjekte = new HashMap<TestWeckObjekt, Integer>();
	
	
	@Test
	public void test1(){
		
		long jetzt = System.currentTimeMillis();
		long jetztPlus2Sek = jetzt + 2 * Konstante.SEKUNDE_IN_MS;
		long jetztPlus3Sek = jetzt + 3 * Konstante.SEKUNDE_IN_MS;
		long jetztPlus5Sek = jetzt + 5 * Konstante.SEKUNDE_IN_MS;
		long jetztPlus7Sek = jetzt + 7 * Konstante.SEKUNDE_IN_MS;
		
		TestWeckObjekt t1 = new TestWeckObjekt("1" , jetztPlus2Sek); //$NON-NLS-1$
		TestWeckObjekt t2 = new TestWeckObjekt("2" , jetztPlus2Sek); //$NON-NLS-1$
		TestWeckObjekt t3 = new TestWeckObjekt("3" , jetztPlus2Sek); //$NON-NLS-1$

		Pause.warte(500L);
		
		TestWeckObjekt t4 = new TestWeckObjekt("4" , jetztPlus3Sek); //$NON-NLS-1$
		TestWeckObjekt t5 = new TestWeckObjekt("5" , jetztPlus3Sek); //$NON-NLS-1$
		TestWeckObjekt t6 = new TestWeckObjekt("6" , jetztPlus3Sek); //$NON-NLS-1$
		
		Pause.warte(2500L);
		
		TestWeckObjekt t7 = new TestWeckObjekt("7" , jetztPlus7Sek); //$NON-NLS-1$
		TestWeckObjekt t8 = new TestWeckObjekt("8" , jetztPlus5Sek); //$NON-NLS-1$
		t8.setNeuenWeckZeitPunkt(jetztPlus7Sek);
		t8.setNeuenWeckZeitPunkt(jetztPlus5Sek);
		t8.setNeuenWeckZeitPunkt(jetztPlus7Sek);

		t7.setNeuenWeckZeitPunkt(jetztPlus5Sek);
		t7.setWeckerAus();
		
		t1.setNeuenWeckZeitPunkt(jetztPlus5Sek);
		t2.setNeuenWeckZeitPunkt(jetztPlus7Sek);
		
		DAVTest.warteBis(jetztPlus7Sek + Konstante.SEKUNDE_IN_MS);
		/**
		 * jetzt alles wieder leer
		 */
		
		/**
		 * jetzt müssten alle Objekten so oft geweckt worden sein,
		 * wie eingeplant
		 */
		Assert.assertEquals(this.zuWeckendeObjekte.keySet().size(), this.geweckteObjekte.keySet().size());
		for(TestWeckObjekt objekt:zuWeckendeObjekte.keySet()){
			Assert.assertEquals("Anzahl der Weckzeiten ungleich: ", zuWeckendeObjekte.get(objekt), geweckteObjekte.get(objekt)); //$NON-NLS-1$
		}
		
		for(int i = 0; i<10; i++){
			jetzt = System.currentTimeMillis();
			jetztPlus2Sek = jetzt + 2 * Konstante.SEKUNDE_IN_MS;
			jetztPlus3Sek = jetzt + 3 * Konstante.SEKUNDE_IN_MS;
			jetztPlus5Sek = jetzt + 5 * Konstante.SEKUNDE_IN_MS;
			jetztPlus7Sek = jetzt + 7 * Konstante.SEKUNDE_IN_MS;
			
			t1.setNeuenWeckZeitPunkt(jetztPlus7Sek);
			t2.setNeuenWeckZeitPunkt(jetztPlus2Sek);
			t1.setNeuenWeckZeitPunkt(jetztPlus5Sek);
			t1.setNeuenWeckZeitPunkt(jetztPlus3Sek);
			t1.setWeckerAus();
			Pause.warte(2900);
			t3.setNeuenWeckZeitPunkt(jetztPlus3Sek);
			
			t4.setNeuenWeckZeitPunkt(jetztPlus5Sek);
			t5.setNeuenWeckZeitPunkt(jetztPlus5Sek);
			t6.setNeuenWeckZeitPunkt(jetztPlus5Sek);
			
			t4.setNeuenWeckZeitPunkt(jetztPlus7Sek);
			t4.setWeckerAus();
			
			t5.setNeuenWeckZeitPunkt(jetztPlus7Sek);
			
			DAVTest.warteBis(jetztPlus7Sek + Konstante.SEKUNDE_IN_MS);
			/**
			 * jetzt müssten alle Objekten so oft geweckt worden sein,
			 * wie eingeplant
			 */
			Assert.assertEquals(this.zuWeckendeObjekte.keySet().size(), this.geweckteObjekte.keySet().size());
			for(TestWeckObjekt objekt:zuWeckendeObjekte.keySet()){
				Assert.assertEquals("Anzahl der Weckzeiten ungleich: ", zuWeckendeObjekte.get(objekt), geweckteObjekte.get(objekt)); //$NON-NLS-1$
			}		
		}
	}
	
	
	/**
	 * Instanzen dieser Klasse werden geweckt
	 * 
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	private class TestWeckObjekt
	implements IObjektWeckerListener{
		
		/**
		 * Zeit-Toleranz beim Wecken in ms
		 */
		private static final long TOLERANZ = 200;

		/**
		 * der eingeplante Weckzeitpunkt
		 */
		private long weckZeitPunkt = -1;
		
		/**
		 * der Name des Objektes
		 */
		private String name = null;
		
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param name der Name des Objektes
		 * @param weckZeitPunkt der eingeplante Weckzeitpunkt
		 */
		public TestWeckObjekt(String name, long weckZeitPunkt){
			this.weckZeitPunkt = weckZeitPunkt;
			this.name = name;
			
			Assert.assertFalse(wecker.isWeckerGestelltFuer(this));
			
			wecker.setWecker(this, weckZeitPunkt);
			
			System.out.println("Eingeplant: " + this); //$NON-NLS-1$
			if( zuWeckendeObjekte.get(this) == null ){
				zuWeckendeObjekte.put(this, 1); 
			}else{
				zuWeckendeObjekte.put(this, zuWeckendeObjekte.get(this) + 1);
			}
		}
		

		/**
		 * Setzt, dass das Objekt nicht mehr geweckt werden soll
		 */
		public void setWeckerAus(){
			this.weckZeitPunkt = -1;
			Assert.assertTrue(wecker.isWeckerGestelltFuer(this));
			
			wecker.setWecker(this, ObjektWecker.AUS);
			Assert.assertFalse(wecker.isWeckerGestelltFuer(this));
			
			System.out.println("Ausgeplant: " + this); //$NON-NLS-1$
			zuWeckendeObjekte.put(this, zuWeckendeObjekte.get(this) - 1);
			if(zuWeckendeObjekte.get(this) == 0){
				zuWeckendeObjekte.remove(this);
			}
		}
		
		
		/**
		 * Setzt einen neuen Weckzeitpunkt
		 * 
		 * @param neuerWeckZeitpunkt ein neuer Weckzeitpunkt
		 */
		public void setNeuenWeckZeitPunkt(final long neuerWeckZeitpunkt){
			
			/**
			 * elter Weckzeitpunkt abgelaufen?
			 */
			if(this.weckZeitPunkt < System.currentTimeMillis()){
				if( zuWeckendeObjekte.get(this) == null ){
					zuWeckendeObjekte.put(this, 1); 
				}else{
					zuWeckendeObjekte.put(this, zuWeckendeObjekte.get(this) + 1);
				}
			}

			this.weckZeitPunkt = neuerWeckZeitpunkt;
			
			wecker.setWecker(this, weckZeitPunkt);
			
			System.out.println("Umgeplant: " + this); //$NON-NLS-1$
		}
		
		
		/**
		 * Erfragt den eingeplanten Weckzeitpunkt
		 * 
		 * @return der eingeplante Weckzeitpunkt
		 */
		public final long getWeckZeitpunk(){
			return this.weckZeitPunkt;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		public void alarm() {
			Assert.assertFalse(wecker.isWeckerGestelltFuer(this));
			final long jetzt = System.currentTimeMillis();
			
			System.out.println("Objekt " + this + " geweckt: " + jetzt + ", Differenz: " + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					 (jetzt - this.weckZeitPunkt >= 0?"+":"-") + (jetzt - this.weckZeitPunkt)); //$NON-NLS-1$ //$NON-NLS-2$
			Assert.assertTrue("Kein Weckzeitpunkt eingeplant", this.weckZeitPunkt != -1); //$NON-NLS-1$
			Assert.assertTrue("Alarm erwartet um: " + this.weckZeitPunkt +  //$NON-NLS-1$
					", war aber " + jetzt + " (Differenz: " + Math.abs(jetzt - this.weckZeitPunkt) + ")" ,   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
					Math.abs(jetzt - this.weckZeitPunkt) < TOLERANZ);
			
			if( geweckteObjekte.get(this) == null ){
				geweckteObjekte.put(this, 1); 
			}else{
				geweckteObjekte.put(this, geweckteObjekte.get(this) + 1);
			}
			
			if( geweckteObjekte.get(this) != null && geweckteObjekte.get(this) == 0) {
				geweckteObjekte.remove(this);
			}
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return name + " (" +  //$NON-NLS-1$
				new Long(this.weckZeitPunkt).toString() + "ms)"; //$NON-NLS-1$
		}
		
	}
}
