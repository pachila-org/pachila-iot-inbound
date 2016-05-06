/**
 * 
 */
package com.iot.bean;

/**
 * @author Administrator
 *
 */
public class QuietModeBean {

	private String quietmodeStatus;
	
	private String macId;
	
	private String deviceVersion;
	
	private String deviceType;
	
	private String ownerId;
	
	public String getQuietmodeStatus() {
		return quietmodeStatus;
	}

	public void setQuietmodeStatus(String quietmodeStatus) {
		this.quietmodeStatus = quietmodeStatus;
	}

	public String getMacId() {
		return macId;
	}

	public void setMacId(String macId) {
		this.macId = macId;
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

	public void setDevicetype(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}


}
