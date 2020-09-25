package javaProject.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 操作应用了注解类(MyValue)的类
 * 	1、创建注解MyValue，模拟实现Value注解
 * 	2、后置处理器MyAutowiredAnnotationBeanPostProcessor中实现属性的赋值
 * @author 56525
 *
 */
public class Test {
	// 启动IOC容器
	static AnnotationConfigApplicationContext applicationContext;

	public static void main(String[] args) {
		applicationContext = new AnnotationConfigApplicationContext(MyConfig1.class);
		Red red = (Red) applicationContext.getBean("red");
		System.out.println(red);
		red.setColor("黄色");
		red.setName("菊花");
		System.out.println(red);
	}
}
