package de.bsvrz.sys.funclib.bitctrl.daf;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * �berwacht alle {@link DavProvider} die als OSGi-Service
 * registriert wurden.
 * 
 * <p>
 * Wird eine neue Verbindung zum Datenverteiler aufgebaut, wird diese als neuer
 * Service registriert. Wird eine Verbindung zum Datenveteiler geschlossen, wird
 * der entsprechende Service deregistriert. Mit Hilfe eines
 * {@link ServiceTrackerCustomizer} kann auf diese �nderungen reagiert werden.
 * 
 * <p>
 * Ein oder mehrere Services mit dem beschriebenen Verhalten m�ssen selbst am
 * BundleContext registriert werden. Als Vorlage f�r einen solchen Service kann
 * {@link DefaultDavProvider} dienen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * @see BundleContext#registerService(String, Object, java.util.Dictionary)
 */
public class DavProviderServiceTracker extends ServiceTracker {

	/**
	 * Erzeugt einen Service Tracker f�r einen bestimmten Bundlekontext.
	 * 
	 * @param context
	 *            der Bundlekontext f�r den Service Tracker angelegt wird.
	 */
	public DavProviderServiceTracker(final BundleContext context) {
		super(context, DavProvider.class.getName(), null);
	}

	/**
	 * Erzeugt einen Service Tracker f�r einen bestimmten Bundlekontext.
	 * 
	 * @param context
	 *            der Bundlekontext f�r den Service Tracker angelegt wird.
	 * @param customizer
	 *            Customizer der auf Hinzuf�gen und Schlie�en von Verbindungen
	 *            reagiert.
	 */
	public DavProviderServiceTracker(final BundleContext context,
			final ServiceTrackerCustomizer customizer) {
		super(context, DavProvider.class.getName(), customizer);
	}

	/**
	 * Gibt eine Datenverteilerverbindung zur�ck.
	 * 
	 * @return eine Datenverteilerverbindung oder <code>null</code>, wenn im
	 *         Moment keine zur Verf�gung steht.
	 * @see #getService()
	 */
	public DavProvider getDatenverteilerVerbindung() {
		return (DavProvider) getService();
	}

	/**
	 * Gibt alle verf�gbaren Datenverteilerverbindungen zur�ck.
	 * 
	 * @return alle im Moment vorhandenen Verbindungen oder <code>null</code>,
	 *         wenn keine zur Verf�gung stehen.
	 * @see #getServices()
	 */
	public DavProvider[] getDatenverteilerVerbindungen() {
		return (DavProvider[]) getServices();
	}

	/**
	 * Gibt eine bestimmte Datenverteilerverbindung zur�ck.
	 * 
	 * @param name
	 *            der Name der gesuchten Verbindung.
	 * @return die gesuchte Verbindung oder <code>null</code>, wenn diese im
	 *         Moment nicht zur Verf�gung steht.
	 */
	public DavProvider getDatenverteilerVerbindung(
			final String name) {
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
