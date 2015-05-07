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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */
package de.bsvrz.sys.funclib.bitctrl.daf;

import java.util.EventListener;

import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Listener zur Anmeldung am {@link SendRegistrationStore}, um &uuml;ber
 * Sendefreigaben (dataRequest) informiert zu werden.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */
public interface SendRegistrationStoreDataRequestListener extends EventListener {

	/**
	 * Wird vom {@link SendRegistrationStore} bei jedem
	 * {@link ClientSenderInterface#dataRequest(SystemObject, DataDescription, byte)}
	 * -Callback aufgerufen.
	 *
	 * @param obj
	 * @param desc
	 * @param state
	 */
	void registrationStoreDataRequest(final SystemObject obj,
			final DataDescription desc, final byte state);
}
