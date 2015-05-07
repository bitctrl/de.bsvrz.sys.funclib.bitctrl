/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2015 BitCtrl Systems GmbH
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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.adapter.AbstraktBearbeitungsKnotenAdapter;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.DFSKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.ModulTyp;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IStandardAspekte;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;

/**
 * Dieses Modul funktioniert wie ein normaler Bearbeitungsknoten mit folgenden
 * Unterschieden:.<br>
 * 1.) Es werden keine Daten plausibilisiert<br>
 * 2.) Die Publikation ist standardm��ig angeschaltet und kann nicht
 * ausgeschaltet werden<br>
 * 3.) F�r das selbe Systemobjekt darf nicht zweimal hintereinander die Kennung
 * <code>keine Daten</code> versendet werden
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class PublikationsModul extends AbstraktBearbeitungsKnotenAdapter {

	/**
	 * der Typ des Moduls, f�r den dieser Bearbeitungsknoten publizieren soll.
	 */
	private final ModulTyp modulTyp;

	/**
	 * Parameter zur Datenflusssteuerung f�r diese SWE und dieses Modul.
	 */
	private IDatenFlussSteuerungFuerModul iDfsMod = DFSKonstanten.STANDARD;

	/**
	 * Zustand <code>keine Daten</code> jedes Objektes.
	 */
	private final Map<SystemObject, Boolean> keineDaten = new HashMap<>();

	/**
	 * Standardkonstruktor.
	 *
	 * @param stdAspekte
	 *            Informationen zu den Standardpublikationsaspekten f�r dieses
	 *            Modul
	 * @param modulTyp
	 *            der Typ des Moduls, f�r den dieser Bearbeitungsknoten
	 *            publizieren soll oder <code>null</code>, wenn die Publikation
	 *            hier nicht dynamisch sein soll (sich also nicht an der
	 *            Datenflusssteuerung f�r dieses Modul orientieren soll)
	 */
	public PublikationsModul(final IStandardAspekte stdAspekte,
			final ModulTyp modulTyp) {
		standardAspekte = stdAspekte;
		this.modulTyp = modulTyp;
	}

	@Override
	public void initialisiere(final IVerwaltung dieVerwaltung)
			throws DUAInitialisierungsException {
		super.initialisiere(dieVerwaltung);
		publikationsAnmeldungen.modifiziereObjektAnmeldung(standardAspekte
				.getStandardAnmeldungen(verwaltung.getSystemObjekte()));
		for (final SystemObject objekt : verwaltung.getSystemObjekte()) {
			keineDaten.put(objekt, true);
		}
	}

	@Override
	public void aktualisiereDaten(final ResultData[] resultate) {
		if (resultate != null) {
			for (final ResultData resultat : resultate) {
				if (resultat != null) {
					ResultData publikationsDatum = null;

					if (modulTyp != null) {
						publikationsDatum = iDfsMod.getPublikationsDatum(
								resultat, resultat.getData(),
								standardAspekte.getStandardAspekt(resultat));
					} else {
						publikationsDatum = new ResultData(
								resultat.getObject(), new DataDescription(
										resultat.getDataDescription()
										.getAttributeGroup(),
										standardAspekte
										.getStandardAspekt(resultat)),
								resultat.getDataTime(), resultat.getData());

					}

					if (publikationsDatum != null) {
						if (publikationsDatum.getData() == null) {
							final Boolean objektStehtAktuellAufKeineDaten = keineDaten
									.get(publikationsDatum.getObject());
							if (objektStehtAktuellAufKeineDaten != null
									&& !objektStehtAktuellAufKeineDaten) {
								publikationsAnmeldungen
								.sende(publikationsDatum);
							}
						} else {
							publikationsAnmeldungen.sende(publikationsDatum);
						}

						keineDaten.put(publikationsDatum.getObject(),
								publikationsDatum.getData() == null);
					}
				}
			}

			if (knoten != null) {
				knoten.aktualisiereDaten(resultate);
			}
		}
	}

	@Override
	public ModulTyp getModulTyp() {
		return modulTyp;
	}

	@Override
	public void aktualisierePublikation(final IDatenFlussSteuerung iDfs) {
		if (modulTyp != null) {
			iDfsMod = iDfs.getDFSFuerModul(verwaltung.getSWETyp(),
					getModulTyp());

			Collection<DAVObjektAnmeldung> anmeldungenStd = new ArrayList<>();

			if (standardAspekte != null) {
				anmeldungenStd = standardAspekte
						.getStandardAnmeldungen(verwaltung.getSystemObjekte());
			}

			final Collection<DAVObjektAnmeldung> anmeldungen = iDfsMod
					.getDatenAnmeldungen(verwaltung.getSystemObjekte(),
							anmeldungenStd);

			synchronized (this) {
				publikationsAnmeldungen.modifiziereObjektAnmeldung(anmeldungen);
			}
		}
	}
}
