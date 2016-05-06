package com.iot.utils;

import com.iot.configuration.IOTConstants;

public class PayLoadMessage {
	public static String HandleMessage(int number,String payloadcontent, String mac_id) {
		String macString=mac_id;
		String HexString = "";
		String[] HexArray = payloadcontent.split("\\s+");
		if (macString.length() < number) {
			for (int i = mac_id.length(); i < number; i++) {
				
				macString = macString + "0";
			}
		}

		for (int num = 0; num < HexArray.length; num++) {
			HexString = HexString + HexArray[num];
		}
		String temp = HexString.replace(IOTConstants.MESSAGEPREFIX, "");
		
		return macString + "01" + temp + "01";
	}
}