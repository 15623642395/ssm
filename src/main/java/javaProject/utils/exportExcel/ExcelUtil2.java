package javaProject.utils.exportExcel;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

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
 * 导出excel工具类:使用SXSSFWorkbook(excel 2007)--支持百万条
 * 
 * 	注意： (1)使用poi时，当sheet的行数超过65535时会报错
 * 			     解决思路，当超过65535行时，新建sheet
 * 		  (2)当数据量太大是list也会内存溢出，建议分页查询在放入到list
 * 		  (3)该方式能生成一个excel不能生成多个excel，当数据量超过最大值时需要生成多个excel压缩并返回一个zip
 * 				压缩原因：
 * 						一次请求只能向客户端返回一个流，此时是不可能生成多个excel的，需要压缩再返回，直接返回一个zip
 * 
 * 备注：https://www.cnblogs.com/wangjianguang/p/7112391.html
 * 	(1).xls为2003版本--使用HSSFWorkbook生成Workbook实例
 * 			--所有sheet页加起来支持最大65535条数据
 * 	(2).xlsx为2007版本--使用new XSSFWorkbook()和new SXSSFWorkbook(100)都可以生成Workbook实例（后者可高效处理大量数据）
 * 			--所有sheet页加起来支持最大1048576条数据
 * @author 56525
 *
 */
public class ExcelUtil2 {

	/**
	 * 导出excel
	 * 	所有的sheet加起来不能超过1048576行
	 * 	这里没做处理，按ExcelUtil1处理即可
	 * 
	 * @param request
	 * 				请求参数
	 * @param response
	 * 				用于写出返回excel字节流
	 * @param list
	 * 				excel表格内容
	 * @param fileName
	 * 				excel文件名
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings({ "rawtypes", "resource", "deprecation" })
	public static void exportData(HttpServletRequest request, HttpServletResponse response, List list, String fileName)
			throws UnsupportedEncodingException {
		String agent = request.getHeader("USER-AGENT").toLowerCase();
		// 每个浏览器处理excel文件中文名称方式不一样
		if (agent.contains("firefox")) {// 火狐浏览器
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
		} else {// 其他浏览器
			fileName = URLEncoder.encode(fileName, "UTF-8");
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		}
		OutputStream ouputStream = null;

		// 创建工作空间
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
		try {
			ouputStream = response.getOutputStream();
			wb.write(ouputStream);
		} catch (Exception e) {
			System.out.println("excel下载异常:" + e.getMessage());
		} finally {
			try {
				ouputStream.flush();
				ouputStream.close();
			} catch (Exception e) {
				System.out.println("流关闭异常:" + e.getMessage());
			}
		}
	}
}