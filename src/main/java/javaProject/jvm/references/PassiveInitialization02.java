package javaProject.jvm.references;

/**
 * 被动使用类字段演示二： 
 * 	通过数组定义来引用类，不会触发此类的初始化
 * 
 * 通过设置参数：-XX:+TraceClassLoading可以查看哪些类被加载了
 * 
 * @author 56525
 *
 */
public class PassiveInitialization02 {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		SuperClass1[] sca = new SuperClass1[10];
	}
}

class SuperClass1 {
	static {
		System.out.println("SuperClass init！");
	}
}