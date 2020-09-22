package javaProject.jvm.staticVariable;

/**
 * 对比StaticVariable01
 * 	1、将bs改为非静态
 * 	2、设置参数：-verbose:gc  -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * 	3、使用多线程实例化对象
 * 	4、查看关闭其中一个线程内存变化
 * 结论：
 * 	1、实例变量有几个新对象就会加载几次
 *		2、静态变量中的引用对像还是会加载进堆内存中
 * @author 56525
 *
 */
public class StaticVariable02 {
	byte[] bs = new byte[2 * 1024 * 1024];

	public static void main(String[] args) {
		Runnable script = new Runnable() {
			@SuppressWarnings("unused")
			public void run() {
				StaticVariable02 staticVariable02 = new StaticVariable02();
			}
		};
		Thread thread1 = new Thread(script);
		thread1.start();
		Thread thread2 = new Thread(script);
		thread2.start();
	}
}
