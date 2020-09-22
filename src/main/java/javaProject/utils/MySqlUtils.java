package javaProject.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MySQL增删改查工具类
 * 
 * @author 56525
 *
 */
public class MySqlUtils {

	/**
	 * 数据增加
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static int insert(String sql, Object[] obj) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionUtils.connection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		int resultSet = 0;
		// 分别给每个问号赋值
		if (obj.length > 0) {
			for (int i = 1; i <= obj.length; i++) {
				preparedStatement.setObject(i, obj[i - 1]);
			}
		}
		try {
			resultSet = preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println("增加数据失败，失败原因：" + e.getMessage());
		} finally {
			ConnectionUtils.close();
		}
		return resultSet;
	}

	/**
	 * 数据删除
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static int delete(String sql, Object[] obj) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionUtils.connection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		int resultSet = 0;
		// 分别给每个问号赋值
		if (obj.length > 0) {
			for (int i = 1; i <= obj.length; i++) {
				preparedStatement.setObject(i, obj[i - 1]);
			}
		}
		try {
			resultSet = preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println("删除数据失败，失败原因：" + e.getMessage());
		} finally {
			ConnectionUtils.close();
		}
		return resultSet;
	}

	/**
	 * 数据修改
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static int update(String sql, Object[] obj) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionUtils.connection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		int resultSet = 0;
		// 分别给每个问号赋值
		if (obj.length > 0) {
			for (int i = 1; i <= obj.length; i++) {
				preparedStatement.setObject(i, obj[i - 1]);
			}
		}
		try {
			resultSet = preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println("修改数据失败，失败原因：" + e.getMessage());
		} finally {
			ConnectionUtils.close();
		}
		return resultSet;
	}

	/**
	 * 数据查询resultSet
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static ResultSet queryResultSet(String sql, Object[] obj) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionUtils.connection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = null;
		// 分别给每个问号赋值
		if (obj.length > 0) {
			for (int i = 1; i <= obj.length; i++) {
				preparedStatement.setObject(i, obj[i - 1]);
			}
		}
		try {
			resultSet = preparedStatement.executeQuery();
		} catch (Exception e) {
			System.out.println("查询数据失败，失败原因：" + e.getMessage());
		} finally {
			ConnectionUtils.close();
		}
		return resultSet;
	}

	/**
	 * 数据查询list，适合单条和多条查询
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> queryList(String sql, Object[] obj)
			throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionUtils.connection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 分别给每个问号赋值
		if (obj.length > 0) {
			for (int i = 1; i <= obj.length; i++) {
				preparedStatement.setObject(i, obj[i - 1]);
			}
		}
		try {
			resultSet = preparedStatement.executeQuery();
			list = ResultSetToListUtils.convertToList(resultSet);
		} catch (Exception e) {
			System.out.println("查询数据失败，失败原因：" + e.getMessage());
		} finally {
			ConnectionUtils.close();
		}
		return list;
	}

	/**
	 * 数据查询map，适合单条查询
	 * 
	 * @param sql
	 * @param obj
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Map<String, Object> queryMap(String sql, Object[] obj) throws ClassNotFoundException, SQLException {
		Connection connection = ConnectionUtils.connection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = null;
		Map<String, Object> map = new HashMap<String, Object>();
		// 分别给每个问号赋值
		if (obj.length > 0) {
			for (int i = 1; i <= obj.length; i++) {
				preparedStatement.setObject(i, obj[i - 1]);
			}
		}
		try {
			resultSet = preparedStatement.executeQuery();
			map = ResultSetToListUtils.convertToMap(resultSet);
		} catch (Exception e) {
			System.out.println("查询数据失败，失败原因：" + e.getMessage());
		} finally {
			ConnectionUtils.close();
		}
		return map;
	}
}

/**
 * 数据库连接
 * 
 * @author 56525
 *
 */
class ConnectionUtils {

	/**
	 * 数据库连接
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false",
				"root", "zhuhao");
		return connection;
	}

	/**
	 * 数据库关闭
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void close() throws ClassNotFoundException, SQLException {
		connection().close();
	}

}
