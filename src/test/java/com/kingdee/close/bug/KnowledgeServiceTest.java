package com.kingdee.close.bug;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kingdee.uranus.UranusServer;
import com.kingdee.uranus.model.Knowledge;
import com.kingdee.uranus.service.KnowledgeService;

@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UranusServer.class)
public class KnowledgeServiceTest {

	@Autowired
	private KnowledgeService service;

	@Test
	public void testUpdate() {
		Knowledge e = new Knowledge();
		e.setId(34L);
		e.setTitle("ABCDE");
		e.setContext("GGGGGG");
		service.update(e);
	}
}
