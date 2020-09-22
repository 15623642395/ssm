package javaProject.jvm.oom;

/**
 * 验证HeapOOM01中while其实时多个方法不停的调用
 * 	-verbose:gc  -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails  -XX:SurvivorRatio=8
 * 使用递归使得每个对象的引用都会继续存在，对象不会被GC
 * @author 56525
 *
 */
public class HeapOOM02 {
	public static void main(String[] args) {
		headOOM();
	}

	@SuppressWarnings("unused")
	public static void headOOM() {
		HeadOOM headOOM = new HeadOOM();
		headOOM();
	}

}

class HeadOOM {
	byte[] a = new byte[1 * 1024 * 1024];
}
