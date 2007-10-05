/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straﬂensubsegmentanalyse
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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Repr&auml;ssentiert einen Messquerschnitt.
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class MessQuerschnitt extends MessQuerschnittAllgemein {

	/**
	 * Definiert eine Ordung auf Messquerschnitte nach deren Offset. Die Ordnung
	 * ist nur innerhalb des selben Stra&szlig;ensegments korrekt.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	static class MessQuerschnittComparator implements
			Comparator<MessQuerschnitt> {

		/**
		 * Vergleicht die beiden Messquerschnitte nach ihrem Offset. Der
		 * vergleich ist nur dann sinnvoll, wenn sich beide Messquerschnitte auf
		 * dem gleichen Stra&szlig;ensegment befinde.
		 * <p>
		 * {@inheritDoc}
		 */
		public int compare(MessQuerschnitt mq1, MessQuerschnitt mq2) {
			return Float.compare(mq1.getStrassenSegmentOffset(), mq2
					.getStrassenSegmentOffset());
		}

	}

	static public Set<MessQuerschnitt> mqListe;

	public static Collection<MessQuerschnitt> getMqListe(final DataModel model) {
		Collection<MessQuerschnitt> result = new ArrayList<MessQuerschnitt>();

		if (mqListe == null) {
			List<SystemObject> listeSO;
			listeSO = model.getType(
					VerkehrsModellTypen.MESSQUERSCHNITT.getPid()).getElements();

			mqListe = new HashSet<MessQuerschnitt>();
			for (SystemObject so : listeSO) {
				MessQuerschnitt mq = (MessQuerschnitt) ObjektFactory
						.getInstanz().getModellobjekt(so);
				if (mq != null) {
					mqListe.add(mq);
				}
			}
		}

		result.addAll(mqListe);
		return result;
	}

	/**
	 * Erzeugt einen Messquerschnitt aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Messquerschnitt sein muss
	 * @throws IllegalArgumentException
	 */
	public MessQuerschnitt(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Messquerschnitt.");
		}

	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.MESSQUERSCHNITT;
	}
}
