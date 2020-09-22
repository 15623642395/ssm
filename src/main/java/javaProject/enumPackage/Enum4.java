package javaProject.enumPackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 使用枚举定义数据状态
 * 	并可以用来检验传入值是否是枚举中定义的值
 * @author admin
 *
 */
@AllArgsConstructor
@Getter
public enum Enum4 {

	First, Second, Three, Four, Five;

	/**
	 * 可以用于校验传入的参数是否是枚举中定义的值
	 * @param type
	 * @return
	 */
	public boolean checkEnum(String type) {
		boolean flag = false;
		for (Enum4 enum4 : Enum4.values()) {
			if (enum4.name().equals(type)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

}
