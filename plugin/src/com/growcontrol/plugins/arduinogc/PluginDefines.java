package com.growcontrol.plugins.arduinogc;

import com.poixson.commonjava.Utils.utilsDirFile;


public final class PluginDefines {
	private PluginDefines() {}


	// plugin config
	public static final String CONFIG_PATH = utilsDirFile.mergePaths("plugins", "ArduinoGC");
	public static final String CONFIG_FILE = "ArduinoGC.yml";
	// config keys
	public static final String CONFIG_VERSION = "Version";
	// arduino hardware
	public static final String CONFIG_HARDWARE = "Hardware";
	public static final String CONFIG_HARDWARE_ENABLED = "Enabled";
	public static final String CONFIG_HARDWARE_TYPE     = "Type";


}
