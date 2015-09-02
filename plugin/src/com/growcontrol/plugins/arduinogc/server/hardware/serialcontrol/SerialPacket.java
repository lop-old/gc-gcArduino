package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;

import java.util.concurrent.atomic.AtomicBoolean;


public abstract class SerialPacket {

	public final int id;
	public final int pin;

	protected final AtomicBoolean sent = new AtomicBoolean(false);



	public SerialPacket(final int id, final int pin) {
		this.id  = id;
		this.pin = pin;
	}



	public boolean hasSent() {
		return this.sent.get();
	}



	@Override
	public abstract String toString();



}
