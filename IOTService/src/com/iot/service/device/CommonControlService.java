package com.iot.service.device;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.iot.bean.IOTDataBean;
import com.iot.bean.ServiceCallResult;
import com.iot.configuration.ErrorMsgConstants;
import com.iot.configuration.ErrorMsgContentManager;
import com.iot.configuration.IOTConstants;
import com.iot.logging.IOTLogging;
import com.iot.mqtt.MqttPublishConnection;
import com.iot.service.BaseService;

/**
 * @author 
 *
 */
@Path("/commoncontrolservice")
public class CommonControlService extends BaseService{
	
	private static final String CLASS_NAME = CommonControlService.class.getName();

	
	/**
	 */
	@POST
	@Path("/common/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response common(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id, String iotDataStr) throws Exception{
		final String METHOD_NAME =  "Common";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);

		mac_id = mac_id.toLowerCase();
		IOTDataBean iotDataBean = new IOTDataBean();
		//StringBuffer payloadcontent = null;
		boolean publishsuccessflag = false;
		StringBuffer errormsg = null;
		String publishmessage = null;
		// fill the macid with the root topic

		String smartLighttopic = smartLight + mac_id;
		// init the return result bean
		ServiceCallResult powerresult = new ServiceCallResult();
		powerresult.setStatus(IOTConstants.SUCCESS);
		powerresult.setReturnMag("");

		try {
			// set the childlock switch bean from json
			//ObjectMapper objectMapper = new ObjectMapper();
			//iotDataBean = objectMapper.readValue(iotDataStr, IOTDataBean.class);
			//local_mac_id = iotDataBean.getDevice_mac().toLowerCase();
			//powerresult.setMacId(local_mac_id); 
			System.out.println(iotDataStr);
			iotDataBean.setQos("1");
			int iotDataStrIndexStart = iotDataStr.indexOf("data");
			int iotDataStrIndexEnd = iotDataStr.indexOf("}");
			String iotDataSubStr = iotDataStr.substring(iotDataStrIndexStart + 6, iotDataStrIndexEnd + 2);
			//if (null != iotDataBean) {
				// get the childlock control value
				MqttPublishConnection mqtttconnect = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout, Keepalive,channel);
				MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
				if (null != mqttClient) {
					MqttMessage returnMessage = new MqttMessage();
					//publishmessage = iotDataBean.getData();
					publishmessage = iotDataSubStr;
					publishmessage = publishmessage.toLowerCase();
					returnMessage.setPayload(publishmessage.getBytes());
					publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, smartLighttopic, Integer.parseInt(iotDataBean.getQos()), false);
					if (publishsuccessflag == false) {
						errormsg = new StringBuffer(
								ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
					}
				} else {

					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
				}

			//}
		//} catch (JsonParseException e) {
		//	// the string split char is not property
		//	errormsg = new StringBuffer(
		//			ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNEXPECTED_CHARACTER));
		//	IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME + " is error");
		//} catch (JsonMappingException e) {
		//	// the json field is not same as javabean
		//	errormsg = new StringBuffer(ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNRECOGNIZED_FIELD));
		//	IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME + " is error");
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
}
