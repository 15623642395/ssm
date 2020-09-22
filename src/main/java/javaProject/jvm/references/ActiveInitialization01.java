package javaProject.jvm.references;

/**
 * 主动初始化第一种方式
 * 	遇到new、getstatic、putstatic或invokestatic这4条字节码指令时，如果类没有进行过初 始化，则需要先触发其初始化。
	 	 生成这4条指令的最常见的Java代码场景是：
	 	 	(1)使用new关键字 实例化对象的时候、
	 	 	(2)读取或设置一个类的静态字段（被final修饰、已在编译期把结果放入常 量池的静态字段除外）的时候，
	 	 	(3)调用一个类的静态方法的时候。
 * @author 56525
 *
 */
public class ActiveInitialization01 {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		// 1、使用new对象的方式主动初始化
		A a = new A();
		// 2、读取或设置一个静态属性会主动初始化
		int i = B.value;
		// 3、调用一个类的静态方法的时候会主动初始化
		C.c();
	}
}

class A {
	static {
		System.out.println("我A被主动初始化了");
	}
}

class B {
	static int value = 123;
	static {
		System.out.println("我B被主动初始化了");
	}
}

class C {
	static {
		System.out.println("我C被主动初始化了");
	}

	public static void c() {

	}
}
