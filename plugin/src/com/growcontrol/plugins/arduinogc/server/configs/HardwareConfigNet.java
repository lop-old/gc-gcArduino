package com.growcontrol.plugins.arduinogc.server.configs;

import java.util.Map;

import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.poixson.commonapp.config.xConfigException;


public class HardwareConfigNet extends HardwareConfig {

	public final String host;
	public final int    port;



	public HardwareConfigNet(final Map<String, Object> datamap)
			throws xConfigException {
		super(datamap);
		this.host = this.getString(PluginDefines.CONFIG_HARDWARE_NET_HOST);
		this.port = this.getInt(
				PluginDefines.CONFIG_HARDWARE_NET_PORT,
				PluginDefines.DEFAULT_NET_PORT
		);
	}



	public String getHost() {
		return this.host;
	}
	public int getPort() {
		return this.port;
	}



	@Override
	protected String genKey() {
		return (new StringBuilder())
				.append(this.getHost())
				.append(':').append(this.getPort())
				.append("#").append(this.getId())
				.toString();
	}



}
