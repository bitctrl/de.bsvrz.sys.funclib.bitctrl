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
 * Universal Transverse Mercator Projektion
 * 
 * Mit Genehmigung von Chuck Taylor (http://home.hiwaay.net/~taylorc/toolbox/geography/geoutm.html)
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version
 *
 */
public class GeoTransformation {
	
	final private static double  UTMScaleFactor = 0.9996;
	
    /** Ellipsoid model constants (actual values here are for WGS84) */
	final private static double   sm_a = 6378137.0;
	final private static double   sm_b = 6356752.314;
	
	/** 
	 * mittlerer Erdradius
	 */
	final private static double   r_m = 6371000.8;
	
	@SuppressWarnings("unused")
	final private static double   sm_EccSquared = 6.69437999013e-03;
	
	private static double xy[] = {0,0};
	private static int zone = 0;
	private static double latlon[] = {0,0};

	/**
	 * Berechnet die L&auml;nge des Kreisbogens auf der Erdoberfl&auml;che zu 
	 * einem Winkel.
	 *  
	 * @param winkel Winkel in Grad
	 * @return Kreisbogenl&auml;nge in m
	 */
	public static double WinkelInMeter(double winkel) {
		return(winkel * r_m * 2 * Math.PI / 360);
	}
	
	
	/**
	 * Transformiert WGS84-Koordinaten nach UTM
	 * 
	 * @param wgs84laenge geographische L&auml;nge in Dezimalgrad
	 * @param wgs84breite geographische Breite in Dezimalgrad
	 * @return die transformierten Koordinaten
	 */
	public static UTMKoordinate WGS84_nach_UTM(double wgs84laenge, double wgs84breite) {
		double lon = Math.toRadians(wgs84laenge);
		double lat = Math.toRadians(wgs84breite);
		
		  // Compute the UTM zone.
       zone = (int) Math.floor ((wgs84laenge + 180.0) / 6) + 1;

        //zone = LatLonToUTMXY (DegToRad (lat), DegToRad (lon), zone, xy);

		LatLonToUTMXY(lat, lon);
		
		return new UTMKoordinate( xy[0], xy[1], zone );	
	}

    /**
     * Transformiert UTM-Koordinaten nach WGS84
     * 
	 * @param _utm 
	 * @return die transformierten Koordinaten
	 */
	public static WGS84Koordinate UTM_nach_WGS84Punkt(UTMKoordinate _utm) {
        boolean southhemi;
	     if (_utm.get_hemisphaere() == UTMKoordinate.UTM_HEMI.SUEDHALBKUGEL)
	            southhemi = true;
	        else
	            southhemi = false;
	     
	     zone = _utm.get_zone();
	     	     
		UTMXYToLatLon( _utm.get_x(), _utm.get_y(), _utm.get_zone(), southhemi );
	        
		return new WGS84Koordinate( Math.toDegrees(latlon[1]),  Math.toDegrees(latlon[0]) );
	}
	

		/**
	    * LatLonToUTMXY
	    *
	    * Converts a latitude/longitude pair to x and y coordinates in the
	    * Universal Transverse Mercator projection.
	    *
	    * Inputs:
	    *   lat - Latitude of the point, in radians.
	    *   lon - Longitude of the point, in radians.
	    *   zone - UTM zone to be used for calculating values for x and y.
	    *          If zone is less than 1 or greater than 60, the routine
	    *          will determine the appropriate zone from the value of lon.
	    *
	    * Outputs:
	    *   xy - A 2-element array where the UTM x and y values will be stored.
	    *
	    * Returns:
	    *   The UTM zone used for calculating the values of x and y.
	    *
	    */
	    private static void LatLonToUTMXY (double lat, double lon)
	    {
	        MapLatLonToXY (lat, lon, UTMCentralMeridian (zone));

	        /* Adjust easting and northing for UTM system. */
	        xy[0] = xy[0] * UTMScaleFactor + 500000.0;
	        xy[1] = xy[1] * UTMScaleFactor;
	        if (xy[1] < 0.0)
	            xy[1] = xy[1] + 10000000.0;

	   }
	    
	    /**
	     * UTMCentralMeridian
	     *
	     * Determines the central meridian for the given UTM zone.
	     *
	     * Inputs:
	     *     zone - An integer value designating the UTM zone, range [1,60].
	     *
	     * Returns:
	     *   The central meridian for the given UTM zone, in radians, or zero
	     *   if the UTM zone parameter is outside the range [1,60].
	     *   Range of the central meridian is the radian equivalent of [-177,+177].
	     *
	     */
	     private static double UTMCentralMeridian (int _zone)
	     {
	         double cmeridian;

	         cmeridian = Math.toRadians(-183.0 + (_zone * 6.0));
	         
	         return cmeridian;
	     }

	
	  /**
	    * MapLatLonToXY
	    *
	    * Converts a latitude/longitude pair to x and y coordinates in the
	    * Transverse Mercator projection.  Note that Transverse Mercator is not
	    * the same as UTM; a scale factor is required to convert between them.
	    *
	    * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
	    * GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
	    *
	    * Inputs:
	    *    phi - Latitude of the point, in radians.
	    *    lambda - Longitude of the point, in radians.
	    *    lambda0 - Longitude of the central meridian to be used, in radians.
	    *
	    * Outputs:
	    *    xy - A 2-element array containing the x and y coordinates
	    *         of the computed point.
	    *
	    * Returns:
	    *    The function does not return a value.
	    *
	    */
	    private static void MapLatLonToXY (double phi, double lambda, double lambda0)
	    {
	        double N, nu2, ep2, t, t2, l;
	        double l3coef, l4coef, l5coef, l6coef, l7coef, l8coef;
	        @SuppressWarnings("unused")
			double tmp;

	        /* Precalculate ep2 */
	        ep2 = (Math.pow (sm_a, 2.0) - Math.pow (sm_b, 2.0)) / Math.pow (sm_b, 2.0);
	    
	        /* Precalculate nu2 */
	        nu2 = ep2 * Math.pow (Math.cos (phi), 2.0);
	    
	        /* Precalculate N */
	        N = Math.pow (sm_a, 2.0) / (sm_b * Math.sqrt (1 + nu2));
	    
	        /* Precalculate t */
	        t = Math.tan (phi);
	        t2 = t * t;
	        tmp = (t2 * t2 * t2) - Math.pow (t, 6.0);

	        /* Precalculate l */
	        l = lambda - lambda0;
	    
	        /* Precalculate coefficients for l**n in the equations below
	           so a normal human being can read the expressions for easting
	           and northing
	           -- l**1 and l**2 have coefficients of 1.0 */
	        l3coef = 1.0 - t2 + nu2;
	    
	        l4coef = 5.0 - t2 + 9 * nu2 + 4.0 * (nu2 * nu2);
	    
	        l5coef = 5.0 - 18.0 * t2 + (t2 * t2) + 14.0 * nu2
	            - 58.0 * t2 * nu2;
	    
	        l6coef = 61.0 - 58.0 * t2 + (t2 * t2) + 270.0 * nu2
	            - 330.0 * t2 * nu2;
	    
	        l7coef = 61.0 - 479.0 * t2 + 179.0 * (t2 * t2) - (t2 * t2 * t2);
	    
	        l8coef = 1385.0 - 3111.0 * t2 + 543.0 * (t2 * t2) - (t2 * t2 * t2);
	    
	        /* Calculate easting (x) */
	        xy[0] = N * Math.cos (phi) * l
	            + (N / 6.0 * Math.pow (Math.cos (phi), 3.0) * l3coef * Math.pow (l, 3.0))
	            + (N / 120.0 * Math.pow (Math.cos (phi), 5.0) * l5coef * Math.pow (l, 5.0))
	            + (N / 5040.0 * Math.pow (Math.cos (phi), 7.0) * l7coef * Math.pow (l, 7.0));
	    
	        /* Calculate northing (y) */
	        xy[1] = ArcLengthOfMeridian (phi)
	            + (t / 2.0 * N * Math.pow (Math.cos (phi), 2.0) * Math.pow (l, 2.0))
	            + (t / 24.0 * N * Math.pow (Math.cos (phi), 4.0) * l4coef * Math.pow (l, 4.0))
	            + (t / 720.0 * N * Math.pow (Math.cos (phi), 6.0) * l6coef * Math.pow (l, 6.0))
	            + (t / 40320.0 * N * Math.pow (Math.cos (phi), 8.0) * l8coef * Math.pow (l, 8.0));
	    
	        return;
	    }
	    
	    /*
	     * ArcLengthOfMeridian
	     *
	     * Computes the ellipsoidal distance from the equator to a point at a
	     * given latitude.
	     *
	     * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
	     * GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
	     *
	     * Inputs:
	     *     phi - Latitude of the point, in radians.
	     *
	     * Globals:
	     *     sm_a - Ellipsoid model major axis.
	     *     sm_b - Ellipsoid model minor axis.
	     *
	     * Returns:
	     *     The ellipsoidal distance of the point from the equator, in meters.
	     *
	     */
	     private static double ArcLengthOfMeridian (double phi)
	     {
	         double alpha, beta, gamma, delta, epsilon, n;
	         double result;

	         /* Precalculate n */
	         n = (sm_a - sm_b) / (sm_a + sm_b);

	         /* Precalculate alpha */
	         alpha = ((sm_a + sm_b) / 2.0)
	            * (1.0 + (Math.pow (n, 2.0) / 4.0) + (Math.pow (n, 4.0) / 64.0));

	         /* Precalculate beta */
	         beta = (-3.0 * n / 2.0) + (9.0 * Math.pow (n, 3.0) / 16.0)
	            + (-3.0 * Math.pow (n, 5.0) / 32.0);

	         /* Precalculate gamma */
	         gamma = (15.0 * Math.pow (n, 2.0) / 16.0)
	             + (-15.0 * Math.pow (n, 4.0) / 32.0);
	     
	         /* Precalculate delta */
	         delta = (-35.0 * Math.pow (n, 3.0) / 48.0)
	             + (105.0 * Math.pow (n, 5.0) / 256.0);
	     
	         /* Precalculate epsilon */
	         epsilon = (315.0 * Math.pow (n, 4.0) / 512.0);
	     
	     /* Now calculate the sum of the series and return */
	     result = alpha
	         * (phi + (beta * Math.sin (2.0 * phi))
	             + (gamma * Math.sin (4.0 * phi))
	             + (delta * Math.sin (6.0 * phi))
	             + (epsilon * Math.sin (8.0 * phi)));

	     return result;
	     }
	     /*
	      * FootpointLatitude
	      *
	      * Computes the footpoint latitude for use in converting transverse
	      * Mercator coordinates to ellipsoidal coordinates.
	      *
	      * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
	      *   GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
	      *
	      * Inputs:
	      *   y - The UTM northing coordinate, in meters.
	      *
	      * Returns:
	      *   The footpoint latitude, in radians.
	      *
	      */
	      private static double FootpointLatitude (double y)
	      {
	          double y_, alpha_, beta_, gamma_, delta_, epsilon_, n;
	          double result;
	          
	          /* Precalculate n (Eq. 10.18) */
	          n = (sm_a - sm_b) / (sm_a + sm_b);
	          	
	          /* Precalculate alpha_ (Eq. 10.22) */
	          /* (Same as alpha in Eq. 10.17) */
	          alpha_ = ((sm_a + sm_b) / 2.0)
	              * (1 + (Math.pow (n, 2.0) / 4) + (Math.pow (n, 4.0) / 64));
	          
	          /* Precalculate y_ (Eq. 10.23) */
	          y_ = y / alpha_;
	          
	          /* Precalculate beta_ (Eq. 10.22) */
	          beta_ = (3.0 * n / 2.0) + (-27.0 * Math.pow (n, 3.0) / 32.0)
	              + (269.0 * Math.pow (n, 5.0) / 512.0);
	          
	          /* Precalculate gamma_ (Eq. 10.22) */
	          gamma_ = (21.0 * Math.pow (n, 2.0) / 16.0)
	              + (-55.0 * Math.pow (n, 4.0) / 32.0);
	          	
	          /* Precalculate delta_ (Eq. 10.22) */
	          delta_ = (151.0 * Math.pow (n, 3.0) / 96.0)
	              + (-417.0 * Math.pow (n, 5.0) / 128.0);
	          	
	          /* Precalculate epsilon_ (Eq. 10.22) */
	          epsilon_ = (1097.0 * Math.pow (n, 4.0) / 512.0);
	          	
	          /* Now calculate the sum of the series (Eq. 10.21) */
	          result = y_ + (beta_ * Math.sin (2.0 * y_))
	              + (gamma_ * Math.sin (4.0 * y_))
	              + (delta_ * Math.sin (6.0 * y_))
	              + (epsilon_ * Math.sin (8.0 * y_));
	          
	          return result;
	      }

	      /*
	       * MapXYToLatLon
	       *
	       * Converts x and y coordinates in the Transverse Mercator projection to
	       * a latitude/longitude pair.  Note that Transverse Mercator is not
	       * the same as UTM; a scale factor is required to convert between them.
	       *
	       * Reference: Hoffmann-Wellenhof, B., Lichtenegger, H., and Collins, J.,
	       *   GPS: Theory and Practice, 3rd ed.  New York: Springer-Verlag Wien, 1994.
	       *
	       * Inputs:
	       *   x - The easting of the point, in meters.
	       *   y - The northing of the point, in meters.
	       *   lambda0 - Longitude of the central meridian to be used, in radians.
	       *
	       * Outputs:
	       *   philambda - A 2-element containing the latitude and longitude
	       *               in radians.
	       *
	       * Returns:
	       *   The function does not return a value.
	       *
	       * Remarks:
	       *   The local variables Nf, nuf2, tf, and tf2 serve the same purpose as
	       *   N, nu2, t, and t2 in MapLatLonToXY, but they are computed with respect
	       *   to the footpoint latitude phif.
	       *
	       *   x1frac, x2frac, x2poly, x3poly, etc. are to enhance readability and
	       *   to optimize computations.
	       *
	       */
	       private static void MapXYToLatLon (double x, double y, double lambda0)
	       {
	           double phif, Nf, Nfpow, nuf2, ep2, tf, tf2, tf4, cf;
	           double x1frac, x2frac, x3frac, x4frac, x5frac, x6frac, x7frac, x8frac;
	           double x2poly, x3poly, x4poly, x5poly, x6poly, x7poly, x8poly;
	           
	           /* Get the value of phif, the footpoint latitude. */
	           phif = FootpointLatitude (y);
	           	
	           /* Precalculate ep2 */
	           ep2 = (Math.pow (sm_a, 2.0) - Math.pow (sm_b, 2.0))
	                 / Math.pow (sm_b, 2.0);
	           	
	           /* Precalculate cos (phif) */
	           cf = Math.cos (phif);
	           	
	           /* Precalculate nuf2 */
	           nuf2 = ep2 * Math.pow (cf, 2.0);
	           	
	           /* Precalculate Nf and initialize Nfpow */
	           Nf = Math.pow (sm_a, 2.0) / (sm_b * Math.sqrt (1 + nuf2));
	           Nfpow = Nf;
	           	
	           /* Precalculate tf */
	           tf = Math.tan (phif);
	           tf2 = tf * tf;
	           tf4 = tf2 * tf2;
	           
	           /* Precalculate fractional coefficients for x**n in the equations
	              below to simplify the expressions for latitude and longitude. */
	           x1frac = 1.0 / (Nfpow * cf);
	           
	           Nfpow *= Nf;   /* now equals Nf**2) */
	           x2frac = tf / (2.0 * Nfpow);
	           
	           Nfpow *= Nf;   /* now equals Nf**3) */
	           x3frac = 1.0 / (6.0 * Nfpow * cf);
	           
	           Nfpow *= Nf;   /* now equals Nf**4) */
	           x4frac = tf / (24.0 * Nfpow);
	           
	           Nfpow *= Nf;   /* now equals Nf**5) */
	           x5frac = 1.0 / (120.0 * Nfpow * cf);
	           
	           Nfpow *= Nf;   /* now equals Nf**6) */
	           x6frac = tf / (720.0 * Nfpow);
	           
	           Nfpow *= Nf;   /* now equals Nf**7) */
	           x7frac = 1.0 / (5040.0 * Nfpow * cf);
	           
	           Nfpow *= Nf;   /* now equals Nf**8) */
	           x8frac = tf / (40320.0 * Nfpow);
	           
	           /* Precalculate polynomial coefficients for x**n.
	              -- x**1 does not have a polynomial coefficient. */
	           x2poly = -1.0 - nuf2;
	           
	           x3poly = -1.0 - 2 * tf2 - nuf2;
	           
	           x4poly = 5.0 + 3.0 * tf2 + 6.0 * nuf2 - 6.0 * tf2 * nuf2
	           	- 3.0 * (nuf2 *nuf2) - 9.0 * tf2 * (nuf2 * nuf2);
	           
	           x5poly = 5.0 + 28.0 * tf2 + 24.0 * tf4 + 6.0 * nuf2 + 8.0 * tf2 * nuf2;
	           
	           x6poly = -61.0 - 90.0 * tf2 - 45.0 * tf4 - 107.0 * nuf2
	           	+ 162.0 * tf2 * nuf2;
	           
	           x7poly = -61.0 - 662.0 * tf2 - 1320.0 * tf4 - 720.0 * (tf4 * tf2);
	           
	           x8poly = 1385.0 + 3633.0 * tf2 + 4095.0 * tf4 + 1575 * (tf4 * tf2);
	           	
	           /* Calculate latitude */
	           latlon[0] = phif + x2frac * x2poly * (x * x)
	           	+ x4frac * x4poly * Math.pow (x, 4.0)
	           	+ x6frac * x6poly * Math.pow (x, 6.0)
	           	+ x8frac * x8poly * Math.pow (x, 8.0);
	           	
	           /* Calculate longitude */
	           latlon[1] = lambda0 + x1frac * x
	           	+ x3frac * x3poly * Math.pow (x, 3.0)
	           	+ x5frac * x5poly * Math.pow (x, 5.0)
	           	+ x7frac * x7poly * Math.pow (x, 7.0);
	           	
	           return;
	       }

	       
		 /**
			    * UTMXYToLatLon
			    *
			    * Converts x and y coordinates in the Universal Transverse Mercator
			    * projection to a latitude/longitude pair.
			    *
			    * Inputs:
			    *	x - The easting of the point, in meters.
			    *	y - The northing of the point, in meters.
			    *	zone - The UTM zone in which the point lies.
			    *	southhemi - True if the point is in the southern hemisphere;
			    *               false otherwise.
			    *
			    * Outputs:
			    *	latlon - A 2-element array containing the latitude and
			    *            longitude of the point, in radians.
			    *
			    * Returns:
			    *	The function does not return a value.
			    *
			    */
		private static void UTMXYToLatLon (double x, double y, int _zone, boolean southhemi) {
			double cmeridian;
			double ly = y;        	
			double lx = x - 500000.0;
			lx /= UTMScaleFactor;
			        	
			/* If in southern hemisphere, adjust y accordingly. */
			if (southhemi)
				ly -= 10000000.0;
			        		
			ly /= UTMScaleFactor;
			        
			cmeridian = UTMCentralMeridian (zone);
			MapXYToLatLon (lx, ly, cmeridian );	
		}





}
