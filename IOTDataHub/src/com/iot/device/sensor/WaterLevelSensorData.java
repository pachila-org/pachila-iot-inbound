package com.iot.device.sensor;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.WaterLevelSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadConstants;
import com.iot.device.controlstatus.DataHubBase;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;


public class WaterLevelSensorData extends DataHubBase{
	
	private final static String CLASS_NAME =  WaterLevelSensorData.class.getName();
	private static WaterLevelSensorData waterLevelSensorData = null;

	private WaterLevelSensorData() {

	}

	public static WaterLevelSensorData getInstance() {
		if (null == waterLevelSensorData) {
			waterLevelSensorData = new WaterLevelSensorData();
		}
		return waterLevelSensorData;
	}

	@Override
	protected Object doExecute(Map<String, String> returnmap,String mac_id,Map<String,String> controlmap) throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String waterlevelSensordata = (String)returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		WaterLevelSwitch waterlevelbean = new WaterLevelSwitch(); 
		// water level is above the level
		if(waterlevelSensordata.equals(controlmap.get(IOTPayLoadConstants.CONTROL_WATRELEVELON))){
			
			waterlevelbean.setMacId(mac_id);
			waterlevelbean.setWaterStatus("waterabovelevel");
			waterlevelbean.setDeviceVersion(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			waterlevelbean.setdeviceType("Watertypeon");
			waterlevelbean.setOwnerId("1");

		}
		// water level is under the level
		if(waterlevelSensordata.equals(controlmap.get(IOTPayLoadConstants.CONTROL_WATRELEVELOFF))){
			
			waterlevelbean.setMacId(mac_id);
			waterlevelbean.setWaterStatus("waterunderlevel");
			waterlevelbean.setDeviceVersion(returnmap.get(IOTPayLoadConstants.RETURN_VALUE));
			waterlevelbean.setdeviceType("Watertypeoff");
			waterlevelbean.setOwnerId("1");
		}
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(waterlevelbean);
        IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,json);

        IOTDataClientHub restClientHub= new IOTDataClientHub();
        restClientHub.doAsyncPost(IOT_ROOT_URL, "water", waterlevelbean);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return waterlevelbean;
	}


}
