package javaProject.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示线程锁Look
 * 	类似于synchronized,只是synchronized不需要解锁
 * 
 * @author 56525
 *
 */
public class ThreadLock1 {

	public static void main(String[] args) {
		final Outputer outputer = new Outputer();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String name = "zhangxiaoxiang";
				while (true) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					outputer.output(name);
				}
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				String name = "lihuoming";
				while (true) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					outputer.output(name);
				}

			}
		}).start();
	}
}

class Outputer {
	Lock lock = new ReentrantLock();

	public void output(String name) {
		int len = name.length();
		//加锁，等第一个进来的全部输出完再输出第二个
		lock.lock();
		try {
			for (int i = 0; i < len; i++) {
				// 注意这里是print不是println没有换行
				System.out.print(name.charAt(i));
			}
			System.out.println();
		} finally {
			// 一定要解锁
			lock.unlock();
		}
	}
}
