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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.Attribute;
import de.bsvrz.dav.daf.main.config.AttributeGroup;

/**
 * Kapselt eine Attributgruppe und erweitert sie um nützliche Funktionen.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class Attributgruppe {

	private final AttributeGroup atg;
	private Data daten;
	private int maxFeldlaenge = 1;
	private AttributFilter filter;

	/** Cached die Liste der Attribute (Hierarchie). */
	private List<Attribut> attribute;

	/** Cached die Liste der "abgeflachten" und gefilterten Attribute. */
	private List<Attribut> attributeFlach;

	/**
	 * Initialisiert das Objekt.
	 *
	 * @param attributgruppe
	 *            eine Attributgruppe.
	 */
	public Attributgruppe(final AttributeGroup attributgruppe) {
		atg = attributgruppe;
	}

	/**
	 * Gibt die gekapselte Attributgruppe zurück.
	 *
	 * @return die gekapselte Attributgruppe.
	 */
	public AttributeGroup getAtg() {
		return atg;
	}

	/**
	 * Gibt die Liste der Attribute der Attributgruppe in hierarchischer Form
	 * zurück.
	 *
	 * @return die hierarchische Liste der Attribute.
	 */
	public List<Attribut> getAttribute() {
		if (attribute == null) {
			attribute = new ArrayList<>();
			for (final Attribute att : atg.getAttributes()) {
				attribute.add(new Attribut(null, att));
			}
		}

		return Collections.unmodifiableList(attribute);
	}

	/**
	 * Gibt die Liste aller Attribute der Attributgruppe in flacher Form zurück.
	 *
	 * @return die flache Liste der Attribute.
	 */
	public List<Attribut> getAttributeFlach() {
		if (attributeFlach == null) {
			attributeFlach = new ArrayList<>();
			for (final Attribut att : getAttribute()) {
				for (final Attribut a : findeAttribute(att)) {
					if (filter != null && !filter.contains(a)) {
						continue;
					}
					attributeFlach.add(a);
				}
			}
		}
		return Collections.unmodifiableList(attributeFlach);
	}

	/**
	 * Gibt den aktuellen Datensatz zurück, der angezeigt wird.
	 *
	 * @return der aktuelle Datensatz.
	 */
	public Data getDaten() {
		return daten;
	}

	/**
	 * Legt den Wert aktuellen Datensatz fest, der angezeigt werden soll.
	 *
	 * @param daten
	 *            der neue Datensatz.
	 */
	public void setDaten(final Data daten) {
		this.daten = daten;
	}

	/**
	 * Gibt die Länge des addressierten Felds zurück.
	 *
	 * @param pfad
	 *            der Pfad zu einem Feld.
	 * @return dessen Länge.
	 */
	public int getFeldLaenge(final String pfad) {
		final Object o = getDaten(pfad);
		if (o instanceof Data && ((Data) o).isArray()) {
			return ((Array) o).getLength();
		} else if (o.equals("<leeres Feld>")) {
			return 0;
		}

		throw new IllegalArgumentException("Der Pfad " + pfad
				+ " addressiert kein Feld.");
	}

	/**
	 * Gibt den Wert des Datums des Attributs zurück, welches der Pfad
	 * adressiert.
	 *
	 * @param pfad
	 *            ein Pfad innerhalb der Attributgruppe.
	 * @return das Datum des Attributs oder {@code null}, wenn kein Datum
	 *         gesetzt wurde oder für den Pfad keine Daten existieren. Der
	 *         letzte Fall tritt ein, wenn ein Feldelement addressiert wird,
	 *         welches über die Länge des Feldes hinausgeht.
	 */
	public Data getDaten(final String pfad) {
		if (daten == null) {
			return null;
		}

		final String[] elemente = pfad.split("\\.");
		Data datum = null;

		for (int i = 0; i < elemente.length; i++) {
			if (datum == null) {
				datum = daten.getItem(elemente[0]);
				if (datum.isArray()) {
					i--;
					if (elemente.length == 1) {
						return datum;
					}
				}
			} else {
				if (datum.isArray()) {
					if (datum.asArray().getLength() > 0) {
						int index;

						i++;
						if (i > elemente.length - 1) {
							return datum;
						}
						index = Integer.valueOf(elemente[i]);
						if (index > datum.asArray().getLength()) {
							/*
							 * Dies tritt ein, wenn in der flachen Darstellung
							 * ein Element abgerufen wird, das physisch nicht
							 * vorhanden ist.
							 */
							return null;
						}
						datum = datum.asArray().getItem(index - 1);
					} else {
						return datum;
					}
				} else {
					datum = datum.getItem(elemente[i]);
					if (datum.isArray()) {
						i--;
					}
				}
			}
		}

		assert datum != null;
		return datum;
	}

	/**
	 * Gibt den Wert des Datums des Attributs, welches der Pfad adressiert, als
	 * String zurück.
	 *
	 * @param pfad
	 *            ein Pfad innerhalb der Attributgruppe.
	 * @return das Attributdatum als String oder {@code null}, wenn kein Datum
	 *         gesetzt wurde.
	 */
	public String getDatum(final String pfad) {
		final Data datum = getDaten(pfad);
		if (datum != null) {
			if (datum.isArray()) {
				return "<Feld>";
			} else if (datum.isList()) {
				return "<Attributliste>";
			}
			return datum.asTextValue().getText();
		}
		return "";
	}

	/**
	 * Gibt den aktuellen Filter für die Attribute der Attributgruppe zurück.
	 *
	 * @return der aktuell Attributfilter.
	 */
	public AttributFilter getFilter() {
		return filter;
	}

	/**
	 * Legt den Filter der Attribute der Attributgruppe fest.
	 *
	 * @param filter
	 *            der neue Attributfilter.
	 */
	public void setFilter(final AttributFilter filter) {
		if (filter != null && !equals(filter.getAtg())) {
			throw new IllegalArgumentException(
					"Der Filter ist auf die Attributgrppe nicht anwendbar.");
		}
		this.filter = filter;
		attributeFlach = null;
	}

	/**
	 * Gibt die maximale Feldlänge zurück.
	 *
	 * @return die maximale Feldlänge.
	 */
	public int getMaxFeldlaenge() {
		return maxFeldlaenge;
	}

	/**
	 * Legt die maximale Feldlänge fest.
	 *
	 * @param maxFeldlaenge
	 *            die neue maximale Feldlänge.
	 */
	public void setMaxFeldlaenge(final int maxFeldlaenge) {
		this.maxFeldlaenge = maxFeldlaenge;
		attributeFlach = null;
	}

	@Override
	public String toString() {
		return atg.getName() + " ( " + atg.getPid() + ")";
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Attributgruppe) {
			final Attributgruppe anderes = (Attributgruppe) obj;

			return atg.equals(anderes.atg);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return atg.hashCode();
	}

	/**
	 * Sucht rekursiv nach Attributen ausgehend von einem übergeordneten
	 * Attribut.
	 *
	 * @param attribut
	 *            das oberste Attribut, bei dem die Suche starten soll.
	 * @return die Liste aller untergeordneten Attribute.
	 */
	private List<Attribut> findeAttribute(final Attribut attribut) {
		final List<Attribut> liste = new ArrayList<>();

		if (attribut.isFeld()) {
			for (int i = 1; i < maxFeldlaenge + 1; i++) {
				attribut.setIndex(i);

				if (attribut.isAttributliste()) {
					for (final Attribut a : attribut.clone().getKinder()) {
						liste.addAll(findeAttribute(a));
					}
				} else {
					liste.add(attribut.clone());
				}
			}
		} else if (attribut.isAttributliste()) {
			for (final Attribut att : attribut.getKinder()) {
				liste.addAll(findeAttribute(att));
			}
		} else {
			liste.add(attribut);
		}

		return liste;
	}

}
