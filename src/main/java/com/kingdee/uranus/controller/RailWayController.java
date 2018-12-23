package com.kingdee.uranus.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.domain.Bug;
import com.kingdee.uranus.redis.RedisPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/api/railway")
public class RailWayController {

	private static final String RUN_LOG_LIST_KEY = "RAILWAY_RUN_LOG_LIST";

	@RequestMapping("run")
	public void run(HttpServletRequest request, String id) {

	}

	@RequestMapping("getRunLogList")
	@ResponseBody
	public PageResult<String> getRunLogList(String railwayName) {
		String url = "";
		JedisPool jedisPool = RedisPool.getRedisPool(url, 6379);
		Jedis jedis = jedisPool.getResource();
		Long llen = jedis.llen(RUN_LOG_LIST_KEY);
		List<String> data = null;
		if (llen >= 5) {
			data = jedis.lrange(RUN_LOG_LIST_KEY, llen - 5, llen);
		} else {
			data = jedis.lrange(RUN_LOG_LIST_KEY, 0, llen);
		}

		return new PageResult<>(5, data);
	}

	@RequestMapping("getRunLog")
	@ResponseBody
	public String getRunLog(String runId) {

		return null;
	}
}
