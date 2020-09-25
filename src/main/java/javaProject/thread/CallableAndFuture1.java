package javaProject.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 演示单线程中Callable和Future
 * 	 Callable和Future配合可以获取每个线程的返回值
 * @author 56525
 *
 */
public class CallableAndFuture1 {
	public static void main(String[] args) {
		// 创建线程池
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		// 使用future和callable获取值
		Future<String> future = executorService.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				Thread.sleep(1000);
				return "hello";
			}
		});
		System.out.println("等待线程执行完成之后，后续代码才会执行，获取线程执行的返回结果");
		try {
			System.out.println("拿到结果：" + future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
