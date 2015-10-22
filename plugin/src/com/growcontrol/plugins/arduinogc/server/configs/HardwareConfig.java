package com.growcontrol.plugins.arduinogc.server.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.growcontrol.common.meta.MetaAddress;
import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.poixson.commonapp.config.xConfig;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.utilsObject;
import com.poixson.commonjava.Utils.xHashable;
import com.poixson.commonjava.xLogger.xLog;


public abstract class HardwareConfig implements xHashable {
	private static final String LOG_NAME = "CONFIG";

	protected final xConfig config;
	public final String name;
	public final String title;
	public final boolean enabled;
	public final int id;

	public final Map<Integer, MetaAddress> dests;

	public final String key;



	// configs from set
	public static Map<String, HardwareConfig> getAll(final Set<Object> dataset) {
		if(utils.isEmpty(dataset))
			return null;
		final Map<String, HardwareConfig> configs = new HashMap<String, HardwareConfig>();
		for(final Object obj : dataset) {
			final Map<String, Object> datamap =
					utilsObject.castMap(
							String.class,
							Object.class,
							obj
					);
			final xConfig config = new xConfig(datamap);
			try {
				// type
				final String typeStr = config.getString(PluginDefines.CONFIG_HARDWARE_TYPE);
				if(utils.isEmpty(typeStr)) throw new RuntimeException("Hardware type is required!");
				final HardwareConfig hw;
				switch(typeStr.toLowerCase()) {
				case "usb":
				case "serial":
					hw = new HardwareConfigSerial(config);
					break;
				case "net":
				case "tcp":
					hw = new HardwareConfigNet(config);
					break;
				default:
					throw new RuntimeException("Invalid hardware type: "+typeStr);
				}
				configs.put(hw.getKey(), hw);
			} catch (Exception e) {
				log().trace(e);
			}
		}
		return configs;
	}
	public HardwareConfig(final xConfig config) {
		if(config == null) throw new NullPointerException("config argument is required!");
		this.config = config;
		this.name = config.getString(PluginDefines.CONFIG_HARDWARE_NAME);
		{
			final String title = config.getString(PluginDefines.CONFIG_HARDWARE_TITLE);
			this.title =
					utils.isEmpty(title)
					? this.name
					: title;
		}
		// default to enabled if key doesn't exist
		this.enabled =
				config.exists(PluginDefines.CONFIG_HARDWARE_ENABLED)
				? config.getBool(PluginDefines.CONFIG_HARDWARE_ENABLED, false)
				: true;
		this.id = config.getInt(PluginDefines.CONFIG_HARDWARE_ID, 0);
		this.key = this.genKey();
		// destination pins
		this.dests = HardwareConfigDestPins.getAll(
				config.getSet(Object.class, PluginDefines.CONFIG_HARDWARE_PINS)
		);
	}



	public Map<Integer, MetaAddress> getPins() {
		return this.dests;
	}



	@Override
	public String toString() {
		return this.key;
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



	// logger
	private static volatile xLog _log = null;
	public static xLog log() {
		if(_log == null)
			_log = xLog.getRoot(LOG_NAME);
		return _log;
	}



}
