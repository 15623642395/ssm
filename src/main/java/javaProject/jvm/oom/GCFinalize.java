package javaProject.jvm.oom;

/**
 * 研究对象不可达之后还能被重新复活
 * 	使用关键字：finalize,一个对象被finalize一次之后再次被GC就不会执行finalize方法了
 * 		该方法一般由垃圾回收器来调用，当我们调用System的gc()方法的时候，由垃圾回收器调用finalize(),但不是无限次调用。 
 *对象的自我救赎
 *		即使在可达性分析算法中不可达的对象，也并非是"非死不可"，这时候它处于待回收阶段，要真正的宣告一个对象的死亡，至少要经历两次标记过程：
 *				第一次，可达性分析后发现对象不可达，将会被第一次标记并进行一次筛选，筛选条件是判断该对象是否有必要执行finalize()方法。
 *			当对象没有覆盖finalize()方法[即没重写finalize方法]或已经被虚拟机调用过finalize()方法，虚拟机将这两种情况称为"没有必要执行"。
 *				第二次，如果这个对象被判定为有必要执行finalize()方法，那么这个对象会被放在一个叫做F-Queue的队列里，
 *			并在稍后由一个虚拟机自动创建的、低优先级的Finalizer线程去执行它。finalize()方法是对象逃脱死亡的最后一次机会，
 *			稍后GC将对F-Queue中的对象进行第二次标记。如果对象在finalize()中成功拯救自己，重新与引用链上的任何一个对象建立了关联，
 *			那么第二次标记将把它移除"即将回收"的集合。否则，就会真正被回收。
 *
 *
 *
 *此代码演示了两点：
 * 	一是：对象可以被GC自我救赎
 * 	二是：这种自我救赎的机会只有一次，因为一个对象的finalize()方法最多只会被系统自动调用一次
 * 
 * @author admin
 *
 */
public class GCFinalize {
	public static GCFinalize obj;

	//分别演示注释掉和保留重写的finalize方法
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("canrelive obj finalize obj");
		System.out.println("this就是这个GCFinalize对象:"+this);
		obj = this;
	}

	public static void main(String[] args) throws InterruptedException {
		obj = new GCFinalize();
		obj = null;
		System.out.println("第一次gc我会执行finalize方法");
		System.gc();
		Thread.sleep(1000);
		if (obj == null) {
			System.out.println("obj 是 null");
		} else {
			System.out.println("obj 可用");
		}
		System.out.println("第二次gc我不会调用finalize方法了");
		obj = null;
		System.gc();
		Thread.sleep(1000);
		if (obj == null) {
			System.out.println("obj 是 null");
		} else {
			System.out.println("obj 可用");
		}
	}

}
