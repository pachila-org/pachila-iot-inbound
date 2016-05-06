package com.iot.device.controlstatus;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import com.iot.bean.IOTDataBean;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.util.IOTDataClientHub;





public class IOTDataHandle extends DataHubBase{
	
	private final static String CLASS_NAME =  IOTDataHandle.class.getName();
	private static IOTDataHandle iotDataHandle = null;

	private IOTDataHandle() {

	}

	public static IOTDataHandle getInstance() {
		if (null == iotDataHandle) {
			iotDataHandle = new IOTDataHandle();
		}
		return iotDataHandle;
	}

	
	//@SuppressWarnings("deprecation")
	@Override
	protected Object doExecute(Map<String, String> returnmap,String mac_id,Map<String,String> controlmap) throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		//String controlValue = (String)returnmap.get(IOTPayLoadConstants.RETURN_VALUE);
		String IOT_ROOT_URL = EnvConfigurationManager.getParameterValue(IOTConstants.IOT_ROOT_URL);
		IOTDataBean iotDataBean = new IOTDataBean();
		iotDataBean.setDevice_mac(returnmap.get("macid"));
		iotDataBean.setData(returnmap.get("getmessage"));
		
		// transfer the javabean to json
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        String json = mapper.writeValueAsString(iotDataBean);
        IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,json);

        IOTDataClientHub restClientHub= new IOTDataClientHub();
        restClientHub.doAsyncPost(IOT_ROOT_URL, "/DeviceAPI/uploadData", iotDataBean);
        

		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return json;
	}
}
