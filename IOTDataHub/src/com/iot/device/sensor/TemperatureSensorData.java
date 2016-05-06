package com.iot.device.sensor;

import java.io.IOException;
import java.util.Map;










import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;








import com.iot.bean.TemporatureSensor;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.device.controlstatus.DataHubBase;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;


public class TemperatureSensorData extends DataHubBase{
	
	private final static String CLASS_NAME =  TemperatureSensorData.class.getName();
	private static TemperatureSensorData temperatureSensorData = null;

	private TemperatureSensorData() {

	}

	public static TemperatureSensorData getInstance() {
		if (null == temperatureSensorData) {
			temperatureSensorData = new TemperatureSensorData();
		}
		return temperatureSensorData;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,String mac_id,Map<String,String> controlmap) throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String humidifySensorControlStatus = (String)returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		TemporatureSensor temporatureSensorbean = new TemporatureSensor();
		if(humidifySensorControlStatus.equals(controlmap.get(IOTPayLoadConstants.DEVICE_ERROR))){
			temporatureSensorbean.setDeviceType("checktemporatrue");
			temporatureSensorbean.setDeviceVersion("1");
			temporatureSensorbean.setMacId(mac_id);
			temporatureSensorbean.setOwnerId("2");
			temporatureSensorbean.setTemperature_value(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			temporatureSensorbean.setTemperatureError("error");

		}
		
		else{
			temporatureSensorbean.setDeviceType("checktemporatrue");
			temporatureSensorbean.setDeviceVersion("1");
			temporatureSensorbean.setMacId(mac_id);
			temporatureSensorbean.setOwnerId("2");
			temporatureSensorbean.setTemperature_value(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			temporatureSensorbean.setTemperatureError("ok");

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(temporatureSensorbean);
        IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,json);
        IOTDataClientHub restClientHub= new IOTDataClientHub();
        restClientHub.doAsyncPost(IOT_ROOT_URL, "temperature", temporatureSensorbean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return temporatureSensorbean;
	}


}
