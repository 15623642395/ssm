package javaProject.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javaProject.proxy.advice.Advice;
import javaProject.proxy.advice.MyAdvice;
import javaProject.proxy.myAdvice.MyProxy;
import javaProject.proxy.myAdvice.MyProxyInteface;

/**
 * 
 * 需求：通过代理方式实现AOP的前置和后置通知
 * 
 * 注：动态代理类返回的实例是目标方法的一个实现接口类及其实现类的父类都可以
 * 
 * 	如：传入的目标是ArrayList，可以使用List接收，也可以用List的父类Collection接收
 * 		只能是父接口接收而不能使用父类接收如：AbstractList
 * 
 * @author 56525
 *
 */
public class JDKProxy2 {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {
		// 0、创建切面
		MyAdvice myAdvice = new MyAdvice();
		// 1、构建目标参数并校验目标参数是否有父接口
		ArrayList<String> target = new ArrayList<String>();
		if (checkIntaface(target)) {
			// 2、使用父类接口接收
			List list = (List) proxy(target, myAdvice);
			list.add("1");
			// System.out.println(list);
			System.out.println("**********************父接口接收完毕*****************");
			// 3、使用父类的父类接口接收
			Collection collection = (Collection) proxy(target, myAdvice);
			collection.add("2");
			// System.out.println(collection);
			System.out.println("**********************父接口的父接口接收完毕*****************");
		}

		System.out.println("------------------------使用任意一个自定义的类实现切面---------------------");
		MyProxy myProxy = new MyProxy();
		if (checkIntaface(myProxy)) {
			MyProxyInteface proxyInteface = (MyProxyInteface) proxy(myProxy, myAdvice);
			System.out.println(proxyInteface.sayHello());
		}
	}

	/**
	 * 动态实现AOP功能
	 * 
	 * @param target
	 * 	传入的需要实现的代理类
	 * @param advice
	 * 	aop切面需要做的事
	 * @return
	 * 	代理类的实现接口类
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object proxy(Object target, Advice advice) throws Exception {
		// 1、创建动态代理类
		Class clazzProxy1 = Proxy.getProxyClass(target.getClass().getClassLoader(), target.getClass().getInterfaces());
		// 2、获取动态代理类的构造方法：只有一个有参构造
		Constructor constructor = clazzProxy1.getDeclaredConstructor(InvocationHandler.class);
		// 3、根据有参构造实例化动态代理实例
		Object object = constructor.newInstance(new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				advice.beforMethod(method);
				advice.afterMethod(method);
				return method.invoke(target, args);
			}
		});
		return object;
	}

	/**
	 * 检验目标类是否存在父接口
	 * 	目标类没有实现的接口是不能获取代理实例的
	 * 
	 * @param object
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Boolean checkIntaface(Object object) {
		Class class1[] = object.getClass().getInterfaces();
		if (class1.length == 0) {
			return false;
		}
		return true;
	}
}
