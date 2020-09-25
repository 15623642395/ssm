package javaProject.jvm.oom;

/**
 * 让new出来的对象直接放入到老年代中
 * 	1、通过参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:PretenureSizeThreshold=3145728‬ 单位是byte
 * 	2、此处设置为3M
 * 	3、改变bs大小，大于3M和小于3M查看内存情况
 * @author 56525
 *
 */
public class OldGeneration02 {
	byte[] bs = new byte[3 * 1024 * 1024];

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		OldGeneration02 oldGeneration02 = new OldGeneration02();
	}
}
