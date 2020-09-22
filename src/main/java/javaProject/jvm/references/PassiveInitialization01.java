package javaProject.jvm.references;

/** 
 * 被动初始化
 * 	被动使用类字段演示一： *通过子类引用父类的静态字段，不会导致子类初始化
 * 	
 * 	通过设置参数：-XX:+TraceClassLoading可以查看哪些类被加载了
 * 
 * 	上述代码运行之后，只会输出“SuperClass init！”，而不会输出“SubClass init！”。
 * 对于静 态字段，只有直接定义这个字段的类才会被初始化，因此通过其子类来引用父类中定义的静 态字段，
 * 只会触发父类的初始化而不会触发子类的初始化。
 * 	 至于是否要触发子类的加载和验 证，在虚拟机规范中并未明确规定，
 * 这点取决于虚拟机的具体实现。对于Sun HotSpot虚拟机 来说，
 * 可通过-XX：+TraceClassLoading参数观察到此操作会导致子类的加载。
 * 
 * 
 * 
 **/
public class PassiveInitialization01 {
	public static void main(String[] args) {
		System.out.println(SubClass.value);
	}
}

class SuperClass {
	static {
		System.out.println("SuperClass init！");
	}
	public static int value = 123;
}

class SubClass extends SuperClass {
	static {
		System.out.println("SubClass init！");
	}
}
