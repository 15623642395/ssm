package javaProject.jvm.staticVariable;

/**
 * 研究静态块中的对象会加载多少次
 * 
 * 	1、设置参数：-verbose:gc  -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * 	2、使用多线程实例化对象
 * 	3、查看关闭其中一个线程内存变化
 * 结论：
 * 	1、静态块中的内容只会被加载一次
 * 	2、静态变量中的引用对像还是会加载进堆内存中
 * @author 56525
 *
 */
@SuppressWarnings("unused")
public class StaticVariable03 {
	static {
		byte[] bs = new byte[2 * 1024 * 1024];
	}

	public static void main(String[] args) {
		Runnable script = new Runnable() {
			public void run() {
				StaticVariable03 staticVariable03 = new StaticVariable03();
			}
		};
		Thread thread1 = new Thread(script);
		thread1.start();
		Thread thread2 = new Thread(script);
		thread2.start();
	}
}
