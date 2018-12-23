/**
 * Copyright (c) 2006-2015 Kingdee Ltd. All Rights Reserved. 
 *  
 * This code is the confidential and proprietary information of   
 * Kingdee. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Kingdee,http://www.Kingdee.com.
 *  
 */
package com.kingdee.uranus.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.ClientProtocolException;
import org.dom4j.DocumentException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kingdee.uranus.constant.ConfigConstant;
import com.kingdee.uranus.controller.param.BugParam;
import com.kingdee.uranus.core.PageBean;
import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.core.ResultMap;
import com.kingdee.uranus.domain.Bug;
import com.kingdee.uranus.model.Log;
import com.kingdee.uranus.model.LogType;
import com.kingdee.uranus.search.DocumentSearch;
import com.kingdee.uranus.search.param.SearchParam;
import com.kingdee.uranus.service.BugService;
import com.kingdee.uranus.service.LogService;
import com.kingdee.uranus.util.IpAdrressUtil;
import com.kingdee.uranus.util.SessionUtil;

/**
 * <p>
 * 
 * </p>
 * 
 * @author Administrator
 * @date 2018年3月22日 下午8:46:45
 * @version
 */
@Controller
@RequestMapping("/api")
public class BugController {

	private static final Logger logger = LoggerFactory.getLogger(BugController.class);

	@Resource
	private BugService service;

	@Resource
	private LogService logService;

	@Resource
	private DocumentSearch documentSearch;

	@RequestMapping("/getBugList")
	@ResponseBody
	public PageResult<Bug> getBugList(HttpServletRequest request)
			throws ClientProtocolException, IOException, DocumentException {

		String userName = SessionUtil.getUser(request).getUserNickname();
		String userId = SessionUtil.getUser(request).getDmpUserId();

		logger.info("查询的用户：" + userName + "的BUG");

		PageResult<Bug> pageResult = new PageResult<Bug>();
		List<Bug> bugList = service.queryBugList(userName, userId);
		pageResult.setCode(0);
		pageResult.setCount(bugList.size());
		pageResult.setData(bugList);

		return pageResult;
	}

	@RequestMapping("/closeBug")
	@ResponseBody
	public Object closeBug(HttpServletRequest request, String bugNo, String fOpinion)
			throws ClientProtocolException, IOException {
		String userName = SessionUtil.getUser(request).getUserNickname();
		service.closeBug(userName, bugNo, fOpinion);

		Log log = new Log();
		log.setBugCloser(userName);
		log.setBugNo(bugNo);
		log.setIp(IpAdrressUtil.getIpAdrress(request));
		log.setReason(fOpinion);
		log.setLogType(LogType.CLOSE);
		logService.saveLog(log);

		return ResultMap.ok();
	}

	@RequestMapping("/forwardBug")
	@ResponseBody
	public Object fowardBug(HttpServletRequest request, String bugNo, String forwardUserName, String fOpinion)
			throws ClientProtocolException, IOException {
		String userName = SessionUtil.getUser(request).getUserNickname();
		try {
			service.forwardBug(forwardUserName, userName, bugNo, fOpinion);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResultMap.error(e.getMessage());
		}
		Log log = new Log();
		log.setBugCloser(userName);
		log.setBugNo(bugNo);
		log.setIp(IpAdrressUtil.getIpAdrress(request));
		log.setReason(fOpinion);
		log.setLogType(LogType.FORWARD);
		logService.saveLog(log);

		return ResultMap.ok();
	}

	@RequestMapping("/closeManyBugs")
	@ResponseBody
	public Object closeManyBugs(HttpServletRequest request, String bugNos) throws ClientProtocolException, IOException {
		String userName = SessionUtil.getUser(request).getUserNickname();

		if (!StringUtils.isEmpty(bugNos)) {

			String[] bugNoArray = bugNos.split(",");

			for (int i = 0; i < bugNoArray.length; i++) {
				service.closeBug(userName, bugNoArray[i], "");
				logger.warn("用户IP：{} 关闭了：{} 的bug:" + bugNoArray[i], IpAdrressUtil.getIpAdrress(request), userName);

				Log log = new Log();
				log.setBugCloser(userName);
				log.setBugNo(bugNoArray[i]);
				log.setIp(IpAdrressUtil.getIpAdrress(request));
				log.setLogType(LogType.CLOSE);
				logService.saveLog(log);

			}
		}

		return ResultMap.ok();
	}

	@RequestMapping("/forwardManyBugs")
	@ResponseBody
	public Object forwardManyBugs(HttpServletRequest request, String bugNos, String forwardUserName)
			throws ClientProtocolException, IOException {
		String userName = SessionUtil.getUser(request).getUserNickname();

		if (!StringUtils.isEmpty(bugNos)) {

			String[] bugNoArray = bugNos.split(",");

			for (int i = 0; i < bugNoArray.length; i++) {
				try {
					service.forwardBug(forwardUserName, userName, bugNoArray[i], "");
				} catch (Exception e) {
					return ResultMap.error(e.getMessage());
				}
				logger.warn("用户IP：{} 转发了：{} 的bug:" + bugNoArray[i], IpAdrressUtil.getIpAdrress(request), userName);

				Log log = new Log();
				log.setBugCloser(userName);
				log.setBugNo(bugNoArray[i]);
				log.setIp(IpAdrressUtil.getIpAdrress(request));
				log.setReason("转让BUG");
				log.setLogType(LogType.FORWARD);
				logService.saveLog(log);

			}
		}

		return ResultMap.ok();
	}

	@RequestMapping("/getBugDetail")
	@ResponseBody
	public Object getBugDetail(HttpServletRequest request, String bugNo, String bugId)
			throws ClientProtocolException, IOException, DocumentException {

		Bug bug = service.getBugInfoByBugNoAndBugId(bugNo, bugId);

		return ResultMap.ok().put("bug", bug);
	}

	@RequestMapping("/queryBugListByParam")
	@ResponseBody
	public PageResult<Bug> queryBugListByParam(HttpServletRequest request, BugParam param)
			throws ClientProtocolException, IOException, DocumentException {

		String userName = param.getName();
		logger.info("查询的用户：" + userName + "的BUG");

		PageResult<Bug> pageResult = new PageResult<Bug>();
		List<Bug> bugList = service.queryBugList(userName, null);
		pageResult.setCode(0);
		pageResult.setCount(bugList.size());
		pageResult.setData(bugList);

		return pageResult;
	}

	@RequestMapping("/searchBug")
	@ResponseBody
	public PageBean<Bug> searchBug(SearchParam param) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// 设置过了字段
		param.setHighLightFields(Lists.newArrayList("bugId", "bugNo", "detail", "bugDesc"));
		param.setIndex(ConfigConstant.ELASTIC_BUG_INDEX);
		param.setQueryFileds(Lists.newArrayList("bugId", "bugNo", "detail", "bugDesc"));

		List<Bug> result = Lists.newArrayList();
		PageBean<Bug> page = new PageBean<Bug>(result);

		SearchResponse response = documentSearch.searchDocument(param);

		SearchHits searchHits = response.getHits();
		long totalHits = searchHits.getTotalHits(); // 查询结果记录条数
		page.setCount(totalHits);

		SearchHit[] hits = searchHits.getHits(); // 查询结果
		for (SearchHit hit : hits) {

			String source = hit.getSourceAsString();
			Bug bug = JSONObject.parseObject(source, Bug.class);
			Map<String, HighlightField> highlightFields = hit.getHighlightFields();
			for (Entry<String, HighlightField> entry : highlightFields.entrySet()) {

				Method method = bug.getClass().getDeclaredMethod(
						"set" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1),
						String.class);
				method.invoke(bug, entry.getValue().fragments()[0].toString());

			}

			if (bug.getBugDesc() != null && bug.getBugDesc().length() > 200) {
				bug.setBugDesc(bug.getBugDesc().substring(0, 200) + "...");
			}
			result.add(bug);
		}

		page.setData(result);

		return page;
	}

	@RequestMapping("/passBug")
	@ResponseBody
	public Object passBug(HttpServletRequest request, String bugNo, String fOpinion)
			throws ClientProtocolException, IOException {
		String userName = SessionUtil.getUser(request).getUserNickname();
		try {
			service.passBug(userName, bugNo, fOpinion);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResultMap.error(e.getMessage());
		}
		Log log = new Log();
		log.setBugCloser(userName);
		log.setBugNo(bugNo);
		log.setIp(IpAdrressUtil.getIpAdrress(request));
		log.setReason("fOpinion");
		log.setLogType(LogType.PASS);
		logService.saveLog(log);

		return ResultMap.ok();
	}
}
