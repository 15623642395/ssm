package javaProject.jvm.oom;

/**
 * 年轻代对象转到老年代
 * 	1、设置参数
 * 		VM参数:-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution 
 * @author 56525
 *
 */
public class TurnOldGeneration {
	private static final int _1MB = 1024 * 1024;

	@SuppressWarnings("unused")
	public static void main(String[] arg) {
		// 什么时候进入老年代取决于XX:MaxTenuringThreshold设置
		byte[] allocation1, allocation2, allocation3;
		allocation1 = new byte[_1MB]; // 该实例会在第一次GC时优先放入到老年代中
		allocation2 = new byte[4 * _1MB];// 该实例会在第一次GC时优先放入到老年代中
		allocation3 = new byte[4 * _1MB];// Eden区内存不足会调用一次GC
		allocation3 = null;
		allocation3 = new byte[4 * _1MB];
	}
}
