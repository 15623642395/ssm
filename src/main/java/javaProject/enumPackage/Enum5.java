package javaProject.enumPackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 演示枚举，返回用户错误码和错误消息
 * 	注：枚举中有几个参数，就需要几个变量去接收参数值：两者必须一一对应
 * 		如QUERY_IS_NULL有两个参数："00001"和 "查询无结果！"
 * 		则需要两个变量去接收 code、message 变量名随意
 * 		默认第一个变量获取枚举中的第一个参数，依次类推
 * @author admin
 *
 */
@Getter
@AllArgsConstructor
public enum Enum5 {
	/**
	 * 查询结果为空
	 */
	QUERY_IS_NULL("00001", "查询无结果！"),
	/**
	 * 修改数据错误
	 */
	UPDATE_ERROR("00002", "数据编辑错误！");
	String code;
	String message;

	/**
	 * 测试返回
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Enum5.QUERY_IS_NULL);
		System.out.println(Enum5.QUERY_IS_NULL.getCode());
		System.out.println(Enum5.QUERY_IS_NULL.getMessage());
	}
}
