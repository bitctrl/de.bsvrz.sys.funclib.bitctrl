/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007 BitCtrl Systems GmbH 
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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.IntegerValueRange;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Umfelddatenart. Verbindet alle notwendigen Informationen (Name, Abkürzung)
 * eines Umfelddaten-Systemobjekttyps mit dem Systemobjekttyp selbst. Diese
 * Klasse muss statisch instanziiert werden, bevor irgendeine Methode daraus das
 * erste Mal benutzt werden kann.<br>
 * z.B.:<br>
 * Typ: typ.ufdsFahrBahnFeuchte --> Name: FahrBahnFeuchte --> Abk: FBF
 * 
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public final class UmfeldDatenArt {

	/**
	 * Mapt den Systemobjekttyp eines Umfelddatensensors auf die Informationen
	 * zu seinem Namen und seiner Abkürzung.
	 */
	private static Map<SystemObjectType, UmfeldDatenArt> typAufArt = null;

	/**
	 * Verbindung zum Datenverteiler.
	 */
	private static ClientDavInterface dav = null;

	/**
	 * <code>typ.ufdsFahrBahnFeuchte</code>.
	 */
	public static UmfeldDatenArt fbf;

	/**
	 * <code>typ.ufdsFahrBahnGlätte</code>.
	 */
	public static UmfeldDatenArt fbg;

	/**
	 * <code>typ.ufdsFahrBahnOberFlächenTemperatur</code>.
	 */
	public static UmfeldDatenArt fbt;

	/**
	 * <code>typ.ufdsGefrierTemperatur</code>.
	 */
	public static UmfeldDatenArt gt;

	/**
	 * <code>typ.ufdsHelligkeit</code>.
	 */
	public static UmfeldDatenArt hk;

	/**
	 * <code>typ.ufdsLuftTemperatur</code>.
	 */
	public static UmfeldDatenArt lt;

	/**
	 * <code>typ.ufdsNiederschlagsArt</code>.
	 */
	public static UmfeldDatenArt ns;

	/**
	 * <code>typ.ufdsNiederschlagsIntensität</code>.
	 */
	public static UmfeldDatenArt ni;

	/**
	 * <code>typ.ufdsNiederschlagsMenge</code>.
	 */
	public static UmfeldDatenArt nm;

	/**
	 * <code>typ.ufdsRelativeLuftFeuchte</code>.
	 */
	public static UmfeldDatenArt rlf;

	/**
	 * <code>typ.ufdsSchneeHöhe</code>.
	 */
	public static UmfeldDatenArt sh;

	/**
	 * <code>typ.ufdsSichtWeite</code>.
	 */
	public static UmfeldDatenArt sw;

	/**
	 * <code>typ.ufdsTaupunktTemperatur</code>.
	 */
	public static UmfeldDatenArt tpt;

	/**
	 * <code>typ.ufdsTemperaturInTiefe1</code>.
	 */
	public static UmfeldDatenArt tt1;

	/**
	 * <code>typ.ufdsTemperaturInTiefe2</code>.
	 */
	public static UmfeldDatenArt tt2;

	/**
	 * <code>typ.ufdsTemperaturInTiefe3</code>.
	 */
	public static UmfeldDatenArt tt3;

	/**
	 * <code>typ.ufdsWasserFilmDicke</code>.
	 */
	public static UmfeldDatenArt wfd;

	/**
	 * <code>typ.ufdsWindRichtung</code>.
	 */
	public static UmfeldDatenArt wr;

	/**
	 * <code>typ.ufdsFahrBahnOberFlächenZustand</code>.
	 */
	public static UmfeldDatenArt fbz;

	/**
	 * <code>typ.ufdsLuftDruck</code>.
	 */
	public static UmfeldDatenArt ld;

	/**
	 * <code>typ.ufdsRestSalz</code>.
	 */
	public static UmfeldDatenArt rs;

	/**
	 * <code>typ.ufdsWindGeschwindigkeitMittelWert</code>.
	 */
	public static UmfeldDatenArt wgm;

	/**
	 * <code>typ.ufdsWindGeschwindigkeitSpitzenWert</code>.
	 */
	public static UmfeldDatenArt wgs;

	/**
	 * der Systemobjekt-Typ des Umfelddatensensors.
	 */
	private SystemObjectType typ = null;

	/**
	 * der Name der Umfelddatenart.
	 */
	private String name = null;

	/**
	 * die Abkürzung der Umfelddatenart.
	 */
	private String abkuerzung = null;

	/**
	 * Die Skalierung von Werten dieses Typs im Datenkatalog.
	 */
	private double skalierung = 1.0;

	/**
	 * Erfragt die Umfelddatenart eines Systemobjekts.
	 * 
	 * @param objekt
	 *            die Umfelddatenart eines Systemobjekts oder <code>null</code>,
	 *            wenn es sich nicht um das Systemobjekt eines
	 *            Umfelddatensensors handelt
	 * @return die Umfelddatenart eines Systemobjekts.
	 */
	public static UmfeldDatenArt getUmfeldDatenArtVon(final SystemObject objekt) {
		if (typAufArt == null) {
			throw new RuntimeException(
					"Umfelddatenarten wurden noch nicht initialisiert"); //$NON-NLS-1$
		}

		UmfeldDatenArt umfeldDatenArt = null;

		if (objekt != null) {
			umfeldDatenArt = typAufArt.get(objekt.getType());
		} else {
			System.out.println();
			Debug.getLogger().error("Uebergebenes Systemobjekt ist <<null>>");
		}

		return umfeldDatenArt;
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse also alle
	 * Umfelddatenarten.
	 * 
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<UmfeldDatenArt> getInstanzen() {
		if (typAufArt == null) {
			throw new RuntimeException(
					"Umfelddatenarten wurden noch nicht initialisiert"); //$NON-NLS-1$
		}

		return typAufArt.values();
	}

	/**
	 * Initialisierung.
	 * 
	 * @param dav1
	 *            eine Datenverteiler-Verbindung
	 * @throws DUAInitialisierungsException
	 *             wenn nicht alle Objekte initialisiert werden konnten
	 */
	public static void initialisiere(final ClientDavInterface dav1)
			throws DUAInitialisierungsException {
		if (typAufArt != null) {
			Debug.getLogger().error(
					"Umfelddatenarten duerfen nur einmal initialisiert werden"); //$NON-NLS-1$
		}
		dav = dav1;
		typAufArt = new HashMap<SystemObjectType, UmfeldDatenArt>();

		DataModel datenModell = dav.getDataModel();

		fbf = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsFahrBahnFeuchte"), "FBF"); //$NON-NLS-1$//$NON-NLS-2$
		fbg = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsFahrBahnGlätte"), "FBG"); //$NON-NLS-1$//$NON-NLS-2$	
		fbt = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsFahrBahnOberFlächenTemperatur"), "FBT"); //$NON-NLS-1$//$NON-NLS-2$	
		hk = new UmfeldDatenArt(datenModell.getType("typ.ufdsHelligkeit"), "HK"); //$NON-NLS-1$//$NON-NLS-2$
		gt = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsGefrierTemperatur"), "GT"); //$NON-NLS-1$//$NON-NLS-2$
		lt = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsLuftTemperatur"), "LT"); //$NON-NLS-1$//$NON-NLS-2$	
		ns = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsNiederschlagsArt"), "NS"); //$NON-NLS-1$//$NON-NLS-2$	
		ni = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsNiederschlagsIntensität"), "NI"); //$NON-NLS-1$//$NON-NLS-2$	
		nm = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsNiederschlagsMenge"), "NM"); //$NON-NLS-1$//$NON-NLS-2$	
		rlf = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsRelativeLuftFeuchte"), "RLF"); //$NON-NLS-1$//$NON-NLS-2$	
		sh = new UmfeldDatenArt(datenModell.getType("typ.ufdsSchneeHöhe"), "SH"); //$NON-NLS-1$//$NON-NLS-2$	
		sw = new UmfeldDatenArt(datenModell.getType("typ.ufdsSichtWeite"), "SW"); //$NON-NLS-1$//$NON-NLS-2$	
		tpt = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsTaupunktTemperatur"), "TPT"); //$NON-NLS-1$//$NON-NLS-2$	
		tt1 = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsTemperaturInTiefe1"), "TT1"); //$NON-NLS-1$//$NON-NLS-2$	
		tt2 = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsTemperaturInTiefe2"), "TT2"); //$NON-NLS-1$//$NON-NLS-2$	
		tt3 = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsTemperaturInTiefe3"), "TT3"); //$NON-NLS-1$//$NON-NLS-2$	
		wfd = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsWasserFilmDicke"), "WFD"); //$NON-NLS-1$//$NON-NLS-2$	
		wr = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsWindRichtung"), "WR"); //$NON-NLS-1$//$NON-NLS-2$	
		fbz = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsFahrBahnOberFlächenZustand"), "FBZ"); //$NON-NLS-1$//$NON-NLS-2$	
		ld = new UmfeldDatenArt(datenModell.getType("typ.ufdsLuftDruck"), "LD"); //$NON-NLS-1$//$NON-NLS-2$	
		rs = new UmfeldDatenArt(datenModell.getType("typ.ufdsRestSalz"), "RS"); //$NON-NLS-1$//$NON-NLS-2$	
		wgm = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsWindGeschwindigkeitMittelWert"), "WGM"); //$NON-NLS-1$//$NON-NLS-2$	
		wgs = new UmfeldDatenArt(datenModell
				.getType("typ.ufdsWindGeschwindigkeitSpitzenWert"), "WGS"); //$NON-NLS-1$//$NON-NLS-2$	

		typAufArt.put(fbf.getTyp(), fbf);
		typAufArt.put(fbg.getTyp(), fbg);
		typAufArt.put(fbt.getTyp(), fbt);
		typAufArt.put(hk.getTyp(), hk);
		typAufArt.put(gt.getTyp(), gt);
		typAufArt.put(lt.getTyp(), lt);
		typAufArt.put(ns.getTyp(), ns);
		typAufArt.put(ni.getTyp(), ni);
		typAufArt.put(nm.getTyp(), nm);
		typAufArt.put(rlf.getTyp(), rlf);
		typAufArt.put(sh.getTyp(), sh);
		typAufArt.put(sw.getTyp(), sw);
		typAufArt.put(tpt.getTyp(), tpt);
		typAufArt.put(tt1.getTyp(), tt1);
		typAufArt.put(tt2.getTyp(), tt2);
		typAufArt.put(tt3.getTyp(), tt3);
		typAufArt.put(wfd.getTyp(), wfd);
		typAufArt.put(wr.getTyp(), wr);
		typAufArt.put(fbz.getTyp(), fbz);
		typAufArt.put(ld.getTyp(), ld);
		typAufArt.put(rs.getTyp(), rs);
		typAufArt.put(wgm.getTyp(), wgm);
		typAufArt.put(wgs.getTyp(), wgs);
	}

	/**
	 * Standardkonstruktor.
	 * 
	 * @param typ
	 *            der Systemobjekttyp der Umfelddatenart
	 * @param abkuerzung
	 *            die Abkürzung der Umfelddatenart
	 * @throws DUAInitialisierungsException
	 *             wenn eines der übergebenen Objekte <code>null</code> ist
	 *             oder der Name des Umfelddatensensor-Typs nicht ermittelt
	 *             werden konnte
	 */
	private UmfeldDatenArt(final SystemObjectType typ, final String abkuerzung)
			throws DUAInitialisierungsException {
		if (typ == null) {
			throw new DUAInitialisierungsException(
					"Umfelddatensensor-Typ ist <<null>>"); //$NON-NLS-1$
		}
		if (abkuerzung == null) {
			throw new DUAInitialisierungsException(
					"Abkuerzung ist <<null>> fuer " + typ); //$NON-NLS-1$
		}

		this.typ = typ;
		this.abkuerzung = abkuerzung;
		this.name = typ.getPid().substring("typ.ufds".length()); //$NON-NLS-1$

		IntegerAttributeType type = (IntegerAttributeType) dav.getDataModel()
				.getAttributeType("att.ufds" + this.name); //$NON-NLS-1$
		IntegerValueRange range = type.getRange();
		if (range != null) {
			this.skalierung = range.getConversionFactor();
		}
	}

	/**
	 * Erfragt die Skalierung von Werten dieses Typs im Datenkatalog.
	 * 
	 * @return die Skalierung von Werten dieses Typs im Datenkatalog
	 */
	public double getSkalierung() {
		return this.skalierung;
	}

	/**
	 * Erfragt den Nam.
	 * 
	 * @return der Name der Umfelddatenart
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Erfragt die Abkürzung der Umfelddatenart.
	 * 
	 * @return die Abkürzung der Umfelddatenart
	 */
	public String getAbkuerzung() {
		return this.abkuerzung;
	}

	/**
	 * Erfragt den Systemobjekt-Typ des Umfelddatensensors.
	 * 
	 * @return den Systemobjekt-Typ des Umfelddatensensors
	 */
	public SystemObjectType getTyp() {
		return this.typ;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		boolean gleich = false;

		if (obj != null && obj instanceof UmfeldDatenArt) {
			UmfeldDatenArt that = (UmfeldDatenArt) obj;
			gleich = this.getTyp().getId() == that.getTyp().getId();
		}

		return gleich;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.name + " (" + this.abkuerzung + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
