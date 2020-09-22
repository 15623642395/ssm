package javaProject.waitAndNotify;

import java.util.Vector;

/**
 * 每2秒消费一条消息
 * @author 56525
 *
 */
public class Consumer implements Runnable {

	@SuppressWarnings("rawtypes")
	private final Vector vector;

	@SuppressWarnings("rawtypes")
	public Consumer(Vector sharedQueue) {
		this.vector = sharedQueue;
	}

	@Override
	public void run() {
		// 模拟有多个线程
		while (true) {
			consume();
		}
	}

	private void consume() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!vector.isEmpty()) {
			synchronized (vector) {
				System.out.println("消费:" + vector.get(0));
				vector.remove(0);
				vector.notifyAll();
			}
		}
	}
}