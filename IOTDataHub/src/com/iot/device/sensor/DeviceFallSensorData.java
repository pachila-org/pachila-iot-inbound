package com.iot.device.sensor;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.FallSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.device.controlstatus.DataHubBase;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;


public class DeviceFallSensorData extends DataHubBase{
	
	private final static String CLASS_NAME =  DeviceFallSensorData.class.getName();
	private static DeviceFallSensorData deviceFallSensorData = null;

	private DeviceFallSensorData() {

	}

	public static DeviceFallSensorData getInstance() {
		if (null == deviceFallSensorData) {
			deviceFallSensorData = new DeviceFallSensorData();
		}
		return deviceFallSensorData;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,String mac_id,Map<String,String> controlmap) throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String deviceFallSensordata = (String)returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		FallSwitch deviceFallSensorbean=new FallSwitch();
		if(deviceFallSensordata.equals(controlmap.get(IOTPayLoadConstants.CONTROL_DEVICE_FALL))){
			deviceFallSensorbean.setDevice_type("devicefallSensor");
			deviceFallSensorbean.setDevice_version("1");
			deviceFallSensorbean.setMac_id(mac_id);
			deviceFallSensorbean.setOwner_id("2");
			deviceFallSensorbean.setFall_status("fall");

		}
		if(deviceFallSensordata.equals(controlmap.get(IOTPayLoadConstants.CONTROL_DEVICE_NOT_FALL))){
			deviceFallSensorbean.setDevice_type("devicefallSensor");
			deviceFallSensorbean.setDevice_version("1");
			deviceFallSensorbean.setMac_id(mac_id);
			deviceFallSensorbean.setOwner_id("2");
			deviceFallSensorbean.setFall_status("notfall");

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(deviceFallSensorbean);
        IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,json);

        IOTDataClientHub restClientHub= new IOTDataClientHub();
        restClientHub.doAsyncPost(IOT_ROOT_URL, "fall", deviceFallSensorbean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return deviceFallSensorbean;
	}


}
