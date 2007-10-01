/*
 * Copyright 2005 by Kappich+Kniß Systemberatung Aachen (K2S)
 *
 * This file is part of K2S-Kernsoftware-Bibliothek.
 *
 * K2S-Kernsoftware-Bibliothek is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * K2S-Kernsoftware-Bibliothek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with K2S-Kernsoftware-Bibliothek; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package de.bsvrz.sys.funclib.parameter.dataIdentificationSettings;

import java.util.EventListener;

/**
 * Schnittstelle für Beobachter, die informiert werden wollen, wann alle Einstellungen abgearbeitet wurden. Diese
 * Information wird benötigt, falls zwischen dem Erhalt aller Einstellungen und der Anmeldung beim Datenverteiler
 * getrennt werden muss (z.B. im {@link ars.archive.admin.ArchiveApplication Archivsystem}).
 *
 * @author Kappich+Kniß Systemberatung Aachen (K2S)
 * @author Stephan Homeyer (sth)
 * @version $Revision: 2031 $ / $Date: 2005-06-09 11:25:24 +0200 (Do, 09 Jun 2005) $ / ($Author: homeyer $)
 */
public interface EndOfSettingsListener extends EventListener {
	/**
	 * Wird aufgerufen, sobald alle Einstellungen für jede Datenidentifikation durchlaufen wurden.
	 */
	void inform();
}
