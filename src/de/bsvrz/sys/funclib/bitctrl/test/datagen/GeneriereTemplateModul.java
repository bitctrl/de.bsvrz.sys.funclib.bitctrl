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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.test.datagen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.Attribute;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.AttributeGroupUsage;
import de.bsvrz.dav.daf.main.config.AttributeListDefinition;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Das Modul zum Erzeugen der Vorlagedateien für die Konfiguration von
 * Datenquellen.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
class GeneriereTemplateModul extends DatenGeneratorModul {

	/**
	 * erzeugt eine Instanz des Moduls.
	 * 
	 * @param connection
	 *            die verwendete Datenverteilerverbindung
	 * @param argumente
	 *            die Argumente des Moduls
	 */
	GeneriereTemplateModul(final ClientDavInterface connection,
			final Map<String, String> argumente) {
		super(connection, argumente);
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.test.datagen.DatenGeneratorModul#ausfuehren()
	 */
	@Override
	protected int ausfuehren() {

		DataModel model = getConnection().getDataModel();

		String templateDirName = getArgumente().get("-verzeichnis");
		if ((templateDirName == null) || (templateDirName.length() <= 0)) {
			templateDirName = "dataTemplates";
		}
		File verzeichnis = new File(templateDirName);
		verzeichnis.mkdirs();

		for (SystemObject obj : (model.getType("typ.attributgruppe"))
				.getElements()) {
			AttributeGroup atg = (AttributeGroup) obj;

			for (Aspect asp : atg.getAspects()) {
				AttributeGroupUsage usage = atg.getAttributeGroupUsage(asp);
				if (!usage.isConfigurating() /*
												 * && (
												 * usage.getUsage().equals(Usage.OnlineDataAsSourceReceiverOrSenderDrain))
												 */) {
					try {
						File file = new File(verzeichnis, atg.getPid() + "_"
								+ asp.getPid() + ".template");
						FileOutputStream output = new FileOutputStream(file);
						PrintWriter writer = new PrintWriter(output, true);

						writer.println("[config]");
						writer.println("objekt = ");
						writer.println("atg = " + atg.getPid());
						writer.println("asp = " + asp.getPid());
						writer.println("rolle = quelle" + asp.getPid());
						writer.println("");

						writer.println("[default]");
						for (Attribute att : atg.getAttributes()) {
							printAttribut(writer, "", att, true);
						}
						writer.println("");

						writer.println("[daten]");
						writer.print("Zeit");
						for (Attribute att : atg.getAttributes()) {
							printAttribut(writer, "", att, false);
						}
						writer.println("");

						output.flush();
						output.close();
					} catch (FileNotFoundException e) {
						// TODO Automatisch erstellter Catch-Block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Automatisch erstellter Catch-Block
						e.printStackTrace();
					}
				}
			}
		}
		return 0;
	}

	/**
	 * gibt ein Attribut aus.
	 * 
	 * @param out
	 *            der Ausgabestrom
	 * @param prefix
	 *            der verwendete Prefix für das Attribut
	 * @param att
	 *            das Attribut
	 * @param defWert
	 *            Attribut im Standardbereich ausgeben ?
	 */
	private void printAttribut(final PrintWriter out, final String prefix,
			final Attribute att, final boolean defWert) {
		if (att.getAttributeType() instanceof AttributeListDefinition) {
			String nextPrefix = prefix;
			if (att.isArray()) {
				nextPrefix = nextPrefix + att.getName() + "[0].";
			} else {
				nextPrefix = nextPrefix + att.getName() + ".";
			}
			for (Attribute atlAtt : ((AttributeListDefinition) att
					.getAttributeType()).getAttributes()) {
				printAttribut(out, nextPrefix, atlAtt, defWert);
			}
		} else {
			if (defWert) {
				out.println(prefix + att.getName() + " = "
						+ att.getDefaultAttributeValue());
			} else {
				out.print(";" + prefix + att.getName());
			}
		}
	}
}
