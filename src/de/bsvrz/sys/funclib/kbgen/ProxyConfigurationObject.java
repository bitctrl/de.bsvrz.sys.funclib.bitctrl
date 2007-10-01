/*
 * Copyright 2006 by inovat, Dipl.-Ing. H. C. Kniß
 * ALL RIGHTS RESERVED.
 *
 * THIS SOFTWARE IS  PROVIDED  "AS IS"  AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF  MERCHANTABILITY  AND  FITNESS  FOR  A PARTICULAR  PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL inovat OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES  (INCL., BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES;  LOSS OF USE, DATA, OR  PROFITS;
 * OR BUSINESS INTERRUPTION)  HOWEVER  CAUSED  AND  ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,  OR TORT (INCL.
 * NEGLIGENCE OR OTHERWISE)  ARISING  IN  ANY  WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.bsvrz.sys.funclib.kbgen;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Repräsentiert ein Stellvertreterobjekt für ein {@link stauma.dav.configuration.interfaces.ConfigurationObject}.
 * Dieses Stellvertreterobjekt hält alle Informationen eines {@link stauma.dav.configuration.interfaces.ConfigurationObject}
 * einschließlich der Daten der konfigurierenden Attributgruppen. Aus diesen Informationen wird dann über ein Objekt der
 * Klasse {@link ConfigAreaCreator} ein Konfigurationsbereich erzeugt. Das Proxyobjekt enthält damit alle Informatioen,
 * die später (nach der Erzeugung des Konfigurationsbereichs) aus der Konfiguration zu dem entsprechenden Objekt
 * ermittelt werden können.
 *
 * @author inovat, innovative systeme - verkehr - tunnel - technik
 * @author Dipl.-Ing. Hans Christian Kniß (HCK)
 * @version $Revision: 113 $ / $Date: 2007-03-25 10:49:50 +0200 (So, 25 Mrz 2007) $ / ($Author: HCK $)
 */

public class ProxyConfigurationObject {
	// ------------------------------------------------------------------------
	// VARIABLENDEKLARATIONEN
	// ------------------------------------------------------------------------

	/** DebugLogger für Debug-Ausgaben. */
	private static final Debug debug = Debug.getLogger();
	private final String _pid;
	private final String _name;
	private final SystemObjectType _type;
	private List<ProxyDataDescription> _proxyConfigurationDataList = new ArrayList<ProxyDataDescription>();
	private List<ProxySetDescription> _proxySetList = new ArrayList<ProxySetDescription>();

	// ------------------------------------------------------------------------
	// KONSTRUKTOREN  (und vom Konstruktor verwende Methoden)
	// ------------------------------------------------------------------------

	/** Erzeugt ein Objekt vom Typ ProxyConfigurationObject */
	public ProxyConfigurationObject(String pid, String name, SystemObjectType type) {
		_pid = pid;
		_name = name;
		_type = type;
	}

	// ------------------------------------------------------------------------
	// GETTER / SETTER METHODEN
	// ------------------------------------------------------------------------

	public String getName() {
		return _name;
	}

	public String getPid() {
		return _pid;
	}

	public List<ProxyDataDescription> getProxyConfigurationDataList() {
		return _proxyConfigurationDataList;
	}

	public List<ProxySetDescription> getProxySetList() {
		return _proxySetList;
	}

	public SystemObjectType getType() {
		return _type;
	}

	// ------------------------------------------------------------------------
	// SONSTIGE METHODEN
	// ------------------------------------------------------------------------

	public void addConfigurationData(ProxyDataDescription proxyDataDescription) {
		_proxyConfigurationDataList.add(proxyDataDescription);
	}

	public void addSet(ProxySetDescription proxySetDescription) {
		_proxySetList.add(proxySetDescription);
	}
}
