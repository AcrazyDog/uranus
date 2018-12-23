package com.kingdee.close.bug;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;
import com.kingdee.uranus.UranusServer;
import com.kingdee.uranus.constant.ConfigConstant;
import com.kingdee.uranus.domain.Bug;
import com.kingdee.uranus.search.DocumentSearch;

@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UranusServer.class)
public class BugServiceTest {

	@Autowired
	private DocumentSearch documentSearch;

	@Test
	public void testIndexBug() {

		Bug bug = new Bug();

		bug.setBugId("1111");
		bug.setBugDesc("222");
		bug.setDetail("333");
		bug.setBugNo("444");

		// 将此BUG写入elasticsearch索引库
		Map<String, Object> params = Maps.newHashMap();
		params.put("bugNo", bug.getBugNo());
		documentSearch.addDocument(ConfigConstant.ELASTIC_BUG_INDEX, bug);
	}
}
