package javaProject.utils;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * 使用哨兵模式连接redis
 * @author 56525
 *
 */
public class JedisSentinelPoolUtil {
	private static volatile JedisSentinelPool jedisSentinelPool = null;
	private static Set<String> sentinels = new HashSet<String>();
	private static JedisPoolConfig gPoolConfig = new JedisPoolConfig();
	static {
		gPoolConfig.setMaxIdle(10);// 最大空闲连接数为10
		gPoolConfig.setMaxTotal(10);// 最大连接数为10
		gPoolConfig.setMaxWaitMillis(-1); // 设置最大等待毫秒数：无限制
		gPoolConfig.setTestOnBorrow(true); // 获取连接时是否检查连接的有效性：是
		gPoolConfig.setTestWhileIdle(true); // 空闲时是否检查连接的有效性：是
	}

	/**
	 * 单例模式使用连接池
	 * 	
	 * 注意:	 
	 *		1、因为现在是一台机器上连接三个redis，所有哨兵模式下会给每个redis重新分配ip(三台物理机上就不会了)
	 *		2、会导致主从互换后配置的住居ip变为了系统自己分配的ip，从而连接失败
	 *		3、出现连接失败可以将H:\资料\资料3_2018-xxxx\004、redis\sentinel-26379.conf替换虚拟机中的文件
	 *		4、配置完之后重启docker、重启本级tomcat即可
	 * 
	 * @return
	 */
	public static JedisSentinelPool getJedisSentinelPool() {
		// 使用饿汉式
		if (null == jedisSentinelPool) {
			synchronized (JedisSentinelPoolUtil.class) {
				if (null == jedisSentinelPool) {
					// 所有的哨兵，docker中只配置一个哨兵，实际中，会在每个redis服务器中都会配置一个哨兵
					// 因为可能一台服务器中宕机导致该台服务器中redis和哨兵都挂了，所以每台服务器上每个redis都会配一个哨兵
					String hostAndPort1 = "192.168.10.12:26379";// 哨兵1
					// String hostAndPort2 = "192.168.10.12:26380";//哨兵2
					// String hostAndPort3 = "192.168.10.12:26381";//哨兵3
					sentinels.add(hostAndPort1);
					// sentinels.add(hostAndPort2);
					// sentinels.add(hostAndPort3);
					String clusterName = "mymaster";// sentinel.conf中的主机名称
					// 配置连接池

					jedisSentinelPool = new JedisSentinelPool(clusterName, sentinels, gPoolConfig);
				}
			}
		}
		return jedisSentinelPool;
	}

}
