/*
 * BitCtrl- Datenverteiler- Funktionsbibliothek
 * Copyright (C) 2007-2010 BitCtrl Systems GmbH 
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
package de.bsvrz.sys.funclib.bitctrl.datenpunkt;

import java.util.NoSuchElementException;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.dav.daf.main.impl.InvalidArgumentException;

/**
 * Eine Repräsentation einer Attributliste vom Format der
 * <code>atl.datenpunkt</code> mitsamt den Live-Daten vom Datenverteiler, d.h.
 * das Objekt ist angemeldet!
 * 
 * @author BitCtrl Systems GmbH, Albrecht Uhlmann
 * @version $Id$
 */
public class Datenpunkt implements ClientReceiverInterface {

	private Data atlDatenpunkt;

	private Data lastValue;

	private ResultData lastResult;

	private ClientDavInterface connection;

	private SystemObject object;

	private DataDescription dataDescription;

	private String[] pfadKomponenten;

	private void abmelden() {
		if (null == atlDatenpunkt) {
			return;
		}
		connection.unsubscribeReceiver(this, object, dataDescription);
		atlDatenpunkt = null;
		dataDescription = null;
		object = null;
		pfadKomponenten = null;
	}

	private void anmelden() throws InvalidArgumentException {
		final SystemObjectType type = (SystemObjectType) atlDatenpunkt
				.getReferenceValue("Typ").getSystemObject();
		final SystemObject o = atlDatenpunkt.getReferenceValue("Objekt")
				.getSystemObject();
		if (!o.isOfType(type.getPid())) {
			throw new InvalidArgumentException("Objekt '" + o
					+ "' ist nicht vom Typ '" + type + "'");
		}
		final AttributeGroup atg = (AttributeGroup) atlDatenpunkt
				.getReferenceValue("Attributgruppe").getSystemObject();
		if (!type.getAttributeGroups().contains(atg)) {
			throw new InvalidArgumentException("Attributgruppe '" + atg
					+ "' ist am Typ '" + type + "' nicht definiert");
		}
		final Aspect asp = (Aspect) atlDatenpunkt.getReferenceValue("Aspekt")
				.getSystemObject();
		if (!atg.getAspects().contains(asp)) {
			throw new InvalidArgumentException("Aspekt '" + asp
					+ "' ist an Attributgruppe '" + atg + "' nicht definiert");
		}
		final String pfad = atlDatenpunkt.getTextValue("Pfad").getText();
		if (null == pfad || pfad.isEmpty()) {
			throw new InvalidArgumentException("Leerer Pfad ist nicht erlaubt");
		}
		pfadKomponenten = pfad.split("\\.");
		if (null == atg.getAttribute(pfadKomponenten[0])) {
			throw new InvalidArgumentException("Das Attribut '"
					+ pfadKomponenten[0] + "' ist an der Attributgruppe '"
					+ atg + "' nicht definiert");
		}
		dataDescription = new DataDescription(atg, asp);
		object = o;
		connection.subscribeReceiver(this, o, dataDescription, ReceiveOptions
				.normal(), ReceiverRole.receiver());
	}

	/**
	 * Liefert die Beschreibung des aktuellen Datenpunktes, also ein Data-Objekt
	 * vom Format der <code>atl.datenpunkt</code>
	 * 
	 * @return der Datenpunkt
	 */
	public Data getAtlDatenpunkt() {
		return atlDatenpunkt;
	}

	/**
	 * Setzt den Datenpunkt.
	 * 
	 * @param atlDatenpunkt
	 *            der Datenpunkt
	 * @throws InvalidArgumentException
	 *             falls das übergebene Objekt inkonsistent ist, z.B. das
	 *             referenzierte Objekt nicht vom angegebenen Typ ist oder der
	 *             referenzierte Typ die referenzierte Attributgruppe gar nicht
	 *             hat, usw.
	 */
	public void setAtlDatenpunkt(final Data atlDatenpunkt)
			throws InvalidArgumentException {
		abmelden();
		this.atlDatenpunkt = atlDatenpunkt;
		anmelden();
	}

	/**
	 * @return the lastValue
	 */
	public Data getLastValue() {
		return lastValue;
	}

	public void update(final ResultData[] results) {
		if (null == atlDatenpunkt) {
			return;
		}
		for (final ResultData result : results) {
			update(result);
		}
	}

	/**
	 * Aktualisiert einen Ergebnisdatensatz aus dem Feld von
	 * Ergebnisdatensätzen, die über die DAF-API kamen.
	 * 
	 * @param result
	 *            der Ergebnisdatensatz
	 */
	protected void update(final ResultData result) {
		if (result.getDataDescription().equals(dataDescription)) {
			lastResult = result;
			if (!result.hasData()) {
				lastValue = null;
			} else {
				try {
					Data currentData = result.getData();
					for (final String pfad : pfadKomponenten) {
						currentData = currentData.getItem(pfad);
					}
					lastValue = currentData;
				} catch (final NoSuchElementException e) {
					lastValue = null;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (null == dataDescription || null == pfadKomponenten) {
			return super.equals(o);
		}
		if (o instanceof Datenpunkt) {
			final Datenpunkt dp = (Datenpunkt) o;
			return dataDescription.equals(dp.getDataDescription())
					&& pfadKomponenten.equals(dp.getPfadKomponenten());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (null == dataDescription || null == pfadKomponenten) {
			return super.hashCode();
		}
		return dataDescription.hashCode() + pfadKomponenten.hashCode();
	}

	/**
	 * Liefert die dataDescription
	 * 
	 * @return die dataDescription, kann <code>null</code> sein.
	 */
	public DataDescription getDataDescription() {
		return dataDescription;
	}

	/**
	 * Liefert die Pfadkomponenten
	 * 
	 * @return die Pfadkomponenten, kann null sein.
	 */
	public String[] getPfadKomponenten() {
		return pfadKomponenten;
	}

	/**
	 * Setzt die Datenverteilerverbindung
	 * 
	 * @param connection
	 *            die Datenverteilerverbindung
	 */
	public void setConnection(final ClientDavInterface connection) {
		this.connection = connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (dataDescription == null || object == null
				|| pfadKomponenten == null) {
			return "Keine Daten";
		}

		String result = object.getPid() + "-" + dataDescription.toString()
				+ "-" + pfadKomponenten[0];
		int loop;
		for (loop = 1; loop < pfadKomponenten.length; ++loop) {
			result += "." + pfadKomponenten[loop];
		}
		result += ":";
		result += getLastValue();
		return result;
	}

	/**
	 * Liefert das Systemobjekt, dem dieser Datenpunkt zugeordnet ist.
	 * 
	 * @return das Objekt oder null, wenn noch keine Anmeldung erfolgt ist.
	 */
	public SystemObject getObject() {
		return object;
	}

	/**
	 * Liefert den letzten empfangenen Ergebnisdatensatz.
	 * 
	 * @return der Ergebnisdatensatz. Kann <code>null</code> sein, wenn noch nie
	 *         einer empfangen wurde. Für den Fall, dass der Ergebnisdatensatz
	 *         keine Daten enthält, wird er trotzdem gespeichert, nur #lastValue
	 *         wird null.
	 */
	public ResultData getLastResult() {
		return lastResult;
	}
}
