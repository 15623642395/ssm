package javaProject.utils.mysqlBlob;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * mysql中插入blob字段
 * 	注：虽然mysql longBlob支持4G但是，mysq会存在默认大小4M的限制
 * 	通过命令：show variables like 'max_allowed_packet';查看大小
 * 	需要修改
 * 		https://blog.csdn.net/hellokandy/article/details/82014265
 * 		找到MySql Server的安装目录，打开my.ini文件，在[mysqld]下面，增加一行：max_allowed_packet=2M
 * @author 56525
 *
 */
public class InsertBlob {

	/**
	 *mysql插入blob
	 */
	public void getBlob() {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = JDBCTools.getConnection();
			String sql = "INSERT INTO t_fileUpload(file_name,file_content) VALUES(?,?)";
			ps = con.prepareStatement(sql);
			ps.setString(1, "1.txt");
			InputStream in = new FileInputStream("D:\\file\\fileLoad\\1.pdf");// 生成被插入文件的节点流
			// 设置Blob
			ps.setBlob(2, in);
			ps.executeUpdate();
			System.out.println("存储文件到数据库完成");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(con, ps);
		}
	}

	/**
	 * 建立jdbc连接
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
		 * 释放连接
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
		InsertBlob insertBlob = new InsertBlob();
		insertBlob.getBlob();
	}
}
