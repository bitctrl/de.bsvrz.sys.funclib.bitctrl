package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.AeusseresStrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.Baustelle;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.FahrStreifen;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.InneresStrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnitt;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnittVirtuell;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.Netz;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.Stau;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.Strasse;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StrassenKnoten;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StrassenTeilSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrModellNetz;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;
import de.bsvrz.sys.funclib.debug.Debug;

public class GeoModellFactory implements ModellObjektFactory {

	private static final Debug LOGGER = Debug.getLogger();

	public SystemObjekt getModellobjekt(SystemObject objekt) {
		if (objekt == null) {
			throw new IllegalArgumentException("Argument darf nicht null sein.");
		}

		SystemObjekt obj = null;
		if (objekt.isOfType(GeoModellTypen.KREISGRENZEN.getPid())) {
			obj = new Kreisgrenzen(objekt);
		} else if (objekt.isOfType(GeoModellTypen.ORTSLAGE.getPid())) {
			obj = new Ortslage(objekt);
		} else if (objekt.isOfType(GeoModellTypen.ORTSNAME.getPid())) {
			obj = new Ortsname(objekt);
		} else if (objekt.isOfType(GeoModellTypen.KOMPLEX_XY.getPid())) {
			obj = new KomplexXY(objekt);
		} else if (objekt.isOfType(GeoModellTypen.PUNKT_XY.getPid())) {
			obj = new PunktXYImpl(objekt);
		} else if (objekt.isOfType(GeoModellTypen.LINIE_XY.getPid())) {
			obj = new LinieXYImpl(objekt);
		} else if (objekt.isOfType(GeoModellTypen.FLAECHE_XY.getPid())) {
			obj = new FlaecheXYImpl(objekt);
		}

		if (obj == null) {
			LOGGER.error("Für das Ohbjekt: " + objekt
					+ " konnte kein Systemobjekt angelegt werden");
		}

		return obj;
	}

	public Collection<? extends SystemObjektTyp> getTypen() {
		return Arrays.asList(GeoModellTypen.values());
	}

}
