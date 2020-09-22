package javaProject.utils;

/**
 * 判断中文字符及中文
 * 
 * @author 56525
 *
 */
public class ChineseUtil {
	/**
	 * 含有中文字符及中文则返回true
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		// 判断中文
		if (c >= 0x4E00 && c <= 0x9FA5) {
			return true;
		}
		// 判断中文字符
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		String str = "";// 中文汉字
		int count = 0;
		for (char c : str.toCharArray()) {
			if (isChinese(c)) {
				count += 3;
			} else {
				count += 1;
			}
		}
		System.out.println("总共有：" + count + "个字符");
	}
}
