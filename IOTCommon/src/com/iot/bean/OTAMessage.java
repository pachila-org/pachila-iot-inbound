package com.iot.bean;

public class OTAMessage {
	private String otaVersion;
	private String macId;
	private String currentBin;
	private String manufacturer;
	

	public String getMac_id() {
		return macId;
	}
	public void setMac_id(String macId) {
		this.macId = macId;
	}
	
	public String getOta_version() {
		return otaVersion;
	}

	public void setOta_version(String otaVersion) {
		this.otaVersion = otaVersion;
	}
	
	public String getCurrent_bin() {
		return currentBin;
	}

	public void setCurrent_bin(String currentBin) {
		this.currentBin = currentBin;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	

}
