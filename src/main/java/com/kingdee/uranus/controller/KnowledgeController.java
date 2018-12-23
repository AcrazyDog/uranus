package com.kingdee.uranus.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kingdee.uranus.constant.ConfigConstant;
import com.kingdee.uranus.controller.param.KnowledgeSearchParam;
import com.kingdee.uranus.core.PageBean;
import com.kingdee.uranus.core.ResultMap;
import com.kingdee.uranus.model.Knowledge;
import com.kingdee.uranus.model.User;
import com.kingdee.uranus.search.DocumentSearch;
import com.kingdee.uranus.search.param.SearchParam;
import com.kingdee.uranus.service.KnowledgeService;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月19日 下午5:25:26
 * @version
 */
@RestController
@RequestMapping("api/knowledge")
public class KnowledgeController extends BaseController {

	@Resource
	private KnowledgeService knowledgeService;

	@Resource
	private DocumentSearch documentSearch;

	/**
	 * 添加知识
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@PostMapping()
	public ResultMap add(Knowledge knowledge, HttpServletRequest request) {

		User user = getUser(request);
		knowledge.setCreateId(user.getUserId());
		knowledge.setCreateName(user.getUserNickname());
		knowledge.setCreateTime(new Date());
		knowledge.setUpdateId(user.getUserId());
		knowledge.setUpdateName(user.getUserNickname());
		knowledge.setUpdateTime(new Date());

		Long id = knowledgeService.save(knowledge);
		return ResultMap.ok("添加成功").put("id", id);
	}

	/**
	 * 修改知识
	 */
	@PutMapping()
	public ResultMap update(HttpServletRequest request, Knowledge knowledge) {

		User user = getUser(request);
		knowledge.setUpdateId(user.getUserId());
		knowledge.setUpdateName(user.getUserNickname());
		knowledge.setUpdateTime(new Date());
		knowledgeService.update(knowledge);
		return ResultMap.ok("修改成功");
	}

	/**
	 * 查询所有知识
	 * 
	 * @return
	 */
	@RequestMapping("list")
	public PageBean<Knowledge> list(HttpServletRequest request, KnowledgeSearchParam param) {
		return knowledgeService.getKnowledgeList(param);
	}

	@RequestMapping("/getKnowledgeInfo")
	@ResponseBody
	public Object getBugDetail(Long id) {
		Knowledge knowledge = knowledgeService.getBugDetail(id);
		return ResultMap.ok().put("knowledge", knowledge);
	}

	@RequestMapping("/deleteKnowledge")
	@ResponseBody
	public Object deleteKnowledge(HttpServletRequest request, Knowledge knowledge) {

		User user = getUser(request);

		if (!user.getUserNickname().equals(knowledge.getCreateName()) && !"聂康".equals(user.getUserNickname())) {
			return ResultMap.error("只能删除自己创建的知识！");
		}

		knowledgeService.deleteKnowledge(knowledge.getId());
		return ResultMap.ok();
	}

	@RequestMapping("/searchKnowledge")
	@ResponseBody
	public PageBean<Knowledge> searchKnowledge(SearchParam param) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// 设置过了字段
		param.setHighLightFields(Lists.newArrayList("title", "context"));
		param.setIndex(ConfigConstant.ELASTIC_KNOWLEDGE_INDEX);
		param.setQueryFileds(Lists.newArrayList("title", "context"));

		List<Knowledge> result = Lists.newArrayList();
		PageBean<Knowledge> page = new PageBean<Knowledge>(result);

		SearchResponse response = documentSearch.searchDocument(param);

		SearchHits searchHits = response.getHits();
		long totalHits = searchHits.getTotalHits(); // 查询结果记录条数
		page.setCount(totalHits);

		SearchHit[] hits = searchHits.getHits(); // 查询结果
		for (SearchHit hit : hits) {

			String source = hit.getSourceAsString();
			Knowledge knowledage = JSONObject.parseObject(source, Knowledge.class);
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			for (Entry<String, HighlightField> entry : highlightFields.entrySet()) {

				Method method = knowledage.getClass().getDeclaredMethod(
						"set" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1),
						String.class);
				method.invoke(knowledage, entry.getValue().fragments()[0].toString());

			}

			if (knowledage.getContext() != null && knowledage.getContext().length() > 200) {
				knowledage.setContext(knowledage.getContext().substring(0, 200) + "...");
			}

			result.add(knowledage);

		}

		page.setData(result);

		return page;
	}
}
