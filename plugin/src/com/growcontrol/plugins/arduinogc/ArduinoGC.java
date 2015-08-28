package com.growcontrol.plugins.arduinogc;

import com.growcontrol.plugins.arduinogc.commands.Commands;
import com.growcontrol.server.plugins.gcServerPlugin;


public class ArduinoGC extends gcServerPlugin {



	@Override
	protected void onEnable() {
		this.register(new Commands());
	}



	@Override
	protected void onDisable() {
		this.unregister(Commands.class);
	}



}
