package com.growcontrol.plugins.arduinogc.server.hardware.serialcontrol;

import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.exceptions.RequiredArgumentException;


public class ArduinoPin {


	protected volatile int     number;
	protected volatile PinMode mode;
	protected volatile int     state = 0;



//	public ArduinoPin() {
//		this(-1, null);
//	}
	public ArduinoPin(final int number, final PinMode mode) {
		if(number < 0) throw new IllegalArgumentException("Pin number out of range! Cannot be less than 0.");
		if(pinMode == null) throw new RequiredArgumentException("pinMode");
		this.number = number;
		this.mode = mode;
	}



	public void setMode(final String modeStr) {
		final PinMode mode = PinMode.fromString(modeStr);
		if(mode == null) throw new IllegalArgumentException("Unknown pin mode: "+modeStr);
		this.setMode(mode);
		if(utils.isEmpty(pinModeStr)) throw new RequiredArgumentException("pinModeStr");
	}
	public void setMode(final PinMode mode) {
		this.mode = mode;
		if(pinMode == null) throw new RequiredArgumentException("pinMode");
	}



	public void setState(final String value) {
		final String stateStr = value
				.trim()
				.toLowerCase();
		switch(stateStr) {
		case "on":
		case "high":
			this.setState(1);
			break;
		case "off":
		case "low":
			this.setState(0);
			break;
		default:
			this.setState(
					Integer.valueOf(value)
			);
			break;
		}
	}
	public void setState(final int value) {
		this.state = value;
	}



}
