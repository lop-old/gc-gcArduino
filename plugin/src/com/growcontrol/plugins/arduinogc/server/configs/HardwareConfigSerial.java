package com.growcontrol.plugins.arduinogc.server.configs;

import java.util.Map;

import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.poixson.commonapp.config.xConfigException;


public class HardwareConfigSerial extends HardwareConfig {

	private final String port;
	private final int    baud;



	public HardwareConfigSerial(final Map<String, Object> datamap)
			throws xConfigException {
		super(datamap);
		this.port = this.getString(PluginDefines.CONFIG_HARDWARE_USB_PORT);
		this.baud = this.getInt(
				PluginDefines.CONFIG_HARDWARE_USB_BAUD,
				PluginDefines.DEFAULT_SERIAL_BAUD
		);
		this.key = this.genKey();
	}



	public String getPort() {
		return this.port;
	}
	public int getBaud() {
		return this.baud;
	}



	@Override
	protected String genKey() {
		return (new StringBuilder())
				.append(this.getPort())
				.append("#")
				.append(this.getId())
				.toString();
	}



}
