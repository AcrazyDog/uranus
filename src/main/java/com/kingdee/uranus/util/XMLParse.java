package com.kingdee.uranus.util;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingdee.uranus.domain.Bug;
import com.kingdee.uranus.domain.HandleDetail;
import com.kingdee.uranus.domain.ProductTaskDomain;
import com.kingdee.uranus.domain.SubSystem;
import com.kingdee.uranus.model.ProjectTask;
import com.kingdee.uranus.model.User;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年2月8日 下午8:26:15
 * @version
 */
public class XMLParse {

	public static List<Bug> parseXMLGetBugList(String xml) throws DocumentException {

		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(0);
		String content = element.getText();

		List<Bug> bugList = Lists.newArrayList();
		if (StringUtils.isEmpty(content)) {
			return bugList;
		}

		String[] taskStrs = content.split("~");

		List<String[]> list = Lists.newArrayList();

		for (String taskStr : taskStrs) {

			String[] taskInfos = taskStr.split("\\|");
			list.add(taskInfos);

			if (taskInfos == null || taskInfos.length < 1) {
				return Lists.newArrayList();
			}

			Bug bug = new Bug();
			bug.setNextHandleUserName(taskInfos[21]);
			bug.setBugDesc(taskInfos[22]);
			bug.setBugId(taskInfos[45]);
			bug.setBugNo(taskInfos[0]);
			bug.setGroup(taskInfos[5]);
			bug.setPriority(taskInfos[50]);
			bug.setTester(taskInfos[28]);
			bug.setTestTime(taskInfos[29]);
			bug.setTxtSubSys(taskInfos[4]);
			bug.setDiscoveryTime(taskInfos[6]);

			bugList.add(bug);

		}

		return bugList;

	}

	public static Bug parseXMLGetBug(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(2);
		String content = element.getText();

		String[] bugStrs = content.split("\\|");

		Bug bug = new Bug();
		bug.setBugId(bugStrs[7]);
		bug.setBugDesc(bugStrs[4]);
		bug.setBugNo(bugStrs[5]);
		bug.setBugType(bugStrs[0]);
		return bug;
	}

	public static List<User> parseXMLGetUserId(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(2);
		String content = element.getText();

		String[] userArray = content.split("~!~");
		List<User> users = Lists.newArrayList();
		for (String userStr : userArray) {
			String[] split = userStr.split("\\|");
			if (split != null && split.length > 1) {
				User user = new User();
				user.setDmpUserAccount(split[0]);
				user.setDmpUserId(split[2]);

				users.add(user);
			}
		}

		return users;
	}

	public static String parseXMLGetSubmitterId(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();

		Element e = (Element) root.content().get(0);
		String[] lines = e.getText().split(",");
		String[] line = lines[0].split("\\|");

		return line[4];

	}

	public static User parseXMLGetFirstName(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();

		Element e = (Element) root.content().get(0);

		String[] lines = e.getText().split("~!~")[0].split("\\|");

		String userName = lines[4];
		String userId = lines[8];

		User user = new User();
		user.setUserNickname(userName);
		user.setUserId(userId);
		return user;

	}

	public static List<ProjectTask> parseXMLGetProjectTaskList(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);

		Element root = document.getRootElement();

		Element element = (Element) root.elements().get(0);

		String content = element.getText();

		List<ProjectTask> taskList = Lists.newArrayList();
		if (StringUtils.isEmpty(content)) {
			return taskList;
		}

		String[] taskStrs = content.split("\\|\\|~");

		List<String[]> list = Lists.newArrayList();

		for (String taskStr : taskStrs) {

			System.out.println(taskStr);

			String[] taskInfos = taskStr.split("\\|");

			list.add(taskInfos);

			if (taskInfos == null || taskInfos.length < 1) {
				return Lists.newArrayList();
			}

			// if ("提交".equals(taskInfos[79])) {
			ProjectTask task = new ProjectTask();
			task.setProjectTaskCode(taskInfos[0]);
			task.setTaskName(taskInfos[2]);
			task.setTaskType(taskInfos[4]);
			task.setGroup(taskInfos[5]);
			task.setAdviceName(taskInfos[10]);
			task.setAdviceTime(taskInfos[11]);
			task.setTaskFlagCode(taskInfos[1]);
			// task.setProjectTask(taskInfos[1]);
			// task.setPreTime(taskInfos[5]);
			task.setWorkflowStatus(taskInfos[79]);
			task.setProjectTaskId(taskInfos[17]);

			taskList.add(task);
			// }

		}

		handle(list);

		return taskList;
	}

	private static void handle(List<String[]> list) {

		Map<Integer, Map<String, Integer>> map = Maps.newConcurrentMap();
		for (int i = 0; i < list.size(); i++) {
			String[] strArray = list.get(i);
			for (int j = 0; j < strArray.length; j++) {
				if (map.containsKey(j)) {
					if (map.get(j).containsKey(strArray[j])) {
						Map<String, Integer> lMap = map.get(j);
						lMap.put(strArray[j], lMap.get(strArray[j]) + 1);
						map.put(j, lMap);
					} else {
						Map<String, Integer> lMap = Maps.newHashMap();
						lMap.put(strArray[j], 1);
						map.put(j, lMap);
					}

				} else {
					Map<String, Integer> lMap = Maps.newHashMap();
					lMap.put(strArray[j], 1);
					map.put(j, lMap);
				}
			}
		}

		System.out.println(JSONObject.toJSONString(map));

	}

	public static String parseXMLGetProjectTaskIds(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(0);
		String content = element.getText();

		String idParam = "";

		if (StringUtils.isEmpty(content)) {
			return idParam;
		}
		String[] taskStrs = content.split("~");
		for (String taskStr : taskStrs) {

			idParam += "'" + taskStr + "'" + ",";

		}

		return idParam.substring(0, idParam.length() - 1);
	}

	public static List<HandleDetail> parseHtmlGetHandleList(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(0);
		String content = element.getText();

		String[] lines = content.split("~!~");

		List<HandleDetail> list = Lists.newArrayList();
		for (String line : lines) {

			String[] strs = line.split("\\|");

			HandleDetail detail = new HandleDetail();
			detail.setSeq(strs[0]);
			detail.setNodeName(strs[1]);
			detail.setOperateName(strs[2]);
			detail.setStatus(strs[3]);
			detail.setHandlerName(strs[4]);
			detail.setReceiverName(strs[5]);
			detail.setHandleTime(strs[6]);
			detail.setHandleRemark(strs[7]);

			list.add(detail);
		}

		return list;
	}

	public static List<ProductTaskDomain> pareXmlGetAllProjectTask(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(2);
		String content = element.getText();

		List<ProductTaskDomain> taskList = Lists.newArrayList();

		if (!StringUtils.isEmpty(content)) {
			String[] split = content.split("~!~");
			for (String taskStr : split) {
				String[] taskArray = taskStr.split("\\|");

				ProductTaskDomain task = new ProductTaskDomain();
				task.setProduct(taskArray[0]);
				task.setVersion(taskArray[1]);
				task.setCode(taskArray[2]);
				task.setName(taskArray[3]);
				task.setRemark(taskArray[4]);
				task.setId(taskArray[5]);
				task.setVersionId(taskArray[6]);

				taskList.add(task);
			}
		}

		return taskList;
	}

	public static List<SubSystem> pareXmlGetAllSubSystem(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(2);
		String content = element.getText();

		List<SubSystem> subSystemList = Lists.newArrayList();

		if (!StringUtils.isEmpty(content)) {
			String[] split = content.split("~!~");
			for (String taskStr : split) {
				String[] taskArray = taskStr.split("\\|");

				SubSystem subSystem = new SubSystem();
				subSystem.setName(taskArray[0]);
				subSystem.setGroup(taskArray[1]);
				subSystem.setId(taskArray[2]);
				subSystem.setGroupId(taskArray[3]);
				subSystem.setProjectTaskId(taskArray[4]);

				subSystemList.add(subSystem);
			}
		}

		return subSystemList;

	}

	public static List<Map<String, Object>> pareXmlGetStage(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(0);
		String content = element.getText();

		List<Map<String, Object>> list = Lists.newArrayList();

		if (!StringUtils.isEmpty(content)) {
			String[] split = content.split("~");
			for (String str : split) {
				String[] array = str.split("\\|");
				Map<String, Object> map = Maps.newHashMap();

				map.put("id", array[0]);
				map.put("name", array[1]);

				list.add(map);
			}
		}
		return list;
	}

	public static List<Map<String, Object>> pareXmlGetRelateProjectTask(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(2);
		String content = element.getText();

		List<Map<String, Object>> list = Lists.newArrayList();

		if (!StringUtils.isEmpty(content)) {
			String[] split = content.split("~!~");
			for (String str : split) {
				String[] array = str.split("\\|");
				Map<String, Object> map = Maps.newHashMap();

				map.put("id", array[5]);
				map.put("name", array[1] + "(" + array[0] + ")");

				list.add(map);
			}
		}
		return list;

	}

	public static List<User> pareXmlGetUserId(String xml) throws DocumentException {
		Document document = DocumentHelper.parseText(xml);
		Element root = document.getRootElement();
		Element element = (Element) root.elements().get(2);
		String content = element.getText();

		List<User> list = Lists.newArrayList();

		if (!StringUtils.isEmpty(content)) {
			String[] split = content.split("~!~");
			for (String str : split) {
				String[] array = str.split("\\|");
				Map<String, Object> map = Maps.newHashMap();

				map.put("id", array[5]);
				map.put("name", array[1] + "(" + array[0] + ")");

				// list.add(map);
			}
		}
		return list;
	}
}
