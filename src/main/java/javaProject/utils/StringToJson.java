package javaProject.utils;

import org.json.JSONObject;

public class StringToJson {
	public static void main(String[] args) {
		String json1 = "{\"name\":\"zhuhao\",\"age\":\"20\"}";
		String json2 = "{'name':'zhuhao','age':'20'}";
		JSONObject jsonObj1 = new JSONObject(json1);// 要解析json格式的字符串时使用这个构造方法
		JSONObject jsonObj2 = new JSONObject(json2);// 要解析json格式的字符串时使用这个构造方法
		System.out.println(jsonObj1);
		System.out.println(jsonObj1.get("name"));
		System.out.println(jsonObj2.get("age"));
	}
}
