package javaProject.reflect;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * 内省操作JavaBean
 * 	JavaBean是特殊的Java类
 * 	需求：  1、有一个类的路径，同时知道类的一个属性是x
 *        2、获取一个属性为x的值，并改变它的值
 * @author 56525
 *
 */
public class Reflect8 {
	public static void main(String[] args) throws Exception {
		// 使用无参构造创建对象
		Object object = Class.forName("javaProject.reflect.JavaBean").newInstance();
		String propertyName = "x";
		PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, object.getClass());
		// 获取x的getX()的返回值
		Method method = propertyDescriptor.getReadMethod();
		Object retVal = method.invoke(object);
		System.out.println(retVal);
		// 调用setX()设置值
		method = propertyDescriptor.getWriteMethod();
		method.invoke(object, 2);
		// 再次获取值
		method = propertyDescriptor.getReadMethod();
		retVal = method.invoke(object);
		System.out.println(retVal);

	}
}
