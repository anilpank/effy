package org.effy.string;

public class StringUtils {

	static public boolean isEmpty(String s) {
		
		return s == null || s.trim().equals("");
	}
}
