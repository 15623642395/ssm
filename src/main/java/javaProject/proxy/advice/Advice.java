package javaProject.proxy.advice;

import java.lang.reflect.Method;

/**
 * 切面
 * 	做成接口具有通用性
 * @author 56525
 *
 */
public interface Advice {
	// 前置通知
	public void beforMethod(Method method);

	// 后置通知
	public void afterMethod(Method method);
}
