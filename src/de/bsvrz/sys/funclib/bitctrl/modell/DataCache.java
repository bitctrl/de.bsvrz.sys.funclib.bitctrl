package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;

public final class DataCache {
	private static final DataCache instance = new DataCache();
	private final Map<SystemObjectType, Set<AttributeGroup>> cachedData = new HashMap<SystemObjectType, Set<AttributeGroup>>();

	private DataCache() {
		super();
	}

	static public void cacheData(SystemObjectType type, AttributeGroup atg) {
		boolean cache = false;
		Set<AttributeGroup> set = instance.cachedData.get(type);
		if (set == null) {
			set = new HashSet<AttributeGroup>();
			instance.cachedData.put(type, set);
			cache = true;
		} else {
			if (!set.contains(atg)) {
				set.add(atg);
				cache = true;
			}
		}

		if (cache) {
			type.getDataModel().getConfigurationData(type.getElements(), atg);
		}
	}
}
