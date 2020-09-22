package javaProject.utils.exportExcel.batch.test;

import javaProject.utils.exportExcel.batch.excel2003.ExcelRowReader;
import javaProject.utils.exportExcel.batch.excel2003.ExcelXlsReader;
import javaProject.utils.exportExcel.batch.excel2003.IExcelRowReader;
import javaProject.utils.exportExcel.batch.excel2007.ExcelXlsxReader;

/**
 * 用于测试excel2003和2007
 * @author 56525
 *
 */
public class ExcelTest {
	// excel2003扩展名
	public static final String EXCEL03_EXTENSION = ".xls";
	// excel2007扩展名
	public static final String EXCEL07_EXTENSION = ".xlsx";

	/**
	 * 读取Excel文件，可能是03也可能是07版本
	 * 
	 * @param excel03
	 * @param excel07
	 * @param fileName
	 * @throws Exception
	 */
	public static void readExcel(IExcelRowReader reader, String fileName) throws Exception {
		// 处理excel2003文件
		if (fileName.endsWith(EXCEL03_EXTENSION)) {
			ExcelXlsReader exceXls = new ExcelXlsReader();
			exceXls.setRowReader(reader);
			exceXls.process(fileName);
			// 处理excel2007文件
		} else if (fileName.endsWith(EXCEL07_EXTENSION)) {
			ExcelXlsxReader exceXlsx = new ExcelXlsxReader();
			exceXlsx.setRowReader(reader);
			exceXlsx.process(fileName);
		} else {
			throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
		}
	}

	/**
	 * 测试excel2003和2007
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		IExcelRowReader rowReader = new ExcelRowReader();
//		String fileName="E:"+File.separator+"1.xlsx";
		String fileName="C:\\Users\\56525\\Desktop\\1.xlsx";
		ExcelTest.readExcel(rowReader, fileName);
	}
}
