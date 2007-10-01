/*
 * Copyright 2004 by Kappich+Kni� Systemberatung Aachen (K2S)
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

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.sys.funclib.dataIdentificationSettings.DataIdentification;

/**
 * Schnittstelle f�r Beobachter der Parameters�tze mit Einstellungen die sich auf Datenidentifikationen beziehen.
 * Derartige Parameters�tze werden z.B. zur Steuerung des Archivverhaltens (atg.archiv) und der Parametrierung
 * (atg.parametrierung) eingesetzt.
 *
 * @author Kappich+Kni� Systemberatung Aachen (K2S)
 * @author Roland Schmitz (rs)
 * @version $Revision: 1237 $ / $Date: 2004-03-24 10:04:03 +0100 (Mi, 24 Mrz 2004) $ / ($Author: roland $)
 */
public interface UpdateListener extends EventListener {
	/**
	 * Wird bei �nderung des Parameters f�r jede Datenidentifikation aufgerufen f�r die es einen Eintrag gab oder gibt.
	 *
	 * @param dataIdentification Betroffene Datenidentifikation.
	 * @param oldSettings        Zur Datenidentifikation geh�rende Einstellungen vor der �nderung oder <code>null</code>
	 *                           wenn es vor der �nderung keinen spezifischen Eintrag gab.
	 * @param newSettings        Zur Datenidentifikation geh�rende Einstellungen nach der �nderung oder <code>null</code>
	 *                           wenn es nach der �nderung keinen spezifischen Eintrag mehr gibt.
	 */
	void update(DataIdentification dataIdentification, Data oldSettings, Data newSettings);
}
