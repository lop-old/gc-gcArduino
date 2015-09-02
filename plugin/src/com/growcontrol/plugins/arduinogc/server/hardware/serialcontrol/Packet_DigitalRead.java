package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;

import com.poixson.commonjava.Utils.utilsString;


public class Packet_DigitalRead extends SerialPacket {



	public Packet_DigitalRead(final int id, final int pin) {
		super(id, pin);
		if(id < 0 || id > 99)
			throw new IllegalArgumentException("id out of range! Valid id's are between 0 and 99.");
	}



	@Override
	public String toString() {
		return (new StringBuilder())
				.append(utilsString.padFront(2, this.id))
				.append("dr")
				.append(utilsString.padFront(4, this.pin))
				.toString();
	}



}
