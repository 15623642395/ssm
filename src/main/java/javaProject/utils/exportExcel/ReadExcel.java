package javaProject.utils.exportExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 使用2007的API解析excel2007
 * @author 56525
 *
 */
public class ReadExcel {

	public static void main(String[] args) throws IOException {
		// String filePath = "C:\\Users\\56525\\Desktop\\1.xls";
		String filePath = "C:\\Users\\56525\\Desktop\\1.xlsx";
		boolean isExcel2003 = filePath.toLowerCase().endsWith("xls") ? true : false;
		int sheetNum = 0;// 工作区间
		List<String[]> datas = new ArrayList<String[]>();// 用来存数据

		if (isExcel2003) {
			datas = readXLS(filePath, sheetNum);
		} else {
			datas = readXLSX(filePath, sheetNum);
		}
		for (int i = 0; i < datas.size(); i++) {
			String[] strings = datas.get(i);
			for (int j = 0; j < strings.length; j++) {
				System.out.println(strings[j]);
			}
		}
		System.out.println(datas);
		// return datas;
	}

	@SuppressWarnings("resource")
	private static List<String[]> readXLS(String filePath, int sheetNum) throws IOException {
		FileInputStream inputStream = new FileInputStream(filePath);
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

		List<String[]> datas = new ArrayList<String[]>();// 用来存数据

		/*** step1: 获取Excel的工作区间总数*/
		int sheetNo = workbook.getNumberOfSheets();// 取得工作区间的个数（一个Excel默认的sheet有三个）
		for (int i = 0; i < sheetNo; i++) {

			if (i != sheetNum) {// 判断是否为需要取得工作区间
				continue;
			}

			/*** step2：取得所需工作区间(下标从0开始) */
			HSSFSheet sheet = workbook.getSheetAt(i);
			if (sheet == null) {
				return datas;
			}

			/*** step3：获取总共有多少行数据因为中间空行的话，则读取出来的数据不准确 */
			int hasRowNum = sheet.getPhysicalNumberOfRows();
			if (hasRowNum == 0) {// sheet中所有行都没有内容
				return datas;
			}
			// 已经处理了的行数
			int procssedNum = 0;
			for (int j = 0;; j++) {
				/** step4: 获取每一行 */
				HSSFRow row = sheet.getRow(j);
				System.out.println(j);
				/** step5 : 去除空行 */
				if (row != null) {
					/** step6: 获取每一行的长度 */
					int length = row.getLastCellNum();
					if (length > 0) {
						String[] data = new String[length];// 定义一个集合，装每一行的数值
						for (int m = 0; m < length; m++) {
							/** step7: 获取每一行的每一列的值 */
							data[m] = row.getCell(m).toString();
						}
						/** step8: 存数据 */
						datas.add(data);
					}
					procssedNum++;
					if (procssedNum == hasRowNum) {// 当处理完所有的数据，终止循环
						break;
					}
				}

			}
		}
		/** step9: 关闭输入流 */
		inputStream.close();
		/** step10: 返回数据 */
		return datas;
	}

	@SuppressWarnings("resource")
	private static List<String[]> readXLSX(String filePath, int sheetNum) throws IOException {
		FileInputStream inputStream = new FileInputStream(new File(filePath));
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

		List<String[]> datas = new ArrayList<String[]>();// 定义一个list用来存数据

		/*** step1: 获取Excel的工作区间的数量*/
		int sheetNo = workbook.getNumberOfSheets();

		for (int i = 0; i < sheetNo; i++) {
			if (i != sheetNum) {
				continue;
			}
			/** step2: 获取某一工作区间 */
			XSSFSheet sheet = workbook.getSheetAt(i);
			if (sheet == null) {
				return datas;
			}
			/** step3: 获取总行数(含空行) */
			int allRow = sheet.getPhysicalNumberOfRows();
			if (allRow == 0) {
				return datas;
			}
			/** step4: 取每一行的数据 */
			for (int j = 0; j < allRow; j++) {
				XSSFRow row = sheet.getRow(j);
				/** step5: 去空行 */
				if (row == null) {
					continue;
				}
				/** step6: 取每一行的长度 */
				int length = row.getLastCellNum();

				String[] data = new String[length];// 定义一个数组用来存数据
				/** step7: 取每一列的数据 */
				for (int k = 0; k < length; k++) {
					XSSFCell cell = row.getCell(k);
					data[k] = cell.toString();
				}
				/** step8: 存数据 */
				datas.add(data);
			}
			/** step9: 关闭输入流 */
			inputStream.close();
		}
		/** step10: 返回数据 */
		return datas;
	}
}
