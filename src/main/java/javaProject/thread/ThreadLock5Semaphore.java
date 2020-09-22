package javaProject.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 需求
 * 	1、因为synchronized和Lock阻塞后只能使单线程访问，怎么实现阻塞后还能有多个线程访问呢
 * 	2、如：阻塞之后限制几个人访问我的文件
 * 	3、如：抢票时，阻塞后限制几个人进入提交抢票成功
 * 解决方式：Semaphore类
 * 	
 * 	
 * @author 56525
 *
 */
public class ThreadLock5Semaphore {
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final Semaphore sp = new Semaphore(3);
		for (int i = 0; i < 10; i++) {
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						sp.acquire();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					System.out.println(
							"线程" + Thread.currentThread().getName() + "进入，当前已有" + (3 - sp.availablePermits()) + "个并发");
					try {
						Thread.sleep((long) (Math.random() * 10000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("线程" + Thread.currentThread().getName() + "即将离开");
					sp.release();
					// 下面代码有时候执行不准确，因为其没有和上面的代码合成原子单元
					System.out.println(
							"线程" + Thread.currentThread().getName() + "已离开，当前已有" + (3 - sp.availablePermits()) + "个并发");
				}
			};
			service.execute(runnable);
		}
	}
}
