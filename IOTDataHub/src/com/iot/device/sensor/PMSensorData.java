package com.iot.device.sensor;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.PMSensor;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.device.controlstatus.DataHubBase;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;


public class PMSensorData extends DataHubBase{
	
	private final static String CLASS_NAME =  PMSensorData.class.getName();
	private static PMSensorData pmSensorData = null;

	private PMSensorData() {
		

	}

	public static PMSensorData getInstance(ExecutorService fixedThreadPool) {
		if (null == pmSensorData) {
			pmSensorData = new PMSensorData();
			
		}
		return pmSensorData;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,String mac_id,Map<String,String> controlmap) throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String vocSensordata = (String)returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		PMSensor pmSensorbean=new PMSensor();
		if(vocSensordata.equals(controlmap.get(IOTPayLoadConstants.DEVICE_ERROR))){
			pmSensorbean.setDeviceType("pmSensor");
			pmSensorbean.setDeviceVersion("1");
			pmSensorbean.setMacId(mac_id);
			pmSensorbean.setOwnerId("2");
			pmSensorbean.setPmValue(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			pmSensorbean.setPmError("error");

		}
		else if(vocSensordata.equals(controlmap.get(IOTPayLoadConstants.DEVICE_NOTREADY))){

			pmSensorbean.setDeviceType("pmSensor");
			pmSensorbean.setDeviceVersion("1");
			pmSensorbean.setMacId(mac_id);
			pmSensorbean.setOwnerId("2");
			pmSensorbean.setPmValue(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			pmSensorbean.setPmError("no check");

		} else {
			pmSensorbean.setDeviceType("pmSensor");
			pmSensorbean.setDeviceVersion("1");
			pmSensorbean.setMacId(mac_id);
			pmSensorbean.setOwnerId("2");
			pmSensorbean.setPmValue(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			pmSensorbean.setPmError("ok");

		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(pmSensorbean);
        IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,json);
        
        IOTDataClientHub restClientHub= new IOTDataClientHub();
        restClientHub.doAsyncPost(IOT_ROOT_URL, "pm", pmSensorbean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return pmSensorbean;
	}


}
