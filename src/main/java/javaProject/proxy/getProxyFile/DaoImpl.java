package javaProject.proxy.getProxyFile;

/**
 * 演示被代理对象必须实现接口,同时他是可以继承其他对象的
 * @author admin
 *
 */
public class DaoImpl extends DaoFather implements DaoInterface1, DaoInterface2{

	/**
	 * 反编译$Proxy0.class中没有say方法，可知代理对象只会代理接口中的为实现的方法，不会代理自身的方法
	 */
	public void say() {
		System.out.println("say1...");
	}
	
	public void daoFather(){
		System.out.println("daoFather...");
	}

	@Override
	public void daoInterface2() {
		System.out.println("daoInterface2...");

	}

	@Override
	public void daoInterface1() {
		System.out.println("daoInterface1...");

	}

}
