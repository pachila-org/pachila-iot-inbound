package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;







import com.iot.bean.PowerSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;





public class PowerSwitchStatus extends DataHubBase{
	
	private final static String CLASS_NAME =  PowerSwitchStatus.class.getName();
	private static PowerSwitchStatus powerControlStatus = null;

	private PowerSwitchStatus() {

	}

	public static PowerSwitchStatus getInstance() {
		if (null == powerControlStatus) {
			powerControlStatus = new PowerSwitchStatus();
		}
		return powerControlStatus;
	}

	
	//@SuppressWarnings("deprecation")
	@Override
	protected Object doExecute(Map<String, String> returnmap,String mac_id,Map<String,String> controlmap) throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String controlValue = (String)returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		PowerSwitch psbean = new PowerSwitch();
		if(controlValue.equals(controlmap.get(IOTPayLoadConstants.CONTROL_POWERON))){
			
			psbean.setMacId(mac_id);
			psbean.setPowerStatus(IOTPayLoadConstants.RESTFUL_POWER_ON);
			psbean.setDeviceVersion(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			psbean.setDeviceType(IOTPayLoadConstants.RESTFUL_POWER_ON);
			psbean.setOwnerId(mac_id);
	
		}
		//
		if(controlValue.equals(controlmap.get(IOTPayLoadConstants.CONTROL_POWEROFF))){
			
			psbean.setMacId(mac_id);
			psbean.setPowerStatus(IOTPayLoadConstants.RESTFUL_POWER_OFF);
			psbean.setDeviceVersion(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			psbean.setDeviceType(IOTPayLoadConstants.RESTFUL_POWER_OFF);
			psbean.setOwnerId(mac_id);

		}
		
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(psbean);
        IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,json);

        IOTDataClientHub restClientHub= new IOTDataClientHub();
        restClientHub.doAsyncPost(IOT_ROOT_URL, "powerstatus", psbean);
        

		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return psbean;
	}
}
