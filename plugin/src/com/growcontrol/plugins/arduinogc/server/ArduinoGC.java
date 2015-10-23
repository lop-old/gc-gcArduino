package com.growcontrol.plugins.arduinogc.server;

import java.util.Map;
import java.util.Set;

import com.growcontrol.api.serverapi.plugins.apiServerPlugin;
import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.growcontrol.plugins.arduinogc.server.commands.Commands;
import com.growcontrol.plugins.arduinogc.server.configs.HardwareConfig;
import com.growcontrol.plugins.arduinogc.server.configs.PluginConfig;
import com.growcontrol.plugins.arduinogc.server.hardware.ArduinoConnection;
import com.poixson.commonapp.config.xConfigLoader;
import com.poixson.commonjava.Utils.Keeper;
import com.poixson.commonjava.xLogger.xLog;


public class ArduinoGC extends apiServerPlugin {
	public static final String LOG_NAME = "ArduinoGC";

	private volatile PluginConfig config = null;

	private static volatile boolean stopping = false;



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
			xLog.getRoot(LOG_NAME).warning("Created default "+PluginDefines.CONFIG_FILE);
		// start hardware
		{
			final Map<String, HardwareConfig> hardwareConfigs =
					this.config.getHardwareConfigs();
			final Set<ArduinoConnection> connections =
					ArduinoConnection.loadAll(
							hardwareConfigs
					);
			Keeper.add(connections);
		}
		// register listeners
		this.register(new Commands());
	}



	@Override
	protected void onDisable() {
		stopping = true;
		this.unregister(Commands.class);
		// close hardware connections
		ArduinoConnection.CloseAll();
	}
	public static boolean isStopping() {
		return stopping;
	}



	// logger
	private static volatile xLog _log = null;
	public static xLog getLogger() {
		if(_log == null)
			_log = xLog.getRoot(LOG_NAME);
		return _log;
	}
	public static xLog getLogger(final String name) {
		return getLogger().get(name);
	}



}
