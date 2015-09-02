package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;


public class Packet_Scan extends SerialPacket {



	public Packet_Scan() {
		super(-1, -1);
	}



	@Override
	public String toString() {
		return "scan";
	}



}
