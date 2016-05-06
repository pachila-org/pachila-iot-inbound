package com.iot.bean;

public class AQISensorValue {
	private String aqiValue;
	private String aqiError;
	private String macId;
	private String ownerId;
	private String deviceVersion;
	private String deviceType;

	public String getAqiValue() {
		return aqiValue;
	}

	public void setaqiValue(String aqiValue) {
		this.aqiValue = aqiValue;
	}

	public String getAqiError() {
		return aqiError;
	}

	public void setAqiError(String aqiError) {
		this.aqiError = aqiError;
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
