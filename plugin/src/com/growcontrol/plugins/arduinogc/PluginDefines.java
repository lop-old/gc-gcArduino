package com.growcontrol.plugins.arduinogc;

import com.poixson.commonjava.Utils.utilsDirFile;


public final class PluginDefines {
	private PluginDefines() {}


	// defaults
	public static final int DEFAULT_SERIAL_BAUD = 9600;
	public static final int DEFAULT_NET_PORT    = 23;


	// plugin config
	public static final String CONFIG_PATH = utilsDirFile.mergePaths("plugins", "ArduinoGC");
	public static final String CONFIG_FILE = "ArduinoGC.yml";
	// config keys
	public static final String CONFIG_VERSION = "Version";
	// arduino hardware
	public static final String CONFIG_HARDWARE = "Hardware";
	public static final String CONFIG_HARDWARE_TYPE     = "Type";
	public static final String CONFIG_HARDWARE_NAME     = "Name";
	public static final String CONFIG_HARDWARE_ENABLED  = "Enabled";
	public static final String CONFIG_HARDWARE_ID       = "id";
	public static final String CONFIG_HARDWARE_USB_PORT = "Port";
	public static final String CONFIG_HARDWARE_USB_BAUD = "Baud";
	public static final String CONFIG_HARDWARE_NET_HOST = "Host";
	public static final String CONFIG_HARDWARE_NET_PORT = "Port";


}
