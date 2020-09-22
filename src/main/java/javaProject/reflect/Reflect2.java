package javaProject.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 演示Class获取字节码中的Constructor构造方法
 * 
 * 根据构造方法的参数类型获取不同的构造方法
 * 
 * @author 56525
 *
 */
public class Reflect2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		Class class1 = Class.forName("javaProject.reflect.Person");
		// 获取无参构造
		Constructor constructor1 = class1.getConstructor();
		// 根据构造方法不同的参数列表获取对应的构造方法
		Constructor constructor2 = class1.getConstructor(int.class);
		Constructor constructor3 = class1.getConstructor(int.class, String.class);
		System.out.println(constructor1);
		System.out.println(constructor2);
		System.out.println(constructor3);
		System.out.println("******************************************************");
		// 获取所有的构造方法
		Constructor[] constructors = class1.getConstructors();
		for (int i = 0; i < constructors.length; i++) {
			System.out.println(constructors[i]);
		}
		System.out.println("******************************************************");
		/**
		 * 应用：通过构造方法创建该对象 
		 * 
		 * 传入相应的参数值，而不是参数类型
		 */
		Person person1 = (Person) constructor1.newInstance();
		Person person2 = (Person) constructor2.newInstance(1);
		Person person3 = (Person) constructor3.newInstance(1, "2");
		System.out.println(person1);
		System.out.println(person2);
		System.out.println(person3);
	}
}
