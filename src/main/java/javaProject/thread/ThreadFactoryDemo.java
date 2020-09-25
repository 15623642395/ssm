package javaProject.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义创建线程
 * @author 56525
 *
 */
public class ThreadFactoryDemo {
	public static class MyTask implements Runnable {

		@Override
		public void run() {
			System.out.println(System.currentTimeMillis() + ":Thread ID:" + Thread.currentThread().getId());
			try {
				// MyTask运行需要1秒，必然导致很多任务被加入到队列中
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws InterruptedException {
		MyTask myTask = new MyTask();
		ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0, TimeUnit.MILLISECONDS,
				new SynchronousQueue<Runnable>(), new ThreadFactory() {

					@Override
					public Thread newThread(Runnable r) {
						//创建自定义线程，并且设置为守护线程
						Thread thread = new Thread(r);
						thread.setDaemon(true);
						System.out.println("create:" + thread);
						return thread;
					}
				});

		for (int i = 0; i < 5; i++) {
			executorService.submit(myTask);
		}
		Thread.sleep(2000);
	}
}