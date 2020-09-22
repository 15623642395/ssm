package javaProject.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.ResultSetMetaData;

/**
 * 将ResultSet转换为list
 * 
 * @author 56525
 *
 */
public class ResultSetToListUtils {
	/*
	 * 将ResultSet转换为list
	 */
	public static List<Map<String, Object>> convertToList(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
		int columnCount = md.getColumnCount();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		List<Map<String, Object>> resultList = list;
		return resultList;
	}

	/*
	 * 单条查询将ResultSet转换为map
	 */
	public static Map<String, Object> convertToMap(ResultSet rs) throws SQLException {
		ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
		int columnCount = md.getColumnCount();
		Map<String, Object> rowData = new HashMap<String, Object>();
		while (rs.next()) {
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
		}
		return rowData;
	}

}
