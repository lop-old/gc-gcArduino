package com.growcontrol.plugins.arduinogc.server.hardware.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import com.growcontrol.plugins.arduinogc.server.hardware.ArduinoConnection;
import com.poixson.commonjava.Utils.utils;


//	private static final int TIMEOUT = 2000;
//	private static final int ASCII_SPACE   = ' ';
//	private static final int ASCII_DASH    = '-';
//	private static final int ASCII_NEWLINE = '\n';
//
//	private Enumeration ports = null;
//
//	private final Map<String, CommPortIdentifier> portMap =
//			new HashMap<String, CommPortIdentifier>();
//
//	private final CommPortIdentifier selectedPortIdent = null;
//	private SerialPort serialPort = null;
//
//	private InputStream  in  = null;
//	private OutputStream out = null;
//
//	private boolean connected = false;
public class ConnectionSerial extends ArduinoConnection implements SerialPortEventListener {

	protected volatile String             portName  = null;
	protected volatile CommPortIdentifier portIdent = null;
	protected final int baud;



	public ConnectionSerial(final String portName, final int baud) {
		if(utils.isEmpty(portName)) throw new NullPointerException("port argument is required!");
		this.portIdent = FindComms.get(portName);
		if(this.portIdent == null)
			this.portName = portName;
		else
			this.portName = this.portIdent.getName();
		this.baud = baud;
	}
	public ConnectionSerial(final CommPortIdentifier portIdent, final int baud) {
		if(portIdent == null) throw new NullPointerException("portIdent argument is required!");
		this.portIdent = portIdent;
		this.portName  = portIdent.getName();
		this.baud = baud;
	}



	@Override
	public void Process() {
	}
	@Override
	public void Send() {
	}



	@Override
	public void serialEvent(final SerialPortEvent event) {
	}



}
