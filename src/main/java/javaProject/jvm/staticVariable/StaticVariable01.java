package javaProject.jvm.staticVariable;

/**
 * 研究静态变量是不是只会加载一次
 * 	答案：只会加载一次，使用多线程查看内存即可知道
 * 	1、设置参数：-verbose:gc  -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * 	2、使用多线程实例化对象
 * 	3、查看关闭其中一个线程内存变化
 *	结论：
 *		1、静态变量只会加载一次
 *		2、静态变量中的引用对像还是会加载进堆内存中
 * @author 56525
 *
 */
public class StaticVariable01 {
	static byte[] bs = new byte[2 * 1024 * 1024];

	public static void main(String[] args) {
		Runnable script = new Runnable() {
			@SuppressWarnings("unused")
			public void run() {
				StaticVariable01 staticVariable01 = new StaticVariable01();
			}
		};
		Thread thread1 = new Thread(script);
		thread1.start();
		Thread thread2 = new Thread(script);
		thread2.start();
	}
}
