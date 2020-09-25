package javaProject.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示对内存溢出
 * 	-verbose:gc  -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * 	1、while中直接new对象为什么不会出现内存溢出
 * 		原因：可以while当作是一个，每次完成之后就会再次调用一个while方法
 * 			  根据可达性分析算法，一个while方法执行完成其内的对象引用就会被回收，所以对象会被GC掉
 * 		根据HeapOOM02可以验证
 * @author 56525
 *
 */
public class HeapOOM01 {

	public static void main(String[] args) {
		List<Menory> list = new ArrayList<Menory>();
		while (true) {
			list.add(new Menory());
		}
	}
}

class Menory {

}
