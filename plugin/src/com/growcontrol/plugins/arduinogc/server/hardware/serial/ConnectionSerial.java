package com.growcontrol.plugins.arduinogc.server.hardware.serial;

import java.nio.charset.Charset;

import com.growcontrol.common.meta.MetaAddress;
import com.growcontrol.common.meta.MetaEvent;
import com.growcontrol.common.meta.MetaType;
import com.growcontrol.common.meta.metaTypes.MetaIO;
import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.growcontrol.plugins.arduinogc.server.hardware.ArduinoConnection;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.utilsString;
import com.poixson.commonjava.Utils.xHashable;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;


//	private static final int TIMEOUT = 2000;
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
//TODO: will need to expect exceptions at every call, in case a port is disconnected
public class ConnectionSerial extends ArduinoConnection implements SerialPortEventListener {

	// port info
	private final String portName;
	private final int    baud;

	// comm port
	private final SerialPort serial;

	private volatile String buffer = "";
	private volatile boolean stopping = false;



	public ConnectionSerial(final String portName, final int baud) {
		super();
		if(utils.isEmpty(portName)) throw new NullPointerException("portName argument is required!");
		if(!isValidBaud(baud))      throw new IllegalArgumentException("Invalid baud rate: "+Integer.toString(baud));
		this.portName = portName;
		this.baud     = baud;
		// open comm port
		this.serial = new SerialPort(this.portName);
		try {
			this.serial.openPort();
			this.serial.setParams(
					this.baud,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE
			);
			this.serial.addEventListener(this);
			this.serial.setEventsMask(SerialPort.MASK_RXCHAR);
			this.send("scan");
		} catch (SerialPortException e) {
			log().severe("Failed to open comm port: "+this.portName);
			log().trace(e);
			throw new RuntimeException("Failed to find comm port: "+this.portName);
		}
	}



	public void send(final String msg) throws SerialPortException {
System.out.println("SENDING: "+msg);
		final byte[] bytes = (new StringBuilder())
				.append(msg)
				.append(PluginDefines.SERIAL_LINE_ENDING)
				.toString().getBytes(Charset.forName("US-ASCII"));
		this.serial.writeBytes(bytes);
	}



	@Override
	public void close() {
		this.stopping = true;
		try {
			this.serial.closePort();
		} catch (SerialPortException e) {
			log().trace(e);
		}
	}
	@Override
	public boolean isClosed() {
		return this.stopping;
	}



	@Override
	public String getName() {
		return this.toString();
	}
	@Override
	public String toString() {
		return this.portName;
	}
	@Override
	public String getKey() {
		return this.portName;
	}
	@Override
	public boolean matches(final xHashable hashable) {
		if(hashable == null || !(hashable instanceof ConnectionSerial))
			return false;
		return this.getKey().equalsIgnoreCase(hashable.getKey());
	}



	private void bufferAppend(final byte[] bytes) {
		synchronized(this.buffer) {
			this.buffer += (new String(bytes));
			final String line = this.bufferGetLine();
			if(line != null) {
				this.Process(line);
			}
		}
	}
	private String bufferGetLine() {
		if(this.buffer.isEmpty())
			return null;
		final String line;
		synchronized(this.buffer) {
			int pos = Integer.MAX_VALUE;
			for(final char c : PluginDefines.SERIAL_LINE_ENDINGS) {
				final int p = this.buffer.indexOf(c);
				if(p == -1)
					continue;
				if(pos > p) pos = p;
				if(pos == 0)
					break;
			}
			if(pos == 0) {
				this.buffer = this.buffer.substring(1);
				return null;
			}
			if(pos == Integer.MAX_VALUE)
				return null;
			line        = this.buffer.substring(0, pos);
			this.buffer = this.buffer.substring(pos + 1);
		}
		return line;
	}



	@Override
	public void serialEvent(final SerialPortEvent event) {
		// data is available
		if(event.isRXCHAR()) {
			final byte[] buf;
			try {
				buf = this.serial.readBytes(event.getEventValue());
				if(utils.isEmpty(buf))
					return;
				this.bufferAppend(buf);
			} catch (SerialPortException e) {
				log().trace(e);
			}
//		} else
//		// CTS line changed state
//		if(event.isCTS()) {
//			log().info("CTS: "+(event.getEventValue() == 1 ? "ON" : "off"));
//		} else
//		// DSR line changed state
//		if(event.isDSR()) {
//			log().info("DSR: "+(event.getEventValue() == 1 ? "ON" : "off"));
		} else {
			log().warning("Unknown serial event type! "+event.getEventType());
		}
	}



	@Override
	public void onMetaEvent(final MetaEvent event) {
		final MetaAddress destAddr = event.destination;
		if(destAddr == null) throw new NullPointerException("Unexpected null destination address!");
		if(!this.dests.containsValue(destAddr))
			return;
		final MetaType value = event.value;
		if(value == null) throw new NullPointerException("Unexpected null meta value!");
		// handle the event




		if(value instanceof MetaIO) {
			try {
				final MetaIO val = (MetaIO) value;
				final int pin = this.DestAddressToPin(destAddr.getKey());
				this.send(
						(new StringBuilder())
						.append("00dw")
						.append(utilsString.padFront(4, Integer.toString(pin), '0'))
						.append(val.getValue() ? "HIGH" : "LOW")
						.toString()
				);
			} catch (SerialPortException e) {
				log().trace(e);
			}
		}


		event.setHandled();
		System.out.println("A"+(event.isHandled() ? "Handled!" : "Not handled.."));
		event.setHandled(true);
		System.out.println("B"+(event.isHandled() ? "Handled!" : "Not handled.."));

		System.out.println("GOT EVENT: "+event.destination.hash);



	}



	public static boolean isValidBaud(final int baud) {
		switch(baud) {
		case SerialPort.BAUDRATE_110:
		case SerialPort.BAUDRATE_300:
		case SerialPort.BAUDRATE_600:
		case SerialPort.BAUDRATE_1200:
		case SerialPort.BAUDRATE_4800:
		case SerialPort.BAUDRATE_9600:
		case SerialPort.BAUDRATE_14400:
		case SerialPort.BAUDRATE_19200:
		case SerialPort.BAUDRATE_38400:
		case SerialPort.BAUDRATE_57600:
		case SerialPort.BAUDRATE_115200:
		case SerialPort.BAUDRATE_128000:
		case SerialPort.BAUDRATE_256000:
			return true;
		default:
			break;
		}
		return false;
	}



}
