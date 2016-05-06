package com.iot.device;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

import com.iot.bean.DeviceOnlineStatusBean;
import com.iot.bean.MqttConfBean;
import com.iot.bean.OTAMessage;
import com.iot.configuration.ControlMapContentManager;
import com.iot.configuration.ErrorMsgConstants;
import com.iot.configuration.ErrorMsgContentManager;
import com.iot.device.controlstatus.DataHubBase;
import com.iot.device.controlstatus.DeviceOnlineStatus;
import com.iot.device.controlstatus.IOTDataHandle;
import com.iot.device.controlstatus.OTAVersionStatus;
import com.iot.logging.IOTLogging;

public class DataHubStream implements MqttCallback {

	private final static String CLASS_NAME = DataHubStream.class.getName();
	private String brokerUrl = null;
	private String clientId = null;
	private boolean cleanSession = false;
	private String userName = null;
	private String password = null;
	private MqttConnectOptions conOpt;
	private MqttAsyncClient mqttClient;
	Map<String, String> controlMap;
	public static java.util.Properties props = new java.util.Properties();
	private MqttConfBean mqttConfBean;

	// start

	// end
	static IMqttToken dataHubStreamClienttoken;

	public DataHubStream(String brokerUrl, String clientId, boolean cleanSession, boolean quietMode, String userName,
			String password) {
		this.brokerUrl = brokerUrl;
		this.cleanSession = cleanSession;
		this.password = password;
		this.userName = userName;
		this.clientId = clientId;
	}

	public MqttAsyncClient getMqttClient() {

		final String METHOD_NAME = "getMqttClient";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);

		controlMap = ControlMapContentManager.getPayLoadContent();
		mqttConfBean = new MqttConfBean();
		mqttConfBean.setBrokerUrl(brokerUrl);
		mqttConfBean.setClientId(clientId);
		// mqttConfBean.setKeepalive(keepalive);
		// mqttConfBean.setConnectionTimeout(connectionTimeout);

		String tmpDir = System.getProperty("java.io.tmpdir");
		MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir + "/upstream");

		try {
			// Construct the object that contains connection parameters
			// such as cleanSession and LWT
			// START DISABLE THE AUTH INSIDE THE Hub
			// conOpt = new MqttConnectOptions();
			// conOpt.setCleanSession(cleanSession);
			// if (password != null) {
			// conOpt.setPassword(this.password.toCharArray());
			// }
			// if (userName != null) {
			// conOpt.setUserName(this.userName);
			// }
			// END DISABLE THE AUTH INSIDE THE Hub
			// Construct the MqttClient instance
			mqttClient = new MqttAsyncClient(this.brokerUrl, clientId, dataStore);
			mqttClient.setCallback(this);
		} catch (MqttException e) {
			e.printStackTrace();
			System.err.println("DataHubStream_getMqttClient_MqttException_Exception");
			// System.exit(1);
		}
		return mqttClient;
	}

	public void connectionLost(Throwable cause) {
		final String METHOD_NAME = "connectionLost";
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, "DataHubStream_Connection Lost");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		final String METHOD_NAME = "deliveryComplete";
		try {
			IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,
					"DataHubStream_deliveryComplete_Throwable_deliveryComplete delivery Complete");
		} catch (Exception e) {

			e.printStackTrace();
			System.err.println("DataHubStream_deliveryComplete_Exception_Error");
		} catch (Throwable e) {
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME,
					"DataHubStream_GdeliveryComplete_Throwable_deliveryComplete is error");
			e.printStackTrace();
			System.err.println("DataHubStream_deliveryComplete_Throwable_Error");
		}
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {

		final String METHOD_NAME = "messageArrived";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		String[] HexArray = topic.split("/");
		String inner_mac_ids = HexArray[HexArray.length - 1];
		String getmessage = message.toString();
		String action_flag = HexArray[HexArray.length - 1];
		Map<String, String> returnmap = new HashMap<String, String>();
		StringBuffer errormsg = null;

		// ExecutorService fixedThreadPool = Executors.newFixedThreadPool(300);

		try {

			String inner_mac_id = HexArray[HexArray.length - 2];

			if (action_flag.equals("ota")) {

				ObjectMapper objectMapper = new ObjectMapper();

				OTAMessage OTAbean = new OTAMessage();
				OTAbean = objectMapper.readValue(getmessage, OTAMessage.class);

				OTAVersionStatus otaControlStatus = OTAVersionStatus.getInstance();
				OTAbean = (OTAMessage) otaControlStatus.controlstatus(inner_mac_id, OTAbean);
				IOTLogging.getInstance().debug(CLASS_NAME, METHOD_NAME, OTAbean.getOta_version());
			} else if (action_flag.equals("onlinestatus")) {

				DeviceOnlineStatus deviceOnlineStatus = DeviceOnlineStatus.getInstance();
				DeviceOnlineStatusBean deviceOnlineStatusBean = (DeviceOnlineStatusBean) deviceOnlineStatus
						.onlinestatus(inner_mac_id, getmessage, mqttConfBean);

				IOTLogging.getInstance().debug(CLASS_NAME, METHOD_NAME, deviceOnlineStatusBean.getOnlinestatus());

			} else {

				if (getmessage != null && getmessage.startsWith(inner_mac_ids)) {

					returnmap.put("getmessage", getmessage);
					returnmap.put("macid", inner_mac_ids);
					DataHubBase iotDataHandle = IOTDataHandle.getInstance();
					iotDataHandle.executeDataLoading(returnmap, inner_mac_ids, controlMap);

				}
			}
		} catch (JsonParseException e) {
			// the string split char is not property
			errormsg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNEXPECTED_CHARACTER));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, errormsg.toString());
		} catch (JsonMappingException e) {
			// the json field is not same as javabean
			errormsg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNRECOGNIZED_FIELD));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, errormsg.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("DataHubStream_messageArrived_Exception_Error");

		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println("DataHubStream_messageArrived_Throwable_Error");

		}
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
	}
}
