package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.CallBackResultBean;
import com.iot.bean.QuietModeBean;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;

public class QuietModeSwitchStatus extends DataHubBase {
	private final static String CLASS_NAME = QuietModeSwitchStatus.class
			.getName();
	private static QuietModeSwitchStatus quietmodeControlStatus = null;

	CallBackResultBean resultBean;

	private QuietModeSwitchStatus() {

	}

	public static QuietModeSwitchStatus getInstance() {
		if (null == quietmodeControlStatus) {
			quietmodeControlStatus = new QuietModeSwitchStatus();
		}
		return quietmodeControlStatus;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,
			String mac_id, Map<String, String> controlmap)
			throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String quietmodeControlStatus = (String) returnmap
				.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager
				.getParameterValue(IOTConstants.IOT_ROOT_URL);
		QuietModeBean quietmodebean = new QuietModeBean();
		if (quietmodeControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_QUIETMODEON))) {
			quietmodebean.setMacId(mac_id);
			quietmodebean
					.setQuietmodeStatus(IOTPayLoadConstants.RESTFUL_QUIET_MODE_ON);
			quietmodebean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			quietmodebean
					.setDevicetype(IOTPayLoadConstants.RESTFUL_QUIET_MODE_ON);
			quietmodebean.setOwnerId(mac_id);
		}

		if (quietmodeControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_QUIETMODEOFF))) {

			quietmodebean.setMacId(mac_id);
			quietmodebean
					.setQuietmodeStatus(IOTPayLoadConstants.RESTFUL_QUIET_MODE_OFF);
			quietmodebean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			quietmodebean
					.setDevicetype(IOTPayLoadConstants.RESTFUL_QUIET_MODE_OFF);
			quietmodebean.setOwnerId(mac_id);

		}
		// 
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		String json = mapper.writeValueAsString(quietmodebean);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, json);
		/*
		 * //
		 * "{\"mac_id\":\""+mac_id
		 * +"\",\"quietmode_status\":\""+quietmodebean.getQuietmode_status
		 * ()+"\"}"; ResteasyClient client = new
		 * ResteasyClientBuilder().build(); ResteasyWebTarget target =
		 * client.target(IOT_ROOT_URL + "quietmode"); Future<Response> response
		 * = target.request().async().post(Entity.entity(jsoncall,
		 * "application/json")); 
		 */
		// String jsoncall =
		// "{\"mac_id\":\""+mac_id+"\",\"quietmode_status\":\""+quietmodebean.getQuietmode_status()+"\"}";
		IOTDataClientHub restClientHub = new IOTDataClientHub();
		restClientHub
				.doAsyncPost(IOT_ROOT_URL, "quietmode", quietmodebean);

		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return quietmodebean;
	}

}
