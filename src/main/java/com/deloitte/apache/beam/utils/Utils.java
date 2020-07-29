package com.deloitte.apache.beam.utils;

import java.text.SimpleDateFormat;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Utils {
	
	public static DateTimeFormatter dateMsFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateSecFormatter = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
	public static DateTimeFormatter dateformatter = DateTimeFormat.forPattern("yyyy-MM-dd");

}