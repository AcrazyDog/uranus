package com.kingdee.uranus.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingdee.uranus.constant.ConfigConstant;
import com.kingdee.uranus.core.ResultMap;
import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.domain.AddBugParam;
import com.kingdee.uranus.domain.ProductTaskDomain;
import com.kingdee.uranus.domain.SubSystem;
import com.kingdee.uranus.search.DocumentSearch;
import com.kingdee.uranus.search.param.SearchParam;
import com.kingdee.uranus.service.AddBugService;
import com.kingdee.uranus.util.SessionUtil;

@RestController
@RequestMapping("api/add/bug/")
public class AddBugController {

	@Autowired
	private DocumentSearch documentSearch;

	@Autowired
	private AddBugService addBugService;

	private static final Logger logger = LoggerFactory.getLogger(AddBugController.class);

	@GetMapping("getAllProductTask")
	@ResponseBody
	public List<ProductTaskDomain> getAllProductTask() {
		return addBugService.getAllProductTask();
	}

	@GetMapping("getAllSubSystem")
	@ResponseBody
	public List<SubSystem> getAllSubSystem() {
		return addBugService.getAllSubSystem();
	}

	@GetMapping("getStageByGroupId")
	@ResponseBody
	public List<Map<String, Object>> getStageByGroupId(String groupId) {
		return addBugService.getStageByGroupId(groupId);
	}

	@GetMapping("getProductTaskByKey")
	@ResponseBody
	public List<Map<String, Object>> getProductTaskByKey(@RequestParam("term") String keyword) {

		SearchParam param = new SearchParam();
		param.setQueryFileds(Lists.newArrayList("product"));
		param.setQueryStr(keyword);
		param.setIndex(ConfigConstant.ELASTIC_PRODUCT_TASK_INDEX);

		List<Map<String, Object>> result = Lists.newArrayList();

		SearchResponse response = documentSearch.searchDocument(param);
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits(); // 查询结果
		for (SearchHit hit : hits) {

			String source = hit.getSourceAsString();
			ProductTaskDomain productTask = JSONObject.parseObject(source, ProductTaskDomain.class);

			Map<String, Object> map = Maps.newHashMap();
			map.put("id", productTask.getId());
			map.put("result", productTask.getName());
			result.add(map);
		}

		return result;
	}

	@RequestMapping("getSubSystemByKey")
	@ResponseBody
	public List<Map<String, Object>> getSubSystemByKey(String key) {

		SearchParam param = new SearchParam();
		param.setQueryFileds(Lists.newArrayList("group", "name"));
		param.setQueryStr(key);
		param.setIndex(ConfigConstant.ELASTIC_SUB_SYSTEM_INDEX);

		List<Map<String, Object>> result = Lists.newArrayList();

		SearchResponse response = documentSearch.searchDocument(param);
		SearchHits searchHits = response.getHits();
		SearchHit[] hits = searchHits.getHits(); // 查询结果
		for (SearchHit hit : hits) {

			String source = hit.getSourceAsString();
			SubSystem subSystem = JSONObject.parseObject(source, SubSystem.class);

			Map<String, Object> map = Maps.newHashMap();
			map.put("id", subSystem.getId());
			map.put("name", subSystem.getName());
			result.add(map);
		}

		return result;
	}

	@Value("${dmp.host}")
	private String dmpHost;

	@RequestMapping("addBug")
	@ResponseBody
	public ResultMap addBug(AddBugParam param, HttpServletRequest request) throws Exception {
		String userName = SessionUtil.getUser(request).getUserNickname();
		param.setTxtTestor(userName);
		param.setTxtTestDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));

		try {
			addBugService.addBug(param, userName);
		} catch (BusinessException e) {
			return ResultMap.error(e.getMessage());
		}
		return ResultMap.ok();
	}

	@RequestMapping("getBugCode")
	@ResponseBody
	public ResultMap getBugCode(HttpServletRequest request) {
		String userName = SessionUtil.getUser(request).getUserNickname();
		return ResultMap.ok(addBugService.getBugCode(userName));
	}

	@RequestMapping("getRelateProjectTask")
	@ResponseBody
	public List<Map<String, Object>> getRelateProjectTask(SubSystem subSystem) {
		return addBugService.getRelateProjectTask(subSystem);
	}
}
