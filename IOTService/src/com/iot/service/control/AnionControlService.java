package com.iot.service.control;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.iot.bean.AnionSwitch;
import com.iot.bean.ServiceCallResult;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.ErrorMsgConstants;
import com.iot.configuration.ErrorMsgContentManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadContentManager;
import com.iot.logging.IOTLogging;
import com.iot.mqtt.MqttPublishConnection;
import com.iot.service.BaseService;
import com.iot.utils.PayLoadMessage;

@Path("/control")
public class AnionControlService extends BaseService {

	final String CLASS_NAME = AnionControlService.class.getName();

	/*
	 * this is about set on/off from anionstatus
	 */
	@POST
	@Path("/anionstatus/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SetAnionswitch(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id,
			String SetanionswitchJson) {
		final String METHOD_NAME = "SetAnionswitch";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		String local_mac_id = mac_id.toLowerCase();
		AnionSwitch anionswitch = null;
		StringBuffer payloadcontent = null;
		boolean publishsuccessflag = false;
		StringBuffer errormsg = null;
		String publishmessage = null;
		// fill the macid with the root topic
		String aniontopic = topic + local_mac_id;
		// init the return result bean
		ServiceCallResult powerresult = new ServiceCallResult();
		powerresult.setStatus(IOTConstants.SUCCESS);
		powerresult.setReturnMag("");

		try {
			// set the anion switch bean from json
			ObjectMapper objectMapper = new ObjectMapper();

			anionswitch = objectMapper.readValue(SetanionswitchJson, AnionSwitch.class);
			local_mac_id = anionswitch.getMacId().toLowerCase();
			powerresult.setMacId(local_mac_id);
			if (null != anionswitch) {
				// get the anion control value
				if (IOTConstants.ANIONSWITCHON.equals(anionswitch.getAnionStatus().toUpperCase())) {
					payloadcontent = new StringBuffer(
							IOTPayLoadContentManager.getParameterValue(IOTConstants.ANIONSWITCHON));

				} else if (IOTConstants.ANIONOFF.equals(anionswitch.getAnionStatus().toUpperCase())) {
					payloadcontent = new StringBuffer(
							IOTPayLoadContentManager.getParameterValue(IOTConstants.ANIONOFF));

				} else {
					// the anionswitch is either on or off
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_Anion_SWITCH_FLAG));

					powerresult.setMacId(powerresult.getMacId());
					powerresult.setStatus(IOTConstants.FAILURE);
					powerresult.setReturnMag(errormsg.toString());
					return Response.status(Response.Status.BAD_REQUEST).entity(powerresult).build();
				}
				// mqtt publish process

				MqttPublishConnection mqtttconnect = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout,
						Keepalive, channel);
				MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
				if (null != mqttClient) {
					MqttMessage returnMessage = new MqttMessage();
					publishmessage = PayLoadMessage.HandleMessage(
							Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.NUMBER)),
							payloadcontent.toString(), local_mac_id);
					returnMessage.setPayload(publishmessage.getBytes());
					publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, aniontopic, Qos, false);
					if (publishsuccessflag == false) {
						errormsg = new StringBuffer(ErrorMsgContentManager
								.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
					}
				} else {

					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
				}

			}
		} catch (JsonParseException e) {
			// the string split char is not property
			errormsg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNEXPECTED_CHARACTER));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME + " is error");
		} catch (JsonMappingException e) {
			// the json field is not same as javabean
			errormsg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNRECOGNIZED_FIELD));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME + " is error");
		} catch (Exception e) {

			errormsg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_SYS_UNEXPECTED_EXCEPTION));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME + " is error");
		} catch (Throwable e) {
			errormsg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_SYS_UNEXPECTED_EXCEPTION));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME + " is error");
		} finally {
			if (null != errormsg || publishsuccessflag == false) {
				powerresult.setStatus(IOTConstants.FAILURE);
				powerresult.setReturnMag(errormsg.toString());
				return Response.status(Response.Status.BAD_REQUEST).entity(powerresult).build();
			}

		}
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return Response.status(Response.Status.OK).entity(powerresult).build();
	}

	@GET
	@Path("/anionstatus/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response Selectanionstatus(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id) {
		final String METHOD_NAME = CLASS_NAME + "Selectanionstatus";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);

		String publishmessage = null;
		boolean publishsuccessflag = false;

		String local_mac_id = mac_id.toLowerCase();
		String aniontopic = topic + mac_id;
		// System.out.println("mac_id===" + mac_id);
		StringBuffer payloadcontent = new StringBuffer(
				IOTPayLoadContentManager.getParameterValue(IOTConstants.ANIONSTATUS));
		// System.out.println(payloadcontent + "from ClientSub ::I am
		// POWER_STATUS--->" + mac_id);

		// start

		// mqtt publish process

		MqttPublishConnection mqtttconnect = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout,
				Keepalive, channel);
		MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
		if (null != mqttClient) {
			MqttMessage returnMessage = new MqttMessage();
			publishmessage = PayLoadMessage.HandleMessage(
					Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.NUMBER)),
					payloadcontent.toString(), local_mac_id);
			returnMessage.setPayload(publishmessage.getBytes());
			publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, aniontopic, Qos, false);
			if (publishsuccessflag == false) {
				StringBuffer errormsg = new StringBuffer(
						ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
			}
		} else {

			StringBuffer errormsg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
		}
		// end
		ServiceCallResult pr = new ServiceCallResult();

		pr.setMacId(mac_id);
		pr.setStatus(IOTConstants.SUCCESS);
		pr.setReturnMag("");
		return Response.status(Response.Status.OK).entity(pr).build();

	}

}
