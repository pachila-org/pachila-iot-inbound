package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.FilterSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;

public class FilterMeshOneControlStatus extends DataHubBase {

	private final static String CLASS_NAME = FilterMeshOneControlStatus.class
			.getName();
	private static FilterMeshOneControlStatus filterControlStatus = null;

	private FilterMeshOneControlStatus() {

	}

	public static FilterMeshOneControlStatus getInstance() {
		if (null == filterControlStatus) {
			filterControlStatus = new FilterMeshOneControlStatus();
		}
		return filterControlStatus;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,
			String mac_id, Map<String, String> controlmap)
			throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String IOT_ROOT_URL = EnvConfigurationManager
				.getParameterValue(IOTConstants.IOT_ROOT_URL);
		FilterSwitch filterSwitchbean = new FilterSwitch();

		filterSwitchbean.setdevice_type(IOTPayLoadConstants.RESTFUL_FILTER_ONE);
		filterSwitchbean.setDevice_version("1");
		filterSwitchbean.setMac_id(mac_id);
		filterSwitchbean.setOwner_id(mac_id);
		filterSwitchbean.setFilter_status(returnmap
				.get(IOTPayLoadConstants.RETURN_VALUE));

		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		String json = mapper.writeValueAsString(filterSwitchbean);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, json);
		/*
		 * 
		 * "{\"mac_id\":\""+mac_id
		 * +"\",\"filter_status\":\""+filterSwitchbean.getFilter_status()+"\"}";
		 * ResteasyClient client = new ResteasyClientBuilder().build();
		 * ResteasyWebTarget target = client.target(IOT_ROOT_URL +
		 * "filteroneswitch"); //String jsoncall =
		 * "{\"mac_id\":\""+mac_id+"\",\"runningmodel\":\""
		 * +modelbean.getRunningmodel()+"\"}";
		 * 
		 * Future<Response> response =
		 * target.request().async().post(Entity.entity(jsoncall,
		 * "application/json")); 
		 */
		// String jsoncall =
		// "{\"mac_id\":\""+mac_id+"\",\"filter_status\":\""+filterSwitchbean.getFilter_status()+"\"}";
		IOTDataClientHub restClientHub = new IOTDataClientHub();
		restClientHub.doAsyncPost(IOT_ROOT_URL, "filteroneswitch",
				filterSwitchbean);

		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return filterSwitchbean;
	}

}
