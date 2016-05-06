package com.iot.bean;

public class VOCSensor {
	
	private String vocValue;
	private String vocError;
	private String macId;
	private String ownerId;
	private String deviceVersion;
	private String deviceType;
	
	public String getVocValue() {
		return vocValue;
	}
	public void setVocValue(String vocValue) {
		this.vocValue = vocValue;
	}
	public String getVocError() {
		return vocError;
	}
	public void setVocError(String vocError) {
		this.vocError = vocError;
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
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
}
