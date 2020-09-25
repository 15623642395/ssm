package javaProject.jvm.references;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.List;

/**
 * 	当使用JDK 1.7的动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后
		 的解析结果REF_getStatic、REF_putStatic、REF_invokeStatic的方法句柄，
		 并且这个方法句柄 所对应的类没有进行过初始化，则需要先触发其初始化。
 * @author 56525
 *
 */
public class ActiveInitialization05 {
	public static void main(String[] args) throws Throwable {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		MethodHandle mh = lookup.findStatic(MethodHandleTest.class, "doubleVal",
				MethodType.methodType(int.class, int.class));
		List<Integer> dataList = Arrays.asList(1, 2, 3, 4, 5);
		MethodHandleTest.transform(dataList, mh);// 方法做为参数
		// for (Integer data : dataList) {
		// System.out.println(data);// 2,4,6,8,10
		// }
	}

}

class MethodHandleTest {
	static {
		System.out.println("我MethodHandleTest被主动初始化");
	}

	public static List<Integer> transform(List<Integer> dataList, MethodHandle handle) throws Throwable {
		for (int i = 0; i < dataList.size(); i++) {
			dataList.set(i, (Integer) handle.invoke(dataList.get(i)));// invoke
		}
		return dataList;
	}

	public static int doubleVal(int val) {
		return val * 2;
	}
}
