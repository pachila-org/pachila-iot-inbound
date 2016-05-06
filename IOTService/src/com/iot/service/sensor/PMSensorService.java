package com.iot.service.sensor;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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


@Path("/sensor")
public class PMSensorService extends BaseService{
	private static final String CLASS_NAME = PMSensorService.class.getName();
	


	/*
	 *this is select from checkPM
	 */
	@GET
	@Path("/checkpm/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SelectcheckPM(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id) {
		final String METHOD_NAME =  "SelectcheckPM";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		StringBuffer errormsg = null;
		String publishmessage = null;
		boolean publishsuccessflag = false;
		// 
		// "" to it.
		String local_mac_id = mac_id.toLowerCase();
		String checktemperaturetopic = topic + local_mac_id;
		
		StringBuffer payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.CHECKPMSTATUS));
		

		// start

		// mqtt publish process

		MqttPublishConnection mqtttconnect = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout, Keepalive,channel);
		MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
		if (null != mqttClient) {
			MqttMessage returnMessage = new MqttMessage();
			publishmessage = PayLoadMessage.HandleMessage(
					Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.NUMBER)), payloadcontent.toString(),
					local_mac_id);
			returnMessage.setPayload(publishmessage.getBytes());
			publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, checktemperaturetopic, Qos, false);
			if (publishsuccessflag == false) {
				errormsg = new StringBuffer(
						ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
			}
		} else {

			errormsg = new StringBuffer(ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
		}
		// end
		ServiceCallResult pr = new ServiceCallResult();
		// 
		if (null != local_mac_id) {
			pr.setMacId(local_mac_id);
			pr.setStatus(IOTConstants.SUCCESS);
			pr.setReturnMag("");
			return Response.status(Response.Status.OK).entity(pr).build();
		}
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return Response.status(Response.Status.BAD_REQUEST).entity(pr).build();
	}

}
