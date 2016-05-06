package com.iot.utils;

public class IsNumber {
	public static boolean isNumeric(String str) {
		boolean b=false;
		for (int i = 0; i<str.length();i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return b;
			}
		}
		b=true;
		return b;
	}
}
