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
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Umfelddatenart. Verbindet alle notwendigen Informationen (Name, Abkürzung)
 * eines Umfelddaten-Systemobjekttyps mit dem Systemobjekttyp selbst. Diese
 * Klasse muss statisch instanziiert werden, bevor irgendeine Methode daraus das
 * erste Mal benutzt werden kann.<br>
 * z.B.:<br>
 * Typ: typ.ufdsFahrBahnFeuchte --&gt; Name: FahrBahnFeuchte --&gt; Abk: FBF
 *
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class UmfeldDatenArt {

	/**
	 * Mapt den Systemobjekttyp eines Umfelddatensensors auf die Informationen
	 * zu seinem Namen und seiner Abkürzung.
	 */
	private static Map<SystemObjectType, UmfeldDatenArt> typAufArt;

	/**
	 * Verbindung zum Datenverteiler.
	 */
	private static ClientDavInterface dav;

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
	 * <code>typ.ufdsTaustoffmenge</code>.
	 */
	public static UmfeldDatenArt tsq;

	/**
	 * <code>typ.ufdsWindGeschwindigkeitMittelWert</code>.
	 */
	public static UmfeldDatenArt wgm;

	/**
	 * <code>typ.ufdsWindGeschwindigkeitSpitzenWert</code>.
	 */
	public static UmfeldDatenArt wgs;

	/**
	 * <code>typ.ufdsZeitreserveGlätteVaisala</code>
	 */
	public static UmfeldDatenArt zg;

	/**
	 * der Systemobjekt-Typ des Umfelddatensensors.
	 */
	private final SystemObjectType typ;

	/**
	 * der Name der Umfelddatenart.
	 */
	private final String name;

	/**
	 * die Abkürzung der Umfelddatenart.
	 */
	private final String abkuerzung;

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
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 *             wenn die Datenart nicht bestimmt werden kann (d.h. der Typ
	 *             ist in der Liste der unterstützten Typen nicht enthalten,
	 *             siehe initialisiere()).
	 */
	public static UmfeldDatenArt getUmfeldDatenArtVon(final SystemObject objekt)
			throws UmfeldDatenSensorUnbekannteDatenartException {
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

		if (umfeldDatenArt == null) {
			throw new UmfeldDatenSensorUnbekannteDatenartException(
					"Datenart von Umfelddatensensor " + objekt + " ("
							+ objekt.getType()
							+ ") konnte nicht identifiziert werden");
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

		final DataModel datenModell = dav.getDataModel();

		fbf = new UmfeldDatenArt(datenModell.getType("typ.ufdsFahrBahnFeuchte"), //$NON-NLS-1$
				"FBF"); //$NON-NLS-1$
		fbg = new UmfeldDatenArt(datenModell.getType("typ.ufdsFahrBahnGlätte"), //$NON-NLS-1$
				"FBG"); //$NON-NLS-1$
		fbt = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsFahrBahnOberFlächenTemperatur"), //$NON-NLS-1$
				"FBT"); //$NON-NLS-1$
		hk = new UmfeldDatenArt(datenModell.getType("typ.ufdsHelligkeit"), //$NON-NLS-1$
				"HK"); //$NON-NLS-1$
		gt = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsGefrierTemperatur"), "GT"); //$NON-NLS-1$//$NON-NLS-2$
		lt = new UmfeldDatenArt(datenModell.getType("typ.ufdsLuftTemperatur"), //$NON-NLS-1$
				"LT"); //$NON-NLS-1$
		ns = new UmfeldDatenArt(datenModell.getType("typ.ufdsNiederschlagsArt"), //$NON-NLS-1$
				"NS"); //$NON-NLS-1$
		ni = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsNiederschlagsIntensität"), "NI"); //$NON-NLS-1$//$NON-NLS-2$
		nm = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsNiederschlagsMenge"), "NM"); //$NON-NLS-1$//$NON-NLS-2$
		rlf = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsRelativeLuftFeuchte"), "RLF"); //$NON-NLS-1$//$NON-NLS-2$
		sh = new UmfeldDatenArt(datenModell.getType("typ.ufdsSchneeHöhe"), //$NON-NLS-1$
				"SH"); //$NON-NLS-1$
		sw = new UmfeldDatenArt(datenModell.getType("typ.ufdsSichtWeite"), //$NON-NLS-1$
				"SW"); //$NON-NLS-1$
		tpt = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsTaupunktTemperatur"), "TPT"); //$NON-NLS-1$//$NON-NLS-2$
		tt1 = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsTemperaturInTiefe1"), "TT1"); //$NON-NLS-1$//$NON-NLS-2$
		tt2 = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsTemperaturInTiefe2"), "TT2"); //$NON-NLS-1$//$NON-NLS-2$
		tt3 = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsTemperaturInTiefe3"), "TT3"); //$NON-NLS-1$//$NON-NLS-2$
		wfd = new UmfeldDatenArt(datenModell.getType("typ.ufdsWasserFilmDicke"), //$NON-NLS-1$
				"WFD"); //$NON-NLS-1$
		wr = new UmfeldDatenArt(datenModell.getType("typ.ufdsWindRichtung"), //$NON-NLS-1$
				"WR"); //$NON-NLS-1$
		fbz = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsFahrBahnOberFlächenZustand"), //$NON-NLS-1$
				"FBZ"); //$NON-NLS-1$
		ld = new UmfeldDatenArt(datenModell.getType("typ.ufdsLuftDruck"), "LD"); //$NON-NLS-1$//$NON-NLS-2$
		rs = new UmfeldDatenArt(datenModell.getType("typ.ufdsRestSalz"), "RS"); //$NON-NLS-1$//$NON-NLS-2$
		wgm = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsWindGeschwindigkeitMittelWert"), //$NON-NLS-1$
				"WGM"); //$NON-NLS-1$
		wgs = new UmfeldDatenArt(
				datenModell.getType("typ.ufdsWindGeschwindigkeitSpitzenWert"), //$NON-NLS-1$
				"WGS"); //$NON-NLS-1$

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

		final SystemObjectType zgType = datenModell.getType("typ.ufdsZeitreserveGlätteVaisala");
		if (null != zgType) {
			zg = new UmfeldDatenArt(zgType, "ZG");
			typAufArt.put(zgType, zg);
		}
		final SystemObjectType tsqType = datenModell.getType("typ.ufdsTaustoffmenge");
		if (null != tsqType) {
			tsq = new UmfeldDatenArt(tsqType, "TSQ");
			typAufArt.put(tsqType, tsq);
		}
	}

	/**
	 * Standardkonstruktor.
	 *
	 * @param typ
	 *            der Systemobjekttyp der Umfelddatenart
	 * @param abkuerzung
	 *            die Abkürzung der Umfelddatenart
	 * @throws DUAInitialisierungsException
	 *             wenn eines der übergebenen Objekte <code>null</code> ist oder
	 *             der Name des Umfelddatensensor-Typs nicht ermittelt werden
	 *             konnte
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
		name = typ.getPid().substring("typ.ufds".length()); //$NON-NLS-1$

		final IntegerAttributeType type = (IntegerAttributeType) dav
				.getDataModel().getAttributeType("att.ufds" + name); //$NON-NLS-1$
		if (type != null) {
			final IntegerValueRange range = type.getRange();
			if (range != null) {
				skalierung = range.getConversionFactor();
			}
		}
	}

	/**
	 * Erfragt die Skalierung von Werten dieses Typs im Datenkatalog.
	 *
	 * @return die Skalierung von Werten dieses Typs im Datenkatalog
	 */
	public double getSkalierung() {
		return skalierung;
	}

	/**
	 * Erfragt den Nam.
	 *
	 * @return der Name der Umfelddatenart
	 */
	public String getName() {
		return name;
	}

	/**
	 * Erfragt die Abkürzung der Umfelddatenart.
	 *
	 * @return die Abkürzung der Umfelddatenart
	 */
	public String getAbkuerzung() {
		return abkuerzung;
	}

	/**
	 * Erfragt den Systemobjekt-Typ des Umfelddatensensors.
	 *
	 * @return den Systemobjekt-Typ des Umfelddatensensors
	 */
	public SystemObjectType getTyp() {
		return typ;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean gleich = false;

		if ((obj != null) && (obj instanceof UmfeldDatenArt)) {
			final UmfeldDatenArt that = (UmfeldDatenArt) obj;
			gleich = getTyp().getId() == that.getTyp().getId();
		}

		return gleich;
	}

	@Override
	public int hashCode() {
		return (int) getTyp().getId();
	}

	@Override
	public String toString() {
		return name + " (" + abkuerzung + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
