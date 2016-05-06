/**
 * 
 */
package com.iot.service.device;

import javax.ws.rs.Consumes;
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

import com.iot.bean.RegisterBean;
import com.iot.bean.RegisterResultBean;
import com.iot.bean.ServiceCallResult;
import com.iot.configuration.EnvConfigurationManager;
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
@Path("/deviceregister")
public class DeviceRegister extends BaseService{
	private static final String CLASS_NAME = DeviceRegister.class.getName();
	String registerTopicString = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_REGISTER_TOPIC);
	String disRegisterTopicString = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_DISREGISTER_TOPIC);
	
	/**
	 */
	@POST
	@Path("/register/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id, String registerString) throws Exception{
		final String METHOD_NAME = "register";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, registerString);
		String local_mac_id = mac_id.toLowerCase();
		RegisterResultBean registerResultBean = null;
		boolean publishSuccessFlag = false;
		boolean isRetain = false;
		StringBuffer errorMeg = null;
		String publishMessage = null;
		String disRegisterMessage = "";
		
		// fill the macid with the root topic

		String topic = EnvConfigurationManager.getParameterValue(registerTopicString, local_mac_id);
		String disRegisterTopic = EnvConfigurationManager.getParameterValue(disRegisterTopicString, local_mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, topic);
		// init the return result bean
		ServiceCallResult registerresult = new ServiceCallResult();
		registerresult.setStatus(IOTConstants.SUCCESS);
		registerresult.setReturnMag("");

		try {
			// set the register bean from json
			ObjectMapper objectMapper = new ObjectMapper();

			// 将json转换为RegisterResultBean对象
			registerResultBean = objectMapper.readValue(registerString, RegisterResultBean.class);
			local_mac_id = registerResultBean.getMac_id().toLowerCase();
			registerresult.setMacId(local_mac_id);
			if (null != registerResultBean) {
				RegisterBean bean = new RegisterBean();
				// get the register_status value
				// 设置设备注册成功时发送给设备MQTT报文内容，暂时发送ok以后修改
				if (IOTConstants.DEVICE_REGISTER_SUCCESS.equals(registerResultBean.getRegister_status().toUpperCase())) {
					bean.setRegisterStatus(IOTConstants.DEVICE_REGISTER_SUCCESS.toLowerCase());
					bean.setMacId(local_mac_id);
					publishMessage = objectMapper.writeValueAsString(bean);
					isRetain = true;
				// 设置设备注册失败时发送给设备MQTT报文内容，暂时发送notok以后修改	
				} else if (IOTConstants.DEVICE_REGISTER_FAIL.equals(registerResultBean.getRegister_status().toUpperCase())) {
					bean.setRegisterStatus(IOTConstants.DEVICE_REGISTER_FAIL.toLowerCase());
					bean.setMacId(local_mac_id);
					publishMessage = objectMapper.writeValueAsString(bean);
					//isRetain = true;
				} else {
					// the register_status is either success or fail
					errorMeg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_REGISTER_FLAG));

					// 返回失败原因
					registerresult.setMacId(registerresult.getMacId());
					registerresult.setStatus(IOTConstants.FAILURE);
					registerresult.setReturnMag(errorMeg.toString());
					return Response.status(Response.Status.BAD_REQUEST).entity(registerresult).build();
				}
				// mqtt publish process

				// 获取mqtt链接发送消息
				MqttPublishConnection mqtttconnect = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout, Keepalive,channel);
				MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
				
				MqttPublishConnection mqtttconnect_disregister = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout, Keepalive,channel);
				MqttAsyncClient mqttClient_disregister = mqtttconnect_disregister.MqttConnect();
				if (null != mqttClient) {
					MqttMessage returnMessage = new MqttMessage();
					returnMessage.setPayload(publishMessage.getBytes());
					
					MqttMessage disRegisterMqttMmsg = new MqttMessage();
					disRegisterMqttMmsg.setPayload(disRegisterMessage.getBytes());
					
					//disable the dis-register in-memory msg 
					publishSuccessFlag = mqtttconnect.doPublish(mqttClient, disRegisterMqttMmsg, disRegisterTopic, Qos, true);
					if(publishSuccessFlag == true){
						//set register to in-memory msg
						publishSuccessFlag = mqtttconnect_disregister.doPublish(mqttClient_disregister, returnMessage, topic, Qos, isRetain);
						
					}
					
					
					if (publishSuccessFlag == false) {
						errorMeg = new StringBuffer(
								ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
					}
				} else {

					errorMeg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
				}

			}
		} catch (JsonParseException e) {
			// the string split char is not property
			errorMeg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNEXPECTED_CHARACTER));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, errorMeg.toString());
		} catch (JsonMappingException e) {
			// the json field is not same as javabean
			errorMeg = new StringBuffer(ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_JSON_UNRECOGNIZED_FIELD));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, errorMeg.toString());
		} catch (Exception e) {

			errorMeg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_SYS_UNEXPECTED_EXCEPTION));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, errorMeg.toString());
		} catch (Throwable e) {
			errorMeg = new StringBuffer(
					ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_SYS_UNEXPECTED_EXCEPTION));
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, METHOD_NAME + errorMeg);
		} finally {
			if (null != errorMeg || publishSuccessFlag == false) {
				registerresult.setStatus(IOTConstants.FAILURE);
				registerresult.setReturnMag(errorMeg.toString());
				return Response.status(Response.Status.BAD_REQUEST).entity(registerresult).build();
			}

		}
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return Response.status(Response.Status.OK).entity(registerresult).build();
	}

}
