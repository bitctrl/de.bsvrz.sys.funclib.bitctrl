/**
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
 * Wei&szlig;enfelser Stra&szlig;e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.geolib;

/**
 * Klasse zur Repr&auml;sentation einer Koordinate im UTM (Universal 
 * Transverse Mercator) Koordinatensystem
 * 
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version
 *
 */
public class UTMKoordinate {

	/**
	 * Konstante für die kleinste erlaubte Zone	 
	 * */
	private static final int UTM_ZONE_MIN = 1;

	/**
	 * Konstante für die gr&ouml;&szlig;te erlaubte Zone	 
	 * */
	private static final int UTM_ZONE_MAX = 60;


	
	/**
	 * der Rechtswert (X-Koordinate)
	 */
	private double _x;
	
	/**
	 * der Hochwert (Y-Koordinate)
	 */
	private double _y;
	
	/**
	 * die Zone (1-60)
	 */
	private int _zone;

	/**
	 * die Hemisphäre
	 */
	private UTM_HEMI _hemisphaere = UTM_HEMI.HORDHALBKUGEL;

	
	/**
	 * Konstruktor f&uuml;r eine UTM-Koordinate auf der n&ouml;rdlichen
	 * Erdhalbkugel
	 * 
	 * @param x X-Koordinate
	 * @param y Y-Koordinate
	 * @param zone Zone
	 */
	public UTMKoordinate( double x, double y, int zone ) {
		if( testZone(zone) )
			throw new IllegalArgumentException ("Der Wert für die Zone ist ungültig!"); //$NON-NLS-1$
		
		_x = x;
		_y= y;
		_zone = zone;
		_hemisphaere = UTM_HEMI.HORDHALBKUGEL;
	}

	
	/**
	 * Konstruktor f&uuml;r eine UTM-Koordinate 
	 *  
	 * @param x X-Koordinate
	 * @param y Y-Koordinate
	 * @param zone Zone
	 * @param hemisphaere die Erdhalbkugel
	 */
	public UTMKoordinate( double x, double y, int zone, UTM_HEMI hemisphaere ) {
		if( testZone(zone) )
			throw new IllegalArgumentException ("Der Wert für die Zone ist ungültig!"); //$NON-NLS-1$
		
		_x = x;
		_y= y;
		_zone = zone;
		_hemisphaere = hemisphaere;
	}
	
	
	
	private boolean testZone( int zone) {
		return ( ( zone < UTM_ZONE_MIN ) || ( zone > UTM_ZONE_MAX) ); 
	}
	
	/**
	 * Konstantendefinition f&uuml;r die Hemisph&auml;re
	 */
	public static class UTM_HEMI {
		
		@SuppressWarnings("unused")
		private final int _hemi;
		
		/**
		 * Konstante für die n&ouml;rdliche Hemisph&auml;re
		 */
		public static final UTM_HEMI HORDHALBKUGEL = new UTM_HEMI(0);
		
		/**
		 * Konstante für die s&uuml;dliche Hemisph&auml;re
		 */
		public static final UTM_HEMI SUEDHALBKUGEL = new UTM_HEMI(1);


		/**
		 * Nicht &ouml;ffentlicher Konstruktor der zum Erzeugen der 
		 * vordefinierten Werte benutzt wird.
		 * @param name  Name des Zustandes.
		 */
		private UTM_HEMI(int wert) {
			_hemi = wert;
		}
	}
	
	
	/**
	 * Gibt die X-Koordinate (Rechtswert) zur&uuml;ck.
	 * 
	 * @return X-Koordinate
	 */
	public double get_x() {
		return _x;
	}

	
	/**
	 * Gibt die Y-Koordinate (Rechtswert) zur&uuml;ck.
	 * 
	 * @return Y-Koordinate
	 */
	public double get_y() {
		return _y;
	}

	/**
	 * Gibt die Zone zur&uuml;ck.
	 * 
	 * @return Zone
	 */
	public int get_zone() {
		return _zone;
	}


	/**
	 * @return die Hemisph&auml;re
	 */
	public UTM_HEMI get_hemisphaere() {
		return _hemisphaere;
	}


	/**
	 * @param _hemisphaere
	 */
	public void set_hemisphaere(UTM_HEMI _hemisphaere) {
		this._hemisphaere = _hemisphaere;
	}

}
