//===================================================================
// Display.java
// 	Description:
// 		Handles the display settings for the orchestrator.
// 		Basically prints a timestamp in front of the outputs.
//===================================================================

package com.socialvagrancy.spectraxml.orchestrator.ui;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Display
{
	public static void println(String line)
	{
		DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();

		System.err.println(dateformat.format(now) + ": " + line);
	}
}
