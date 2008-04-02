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
public final class Umrechung {

	/**
	 * Berechnet den Lkw-Anteil.
	 * 
	 * @param qLkw
	 *            Lkw-Vekehrsst&auml;rke
	 * @param qKfz
	 *            Gesamte Vekehrsst&auml;rke
	 * @return Lkw-Anteil
	 */
	public static Integer getALkw(final Integer qLkw, final Integer qKfz) {
		if (qLkw != null && qKfz != null) {
			assert qKfz >= qLkw;

			if (qKfz != 0) {
				return qLkw * 100 / qKfz;
			}

			return 0;
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
	public static Double getQB(final Double qLkw, final Double qKfz,
			final Double vPkw, final Double vLkw, final float k1, final float k2) {
		Double qb;

		qb = null;
		if (vPkw != null && qLkw != null && vLkw != null && qKfz != null) {
			assert qKfz >= qLkw;

			Double fLGL;
			Double qPkw;

			qPkw = getQPkw(qKfz, qLkw);
			if (vPkw > vLkw) {
				fLGL = k1 + k2 * (vPkw - vLkw);
			} else {
				fLGL = (double) k1;
			}

			qb = qPkw + fLGL * qLkw;
		} else if (qLkw != null && qLkw == 0) {
			qb = getQPkw(qKfz, qLkw);
		}

		return qb;
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
	public static Integer getQB(final Integer qLkw, final Integer qKfz,
			final Integer vPkw, final Integer vLkw, final float k1,
			final float k2) {
		if (vPkw != null && qLkw != null && vLkw != null && qKfz != null) {
			assert qKfz >= qLkw;

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
	 * Berechnet QPkw aus QKfz und QLkw.
	 * 
	 * @param qKfz
	 *            QKfz
	 * @param qLkw
	 *            QLkw
	 * @return QPkw
	 */
	public static Double getQPkw(final Double qKfz, final Double qLkw) {
		if (qKfz != null && qLkw != null) {
			assert qKfz >= qLkw;
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
	public static Integer getQPkw(final Integer qKfz, final Integer qLkw) {
		if (qKfz != null && qLkw != null) {
			assert qKfz >= qLkw;
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
	public static Double getVKfz(final Double qLkw, final Double qKfz,
			final Double vPkw, final Double vLkw) {
		if (vPkw != null && qLkw != null && vLkw != null && qKfz != null
				&& qKfz > 0) {
			assert qKfz >= qLkw;

			Double qPkw;

			qPkw = getQPkw(qKfz, qLkw);
			return (qPkw * vPkw + qLkw * vLkw) / qKfz;
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
	public static Integer getVKfz(final Integer qLkw, final Integer qKfz,
			final Integer vPkw, final Integer vLkw) {
		if (vPkw != null && qLkw != null && vLkw != null && qKfz != null
				&& qKfz > 0) {
			assert qKfz >= qLkw;

			Integer qPkw;

			qPkw = getQPkw(qKfz, qLkw);
			return Math.round((float) (qPkw * vPkw + qLkw * vLkw) / qKfz);
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
