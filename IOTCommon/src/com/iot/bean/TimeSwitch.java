package com.iot.bean;

public class TimeSwitch {
	private String timeStatus;
	private String macId;
	private String ownerId;
	private String deviceVersion;
	private String deviceType;
	private String timeRemind;
	public String gettimeStatus() {
		return timeStatus;
	}
	public void setTimeStatus(String timeStatus) {
		this.timeStatus = timeStatus;
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
	public String getTimeRemind() {
		return timeRemind;
	}
	public void setTimeRemind(String timeRemind) {
		this.timeRemind = timeRemind;
	}
}
