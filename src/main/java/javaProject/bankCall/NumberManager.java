package javaProject.bankCall;

import java.util.ArrayList;
import java.util.List;

/**
 * 1、客户取号生成器
 * 2、窗口叫号器
 * @author 56525
 *
 */
public class NumberManager {
	private int lastNumber = 0;
	private List<Integer> queueNumbers = new ArrayList<Integer>();

	public synchronized Integer generateNewNumber() {
		queueNumbers.add(++lastNumber);
		return lastNumber;
	}

	public synchronized Integer fetchNumber() {
		if (queueNumbers.size() > 0) {
			return (Integer) queueNumbers.remove(0);
		} else {
			return null;
		}
	}
}
