/*
 * BitCtrl- Datenverteiler- Funktionsbibliothek
 * Copyright (C) 2007-2010 BitCtrl Systems GmbH 
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
package de.bsvrz.sys.funclib.bitctrl.datenpunkt;

import com.bitctrl.util.resultset.IIndividualResult;
import com.bitctrl.util.resultset.IRelatedResultSet;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Ganzzahliger Ergebniswert aus dem Datenverteiler.
 * 
 * @author BitCtrl Systems GmbH, uhlmann
 * @version $Id: DavUnscaledValueIndividualResult.java 27375 2010-11-04
 *          16:30:14Z uhlmann $
 */
public class DavUnscaledValueIndividualResult extends Datenpunkt implements
		IIndividualResult<Long, ResultData> {

	private final IRelatedResultSet<Long, ResultData> parent;

	/**
	 * Konstruktor weist nur das Elternobjekt zu.
	 * 
	 * @param parent
	 *            das Elternobjekt, also die ERgebnismenge, der wir angehören
	 */
	public DavUnscaledValueIndividualResult(
			final IRelatedResultSet<Long, ResultData> parent) {
		super();
		this.parent = parent;
	}

	public Long getCurrentValue() {
		final Long currentValue;
		final Data v = getLastValue();
		if (v != null && v.isPlain()
				&& v.getAttributeType() instanceof IntegerAttributeType) {
			currentValue = v.asUnscaledValue().longValue();
		} else {
			currentValue = -1L;
		}
		return currentValue;
	}

	public ResultData getCurrentBaseSetValue() {
		return getLastResult();
	}

	@Override
	public void update(final ResultData result) {
		super.update(result);
		parent.neuerWert(this, getCurrentValue());
	}

	public String getName() {
		final SystemObject o = getObject();
		if (null == o) {
			return "Nicht verbundener Datenpunkt";
		}
		return o.getName();
	}

	public void dispose() {
		abmelden();
	}

}
