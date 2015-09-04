package com.growcontrol.plugins.arduinogc.server.hardware.serial;


public interface SerialPortWatchListener {


	public void newPort(    final String portName);
	public void removedPort(final String portName);


}
