package com.iot.bean;

public class OTAServiceBean {
	private String otaVersion;
	private String macId;
	private String otaServerUrl;
	private String otaServerIP;
	private int otaServerPort;
	private String otaFile;



	public void setOtaServerIP(String otaServerIP) {
		this.otaServerIP = otaServerIP;
	}
	public String getOtaServerIP() {
		return otaServerIP;
	}


	public void setOtaServerPort(int otaServerPort) {
		this.otaServerPort = otaServerPort;
	}
	public int getOtaServerPort() {
		return otaServerPort;
	}


	public void setOtaFile(String otaFile) {
		this.otaFile = otaFile;
	}
	public String getOtaFile() {
		return otaFile;
	}


	public String getOtaVersion() {
		return otaVersion;
	}

	public void setOtaVersion(String otaVersion) {
		this.otaVersion = otaVersion;
	}



	public String getMacId() {
		return macId;
	}
	public void setMacId(String macId) {
		this.macId = macId;
	}



	public String getOtaServerUrl() {
		return otaServerUrl;
	}
	public void setOtaServerUrl(String otaServerUrl) {
		this.otaServerUrl = otaServerUrl;
	}
}
