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

package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Implementiert gemeinsame Funktionen von Parametern.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public abstract class AbstractParameterDatensatz extends AbstractDatensatz
		implements ParameterDatensatz {

	// TODO Asynchronen Sender einbauen

	/**
	 * der Aspekt für die Abfrage oder den Empfang von Sollparametern.
	 */
	private Aspect sollAspekt;

	/**
	 * Konstruktor.
	 * 
	 * @param objekt
	 *            ds Objekt, dem der Datensatz zugeordnet ist.
	 */
	public AbstractParameterDatensatz(SystemObjekt objekt) {
		super(objekt);
		if (sollAspekt == null) {
			sollAspekt = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel().getAspect(Konstante.DAV_ASP_PARAMETER_SOLL);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireAutoUpdate() {
		ClientDavInterface dav;
		DataDescription dbs;

		dav = ObjektFactory.getInstanz().getVerbindung();
		dbs = new DataDescription(getAttributGruppe(), sollAspekt);

		if (isAutoUpdate()) {
			dav.subscribeReceiver(getReceiver(), getObjekt().getSystemObject(),
					dbs, ReceiveOptions.normal(), ReceiverRole.receiver());
		} else {
			dav.unsubscribeReceiver(getReceiver(), getObjekt()
					.getSystemObject(), dbs);
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#fireUpdate()
	 */
	@Override
	protected void fireUpdate() {
		if (!isAutoUpdate()) {
			ClientDavInterface dav;
			DataDescription dbs;

			dav = ObjektFactory.getInstanz().getVerbindung();
			dbs = new DataDescription(getAttributGruppe(), sollAspekt);
			setDaten(dav.getData(getObjekt().getSystemObject(), dbs, 0)
					.getData());
		}
	}
}
