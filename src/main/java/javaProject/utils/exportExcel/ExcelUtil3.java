
package javaProject.utils.exportExcel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import webProject.emp.entity.Emp;

/**
 * 一次导出多张excel并压缩
 * 
 * @author 56525
 *
 */
public class ExcelUtil3 {

	/**
	 * 生成excel到本地
	 * 
	 * @param request
	 * @param response
	 * @param list
	 * @param fileName
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "resource", "deprecation" })
	public static void exportData(HttpServletRequest request, HttpServletResponse response, List list, String path,
			String fileName) throws IOException {
		FileOutputStream os = null;
		try {

			// 创建文件夹
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
			// 准备生成文件
			os = new FileOutputStream(path + File.separator + fileName);
			// 创建工作空间并缓存
			SXSSFWorkbook wb = new SXSSFWorkbook(100);
			// 计算sheet的次数,每个sheet设置为65535行(所有加起来最大1048576行)
			int num = 0;
			int sheetCount = 5000;// 该数字可变，根据实际调整，每个sheet的条数
			// 判断分页次数
			if (list.size() % sheetCount == 0) {
				num = list.size() / sheetCount;
			} else {
				num = list.size() / sheetCount + 1;
			}
			// 根据num进行分页(sheet页)
			for (int i = 0; i < num; i++) {
				// 创建表
				String sheetName = "Sheet" + (i + 1);
				Sheet sheet = wb.createSheet(sheetName);
				sheet.setDefaultColumnWidth(20);
				sheet.setDefaultRowHeightInPoints(20);

				// 创建第一行：表头行
				Row row = sheet.createRow(0);

				// 生成一个样式
				XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
				style.setAlignment(HorizontalAlignment.CENTER);// 水平居中
				style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中

				// 设置边框
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框

				// 生成一个字体
				Font font = wb.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
				font.setFontName("宋体");// 这是字体

				// 把字体 应用到当前样式
				style.setFont(font);

				// 添加表头数据,表头数据肯定是固定的
				String[] excelHeader = { "ID", "创建时间", "部门ID", "邮箱", "性别", "登陆账户", "密码", "手机号", "状态", "修改时间", "用户名" };
				// 将表头数据添加到第一行的所有列中
				for (int j = 0; j < excelHeader.length; j++) {
					Cell cell = row.createCell(j);
					cell.setCellValue(excelHeader[j]);
					// 给表头单元格添加样式
					cell.setCellStyle(style);
				}
				// 最后一次的数据条数
				int lastSheetCount = sheetCount;
				if (i + 1 == num) {
					sheetCount = list.size() - i * sheetCount;
				}
				// 添加其他行的数据
				for (int j = 0; j < sheetCount; j++) {
					Emp emp = null;
					// 创建其他行
					row = sheet.createRow(j + 1);
					if (i == 0) {
						emp = (Emp) list.get(j + i * sheetCount);
					} else {
						emp = (Emp) list.get(j + i * lastSheetCount);
					}
					// 为其他行的每列添加数据
					String[] excelBody = { emp.getId(), emp.getCreatedtime(), emp.getDeptid(), emp.getEmail(),
							emp.getGender(), emp.getLogin_account(), emp.getPassword(), emp.getPhone(), emp.getStatus(),
							emp.getUpdatetime(), emp.getUser_name() };
					// 设置每列数据
					for (int k = 0; k < excelBody.length; k++) {
						row.createCell(k).setCellValue(excelBody[k]);
					}
				}
			}
			// 将excel生成到指定位置
			wb.write(os);
		} catch (Exception e) {
			System.out.println("本地生成excel失败，失败原因:" + e.getMessage());
		} finally {
			if (null != os) {
				os.close();
			}
		}
	}

	/**
	 * 将文件压缩为zip
	 * 	注意：
	 * 		(1)切记只需要关闭包装流,否者会出现问题
	 * 		(2)先关输入流再关输出流
	 * 
	 * @param sourceFilePath
	 * 				文件位置
	 * @param zipFilePath
	 * 				zip文件存放位置
	 * @param fileName
	 * 				zip文件名称
	 * @return
	 */
	public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		if (!sourceFile.exists()) {
			System.out.println("待压缩的文件目录：" + sourceFilePath + "不存在。");
		} else {
			try {
				File zipFile = new File(zipFilePath + File.separator + fileName + ".zip");
				if (zipFile.exists()) {
					System.out.println(zipFilePath + "目录下存在名字为:" + fileName + ".zip" + "打包文件。");
				} else {
					File[] sourceFiles = sourceFile.listFiles();
					if (null == sourceFiles || sourceFiles.length < 1) {
						System.out.println("待压缩的文件目录：" + sourceFilePath + "里面不存在文件，无需压缩.");
					} else {
						fos = new FileOutputStream(zipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[1024 * 10];
						for (int i = 0; i < sourceFiles.length; i++) {
							// 创建ZIP实体，并添加进压缩包
							ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
							zos.putNextEntry(zipEntry);
							// 读取待压缩的文件并写进压缩包里
							fis = new FileInputStream(sourceFiles[i]);
							bis = new BufferedInputStream(fis, 1024 * 10);
							int read = 0;
							while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
								zos.write(bufs, 0, read);
							}
						}
						flag = true;
					}
				}
			} catch (Exception e) {
				System.out.println("生成zip异常，异常原因:" + e.getMessage());
			} finally {
				// 关闭流,切记只需要关闭包装流
				try {
					if (null != bis) {
						bis.close();
					}
					if (null != zos) {
						zos.close();
					}
				} catch (Exception e) {
					System.out.println("流关闭异常，异常原因:" + e.getMessage());
				}
			}
		}
		return flag;
	}

	/**
	 * 将文件写入到浏览器，浏览器进行下载
	 * 
	 * @param request
	 * @param response
	 * @param filePath
	 * 				文件路径
	 * @param fileName
	 * 				文件名
	 */
	public static void writeToClient(HttpServletRequest request, HttpServletResponse response, String filePath,
			String fileName, List<String> fileList) {
		FileInputStream fis = null;
		OutputStream ous = null;
		try {
			File file = new File(filePath + File.separator + fileName);
			fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);

			String agent = request.getHeader("USER-AGENT").toLowerCase();
			// 每个浏览器处理excel文件中文名称方式不一样
			if (agent.contains("firefox")) {// 火狐浏览器
				response.reset();
				response.setContentType("application/octet-stream;charset=UTF-8");// 以流的形式写出
				response.addHeader("Content-Length", "" + file.length());
				response.setHeader("Content-Disposition",
						"attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
			} else {// 其他浏览器
				fileName = URLEncoder.encode(fileName, "UTF-8");
				response.reset();
				response.setContentType("application/octet-stream;charset=UTF-8");// 以流的形式写出
				response.addHeader("Content-Length", "" + file.length());
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			}
			ous = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			ous.write(buffer);
			ous.flush();
		} catch (Exception e) {
			System.out.println("浏览器下载失败:" + e.getMessage());
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (ous != null) {
					ous.close();
				}

			} catch (IOException e) {
				System.out.println("流关闭失败:" + e.getMessage());
			}
		}
	}

	/**
	 * 删除指定的文件
	 * 	
	 * @param fileList
	 */
	public static void deleteFile(List<String> fileList) {
		// 删除已生成的文件，会存在映射，无法删除的情况，多执行几次删除即可
		// https://blog.csdn.net/qq_36752632/article/details/72869331#commentBox
		for (int i = 0; i < fileList.size(); i++) {
			File file = new File(fileList.get(i));
			boolean result = false;
			int tryCount = 0;
			// 没删掉的再删除10次
			while (!result && tryCount++ < 10) {
				System.gc();
				result = file.delete();
			}
		}
	}

	public static void main(String[] args) {
		String sourceFilePath = "D:" + File.separator + "file" + File.separator + "excel";
		String zipFilePath = "D:" + File.separator + "file" + File.separator + "excel";
		String fileName = "12700153file";
		boolean flag = ExcelUtil3.fileToZip(sourceFilePath, zipFilePath, fileName);
		if (flag) {
			System.out.println("文件打包成功!");
		} else {
			System.out.println("文件打包失败!");
		}
	}
}