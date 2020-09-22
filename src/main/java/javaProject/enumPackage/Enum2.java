package javaProject.enumPackage;

/**
 * 演示枚举中的构造方法
 * https://www.jianshu.com/p/7d3e3f6695a5
 * 1、修饰词只能是private
 * 2、构造方法只能位于枚举值下方
 * 3、枚举值后的参数表示通过对应得构造方法创建对象
 * 4、枚举值都是静态变量，在初始化得时候就会被加载，详见：
 *   F:\Java视频\2-张孝祥进阶技术\Java高级技术\13_用普通类模拟枚举的实现原理.avi
 *   
 * @author 56525
 *
 */
public class Enum2 {
	public enum WeekDay {
		SUN(3), MON, TUE, WED, THU, FRI, SAT;
		private WeekDay() {
			System.out.println("first");
		}

		private WeekDay(int day) {
			System.out.println("second");
		}
	}

	public static void main(String[] args) {
		System.out.println(WeekDay.SUN);
	}
}
