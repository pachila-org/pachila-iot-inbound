package com.iot.device.controlstatus;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.iot.bean.DeviceOnlineStatusBean;
import com.iot.bean.MqttConfBean;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.exception.IOTTechnicalException;
import com.iot.logging.IOTLogging;
import com.iot.mqtt.MqttPublishConnection;
import com.iot.util.IOTDataClientHub;

public class DeviceOnlineStatus {
	private static final String CLASS_NAME = DeviceOnlineStatus.class.getName();
	private static DeviceOnlineStatus deviceOnlineStatus = null;

	private DeviceOnlineStatus() {
	}

	public static DeviceOnlineStatus getInstance() {
		if (null == deviceOnlineStatus) {
			deviceOnlineStatus = new DeviceOnlineStatus();
		}
		return deviceOnlineStatus;
	}

	public Object onlinestatus(String mac_id, String message,MqttConfBean mqttConfBean)
			throws IOTTechnicalException, IOException {
		final String METHOD_NAME = "controlstatus";
		String publishMessage = "";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		//String registerTopic = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_REGISTER_TOPIC);
		String topic = "";
		try {
			topic = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_REGISTER_TOPIC, mac_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String IOT_ROOT_URL = EnvConfigurationManager
				.getParameterValue(IOTConstants.IOT_ROOT_URL);

		DeviceOnlineStatusBean deviceOnlineStatusBean = new DeviceOnlineStatusBean();
		ObjectMapper objectMapper = new ObjectMapper();

		deviceOnlineStatusBean = objectMapper.readValue(message,
				DeviceOnlineStatusBean.class);
		// transfer the javabean to json
		// justify the device if online or not
		if(IOTConstants.DEVICE_ONLINE.equals(deviceOnlineStatusBean.getOnlinestatus())){
			
			MqttPublishConnection mqtttconnect = new MqttPublishConnection(mqttConfBean.getBrokerUrl(), mqttConfBean.getClientId(), mqttConfBean.getConnectionTimeout(), mqttConfBean.getKeepalive(),mqttConfBean.getChannel());
			MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
			if (null != mqttClient) {
				MqttMessage returnMessage = new MqttMessage();
				returnMessage.setPayload(publishMessage.getBytes());
				mqtttconnect.doPublish(mqttClient, returnMessage, topic, 0, true);
			}
		}

		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, message);
		if(IOTConstants.DEVICE_OFFLINE.equals(deviceOnlineStatusBean.getOnlinestatus())){
			//String jsoncall = "{\"mac_id\":\""+deviceOnlineStatusBean.getMac_id() +"\",\"online_status\":\""+deviceOnlineStatusBean.getOnlinestatus()+"\"}";
			String jsoncall = "{\"device_mac\":\""+deviceOnlineStatusBean.getMac_id() +"\"}";


			IOTDataClientHub restClientHub = new IOTDataClientHub();
			restClientHub.doAsyncPost(IOT_ROOT_URL, "/DeviceAPI/notifyOffline",jsoncall);
		}

		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return deviceOnlineStatusBean;
	}
}
