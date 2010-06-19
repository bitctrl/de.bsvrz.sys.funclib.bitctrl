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
 * @version $Id: DavProviderServiceTracker.java 20127 2009-10-23 12:27:22Z
 *          schumann $
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
