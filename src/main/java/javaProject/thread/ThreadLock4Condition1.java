package javaProject.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition
 * 	:lock的同步通信
 * @author 56525
 *
 */
public class ThreadLock4Condition1 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final Business business = new Business();
		new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 1; i <= 50; i++) {
					business.sub(i);
				}

			}
		}).start();

		for (int i = 1; i <= 50; i++) {
			business.main(i);
		}

	}

	static class Business {
		Lock lock = new ReentrantLock();
		Condition condition = lock.newCondition();
		private boolean bShouldSub = true;

		public void sub(int i) {
			lock.lock();
			try {
				while (!bShouldSub) {
					try {
						// 让其他线程等待
						condition.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (int j = 1; j <= 10; j++) {
					System.out.println("sub thread sequence of " + j + ",loop of " + i);
				}
				bShouldSub = false;
				// 自己完成后，告诉其他线程开始运行，自己等待，从而实现同步
				condition.signal();
			} finally {
				lock.unlock();
			}
		}

		public void main(int i) {
			lock.lock();
			try {
				while (bShouldSub) {
					try {
						// 让其他线程等待
						condition.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (int j = 1; j <= 100; j++) {
					System.out.println("main thread sequence of " + j + ",loop of " + i);
				}
				bShouldSub = true;
				// 自己完成后，告诉其他线程开始运行，自己等待，从而实现同步
				condition.signal();
			} finally {
				lock.unlock();
			}
		}
	}
}
