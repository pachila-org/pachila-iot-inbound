package com.iot.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "powerresult")
public class ServiceCallResult {
	private String macId;
	private String status;
	private String returnMessage;

	public String getMacId() {
		return macId;
	}

	public void setMacId(String macid) {
		this.macId = macid;
	}

	@XmlElement
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReturnMsg() {
		return returnMessage;
	}

	public void setReturnMag(String returnmessage) {
		this.returnMessage = returnmessage;
	}

}
