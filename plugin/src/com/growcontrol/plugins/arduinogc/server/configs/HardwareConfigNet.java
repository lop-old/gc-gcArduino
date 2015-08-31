package com.growcontrol.plugins.arduinogc.server.configs;

import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.poixson.commonapp.config.xConfig;


public class HardwareConfigNet extends HardwareConfig {

	public final String host;
	public final int    port;



	public HardwareConfigNet(final xConfig config) {
		super(config);
		this.host = config.getString(PluginDefines.CONFIG_HARDWARE_NET_HOST);
		this.port = config.getInt(   PluginDefines.CONFIG_HARDWARE_NET_PORT,
				PluginDefines.DEFAULT_NET_PORT);
	}



	@Override
	protected String genKey() {
		final StringBuilder str = new StringBuilder();
		str.append(this.host)
			.append(':').append(this.port)
			.append(":#").append(this.id);
		return str.toString();
	}



}
