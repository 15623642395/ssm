package javaProject.jvm.references;

import java.lang.reflect.Method;

/**
 * 使用java.lang.reflect包的方法对类进行反射调用的时候，如果类没有进行过初始化， 则需要先触发其初始化。
 * @author 56525
 *
 */
public class ActiveInitialization02 {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args){
		Class class1 = D.class;
		Method[] methods = class1.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			try {
				methods[i].invoke(D.class.newInstance(), args);
			} catch (Exception e) {
					System.out.println(e.getMessage());
			}
		}
	}
}

class D {
	static {
		System.out.println("我D被主动初始化了");
	}

	public void d() {

	}
}
