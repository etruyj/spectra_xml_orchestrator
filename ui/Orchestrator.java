//===================================================================
// Orchestrator.java
// 	Description: A script to generate the an XML document from
// 	multiple XML calls. The intended use is to generate a bunch
// 	of XML documents to be fed into applications like Splunk
// 	for logging purposes.
//===================================================================

package com.socialvagrancy.spectraxml.orchestrator.ui;

import com.socialvagrancy.spectraxml.commands.URLs;
import com.socialvagrancy.spectraxml.utils.Connector;
import com.socialvagrancy.spectraxml.structures.XMLResult;
import com.socialvagrancy.spectraxml.ui.ArgParser;
import com.socialvagrancy.utils.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeUnit.MINUTES;
//import java.util.concurrent.TimeUnit.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit.SECONDS;

public class Orchestrator
{
	public void runScheduledService()
	{
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		final Runnable beeper = new Runnable()
	       	{
			public void run() { test(); } 
		};
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 1, 10, TimeUnit.SECONDS);	
	}

	public static void main(String args[])
	{
		Orchestrator orc = new Orchestrator();
		orc.runScheduledService();
	}

	public void test()
	{
		Logger log = new Logger("../logs/slxml-orchestrator.log", 1024, 3, 1);
		Connector conn = new Connector(false, log);
		conn.queryLibrary("http://10.85.41.7/gf/login.xml?username=su");
		conn.downloadFromLibrary("http://10.85.41.7/gf/inventory.xml?partition=BP%20Test%20LTO6", "../", "inv.xml");
		System.err.println("Beep");
	}
}
