package webProject.uploadEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 文件正式下载
 * 
 * 	根据各线程根据指定的范围大小下载
 * 
 * @author 56525
 *
 */
public class DownloadWithRange {
	Logger logger = LogManager.getLogger(DownloadWithRange.class.getName());
	private String downloadUrl;

	private String filePath;

	private String fileName;

	private long start;

	private long end;

	public DownloadWithRange(String downloadUrl, String filePath, String fileName, long start, long end) {
		this.downloadUrl = downloadUrl;
		this.filePath = filePath;
		this.fileName = fileName;
		this.start = start;
		this.end = end;
	}

	public boolean run() {
		InputStream is = null;
		RandomAccessFile raf = null;
		File file = null;
		try {
			// 校验上传的文件夹是否存在
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			// 校验文件名
			String path = filePath + File.separator + fileName;
			// 校验文件是否存在，存在相同的文件就重命名,指定文件名的编码格式
			file = new File(new String(path.getBytes(), "utf-8"));
			// 上传文件(即使上面不删除也会被覆盖)
			URL url = new URL(downloadUrl);
			// URLConnection connection = url.openConnection();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 设置连接超时间(对下载时间没影响，仅仅是ur.openConnection()的连接时间),单位是豪秒,这个一定要有,不然要是链接下载不了就会一直连着
			connection.setConnectTimeout(30000);
			// 设置下载超时时间
			connection.setReadTimeout(30000);
			// 防止屏蔽程序抓取而返回403错误
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			// 设置下载的起始值
			connection.setRequestProperty("Range", "bytes=" + start + "-" + end);
			// 将文件转换为字节流用于上传
			is = connection.getInputStream();
			if (file != null) {
				raf = new RandomAccessFile(file, "rw");
			}
			raf.seek(start);
			// 上传文件到服务器
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = is.read(b)) >= 0) {
				raf.write(b, 0, len);
			}
		} catch (Exception e) {
			System.out.println("下载附件异常原因：" + e.getMessage());
			return false;
		} finally {
			try {
				if (raf != null) {
					raf.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				System.out.println("输入输出流关闭异常:" + e.getMessage());
			}
		}
		return true;
	}
}
