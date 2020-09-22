package javaProject.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 模拟队列的实现
 * 	场景：收发短信,队列中短信最大存储数量时100
 * @author 56525
 *
 */
public class ThreadLock4Condition2 {
	final Lock lock = new ReentrantLock();
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();

	final Object[] items = new Object[100];
	int putptr;// 队列中已存数据的下标
	int takeptr;// 队列中已取数据的下标
	int count;// 记录队列中已经放了多少元素

	public void put(Object x) throws InterruptedException {
		lock.lock();
		try {
			// 当放入短信等于队列中规定的最大值100时，告诉其他线程取短信
			while (count == items.length) {
				// 告诉notFull等待，一直等待，直到task()方法执行，数据被取出，task()一直不执行就会一直阻塞
				notFull.await();
			}
			items[putptr] = x;
			if (++putptr == items.length) {
				putptr = 0;
			}
			++count;
			// 每次存一条短信就通知notEmpty运行，至于take()何时被调用就不是自己的事了
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}

	public Object take() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				notEmpty.await();
			}
			Object x = items[takeptr];
			if (++takeptr == items.length) {
				takeptr = 0;
			}
			--count;
			// 短信被取出了一条，告诉notFull可以存短信了
			notFull.signal();
			return x;
		} finally {
			lock.unlock();
		}
	}

}
