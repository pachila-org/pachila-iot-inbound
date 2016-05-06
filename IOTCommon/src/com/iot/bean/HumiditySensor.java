package com.iot.bean;

public class HumiditySensor {
	public String getHumidity_value() {
		return humidity_value;
	}
	public void setHumidity_value(String humidity_value) {
		this.humidity_value = humidity_value;
	}
	public String getHumidity_error() {
		return humidity_error;
	}
	public void setHumidity_error(String humidity_error) {
		this.humidity_error = humidity_error;
	}
	public String getMac_id() {
		return mac_id;
	}
	public void setMac_id(String mac_id) {
		this.mac_id = mac_id;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public String getDevice_version() {
		return device_version;
	}
	public void setDevice_version(String device_version) {
		this.device_version = device_version;
	}
	public String getdevice_type() {
		return device_type;
	}
	public void setdevice_type(String device_type) {
		this.device_type = device_type;
	}
	private String humidity_value;
	private String humidity_error;
	private String mac_id;
	private String owner_id;
	private String device_version;
	private String device_type;
	

}
