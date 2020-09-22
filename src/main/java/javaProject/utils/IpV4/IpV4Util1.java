package javaProject.utils.IpV4;

import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * 通过ip和子网掩码验证是否在同一网段
 * IP地址范围：
 * 0.0.0.0～255.255.255.255，包括了mask地址。
 *
 * 如何判断两个IP地址是否是同一个网段中:
 * 要判断两个IP地址是不是在同一个网段，就将它们的IP地址分别与子网掩码做与运算，得到的结果一网络号，如果网络号相同，就在同一子网，否则，不在同一子网。
 * 例：假定选择了子网掩码255.255.254.0，现在分别将上述两个IP地址分别与掩码做与运算，如下图所示：
 * 211.95.165.24  11010011 01011111 10100101 00011000
 * 255.255.254.0  11111111 11111111 11111110 00000000
 * 与的结果是:       11010011 01011111 10100100 00000000
 *
 * 211.95.164.78 11010011 01011111 10100100  01001110
 * 255.255.254.0 11111111 11111111 11111110  00000000
 * 与的结果是:      11010011 01011111 10100100  00000000
 * 可以看出,得到的结果(这个结果就是网络地址)都是一样的，因此可以判断这两个IP地址在同一个子网。
 * 
 * @author 56525
 *
 */
public class IpV4Util1 {
	// IpV4的正则表达式，用于判断IpV4地址是否合法
	private static final String IPV4_REGEX = "((\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3})";

	/**
	 * 
	 * 比较两个ip地址是否在同一个网段中，如果两个都是合法地址，两个都是非法地址时，可以正常比较；
	 * 如果有其一不是合法地址则返回false；
	 * 注意此处的ip地址指的是如“192.168.1.1”地址
	 * @return
	 */
	public static boolean checkSameSegment(String ip1, String ip2, int mask1, int mask2) {
		// 判断IPV4是否合法
		if (!ipV4Validate(ip1)) {
			return false;
		}
		if (!ipV4Validate(ip2)) {
			return false;
		}
		int ipValue1 = getIpV4Value(ip1);
		int ipValue2 = getIpV4Value(ip2);
		return (mask1 & ipValue1) == (mask2 & ipValue2);
	}

	/**
	 * 判断ipV4或者mask地址是否合法，通过正则表达式方式进行判断
	 * 
	 * @param ipv4
	 */
	public static boolean ipV4Validate(String ipv4) {
		return ipv4Validate(ipv4, IPV4_REGEX);
	}

	/**
	 * 
	 * @param addr
	 * @param regex
	 * @return
	 */
	private static boolean ipv4Validate(String addr, String regex) {
		if (addr == null) {
			return false;
		} else {
			return Pattern.matches(regex, addr.trim());
		}
	}

	/**
	 * @param ipOrMask
	 * @return
	 */
	public static int getIpV4Value(String ipOrMask) {
		byte[] addr = getIpV4Bytes(ipOrMask);
		int address1 = addr[3] & 0xFF;
		address1 |= ((addr[2] << 8) & 0xFF00);
		address1 |= ((addr[1] << 16) & 0xFF0000);
		address1 |= ((addr[0] << 24) & 0xFF000000);
		return address1;
	}

	/**
	 * @param ipOrMask
	 * @return
	 */
	public static byte[] getIpV4Bytes(String ipOrMask) {
		try {
			String[] addrs = ipOrMask.split("\\.");
			int length = addrs.length;
			byte[] addr = new byte[length];
			for (int index = 0; index < length; index++) {
				addr[index] = (byte) (Integer.parseInt(addrs[index]) & 0xff);
			}
			return addr;
		} catch (Exception e) {
		}
		return new byte[4];
	}

	public static void main(String[] args) throws UnknownHostException {
		String ip1 = "192.168.0.1";
		for (int i = 0; i < 256; i++) {
			String ip3 = "192.168.1.";
			String ip2 = ip3 + i;
			// 判断ip两个地址是否是同一个网段,传入子网掩码,子网掩码不同一定不在同一网段
			int mask1 = getIpV4Value("255.255.254.0");
			int mask2 = getIpV4Value("255.255.254.0");
			System.out.println("ip1和" + ip2 + "在同一个网段中？ " + (checkSameSegment(ip1, ip2, mask1, mask2)));
		}
	}
}