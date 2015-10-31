package com.growcontrol.plugins.arduinogc.server.configs;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.growcontrol.common.gcCommonDefines;
import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.poixson.commonapp.config.xConfig;
import com.poixson.commonapp.config.xConfigException;
import com.poixson.commonjava.Utils.utilsObject;


public class PluginConfig extends xConfig {

	public final String version;

	private final Map<String, HardwareConfig> hardwareConfigs;



	public PluginConfig(final Map<String, Object> datamap)
			throws xConfigException {
		super(datamap);
		this.version = this.getString(gcCommonDefines.CONFIG_VERSION);
		this.hardwareConfigs = this.loadHardwareConfigs();
	}
	// hardware configs
	private Map<String, HardwareConfig> loadHardwareConfigs()
			throws xConfigException {
		final Set<Object> dataset = this.getSet(
				Object.class,
				PluginDefines.CONFIG_HARDWARE
		);
		final Map<String, HardwareConfig> configs = new LinkedHashMap<String, HardwareConfig>();
		for(final Object obj : dataset) {
			final Map<String, Object> datamap = utilsObject.castMap(
					String.class,
					Object.class,
					obj
			);
			final String typeStr = (String) datamap.get(PluginDefines.CONFIG_HARDWARE_TYPE);
			final HardwareConfig cfg;
			switch(typeStr.toUpperCase()) {
			case "USB":
			case "SERIAL": {
				cfg = new HardwareConfigSerial(datamap);
				break;
			}
			case "NET":
			case "TCP":
			case "ETHERNET":
			case "WIFI":
			case "WEB": {
				cfg = new HardwareConfigNet(datamap);
				break;
			}
			default: {
				throw new xConfigException("Unknown hardware type: "+typeStr);
			}
			}
			configs.put(cfg.getKey(), cfg);
		}
		return Collections.unmodifiableMap(configs);
	}



	// plugin version
	public String getVersion() {
		return this.version;
	}
	public Map<String, HardwareConfig> getHardwareConfigs() {
		return this.hardwareConfigs;
	}



}
