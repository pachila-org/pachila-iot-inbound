package com.iot.bean;

public class PMSensor {
	
	private String pmValue;
	private String pmStatus;
	private String macId;
	private String ownerId;
	private String deviceVersion;
	private String deviceType;
	private String pmError;
	
	public String getPmValue() {
		return pmValue;
	}
	public void setPmValue(String pmValue) {
		this.pmValue = pmValue;
	}
	public String getPmStatus() {
		return pmStatus;
	}
	public void setPmStatus(String pmStatus) {
		this.pmStatus = pmStatus;
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
	public String getPmError() {
		return pmError;
	}
	public void setPmError(String pmError) {
		this.pmError = pmError;
	}

	

}
