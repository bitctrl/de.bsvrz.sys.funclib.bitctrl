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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.Arrays;
import java.util.Collection;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;

/**
 * Implementiert gemeinsame Funktionen von Parametern.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @param <T>
 *            Der Typ des Datums den der Datensatz sichert.
 */
public abstract class AbstractParameterDatensatz<T extends Datum>
extends AbstractDatensatz<T>implements ParameterDatensatz<T> {

	/** Der Aspaket mit dem Parameter empfangen werden. */
	private static Aspect receiverAsp;

	/** Der Aspaket mit dem Parameter gesendet werden. */
	private static Aspect senderAsp;

	/**
	 * Konstruktor.
	 *
	 * @param objekt
	 *            ds Objekt, dem der Datensatz zugeordnet ist.
	 */
	public AbstractParameterDatensatz(final SystemObjekt objekt) {
		super(objekt);

		if ((receiverAsp == null) || (senderAsp == null)) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			receiverAsp = modell.getAspect(DaVKonstanten.ASP_PARAMETER_SOLL);
			senderAsp = modell.getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE);
			assert(receiverAsp != null) && (senderAsp != null);
		}
	}

	@Override
	public void abmeldenSender() {
		abmeldenSender(senderAsp);
	}

	@Override
	public T abrufenDatum() {
		return super.abrufenDatum(receiverAsp);
	}

	@Override
	public void addUpdateListener(final DatensatzUpdateListener l) {
		addUpdateListener(receiverAsp, l);
	}

	@Override
	public void anmeldenSender() throws AnmeldeException {
		anmeldenSender(senderAsp);
	}

	/**
	 * Informiert angemeldete Listener &uuml;ber ein neues Datum.
	 *
	 * @param datum
	 *            das neue Datum.
	 * @see AbstractDatensatz#fireDatensatzAktualisiert(Aspect, Datum)
	 */
	protected void fireDatensatzAktualisiert(final T datum) {
		fireDatensatzAktualisiert(receiverAsp, datum);
	}

	@Override
	protected Collection<Aspect> getAspekte() {
		return Arrays.asList(new Aspect[] { receiverAsp, senderAsp });
	}

	@Override
	public T getDatum() {
		return getDatum(receiverAsp);
	}

	@Override
	public Status getStatusSendesteuerung() {
		return super.getStatusSendesteuerung(senderAsp);
	}

	@Override
	public boolean isAngemeldetSender() {
		return isAngemeldetSender(senderAsp);
	}

	@Override
	public boolean isAutoUpdate() {
		return isAutoUpdate(receiverAsp);
	}

	@Override
	protected boolean isQuelle(final Aspect asp) {
		return false;
	}

	@Override
	protected boolean isSenke(final Aspect asp) {
		return false;
	}

	@Override
	public void removeUpdateListener(final DatensatzUpdateListener l) {
		removeUpdateListener(receiverAsp, l);
	}

	@Override
	public void sendeDaten(final T datum) throws DatensendeException {
		sendeDaten(senderAsp, datum);
	}

	@Override
	public void sendeDaten(final T datum, final long timeout)
			throws DatensendeException {
		sendeDaten(senderAsp, datum, timeout);
	}

	/**
	 * Legt die Daten des Parameters fest.
	 *
	 * @param datum
	 *            das neue Datum.
	 * @see AbstractDatensatz#setDatum(Aspect, Datum)
	 */
	protected void setDatum(final T datum) {
		setDatum(receiverAsp, datum);
	}

}
