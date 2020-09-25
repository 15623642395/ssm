package javaProject.redis;

import javaProject.utils.JedisSentinelPoolUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Transaction;

/**
 * redis事务
 * @author 56525
 *
 */
public class RedisTrans {
	public static void main(String[] args) {
		RedisTrans redisTrans = new RedisTrans();
		// 模拟消费金额，应该由前台表单提交
		System.out.println(redisTrans.transactionUtil(10));
	}

	/**
	 * 事务工具类
	 * 	模拟消费
	 * @return
	 */
	public boolean transactionUtil(int monetary) {
		JedisSentinelPool jedisPool = JedisSentinelPoolUtil.getJedisSentinelPool();
		Transaction transaction = null;
		Jedis jedis = null;
		jedis = jedisPool.getResource();
		int balance = 0;
		// 监听balance有没有被改变
		jedis.watch("balance");
		balance = Integer.valueOf(jedis.get("balance"));
		// 事务开启之前判断是否需要开启事务，不需要开启则unwatch
		if (balance < monetary) {
			jedis.unwatch();
			return false;
		} else {
			try {
				transaction = jedis.multi();// 开启事务
				transaction.decrBy("balance", monetary);
				transaction.incrBy("consume", monetary);
				transaction.exec();
				System.out.println("banlance:" + jedis.get("balance"));
				System.out.println("consume:" + jedis.get("consume"));
				return true;
			} catch (Exception e) {
				transaction.discard();
				return false;
			} finally {
				jedis.close();
			}
		}
	}
}
