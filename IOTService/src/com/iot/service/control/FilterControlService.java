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

import com.iot.bean.FilterSwitch;
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
public class FilterControlService extends BaseService{

	final String CLASS_NAME   = FilterControlService.class.getName();
	/*
	 * this is set internetswitch
	 */
	@POST
	@Path("/filteroneswitch/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SetIntnetSwitch(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id, String SetInternetJson) {
		final String METHOD_NAME = "SetLightswitch";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		
		String local_mac_id = mac_id.toLowerCase();
		FilterSwitch internetswitch = null;
		StringBuffer payloadcontent = null;
		boolean publishsuccessflag = false;
		StringBuffer errormsg = null;
		String publishmessage = null;
		// fill the macid with the root topic
		String internettopic = topic + local_mac_id;
		// init the return result bean
		ServiceCallResult powerresult = new ServiceCallResult();
		powerresult.setStatus(IOTConstants.SUCCESS);
		powerresult.setReturnMag("");
		try {
			// set the internet switch bean from json
			ObjectMapper objectMapper = new ObjectMapper();
			internetswitch = objectMapper.readValue(SetInternetJson, FilterSwitch.class);
			local_mac_id = internetswitch.getMac_id().toLowerCase();
			powerresult.setMacId(local_mac_id);
			if (null != internetswitch) {
				// get the internet control value
				if (IOTConstants.FILTER.equals(internetswitch.getFilter_status().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.FILTER));
				} else {
					// the internetswitch is error
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_INTERNET_SWITCH_FLAG));

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
					publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, internettopic, Qos, false);
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

	// this is select from internet
	@GET
	@Path("/filteroneswitch/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SelectInternetstatus(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id) {
		final String METHOD_NAME = "SelectInternetstatus";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		
		StringBuffer errormsg = null;
		String publishmessage = null;
		boolean publishsuccessflag = false;
		String local_mac_id = mac_id.toLowerCase();
		String internettopic = topic + local_mac_id;
		System.out.println("mac_id===" + local_mac_id);
		StringBuffer payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.FILTERSTATUS));
		//System.out.println(payloadcontent + "from ClientSub  ::I am INTERNETSTATUS--->" + mac_id);
		// mqtt publish process
		ServiceCallResult pr = new ServiceCallResult();
		try {
			MqttPublishConnection mqtttconnect = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout, Keepalive,channel);
			MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
			if (null != mqttClient) {
				MqttMessage returnMessage = new MqttMessage();
				publishmessage = PayLoadMessage.HandleMessage(
						Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.NUMBER)), payloadcontent.toString(),
						local_mac_id);
				returnMessage.setPayload(publishmessage.getBytes());
				publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, internettopic, Qos, false);
				if (publishsuccessflag == false) {
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
				}
			} else {

				errormsg = new StringBuffer(
						ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
			}
			// end
//			pr.setMac_id(mac_id);
//			pr.setResult("1");
//			pr.setReturnMag("OK");

			
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
				pr.setStatus(IOTConstants.FAILURE);
				pr.setReturnMag(errormsg.toString());
				return Response.status(Response.Status.BAD_REQUEST).entity(pr).build();
			}
		}
		//start 6.1
		if (null != mac_id) {
			pr.setMacId(mac_id);
			pr.setStatus(IOTConstants.SUCCESS);
			pr.setReturnMag("");
			return Response.status(Response.Status.OK).entity(pr).build();
		}
		//end 6.1
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return Response.status(Response.Status.OK).entity(pr).build();
	}

	/*
	 * this is set internetswitch
	 */
	@POST
	@Path("/filtertwoswitch/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SetIntnetTwoSwitch(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id, String SetInternetJson) {
		final String METHOD_NAME = "SetIntnetTwoSwitch";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		String local_mac_id = mac_id.toLowerCase();
		FilterSwitch internetswitch = null;
		StringBuffer payloadcontent = null;
		boolean publishsuccessflag = false;
		StringBuffer errormsg = null;
		String publishmessage = null;
		// fill the macid with the root topic
		String internettopic = topic + local_mac_id;
		// init the return result bean
		ServiceCallResult powerresult = new ServiceCallResult();
		powerresult.setStatus(IOTConstants.SUCCESS);
		powerresult.setReturnMag("");
		try {
			// set the internet switch bean from json
			ObjectMapper objectMapper = new ObjectMapper();
			internetswitch = objectMapper.readValue(SetInternetJson, FilterSwitch.class);
			local_mac_id = internetswitch.getMac_id().toLowerCase();
			powerresult.setMacId(local_mac_id);
			if (null != internetswitch) {
				// get the internet control value
				if (IOTConstants.FILTER.equals(internetswitch.getFilter_status().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.FILTERTWO));
				} else {
					// the internetswitch is error
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_INTERNET_SWITCH_FLAG));

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
					publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, internettopic, Qos, false);
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

	// this is select from internet
	@GET
	@Path("/filtertwoswitch/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SelectInternetTwostatus(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id) {
		final String METHOD_NAME = "SelectInternetTwostatus";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		StringBuffer errormsg = null;
		String publishmessage = null;
		boolean publishsuccessflag = false;
		String local_mac_id = mac_id.toLowerCase();
		String internettopic = topic + local_mac_id;
		System.out.println("mac_id===" + local_mac_id);
		StringBuffer payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.FILTERTWOSTATUS));
		System.out.println(payloadcontent + "from ClientSub  ::I am INTERNETSTATUS--->" + mac_id);
		// mqtt publish process
		ServiceCallResult pr = new ServiceCallResult();
		try {
			MqttPublishConnection mqtttconnect = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout, Keepalive,channel);
			MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
			if (null != mqttClient) {
				MqttMessage returnMessage = new MqttMessage();
				publishmessage = PayLoadMessage.HandleMessage(
						Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.NUMBER)), payloadcontent.toString(),
						local_mac_id);
				returnMessage.setPayload(publishmessage.getBytes());
				publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, internettopic, Qos, false);
				if (publishsuccessflag == false) {
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
				}
			} else {

				errormsg = new StringBuffer(
						ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
			}
			// end
//			pr.setMac_id(mac_id);
//			pr.setResult("1");
//			pr.setReturnMag("OK");

			
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
				pr.setStatus(IOTConstants.FAILURE);
				pr.setReturnMag(errormsg.toString());
				return Response.status(Response.Status.BAD_REQUEST).entity(pr).build();
			}
		}
		//start 6.1
		if (null != mac_id) {
			pr.setMacId(mac_id);
			pr.setStatus(IOTConstants.SUCCESS);
			pr.setReturnMag("");
			return Response.status(Response.Status.OK).entity(pr).build();
		}
		//end 6.1
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return Response.status(Response.Status.OK).entity(pr).build();
	}
	
	/*
	 * this is set internetswitch
	 */
	@POST
	@Path("/filterthreeswitch/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SetIntnetThirdSwitch(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id, String SetInternetJson) {
		final String METHOD_NAME = "SetIntnetThirdSwitch";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		String local_mac_id = mac_id.toLowerCase();
		FilterSwitch internetswitch = null;
		StringBuffer payloadcontent = null;
		boolean publishsuccessflag = false;
		StringBuffer errormsg = null;
		String publishmessage = null;
		// fill the macid with the root topic
		String internettopic = topic + local_mac_id;
		// init the return result bean
		ServiceCallResult powerresult = new ServiceCallResult();
		powerresult.setStatus(IOTConstants.SUCCESS);
		powerresult.setReturnMag("");
		try {
			// set the internet switch bean from json
			ObjectMapper objectMapper = new ObjectMapper();
			internetswitch = objectMapper.readValue(SetInternetJson, FilterSwitch.class);
			local_mac_id = internetswitch.getMac_id();
			powerresult.setMacId(local_mac_id);
			if (null != internetswitch) {
				// get the internet control value
				if (IOTConstants.FILTER.equals(internetswitch.getFilter_status().toUpperCase())) {
					payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.FILTERTHIRD));
				} else {
					// the internetswitch is error
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_INTERNET_SWITCH_FLAG));

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
					publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, internettopic, Qos, false);
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

	// this is select from internet
	@GET
	@Path("/filterthreeswitch/{mac_id}/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response SelectInternetThirdstatus(@PathParam("mac_id") String mac_id, @PathParam("user_id") String user_id) {
		final String METHOD_NAME = "SelectInternetThirdstatus";
		final String ACTION_USER_ID = ACTION_MSG + user_id;
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, mac_id);
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, ACTION_USER_ID);
		StringBuffer errormsg = null;
		String publishmessage = null;
		boolean publishsuccessflag = false;
		String local_mac_id = mac_id.toLowerCase();
		String internettopic = topic + local_mac_id;
		System.out.println("mac_id===" + local_mac_id);
		StringBuffer payloadcontent = new StringBuffer(IOTPayLoadContentManager.getParameterValue(IOTConstants.FILTERTHIRDSTATUS));
		System.out.println(payloadcontent + "from ClientSub  ::I am INTERNETSTATUS--->" + mac_id);
		// mqtt publish process
		ServiceCallResult pr = new ServiceCallResult();
		try {
			MqttPublishConnection mqtttconnect = new MqttPublishConnection(brokerUrl, clientId, connectionTimeout, Keepalive,channel);
			MqttAsyncClient mqttClient = mqtttconnect.MqttConnect();
			if (null != mqttClient) {
				MqttMessage returnMessage = new MqttMessage();
				publishmessage = PayLoadMessage.HandleMessage(
						Integer.parseInt(EnvConfigurationManager.getParameterValue(IOTConstants.NUMBER)), payloadcontent.toString(),
						local_mac_id);
				returnMessage.setPayload(publishmessage.getBytes());
				publishsuccessflag = mqtttconnect.doPublish(mqttClient, returnMessage, internettopic, Qos, false);
				if (publishsuccessflag == false) {
					errormsg = new StringBuffer(
							ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
				}
			} else {

				errormsg = new StringBuffer(
						ErrorMsgContentManager.getParameterValue(ErrorMsgConstants.ERROR_MQTT_CONNECTION_ERROR));
			}
			// end
//			pr.setMac_id(mac_id);
//			pr.setResult("1");
//			pr.setReturnMag("OK");

			
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
				pr.setStatus(IOTConstants.FAILURE);
				pr.setReturnMag(errormsg.toString());
				return Response.status(Response.Status.BAD_REQUEST).entity(pr).build();
			}
		}
		//start 6.1
		if (null != mac_id) {
			pr.setMacId(mac_id);
			pr.setStatus(IOTConstants.SUCCESS);
			pr.setReturnMag("");
			return Response.status(Response.Status.OK).entity(pr).build();
		}
		//end 6.1
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
		return Response.status(Response.Status.OK).entity(pr).build();
	}
}
