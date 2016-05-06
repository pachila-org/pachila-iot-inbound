/**
 * Licensed Materials - Property of Pachila
 * 

 * 
 */

package com.iot.configuration;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class EnvConfigurationManager {
    private static ResourceBundle localizedConfigFile;
    private static Map<String, String> localizedProperties;
    
    static {
        try {
            localizedConfigFile = ResourceBundle.getBundle("com.iot.iotservice.configuration.properties.iotconfiguration");
        } catch (MissingResourceException e) {
            System.err.println("Error: Configuration File com.iot.iotservice.configuration.properties.iotconfiguration Missing. Exception: " + e);
        }
        localizedProperties = new Hashtable<String, String>();
        readResourceintoMap(localizedConfigFile);
        //readJNDIintoMap();
        readLocalConfigIntoMap();
    }
    
    
    private static void readResourceintoMap(ResourceBundle resouceBundle) {
        if (resouceBundle != null) {
            for (Enumeration<String> enumeration = resouceBundle.getKeys(); enumeration.hasMoreElements();) {
                String key = (String) enumeration.nextElement();
                localizedProperties.put(key, (String) resouceBundle.getObject(key));
            }
        }
    }
    

    @SuppressWarnings("unused")
    private static void readJNDIintoMap() {
        Set<String> etpKeySet = localizedProperties.keySet();
        Context context;
        try {
            context = new InitialContext();
            String mapValue = null;
            for (Iterator<String> propertiesIteratorr = etpKeySet.iterator(); propertiesIteratorr.hasNext();) {
                String key = (String) propertiesIteratorr.next();
                mapValue = getlookupValue(context, key);
                if (null != mapValue) {
                    localizedProperties.put(key, mapValue);
                }
            }
        } catch (NamingException e) {
            System.err.println("Error while loading JNDI properties to map. " + e.getMessage());
        }
    }
    

    private static void readLocalConfigIntoMap() {
        Set<String> etpKeySet = localizedProperties.keySet();
        java.util.Properties props = new java.util.Properties();
        
        try {
        	System.out.println(System.getProperty("user.dir"));
            java.io.FileInputStream fis = new java.io.FileInputStream(new java.io.File("iotconfiguration.properties"));
            props.load(fis);
            fis.close();
            String mapValue = null;
            for (Iterator<String> propertiesIteratorr = etpKeySet.iterator(); propertiesIteratorr.hasNext();) {
                String key = (String) propertiesIteratorr.next();
                mapValue = props.getProperty(key);
                if (null != mapValue) {
                    localizedProperties.put(key, mapValue);
                }
            }
        } catch (Exception e) {
            System.err.println("Error while loading Local properties to map. " + e.getMessage());
        }
    }
    
    
    private static String getlookupValue(Context context, String key) {
        try {
            String value = (String) context.lookup(key);
            return value;
        } catch (NamingException e) {
            return null;
        }
    }
    
  
    public static String getParameterValue(String key) {
        String value = localizedProperties.get(key);
        if (null != value) {
            return value;
        } else {
            return key;
        }
    } 
    
    public static String getParameterValue(String key,String inputValue) {
    	String value = localizedProperties.get(key);
    	 if (null != value) {
    		 String formattedMessage = MessageFormat.format(value, inputValue);
          	System.out.println(formattedMessage); 
             return formattedMessage;
         } else {
        	
         	 return key;
         }
    	
    }

}
