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
package de.bsvrz.sys.funclib.bitctrl.util;

import java.text.Collator;
import java.util.Comparator;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Sortiert Systemobjekte nach ihrem Namen. Die Sortierung berücksichtigt lokale
 * Besonderheiten wie z.&nbsp;B. Umlaute.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class SystemObjectComparator implements Comparator<SystemObject> {

	@Override
	public int compare(final SystemObject so1, final SystemObject so2) {
		return Collator.getInstance().compare(so1.getNameOrPidOrId(),
				so2.getNameOrPidOrId());
	}

}
