package com.growcontrol.plugins.arduinogc.server.hardware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.growcontrol.plugins.arduinogc.server.configs.HardwareConfig;
import com.poixson.commonjava.Utils.xHashable;
import com.poixson.commonjava.Utils.threads.xThreadPool;
import com.poixson.commonjava.xLogger.xLog;


//	{
//		final HardwareManager manager = instances.get(key);
//		if(manager != null)
//			return manager;
//	}
//	synchronized(instances) {
//		if(instances.containsKey(key))
//			return instances.get(key);
//		final HardwareManager manager = new HardwareManager();
//		instances.put(manager.getKey(), manager);
//		return manager;
//	}
public class HardwareManager implements xHashable {
	private static final String LOG_NAME  = "ArduinoGC";
	private static final String POOL_NAME = "ArduinoGC";

	private static final Map<String, HardwareManager> instances =
			new ConcurrentHashMap<String, HardwareManager>();

	private final xThreadPool pool;
//	private final Queue<Protocol> queue = new ConcurrentLinkedQueue<Protocol>();

	public final String key;



	// load new
	public static HardwareManager load(final HardwareConfig config) {
		final String key = config.getKey();
		// existing
		{
			final HardwareManager hw = instances.get(key);
			if(hw != null)
				return hw;
		}
		synchronized(instances) {
			if(instances.containsKey(key))
				return instances.get(key);
			final HardwareManager hw = new HardwareManager(config);
			instances.put(hw.getKey(), hw);
			return hw;
		}
	}
	// get existing
	public static HardwareManager get(final String key) {
		return instances.get(key);
	}
	// new instance
	public HardwareManager(final HardwareConfig config) {
		this.key = this.genKey();
		this.pool = xThreadPool.get(
				POOL_NAME,
				0
		);
		this.pool.Start();
	}



	@Override
	public String toString() {
		return this.key;
	}
	@Override
	public String getKey() {
		return this.key;
	}
	private String genKey() {
return "key";
	}
	@Override
	public boolean matches(final xHashable arg0) {
return false;
	}



	// logger
	public static xLog log() {
		return xLog.getRoot(LOG_NAME);
	}



}
