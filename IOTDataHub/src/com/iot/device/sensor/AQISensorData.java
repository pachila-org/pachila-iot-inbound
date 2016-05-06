package com.iot.device.sensor;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.AQISensorValue;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.device.controlstatus.DataHubBase;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;

public class AQISensorData extends DataHubBase {

	private final static String CLASS_NAME = AQISensorData.class.getName();
	private static AQISensorData aqiSensorData = null;

	private AQISensorData() {

	}

	public static AQISensorData getInstance() {
		if (null == aqiSensorData) {
			aqiSensorData = new AQISensorData();
		}
		return aqiSensorData;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap, String mac_id, Map<String, String> controlmap)
			throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String vocSensordata = (String) returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		AQISensorValue aqiSensor = new AQISensorValue();
		if (vocSensordata.equals(controlmap.get(IOTPayLoadConstants.DEVICE_ERROR))) {
			aqiSensor.setDeviceType("aqiSensorbean");
			aqiSensor.setDeviceVersion("1");
			aqiSensor.setMacId(mac_id);
			aqiSensor.setOwnerId("2");
			aqiSensor.setaqiValue(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			aqiSensor.setAqiError("error");

		} else if (vocSensordata.equals(controlmap.get(IOTPayLoadConstants.DEVICE_NOTREADY))) {

			aqiSensor.setDeviceType("aqiSensorbean");
			aqiSensor.setDeviceVersion("1");
			aqiSensor.setMacId(mac_id);
			aqiSensor.setOwnerId("2");
			aqiSensor.setaqiValue(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			aqiSensor.setAqiError("no check");

		} else {
			aqiSensor.setDeviceType("aqiSensorbean");
			aqiSensor.setDeviceVersion("1");
			aqiSensor.setMacId(mac_id);
			aqiSensor.setOwnerId("2");
			aqiSensor.setaqiValue(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			aqiSensor.setAqiError("ok");

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		String json = mapper.writeValueAsString(aqiSensor);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, json);

		IOTDataClientHub restClientHub = new IOTDataClientHub();
		restClientHub.doAsyncPost(IOT_ROOT_URL, "aqi", aqiSensor);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return aqiSensor;
	}

}
