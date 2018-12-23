package com.kingdee.uranus.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kingdee.uranus.constant.ConfigConstant;
import com.kingdee.uranus.domain.ProductTaskDomain;
import com.kingdee.uranus.domain.SubSystem;
import com.kingdee.uranus.search.DocumentSearch;
import com.kingdee.uranus.service.AddBugService;
import com.kingdee.uranus.service.BugPropertyLoadService;

@Service
public class BugPropertyLoadServiceImpl implements BugPropertyLoadService {

	private static final Logger logger = LoggerFactory.getLogger(BugPropertyLoadServiceImpl.class);

	@Value("${dmp.host}")
	private String dmpHost;

	@Autowired
	private DocumentSearch documentSearch;

	@Autowired
	private AddBugService addBugService;

	@Override
	public void loadProductTask() {

		List<ProductTaskDomain> taskList = addBugService.getAllProductTask();

		logger.info("get all product task size :{}", taskList.size());

		// 加入索引库
		for (ProductTaskDomain productTaskDomain : taskList) {
			documentSearch.addDocument(ConfigConstant.ELASTIC_PRODUCT_TASK_INDEX, productTaskDomain);
		}

	}

	@Override
	public void loadSubSystem() {
		List<SubSystem> subSystemList = addBugService.getAllSubSystem();

		logger.info("get all sub system size :{}", subSystemList.size());

		// 加入索引库
		for (SubSystem subSystem : subSystemList) {
			documentSearch.addDocument(ConfigConstant.ELASTIC_SUB_SYSTEM_INDEX, subSystem);
		}
	}
}
