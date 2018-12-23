package com.kingdee.uranus.redis;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月19日 下午6:53:19
 * @version
 */
public class RedisPool {

	private static final Logger logger = LoggerFactory.getLogger(RedisPool.class);

	public static volatile Map<String, JedisPool> jedisPoolMap = new ConcurrentHashMap<String, JedisPool>();

	public static JedisPool getRedisPool(String url, int port) {
		if (jedisPoolMap.get(url) == null) {
			synchronized (jedisPoolMap) {
				if (jedisPoolMap.get(url) == null) {
					JedisPoolConfig config = new JedisPoolConfig();
					config.setMaxTotal(400);
					config.setMaxIdle(20);
					config.setMaxWaitMillis(30000L);
					config.setMinIdle(5);
					// 在获取连接的时候检查有效性, 默认false
					config.setTestOnBorrow(false);
					// 在空闲时检查有效性, 默认false
					config.setTestWhileIdle(false);
					JedisPool jedisPool = new JedisPool(config, url, port == 0 ? 6379 : port);
					jedisPoolMap.put(url, jedisPool);
				}
			}
		}

		JedisPool jedisPool = jedisPoolMap.get(url);

		logger.info("jedis pool {} ,idle thread number = " + jedisPool.getNumIdle() + ",wait thread number = {}",
				jedisPool.toString(), jedisPool.getNumWaiters());

		return jedisPool;
	}

	public static Jedis getResouce(JedisPool jedisPool) {
		return jedisPool.getResource();
	}

	public static void main(String[] args) {
		JedisPool redisPool = RedisPool.getRedisPool("172.17.4.94", 0);

		Jedis jedis = redisPool.getResource();

		// 1537347340106693917
		Set<String> keys = jedis.keys("*");

		for (String key : keys) {

			if (!key.contains("_meta_data_lock")) {
				System.out.println(key);
				jedis.del(key);
			}

		}
		jedis.close();
	}

}
