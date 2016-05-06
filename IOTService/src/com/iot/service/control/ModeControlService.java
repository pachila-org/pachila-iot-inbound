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

import com.iot.bean.RunModelSwitch;
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
public class ModeControlService extends BaseService{
	
	final String CLASS_NAME   =ModeControlService.class.getName();
	/*
	 * this is set runningmodel
	 */
	@POST
	@Path("/runningmodel/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SetRunModelSwitch(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id, String SetRunModelJson) {
		final String METHOD_NAME =  "SetRunModelSwitch";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		String local_mac_id = mac_id.toLowerCase();
		RunModelSwitch runmodelswitch = null;
		StringBuffer payloadcontent = null;
		boolean publishsuccessflag = false;
		StringBuffer errormsg = null;
		String publishmessage = null;
		// fill the macid with the root topic
		String runningmodeltopic = topic + local_mac_id;
		// init the return result bean
		ServiceCallResult powerresult = new ServiceCallResult();
		powerresult.setStatus(IOTConstants.SUCCESS);
		powerresult.setReturnMag("");
		try {
			// set the runningmodel switch bean from json
			ObjectMapper objectMapper = new ObjectMapper();
			runmodelswitch = objectMapper.readValue(SetRunModelJson, RunModelSwitch.class);
			local_mac_id = runmodelswitch.getMacId().toLowerCase();
			powerresult.setMacId(local_mac_id);
			if (null != runmodelswitch) {
				// get the runningmodel control value
				if (IOTConstants.RUNMODELAUTO.equals(runmodelswitch.getRunningmodel().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.RUNMODELAUTO));
				} else if (IOTConstants.RUNMODELSLEEP.equals(runmodelswitch.getRunningmodel().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.RUNMODELSLEEP));
				} else if (IOTConstants.RUNMODELSTRONG.equals(runmodelswitch.getRunningmodel().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.RUNMODELSTRONG));
				} else if (IOTConstants.RUNMODELHANDONE.equals(runmodelswitch.getRunningmodel().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.RUNMODELHANDONE));
				} else if (IOTConstants.RUNMODELHANDTWO.equals(runmodelswitch.getRunningmodel().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.RUNMODELHANDTWO));
				} else if (IOTConstants.RUNMODELHANDTHREE.equals(runmodelswitch.getRunningmodel().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.RUNMODELHANDTHREE));
				} else {
					// the runningmodel is either on or off so on
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_RUNMODEL_SWITCH_FLAG));

					powerresult.setMacId(powerresult.getMacId());
					powerresult.setStatus(IOTConstants.FAILURE);
					powerresult.setReturnMag(errormsg.toString());
					return Response.status(Response.Status.BAD_REQUEST).entity(powerresult).build();
				}
				// mqtt publish process

				MqttPublishConnection mqtttconnect = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout, Keepalive,channel);
				MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
				if (null != mqttClient) {
					MqttMessage returnMessage = new MqttMessage();
					publishmessage = PayLoadMessage.HandleMessage(
							Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.NUMBER)),
							payloadcontent.toString(), local_mac_id);
					returnMessage.setPayload(publishmessage.getBytes());
					publishsuccessflag = mqtttconnect
							.doPublish(mqttClient, returnMessage, runningmodeltopic, Qos, false);
					if (publishsuccessflag == false) {
						errormsg = new StringBuffer(
								ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
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
			errormsg = new StringBuffer(ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNRECOGNIZED_FIELD));
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
	

	// this is select from runningmodel
	@GET
	@Path("/runningmodel/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	// 
	public Response Selectmodelstatus(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id) {
		final String METHOD_NAME = CLASS_NAME + "Selectmodelstatus";
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
		String runmodeltopic = topic + local_mac_id;
		//System.out.println("mac_id===" + local_mac_id);
		StringBuffer payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.RUNMODELSTATUS));
		//System.out.println(payloadcontent + "from ClientSub  ::I am POWER_STATUS--->" + mac_id);

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
			publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, runmodeltopic, Qos, false);
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
		if (null != mac_id) {
			pr.setMacId(mac_id);
			pr.setStatus(IOTConstants.SUCCESS);
			pr.setReturnMag("");
			return Response.status(Response.Status.OK).entity(pr).build();
		}

		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return Response.status(Response.Status.BAD_REQUEST).entity(pr).build();
	}

	
}
