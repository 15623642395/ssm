package javaProject.reflect;

import java.lang.reflect.Method;

/**
 * 反射方式执行其他对象中的main方法
 * 
 * 静态方法不需要传对象
 * 
 * 注意：Reflect5和MainTest这两个类虽然在同一个java源文件中，
 * 
 * 但是编译之后会变为MainTest.class和Reflect5.class
 * 
 * @author 56525
 *
 */
public class Reflect5 {
	public static void main(String[] args) throws Exception {
		Method method = Class.forName("reflect.MainTest").getDeclaredMethod("main", String[].class);
		method.invoke(null, (Object) new String[] { "123", "456", "789" });
	}

}

class MainTest {
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
	}
}