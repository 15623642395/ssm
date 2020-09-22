package javaProject.proxy.cglibProxy.advice;

import java.lang.reflect.Method;

/**
 * 切面实现类
 * 
 * @author 56525
 *
 */
public class MyAdviceCGLIB implements AdviceCGLIB {

	@Override
	public void beforMethod(Method method) {
		System.out.println("我是前置方法,我在目标方法:" + method.getName() + "之前实现");

	}

	@Override
	public void afterMethod(Method method) {
		System.out.println("我是后置方法,我在目标方法:" + method.getName() + "之后实现");
	}

}
