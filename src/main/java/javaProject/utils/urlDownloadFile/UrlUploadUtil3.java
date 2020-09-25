package javaProject.utils.urlDownloadFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 多线程下载文件:文件重名则会重新命名文件
 * 
 * 	该方式URL后缀是有文件名的，不需要指定文件名
 * 
 * 备注：该工具类可以监控每个子线程得返回状态，比UrlUploadUtil2更全面，可以判断只要子线程下载错误则删除该缺损文件
 * 
 * @author 56525
 *
 */
public class UrlUploadUtil3 {
	// 记录文件名
	ThreadLocal<String> fileNameNew = new ThreadLocal<String>();

	/**
	 * 获取图片地址
	 * 	1、百度搜索图片
	 * 	2、点击进入到图片中
	 * 	3、选中图片，右键
	 * 	4、复制图片地址即可
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Long start = System.currentTimeMillis();
		UrlUploadUtil3 uploadUtil = new UrlUploadUtil3();
		// 指定存放地址
		String filePath = "D:\\file";
		// 获取下载地址http://txt.bookdown.net/home/down/zip/id/40568 这个链接很好
		// http://192.168.10.12:8080/ssm/image/4.png--下载虚拟机中ssm工程下的图片
		String downloadUrl = "http://192.168.10.12:8080/ssm/image/4.png";
		// 获取文件名称
		String fileName = "1.png";
		String localFileName = "1.png";
		fileName = uploadUtil.getFileName(downloadUrl, fileName, localFileName);
		// 线程数
		uploadUtil.uploadFile(downloadUrl, filePath, fileName, localFileName);
		Long end = System.currentTimeMillis();
		System.out.println("总共耗时:" + (end - start) / 1000);
	}

	/**
	 * 获取文件名
	 * 	getOldFileName()该方法具有局限性，但是也有参考性
	 * @param downloadUrl
	 * @param fileName
	 * @return
	 */
	public String getFileName(String downloadUrl, String fileName, String localFileName) {
		fileName = localFileName;
		return fileName;
	}

	/**
	 * 获取下载文件的文件名
	 * 	在实际演示中，可能附件下载时根本截取不到文件名，最好还是别人传进来
	 * 
	 * @param downloadUrl
	 * @param fileName
	 * @return
	 */
	public String getOldFileName(String downloadUrl, String fileName, String localFileName) {
		try {
			String suffixes = "[^/]+[\\.](avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|pdf|rar|zip|docx|doc|txt)";
			Pattern pat = Pattern.compile(suffixes);// 正则判断,以/开始，以.avi等后缀结尾的文件名
			Matcher mc = pat.matcher(downloadUrl);// 条件匹配
			if (mc.find()) {
				String rtnName = mc.group();
				return rtnName;
			} else {
				fileName = localFileName;
			}
		} catch (Exception e) {
			System.out.println("文件名称获取失败！");
		}
		return fileName;
	}

	/**
	 * 下载附件
	 * 
	 * @param downloadUrl
	 * 				附件下载的url
	 * @param filePath
	 * 				存放附件的地址
	 * @param fileName
	 * 				附件名称
	 * @throws IOException
	 */
	public synchronized boolean uploadFile(String downloadUrl, String filePath, String fileName, String localFileName)
			throws IOException {
		ThreadPoolExecutor threadPool = null;
		boolean flag = true;// 文件是否下载成功标识
		boolean threadFlag = true;// 监控线程完成状况
		try {
			// 获取文件大小
			URL url = new URL(downloadUrl);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(3 * 1000);
			long fileSize = connection.getContentLengthLong();
			// 校验文件是否存在，文件要是存在则重命名，必须做同步，此时只是获取到文件名，但是文件夹中还未正式写文件，不存在该文件名的文件(线程那里才写文件)
			// 所以此时不做同步其他线程也会取到相应的文件名
			checkFileName(downloadUrl, filePath, fileName, localFileName, 1);
			System.out.println("下载文件为:" + fileNameNew.get());
			// 创建线程池，确定每个线程的起始值，https://zhidao.baidu.com/question/1609275974943756907.html
			int corePoolSize = 4;// 核心线程个数(跟电脑一致)
			int maximumPoolSize = 8;// 当任务数超过核心线程数时的最大线程数(跟电脑一致)
			long keepAliveTime = 3;// 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间
			TimeUnit unit = TimeUnit.SECONDS;// 指明等待时间的量化值的单位
			// 保存到队列的任务数，当任务数达到队列最大时拒绝加入，抛异常等
			BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(8);// 队列中最大接收8个任务，超过8个则会抛异常
			// 特别强调:要使用new ThreadPoolExecutor创建，这样main方法和页面测试都没问题
			threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
			threadPool.prestartAllCoreThreads(); // 预启动所有核心线程
			// CompletionService可以监控所有子线程的返回结果
			CompletionService<Boolean> completionService = new ExecutorCompletionService<Boolean>(threadPool);
			// 分段下载
			for (int i = 0; i < maximumPoolSize; i++) {
				long start = i * fileSize / maximumPoolSize;
				long end = (i + 1) * fileSize / maximumPoolSize;
				if (i == maximumPoolSize - 1) {
					end = fileSize;
				}
				final UtilUploadUtil3WithRange download = new UtilUploadUtil3WithRange(downloadUrl, filePath, fileNameNew.get(),
						start, end);
				completionService.submit(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						boolean flag = download.run();
						return flag;
					}
				});
			}
			// 获取每个线程的返回值
			for (int i = 0; i < maximumPoolSize; i++) {
				// 只要有一个线程下载失败则是下载失败
				if (false == completionService.take().get()) {
					threadFlag = false;
				}
			}
			System.out.println("下载完成");
		} catch (Exception e) {
			System.out.println("多线程创建异常:" + e.getMessage());
			flag = false;// 文件下载异常
		} finally {
			// 注意shutdown要在awaitTermination之前调用，否则线程池关闭不掉
			if (threadPool != null) {
				System.out.println("关闭线程池");
				threadPool.shutdown();
			}
			try {// 这个必须要有，主线程等待直到线程池中所有任务完成
				threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("所有子线程执行完成！");
		// 文件下载异常则删除文件
		String path = filePath + File.separator + fileNameNew.get();
		File file = new File(new String(path.getBytes(), "utf-8"));
		if (!flag && file.exists()) {
			System.out.println("进入文件删除1,删除文件:" + file);
			boolean mark = file.delete();
			System.out.println("删除下载异常文件:" + fileNameNew.get() + ":" + mark);
			return false;
		}
		if (!threadFlag && file.exists()) {
			System.out.println("进入文件删除2,删除文件:" + file);
			System.out.println("删除下载异常文件:" + fileNameNew.get() + ":" + file.delete());
			return false;
		}
		return true;
	}

	/**
	 * 递归查询文件名是否存在，存在则重命名
	 * 	命名规则：在文件名称之前加(i)i从1开始
	 * 
	 * @param downloadUrl
	 * 				文件下载的url
	 * @param filePath
	 * 				存放文件路径
	 * @param fileName
	 * 				文件名称
	 *  @param index
	 * 				文件重命名的下标
	 * @throws UnsupportedEncodingException
	 */
	public void checkFileName(String downloadUrl, String filePath, String fileName, String localFileName, int index)
			throws UnsupportedEncodingException {
		// 获取文件名称
		String path = filePath + File.separator + fileName;
		// 校验文件是否存在，存在相同的文件就重命名,指定文件名的编码格式
		File file = new File(new String(path.getBytes(), "utf-8"));
		if (file.exists()) {
			// 获取原始的文件名称
			String rtnFileName = getFileName(downloadUrl, fileName, localFileName);
			String prefixName = rtnFileName.substring(0, rtnFileName.lastIndexOf("."));
			String postfixName = rtnFileName.substring(rtnFileName.lastIndexOf("."), rtnFileName.length());
			rtnFileName = prefixName + "(" + index + ")" + postfixName;
			path = filePath + File.separator + rtnFileName;
			fileNameNew.set(rtnFileName);
			index++;
			checkFileName(downloadUrl, filePath, fileNameNew.get(), localFileName, index);
		} else {
			fileNameNew.set(fileName);
		}
	}
}