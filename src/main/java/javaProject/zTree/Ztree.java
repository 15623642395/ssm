package javaProject.zTree;

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
 * 迭代——树形结构
 * 此外，可以直接通过sql的递归查询得到
 * @author 56525
 *
 */
public class Ztree {
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Ztree tZtreeTest = new Ztree();
		List<Map<String, String>> list = tZtreeTest.getChildNodes("0");
		System.out.println(list);
	}

	/**
	 * 根据根节点pId进行查询
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public List<Map<String, String>> getChildNodes(String pId) throws ClassNotFoundException, SQLException {
		String sql = "select * from sys_menu where status=1 and PARENT_ID='" + pId + "'";
		ResultSet res = connection(sql);
		// 获取根节点的id，并将根节点加入到集合中
		while (res.next()) {
			String rootId = res.getString("ID");
			Map<String, String> map = new HashMap<String, String>();
			map.put("ID", res.getString("ID"));
			map.put("PID", res.getString("PARENT_ID"));
			map.put("NAME", res.getString("MENU_NAME"));
			map.put("OPEN", "true");
			// 根据根节点的id去查询旗下的所有数据
			list = getAllData(rootId);
			list.add(map);
		}
		return list;
	}

	/**
	 * 查询节点下的所有数据
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private List<Map<String, String>> getAllData(String pId) throws ClassNotFoundException, SQLException {
		String sql = "select * from test.sys_menu a where status=1 and a.PARENT_ID='" + pId + "'";
		ResultSet res = connection(sql);
		while (res.next()) {
			String isLeafNode = res.getString("IS_LEAF_NODE");
			String id = res.getString("ID");
			// 如果是节点进行迭代，不是根节点直接加入list中
			if ("1".equals(isLeafNode)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("ID", res.getString("ID"));
				map.put("PID", res.getString("PARENT_ID"));
				map.put("NAME", res.getString("MENU_NAME"));
				list.add(map);
			} else if ("0".equals(isLeafNode)) {// 不是节点的直接加入到list并迭代进行查询旗下的子节点
				Map<String, String> map = new HashMap<String, String>();
				map.put("ID", res.getString("ID"));
				map.put("PID", res.getString("PARENT_ID"));
				map.put("NAME", res.getString("MENU_NAME"));
				map.put("OPEN", "true");
				list.add(map);
				getAllData(id);
			}
		}
		return list;
	}

	/**
	 * 创建数据库连接
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private ResultSet connection(String sql) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?characterEncoding=UTF-8",
				"root", "zhuhao");
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		return resultSet;
	}
}
