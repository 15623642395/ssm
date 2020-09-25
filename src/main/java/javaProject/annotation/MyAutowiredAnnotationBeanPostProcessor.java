package javaProject.annotation;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 自定义后置处理器
 *    实现自定义注解值的注入
 *    AutowiredAnnotationBeanPostProcessor是处理属性赋值的后置处理器，详见IOC源码
 * @author 56525
 *
 */
@Component
public class MyAutowiredAnnotationBeanPostProcessor extends AutowiredAnnotationBeanPostProcessor {

	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean,
			String beanName) throws BeanCreationException {
		// 获取对象的模板class
		@SuppressWarnings("rawtypes")
		Class class1 = bean.getClass();
		// 获取模板中的属性
		Field[] field = class1.getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			// 设置field对非public修饰词的访问为true
			field[i].setAccessible(true);
			// 获取属性上的所有注解
			Annotation[] annotations = field[i].getDeclaredAnnotations();
			for (int j = 0; j < annotations.length; j++) {
				Annotation annotation = annotations[j];
				// 获取并解析注解@MyValue
				if (annotations[j].annotationType().equals(MyValue.class)) {
					if (!"".equals(((MyValue) annotation).value())) {
						// 获取@MyValue注解的value的值
						String value = ((MyValue) annotation).value();
						// 给标注有@MyValue注解的属性赋值
						try {
							field[i].set(bean, value);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return pvs;
	}
}
