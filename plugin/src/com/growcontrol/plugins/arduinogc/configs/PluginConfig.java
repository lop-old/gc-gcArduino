package com.growcontrol.plugins.arduinogc.configs;

import java.util.Map;
import java.util.Set;

import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.poixson.commonapp.config.xConfig;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.xLogger.xLog;


public class PluginConfig extends xConfig {
	private static final String LOG_NAME = "CONFIG";

	private volatile Map<String, HardwareConfig> hardwareConfigs = null;
	private final Object configLock = new Object();



	public PluginConfig(final Map<String, Object> datamap) {
		super(datamap);
	}



	// plugin version
	public String getVersion() {
		final String value = this.getString(PluginDefines.CONFIG_VERSION);
		if(utils.isEmpty(value))
			return null;
		return value;
	}



	// hardware configs
	public Map<String, HardwareConfig> getHardwareConfigs() {
		if(this.hardwareConfigs == null) {
			synchronized(this.configLock){
				if(this.hardwareConfigs == null) {
					final Set<Object> dataset = this.getSet(
							Object.class,
							PluginDefines.CONFIG_HARDWARE
					);
					this.hardwareConfigs = HardwareConfig.get(dataset);
				}
			}
		}
		return this.hardwareConfigs;
	}





	// logger
	private volatile xLog _log = null;
	public xLog log() {
		if(this._log == null)
			this._log = xLog.getRoot(LOG_NAME);
		return this._log;
	}



}
