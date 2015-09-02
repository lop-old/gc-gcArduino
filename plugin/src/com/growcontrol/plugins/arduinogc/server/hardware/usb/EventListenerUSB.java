package com.growcontrol.plugins.arduinogc.server.hardware.usb;

import gnu.io.CommPortIdentifier;


public interface EventListenerUSB {


	public void added(  final String name, final CommPortIdentifier ident);
	public void removed(final String name);


}
