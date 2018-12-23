package com.kingdee.uranus.schedule;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kingdee.uranus.controller.param.KnowledgeSearchParam;
import com.kingdee.uranus.core.PageBean;
import com.kingdee.uranus.model.Knowledge;
import com.kingdee.uranus.redis.RedisPool;
import com.kingdee.uranus.search.DocumentSearch;
import com.kingdee.uranus.service.KnowledgeService;
import com.kingdee.uranus.util.MysqlUtil;
import com.kingdee.uranus.util.MysqlUtil.ConnectSet;

import redis.clients.jedis.Jedis;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月19日 下午6:55:48
 * @version
 */
@Component
public class Schedule {

	private static final Logger logger = LoggerFactory.getLogger(Schedule.class);

	@Resource
	private KnowledgeService knowledageService;

	@Resource
	private DocumentSearch documentSearch;

	@Scheduled(cron = "0/45 * * * * ?")
	public void setManager() {

		List<ConnectSet> list = MysqlUtil.list;

		list.forEach(v -> {
			setRedisKeyValue(v);
		});

		logger.info("设置管理员完成！！！！！！！！！！！！！！");

	}

	private void setRedisKeyValue(ConnectSet connectSet) {
		Jedis jedis = RedisPool.getRedisPool(connectSet.getRedisUrl(), 0).getResource();
		// 设置超级管理员
		Set<String> keys = jedis.keys("*SUPERUSER*");

		List<Long> userIds = MysqlUtil.getUserIdByNames(connectSet);

		keys.stream().forEach(v -> {
			userIds.stream().forEach(c -> {
				jedis.hset(v, c.toString(), "1");
			});
		});

		// 设置admin用户
		keys = jedis.keys("*BS_PERM_USER_APP_*");
		keys.stream().forEach(v -> {
			userIds.stream().forEach(c -> {
				String value = jedis.hget(v, c.toString());
				List<String> array = JSONObject.parseArray(value, String.class);
				if (array == null) {
					array = Lists.newArrayList();
				}

				setAllMenu(array, connectSet);

				array.add("83bfebc8000037ac");
				array.add("4715a0df000000ac");
				jedis.hset(v, c.toString(), JSONObject.toJSONString(array));

			});
		});

		jedis.close();
	}

	private void setAllMenu(List<String> array, ConnectSet connectSet) {

		List<String> menuIds = MysqlUtil.getAllMenuIds(connectSet);

		array.addAll(menuIds);
	}

	private void addIndex() {
		PageBean<Knowledge> knowledgeList = knowledageService.getKnowledgeList(new KnowledgeSearchParam());

		for (Knowledge knowledge : knowledgeList.getData()) {

			Knowledge bugDetail = knowledageService.getBugDetail(knowledge.getId());

			documentSearch.addDocument("knowledgeinfo", bugDetail);
		}

	}

	public static void main(String[] args) {
		Schedule s = new Schedule();
		s.setManager();
	}
}
