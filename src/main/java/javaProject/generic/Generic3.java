package javaProject.generic;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Vector;

/**
 * 通过反射获取泛型的实际类型:hibernate sql实现返回值时就是通过这个反射返回用户所需要的泛型类型
 * 	需求：获取applyVector参数Vector<Date>中的泛型类型
 * @author 56525
 *
 */
public class Generic3 {

	public static void main(String[] args) throws Exception {
		// 演示获取applyVector中的泛型Date
		Method method = Generic3.class.getDeclaredMethod("applyVector", Vector.class);
		// 获取调用方法上的所有参数类型
		Type[] types = method.getGenericParameterTypes();
		// 获取方法中的所有参数
		for (int i = 0; i < types.length; i++) {
			ParameterizedType pType = (ParameterizedType) types[i];
			// 获取参数类型
			System.out.println(pType.getRawType());
			// 获取参数泛型类型
			System.out.println(pType.getActualTypeArguments()[i]);
		}
	}

	@SuppressWarnings("unused")
	private static void applyVector(Vector<Date> v1) {

	}
}
