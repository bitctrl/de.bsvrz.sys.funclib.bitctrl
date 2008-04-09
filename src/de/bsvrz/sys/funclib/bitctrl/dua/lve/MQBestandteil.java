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

package de.bsvrz.sys.funclib.bitctrl.dua.lve;

/**
 * Messquerschnittbestandteil eines virtuellen Messquerschnittes.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public class MQBestandteil {

	/**
	 * ein Messquerschnitt.
	 */
	private MessQuerschnitt mq = null;

	/**
	 * der Anteil, mit dem dieser Messquerschnitt in den virtuellen
	 * Messquerschnitt eingeht.
	 */
	private double anteil = 1.0;

	/**
	 * Standardkonstruktor.
	 * 
	 * @param mq
	 *            ein Messquerschnitt
	 * @param anteil
	 *            der Anteil, mit dem dieser Messquerschnitt in den virtuellen
	 *            Messquerschnitt eingeht
	 */
	public MQBestandteil(MessQuerschnitt mq, final double anteil) {
		this.mq = mq;
		this.anteil = anteil;
	}

	/**
	 * Erfragt den Anteil, mit dem dieser Messquerschnitt in den virtuellen
	 * Messquerschnitt eingeht.
	 * 
	 * @return anteil der Anteil, mit dem dieser Messquerschnitt in den
	 *         virtuellen Messquerschnitt eingeht
	 */
	public final double getAnteil() {
		return anteil;
	}

	/**
	 * Erfragt den Messquerschnitt.
	 * 
	 * @return mq der Messquerschnitt
	 */
	public final MessQuerschnitt getMq() {
		return mq;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.mq + ", Anteil: " + this.anteil; //$NON-NLS-1$
	}

}
