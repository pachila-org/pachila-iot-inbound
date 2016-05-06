package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.AnionSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;

public class AnionSwitchStatus extends DataHubBase {

	private static final String CLASS_NAME = AnionSwitchStatus.class.getName();
	private static AnionSwitchStatus anionControlStatus = null;

	// static final String IOT_ROOT_URL = "http://192.168.12.106:8080/Inbound/";

	private AnionSwitchStatus() {
	}

	public static AnionSwitchStatus getInstance() {
		if (null == anionControlStatus) {
			anionControlStatus = new AnionSwitchStatus();
		}
		return anionControlStatus;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,
			String mac_id, Map<String, String> controlmap)
			throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String IOT_ROOT_URL = EnvConfigurationManager
				.getParameterValue(IOTConstants.IOT_ROOT_URL);

		String anionControlStatus = (String) returnmap
				.get(IOTPayLoadConstants.RETURN_VALUE);
		AnionSwitch anionbean = new AnionSwitch();
		if (anionControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_ANIONON))) {

			anionbean.setMacId(mac_id);
			anionbean.setAnionStatus(IOTPayLoadConstants.RESTFUL_ANION_ON);
			anionbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			anionbean.setDeviceType(IOTPayLoadConstants.RESTFUL_ANION_ON);
			anionbean.setOwnerId(mac_id);

		}
		if (anionControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_ANIONOFF))) {

			anionbean.setMacId(mac_id);
			anionbean.setAnionStatus(IOTPayLoadConstants.RESTFUL_ANION_OFF);
			anionbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			anionbean.setDeviceType(IOTPayLoadConstants.RESTFUL_ANION_OFF);
			anionbean.setOwnerId(mac_id);

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		String json = mapper.writeValueAsString(anionbean);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, json);

		IOTDataClientHub restClientHub = new IOTDataClientHub();
		restClientHub.doAsyncPost(IOT_ROOT_URL, "anionswitch", anionbean);

		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return anionbean;
	}

}
