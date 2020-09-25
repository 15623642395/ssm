package javaProject.reflect;

import java.lang.reflect.Field;

/**
 * 演示Class获取字节码中的Field
 * @author 56525
 *
 */
public class Reflect3 {
	public static void main(String[] args) throws Exception {
		Person person = new Person(20, "张三");
		// 注意成员变量必须是public修饰的
		Field field1 = person.getClass().getField("name");
		// 获取属性值，获取哪个对象的值就传入哪个对象
		System.out.println(field1.get(person));

		// 因为age是private修饰的所以会报错
		// Field field2 = person.getClass().getField("age");

		// getDeclaredField不管是什么修饰词都可以获取到
		Field field2 = person.getClass().getDeclaredField("age");
		// private修饰的通过设置暴力反射才能field2.get(person)取值
		field2.setAccessible(true);
		System.out.println(field2.get(person));
		System.out.println("*****************************************");
		Reflect3 reflect3 = new Reflect3();
		reflect3.changeValue();
	}

	/**
	 * 需求：
	 *  将任意一个对象中的所有String类型的成员变量对应的字符串内容中的b改为a
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private void changeValue() throws Exception {
		Person person = new Person("bbbbb", 20, "bbbaaa", "bbbaaa");
		Class class1 = Person.class;
		Field[] field = class1.getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			field[i].setAccessible(true);
			// field[i].getType() == String.class这里用等号不要用equal，二进制用等号更准确
			// System.out.println(field[i].getType());
			if (field[i].getType() == String.class) {
				String value = field[i].get(person).toString().replace("b", "a");
				field[i].set(person, value);
			}
		}
		System.out.println(person);
	}
}
