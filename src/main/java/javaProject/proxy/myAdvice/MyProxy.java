package javaProject.proxy.myAdvice;

public class MyProxy implements MyProxyInteface {

	@Override
	public String sayHello() {
		return "Hello";
	}
}
