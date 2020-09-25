package javaProject.generic;

/**
 * 自定义泛型的应用
 * 	1、在方法返回值前加上<T>
 * 	2、调用时T只能是引用类型而不能是8种基本类型，以及8种类型的变形,如：int[],float[]
 * 	3、返回值和入参都可以定义为泛型T，也可以是任意一种指定类型String、int等
 * 	
 * @author 56525
 *
 */
public class Generic2 {
	public static void main(String[] args) {
		String string[] = new String[] { "1", "2", "3" };
		svap(string, 0, 1);
		// 只能是引用类型，基本类型会报错
		// int test[] = new int[] { 1, 2, 3 };
		// svap(test, 0, 1);
		System.out.println(string[0] + "," + string[1]);
		Object object = "13";
		String string2 = autoConvert(object);
		System.out.println(string2);
	}

	/**
	 * 交换任意数组中的任意两个元素
	 * 
	 * @param x
	 * 	数组
	 * @param m
	 * 	数组下标1
	 * @param n
	 * 	数组下标2
	 */
	private static <T> void svap(T[] x, int m, int n) {
		T tmp = x[m];
		x[m] = x[n];
		x[n] = tmp;
	}

	/**
	 * 将任意类型进行转换
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T autoConvert(Object object) {
		return (T) object;
	}
}
