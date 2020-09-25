package javaProject.proxy.cglibProxy;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import javaProject.proxy.cglibProxy.advice.AdviceCGLIB;

/**
 * sub：
 * 	cglib生成的代理对象
 * method：
 * 	被代理对象方法
 * objects：
 * 	方法入参
 * methodProxy: 
 * 	代理方法
 */
public class MyMethodInterceptor implements MethodInterceptor {
	// 切面
	private AdviceCGLIB adviceCGLIB;

	public MyMethodInterceptor(AdviceCGLIB adviceCGLIB) {
		this.adviceCGLIB = adviceCGLIB;
	}

	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {
		adviceCGLIB.beforMethod(arg1);
		Object object = arg3.invokeSuper(arg0, arg2);
		adviceCGLIB.afterMethod(arg1);
		return object;
	}
}
