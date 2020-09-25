package javaProject.utils.OracleArray;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleDriver;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 * 将JAVA数组转换成ORACLE数组
 * 参考：https://www.cnblogs.com/firstdream/p/8631072.html
 * @param mDP
 * @param conn
 * @param oracleType
 * @param objarr
 *            数组
 * @return
 * @throws Exception
 */
public class OracleArrayUtil {

	/**
	 * 将java数组转化为oracle数组
	 * @param javaArray
	 * @return
	 * @throws SQLException
	 */
	public static Array castToOracleArray(Object[] objects) throws SQLException {
		Array array = null;
		Connection conn = null;
		try {
			// 1、获取oracle连接
			DriverManager.registerDriver(new OracleDriver());
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:myOracle", "zhuhao", "zhuhao");
			// 2、Oracle中创建的数组名称，在plsql中的Types中可以找到的数组名称：全大写
			// ArrayDescriptor arrDesc =
			// ArrayDescriptor.createDescriptor("TYP_CA_VARCHAR2", conn);
			ArrayDescriptor arrDesc = ArrayDescriptor.createDescriptor("ORACLE_ARRAY", conn);
			// 3、将Java数组转换为Oracle数组,TYP_CA_VARCHAR2类型
			array = new ARRAY(arrDesc, conn, objects);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

		return array;
	}
}
