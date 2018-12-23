package com.kingdee.close.bug;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisServiceTest {

	@Test
	public void testConnect() {
		Jedis jedis = new Jedis("172.17.52.49");
		jedis.set("a", "c");
		jedis.close();
	}
}
