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
package de.bsvrz.sys.funclib.bitctrl.daf;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * Überwacht alle {@link DavProvider} die als OSGi-Service registriert wurden.
 *
 * <p>
 * Wird eine neue Verbindung zum Datenverteiler aufgebaut, wird diese als neuer
 * Service registriert. Wird eine Verbindung zum Datenveteiler geschlossen, wird
 * der entsprechende Service deregistriert. Mit Hilfe eines
 * {@link ServiceTrackerCustomizer} kann auf diese Änderungen reagiert werden.
 *
 * <p>
 * Ein oder mehrere Services mit dem beschriebenen Verhalten müssen selbst am
 * BundleContext registriert werden. Als Vorlage für einen solchen Service kann
 * {@link DefaultDavProvider} dienen.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @see BundleContext#registerService(String, Object, java.util.Dictionary)
 */
public class DavProviderServiceTracker extends ServiceTracker {

	/**
	 * Erzeugt einen Service Tracker für einen bestimmten Bundlekontext.
	 *
	 * @param context
	 *            der Bundlekontext für den Service Tracker angelegt wird.
	 */
	public DavProviderServiceTracker(final BundleContext context) {
		super(context, DavProvider.class.getName(), null);
	}

	/**
	 * Erzeugt einen Service Tracker für einen bestimmten Bundlekontext.
	 *
	 * @param context
	 *            der Bundlekontext für den Service Tracker angelegt wird.
	 * @param customizer
	 *            Customizer der auf Hinzufügen und Schließen von Verbindungen
	 *            reagiert.
	 */
	public DavProviderServiceTracker(final BundleContext context,
			final ServiceTrackerCustomizer customizer) {
		super(context, DavProvider.class.getName(), customizer);
	}

	/**
	 * Gibt die Objekt Factory der Nutzerverbindung zurück.
	 *
	 * @return die Objekt Factory oder <code>null</code>, wenn keine verfügbar
	 *         ist.
	 */
	public DavProvider getNutzerverbindung() {
		final ServiceReference[] serviceReferences = getServiceReferences();
		if (serviceReferences != null) {
			for (final ServiceReference reference : serviceReferences) {
				final Object name = reference
						.getProperty(DavProvider.PROP_NAME);
				if (DavProvider.NUTZVERVERBINDUNG.equals(name)) {
					return (DavProvider) getService(reference);
				}
			}
		}

		return null;
	}

	/**
	 * Gibt die Objekt Factory der Urlasserverbindung zurück.
	 *
	 * @return die Objekt Factory oder <code>null</code>, wenn keine verfügbar
	 *         ist.
	 */
	public DavProvider getUrlasserverbindung() {
		final ServiceReference[] serviceReferences = getServiceReferences();
		if (serviceReferences != null) {
			for (final ServiceReference reference : serviceReferences) {
				final Object name = reference
						.getProperty(DavProvider.PROP_NAME);
				if (DavProvider.URLASSERVERBINDUNG.equals(name)) {
					return (DavProvider) getService(reference);
				}
			}
		}

		return null;
	}

	/**
	 * Gibt eine Datenverteilerverbindung zurück.
	 *
	 * @return eine Datenverteilerverbindung oder <code>null</code>, wenn im
	 *         Moment keine zur Verfügung steht.
	 * @see #getService()
	 */
	public DavProvider getDatenverteilerVerbindung() {
		return (DavProvider) getService();
	}

	/**
	 * Gibt alle verfügbaren Datenverteilerverbindungen zurück.
	 *
	 * @return alle im Moment vorhandenen Verbindungen oder <code>null</code>,
	 *         wenn keine zur Verfügung stehen.
	 * @see #getServices()
	 */
	public DavProvider[] getDatenverteilerVerbindungen() {
		return (DavProvider[]) getServices();
	}

	/**
	 * Gibt eine bestimmte Datenverteilerverbindung zurück.
	 *
	 * @param name
	 *            der Name der gesuchten Verbindung.
	 * @return die gesuchte Verbindung oder <code>null</code>, wenn diese im
	 *         Moment nicht zur Verfügung steht.
	 */
	public DavProvider getDatenverteilerVerbindung(final String name) {
		final DavProvider[] verbindungen = getDatenverteilerVerbindungen();
		if (verbindungen != null) {
			for (final DavProvider v : verbindungen) {
				if (name.equals(v.getName())) {
					return v;
				}
			}
		}
		return null;
	}

}
