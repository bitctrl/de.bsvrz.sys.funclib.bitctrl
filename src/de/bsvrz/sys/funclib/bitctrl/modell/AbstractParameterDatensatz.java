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

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;

/**
 * Implementiert gemeinsame Funktionen von Parametern.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public abstract class AbstractParameterDatensatz extends AbstractDatensatz
		implements ParameterDatensatz {

	/** Die Datenbeschreibung mit der Daten empfangen werden. */
	private final DataDescription receiverDbs;

	/** Die Datenbeschreibung mit der Daten gesendet werden. */
	private final DataDescription senderDbs;

	/**
	 * Konstruktor.
	 * 
	 * @param objekt
	 *            ds Objekt, dem der Datensatz zugeordnet ist.
	 */
	public AbstractParameterDatensatz(SystemObjekt objekt) {
		super(objekt);

		Aspect asp;

		DataModel modell = ObjektFactory.getInstanz().getVerbindung()
				.getDataModel();
		asp = modell.getAspect(DaVKonstanten.ASP_PARAMETER_SOLL);
		receiverDbs = new DataDescription(getAttributGruppe(), asp);
		asp = modell.getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE);
		senderDbs = new DataDescription(getAttributGruppe(), asp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataDescription getEmpfangsDatenbeschreibung() {
		return receiverDbs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataDescription getSendeDatenbeschreibung() {
		return senderDbs;
	}

	/**
	 * Gibt immer {@code false} zur&uuml;ck. {@inheritDoc}
	 */
	@Override
	protected boolean isQuelle() {
		return false;
	}

	/**
	 * Gibt immer {@code false} zur&uuml;ck. {@inheritDoc}
	 */
	@Override
	protected boolean isSenke() {
		return false;
	}

}
