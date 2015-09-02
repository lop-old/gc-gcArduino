package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;

import com.poixson.commonjava.Utils.utilsString;


public class Packet_PinMode extends SerialPacket {

	public final String mode;



	public Packet_PinMode(final int id, final int pin, final String mode) {
		super(id, pin);
		if(id < 0 || id > 99)
			throw new IllegalArgumentException("id out of range! Valid id's are between 0 and 99.");
//todo: don't allow:  ; \r \n
		this.mode = mode;
	}



	@Override
	public String toString() {
		return (new StringBuilder())
				.append(utilsString.padFront(2, this.id))
				.append("pm")
				.append(this.mode)
				.toString();
	}



}
