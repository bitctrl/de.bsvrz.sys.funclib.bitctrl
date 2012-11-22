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
 * @version $Id: $
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
