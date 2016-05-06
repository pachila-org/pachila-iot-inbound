package com.iot.service.control;

import java.text.MessageFormat;

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

import com.iot.bean.ServiceCallResult;
import com.iot.bean.TimeSwitch;
import com.iot.configuration.EnvConfigurationManager;
import com.iot.configuration.ErrorMsgConstants;
import com.iot.configuration.ErrorMsgContentManager;
import com.iot.configuration.IOTConstants;
import com.iot.configuration.IOTPayLoadContentManager;
import com.iot.logging.IOTLogging;
import com.iot.mqtt.MqttPublishConnection;
import com.iot.service.BaseService;
import com.iot.utils.ChangeTohex;
import com.iot.utils.IsNumber;
import com.iot.utils.PayLoadMessage;

@Path("/control")
public class TimerControlService extends BaseService{
	//private final static int HEX_CONVERSION = 16;

	final String CLASS_NAME   =TimerControlService .class.getName();
	/*
	 * this is set timeswitch
	 */
	@POST
	@Path("/timeswitch/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SetTimeSwitch(@PathParam("mac_id") String mac_id,  @PathParam("user_id") String user_id, String SetTimeJson) {
		final String METHOD_NAME ="SetLightswitch";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		String local_mac_id = mac_id.toLowerCase();
		String remind = null;
		TimeSwitch timeswitch = null;
		StringBuffer payloadcontent = null;
		boolean publishsuccessflag = false;
		StringBuffer errormsg = null;
		String publishmessage = null;
		// fill the macid with the root topic
		String timetopic = topic + local_mac_id;
		// init the return result bean
		ServiceCallResult powerresult = new ServiceCallResult();
		powerresult.setStatus(IOTConstants.SUCCESS);
		powerresult.setReturnMag("");
		try {
			// set the time switch bean from json
			ObjectMapper objectMapper = new ObjectMapper();
			timeswitch = objectMapper.readValue(SetTimeJson, TimeSwitch.class);
			local_mac_id = timeswitch.getMacId().toLowerCase();
			powerresult.setMacId(local_mac_id);
			remind = timeswitch.getTimeRemind();
			System.out.println("remind==="+remind);
			if (null != timeswitch) {
				// get the time control value
				if (IOTConstants.TIMENO.equals(timeswitch.gettimeStatus().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.TIMENO));
				} else if (IOTConstants.TIMEYES.equals(timeswitch.gettimeStatus().toUpperCase())) {
					
					if (!IsNumber.isNumeric(remind)) {
						errormsg = new StringBuffer(
								ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_TIME_UNNUMBER_FLAG));
						powerresult.setStatus(IOTConstants.FAILURE);
						powerresult.setReturnMag(errormsg.toString());
						return Response.status(Response.Status.BAD_REQUEST).entity(powerresult).build();
					}
					if (Integer.parseInt(remind) < Integer.parseInt(EnvConfigurationManager
							.getParameterValue(IOTConstants.TIMEMIN))
							|| Integer.parseInt(remind) > Integer.parseInt(EnvConfigurationManager
									.getParameterValue(IOTConstants.TIMEMAX))) {
						errormsg = new StringBuffer(
								ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_TIME_UNRECOGNIZED_FLAG));
						powerresult.setStatus(IOTConstants.FAILURE);
						powerresult.setReturnMag(errormsg.toString());
						return Response.status(Response.Status.BAD_REQUEST).entity(powerresult).build();
					}
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.TIMEYES));
					//  this (remind)*60) is not best and now change to mins way
					//payloadcontent = new StringBuffer(MessageFormat.format(payloadcontent.toString(),
					//		ChangeTohex.tohex(Integer.parseInt(remind) * 60),ChangeTohex.changestr));
					String remindHexValue= ChangeTohex.tohex(Integer.parseInt(remind));
					//int command_no_int = Integer.toHexString(Integer.parseInt(remind));
					int frame_data_low_int  = Integer.parseInt(remind) & 0xff;
					int frame_data_low_high = Integer.parseInt(remind) >>8;
					String chksum = ChangeTohex.chksumHex(7,frame_data_low_high,frame_data_low_int);
					
					//payloadcontent = new StringBuffer(MessageFormat.format(payloadcontent.toString(),
					//		ChangeTohex.tohex(Integer.parseInt(remind) ),ChangeTohex.changestr));
					payloadcontent = new StringBuffer(MessageFormat.format(payloadcontent.toString(),
							remindHexValue,chksum));
				}

				else {
					// the time switch is either on or off
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_TIME_SWITCH_FLAG));

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
					publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, timetopic, Qos, false);
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
	
	// this is select from timeswitch
		@GET
		@Path("/timeswitch/{mac_id}/{user_id}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		//  this select(GET) logic do not write.the same as powerstatus GET
		public Response Selecttimestatus(@PathParam("mac_id") String mac_id,@PathParam("user_id") String user_id) {
			final String METHOD_NAME = CLASS_NAME + "Selecttimestatus";
			final String ACTION_USER_ID = ACTION_MSG + user_id;
			IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
			IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
			IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
			StringBuffer errormsg = null;
			String publishmessage = null;
			boolean publishsuccessflag = false;
			//  beacuse of when we test,mac_id start with ESP_.we replace with
			// "" to it.
			String local_mac_id = mac_id.toLowerCase();
			String timetopic = topic + local_mac_id;
			//System.out.println("mac_id===" + local_mac_id);
			StringBuffer payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.TIMESTATUS));
			//System.out.println(payloadcontent + "from ClientSub  ::I am POWER_STATUS--->" + local_mac_id);

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
				publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, timetopic, Qos, false);
				if (publishsuccessflag == false) {
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
				}
			} else {

				errormsg = new StringBuffer(ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
			}
			// end
			ServiceCallResult pr = new ServiceCallResult();
			//  the check null != mac_id is not best
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
