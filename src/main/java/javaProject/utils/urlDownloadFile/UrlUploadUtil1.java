package javaProject.utils.urlDownloadFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 单线程下载文件：文件名要有唯一性，不然会被覆盖
 * 
 * 通过页面传过来的URL进行文件上传
 * 	url下载附件地址
 * 	该方式的URL是不带文件名的，需要指定文件名
 * 注:一般在下载都会有文件名后缀的，但是下面通过浏览器下载就会存在没有文件名的情况
 * @author 56525
 *
 */
public class UrlUploadUtil1 {

	/**
	 * 
	 * @param url
	 * 				附件下载的url
	 * @param filePath
	 * 				存放附件的地址
	 * @param fileName
	 * 				附件名称
	 * @throws IOException
	 */
	public void uploadFile(String urls, String filePath, String fileNames) throws IOException {
		InputStream is = null;
		OutputStream os = null;
		System.out.println("下载开始");
		try {
			// 校验上传的文件夹是否存在
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 多个附件下载
			for (int i = 0; i < urls.split(",").length; i++) {
				String fileName = fileNames.split(",")[i];
				String url = urls.split(",")[i];
				// 文件路径及文件名称
				String path = filePath + File.separator + fileName;
				// 校验文件是否存在，存在相同的文件就删除,指定文件名的编码格式
				file = new File(new String(path.getBytes(), "utf-8"));
				if (file.exists()) {
					file.delete();
				}

				// 上传文件(即使上面不删除也会被覆盖)
				URL ur = new URL(url);
				URLConnection connection = ur.openConnection();
				System.out.println(fileName + "文件大小：" + connection.getContentLength());
				Thread.sleep(1000);
				// 设置连接超时间(对下载时间没影响，仅仅是ur.openConnection()的连接时间),单位是豪秒
				// connection.setConnectTimeout(3 * 1000);
				// 将文件转换为字节流用于上传
				is = connection.getInputStream();
				byte[] bs = new byte[1024];
				int len;
				os = new FileOutputStream(file);
				// 上传文件到服务器
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
			}
			System.out.println("下载完成");
		} catch (Exception e) {
			System.out.println("下载附件异常原因：" + e.getMessage());
		} finally {
			is.close();
			os.close();
		}
	}

	/**
	 * 
	 * 1、测试地址可以在网上任意找个能下载文件的地址即可(如鬼吹灯的rar下载)
	 * 2、然后在浏览器下载页面中把下载地址复制下来即可
	 * 3、在下载地址中鼠标右键，复制链接地址则可得到下载地址
	 * 4、下载地址会有更新，注意每天重新输入下载地址
	 * 5、该种方式下载时没有文件名称的(可能是浏览器自己加密了)
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		UrlUploadUtil1 uploadUtil = new UrlUploadUtil1();
		String filePath = "D:\\file\\file";
		// 指定下载文件名称，注意后缀文件格式，格式错误会打不开,可以以逗号形式传多个，fileName和urls要一致
		String fileNames = "1.zip";
		String urls = "http://txt.bookdown.net/home/down/zip/id/40568";
		// 获取鬼吹灯的下载地址
		uploadUtil.uploadFile(urls, filePath, fileNames);
		long end = System.currentTimeMillis();
		System.out.println("总共用时:" + (end - start) / 1000);
	}
}
