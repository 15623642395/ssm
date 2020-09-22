package javaProject.thread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 演示多线程中Callable和Future
 * 	 Callable和Future配合可以获取每个线程的返回值
 * @author 56525
 *
 */
public class CallableAndFuture2 {
	public static void main(String[] args) {
		// 线程池
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		// 让线程分别做自己的事情
		CompletionService<String> completionService = new ExecutorCompletionService<String>(executorService);
		for (int i = 0; i < 15; i++) {
			final int seq = i;
			completionService.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					// 让线程之间休眠5秒之内
					Thread.sleep(new Random().nextInt(5000));
					String rtn = "返回线程:" + Thread.currentThread().getName() + ":" + seq;
					return rtn;
				}
			});
		}
		// 获取个线程的输出值，哪个线程先完成，先获取哪个,最先拿到运行完的
		for (int i = 0; i < 15; i++) {
			try {
				System.out.println(completionService.take().get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		executorService.shutdown();
	}
}
