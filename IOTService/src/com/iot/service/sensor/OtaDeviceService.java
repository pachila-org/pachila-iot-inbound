package com.iot.service.sensor;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.iot.bean.OTAServiceBean;
import com.iot.bean.ServiceCallResult;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.ErrorMsgConstants;
import com.iot.configuration.ErrorMsgContentManager;
import com.iot.configuration.IOTConstants;
import com.iot.logging.IOTLogging;
import com.iot.mqtt.MqttPublishConnection;
import com.iot.service.BaseService;

@Path("/device")
public class OtaDeviceService extends BaseService {
	private static final String CLASS_NAME = OtaDeviceService.class.getName();
	
	String OTATopic = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_OTA_TOPIC);
	/**
	 * this is select from checkOta post json 
	 * { "mac_id": "18fe349ab171",
	 * "ota_version": "0.1.1", "ota_server_ip": "192.168.1.228",
	 * "ota_server_port": 80, "ota_server_url": "/espressif/v1.0", "ota_file":
	 * "user1.1024.new.2.bin" }
	 */
	@POST
	@Path("/ota/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SetOta(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id, String SetOtaJson) throws Exception{
		final String METHOD_NAME = "SetOta";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		String local_mac_id = mac_id.toLowerCase();
		OTAServiceBean otamessage = null;
		String payloadcontent = null;
		boolean publishsuccessflag = false;
		StringBuffer errormsg = null;
		String publishmessage = null;

		// fill the mac_id with the root topic
		//String Otatopic = topic + mac_id + "/" + "ota";
		String OTAtopic = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_OTA_TOPIC, local_mac_id);
		// init the return result bean
		ServiceCallResult powerresult = new ServiceCallResult();
		powerresult.setStatus(IOTConstants.SUCCESS);
		powerresult.setReturnMag("");

		try {
			// set the OtaMessage bean from json
			ObjectMapper objectMapper = new ObjectMapper();

			otamessage = objectMapper.readValue(SetOtaJson, OTAServiceBean.class);
			if (null == otamessage)
			{
				errormsg = new StringBuffer(ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
				IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME + errormsg);
				powerresult.setStatus(IOTConstants.FAILURE);
				powerresult.setReturnMag(errormsg.toString());
				return Response.status(Response.Status.BAD_REQUEST).entity(powerresult).build();
				
			}
			local_mac_id = otamessage.getMacId().toLowerCase();

			powerresult.setMacId(local_mac_id);
			
			
			 
			if (null != otamessage) {

				ObjectMapper mapper = new ObjectMapper();
				mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
				mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT,
						true);
				String json = mapper.writeValueAsString(otamessage);
				json=json.replace("\r\n","");
				json=json.replaceAll(" ","");
				payloadcontent = json;
			}

			MqttPublishConnection mqtttconnect = new MqttPublishConnection(
					brokerUrl, clientId, connectionTimeout, Keepalive, channel);
			MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
			if (null != mqttClient) {
				MqttMessage returnMessage = new MqttMessage();
				publishmessage = payloadcontent;
				returnMessage.setPayload(publishmessage.getBytes());

				publishsuccessflag = mqtttconnect.doPublish(
						mqttClient, returnMessage, OTAtopic, Qos, false);
				if (publishsuccessflag == false) {
					errormsg = new StringBuffer(
							ErrorMsgContentManager
									.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
				}
			} else {

				errormsg = new StringBuffer(
						ErrorMsgContentManager
								.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
			}

		} catch (JsonParseException e) {
			// the string split char is not property
			errormsg = new StringBuffer(
					ErrorMsgContentManager
							.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNEXPECTED_CHARACTER));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME,
					METHOD_NAME + " is error");
		} catch (JsonMappingException e) {
			// the json field is not same as javabean
			errormsg = new StringBuffer(
					ErrorMsgContentManager
							.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNRECOGNIZED_FIELD));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME,
					METHOD_NAME + " is error");
		} catch (Exception e) {

			errormsg = new StringBuffer(
					ErrorMsgContentManager
							.getParameterValue(ErrorMsgConstants.ERROR_SYS_UNEXPECTED_EXCEPTION));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME,
					METHOD_NAME + " is error");
		} catch (Throwable e) {
			errormsg = new StringBuffer(
					ErrorMsgContentManager
							.getParameterValue(ErrorMsgConstants.ERROR_SYS_UNEXPECTED_EXCEPTION));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME,
					METHOD_NAME + " is error");
		} finally {
			if (null != errormsg || publishsuccessflag == false) {
				powerresult.setStatus(IOTConstants.FAILURE);
				powerresult.setReturnMag(errormsg.toString());
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(powerresult).build();
			}

		}
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return Response.status(Response.Status.OK).entity(powerresult).build();
	}
}