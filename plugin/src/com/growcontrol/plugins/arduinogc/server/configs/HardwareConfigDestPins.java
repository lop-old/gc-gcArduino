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
			final Map<String, Object> datamap =
					utilsObject.castMap(
							String.class,
							Object.class,
							obj
					);
			for(final Entry<String, Object> entry : datamap.entrySet()) {
				System.out.println("<< "+entry.getKey()+" :: "+entry.getValue()+" >>");
			}
		}
		return pins;
	}



}
