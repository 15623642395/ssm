package javaProject.utils.mysqlBlob;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GetBlob {
	/**
	 * mysql读取blob字段
	 */
	@SuppressWarnings("resource")
	public void getBlob() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = JDBCTools.getConnection();
			String sql = "SELECT * FROM t_fileUpload WHERE id=4";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				String fileName = rs.getString(2);
				Blob content = rs.getBlob(3);// 得到Blob对象
				// 开始读入文件
				InputStream in = content.getBinaryStream();
				OutputStream out = new FileOutputStream("D:\\file\\fileload\\getFile\\" + fileName);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				System.out.println("获取blob文件完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建jdbc链接
	 * @author 56525
	 *
	 */
	static class JDBCTools {// JDBC工具类 用来建立连接和释放连接
		public static Connection getConnection() throws Exception {// 连接数据库
			String url = "jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8";
			String user = "root";
			String password = "zhuhao";
			return DriverManager.getConnection(url, user, password);
		}

		/**
		 * 释放jdbc链接
		 * @param con
		 * @param state
		 */
		public static void release(Connection con, Statement state) {// 关闭数据库连接
			if (state != null) {
				try {
					state.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		GetBlob getBlob = new GetBlob();
		getBlob.getBlob();
	}
}
