package com.iot.datahubservice;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.device.DataHubStream;
import com.iot.logging.IOTLogging;

public class IOTDataHub {
	private final static String CLASS_NAME = IOTDataHub.class.getName();

	public static java.util.Properties props = new java.util.Properties();
	private static MqttAsyncClient dataHubStreamClient;

	// start

	// end
	static IMqttToken dataHubStreamClienttoken;

	// end
	static {

		try {


		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		// With a valid set of arguments, the real work of
		// driving the client API can begin
		final String METHOD_NAME = CLASS_NAME;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);

		try {

			String url = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_IP) + ":"
					+ EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_PORT);
			String topic = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_UPSTREAM_TOPIC);
			String clientId = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_UPSTREAM_CLIENTID);
			
			String username = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_USERNAME);
			String password = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_PASSWORD);
			String MQTT_IS_AUTH_CONNECT = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_IS_AUTH_CONNECT);

			int QoS = Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_CONTROL_QOS));
			DataHubStream dataHubStream  = new DataHubStream(url, clientId, false, false, null, null);
			
			MqttConnectOptions conOpt;
			conOpt = new MqttConnectOptions();
			conOpt.setCleanSession(false);
			if(MQTT_IS_AUTH_CONNECT.equals(IOTConstants.MQTT_AUTH_CONNECT_KEY)) {
				conOpt.setPassword(password.toCharArray());
				conOpt.setUserName(username);
			
			}

			dataHubStreamClient = dataHubStream.getMqttClient();

			dataHubStreamClienttoken = dataHubStreamClient.connect(conOpt);
			
			dataHubStreamClienttoken.waitForCompletion(35000);

			// client.connect();
			dataHubStreamClient.subscribe(topic, QoS);
			// 
			dataHubStreamClienttoken.waitForCompletion(35000);

		} catch (Exception th) {
			// System.out.println("Throwable caught " + th);
			// th.printStackTrace();
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, "Technical Exception Occured. " + th.getMessage(), th);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);

	}

}
