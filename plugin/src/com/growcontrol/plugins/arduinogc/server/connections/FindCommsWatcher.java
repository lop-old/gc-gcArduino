package com.growcontrol.plugins.arduinogc.server.connections;

import gnu.io.CommPortIdentifier;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

import com.poixson.commonjava.Utils.CoolDown;
import com.poixson.commonjava.Utils.Keeper;
import com.poixson.commonjava.xLogger.xLog;


public class FindCommsWatcher {

	private static volatile FindCommsWatcher instance = null;
	private static final Object instanceLock = new Object();

	private final Set<String> current = new CopyOnWriteArraySet<String>();
	private final Set<EventListener> listeners = new CopyOnWriteArraySet<EventListener>();

	private final CoolDown cool = CoolDown.get("1s");
	private final AtomicBoolean updating = new AtomicBoolean(false);
	private final Object updateLock = new Object();



	public FindCommsWatcher get() {
		if(instance == null) {
			synchronized(instanceLock) {
				if(instance == null) {
					instance = new FindCommsWatcher();
					Keeper.add(instance);
				}
			}
		}
		return instance;
	}
	private FindCommsWatcher() {
		this.update();
	}



	public void update() {
		if(!this.cool.runAgain())
			return;
		if(!this.updating.compareAndSet(false, true))
			return;
		synchronized(this.updateLock) {
			final Map<String, CommPortIdentifier> map = FindComms.search();
			// found new
			for(final Entry<String, CommPortIdentifier> entry : map.entrySet()) {
				if(!this.current.contains(entry.getKey())) {
xLog.getRoot().info("New comm found: "+entry.getKey());
					for(final EventListener listener : this.listeners) {
						listener.added(entry.getKey(), entry.getValue());
					}
				}
			}
			// found removed
			for(final String name : this.current) {
				if(!map.containsKey(name)) {
xLog.getRoot().info("Comm removed: "+name);
					for(final EventListener listener : this.listeners) {
						listener.removed(name);
					}
				}
			}
		}
		this.updating.set(false);
	}



	public void addListener(final EventListener listener) {
		this.listeners.add(listener);
	}



	public static interface EventListener {

		public void added(  final String name, final CommPortIdentifier ident);
		public void removed(final String name);

	}



}
