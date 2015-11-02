package com.growcontrol.plugins.arduinogc.server.hardware.serial;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import jssc.SerialPortList;

import com.poixson.commonjava.Utils.CoolDown;
import com.poixson.commonjava.Utils.Keeper;
import com.poixson.commonjava.Utils.exceptions.RequiredArgumentException;
import com.poixson.commonjava.xLogger.xLog;


public final class SerialPortWatcher {

	private static volatile SerialPortWatcher instance = null;
	private static final Object instanceLock = new Object();

	private final Set<String> cached = new CopyOnWriteArraySet<String>();
	private final Set<SerialPortWatchListener> listeners =
			new CopyOnWriteArraySet<SerialPortWatchListener>();

	private final Object updateLock = new Object();
	private final CoolDown cool = CoolDown.get("2s");



	// get instance
	public static SerialPortWatcher get() {
		if(instance == null) {
			synchronized(instanceLock) {
				if(instance == null) {
					instance = new SerialPortWatcher();
					Keeper.add(instance);
				}
			}
		}
		return instance;
	}



	// get all ports
	public static Set<String> getAll() {
		return get()
			.getAllPorts();
	}
	public Set<String> getAllPorts() {
		// use cache
		if(this.cool.runAgain()) {
			synchronized(this.updateLock) {
				log().finest("Updating comm ports cache..");
				final Set<String> lastCached = new HashSet<String>(this.cached);
				this.cached.clear();
				final SerialPortWatchListener[] listeners =
						this.listeners.toArray(new SerialPortWatchListener[0]);
				final String[] ports = SerialPortList.getPortNames();
				for(final String portName : ports) {
					this.cached.add(portName);
					this.log().info("New comm port detected: "+portName);
					// trigger added
					if(!lastCached.contains(portName)) {
						for(final SerialPortWatchListener listener : listeners)
							listener.newPort(portName);
					}
				} // end for
				// trigger removed
				if(!lastCached.isEmpty()) {
					for(final String portName : lastCached) {
						if(!this.cached.contains(portName)) {
							this.log().warning("Comm port removed: "+portName);
							// trigger removed
							for(final SerialPortWatchListener listener : listeners)
								listener.removedPort(portName);
						}
					}
				}
			} // end synchronized
		} // end cool
		return Collections.unmodifiableSet(this.cached);
	}


//	public void addListener(final EventListenerSerial listener) {
//		this.addListener(listener, false);
//	}
	public void addListener(final SerialPortWatchListener listener,
			final boolean triggerExisting) {
		if(listener == null) throw new RequiredArgumentException("listener");
		if(triggerExisting) {
			final String[] current;
			synchronized(this.updateLock) {
				current = this.cached.toArray(new String[0]);
				this.listeners.add(listener);
			}
			for(final String portName : current) {
				listener.newPort(portName);
			}
		} else {
			this.listeners.add(listener);
		}
	}
	public void removeListener(final SerialPortWatchListener listener) {
		this.listeners.remove(listener);
	}
	public void clearListeners() {
		this.listeners.clear();
	}



	// logger
	public xLog log() {
		return xLog.getRoot();
	}



}
