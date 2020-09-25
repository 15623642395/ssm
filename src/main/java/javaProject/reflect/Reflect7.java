package javaProject.reflect;

import java.util.Collection;
import java.util.HashSet;

/**
 * 修改参与哈希值的字段会影响对象的删除等操作
 * 
 * @author 56525
 *
 */
public class Reflect7 {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {

		Collection collection = new HashSet<>();
		Person1 person1 = new Person1(1, 1);
		Person1 person2 = new Person1(2, 2);
		collection.add(person1);
		collection.add(person2);
		// 改变对象的值,再移除person1，发现值还是2；注掉person1.x=3(不要设置为跟person2的一样);发现移除了，值为1
		// 因为改变参与哈希值计算的字段，内存地址改变了，用原来对象引用的地址person1肯定是删除不掉的
		person1.x = 3;
		System.out.println(collection.size());
		collection.remove(person1);
		System.out.println(collection.size());

	}
}
