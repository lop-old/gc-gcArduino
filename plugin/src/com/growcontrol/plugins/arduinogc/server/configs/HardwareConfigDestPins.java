package com.growcontrol.plugins.arduinogc.server.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.growcontrol.common.meta.MetaAddress;
import com.poixson.commonjava.Utils.utils;
import com.poixson.commonjava.Utils.utilsObject;


public final class HardwareConfigDestPins {
	private HardwareConfigDestPins() {}



	// configs from set
	public static Map<Integer, MetaAddress> getAll(final Set<Object> dataset) {
		if(utils.isEmpty(dataset))
			return null;
		final Map<Integer, MetaAddress> pins = new HashMap<Integer, MetaAddress>();
		for(final Object obj : dataset) {
			// address => pin
			final Map<String, Object> datamap =
					utilsObject.castMap(
							String.class,
							Object.class,
							obj
					);
			for(final Entry<String, Object> entry : datamap.entrySet()) {
				final String addressStr = entry.getKey();
				final MetaAddress address = MetaAddress.get(addressStr);
				if(address == null) throw new RuntimeException("Invalid pin destination address in config: "+addressStr);
				final Integer pin = (Integer) entry.getValue();
				if(pin == null) throw new RuntimeException("Invalid pin in config: "+entry.getKey());
				pins.put(pin, address);
			}
		}
		return pins;
	}



}
