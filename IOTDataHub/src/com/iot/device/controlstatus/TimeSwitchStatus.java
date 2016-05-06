package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;








import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;







import com.iot.bean.TimeSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;


public class TimeSwitchStatus extends DataHubBase{
	
	private final static String CLASS_NAME =  TimeSwitchStatus.class.getName();
	private static TimeSwitchStatus timeControlStatus = null;

	private TimeSwitchStatus() {

	}

	public static TimeSwitchStatus getInstance() {
		if (null == timeControlStatus) {
			timeControlStatus = new TimeSwitchStatus();
		}
		return timeControlStatus;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,String mac_id,Map<String,String> controlmap) throws IOTTechnicalException, IOException {

		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		String timerControlStatus = (String)returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		TimeSwitch timebean = new TimeSwitch(); 

		//
		if(timerControlStatus.equals(controlmap.get(IOTPayLoadConstants.CONTROL_ANIONOFF))){
			
			timebean.setMacId(mac_id);
			timebean.setTimeStatus("timeno");
			timebean.setDeviceVersion(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			timebean.setdeviceType("Timetypeoff");
			timebean.setOwnerId("1");
			timebean.setTimeRemind("0");

		} else{
			timebean.setMacId(mac_id);
			timebean.setTimeStatus("timeyes");
			timebean.setDeviceVersion("1");
			timebean.setdeviceType("Timetypeon");
			timebean.setOwnerId("1");
			timebean.setTimeRemind(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(timebean);
        IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,json);
    
        IOTDataClientHub restClientHub= new IOTDataClientHub();
        restClientHub.doAsyncPost(IOT_ROOT_URL, "timer", timebean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return timebean;
	}


}
