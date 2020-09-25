package javaProject.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class HttpUtil {

	/**
	 * java发送Http  Post json请求
	 * 
	 * @param strURL
	 *            服务地址
	 * @param params
	 *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
	 * @return 成功:返回json字符串<br/>
	 */
	public static JSONObject post(String strURL, String params) {
		BufferedReader reader = null;
		OutputStreamWriter out = null;
		JSONObject jsonObject = null;
		try {
			URL url = new URL(strURL);// 创建连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("POST"); // 设置请求方式
			connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
			connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
			connection.connect();
			// 一定要用BufferedReader 来接收响应， 使用字节来接收响应的方法是接收不到内容的
			out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
			out.append(params);
			out.flush();
			// 读取响应
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;
			String res = "";
			while ((line = reader.readLine()) != null) {
				res += line;
			}
			jsonObject = new JSONObject(res);
		} catch (Exception e) {
			System.out.println("Http请求错误：" + e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				reader.close();
			} catch (Exception e) {
				System.out.println("http调用出错：" + e.getMessage());
			}
		}
		return jsonObject;
	}
}