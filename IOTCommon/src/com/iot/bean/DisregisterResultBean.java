/**
 * 
 */
package com.iot.bean;

/**
 * @author Administrator
 *
 */
public class DisregisterResultBean {
	/**
	 * 注销结果
	 */
	private String disregister_status;
	
	public String getDisregister_status() {
		return disregister_status;
	}

	public void setDisregister_status(String disregister_status) {
		this.disregister_status = disregister_status;
	}

	/**
	 * 设备号
	 */
	private String mac_id;
	
	/**
	 * 设备固件版本
	 */
	private String device_version;
	
	/**
	 * 设备类型
	 */
	private String device_type;
	
	/**
	 * 所有者
	 */
	private String owner_id;

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
