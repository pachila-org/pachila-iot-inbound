package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.HumidifySwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;

public class HumiditySwitchStatus extends DataHubBase {

	private final static String CLASS_NAME = HumiditySwitchStatus.class
			.getName();
	private static HumiditySwitchStatus humidityControlStatus = null;

	private HumiditySwitchStatus() {

	}

	public static HumiditySwitchStatus getInstance() {
		if (null == humidityControlStatus) {
			humidityControlStatus = new HumiditySwitchStatus();
		}
		return humidityControlStatus;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,
			String mac_id, Map<String, String> controlmap)
			throws IOTTechnicalException, IOException {

		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);

		String humidityControlStatus = (String) returnmap
				.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager
				.getParameterValue(IOTConstants.IOT_ROOT_URL);

		HumidifySwitch humiditybean = new HumidifySwitch();
		if (humidityControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_HUMIDIFYAUTO_ON))) {

			humiditybean.setMac_id(mac_id);
			humiditybean
					.setHumidify_status(IOTPayLoadConstants.RESTFUL_HUMIDIFY_AUTO_ON);
			humiditybean.setDevice_version(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			humiditybean
					.setdevice_type(IOTPayLoadConstants.RESTFUL_HUMIDIFY_AUTO_ON);
			humiditybean.setOwner_id(mac_id);

		}

		if (humidityControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_HUMIDIFYAUTO_OFF))) {

			humiditybean.setMac_id(mac_id);
			humiditybean
					.setHumidify_status(IOTPayLoadConstants.RESTFUL_HUMIDIFY_AUTO_OFF);
			humiditybean.setDevice_version(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			humiditybean
					.setdevice_type(IOTPayLoadConstants.RESTFUL_HUMIDIFY_AUTO_OFF);
			humiditybean.setOwner_id(mac_id);

		}
		if (humidityControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_HUMIDIFY_CONTINUATION_ON))) {

			humiditybean.setMac_id(mac_id);
			humiditybean
					.setHumidify_status(IOTPayLoadConstants.RESTFUL_HUMIDIFY_CONTINUATION_ON);
			humiditybean.setDevice_version(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			humiditybean
					.setdevice_type(IOTPayLoadConstants.RESTFUL_HUMIDIFY_CONTINUATION_ON);
			humiditybean.setOwner_id(mac_id);

		}
		if (humidityControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_HUMIDIFYOFF))) {

			humiditybean.setMac_id(mac_id);
			humiditybean
					.setHumidify_status(IOTPayLoadConstants.RESTFUL_HUMIDIFY_OFF);
			humiditybean.setDevice_version(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			humiditybean.setdevice_type(IOTPayLoadConstants.RESTFUL_HUMIDIFY_OFF);
			humiditybean.setOwner_id(mac_id);

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		String json = mapper.writeValueAsString(humiditybean);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, json);

		IOTDataClientHub restClientHub = new IOTDataClientHub();
		restClientHub.doAsyncPost(IOT_ROOT_URL, "humidityswitch",
				humiditybean);

		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return humiditybean;
	}

}
