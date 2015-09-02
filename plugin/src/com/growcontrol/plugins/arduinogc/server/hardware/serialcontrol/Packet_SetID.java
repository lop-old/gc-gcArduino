package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;

import com.poixson.commonjava.Utils.utilsString;


public class Packet_SetID extends SerialPacket {

	public final int newId;



	public Packet_SetID(final int oldId, final int newId) {
		super(oldId, -1);
		if(oldId < 0 || oldId > 99)
			throw new IllegalArgumentException("oldId out of range! Valid id's are between 0 and 99.");
		if(newId < 0 || newId > 99)
			throw new IllegalArgumentException("newId out of range! Valid id's are between 0 and 99.");
		this.newId = newId;
	}



	@Override
	public String toString() {
		return (new StringBuilder())
				.append(utilsString.padFront(2, this.id))
				.append("id")
				.append(utilsString.padFront(4, this.newId))
				.toString();
	}



}
