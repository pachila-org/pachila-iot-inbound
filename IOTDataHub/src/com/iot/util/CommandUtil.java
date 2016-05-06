package com.iot.util;

import java.util.HashMap;
import java.util.Map;

import com.iot.configuration.IOTPayLoadConstants;
import com.iot.logging.IOTLogging;


public class CommandUtil {

	private final static String CLASS_NAME =  CommandUtil.class.getName(); 
	
	private final static int HEX_CONVERSION = 16;
	/**
	 * check the command is right way or not
	 * */
	public static Map<String, String> commandcheck(String inputcommand) {
		
		final String METHOD_NAME = "commandcheck";
		IOTLogging.getInstance().entry(CLASS_NAME, METHOD_NAME);
		Map<String, String> returnmap = new HashMap<String, String>();
		// 55 06 0002 08 aa
		// 55 01 0001 02 aa
		// 55 05 ffff 04 aa
		String localcommand = inputcommand.substring(0, inputcommand.length() - 2);
		//String frame_head = "";
		String command_no = "";
		String frame_data_high = "";
		String frame_data_low = "";
		String frame_data = "";
		String chksum = "";
		//String frame_end = "";
		//Integer temp = null;
		String returnvalue = null;
		
		IOTLogging.getInstance().debug(CLASS_NAME, METHOD_NAME,localcommand);

		if (!localcommand.startsWith(IOTPayLoadConstants.COMMAND_FRAME_HEAD)
				|| !localcommand.endsWith(IOTPayLoadConstants.COMMAND_FRAME_END)) {
			IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME,"Command Head and End error "+localcommand);
			return null;
		} else {
			//frame_head = localcommand.substring(0, 2);// 55
			command_no = localcommand.substring(2, 4);// 01
			frame_data_high = localcommand.substring(4, 6);// 00
			frame_data_low = localcommand.substring(6, 8);// 01
			frame_data =  localcommand.substring(4, 8);//0001
			chksum = localcommand.substring(8, 10);// 02
			//frame_end = localcommand.substring(10, 12);// aa

			int command_no_int = Integer.valueOf(command_no, HEX_CONVERSION);//01
			int frame_data_low_int = Integer.valueOf(frame_data_low, HEX_CONVERSION);//01
			int frame_data_low_high = Integer.valueOf(frame_data_high, HEX_CONVERSION);//01
			int chksum_int = Integer.valueOf(chksum, HEX_CONVERSION);//02
			//int frame_data_int = Integer.valueOf(frame_data, HEX_CONVERSION);//02
			//get the 16bit low bit value to validate the check sum
			int low_correct_int = (command_no_int + frame_data_low_int + frame_data_low_high) & 0xff;
			//int low_correct_int = (command_no_int +frame_data_int) & 0xff;
			if (low_correct_int != chksum_int) {
				//System.out.println("chksum_int is null");
				IOTLogging.getInstance().error(CLASS_NAME, METHOD_NAME,"check sum error "+localcommand);
				IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
				return null;
			} else {
				returnvalue = String.valueOf(Integer.valueOf(frame_data, 16));
				//System.out.println("returnvalue===" + returnvalue);
				returnmap.put(IOTPayLoadConstants.COMMAND_ON, String.valueOf(command_no_int));//01
				returnmap.put(IOTPayLoadConstants.RETURN_VALUE, returnvalue);//0001
				IOTLogging.getInstance().exit(CLASS_NAME, METHOD_NAME);
				return returnmap;
			}

		}
	}

}
