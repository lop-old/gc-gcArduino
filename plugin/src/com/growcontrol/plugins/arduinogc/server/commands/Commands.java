package com.growcontrol.plugins.arduinogc.server.commands;

import com.poixson.commonjava.xEvents.annotations.xEvent;
import com.poixson.commonjava.xLogger.commands.xCommandEvent;
import com.poixson.commonjava.xLogger.commands.xCommandListener;


public class Commands implements xCommandListener {
//	private static final String LOG_NAME = ArduinoGC.LOG_NAME;



	// server commands
	@Override
	@xEvent(
			priority=ListenerPriority.NORMAL,
//			async=false,
			filterHandled=true,
			filterCancelled=true)
	public void onCommand(final xCommandEvent event) {

	}



}
