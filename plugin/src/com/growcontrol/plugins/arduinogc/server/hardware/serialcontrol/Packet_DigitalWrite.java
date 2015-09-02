package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;

import com.poixson.commonjava.Utils.utilsString;


public class Packet_DigitalWrite extends SerialPacket {

	public final boolean value;



	public Packet_DigitalWrite(final int id, final int pin, final boolean value) {
		super(id, pin);
		if(id < 0 || id > 99)
			throw new IllegalArgumentException("id out of range! Valid id's are between 0 and 99.");
		this.value = value;
	}



	@Override
	public String toString() {
		return (new StringBuilder())
				.append(utilsString.padFront(2, this.id))
				.append("dw")
				.append(utilsString.padFront(4, this.pin))
				.append(this.value ? "HIGH" : "LOW_")
				.toString();
	}



}
