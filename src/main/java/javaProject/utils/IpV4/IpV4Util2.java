package javaProject.utils.IpV4;

/**
 * 判断IP是否在同一网段
 * 	通常来说：
 * 		子网掩码一样，只需要看.的前三部分，要是一样就会使同一网段
 * 		如IP为：192.168.10.0 子网掩码为：255.255.255.0
 * 		则：192.168.10.1到192.168.10.22跟上面都是一个网段
 * @author 56525
 *
 */
public class IpV4Util2 {

	/**
	 * 将ip转为二进制
	 * 
	 * @param address
	 * @return
	 */
	public static String getBinaryNumber(String address) {
		String[] networkAddressArray = address.split("\\.");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < networkAddressArray.length; i++) {
			String str = networkAddressArray[i];
			if (Integer.toBinaryString(Integer.parseInt(str)).length() < 8) {
				int num = 8 - Integer.toBinaryString(Integer.parseInt(str)).length();
				String ipStr1 = "";
				for (int j = 0; j < num; j++) {
					ipStr1 += "0";
				}
				sb.append(ipStr1 + (String) Integer.toBinaryString(Integer.parseInt(str)));
			} else {
				sb.append((String) Integer.toBinaryString(Integer.parseInt(str)));
			}
		}
		System.out.println("转换二进制:" + addTrim(sb.toString()));
		return sb.toString();
	}

	/**
	 * 将IP地址和mask地址进行&(与)运算
	 * 	计算规则：1和1为1,0和1为0,0和0为0
	 * 	注：ip和mask地址换算二二进制都是32位
	 * @param ip
	 * @param mask
	 * @return
	 */
	public static String ipMask(String ip, String mask) {
		String ipMask = "";
		for (int i = 0; i < ip.length(); i++) {
			String string1 = Character.toString(ip.charAt(i));
			String string2 = Character.toString(mask.charAt(i));
			if (string1.equals(string2) && "1".equals(string1)) {
				ipMask = ipMask + "1";
			} else {
				ipMask = ipMask + "0";
			}
		}
		System.out.println("计算二进制:" + addTrim(ipMask));
		return ipMask;
	}

	/**
	 * 将二进制字符串每8位给个空格
	 * @param string
	 * @return
	 */
	private static String addTrim(String string) {
		String regex = "(.{8})";
		string = string.replaceAll(regex, "$1 ");
		return string;
	}

	public static void main(String[] args) {
		// Ip
		String ip1 = getBinaryNumber("192.168.1.0");
		String ip2 = getBinaryNumber("192.168.2.1");
		// 子网掩码
		String mask = getBinaryNumber("255.255.255.0");
		String ipMask1 = ipMask(ip1, mask);
		String ipMask2 = ipMask(ip2, mask);
		if (ipMask1.equals(ipMask2)) {
			System.out.println("是同一网段");
		} else {
			System.out.println("不是同一网段");
		}
	}
}
