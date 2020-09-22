package javaProject.proxy;

import org.springframework.cglib.proxy.Enhancer;

import javaProject.proxy.cglibProxy.HelloService;
import javaProject.proxy.cglibProxy.MyMethodInterceptor;
import javaProject.proxy.cglibProxy.advice.MyAdviceCGLIB;

/**
 * 
 * 使用CGLIB实现动态代理
 * 	1、CGLIB不需要目标类实现某种接口
 * 	2、CGLIB也可以实现某种接口只是，代理实例时用子类(实现类)接收，而不是像JDK动态代理需要父接口接收
 * 		详见：https://blog.csdn.net/yhl_jxy/article/details/80635012
 * 
 * @author 56525
 *
 */
public class CGLibProxy {

	public static void main(String[] args) {
		// 代理类class文件存入本地磁盘方便我们反编译查看源码
		// System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,
		// "D:\\code");
		// 通过CGLIB动态代理获取代理对象的过程
		Enhancer enhancer = new Enhancer();
		// 设置enhancer对象的父类即目标类
		enhancer.setSuperclass(HelloService.class);
		// 传入切面，设置enhancer的回调对象
		enhancer.setCallback(new MyMethodInterceptor(new MyAdviceCGLIB()));
		// 创建代理对象
		HelloService proxy = (HelloService) enhancer.create();
		// 通过代理对象调用目标方法
		proxy.sayHello();
		// final修饰的类不会被代理，不会执行切面方法
		proxy.sayOthers("我不会调用切面方法");
	}
}
