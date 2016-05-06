package com.iot.bean;

public class MqttConfBean {
	String brokerUrl;
	String clientId;
	int connectionTimeout;
	int Keepalive;
	String channel;
	public String getBrokerUrl() {
		return brokerUrl;
	}
	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	public int getKeepalive() {
		return Keepalive;
	}
	public void setKeepalive(int keepalive) {
		Keepalive = keepalive;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	
}
