package com.iot.device.sensor;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.VOCSensor;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.device.controlstatus.DataHubBase;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;


public class VOCSensorData extends DataHubBase{
	
	private final static String CLASS_NAME =  VOCSensorData.class.getName();
	private static VOCSensorData vocSensorData = null;

	private VOCSensorData() {

	}

	public static VOCSensorData getInstance() {
		if (null == vocSensorData) {
			vocSensorData = new VOCSensorData();
		}
		return vocSensorData;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,String mac_id,Map<String,String> controlmap) throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String vocSensordata = (String)returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		VOCSensor vocSensorbean=new VOCSensor();
		if(vocSensordata.equals(controlmap.get(IOTPayLoadConstants.DEVICE_ERROR))){
			vocSensorbean.setDeviceType("vocSensorbean");
			vocSensorbean.setDeviceVersion("1");
			vocSensorbean.setMacId(mac_id);
			vocSensorbean.setOwnerId("2");
			vocSensorbean.setVocValue(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			vocSensorbean.setVocError("error");

		}
		else if(vocSensordata.equals(controlmap.get(IOTPayLoadConstants.DEVICE_NOTREADY))){

			vocSensorbean.setDeviceType("vocSensorbean");
			vocSensorbean.setDeviceVersion("1");
			vocSensorbean.setMacId(mac_id);
			vocSensorbean.setOwnerId("2");
			vocSensorbean.setVocValue(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			vocSensorbean.setVocError("no check");

		} else {
			vocSensorbean.setDeviceType("vocSensorbean");
			vocSensorbean.setDeviceVersion("1");
			vocSensorbean.setMacId(mac_id);
			vocSensorbean.setOwnerId("2");
			vocSensorbean.setVocValue(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			vocSensorbean.setVocError("ok");

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(vocSensorbean);
        IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,json);

        IOTDataClientHub restClientHub= new IOTDataClientHub();
        restClientHub.doAsyncPost(IOT_ROOT_URL, "voc", vocSensorbean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return vocSensorbean;
	}


}
