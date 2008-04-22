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
 * @version $Id$
 * @param <T>
 *            Der Typ des Datums den der Datensatz sichert.
 */
public abstract class AbstractParameterDatensatz<T extends Datum> extends
		AbstractDatensatz<T> implements ParameterDatensatz<T> {

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

		if (receiverAsp == null || senderAsp == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			receiverAsp = modell.getAspect(DaVKonstanten.ASP_PARAMETER_SOLL);
			senderAsp = modell.getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE);
			assert receiverAsp != null && senderAsp != null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#abmeldenSender()
	 */
	public void abmeldenSender() {
		abmeldenSender(senderAsp);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#abrufenDatum()
	 */
	public T abrufenDatum() {
		return super.abrufenDatum(receiverAsp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#addUpdateListener(de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener)
	 */
	public void addUpdateListener(final DatensatzUpdateListener l) {
		addUpdateListener(receiverAsp, l);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#anmeldenSender()
	 */
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#getAspekte()
	 */
	@Override
	protected Collection<Aspect> getAspekte() {
		return Arrays.asList(new Aspect[] { receiverAsp, senderAsp });
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#getDatum()
	 */
	public T getDatum() {
		return getDatum(receiverAsp);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#getStatusSendesteuerung()
	 */
	public Status getStatusSendesteuerung() {
		return super.getStatusSendesteuerung(senderAsp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#isAngemeldetSender()
	 */
	public boolean isAngemeldetSender() {
		return isAngemeldetSender(senderAsp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#isAutoUpdate()
	 */
	public boolean isAutoUpdate() {
		return isAutoUpdate(receiverAsp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#isQuelle(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	protected boolean isQuelle(final Aspect asp) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#isSenke(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	protected boolean isSenke(final Aspect asp) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#removeUpdateListener(de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener)
	 */
	public void removeUpdateListener(final DatensatzUpdateListener l) {
		removeUpdateListener(receiverAsp, l);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#sendeDaten(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	public void sendeDaten(final T datum) throws DatensendeException {
		sendeDaten(senderAsp, datum);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#sendeDaten(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
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
