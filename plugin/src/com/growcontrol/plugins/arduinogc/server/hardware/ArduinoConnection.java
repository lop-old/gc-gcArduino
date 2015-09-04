package com.growcontrol.plugins.arduinogc.server.hardware;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.growcontrol.plugins.arduinogc.PluginDefines;
import com.growcontrol.plugins.arduinogc.server.ArduinoGC;
import com.growcontrol.plugins.arduinogc.server.configs.HardwareConfig;
import com.growcontrol.plugins.arduinogc.server.configs.HardwareConfigNet;
import com.growcontrol.plugins.arduinogc.server.configs.HardwareConfigSerial;
import com.growcontrol.plugins.arduinogc.server.hardware.net.ConnectionNet;
import com.growcontrol.plugins.arduinogc.server.hardware.serial.ConnectionSerial;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.xCloseable;
import com.poixson.commonjava.Utils.xHashable;
import com.poixson.commonjava.xLogger.xLog;


public abstract class ArduinoConnection implements xCloseable, xHashable {
	private static final String LOG_NAME = ArduinoGC.LOG_NAME;

	private static final Map<String, ArduinoConnection> connections =
			new ConcurrentHashMap<String, ArduinoConnection>();

	// ready device id's
	protected final Set<Integer> ready = new CopyOnWriteArraySet<Integer>();

//TODO: not used yet
//	protected static final xTime HEARTBEAT = xTime.get("30s");
//	protected static final xTime THREAD_WAIT = xTime.get(100L);

//TODO: not used yet
//	protected final Map<Integer, ArduinoPin> pinsOut = new HashMap<Integer, ArduinoPin>();
//	protected final msgQueue queue = new msgQueue();
//	private final Queue<Protocol> queue = new ConcurrentLinkedQueue<Protocol>();



	public static ArduinoConnection get(final HardwareConfig config) {
		final String key = config.key;
		// existing connection
		{
			final ArduinoConnection connect = connections.get(key);
			if(connect != null)
				return connect;
		}
		// new connection
		synchronized(connections) {
			if(connections.containsKey(key))
				return connections.get(key);
			final ArduinoConnection connect = ArduinoConnection.load(config);
			if(connect == null)
				return null;
			connections.put(key, connect);
			return connect;
		}
	}
	private static ArduinoConnection load(final HardwareConfig config) {
		if(ArduinoGC.isStopping())
			return null;
		// serial config
		if(config instanceof HardwareConfigSerial) {
			final HardwareConfigSerial cfg = (HardwareConfigSerial) config;
			final String portName = cfg.port;
			final int    baud     = cfg.baud;
			return new ConnectionSerial(
					portName,
					baud
			);
		}
		// net config
		if(config instanceof HardwareConfigNet) {
			final HardwareConfigNet cfg = (HardwareConfigNet) config;
			final String host = cfg.host;
			final int    port = cfg.port;
			return new ConnectionNet(
					host,
					port
			);
		}
		// unknown config
		throw new RuntimeException("Unknown hardware config type: "+
				config.getClass().getName());
	}
	public static Set<ArduinoConnection> loadAll(
			final Map<String, HardwareConfig> configs) {
		if(utils.isEmpty(configs)) {
			log().fine("No arduino devices found in "+PluginDefines.CONFIG_FILE);
			return null;
		}
		// load from configs
		log().info("Connecting to hardware..");
		final Set<ArduinoConnection> connections = new HashSet<ArduinoConnection>();
		int count  = 0;
		int failed = 0;
		for(final HardwareConfig cfg : configs.values()) {
			ArduinoConnection connect = null;
			try {
				connect = ArduinoConnection.get(cfg);
			} catch (Exception e) {
				connect = null;
				log().trace(e);
			}
			if(connect == null) {
				failed++;
				log().warning("Failed to open comm port: "+cfg.getKey());
			} else {
				count++;
				log().info("Successfully connected to: "+cfg.getKey());
			}
			connections.add(connect);
		}
		if(count > 0)
			log().info("Successfully connected to [ "+Integer.toString(count)+" ] devices!");
		if(failed > 0)
			log().warning("Failed connecting to [ "+Integer.toString(failed)+" ] devices!");
		return connections;
	}



	public static void Close(final ConnectionSerial connect) {
		utils.safeClose(connect);
		synchronized(connections) {
			connections.remove(connect.getKey());
		}
	}
	public static void Close(final String portName) {
		if(!connections.containsKey(portName))
			return;
		synchronized(connections) {
			final ArduinoConnection connect = connections.get(portName);
			if(connect == null)
				return;
			utils.safeClose(connect);
			connections.remove(portName);
		}
	}
	public static void CloseAll() {
		if(connections.isEmpty())
			return;
		synchronized(connections) {
			final Iterator<ArduinoConnection> it = connections.values().iterator();
			while(it.hasNext()) {
				utils.safeClose(it.next());
				it.remove();
			}
			connections.clear();
		}
	}



	public boolean isReady(final int id) {
		return this.ready.contains(new Integer(id));
	}



	// logger
	private static volatile xLog _log = null;
	public static xLog log() {
		if(_log == null)
			_log = xLog.getRoot(LOG_NAME);
		return _log;
	}



}
