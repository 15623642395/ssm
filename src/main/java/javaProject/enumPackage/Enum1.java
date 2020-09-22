package javaProject.enumPackage;

/**
 * 枚举就是一个类
 * @author 56525
 *
 */
public class Enum1{

	/**
	 * 创建枚举，使用enum关键字，各枚举值使用逗号隔开 WeekDay该对象自定义，
	 * 
	 * enum会自动创建该对象，不需要自己实现
	 * 
	 * 枚举其实是一个个类的对象
	 * 
	 * SUN, MON, TUE, WED, THU, FRI, SAT就是一个个的WeekDay对象
	 */
	public enum WeekDay {
		SUN, MON, TUE, WED, THU, FRI, SAT;
	}

	public static void main(String[] args) {
		WeekDay weekDay = WeekDay.MON;
		// 获取，枚举对象
		System.out.println(weekDay);
		// 获取，枚举对象名称
		System.out.println(weekDay.name());
		// 获取在枚举对象中的下标值，下标从0开始
		System.out.println(weekDay.ordinal());
		// 将传入的字符串转变为枚举对象
		System.out.println(WeekDay.valueOf("SUN"));
		//获取枚举个数
		System.out.println(WeekDay.values().length);
	}
}
