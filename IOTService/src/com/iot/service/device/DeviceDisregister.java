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

import com.iot.bean.DisregisterBean;
import com.iot.bean.DisregisterResultBean;
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
public class DeviceDisregister extends BaseService{
	
	private static final String CLASS_NAME = DeviceDisregister.class.getName();
	String disRegisterTopic = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_DISREGISTER_TOPIC);
	String registerTopicString = EnvConfigurationManager.getParameterValue(IOTConstants.MQTT_REGISTER_TOPIC);
	
	/**
	 */
	@POST
	@Path("/disregister/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response disregister(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id, String disRegisterString) throws Exception{
		final String METHOD_NAME = "disregister";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, disRegisterString);
		String local_mac_id = mac_id.toLowerCase();
		DisregisterResultBean disRegisterResultBean = null;
		boolean publishSuccessFlag = false;
		StringBuffer errorMeg = null;
		String publishMessage = null;
		String registerMessage = "";
		
		// fill the macid with the root topic
		String topic = EnvConfigurationManager.getParameterValue(disRegisterTopic, local_mac_id);
		String registerTopic = EnvConfigurationManager.getParameterValue(registerTopicString, local_mac_id);
		// init the return result bean
		ServiceCallResult registerresult = new ServiceCallResult();
		registerresult.setStatus(IOTConstants.SUCCESS);
		registerresult.setReturnMag("");

		try {
			// set the register bean from json
			ObjectMapper objectMapper = new ObjectMapper();

			// 将json转换为DisregisterResultBean对象
			disRegisterResultBean = objectMapper.readValue(disRegisterString, DisregisterResultBean.class);
			local_mac_id = disRegisterResultBean.getMac_id().toLowerCase();
			registerresult.setMacId(local_mac_id);
			if (null != disRegisterResultBean) {
				
				// get the register_status value
				// 设置设备注册成功时发送给设备MQTT报文内容，暂时发送ok以后修改
				if (IOTConstants.DEVICE_DISREGISTER.equals(disRegisterResultBean.getDisregister_status().toUpperCase())) {
					DisregisterBean bean = new DisregisterBean();
					bean.setDisregister_status(IOTConstants.DEVICE_DISREGISTER.toLowerCase());
					bean.setMac_id(local_mac_id);
					publishMessage = objectMapper.writeValueAsString(bean);
				} else {
					// the register_status is either success or fail
					errorMeg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_DISREGISTER_FLAG));

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
					
					MqttMessage registerMqttMessage = new MqttMessage();
					registerMqttMessage.setPayload(registerMessage.getBytes());
					publishSuccessFlag = mqtttconnect.doPublish(mqttClient, registerMqttMessage, registerTopic, Qos, true);
					if(publishSuccessFlag == true){
						//set dis-register to in-memory msg 
						publishSuccessFlag = mqtttconnect_disregister.doPublish(mqttClient_disregister, returnMessage, topic, Qos, true);
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
