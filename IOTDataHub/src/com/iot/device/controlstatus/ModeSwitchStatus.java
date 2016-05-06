package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.RunModelSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;

public class ModeSwitchStatus extends DataHubBase {
	private final static String CLASS_NAME = ModeSwitchStatus.class.getName();
	private static ModeSwitchStatus modeControlStatus = null;

	private ModeSwitchStatus() {

	}

	public static ModeSwitchStatus getInstance() {
		if (null == modeControlStatus) {
			modeControlStatus = new ModeSwitchStatus();
		}
		return modeControlStatus;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,
			String mac_id, Map<String, String> controlmap)
			throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);

		String IOT_ROOT_URL = EnvConfigurationManager
				.getParameterValue(IOTConstants.IOT_ROOT_URL);

		RunModelSwitch modelbean = new RunModelSwitch();
		String controlValue = (String) returnmap
				.get(IOTPayLoadConstants.RETURN_VALUE);
		if (controlValue.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_RUNMODELSTOP))) {
			modelbean.setMacId(mac_id);
			modelbean.setRunningmodel(IOTPayLoadConstants.RESTFUL_MODE_STOP);
			modelbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			modelbean.setDeviceType(IOTPayLoadConstants.RESTFUL_MODE_STOP);
			modelbean.setOwnerId(mac_id);

		} else if (controlValue.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_RUNMODELAUTO))) {
			modelbean.setMacId(mac_id);
			modelbean.setRunningmodel(IOTPayLoadConstants.RESTFUL_MODE_AUTO);
			modelbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			modelbean.setDeviceType(IOTPayLoadConstants.RESTFUL_MODE_AUTO);
			modelbean.setOwnerId(mac_id);

		} else if (controlValue.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_RUNMODELSLEEP))) {
			modelbean.setMacId(mac_id);
			modelbean.setRunningmodel(IOTPayLoadConstants.RESTFUL_MODE_SLEEP);
			;
			modelbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			modelbean.setDeviceType(IOTPayLoadConstants.RESTFUL_MODE_SLEEP);
			modelbean.setOwnerId(mac_id);

		} else if (controlValue.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_RUNMODELSTRONG))) {
			modelbean.setMacId(mac_id);
			modelbean.setRunningmodel(IOTPayLoadConstants.RESTFUL_MODE_STRONG);
			modelbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			modelbean.setDeviceType(IOTPayLoadConstants.RESTFUL_MODE_STRONG);
			modelbean.setOwnerId(mac_id);

		} else if (controlValue.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_RUNMODELHANDONE))) {
			modelbean.setMacId(mac_id);
			modelbean.setRunningmodel(IOTPayLoadConstants.RESTFUL_MODE_ONE);
			modelbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			modelbean.setDeviceType(IOTPayLoadConstants.RESTFUL_MODE_ONE);
			modelbean.setOwnerId(mac_id);

		} else if (controlValue.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_RUNMODELHANDTWO))) {
			modelbean.setMacId(mac_id);
			modelbean.setRunningmodel(IOTPayLoadConstants.RESTFUL_MODE_TWO);
			modelbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			modelbean.setDeviceType(IOTPayLoadConstants.RESTFUL_MODE_TWO);
			modelbean.setOwnerId(mac_id);

		} else if (controlValue.equals(controlmap
				.get(IOTPayLoadConstants.CONTROL_RUNMODELHANDTHREE))) {
			modelbean.setMacId(mac_id);
			modelbean.setRunningmodel(IOTPayLoadConstants.RESTFUL_MODE_THREE);
			modelbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			modelbean.setDeviceType(IOTPayLoadConstants.RESTFUL_MODE_THREE);
			modelbean.setOwnerId(mac_id);

		} else if (controlValue.equals(controlmap
				.get(IOTPayLoadConstants.DEVICE_ERROR))) {
			modelbean.setMacId(mac_id);
			modelbean.setRunningmodel(IOTPayLoadConstants.RESTFUL_MODE_ERROR);
			modelbean.setDeviceVersion(returnmap
					.get(IOTPayLoadConstants.RETURN_VALUE));
			modelbean.setDeviceType(IOTPayLoadConstants.RESTFUL_MODE_ERROR);
			modelbean.setOwnerId(mac_id);

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		String json = mapper.writeValueAsString(modelbean);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, json);
		
		IOTDataClientHub restClientHub = new IOTDataClientHub();
		restClientHub.doAsyncPost(IOT_ROOT_URL, "runningmodel", modelbean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);

		return modelbean;
	}

}
