package javaProject.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 利用读写锁模拟缓存器
 * 
 * @author 56525
 *
 */
public class ThreadLock3Cache {
	private Map<String, Object> cache = new HashMap<String, Object>();

	public static void main(String[] args) {

	}

	private ReadWriteLock rwl = new ReentrantReadWriteLock();

	// 模拟获取缓存数据
	public Object getData(String key) {
		rwl.readLock().lock();
		Object value = null;
		try {
			// 读取缓存数据
			value = cache.get(key);
			if (value == null) {
				rwl.readLock().unlock();
				rwl.writeLock().lock();
				try {
					// 没有缓存则写入缓存
					if (value == null) {
						value = "aaaa";// 实际失去queryDB();
					}
				} finally {
					rwl.writeLock().unlock();
				}
				rwl.readLock().lock();
			}
		} finally {
			rwl.readLock().unlock();
		}
		return value;
	}
}
