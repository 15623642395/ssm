package javaProject.reflect;

/**
 * 演示Java反射
 * 
 * 1、获取Class对象的三种方式
 * 
 * @author 56525
 *
 */
public class Reflect1 {
	public static void main(String[] args) throws ClassNotFoundException {
		// 方式一
		@SuppressWarnings("rawtypes")
		Class class1 = Person.class;

		// 方式二
		Person person = new Person();
		@SuppressWarnings("rawtypes")
		Class class2 = person.getClass();

		// 方式三
		@SuppressWarnings("rawtypes")
		Class class3 = Class.forName("reflect.Person");

		System.out.println(class1 == class2);
		System.out.println(class1 == class3);
	}
}
