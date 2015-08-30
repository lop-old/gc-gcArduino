package com.growcontrol.plugins.arduinogc.server;

import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.growcontrol.plugins.arduinogc.server.commands.Commands;
import com.growcontrol.plugins.arduinogc.server.configs.PluginConfig;
import com.growcontrol.server.gcServerDefines;
import com.growcontrol.server.plugins.gcServerPlugin;
import com.poixson.commonapp.config.xConfigLoader;
import com.poixson.commonjava.xLogger.xLog;


public class ArduinoGC extends gcServerPlugin {
	public static final String LOG_NAME = "ArduinoGC";

	private volatile PluginConfig config = null;



	@Override
	protected void onEnable() {
		// load config
		this.config = (PluginConfig) xConfigLoader.Load(
				PluginDefines.CONFIG_PATH,
				PluginDefines.CONFIG_FILE,
				PluginConfig.class,
				ArduinoGC.class
		);
		if(this.config == null) {
			this.fail("Failed to load "+PluginDefines.CONFIG_FILE);
			return;
		}
		if(this.config.isFromResource())
			xLog.getRoot(LOG_NAME).warning("Created default "+gcServerDefines.CONFIG_FILE);
		// register listeners
		this.register(new Commands());
	}



	@Override
	protected void onDisable() {
		this.unregister(Commands.class);
	}



}
