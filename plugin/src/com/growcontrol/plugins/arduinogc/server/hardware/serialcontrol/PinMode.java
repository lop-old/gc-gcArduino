package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;

import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.exceptions.RequiredArgumentException;


public enum PinMode {

//	DISABLED("DISABLED"),
//	IO,
//	PWM,
//	IN,
//	INH,
//	ANALOG

	OUTPUT("OUTP"),
	INPUT( "INPU"),
	INHIGH("INHI");


	public final String value;


	PinMode(final String value) {
		if(utils.isEmpty(value)) throw new RequiredArgumentException("value");
		this.value = value;
	}



	public static PinMode fromString(final String mode) {
		if(utils.isEmpty(mode))
			return null;
		final String modeStr = mode
				.trim()
				.toLowerCase()
				.replace(" ", "");
		switch(modeStr) {
		case "x":
			return null;
		case "io":
		case "out":
		case "output":
		case "write":
			return OUTPUT;
		case "in":
		case "input":
		case "read":
			return INPUT;
		case "inh":
		case "inputh":
		case "inputhigh":
			return INHIGH;
		default:
		}
		return null;
	}



}
