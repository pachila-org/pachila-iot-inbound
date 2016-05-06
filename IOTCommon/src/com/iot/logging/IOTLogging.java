/**
 * Licensed Materials - Property of Pachila
 * 
 */
package com.iot.logging;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.iot.logging.IOTLogFormatter;
import com.iot.logging.IOTLogging;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;

/**
 * This class is used for logging purpose
 * 
 * 
 */
public class IOTLogging {

	private static IOTLogging loggingInstance = null;
	private static Logger logger = null;
	final static String CLASS_NAME = IOTLogging.class.getName();

	private IOTLogging() {
		logger = Logger.getLogger(CLASS_NAME);

		// Set parent(default) Handler as false
		logger.setUseParentHandlers(false);

		// Get File Handler parameters - need to move this to a configuration
		// file
		String filePattern = EnvConfigurationManager.getParameterValue(IOTConstants.FILEPATTERN_FOR_LOGGING);
		int fileBytesLimit = Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.FILEBYTESLIMIT_FOR_LOGGING));
		int fileCount = Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.FILECOUNT_FOR_LOGGING));
		boolean append = Boolean.parseBoolean(EnvConfigurationManager.getParameterValue(IOTConstants.APPENDMODE_FOR_LOGGING));
		String formatterName = EnvConfigurationManager.getParameterValue(IOTConstants.FORMATTERNAME_FOR_LOGGING);
		String logtargettype = EnvConfigurationManager.getParameterValue(IOTConstants.LOGGING_FILEHANDLER_LOG_TARGET_TYPE);
		

		Handler fileHandler = null;
		try {

			fileHandler = new FileHandler(filePattern, fileBytesLimit, fileCount, append);
		} catch (IOException exception) {
			System.err.println("Error: Logging failed due to IOException. Exception: " + exception);
		}

		// Set formatter for handler e.g text or XML
		try {
			ClassLoader.getSystemClassLoader().loadClass(formatterName);
			fileHandler.setFormatter(new IOTLogFormatter());
		} catch (ClassNotFoundException e) {
			fileHandler.setFormatter(new SimpleFormatter());
		}

		// Add Handler to logger
		if(IOTConstants.LOG_TARGET_CONSOLE.equals(logtargettype))
		{
			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new IOTLogFormatter());
			logger.addHandler(consoleHandler);
		} else {
			logger.addHandler(fileHandler);
		}
		
		//logger.addHandler(new ConsoleHandler());
		// Set log level
		String logLevel = EnvConfigurationManager.getParameterValue(IOTConstants.LOGLEVEL_OF_LOGGING);
		logger.setLevel(Level.parse(logLevel));

	}

	/**
	 * Get instance of logging service
	 * 
	 * @return instance of logging service
	 */
	public static IOTLogging getInstance() {
		if (loggingInstance == null) {
			loggingInstance = new IOTLogging();
		}
		return loggingInstance;
	}

	/**
	 * Logging level entry method
	 * 
	 * @param className
	 *            Class Name for logging
	 * @param methodName
	 *            Method Name for logging
	 */
	public void entry(String className, String methodName) {
		logger.entering(className, methodName);
	}

	/**
	 * Logging level entry method
	 * 
	 * @param className
	 *            Class Name for logging
	 * @param methodName
	 *            Method Name for logging
	 * @param message
	 *            String message to be logged
	 */
	public void entry(String className, String methodName, String message) {
		logger.logp(Level.FINER, className, methodName, message);
	}

	/**
	 * Logging level exit method
	 * 
	 * @param className
	 *            Class Name for logging
	 * @param methodName
	 *            Method Name for logging
	 */
	public void exit(String className, String methodName) {
		logger.exiting(className, methodName);
	}

	/**
	 * Logging level exit method
	 * 
	 * @param className
	 *            Class Name for logging
	 * @param methodName
	 *            Method Name for logging
	 * @param msg
	 *            String message to be logged
	 */
	public void info(String className, String methodName, String msg) {
		logger.logp(Level.INFO, className, methodName, msg);
	}

	/**
	 * Logging level debug method
	 * 
	 * @param className
	 *            Class Name for logging
	 * @param methodName
	 *            Method Name for logging
	 * @param msg
	 *            String message to be logged
	 */
	public void debug(String className, String methodName, String msg) {
		logger.logp(Level.INFO, className, methodName, msg);
	}

	/**
	 * Logging level warning method
	 * 
	 * @param className
	 *            Class Name for logging
	 * @param methodName
	 *            Method Name for logging
	 * @param msg
	 *            String message to be logged
	 */
	public void warn(String className, String methodName, String msg) {
		logger.logp(Level.WARNING, className, methodName, msg);
	}

	/**
	 * Logging level error method
	 * 
	 * @param className
	 *            Class Name for logging
	 * @param methodName
	 *            Method Name for logging
	 * @param msg
	 *            String message to be logged
	 */
	public void error(String className, String methodName, String msg) {
		logger.logp(Level.SEVERE, className, methodName, msg);
	}

	/**
	 * Logging level error method
	 * 
	 * @param className
	 *            Class Name for logging
	 * @param methodName
	 *            Method Name for logging
	 * @param msg
	 *            String message to be logged
	 * @param throwable
	 *            Throwable object for logging stack trace
	 */
	public void error(String className, String methodName, String msg, Throwable throwable) {
		Object params[] = new Object[2];
		params[0] = throwable;
		params[1] = "";
		logger.logp(Level.SEVERE, className, methodName, msg, params);
	}

}
