package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;

public abstract class DataHubBase {

	final static String CLASS_NAME = DataHubBase.class.getName();

	public Object executeDataLoading(Map<String, String> returnmap, String mac_id, Map<String, String> controlmap) {
		final String METHOD_NAME = "executeDataLoading";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		Object object = null;
		try {
			object = doExecute(returnmap, mac_id, controlmap);

		} catch (IOTTechnicalException technicalException) {
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME,
					"Technical Exception Occured. " + technicalException.getMessage(), technicalException);
		} catch (Exception exception) {
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, "Exception Occured. " + exception.getMessage(),
					exception);
		}
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return object;
	}

	protected abstract Object doExecute(Map<String, String> returnmap, String mac_id, Map<String, String> controlmap)
			throws IOTTechnicalException, IOException;

}
