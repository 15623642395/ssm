package javaProject.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**
	 * 匹配所有的:{中间任意字符或中文},的个数
	 * 	[\\u4e00-\\u9fa5]*匹配任意中文个数不限
	 * @param src
	 * @return
	 */
	public static int apperNum(String src) {
		int count = 0;
		Pattern pattern = Pattern.compile("\\{[\\w]*\\||[\\W]*\\||[\\u4e00-\\u9fa5]*}");
		Matcher matcher = pattern.matcher(src);
		while (matcher.find()) {
			count++;
		}
		return count;
	}
}
