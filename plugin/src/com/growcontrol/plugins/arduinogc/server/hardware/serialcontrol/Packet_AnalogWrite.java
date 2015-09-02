package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;

import com.poixson.commonjava.Utils.utilsString;


public class Packet_AnalogWrite extends SerialPacket {

	public final int value;



	public Packet_AnalogWrite(final int id, final int pin, final int value) {
		super(id, pin);
		if(id < 0 || id > 99)
			throw new IllegalArgumentException("id out of range! Valid id's are between 0 and 99.");
		this.value = value;
	}



	@Override
	public String toString() {
		return (new StringBuilder())
				.append(utilsString.padFront(2, this.id))
				.append("aw")
				.append(utilsString.padFront(4, this.pin))
				.append(utilsString.padFront(4, this.value))
				.toString();
	}



}
