/**
 * 
 */
package com.iot.bean;

/**
 * @author Administrator
 *
 */
public class DisregisterBean {
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
	
	public String getMac_id() {
		return mac_id;
	}

	public void setMac_id(String mac_id) {
		this.mac_id = mac_id;
	}

}
