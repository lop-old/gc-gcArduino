package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;


public class Packet_SetAllPins extends SerialPacket {

	public final String value;



	public Packet_SetAllPins(final int id, final int value) {
		this(id, Integer.toString(value));
	}
	public Packet_SetAllPins(final int id, final String value) {
		super(id, -1);
		if(id < 0 || id > 99)
			throw new IllegalArgumentException("id out of range! Valid id's are between 0 and 99.");
		this.value = value;
	}



//TODO: this should create multiple Packet_Write instances
	@Override
	public String toString() {
//		return (new StringBuilder())
//				.append(utilsString.padFront(2, this.id,    '0'))
//				.append("al")
//				.append(utilsString.padFront(4, pin,        '0'))
//				.append(utilsString.padFront(4, this.value, '0'))
//				.append()
//				.append()
//				.toString();
return null;
	}



}
