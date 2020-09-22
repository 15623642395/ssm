package javaProject.jvm.oom;

/**
 * 在Eden区内存不足，但是堆中内存还存在时，会将后续对象直接放入老年代
 * 	查看日志中老年代的内存大小即可知道
 * 	
 * 	1、Run As 中设置参数-verbose:gc  -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails  -XX:SurvivorRatio=8
 * 	2、Xms20M -Xmx20M	堆内存大小20M且不可扩展
 * 	3、-Xmn10M	年轻代设置为10兆
 * 	4、-XX:+PrintGCDetails	打印GC日志
 * 	5、-XX:SurvivorRatio=8	使伊甸区和幸存区的比例为8:1:1
 * @author 56525
 *
 */
public class OldGeneration01 {

	public static void main(String[] args) {
		testAllocation();
	}

	@SuppressWarnings("unused")
	public static void testAllocation() {
		byte[] allocation1 = new byte[1 * 1024 * 1024];
		byte[] allocation2 = new byte[2 * 1024 * 1024];
		byte[] allocation3 = new byte[2 * 1024 * 1024];
//		// 出现一次Minor GC，Edent区内存不足，但是前三个对象又不能回收掉，则第四个对象放入到老年代中
		byte[] allocation4 = new byte[6 * 1024 * 1024];
	}
}
