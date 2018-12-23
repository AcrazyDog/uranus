package com.kingdee.uranus.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.kingdee.uranus.model.ProjectTask;
import com.kingdee.uranus.model.User;
import com.kingdee.uranus.util.HtmlParse;
import com.kingdee.uranus.util.HttpClientUtil;
import com.kingdee.uranus.util.StringUtil;
import com.kingdee.uranus.util.XMLParse;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年4月26日 下午7:21:23
 * @version
 */
@Service
public class ProjectTaskService {

	private static Map<String, String> commonParams = Maps.newConcurrentMap();
	@Resource
	private BugService bugService;

	static {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		commonParams.put("status", "a16");
		commonParams.put("sst", "%E8%AE%BE%E8%AE%A1%E9%AA%8C%E8%AF%81");
		commonParams.put("teststatus", "%E5%90%A6");
		commonParams.put("tester", "");
		commonParams.put("nexter", "");
		commonParams.put("FProjT_Tester", "");
		commonParams.put("FProjT_TesterName", "");
		commonParams.put("FProjT_Nexter", "");
		commonParams.put("FProjT_NexterName", "");
		commonParams.put("FProjT_DevStatus", "%E6%98%AF");
		commonParams.put("FProjT_IsSubmitToValidate", "%E5%90%A6");
		commonParams.put("activeStage", "%E8%AE%BE%E8%AE%A1");
		commonParams.put("vclr", "");
		commonParams.put("clr", "");
		commonParams.put("czyj", "1");
		commonParams.put("jt", "1");
		commonParams.put("desc", "ok");
		try {
			commonParams.put("tjsj", URLEncoder.encode(sdf.format(new Date()), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public List<ProjectTask> getAllProjectTask(String userName) throws Exception {

		// 先查询处理人的所有的
		String requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/xmlLedger_Data.jsp?url=prjlist.jsp&wt=a~aa~afprojtask_codea~anota~alikea~af~fj~j.j~jf~fa~aa~aanda~aa~a1=1a~aanda~a(a~aFProjT_DesignDealera~a=a~af~f{userId}f~fa~aanda~aFProjT_IsSubmitToValidatea~a=a~af~f%E5%90%A6f~f)&status=id";
		requestUrl = requestUrl.replace("{userId}", bugService.getUserIdByUserName(userName));
		List<String> lines = HttpClientUtil.get(requestUrl, null);

		String idParam = "";
		for (String line : lines) {
			if (!line.isEmpty() && !line.trim().isEmpty()) {
				idParam = XMLParse.parseXMLGetProjectTaskIds(line);
				break;
			}

		}

		requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/xmlLedger_Data.jsp?url=prjlist.jsp&status=data&wt={idParam}";
		requestUrl = requestUrl.replace("{idParam}", URLEncoder.encode(idParam, "GBK"));
		lines = HttpClientUtil.get(requestUrl, "GBK");

		String xml = "";
		for (String line : lines) {
			if (!line.isEmpty() && !line.trim().isEmpty()) {
				xml += line;
			}
		}

		List<ProjectTask> taskList = XMLParse.parseXMLGetProjectTaskList(xml);
		return taskList;
	}

	public void submitTask(ProjectTask task, String userName) throws ClientProtocolException, IOException {
		String userId = bugService.getUserIdByUserName(userName);
		User submitter = this.getSubmitter(task.getProjectTaskId());

		// 第一步：标注
		Map<String, String> param = getParams(task, userName, userId, submitter, 1);
		this.markProjectTask(param, userId, userName);
		// 第二步：提交设计者
		this.submitToDesigner(task.getProjectTaskId(), userName, userId);
		// 第三步：标注
		param = getParams(task, userName, userId, submitter, 4);
		this.markProjectTask(param, userId, userName);
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 第四步：提交需求
		this.submitToNeeder(task.getProjectTaskId(), userName, submitter.getUserId(), userId);
		// 第五步：标注
		// param = getParams(projectTaskId, projectTaskCode, userName, userId,
		// submitter, 2);
		// this.markProjectTask(param, userId, userName);
		// 第六步：提交测试
		// this.submitToTester(projectTaskId, userName, submitter.getUserId());

	}

	private void submitToTester(String projectTaskId, String userName, String userId) {
		String requestParam = "objType=wf_projtasklabel&StatusType=99&FFormId={projectTaskId}&FFlowId=41296968658870084043824561450545198&FNodeNo=4&FNodeName=%E6%B5%8B%E8%AF%95%E7%A1%AE%E8%AE%A4&FHandlerId={userId}&FpreNodeNo=3&FpreNodeName=%E6%8F%90%E4%BA%A4%E6%B5%8B%E8%AF%95&FIsAgreed=1&FOpinion=&FOperate=%E6%8F%90%E4%BA%A4&FObjStatus=%E6%B5%8B%E8%AF%95%E9%AA%8C%E8%AF%81";
		requestParam = requestParam.replace("{projectTaskId}", projectTaskId).replace("{userId}", userId);
		String requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_SaveHandle.jsp?state=2";
		requestUrl += "&" + requestParam;
		try {
			HttpClientUtil.post(requestUrl, userId, userName, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void submitToNeeder(String projectTaskId, String userName, String submitId, String userId) {

		String requestParam = "objType=wf_projtasklabel&StatusType=99&FFormId={projectTaskId}&FFlowId=41296968658870084043824561450545198&FNodeNo=3&FNodeName=%E6%8F%90%E4%BA%A4%E6%B5%8B%E8%AF%95&FHandlerId={userId}&FpreNodeNo=2&FpreNodeName=%E6%8F%90%E4%BA%A4%E9%AA%8C%E8%AF%81&FIsAgreed=1&FOpinion=&FOperate=%E6%8F%90%E4%BA%A4&FObjStatus=%E9%9C%80%E6%B1%82%E9%AA%8C%E8%AF%81";
		requestParam = requestParam.replace("{projectTaskId}", projectTaskId).replace("{userId}", submitId);
		String requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_SaveHandle.jsp?state=2";
		requestUrl += "&" + requestParam;
		try {
			HttpClientUtil.post(requestUrl, userId, userName, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> getParams(ProjectTask task, String userName, String userId, User submitter,
			Integer step) {
		Map<String, String> param = Maps.newHashMap();
		try {
			param.put("prjtid", URLEncoder.encode("'" + task.getProjectTaskId() + "'", "UTF-8"));
			param.put("prjtcode", task.getProjectTaskCode());
			param.put("prjtflagcode", task.getTaskFlagCode());
			param.putAll(commonParams);
			switch (step) {
			case 1:
				param.put("FProjT_DevSubmitter", "");
				param.put("FProjT_DevSubmitterName", "");
				param.put("FProjT_DevDealer", "");
				param.put("FProjT_DevDealerName", "");
				param.put("FProjT_ValidateSubmiter", "");
				param.put("FProjT_ValidateSubmiterName", "");
				param.put("FProjT_ValidateProcesserID", "");
				param.put("FProjT_ValidateProcesserName", "");
				param.put("FProjT_DesignStatus", "");
				param.put("FProjT_DesignSubmitter", submitter.getUserId());
				param.put("FProjT_DesignSubmitterName", URLEncoder.encode(submitter.getUserNickname(), "UTF-8"));
				param.put("FProjT_DesignDealer", userId);
				param.put("FProjT_DesignDealerName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_DesignConfirmStatus", "");
				param.put("tjr", URLEncoder.encode(userName, "UTF-8"));
				break;
			case 2:
				param.put("FProjT_DevSubmitter", userId);
				param.put("FProjT_DevSubmitterName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_DevDealer", userId);
				param.put("FProjT_DevDealerName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_ValidateSubmiter", "");
				param.put("FProjT_ValidateSubmiterName", "");
				param.put("FProjT_ValidateProcesserID", "");
				param.put("FProjT_ValidateProcesserName", "");
				param.put("FProjT_DesignStatus", "");
				param.put("FProjT_DesignSubmitter", submitter.getUserId());
				param.put("FProjT_DesignSubmitterName", URLEncoder.encode(submitter.getUserNickname(), "UTF-8"));
				param.put("FProjT_DesignDealer", userId);
				param.put("FProjT_DesignDealerName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_DesignConfirmStatus", "");
				param.put("tjr", URLEncoder.encode(userName, "UTF-8"));
				break;
			case 3:
				param.put("FProjT_DevSubmitter", userId);
				param.put("FProjT_DevSubmitterName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_DevDealer", userId);
				param.put("FProjT_DevDealerName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_ValidateSubmiter", userId);
				param.put("FProjT_ValidateSubmiterName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_ValidateProcesserID", userId);
				param.put("FProjT_ValidateProcesserName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_DesignStatus", "");
				param.put("FProjT_DesignSubmitter", submitter.getUserId());
				param.put("FProjT_DesignSubmitterName", URLEncoder.encode(submitter.getUserNickname(), "UTF-8"));
				param.put("FProjT_DesignDealer", userId);
				param.put("FProjT_DesignDealerName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_DesignConfirmStatus", "");
				param.put("tjr", URLEncoder.encode(userName, "UTF-8"));
				break;
			case 4:
				param.put("tester", submitter.getUserId());
				param.put("nexter", submitter.getUserId());
				param.put("FProjT_Tester", submitter.getUserId());
				param.put("FProjT_TesterName", URLEncoder.encode(submitter.getUserNickname(), "UTF-8"));
				param.put("FProjT_Nexter", submitter.getUserId());
				param.put("FProjT_NexterName", URLEncoder.encode(submitter.getUserNickname(), "UTF-8"));
				param.put("FProjT_DevStatus", "%E6%98%AF");
				param.put("FProjT_DevSubmitter", userId);
				param.put("FProjT_DevSubmitterName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_DevDealer", userId);
				param.put("FProjT_DevDealerName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_ValidateSubmiter", userId);
				param.put("FProjT_ValidateSubmiterName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_ValidateProcesserID", submitter.getUserId());
				param.put("FProjT_ValidateProcesserName", URLEncoder.encode(submitter.getUserNickname(), "UTF-8"));
				param.put("FProjT_DesignStatus", "%E6%98%AF");
				param.put("FProjT_DesignSubmitter", submitter.getUserId());
				param.put("FProjT_DesignSubmitterName", URLEncoder.encode(submitter.getUserNickname(), "UTF-8"));
				param.put("FProjT_DesignDealer", userId);
				param.put("FProjT_DesignDealerName", URLEncoder.encode(userName, "UTF-8"));
				param.put("FProjT_DesignConfirmStatus", "%E9%80%9A%E8%BF%87");
				param.put("vclr", URLEncoder.encode(submitter.getUserNickname(), "UTF-8"));
				param.put("clr", submitter.getUserId());
				param.put("ft", "0");
				param.put("yw", "0");
				param.put("status", "a15");
				param.put("sst", "%E5%BC%80%E5%8F%91%E9%AA%8C%E8%AF%81");
				param.put("tjr", URLEncoder.encode(userName, "UTF-8"));
				break;
			default:
				break;
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return param;
	}

	private void submitToDesigner(String projectTaskId, String userName, String userId) {

		String requestUrl;
		String requestParam = "objType=wf_projtasklabel&StatusType=99&FFormId={projectTaskId}&FFlowId=41296968658870084043824561450545198&FNodeNo=2&FNodeName=%E6%8F%90%E4%BA%A4%E9%AA%8C%E8%AF%81&FHandlerId={userId}&FpreNodeNo=1&FpreNodeName=%E6%8F%90%E4%BA%A4%E5%BC%80%E5%8F%91&FIsAgreed=1&FOpinion=&FOperate=%E6%8F%90%E4%BA%A4&FObjStatus=%E5%BC%80%E5%8F%91%E9%AA%8C%E8%AF%81";
		requestParam = requestParam.replace("{projectTaskId}", projectTaskId).replace("{userId}", userId);
		requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_SaveHandle.jsp?state=2";
		requestUrl += "&" + requestParam;
		try {
			HttpClientUtil.post(requestUrl, userId, userName, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void markProjectTask(Map<String, String> param, String userId, String userName) {

		String baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/prjmanager/SaveStatus.jsp";
		// 修改标注的flagcode
		String paramStr = this.changeParamToString(param);
		baseUrl += "?" + paramStr.substring(0, paramStr.length() - 1);
		try {
			HttpClientUtil.post(baseUrl, userId, userName, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String changeParamToString(Map<String, String> param) {
		String paramStr = "";
		for (Entry<String, String> entry : param.entrySet()) {
			paramStr += entry.getKey() + "=" + entry.getValue() + "&";
		}
		return paramStr;
	}

	private User getSubmitter(String projectTaskId) {

		User user = new User();
		String baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_Flow_Data.jsp?state=1&formId={projectTaskId}";
		baseUrl = baseUrl.replace("{projectTaskId}", projectTaskId);
		try {
			List<String> lines = HttpClientUtil.get(baseUrl, "GBK");

			for (String line : lines) {
				if (!StringUtils.isEmpty(line) && !StringUtils.isEmpty(line.trim())) {
					user = XMLParse.parseXMLGetFirstName(line);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return user;
	}

	public static void main(String[] args) throws Exception {
		ProjectTaskService service = new ProjectTaskService();

		// service.submitTask("ffe10d0b-d4ec-4eb6-bbdc-ef88815ff1ec",
		// "PRJ00454844", "聂康");
		// service.getAllProjectTask("聂康");

		// System.out.println(URLDecoder.decode("ae241aae-b73a-41d0-aaa0-4d806ae41a80"));

		// HttpClientUtil.get("http://192.168.16.18/DMPV2.0/JSPSource/xmlLedger_Data.jsp?url=prjlist.jsp&FProjT_DesignDealer=f~f20BFECC6-5157-4570-911C-D244B0E04251f~f",
		// null);

		System.out.println(URLDecoder.decode("%E5%90%A6", "UTF-8"));

		service.viewTask("f304b1d5-812a-4822-b624-78a033ae33b3");
	}

	public ProjectTask viewTask(String taskId) throws ClientProtocolException, IOException {
		String url = "http://192.168.16.18/DMPV2.0/JSPSource/prjmanager/PrjNew.jsp?state=3&pid={taskId}";
		url = url.replace("{taskId}", taskId);
		List<String> lines = HttpClientUtil.get(url, "UTF-8");
		String html = StringUtil.readLinesToString(lines);
		return HtmlParse.parseHtmlGetTask(html);
	}
}
