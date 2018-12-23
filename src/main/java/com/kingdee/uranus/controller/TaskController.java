package com.kingdee.uranus.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.core.ResultMap;
import com.kingdee.uranus.model.ProjectTask;
import com.kingdee.uranus.service.ProjectTaskService;
import com.kingdee.uranus.util.IpAdrressUtil;
import com.kingdee.uranus.util.SessionUtil;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年4月27日 下午1:24:21
 * @version
 */
@Controller
@RequestMapping("/api")
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(BugController.class);

	@Resource
	private ProjectTaskService service;

	@RequestMapping("/getTaskList")
	@ResponseBody
	public PageResult<ProjectTask> getBugList(HttpServletRequest request) throws Exception {

		String userName = SessionUtil.getUser(request).getUserNickname();
		PageResult<ProjectTask> pageResult = new PageResult<>();
		List<ProjectTask> taskList = service.getAllProjectTask(userName);
		pageResult.setCode(0);
		pageResult.setCount(taskList.size());
		pageResult.setData(taskList);

		return pageResult;
	}

	@RequestMapping("/closeTask")
	@ResponseBody
	public Object closeTask(HttpServletRequest request, ProjectTask task) throws ClientProtocolException, IOException {
		String userName = SessionUtil.getUser(request).getUserNickname();

		service.submitTask(task, userName);

		logger.warn("用户IP：{} 关闭了：{} 的task:" + task.getProjectTaskCode(), IpAdrressUtil.getIpAdrress(request), userName);

		return ResultMap.ok();
	}

	@RequestMapping("/closeManyTasks")
	@ResponseBody
	public Object closeManyTasks(HttpServletRequest request, String projectTaskIds, String projectTaskCodes,
			String taskFlagCodes) throws ClientProtocolException, IOException {
		String userName = SessionUtil.getUser(request).getUserNickname();

		if (!StringUtils.isEmpty(projectTaskIds)) {
			String[] projectTaskArray = projectTaskIds.split(",");
			String[] projectTaskCodeArray = projectTaskCodes.split(",");
			String[] taskFlagCodeArray = taskFlagCodes.split(",");
			for (int i = 0; i < projectTaskArray.length; i++) {
				ProjectTask task = new ProjectTask();
				task.setProjectTaskId(projectTaskArray[i]);
				task.setProjectTaskCode(projectTaskCodeArray[i]);
				task.setTaskFlagCode(taskFlagCodeArray[i]);
				service.submitTask(task, userName);
				logger.warn("用户IP：{} 提交了：{} 的任务:" + projectTaskCodeArray[i], IpAdrressUtil.getIpAdrress(request),
						userName);
			}
		}

		return ResultMap.ok();
	}

	/**
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param request
	 * @param taskId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 *
	 */
	@RequestMapping("/viewTask")
	@ResponseBody
	public Object viewTask(HttpServletRequest request, String taskId) throws ClientProtocolException, IOException {
		ProjectTask task = service.viewTask(taskId);
		return ResultMap.ok().put("task", task);
	}
}
