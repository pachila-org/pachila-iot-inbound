package com.iot.service;

import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;

public abstract class BaseService {

	protected final static String CLASS_NAME = BaseService.class.getName();
	protected static String ACTION_MSG= EnvConfigurationManager.getParameterValue(IOTConstants.ACTION_MSG);
	protected static String clientId = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_DOWNSTREAM_CLIENTID);
	protected static String brokerUrl = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_IP) + ":"
			+ EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_PORT);
	protected static String topic = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_DOWNSTREAM_TOPIC);
	protected static String smartLight = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_DOWNSTREAM_SMARTLIGHT_TOPIC);
	
	protected int Qos = Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_CONTROL_QOS));
	protected int Keepalive = Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.KEEPALIVE));
	protected int connectionTimeout = Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.CONNECTION_TIME_OUT));
	protected static String channel = "downstream";
	protected static String initErrorMsg = EnvConfigurationManager.getParameterValue(IOTConstants.INIT_ERROR_MSG);

}
