package com.iot.bean;

public class RegisterResultBean {

	private String register_status;

	private String mac_id;

	private String device_version;

	private String device_type;

	private String owner_id;

	public String getRegister_status() {
		return register_status;
	}

	public void setRegister_tatus(String register_status) {
		this.register_status = register_status;
	}

	public String getMac_id() {
		return mac_id;
	}

	public void setMac_id(String mac_id) {
		this.mac_id = mac_id;
	}

	public String getDevice_version() {
		return device_version;
	}

	public void setDevice_version(String device_version) {
		this.device_version = device_version;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	
	

}
