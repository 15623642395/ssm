package javaProject.jvm.oom;
/**
 * 查看main所处的对象在main方法压栈时是否被实例化
 * 	1、打印GC日志-verbose:gc  -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 *		2、main方法中分别new本类和不new本类
 *		3、比较二者的内存占用情况
 *		4、设置参数-XX:+TraceClassLoading可查看是否被加载进虚拟机
 *结论：
 *		执行main方法是不实例化本类，但类信息会被加载进虚拟机，设置参数-XX:+TraceClassLoading可查看是否被加载进虚拟机
 * @author 56525
 *
 */
public class CheckObject {
	byte[] bs = new byte[2 * 1024 * 1024];

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		CheckObject checkObject=new CheckObject();
	}
}
