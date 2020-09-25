package javaProject.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 演示Class获取字节码中的Method
 * 
 * @author 56525
 *
 */
public class Reflect4 {
	public static void main(String[] args) throws Exception {
		String str = "abc";
		// 两个参数：方法名称，方法的参数class类型,如果第一个参数是null代表的则是静态方法
		Method method = String.class.getDeclaredMethod("charAt", int.class);
		// str为传入的对象，1为参数
		System.out.println(method.invoke(str, 1));
	}
}

class TestClassLoad {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
		Method method = TestClassLoad.class.getMethod("getName", String.class);
		Object object = TestClassLoad.class.newInstance();
		//传入实例化对象object和参数
		System.out.println(method.invoke(object, "abd"));
	}

	public static String getName(String name) {
		return name;
	}
}
