package com.growcontrol.plugins.arduinogc.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.poixson.commonapp.config.xConfig;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.utilsObject;
import com.poixson.commonjava.Utils.xHashable;
import com.poixson.commonjava.xLogger.xLog;


public class HardwareConfig implements xHashable {
	private static final String LOG_NAME = "CONFIG";

	public final String key;

	public final boolean enabled;
	public final HardwareType type;



	// configs from set
	public static Map<String, HardwareConfig> get(final Set<Object> dataset) {
		if(utils.isEmpty(dataset))
			return null;
		final Map<String, HardwareConfig> configs = new HashMap<String, HardwareConfig>();
		for(final Object obj : dataset) {
			try {
				final Map<String, Object> datamap =
						utilsObject.castMap(
								String.class,
								Object.class,
								obj
						);
				final HardwareConfig cfg = get(datamap);
				configs.put(cfg.getKey(), cfg);
			} catch (Exception e) {
xLog.getRoot(LOG_NAME).trace(e);
			}
		}
		return configs;
	}
	// config from map
	public static HardwareConfig get(final Map<String, Object> datamap) {
		if(utils.isEmpty(datamap))
			return null;
		final xConfig config = new xConfig(datamap);
		// default to enabled if key doesn't exist
		final boolean enabled =
				config.exists(PluginDefines.CONFIG_HARDWARE_ENABLED)
				? config.getBool(PluginDefines.CONFIG_HARDWARE_ENABLED, false)
				: true;
		final HardwareType type = HardwareType.get(datamap);
		return new HardwareConfig(
				enabled,
				type
		);
	}



	// new config instance
	public HardwareConfig(final boolean enabled, final HardwareType type) {
		this.enabled = enabled;
		this.type    = type;
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
	private String genKey() {
		final StringBuilder str = new StringBuilder();

		return str.toString();
	}
	@Override
	public boolean matches(final xHashable hashable) {
		if(hashable == null || !(hashable instanceof HardwareConfig) )
			return false;
		final HardwareConfig config = (HardwareConfig) hashable;
		if(this.enabled != config.enabled)
			return false;
		return this.getKey().equalsIgnoreCase(config.getKey());
	}



	// logger
	private volatile xLog _log = null;
	public xLog log() {
		if(this._log == null)
			this._log = xLog.getRoot(LOG_NAME);
		return this._log;
	}



}
