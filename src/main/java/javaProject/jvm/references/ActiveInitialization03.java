package javaProject.jvm.references;
/**
 * 当初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父 类的初始化。 
 * @author 56525
 *
 */
public class ActiveInitialization03 {
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		E e = new E();
	}
}

class E extends F {
	static {
		System.out.println("我E被主动初始化了");
	}
}

class F {
	static {
		System.out.println("我F被主动初始化了");
	}
}