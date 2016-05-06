package com.iot.util;

import java.util.concurrent.ExecutorService;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.iot.configuration.IOTConstants;
import com.iot.logging.IOTLogging;

public class IOTDataClientHub {
	
	private final static String CLASS_NAME =  IOTDataClientHub.class.getName();

	
	public void doAsyncPost(String rootUrl, String path, final Object json) {

		final String METHOD_NAME = "doAsyncPost";

		final String serviceNameURI = rootUrl + path;
		IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME, serviceNameURI);
		Thread t = new Thread() {
			@Override
			public void run() {
				
				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget target = client.target(serviceNameURI);
				
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
	
	
	/**
	 * Thread Pool Post 
	 * */
	public void executeThreadPost(String rootUrl,String path,final Object json, ExecutorService localfixedThreadPool){
		
		final String METHOD_NAME = "executeSyncPost";
        
        final String serviceURL = rootUrl + path;
        localfixedThreadPool.execute(new Runnable() {
        	 
    		@Override
    		public void run() {
    			IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,serviceURL);
				
				ResteasyClient client = new ResteasyClientBuilder().build();
				ResteasyWebTarget target = client.target(serviceURL);
				target.request().post(Entity.entity(json, MediaType.APPLICATION_JSON));
				client.close();

				IOTLogging.getInstance().info(CLASS_NAME, METHOD_NAME,IOTConstants.ASYNC_CALL_FINISH);
				IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
    		}
    	});

	}

}
