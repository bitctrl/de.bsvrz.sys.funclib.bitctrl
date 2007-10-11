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

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Implementiert gemeinsame Funktionen von Parametern.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public abstract class AbstractParameterDatensatz extends AbstractDatensatz {

	// TODO Asynchronen Sender einbauen

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireAutoUpdate() {
		ClientDavInterface dav;
		DataModel modell;
		DataDescription dbs;

		dav = ObjektFactory.getInstanz().getVerbindung();
		modell = dav.getDataModel();
		dbs = new DataDescription(getAttributGruppe(), modell
				.getAspect(Konstante.DAV_ASP_PARAMETER_SOLL));

		if (isAutoUpdate()) {
			dav.subscribeReceiver(this, getObjekt().getSystemObject(), dbs,
					ReceiveOptions.normal(), ReceiverRole.receiver());
		} else {
			dav.unsubscribeReceiver(this, getObjekt().getSystemObject(), dbs);
		}
	}

}
