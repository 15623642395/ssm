package javaProject.clsssLoad;

/**
 * 类加载器：
 * 	类加载器就是将.class的文件以二进制形式加载进虚拟机
 * 
 * @author 56525
 *
 */
public class ClassLoad {
	public static void main(String[] args) {
		ClassLoader classLoader = ClassLoad.class.getClassLoader();
		while (classLoader != null) {
			System.out.println(classLoader.getClass().getName());
			classLoader = classLoader.getParent();
			System.out.println("***********************************");
		}
		System.out.println("我是BootStrap类加载器:" + classLoader);
	}
}
