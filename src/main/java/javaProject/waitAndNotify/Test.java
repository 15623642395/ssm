package javaProject.waitAndNotify;

import java.util.Vector;

/**
 * 使用wait和notifyAll模拟MQ
 * 
 * 	1、wait\notifyAll\notify必须包含在同一把同步锁之类
 * 	2、notifyAll、notify通知wait之后，wait还是需要notifyAll或者notify释放锁之后才会重新继续进行
 * @author 56525
 *
 */
public class Test {
	private static Vector<Object> vector = new Vector<Object>();

	public static void main(String[] args) {
		Thread thread1 = new Thread(new Producter(vector));
		Thread thread2 = new Thread(new Consumer(vector));
		thread1.start();
		thread2.start();
	}
}
