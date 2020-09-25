package javaProject.utils;

/**
 * null转字符串
 * 
 * @author 56525
 *
 */
public class NullToStringUtil {
	public static String nullToString(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}

	}
}
