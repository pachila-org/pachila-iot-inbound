package com.iot.bean;

public class ChildLockSwitch {
	private String childlockStatus;
	private String macId;
	private String ownerId;
	private String deviceVersion;
	private String deviceType;

	public String getChildlockStatus() {
		return childlockStatus;
	}

	public void setChildlockStatus(String childlockStatus) {
		this.childlockStatus = childlockStatus;
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
