package de.bsvrz.sys.funclib.bitctrl.util;

import java.text.Collator;
import java.util.Comparator;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Sortiert Systemobjekte nach ihrem Namen. Die Sortierung berücksichtigt lokale
 * Besonderheiten wie z.&nbsp;B. Umlaute.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class SystemObjectComparator implements Comparator<SystemObject> {

	public int compare(final SystemObject so1, final SystemObject so2) {
		return Collator.getInstance().compare(so1.getNameOrPidOrId(),
				so2.getNameOrPidOrId());
	}

}
