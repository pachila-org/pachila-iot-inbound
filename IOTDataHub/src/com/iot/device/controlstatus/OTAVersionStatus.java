package com.iot.device.controlstatus;

import java.io.IOException;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.CallBackResultBean;
import com.iot.bean.OTAMessage;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;

public class OTAVersionStatus {

	private final static String CLASS_NAME = OTAVersionStatus.class.getName();
	private static OTAVersionStatus OTAControlStatus = null;

	CallBackResultBean resultBean;

	private OTAVersionStatus() {

	}

	public static OTAVersionStatus getInstance() {
		if (null == OTAControlStatus) {
			OTAControlStatus = new OTAVersionStatus();
		}
		return OTAControlStatus;
	}

	public Object controlstatus(String mac_id, OTAMessage OTAbean) throws IOTTechnicalException, IOException {
		// 
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String IOT_ROOT_URL = EnvConfigurationManager
				.getParameterValue(IOTConstants.IOT_ROOT_URL);
		//START change json string way to javabean to post the data 
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		//String json = mapper.writeValueAsString(OTAbean);
		//set defaule Manufacturer to '0'
		if(null == OTAbean.getManufacturer()){
			OTAbean.setManufacturer(IOTConstants.DEVICE_DEFAULE_MANUFACTURER);
		}
		String json = mapper.writeValueAsString(OTAbean);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, json);
		
		IOTDataClientHub restClientHub = new IOTDataClientHub();
		restClientHub.doAsyncPost(IOT_ROOT_URL, "ota", OTAbean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return OTAbean;

	}

}
