package com.iot.utils;

public class ChangeTohex {
	
	//private final static int HEX_CONVERSION = 16;
	public static String changestr="";
	public static String tohex(int x){
		 int tt=Integer.parseInt("07", 16);
		 StringBuffer stringbuffer=new StringBuffer(String.format("%#06X", x+tt));
		 changestr = stringbuffer.replace(2, 4, "").toString();
		String s=String.format("%#06X", x);
		return s;
	}
	
	
	public static String intToHex(int x){

		String s=String.format("%#06X", x);
		return s;
	}
	
	public static String chksumHex(int command_no,int frame_data_low_high,int frame_data_low_int){
		//int hexValue = Integer.valueOf(x, HEX_CONVERSION);//01
		int chksumInt = command_no + frame_data_low_high + frame_data_low_int;
		chksumInt = chksumInt & 0xff;
		String s=String.format("%#04X", chksumInt);
		return s;
	}
	
	

}
