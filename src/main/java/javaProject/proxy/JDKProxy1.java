package javaProject.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 需求：
 * 	通过接口方式实现动态代理Collection
 * 
 * 动态代理
 * 	1、JVM生成的动态类必须实现一个或多个接口，所以，JVM生成的动态类只能用作具有相同接口的目标类的代理。
 * 	2、不是所有类都有接口，此时可通过第三方库CGLIB库可以动态生成一个类的子类，一个类的子类也可以用作该类的代理，
 *      所以，如果要为一个没有实现接口的类生成动态代理类，那么可以使用CGLIB库。
 *      
 * 动态代理类总结：
 * 	1、动态代理类只有一个有参构造com.sun.proxy.$Proxy0(java.lang.reflect.InvocationHandler)
 * 	2、动态代理类拥有他所需要代理类的所有的方法及该需要代理类的所有父类方法
 * 	3、通过动态代理类去实例化代理类实例
 * @author 56525
 *
 */
public class JDKProxy1 {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {
		// 1、获取动态代理类
		Class clazzProxy1 = Proxy.getProxyClass(Collection.class.getClassLoader(), Collection.class);
		System.out.println("动态代理对象:" + clazzProxy1.getName());
		// 2、获取动态代理类的所有构造方法及参数列表,为之后创建代理实例用
		Constructor[] constructors = clazzProxy1.getConstructors();
		for (Constructor constructor : constructors) {
			String name = constructor.getName();
			StringBuilder sBuilder = new StringBuilder(name);
			sBuilder.append('(');
			Class[] clazzParams = constructor.getParameterTypes();
			for (Class clazzParam : clazzParams) {
				sBuilder.append(clazzParam.getName()).append(',');
			}
			if (clazzParams != null && clazzParams.length != 0)
				sBuilder.deleteCharAt(sBuilder.length() - 1);
			sBuilder.append(')');
			System.out.println("构造方法及其参数列表为：" + sBuilder.toString());
		}

		// 3、获取动态代理类的其他非构造方法及其参数列表，跟Collection比较得知，动态代理类拥有它所代理类的所有方法
		Method[] methods = clazzProxy1.getMethods();
		for (Method method : methods) {
			String name = method.getName();
			StringBuilder sBuilder = new StringBuilder(name);
			sBuilder.append('(');
			// 获取方法中的参数列表
			Class[] clazzParams = method.getParameterTypes();
			for (Class clazzParam : clazzParams) {
				sBuilder.append(clazzParam.getName()).append(',');
			}
			if (clazzParams != null && clazzParams.length != 0)
				sBuilder.deleteCharAt(sBuilder.length() - 1);
			sBuilder.append(')');
			System.out.println("非构造方法及其方法参数:" + sBuilder.toString());
		}

		System.out.println("----------根据动态代理类的构造方法创建动态实例Collection----------");
		/**
		 * 
		 * Object obj = clazzProxy1.newInstance();//该方式需要有无参构造才能创建,所以此处通过有参构造创建实例
		 * 
		 * 4、根据2中获取到的动态类的有参构造创建实例
		 * 	分析：
		 * 		InvocationHandler 是代理实例的调用处理程序 实现的接口。 
		 */
		// 创建实例方式一
		Constructor constructor = clazzProxy1.getConstructor(InvocationHandler.class);
		Collection collectionProxy1 = (Collection) constructor.newInstance(new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("只要是对collectionProxy1操作时都会触发该方法,syso会触发collectionProxy1的toString方法");
				return null;
			}
		});
		System.out.println("****************************创建实例方式一**********************************");
		collectionProxy1.clear();
		System.out.println(collectionProxy1);
		// 创建实例方式二
		Collection collectionProxy2 = (Collection) Proxy.newProxyInstance(Collection.class.getClassLoader(),
				new Class[] { Collection.class }, new InvocationHandler() {
					// InvocationHandler类的成员变量
					// 为动态实例指定目标：动态类的子类，所以第一种方式是通过接口的方式实现动态代理
					ArrayList target = new ArrayList();
					int i = 1;

					/**
					 * 
					 * proxy:
					 * 	动态代理类:com.sun.proxy.$Proxy0	
					 * method:
					 * 	代理实例被调用的方法
					 * args:
					 * 	执行该方法所传入的参数,如啊a、b、c，没有就传null
					 * 
					 */
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						// 只想打印一次，不想每次都触发
						if (i == 1) {
							System.out.println("proxy名称:" + proxy.getClass().getName());
							i++;
						}
						// 为add方法增加前置和后置通知
						if (method.getName() == "add") {
							System.out.println("触发的方法是:" + method.getName());
							// 演示AOP前置通知：在方法执行前执行
							target.add("前置：调用add之前");
							Object retVal = method.invoke(target, args);
							// 后置通知:在方法执行后执行
							target.add("后置：调用add之后");
							return retVal;
						} else {
							System.out.println("触发的方法是:" + method.getName());
							Object retVal = method.invoke(target, args);
							return retVal;
						}
					}
				});
		System.out.println("****************************创建实例方式二**************************");
		// 使用一次collectionProxy2时就会触发一次invoke方法
		// System.out.println(collectionProxy2);触发的是collectionProxy2的toString方法
		collectionProxy2.add("a");
		collectionProxy2.add("b");
		collectionProxy2.add("c");
		System.out.println(collectionProxy2.size());
		System.out.println(collectionProxy2);
	}
}
