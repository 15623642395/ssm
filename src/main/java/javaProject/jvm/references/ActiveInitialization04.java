package javaProject.jvm.references;
/**
 * 当虚拟机启动时，用户需要指定一个要执行的主类（包含main（）方法的那个 类），虚拟机会先初始化这个主类。 
 * @author 56525
 *
 */
public class ActiveInitialization04 {
	static{
		System.out.println("我被主动初始化了");
	}
	public static void main(String[] args) {
		
	}
}
