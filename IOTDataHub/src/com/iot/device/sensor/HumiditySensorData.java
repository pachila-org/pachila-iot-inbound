package com.iot.device.sensor;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.HumiditySensor;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.device.controlstatus.DataHubBase;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;


public class HumiditySensorData extends DataHubBase{
	
	private final static String CLASS_NAME =  HumiditySensorData.class.getName();
	private static HumiditySensorData humiditySensorData = null;

	private HumiditySensorData() {

	}

	public static HumiditySensorData getInstance() {
		if (null == humiditySensorData) {
			humiditySensorData = new HumiditySensorData();
		}
		return humiditySensorData;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,String mac_id,Map<String,String> controlmap) throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String humidifySensordata = (String)returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		HumiditySensor humiditySensorbean = new HumiditySensor();
		if(humidifySensordata.equals(controlmap.get(IOTPayLoadConstants.DEVICE_ERROR))){
			humiditySensorbean.setdevice_type("checkhumidity");
			humiditySensorbean.setDevice_version("1");
			humiditySensorbean.setMac_id(mac_id);
			humiditySensorbean.setOwner_id("2");
			humiditySensorbean.setHumidity_value(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			humiditySensorbean.setHumidity_error("error");

		}
		
		else{
			humiditySensorbean.setdevice_type("checkhumidity");
			humiditySensorbean.setDevice_version("1");
			humiditySensorbean.setMac_id(mac_id);
			humiditySensorbean.setOwner_id("2");
			humiditySensorbean.setHumidity_value(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			humiditySensorbean.setHumidity_error("ok");

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(humiditySensorbean);
        IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,json);
        IOTDataClientHub restClientHub= new IOTDataClientHub();
        restClientHub.doAsyncPost(IOT_ROOT_URL, "humidity", humiditySensorbean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return humiditySensorbean;
	}


}
