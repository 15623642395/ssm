package javaProject.utils.OracleArray;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleDriver;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 * 将Java数组转换为Oracle数组，
 * 	存储过程中要是数组作为入参，则该入参必须是Oracle(java.sql.Array)，而不能是java的数组
 * 
 * 参考：https://www.cnblogs.com/firstdream/p/8631072.html
 * @author admin
 *
 */
public class JavaArrayToOracleArray {
	public static void main(String[] args) throws Exception {
		// 1、获取链接
		DriverManager.registerDriver(new OracleDriver());
		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:myOracle", "zhuhao", "zhuhao");

		CallableStatement stmt = conn.prepareCall("{call pkg_ca_wechat_ws.p_query_dept_by_arr(?,?,?,?)}");
		// 2、获取Oracle中创建的数组类型
		ArrayDescriptor arrDesc = ArrayDescriptor.createDescriptor("TYP_CA_VARCHAR2", conn);
		// 3、将java中的数组类型转换为Oracle中的数组类型
		String[] data = { "2", "3" };
		Array array = new ARRAY(arrDesc, conn, data);
		// 3、给存储过程设置参数
		// 3.1、设置入参,给第一个问号设值
		stmt.setArray(1, array);
		// 3.2、设置出参，给2，3，4三个参数设值
		stmt.registerOutParameter(2, OracleTypes.CURSOR);// 游标类型
		stmt.registerOutParameter(3, OracleTypes.NUMBER);
		stmt.registerOutParameter(4, OracleTypes.VARBINARY);
		// 4、执行调用
		stmt.execute();
		// 5、取出结果
		// 取出结果
		ResultSet resultSet = stmt.getResultSet();
		int out_dm = stmt.getInt(3);
		String out_msg = stmt.getString(4);
		System.out.println(resultSet);
		System.out.println(out_dm);
		System.out.println(out_msg);
		 conn.commit();
		conn.close();
	}
}
