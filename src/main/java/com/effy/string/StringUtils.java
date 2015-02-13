package com.effy.string;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	static public boolean isEmpty(String s) {
		
		return s == null || s.trim().equals("");
	}
}
