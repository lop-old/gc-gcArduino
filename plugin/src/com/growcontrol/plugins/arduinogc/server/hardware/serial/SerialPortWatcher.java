package com.growcontrol.plugins.arduinogc.server.hardware.serial;

import gnu.io.CommPortIdentifier;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.poixson.commonjava.Utils.CoolDown;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.xLogger.xLog;


public final class SerialPortWatcher {

	private static volatile SerialPortWatcher instance = null;
	private static final Object instanceLock = new Object();

	private final Map<String, CommPortIdentifier> cached =
			new HashMap<String, CommPortIdentifier>();
	private final Set<EventListenerSerial> listeners =
			new CopyOnWriteArraySet<EventListenerSerial>();

	private final Object updateLock = new Object();
	private final CoolDown cool = CoolDown.get("2s");



	// get instance
	public static SerialPortWatcher get() {
		if(instance == null) {
			synchronized(instanceLock) {
				if(instance == null)
					instance = new SerialPortWatcher();
			}
		}
		return instance;
	}



	// get by port name
	public static CommPortIdentifier get(final String portName) {
		return get()
			.getByName(portName);
	}
	public CommPortIdentifier getByName(final String portName) {
		if(utils.isEmpty(portName)) throw new NullPointerException("portName argument is required!");
		// get available comm ports
		final Map<String, CommPortIdentifier> map = this.getAllPorts();
		if(utils.isEmpty(map))
			return null;
		return map.get(portName.toLowerCase());
	}



	// get all ports
	public static Map<String, CommPortIdentifier> getAll() {
		return get()
			.getAllPorts();
	}
	public Map<String, CommPortIdentifier> getAllPorts() {
		synchronized(this.updateLock) {
			if(!this.cool.runAgain())
				return Collections.unmodifiableMap(this.cached);
			final Set<String> lastSet = this.cached.keySet();
			this.cached.clear();
			final EventListenerSerial[] listeners = this.listeners.toArray(new EventListenerSerial[0]);
			final Enumeration<?> idents = CommPortIdentifier.getPortIdentifiers();
			while(idents.hasMoreElements()) {
				final CommPortIdentifier port = (CommPortIdentifier) idents.nextElement();
				final String portName = port.getName();
				final int    portType = port.getPortType();
				switch(portType) {
				case CommPortIdentifier.PORT_SERIAL:
					this.cached.put(portName, port);
					break;
				case CommPortIdentifier.PORT_RS485:
				case CommPortIdentifier.PORT_PARALLEL:
				case CommPortIdentifier.PORT_I2C:
				case CommPortIdentifier.PORT_RAW:
					break;
				default:
					throw new RuntimeException("Unknown port type: "+
							Integer.toString(portType));
				}
				// trigger added
				if(!this.cached.containsKey(portName)) {
					this.log().info("New comm port detected: "+portName);
					for(final EventListenerSerial listener : listeners) {
						listener.added(portName, port);
					}
				}
			} // end while
			// trigger removed
			if(lastSet.isEmpty()) {
				for(final String portName : lastSet) {
					this.log().warning("Comm port removed: "+portName);
					for(final EventListenerSerial listener : listeners) {
						listener.removed(portName);
					}
				}
			}
		} // end synchronized
		return Collections.unmodifiableMap(this.cached);
	}



	public FindComms() {
	}



	public void addListener(final EventListenerSerial listener) {
		final CommPortIdentifier[] current;
		synchronized(this.updateLock) {
			current = (CommPortIdentifier[]) this.listeners.toArray(new EventListenerSerial[0]);
			this.listeners.add(listener);
		}
		for(final CommPortIdentifier ident : current) {
			listener.added(ident.getName(), ident);
		}
	}
	public void removeListener(final EventListenerSerial listener) {
		this.listeners.remove(listener);
	}
	public void clearListener() {
		this.listeners.clear();
	}



	// logger
	public xLog log() {
		return xLog.getRoot();
	}



}
