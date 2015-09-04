package com.growcontrol.plugins.arduinogc.server.hardware.serial;

import gnu.io.CommPortIdentifier;


public interface SerialPortWatchListener {


	public void added(  final String name, final CommPortIdentifier ident);
	public void removed(final String name);


}
