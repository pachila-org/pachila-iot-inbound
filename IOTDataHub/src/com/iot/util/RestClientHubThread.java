package com.iot.util;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.iot.configuration.IOTConstants;
import com.iot.logging.IOTLogging;

public class RestClientHubThread {

	private final static String CLASS_NAME = RestClientHubThread.class.getName();

	public void doAsyncPost(String rootUrl, String path, final Object json) {

		final String METHOD_NAME = "doAsyncPost";

		final String serviceURL = rootUrl + path;
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, serviceURL);
		Thread t = new Thread() {
			@Override
			public void run() {
				//serviceURL = rootUrl + path;
				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget target = client.target(serviceURL);
				// target.request().async().post(Entity.entity(json,
				// MediaType.APPLICATION_JSON));
				Response response = target.request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
				if (200 == response.getStatus()) {
					IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, IOTConstants.ASYNC_CALL_FINISH);
				} else {
					IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME, String.valueOf(response.getStatus()));
				}
				client.close();
			}
		};
		t.start();
		// syncInvoker.
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, IOTConstants.ASYNC_CALL_FINISH);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
	}

	public void doSyncPost(String rootUrl, String path, Object json) {

		final String METHOD_NAME = "doSyncPost";

		String serviceURL = rootUrl + path;

		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, serviceURL);

		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(serviceURL);
		target.request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
		client.close();

		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, IOTConstants.ASYNC_CALL_FINISH);
		IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
	}

}
