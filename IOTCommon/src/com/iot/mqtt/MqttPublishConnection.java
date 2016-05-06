package com.iot.mqtt;

import java.util.Random;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.IOTConstants;
import com.iot.logging.IOTLogging;

public class MqttPublishConnection {
	private final static String CLASS_NAME = MqttPublishConnection.class.getName();
	private String brokerUrl;
	private String clientId;
	private int connectionTimeout;
	private int keepAliveInterval;
	private String channel;
	
	public MqttPublishConnection(String brokerUrl, String clientId,int connectionTimeout ,int keepAliveInterval,String channel){
		this.brokerUrl = brokerUrl;
		this.clientId = clientId;
		this.connectionTimeout = connectionTimeout;
		this.keepAliveInterval = keepAliveInterval;
		this.channel = channel;
		
	}
	
	public MqttAsyncClient MqttConnect() {
		final String METHOD_NAME = CLASS_NAME + "MqttConnect";
		MqttConnectOptions conOptions = new MqttConnectOptions();
		conOptions.setCleanSession(true);
		conOptions.setConnectionTimeout(connectionTimeout);
		conOptions.setKeepAliveInterval(keepAliveInterval);
		String MQTT_IS_AUTH_CONNECT = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_IS_AUTH_CONNECT);
		
		//get username & password
		String username = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_USERNAME);
		String password = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_PASSWORD);
		//set borker connect to user/password way
		if(MQTT_IS_AUTH_CONNECT.equals(IOTConstants.MQTT_AUTH_CONNECT_KEY)) {
			
			conOptions.setUserName(username);
			conOptions.setPassword(password.toCharArray());
		} 
		
		
		MqttAsyncClient mqttClient = null;
    	//disable file Persistence dataStore
		//String tmpDir = System.getProperty("java.io.tmpdir");
    	channel = channel + System.currentTimeMillis();
    	Random random1 = new Random(System.currentTimeMillis());
    	String randomLong_suffix = Long.toString(random1.nextLong());
        System.out.println(randomLong_suffix);
    	//clientId = clientId + "_" + System.currentTimeMillis();
    	clientId = clientId + "_" + System.nanoTime();
    	clientId = clientId + "_" + randomLong_suffix;
    	//System.out.println(channel);
    	System.out.println(clientId);
    	//System.out.println(tmpDir+channel);
    	//MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir+channel);
    	MemoryPersistence dataStore= new MemoryPersistence();
		try {
			mqttClient = new MqttAsyncClient(brokerUrl, clientId,dataStore);
			//client.connect(conOptions);
			IMqttToken conToken = mqttClient.connect(conOptions,null,null);
			conToken.waitForCompletion();
		} catch (MqttException e) {
			

			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, "------4");
			System.out.println(e.getCause().toString());
			e.printStackTrace();
			return null;
		}
		return mqttClient;
	}
	


	public boolean doPublish(MqttAsyncClient mqttClient,MqttMessage returnMessage, String topic,int qos,boolean retained ) {
		final String METHOD_NAME = CLASS_NAME + "doPublish";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, clientId + topic);
		MqttConnectOptions conOptions = new MqttConnectOptions();
		conOptions.setCleanSession(true);
		try {

			mqttClient.publish(topic, returnMessage.getPayload(), qos, retained);
			mqttClient.disconnect();
		} catch (MqttException e) {

			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME);
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME);
			e.printStackTrace();
			return false;
		} catch (Throwable e) {
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME);
			e.printStackTrace();
			
			return false;
		}
		System.out.println("End of publish " + clientId);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		
		return true;
	}
}
