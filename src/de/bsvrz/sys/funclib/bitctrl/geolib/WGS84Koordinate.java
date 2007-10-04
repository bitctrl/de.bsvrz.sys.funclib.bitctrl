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
 * Klasse zur Repr&auml;sentation einer Koordinate in WGS84
 * 
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version
 *
 */
public class WGS84Koordinate {

	/**
	 * kleinster zul&auml;ssiger Wert für die L&auml;nge
	 */
	public final static double MIN_LAENGE = -180;
	
	/**
	 * gr&ouml;ßter zul&auml;ssiger Wert für die Lä&auml;nge
	 */
	public final static double MAX_LAENGE = 180;

	/**
	 * kleinster zul&auml;ssiger Wert für die Breite
	 */
	public final static double MIN_BREITE = -90;
	
	/**
	 * gr&ouml;ßter zul&auml;ssiger Wert für die Breite
	 */
	public final static double MAX_BREITE = 90;
	

	private double _laenge;

	private double _breite;


	/**
	 * Konstruktor f&uuml;r eine WGS84-Koordinate
	 * 
	 * @param laenge geographische L&auml;nge in Dezimalgrad
	 * @param breite geographische Breite in Dezimalgrad
	 */
	public WGS84Koordinate( double laenge, double breite ) {
		if( testBreite(breite) )
			throw new IllegalArgumentException ("Der Wert für die Breite ist ungültig!"); //$NON-NLS-1$
		
		if( testLaenge(laenge) )
			throw new IllegalArgumentException ("Der Wert für die Länge ist ungültig!"); //$NON-NLS-1$

		this._laenge = laenge;
		this._breite = breite;
	}

	/**
	 * @return geographische Breite in Dezimalgrad
	 */
	public double get_breite() {
		return _breite;
	}

	/**
	 * @param _breite neue geographische Breite in Dezimalgrad
	 */
	public void set_breite(double _breite) {
		if( testBreite(_breite) )
			throw new IllegalArgumentException ("Der Wert für die Breite ist ungültig!"); //$NON-NLS-1$
		
		this._breite = _breite;
	}

	/**
	 * @return geographische L&auml;nge in Dezimalgrad
	 */
	public double get_laenge() {
		return _laenge;
	}

	/**
	 * @param _laenge neue geographische L&auml;nge in Dezimalgrad
	 */
	public void set_laenge(double _laenge) {
		if( testLaenge(_laenge) )
			throw new IllegalArgumentException ("Der Wert für die Länge ist ungültig!"); //$NON-NLS-1$
		
		this._laenge = _laenge;
	}

	private boolean testLaenge( double laenge) {
		return ( ( laenge < MIN_LAENGE ) || ( laenge > MAX_LAENGE ) ); 
	}
	
	private boolean testBreite( double breite) {
		return ( ( breite < MIN_BREITE ) || ( breite > MAX_BREITE) ); 
	}
}
