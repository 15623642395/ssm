package javaProject.enumPackage;

/**
 * 实现带有抽象方法的枚举
 * 
 * 1、枚举属性之后写一个抽象方法
 * 
 * 2、枚举属性中通过匿名内部类实现改方法
 * 
 * @author 56525
 *
 */
public class Enum3 {

	public enum TrafficLight {
		// 调用父类的构造方法创建匿名内部类
		// 调用父类的无参构造的匿名内部类Date date=new Date(){}
		// 调用父类的有参构造的匿名内部类Date date=new Date(12){}

		RED(30) {
			public TrafficLight nextLight() {
				return GREEN;
			}
		},
		GREEN(45) {
			public TrafficLight nextLight() {
				return YELLOW;
			}
		},
		YELLOW(5) {
			public TrafficLight nextLight() {
				return RED;
			}
		};
		public abstract TrafficLight nextLight();

		@SuppressWarnings("unused")
		private int time;

		private TrafficLight(int time) {
			this.time = time;
		}
	}
}
