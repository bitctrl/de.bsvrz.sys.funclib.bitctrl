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

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;

/**
 * Implementiert gemeinsame Funktionen von Parametern.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id: AbstractParameterDatensatz.java 4345 2007-10-12 15:23:20Z
 *          Schumann $
 */
public abstract class AbstractParameterDatensatz extends AbstractDatensatz
		implements ParameterDatensatz {

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
	public AbstractParameterDatensatz(SystemObjekt objekt) {
		super(objekt);

		if (receiverAsp == null && senderAsp == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			receiverAsp = modell.getAspect(DaVKonstanten.ASP_PARAMETER_SOLL);
			senderAsp = modell.getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Aspect getEmpfangsAspekt() {
		return receiverAsp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Aspect getSendeAspekt() {
		return senderAsp;
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
