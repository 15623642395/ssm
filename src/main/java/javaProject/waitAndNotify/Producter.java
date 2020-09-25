package javaProject.waitAndNotify;

import java.util.Vector;

/**
 * 每秒产生一条消息
 * @author 56525
 *
 */
public class Producter implements Runnable {

	@SuppressWarnings("rawtypes")
	private final Vector vector;

	@SuppressWarnings("rawtypes")
	public Producter(Vector sharedQueue) {
		this.vector = sharedQueue;
	}

	@Override
	public void run() {
		try {
			producter();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void producter() throws InterruptedException {
		int i = 0;
		while (true) {
			if (vector.size() == 10) {
				synchronized (vector) {
					System.out.println("容器消息已满，开启等待");
					vector.wait();
				}
			} else {
				Thread.sleep(1000);
				String string = "消息" + i++;
				vector.add(string);
				System.out.println("产生:" + string);
			}
		}

	}
}