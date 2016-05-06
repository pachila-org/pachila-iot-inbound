package com.iot.bean;

public class LightSwitch {

	private String lightStatus;
	
	private String macId;
	private String ownerId;
	private String deviceVersion;
	private String deviceType;
	
	
	public String getLightStatus() {
		return lightStatus;
	}
	public void setLightStatus(String lightStatus) {
		this.lightStatus = lightStatus;
	}
	public String getMacId() {
		return macId;
	}
	public void setMacId(String macId) {
		this.macId = macId;
	}
	
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public String getDeviceVersion() {
		return deviceVersion;
	}
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}
	
	public String getdeviceType() {
		return deviceType;
	}
	public void setdeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	

}
