package com.kingdee.uranus.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.core.ResultMap;
import com.kingdee.uranus.model.User;
import com.kingdee.uranus.redis.RedisPool;

import redis.clients.jedis.Jedis;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年5月10日 下午7:15:37
 * @version
 */
@Controller
@RequestMapping("redis/")
public class RedisController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BugController.class);

	@RequestMapping("getAllRedisInfo")
	@ResponseBody
	public PageResult<Map<String, String>> getAllRedisInfo(HttpServletRequest req, String url, String key) {

		if (StringUtils.isEmpty(url) || StringUtils.isEmpty(key)) {
			return new PageResult<>();
		}

		Jedis jedis = RedisPool.getRedisPool(url, 0).getResource();
		User user = getUser(req);
		// if(user.getUserNickname().equals("聂康")){
		Set<String> keys = jedis.keys("*" + key + "*");
		// }else{
		// Set<String> keys = jedis.keys( key);
		// }

		List<Map<String, String>> result = Lists.newArrayList();
		keys.forEach(v -> {
			Map<String, String> map = Maps.newHashMap();
			map.put("key", v);
			map.put("url", url);
			result.add(map);
		});

		PageResult<Map<String, String>> pageResult = new PageResult<>();
		pageResult.setCode(0);
		pageResult.setCount(result.size());
		pageResult.setData(result);
		return pageResult;
	}

	@RequestMapping("getRedisInfo")
	@ResponseBody
	public String getRedisInfo(HttpServletRequest req, String url, String key) {
		Jedis jedis = RedisPool.getRedisPool(url, 0).getResource();
		String value = null;
		try {
			// 获取key对应的数据类型
			String type = jedis.type(key);
			logger.info("key:" + key + " 的类型为：" + type);
			if (type.equals("string")) {
				// get(key)方法返回key所关联的字符串值
				value = jedis.get(key);
			} else if (type.equals("hash")) {
				Map<String, String> map = jedis.hgetAll(key);
				value = JSONObject.toJSONString(map);
			} else if (type.equals("list")) {
				long length = jedis.llen(key);
				List<String> list = jedis.lrange(key, 0, length);
				value = JSONObject.toJSONString(list);
			} else if (type.equals("set")) {
				logger.info(key + "类型为set暂未处理...");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			// 关闭连接
			jedis.close();
		}
		return value;
	}

	@RequestMapping("removeKey")
	@ResponseBody
	public ResultMap removeKey(HttpServletRequest req, String url, String key) {
		Jedis jedis = RedisPool.getRedisPool(url, 0).getResource();
		try {
			// 获取key对应的数据类型
			jedis.del(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResultMap.error(e.getMessage());
		} finally {
			// 关闭连接
			jedis.close();
		}
		return ResultMap.ok();
	}
}
