package com.iot.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.iot.service.device.CommonControlService;
import com.iot.service.device.CommonDeviceService;
import com.iot.service.device.DeviceDisregister;
import com.iot.service.device.DeviceRegister;
import com.iot.service.sensor.OtaDeviceService;

public class IOTService extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public IOTService() {
		try {
			// handle device control related service ,such as power,light
			// ,childlock
			// anion control on/off
			
			singletons.add(new DeviceRegister());
			
			singletons.add(new DeviceDisregister());
			
			singletons.add(new OtaDeviceService());
			
			singletons.add(new CommonDeviceService());
			
			singletons.add(new CommonControlService());

		} catch (Exception e) {
			System.out.println("Top Error Catch");
			
		} catch (Throwable e) {
			// 
			e.printStackTrace();
		}

	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
