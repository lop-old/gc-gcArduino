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
		final String host = this.config.getString(PluginDefines.CONFIG_HARDWARE_NET_HOST);
		final int    port = this.config.getInt(PluginDefines.CONFIG_HARDWARE_NET_PORT, PluginDefines.DEFAULT_NET_PORT);
		return (new StringBuilder())
				.append(host)
				.append(':').append(port)
				.append("#").append(this.id)
				.toString();
	}



}
