package javaProject.jvm.oom;

/**
 * 演示栈StackOverflowError溢出
 * 	1、不能在main方法中使用while循环调用
 * 		原因：每次调用failStack()方法，因为该方法内未作任何其他处理或是方法调用
 * 			    所以每次执行到failStack()方法后就会立刻出栈
 * 	2、调用stack()能演示溢出
 * 		原因：mian方法中调用stack()后，stack()方法中调用自己，
 * 			   此时第一个stack()未完成就会将递归中的stack()压栈，
 * 			   每次执行的都是递归中的stack()方法，但是每次都调用不完
 * @author 56525
 *
 */
public class StackOOM {

	public static void stack() {
		stack();
	}

	public static void main(String[] args) {
//		while(true){
//			failStack();
//		}
		stack();
	}
	
	public static void failStack() {
	}
	
}
