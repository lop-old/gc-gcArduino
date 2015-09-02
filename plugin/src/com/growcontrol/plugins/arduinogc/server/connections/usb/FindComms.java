package com.growcontrol.plugins.arduinogc.server.connections;

import gnu.io.CommPortIdentifier;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public final class FindComms {
	private FindComms() {}



	public static Map<String, CommPortIdentifier> search() {
		final Map<String, CommPortIdentifier> map =
				new HashMap<String, CommPortIdentifier>();
		final Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
		while(ports.hasMoreElements()) {
			final CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
			switch (port.getPortType()) {
			case CommPortIdentifier.PORT_SERIAL:
				map.put(port.getName(), port);
				break;
			case CommPortIdentifier.PORT_RS485:
			case CommPortIdentifier.PORT_PARALLEL:
			case CommPortIdentifier.PORT_I2C:
			case CommPortIdentifier.PORT_RAW:
				break;
			default:
				throw new RuntimeException("Unknown port type: "+Integer.toString(port.getPortType()));
			}
		}
		return Collections.unmodifiableMap(map);
	}



}
