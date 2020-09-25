package javaProject.thread;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 演示类的读写锁
 * 	读锁：可以多线程一起读，此时不会穿插写，但是读与读之间可以穿插
 * 	写锁：单个线写完之后另一个线程才能写
 * 互斥原则：
 *		读-读能共存，
 *		读-写不能共存，
 *		写-写不能共存
 * 
 * @author 56525
 *
 */
public class ThreadLock2 {
	public static void main(String[] args) throws InterruptedException {
		final Queue3 q3 = new Queue3();
		for (int i = 0; i < 3; i++) {
			// 读
			new Thread() {
				public void run() {
					while (true) {
						q3.get();
					}
				}

			}.start();
			// 写
			new Thread() {
				public void run() {
					while (true) {
						q3.put(new Random().nextInt(10000));
					}
				}
			}.start();
		}
	}
}

class Queue3 {
	private Object data = null;// 共享数据，只能有一个线程能写该数据，但可以有多个线程同时读该数据。
	// 使用读写锁
	ReadWriteLock rwl = new ReentrantReadWriteLock();

	// 读数据
	public void get() {
		rwl.readLock().lock();
		try {
			System.out.println(Thread.currentThread().getName() + " 读 data!");
			Thread.sleep((long) (Math.random() * 1000));
			System.out.println(Thread.currentThread().getName() + "读完data :" + data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwl.readLock().unlock();
		}
	}

	// 写数据
	public void put(Object data) {
		rwl.writeLock().lock();
		try {
			System.out.println(Thread.currentThread().getName() + " 写 data!");
			Thread.sleep((long) (Math.random() * 1000));
			this.data = data;
			System.out.println(Thread.currentThread().getName() + " 写完 data: " + data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwl.writeLock().unlock();
		}
	}
}
