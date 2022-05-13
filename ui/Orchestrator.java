//===================================================================
// Orchestrator.java
// 	Description: A script to generate the an XML document from
// 	multiple XML calls. The intended use is to generate a bunch
// 	of XML documents to be fed into applications like Splunk
// 	for logging purposes.
//===================================================================

package com.socialvagrancy.spectraxml.orchestrator.ui;

import com.socialvagrancy.spectraxml.orchestrator.utils.OrchestratedController;
import com.socialvagrancy.spectraxml.commands.URLs;
import com.socialvagrancy.spectraxml.utils.Connector;
import com.socialvagrancy.spectraxml.utils.SpectraController;
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
	private SpectraController octl;

	public Orchestrator(String ip, boolean is_https, boolean ignore_ssl)
	{
		octl = new SpectraController(ip, is_https, ignore_ssl, "../logs/slxml-orchestrator.log", 1, 10240, 3);	
	}

	public void runScheduledService(String option1, String option2, String option3, String option4, String option5)
	{
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		final Runnable tasks = new Runnable()
	       	{
			public void run() { test(option1, option2, option3, option4); } 
		};
		final ScheduledFuture<?> taskHandle = scheduler.scheduleAtFixedRate(tasks, 1, Long.valueOf(option5), TimeUnit.MINUTES);	
	}

	public boolean login(String username, String password)
	{
		return octl.login(username, password);
	}

	public static void main(String args[])
	{
		ArgParser aparser = new ArgParser();
		
		if(args.length > 0)
		{
			aparser.parseArguments(args);
		}

		if(aparser.checkValidInput())
		{
			Orchestrator orc = new Orchestrator(aparser.getIPAddress(), aparser.getSecureHTTPS(), aparser.getIgnoreSSL());

			if(orc.login(aparser.getUsername(), aparser.getPassword()))
			{
				Display.println("login successful.");
				orc.runScheduledService(aparser.getCmdOption(), aparser.getCmdOption2(), aparser.getCmdOption3(), aparser.getCmdOption4(), aparser.getCmdOption5());
			}
			else
			{
				Display.println("Unable to login to the Spectra Logic tape library at " + aparser.getIPAddress() + " with specified username " + aparser.getUsername() + ".");
			}
		}
		else
		{
			System.err.println("Invalid options selected. Please use -h or --help to determine the commands.");
		}
	}

	public void test(String option1, String option2, String option3, String option4)
	{
		Display.println("Starting download of XML docs...");
	
		octl.downloadXMLSheet("library-status", option1, option2, "libraryStatus.xml");	
		octl.downloadXMLSheet("list-controllers", option1, option2, "controllers.xml");
		octl.downloadXMLSheet("list-inventory", option1, option2, "inventory.xml");
		octl.downloadXMLSheet("list-drives", option1, option2, "driveList.xml");
		octl.downloadXMLSheet("mlmdb", option1, option2, "mlmdb.csv");
		octl.downloadXMLSheet("system-messages", option1, option2, "systemMessages.xml");
		

		Display.println("Download of XML docs completed.");
	/*	Logger log = new Logger("../logs/slxml-orchestrator.log", 1024, 3, 1);
		Connector conn = new Connector(false, log);

		System.err.println("Starting XML download.");
		conn.queryLibrary("http://10.85.41.7/gf/login.xml?username=su");
		conn.downloadFromLibrary("http://10.85.41.7/gf/controllers.xml?action=list", "../", "controllers.xml");
		conn.downloadFromLibrary("http://10.85.41.7/gf/inventory.xml?partition=BP%20Test%20LTO6", "../", "inventory.xml");
		conn.downloadFromLibrary("http://10.85.41.7/gf/drivelist.xml", "../", "driveList.xml");
		conn.downloadFromLibrary("http://10.85.41.7/gf/mlmdb.csv", "../", "mlmdb.csv");
		conn.downloadFromLibrary("http://10.85.41.7/gf/systemMessages.xml", "../", "systemMessages.xml");
		System.err.println("XML Files Downloaded.");
	*/
	}
}
