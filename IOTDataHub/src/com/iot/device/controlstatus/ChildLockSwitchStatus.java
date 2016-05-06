package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.CallBackResultBean;
import com.iot.bean.ChildLockSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;

public class ChildLockSwitchStatus extends DataHubBase {

	private final static String CLASS_NAME = ChildLockSwitchStatus.class
			.getName();
	private static ChildLockSwitchStatus childLockControlStatus = null;

	CallBackResultBean resultBean;

	private ChildLockSwitchStatus() {

	}

	public static ChildLockSwitchStatus getInstance() {
		if (null == childLockControlStatus) {
			childLockControlStatus = new ChildLockSwitchStatus();
		}
		return childLockControlStatus;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,
			String mac_id, Map<String, String> controlmap)
			throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String childLockControlStatus = (String) returnmap
				.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager
				.getParameterValue(IOTConstants.IOT_ROOT_URL);
		ChildLockSwitch childloclbean = new ChildLockSwitch();
		if (childLockControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_CHILDLOCKON))) {

			childloclbean.setMacId(mac_id);
			childloclbean
					.setChildlockStatus(IOTPayLoadConstants.RESTFUL_CHILELOCK_ON);
			childloclbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			childloclbean.setDeviceType(IOTPayLoadConstants.RESTFUL_CHILELOCK_ON);
			childloclbean.setOwnerId(mac_id);

		}

		if (childLockControlStatus.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_CHILDLOCKOFF))) {

			childloclbean.setMacId(mac_id);
			childloclbean
					.setChildlockStatus(IOTPayLoadConstants.RESTFUL_CHILELOCK_OFF);
			childloclbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			childloclbean
					.setDeviceType(IOTPayLoadConstants.RESTFUL_CHILELOCK_OFF);
			childloclbean.setOwnerId(mac_id);

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		String json = mapper.writeValueAsString(childloclbean);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, json);
		/*
		 * //
		 * "{\"mac_id\":\""+mac_id
		 * +"\",\"childlock_status\":\""+childloclbean.getChildlock_status
		 * ()+"\"}"; ResteasyClient client = new
		 * ResteasyClientBuilder().build(); ResteasyWebTarget target =
		 * client.target(IOT_ROOT_URL + "childlockswitch"); Future<Response>
		 * response = target.request().async().post(Entity.entity(jsoncall,
		 * "application/json")); 
		 */
		// String jsoncall =
		// "{\"mac_id\":\""+mac_id+"\",\"childlock_status\":\""+childloclbean.getChildlock_status()+"\"}";
		IOTDataClientHub restClientHub = new IOTDataClientHub();
		restClientHub.doAsyncPost(IOT_ROOT_URL, "childlockswitch",
				childloclbean);

		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return childloclbean;
	}

}
