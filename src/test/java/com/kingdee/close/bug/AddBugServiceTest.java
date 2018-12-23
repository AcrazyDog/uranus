package com.kingdee.close.bug;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.uranus.UranusServer;
import com.kingdee.uranus.service.AddBugService;

@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UranusServer.class)
public class AddBugServiceTest {

	@Resource
	private AddBugService service;

	@Test
	public void testGetStageBySubSytemId() {
		List<Map<String, Object>> stageBySubSytemId = service.getStageByGroupId("597806855988735674730210888250043659");

		System.out.println(JSONObject.toJSONString(stageBySubSytemId));
	}

}
