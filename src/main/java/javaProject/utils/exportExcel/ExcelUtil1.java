package javaProject.utils.exportExcel;

//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.ss.usermodel.BorderStyle;
//import org.apache.poi.ss.usermodel.FillPatternType;
//import org.apache.poi.ss.usermodel.HorizontalAlignment;
//import org.apache.poi.ss.usermodel.VerticalAlignment;
//
//import webProject.emp.entity.Emp;
//
///**
// * 导出excel工具类:使用HSSFWorkbook(excel 2003)--支持6万条
// * 
// * 
// * 	注意：
// * 			0、注意跟ExcelUtil2的maven包只能引入一个
// * 			1、使用poi时，当sheet的行数超过65535时会报错
// * 			2、解决思路，当超过65535行时，新建sheet
// * 
// * 备注：
// * 	(1).xls为2003版本--使用HSSFWorkbook生成Workbook实例
// * 			--所有sheet页加起来支持最大65535条数据
// * 	(2).xlsx为2007版本--使用new XSSFWorkbook()和new SXSSFWorkbook(100)都可以生成Workbook实例（后者可高效处理大量数据）
// * 			--所有sheet页加起来支持最大1048576条数据
// * @author 56525
// *
// */
//
//public class ExcelUtil1 {
//
//	/**
//	 * 导出excel
//	 * 
//	 * @param request
//	 * 				请求参数
//	 * @param response
//	 * 				用于写出返回excel字节流
//	 * @param list
//	 * 				excel表格内容
//	 * @param fileName
//	 * 				excel文件名
//	 * @return
//	 * @throws UnsupportedEncodingException 
//	 */
//
//	@SuppressWarnings({ "rawtypes", "resource" })
//	public static void exportData(HttpServletRequest request, HttpServletResponse response, List list, String fileName)
//			throws UnsupportedEncodingException {
//		String agent = request.getHeader("USER-AGENT").toLowerCase();
//		// 每个浏览器处理excel文件中文名称方式不一样
//		if (agent.contains("firefox")) {// 火狐浏览器
//			response.reset();
//			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//			response.setHeader("Content-Disposition",
//					"attachment;filename=" + new String(fileName.getBytes(), "ISO8859-1"));
//		} else {// 其他浏览器
//			fileName = URLEncoder.encode(fileName, "UTF-8");
//			response.reset();
//			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//		}
//		OutputStream ouputStream = null;
//
//		// 创建工作空间
//		HSSFWorkbook wb = new HSSFWorkbook();
//		// 计算sheet的次数,每个sheet最多65535行
//		int num = list.size() / 65535 + 1;
//		for (int i = 0; i < num; i++) {
//			// 创建表
//			String sheetName = "Sheet" + (i + 1);
//			HSSFSheet sheet = wb.createSheet(sheetName);
//			sheet.setDefaultColumnWidth(20);
//			sheet.setDefaultRowHeightInPoints(20);
//
//			// 创建第一行：表头行
//			HSSFRow row = sheet.createRow(0);
//
//			// 生成一个样式
//			HSSFCellStyle style = wb.createCellStyle();
//			style.setAlignment(HorizontalAlignment.CENTER);// 水平居中
//			style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
//
//			// 背景色
//			style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
//			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//			style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
//
//			// 设置边框
//			style.setBorderBottom(BorderStyle.THIN);
//			style.setBorderLeft(BorderStyle.THIN);
//			style.setBorderRight(BorderStyle.THIN);
//			style.setBorderTop(BorderStyle.THIN);
//
//			// 生成一个字体
//			HSSFFont font = wb.createFont();
//			font.setFontHeightInPoints((short) 10);
//			font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
//			font.setBold(true);
//			font.setFontName("宋体");
//
//			// 把字体 应用到当前样式
//			style.setFont(font);
//
//			// 添加表头数据,表头数据肯定是固定的
//			String[] excelHeader = { "ID", "创建时间", "部门ID", "邮箱", "性别", "登陆账户", "密码", "手机号", "状态", "修改时间", "用户名" };
//			// 将表头数据添加到第一行的所有列中
//			for (int j = 0; j < excelHeader.length; j++) {
//				HSSFCell cell = row.createCell(j);
//				cell.setCellValue(excelHeader[j]);
//				cell.setCellStyle(style);
//			}
//
//			// 添加其他行的数据
//			for (int j = 0; j < 65535; j++) {
//				// 创建其他行
//				row = sheet.createRow(j + 1);
//				Emp emp = (Emp) list.get(j);
//				// 为其他行的每列添加数据
//				row.createCell(0).setCellValue(emp.getId());
//				row.createCell(1).setCellValue(emp.getCreatedtime());
//				row.createCell(2).setCellValue(emp.getDeptid());
//				row.createCell(3).setCellValue(emp.getEmail());
//				row.createCell(4).setCellValue(emp.getGender());
//				row.createCell(5).setCellValue(emp.getLogin_account());
//				row.createCell(6).setCellValue(emp.getPassword());
//				row.createCell(7).setCellValue(emp.getPhone());
//				row.createCell(8).setCellValue(emp.getStatus());
//				row.createCell(9).setCellValue(emp.getUpdatetime());
//				row.createCell(10).setCellValue(emp.getUser_name());
//			}
//		}
//		try {
//			ouputStream = response.getOutputStream();
//			wb.write(ouputStream);
//		} catch (Exception e) {
//			System.out.println("excel下载异常:" + e.getMessage());
//		} finally {
//			try {
//				ouputStream.flush();
//				ouputStream.close();
//			} catch (Exception e) {
//				System.out.println("流关闭异常:" + e.getMessage());
//			}
//		}
//	}
//}
