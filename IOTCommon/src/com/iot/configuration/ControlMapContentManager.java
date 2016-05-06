/**
 * Licensed Materials - Property of Pachila
 * 
 * 
 */
package com.iot.configuration;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class handles reading data from SQL configuration file
 * 
 */
public class ControlMapContentManager {

	/*
	 * Resource bundle to hold configuration key values
	 */

	private static ResourceBundle controlmapContentFile;

	private static Map<String, String> controlmapContents;

	static {
		try {

			controlmapContentFile = ResourceBundle.getBundle("com.iot.iotservice.configuration.properties.controlmap");

		} catch (MissingResourceException e) {
			System.err.println("SQLManager Error: Configuration File Missing. Exception: " + e);
		}
	}

	/**
	 * Return the configuration value for the given key
	 * @param key key
	 * @return value if found, else the key
	 */
	public static String getParameterValue(String key){
		try {		
			String value = (String)controlmapContentFile.getObject(key);
			return value;
		} catch(Exception exception){			
			return key;
		}	
	} 
	
	/**
	 * Prepare a Map from resource bundle
	 * 
	 * @param ResourceBundle
	 * 
	 */
	private static Map<String, String> readResourceBundleintoMap(ResourceBundle resouceBundle) {
		Map<String, String> map = new Hashtable<String, String>();
		if (resouceBundle != null) {
			for (Enumeration<String> enumeration = resouceBundle.getKeys(); enumeration.hasMoreElements();) {
				String key = (String) enumeration.nextElement();
				map.put(key, (String)resouceBundle.getObject(key));
			}
		}
		return map;
	}

	/**
	 * Return the extraction queries for PNM System.
	 * 
	 * @return Map<String, String>
	 */
	public static Map<String, String> getPayLoadContent() {
		if (null == controlmapContents) {
			controlmapContents = readResourceBundleintoMap(controlmapContentFile);
		}
		return controlmapContents;
	}
}
