package com.growcontrol.plugins.arduinogc.server.configs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.growcontrol.common.meta.MetaAddress;
import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.poixson.commonapp.config.xConfig;
import com.poixson.commonapp.config.xConfigException;
import com.poixson.commonjava.Utils.DualKeyMap;
import com.poixson.commonjava.Utils.DualKeyMapUnmodifiable;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.xHashable;


public abstract class HardwareConfig extends xConfig implements xHashable {

	protected volatile String key = null;

	private final String name;
	private final String title;
	private final boolean enabled;
	private final int id;

	private final DualKeyMap<String, Integer, MetaAddress> dests;



	public HardwareConfig(final Map<String, Object> datamap)
			throws xConfigException {
		super(datamap);
		// hardware name
		this.name = this.getString(PluginDefines.CONFIG_HARDWARE_NAME);
		if(utils.isEmpty(this.name)) throw new xConfigException("Name is missing from config!");
		// hardware title
		{
			final String value = this.getStr(
					PluginDefines.CONFIG_HARDWARE_NAME,
					null
			);
			this.title =
					utils.isEmpty(value)
					? this.name
					: value;
		}
		// enabled - default to enabled if key doesn't exist
		this.enabled =
				this.exists(PluginDefines.CONFIG_HARDWARE_ENABLED)
				? this.getBool(PluginDefines.CONFIG_HARDWARE_ENABLED, false)
				: true;
		// hardware id
		this.id = this.getInt(PluginDefines.CONFIG_HARDWARE_ID, 0);
		// destination pin addresses
		{
			final Map<String, Object> destMap = this.getMap(
					String.class,
					Object.class,
					PluginDefines.CONFIG_HARDWARE_PINS
			);
			if(utils.isEmpty(destMap)) throw new xConfigException("Pins list is missing from config!");
			final Map<String,  MetaAddress> strMap = new LinkedHashMap<String,  MetaAddress>();
			final Map<Integer, MetaAddress> idMap  = new LinkedHashMap<Integer, MetaAddress>();
			for(final Entry<String, Object> entry : destMap.entrySet()) {
				final String addrStr = entry.getKey().toLowerCase();
				final Object obj = entry.getValue();
				final Integer pinInt;
				if(obj instanceof Integer) {
					pinInt = (Integer) entry.getValue();
				} else {
//TODO:
throw new UnsupportedOperationException("UNFINISHED");
//					final String str = obj.toString();
				}
				final MetaAddress addr = MetaAddress.get(addrStr);
				strMap.put(addr.getKey(), addr);
				idMap.put( pinInt,        addr);
			}
			this.dests = new DualKeyMapUnmodifiable<String, Integer, MetaAddress>(strMap, idMap);
		}
	}



	public String getName() {
		return this.name;
	}
	public String getTitle() {
		return this.title;
	}
	public boolean isEnabled() {
		return this.enabled;
	}
	public int getId() {
		return this.id;
	}



	public Map<String, MetaAddress> getPinsByAddr() {
		return this.dests.getMapK();
	}
	public Map<Integer, MetaAddress> getPinsById() {
		return this.dests.getMapJ();
	}
	public List<MetaAddress> getPins() {
		final Collection<MetaAddress> addrs = this.dests.values();
		if(utils.isEmpty(addrs))
			return null;
		return Collections.unmodifiableList(
				new ArrayList<MetaAddress>(addrs)
		);
	}



	@Override
	public String toString() {
		return this.getKey();
	}
	@Override
	public String getKey() {
		return this.key;
	}
	protected abstract String genKey();
	@Override
	public boolean matches(final xHashable hashable) {
		if(hashable == null || !(hashable instanceof HardwareConfig) )
			return false;
		return this.getKey().equalsIgnoreCase(hashable.getKey());
	}



}
