package javaProject.bankCall;

/**
 * 使用单例模式
 *		1、私有的构造方法
 *		2、静态的对象实例方法
 *		3、返回对象的静态方法
 *	原因
 *		1、虽然有全局变量instance但是不会参与运算
 *		2、虽然有全局变量NumberManager，但是他的方法是同步的，不会产生线程不安全问题
 * @author 56525
 *
 */
public class NumberMachine {

	private NumberMachine() {
	}

	private static NumberMachine instance = new NumberMachine();

	public static NumberMachine getInstance() {
		return instance;
	}

	private NumberManager commonManager = new NumberManager();
	private NumberManager expressManager = new NumberManager();
	private NumberManager vipManager = new NumberManager();

	public NumberManager getCommonManager() {
		return commonManager;
	}

	public NumberManager getExpressManager() {
		return expressManager;
	}

	public NumberManager getVipManager() {
		return vipManager;
	}

}
