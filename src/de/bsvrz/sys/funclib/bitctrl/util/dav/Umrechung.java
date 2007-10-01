/*
 * Allgemeine Funktionen
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.util.dav;

/**
 * Beinhaltet Hilfsfunktionen zum Umrechnen zwischen verschieden Messwerten.
 * Berechnet werden aus den Eingangswerten:
 * <ul>
 * <li>QKfz</li>
 * <li>QLkw</li>
 * <li>VPkw</li>
 * <li>VLkw</li>
 * </ul>
 * die Ausgangswerte:
 * <ul>
 * <li>ALkw</li>
 * <li>QPkw</li>
 * <li>VKfz</li>
 * <li>QB</li>
 * </ul>
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class Umrechung {

	/**
	 * Berechnet den Lkw-Anteil.
	 * 
	 * @param qLkw
	 *            Lkw-Vekehrsst&auml;rke
	 * @param qKfz
	 *            Gesamte Vekehrsst&auml;rke
	 * @return Lkw-Anteil
	 */
	public static Integer getALkw(Integer qLkw, Integer qKfz) {
		if (qLkw != null && qKfz != null) {
			// assert qKfz >= qLkw;

			if (qKfz != 0) {
				return qLkw * 100 / qKfz;
			}

			return 0;
		}

		return null;
	}

	/**
	 * Berechnet QPkw aus QKfz und QLkw.
	 * 
	 * @param qKfz
	 *            QKfz
	 * @param qLkw
	 *            QLkw
	 * @return QPkw
	 */
	public static Integer getQPkw(Integer qKfz, Integer qLkw) {
		if (qKfz != null && qLkw != null) {
			// assert qKfz > qLkw;
			return qKfz - qLkw;
		}

		return null;
	}

	/**
	 * Berechnet QPkw aus QKfz und QLkw.
	 * 
	 * @param qKfz
	 *            QKfz
	 * @param qLkw
	 *            QLkw
	 * @return QPkw
	 */
	public static Double getQPkw(Double qKfz, Double qLkw) {
		if (qKfz != null && qLkw != null) {
			// assert qKfz > qLkw;
			return qKfz - qLkw;
		}

		return null;
	}

	/**
	 * Berechnet VKfz aus QLkw, QKfz, VPkw und VLkw.
	 * 
	 * @param qLkw
	 *            QLkw
	 * @param qKfz
	 *            QKfz
	 * @param vPkw
	 *            VPkw
	 * @param vLkw
	 *            VLkw
	 * @return VKfz
	 */
	public static Integer getVKfz(Integer qLkw, Integer qKfz, Integer vPkw,
			Integer vLkw) {
		if (vPkw != null && qLkw != null && vLkw != null && qKfz != null
				&& qKfz > 0) {
			// assert qKfz > qLkw;

			Integer qPkw;

			qPkw = getQPkw(qKfz, qLkw);
			return Math.round((float) (qPkw * vPkw + qLkw * vLkw) / qKfz);
		}

		return null;
	}

	/**
	 * Berechnet VKfz aus QLkw, QKfz, VPkw und VLkw.
	 * 
	 * @param qLkw
	 *            QLkw
	 * @param qKfz
	 *            QKfz
	 * @param vPkw
	 *            VPkw
	 * @param vLkw
	 *            VLkw
	 * @return VKfz
	 */
	public static Double getVKfz(Double qLkw, Double qKfz, Double vPkw,
			Double vLkw) {
		if (vPkw != null && qLkw != null && vLkw != null && qKfz != null
				&& qKfz > 0) {
			// assert qKfz > qLkw;

			Double qPkw;

			qPkw = getQPkw(qKfz, qLkw);
			return (qPkw * vPkw + qLkw * vLkw) / qKfz;
		}

		return null;
	}

	/**
	 * Berechnet QB aus QLkw, QKfz, VPkw und VLkw sowie mit Hilfe zweier
	 * Parameter k1 und k2.
	 * 
	 * @param qLkw
	 *            QLkw
	 * @param qKfz
	 *            QKfz
	 * @param vPkw
	 *            VPkw
	 * @param vLkw
	 *            VLkw
	 * @param k1
	 *            k1
	 * @param k2
	 *            k2
	 * @return QB
	 */
	public static Integer getQB(Integer qLkw, Integer qKfz, Integer vPkw,
			Integer vLkw, float k1, float k2) {
		if (vPkw != null && qLkw != null && vLkw != null && qKfz != null) {
			// assert qKfz > qLkw;

			float fLGL;
			Integer qPkw;

			qPkw = getQPkw(qKfz, qLkw);
			if (vPkw > vLkw) {
				fLGL = k1 + k2 * (vPkw - vLkw);
			} else {
				fLGL = k1;
			}

			return Math.round(qPkw + fLGL * qLkw);
		}

		return null;
	}

	/**
	 * Berechnet QB aus QLkw, QKfz, VPkw und VLkw sowie mit Hilfe zweier
	 * Parameter k1 und k2.
	 * 
	 * @param qLkw
	 *            QLkw
	 * @param qKfz
	 *            QKfz
	 * @param vPkw
	 *            VPkw
	 * @param vLkw
	 *            VLkw
	 * @param k1
	 *            k1
	 * @param k2
	 *            k2
	 * @return QB
	 */
	public static Double getQB(Double qLkw, Double qKfz, Double vPkw,
			Double vLkw, float k1, float k2) {
		if (vPkw != null && qLkw != null && vLkw != null && qKfz != null) {
			// assert qKfz > qLkw;

			Double fLGL;
			Double qPkw;

			qPkw = getQPkw(qKfz, qLkw);
			if (vPkw > vLkw) {
				fLGL = k1 + k2 * (vPkw - vLkw);
			} else {
				fLGL = (double) k1;
			}

			return qPkw + fLGL * qLkw;
		}

		return null;
	}

	/**
	 * Konstruktor verstecken, da es nur statische Methoden gibt.
	 */
	private Umrechung() {
		// nix
	}

}
