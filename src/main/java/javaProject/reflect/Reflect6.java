package javaProject.reflect;

import java.util.Collection;
import java.util.HashSet;

/**
 * 演示ArrayList、HashSet比较以及Hashcode
 * 	1、比较不重写和重写hashcode和equal的结果
 * 	2、默认的hashcode计算的是内存地址
 * 	3、重写后的hashcode计算的是一个变量的值，见Person
 * 	4、所以重写后的输出值是2，不重写后的输出值是3
 * 注：重新equals一定要重写hashcode，否则意义不大
 * 
 * HashSet比较规则
 * 	1、每当需要对比的时候，首先用hashCode()去对比，如果hashCode()不一样，
 *      则表示这两个对象肯定不相等（也就是不必再用equals()去再对比了）
 *   2、如果hashCode()相同，equals未必相同，此时再对比他们的equals()
 *
 *测试步骤
 *1、将重写的hashcode去掉，循环10亿次可以看到结果一直会是3，因为比较的是内存地址，不同的对象绝对不一样，hashcode不同则equals不需要比较了(机制是这样的)
 *2、保留hashcode方法，可以看到hashcode的返回值就是一个变量的值，此时比较的不再是内存地址，而是重写的hashcode
 *	     而此时person1和person3的x和y都一样，所以hashcode的值都会一样，又因为equals比较也一样，
 *  所以person1和person3在存入HashSet时会被判断时一样的，所以输出2
 *
 * @author 56525
 *
 */
public class Reflect6 {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		for (int i = 0; i < 10000; i++) {
			Collection collection = new HashSet<>();
			Person person1 = new Person(1, 1);
			Person person2 = new Person(2, 2);
			Person person3 = new Person(1, 1);
			collection.add(person1);
			collection.add(person2);
			collection.add(person3);
			// 比较不重写和重写hashcode和equal的结果
			if(collection.size()==2){
				System.out.println("2");
			}else if(collection.size()==3){
				System.out.println("3");
			}
		}
	}
}
