package javaProject.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用map赋值时注意事项
 * 	map赋值不能直接等，要用putAll赋值
 * @author 56525
 *
 */
public class MapUtil {
	public static void main(String[] args) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		map1.put("k1", 1);
		// 使用putAll
		map2.putAll(map1);
		map2.put("k2", 2);
		// 使用等号
		map3 = map1;
		map3.put("k3", 3);
		// 注意map1的变化,可知map3要是使用等号赋值，那么map3put值之后map1也会相应put进去
		System.out.println("map1:" + map1);
		System.out.println("map2:" + map2);
		System.out.println("map3:" + map3);
	}
}
