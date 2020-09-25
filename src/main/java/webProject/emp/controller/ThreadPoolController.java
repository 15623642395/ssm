package webProject.emp.controller;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * http://localhost:8080/ssm/threadPool/fixedThreadPool.do
 * 该对象专门用于研究多线程的
 * @author admin
 *
 */
@Controller
@RequestMapping("/threadPool")
public class ThreadPoolController {
	Logger logger = LogManager.getLogger(ThreadPoolController.class.getName());

	// 固定个数的线程池
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	// 单个个数的线程池
	private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	// 该方法返回一个可根据实际情况调整线程数量的线程池。
	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	// 该方法返回一个ScheduledExecutorService对象，线程池大小为1
	// 在指定时间执行某任务的功能，如在某个固定的延时之后执行，或者周期性执行某个任务。
	private ScheduledExecutorService singleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
	// 自定义线程池
	private ExecutorService myThreadPool = new ThreadPoolExecutor(1, 4, 60L, TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(10));

	/**
	 * 	该方法返回一个固定数量的线程池。该线程池中的线程数量始终不变。
	 * 当有新任务提交时，线程池若有空闲的线程则立即执行，否则则等待，直到线程池中有空闲的线程。
	 * 	
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@RequestMapping(value = "/fixedThreadPool.do", method = RequestMethod.POST)
	@ResponseBody
	public String fixedThreadPool() throws InterruptedException, ExecutionException {
		fixedThreadPool.execute(new TaskOne());
		fixedThreadPool.execute(new TaskTwo());
		fixedThreadPool.submit(new TaskThree());
		fixedThreadPool.submit(new TaskFour());
		return "success";
	}

	/**
	 *	该方法返回一个只有一个线程的线程池。若多余一个任务被提交到线程池，
	 *	任务会被保留在任务队列中，待线程空闲在根据先进先出的规则执行任务。
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@RequestMapping(value = "/singleThreadExecutor.do", method = RequestMethod.POST)
	@ResponseBody
	public String singleThreadExecutor() throws InterruptedException, ExecutionException {
		singleThreadExecutor.execute(new TaskOne());
		singleThreadExecutor.execute(new TaskTwo());
		singleThreadExecutor.submit(new TaskThree());
		singleThreadExecutor.submit(new TaskFour());
		return "success";
	}

	/**
	 *		该方法返回一个可根据实际情况调整线程数量的线程池。线
	 *程池的线程数量不确定，但若有空闲的线程可以复用，则优先使用可复用的线程。
	 *若线程池中所有线程在工作，此时要是进来新的任务，线程池自身会创建新的线程进行处理，
	 *所有线程执行完毕之后，将返回线程池进行复用。
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@RequestMapping(value = "/cachedThreadPool.do", method = RequestMethod.POST)
	@ResponseBody
	public String cachedThreadPool() throws InterruptedException, ExecutionException {
		cachedThreadPool.execute(new TaskOne());
		cachedThreadPool.execute(new TaskTwo());
		cachedThreadPool.submit(new TaskThree());
		cachedThreadPool.submit(new TaskFour());
		return "success";
	}

	/**
	 * 		该方法返回一个ScheduledExecutorService对象，线程池大小为1，
	 *		 ScheduledExecutorService接口在ExecutorService接口之上扩展了在指定时间执行某任务的功能，
	 * 	  如在某个固定的延时之后执行，或者周期性执行某个任务。
	 * 	  有多个执行方法，详见pdf3.2.2
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@RequestMapping(value = "/singleThreadScheduledExecutor.do", method = RequestMethod.POST)
	@ResponseBody
	public String singleThreadScheduledExecutor() throws InterruptedException, ExecutionException {
		singleThreadScheduledExecutor.scheduleAtFixedRate(new TaskOne(), 0, 2, TimeUnit.SECONDS);
		return "success";
	}

	/**
	 * 自定义线程池
	 * 	通常能处理的并发次数：max线程数+队列容量，即14个
	 * 	jmeter中启动20个线程，则可发现有6个报错(被拒绝了)，14个成功
	 * 这是通常情况：指的是加入队列的时候核心线程数被占用着
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@RequestMapping(value = "/customThread.do", method = RequestMethod.POST)
	@ResponseBody
	public String customThread() throws InterruptedException, ExecutionException {
		try {
			myThreadPool.execute(new TaskOne());
		} catch (Exception e) {
			logger.error("任务数过大，超过预设值！");
		}

		return "success";
	}

	/**
	 * 第一个任务
	 * @author admin
	 *
	 */
	class TaskOne implements Runnable {
		@Override
		public void run() {
			ThreadContext.put("log4jdir", "/ssm/threadPool");
			ThreadContext.put("traceId", UUID.randomUUID().toString());
			logger.info("第一个任务执行完成");
			ThreadContext.remove("log4jdir");
			ThreadContext.remove("traceId");
		}

	}

	/**
	 * 第二个任务
	 * @author admin
	 *
	 */
	class TaskTwo implements Runnable {

		@Override
		public void run() {

			ThreadContext.put("log4jdir", "/ssm/threadPool");
			ThreadContext.put("traceId", UUID.randomUUID().toString());
			logger.info("第二个任务执行完成");
			ThreadContext.remove("log4jdir");
			ThreadContext.remove("traceId");
		}

	}

	/**
	 * 第三个任务
	 * @author admin
	 *
	 */
	class TaskThree implements Callable<String> {

		@Override
		public String call() throws Exception {

			ThreadContext.put("log4jdir", "/ssm/threadPool");
			ThreadContext.put("traceId", UUID.randomUUID().toString());
			logger.info("第三个任务执行完成");
			ThreadContext.remove("log4jdir");
			ThreadContext.remove("traceId");
			return "TaskThree finish";
		}

	}

	/**
	 * 第四个任务
	 * @author admin
	 *
	 */
	class TaskFour implements Callable<String> {

		@Override
		public String call() throws Exception {

			ThreadContext.put("log4jdir", "/ssm/threadPool");
			ThreadContext.put("traceId", UUID.randomUUID().toString());
			logger.info("第四个任务执行完成");
			ThreadContext.remove("log4jdir");
			ThreadContext.remove("traceId");
			return "TaskFour finish";
		}

	}

}
