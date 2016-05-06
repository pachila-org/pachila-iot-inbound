package com.iot.bean;

public class TemporatureSensor {
	private String temperatureValue;
	private String temperatureError;
	private String macId;
	private String ownerId;
	private String deviceVersion;
	private String deviceType;
	
	
	public String getTemperatureValue() {
		return temperatureValue;
	}
	public void setTemperature_value(String temperature_value) {
		this.temperatureValue = temperature_value;
	}
	public String getTemperatureError() {
		return temperatureError;
	}
	public void setTemperatureError(String temperature_error) {
		this.temperatureError = temperature_error;
	}
	public String getMacId() {
		return macId;
	}
	public void setMacId(String mac_id) {
		this.macId = mac_id;
	}

	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String owner_id) {
		this.ownerId = owner_id;
	}
	public String getDeviceVersion() {
		return deviceVersion;
	}
	public void setDeviceVersion(String device_version) {
		this.deviceVersion = device_version;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String device_type) {
		this.deviceType = device_type;
	}
	

}
