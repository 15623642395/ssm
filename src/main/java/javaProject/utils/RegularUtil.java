package javaProject.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {
	/**
	 * 匹配所有{}的字符串个数
	 * @param args
	 */
	public static void main(String[] args) {
		String string = "{nihao1}{1}{n你好}{name}{name}";
		Pattern pattern = Pattern.compile("\\{[\\w]*\\||[\\W]*}");
		Matcher matcher = pattern.matcher(string);
		// 循环，字符串中有多少个符合的，就循环多少次
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		System.out.println(count);
	}
}
