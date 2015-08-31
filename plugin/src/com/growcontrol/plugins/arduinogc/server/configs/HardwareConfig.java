package com.growcontrol.plugins.arduinogc.server.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.poixson.commonapp.config.xConfig;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.utilsObject;
import com.poixson.commonjava.Utils.xHashable;
import com.poixson.commonjava.xLogger.xLog;


public abstract class HardwareConfig implements xHashable {
	private static final String LOG_NAME = "CONFIG";

	public final String name;
	public final boolean enabled;
	public final int id;

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
				final HardwareConfig hardware;
				switch(typeStr.toLowerCase()) {
				case "net":
				case "tcp":
					hardware = new HardwareConfigUSB(config);
					break;
				case "usb":
				case "serial":
					hardware = new HardwareConfigNet(config);
					break;
				default:
					throw new RuntimeException("Unknown hardware type: "+typeStr);
				}
				configs.put(hardware.getKey(), hardware);
			} catch (Exception e) {
				log().trace(e);
			}
		}
		return configs;
	}
	public HardwareConfig(final xConfig config) {
		if(config == null) throw new NullPointerException("config argument is required!");
		this.name = config.getString(PluginDefines.CONFIG_HARDWARE_NAME);
		// default to enabled if key doesn't exist
		this.enabled =
				config.exists(PluginDefines.CONFIG_HARDWARE_ENABLED)
				? config.getBool(PluginDefines.CONFIG_HARDWARE_ENABLED, false)
				: true;
		this.id = config.getInt(PluginDefines.CONFIG_HARDWARE_ID, 0);
		this.key = this.genKey();
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
		final HardwareConfig config = (HardwareConfig) hashable;
//		if(this.enabled != config.enabled)
//			return false;
		return this.getKey().equalsIgnoreCase(config.getKey());
	}



	// logger
	private static volatile xLog _log = null;
	public static xLog log() {
		if(_log == null)
			_log = xLog.getRoot(LOG_NAME);
		return _log;
	}



}
