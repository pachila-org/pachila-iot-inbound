package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.CallBackResultBean;
import com.iot.bean.LightSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;

public class LightSwitchStatus extends DataHubBase {

	private final static String CLASS_NAME = LightSwitchStatus.class.getName();
	private static LightSwitchStatus lightControlStatus = null;
	// static final String IOT_ROOT_URL = "http://192.168.12.106:8080/Inbound/";
	CallBackResultBean resultBean;

	private LightSwitchStatus() {

	}

	public static LightSwitchStatus getInstance() {
		if (null == lightControlStatus) {
			lightControlStatus = new LightSwitchStatus();
		}
		return lightControlStatus;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,
			String mac_id, Map<String, String> controlmap)
			throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String IOT_ROOT_URL = EnvConfigurationManager
				.getParameterValue(IOTConstants.IOT_ROOT_URL);

		String lightControlStatus = (String) returnmap
				.get(IOTPayLoadConstants.RETURN_VALUE);
		LightSwitch lightSwitchbean = new LightSwitch();

		if (lightControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_LIGHTON))) {

			lightSwitchbean.setMacId(mac_id);
			lightSwitchbean.setLightStatus(IOTPayLoadConstants.RESTFUL_LIGHT_ON);
			lightSwitchbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			lightSwitchbean.setdeviceType(IOTPayLoadConstants.RESTFUL_LIGHT_ON);
			lightSwitchbean.setOwnerId(mac_id);
		}
		// 
		if (lightControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_LIGHTOFF))) {

			lightSwitchbean.setMacId(mac_id);
			lightSwitchbean.setLightStatus(IOTPayLoadConstants.RESTFUL_LIGHT_OFF);
			lightSwitchbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			lightSwitchbean.setdeviceType(IOTPayLoadConstants.RESTFUL_LIGHT_OFF);
			lightSwitchbean.setOwnerId("1");
		}

		// 
		if (lightControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_LIGHTHALFON))) {

			lightSwitchbean.setMacId(mac_id);
			lightSwitchbean
					.setLightStatus(IOTPayLoadConstants.RESTFUL_LIGHT_HALF_ON);
			lightSwitchbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			lightSwitchbean
					.setdeviceType(IOTPayLoadConstants.RESTFUL_LIGHT_HALF_ON);
			lightSwitchbean.setOwnerId(mac_id);
		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		String json = mapper.writeValueAsString(lightSwitchbean);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, json);
		
		IOTDataClientHub restClientHub = new IOTDataClientHub();
		restClientHub.doAsyncPost(IOT_ROOT_URL, "lightswitch",
				lightSwitchbean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return lightSwitchbean;
	}

}
