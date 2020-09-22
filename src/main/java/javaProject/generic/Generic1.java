package javaProject.generic;

import java.util.ArrayList;

/**
 * 泛型:
 * 	泛型是给编译器看的，通过反射可以跳过泛型
 * @author 56525
 *
 */
public class Generic1 {
	public static void main(String[] args) throws Exception {
		ArrayList<Integer> arrayList = new ArrayList<>();
		arrayList.add(1);
		// 跳过泛型，给集合加入String
		arrayList.getClass().getMethod("add", Object.class).invoke(arrayList, "abc");
		System.out.println(arrayList.get(1));
	}
}
