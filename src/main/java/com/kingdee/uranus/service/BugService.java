package com.kingdee.uranus.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kingdee.uranus.constant.ConfigConstant;
import com.kingdee.uranus.core.RequestContext;
import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.domain.Bug;
import com.kingdee.uranus.domain.HandleDetail;
import com.kingdee.uranus.model.User;
import com.kingdee.uranus.search.DocumentSearch;
import com.kingdee.uranus.util.HtmlParse;
import com.kingdee.uranus.util.HttpClientUtil;
import com.kingdee.uranus.util.StringUtil;
import com.kingdee.uranus.util.XMLParse;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年3月22日 上午8:33:08
 * @version
 */
@Service
public class BugService {

	@Autowired
	private DocumentSearch documentSearch;

	private static final Logger logger = LoggerFactory.getLogger(BugService.class);

	public void closeBug(String userName, String bugNo, String fOpinion) throws ClientProtocolException, IOException {
		String userId = this.getUserIdByUserName(userName);
		Bug bug = this.getBugByBugNo(bugNo, userName, userId);
		String handleId = this.getCurrentHandleIdByBugId(bug.getBugId());
		String submitId = this.getBugSubmitterByBugId(bug.getBugId());
		this.fixBug(bug.getBugId(), handleId == null ? userId : handleId, userId, userName, submitId, fOpinion);

		// 将此BUG写入elasticsearch索引库
		Map<String, Object> params = Maps.newHashMap();
		params.put("bugNo", bug.getBugNo());
		documentSearch.updateDocument(ConfigConstant.ELASTIC_BUG_INDEX, bug, params);
	}

	public void forwardBug(String forwardUserName, String currentUserName, String bugNo, String fOpinion) throws Exception {
		String forwardUserId = this.getUserIdByUserName(forwardUserName);

		if (forwardUserId == null) {
			throw new Exception("找不到对应的转发人");
		}
		String currentUserId = this.getUserIdByUserName(currentUserName);

		String bugId = this.getBugByBugNo(bugNo, currentUserName, currentUserId).getBugId();

		String requestUrl;
		String requestParam = "objType=wf_bug&StatusType=1&FFormId={bugId}&FFlowId=45580270699443157042898068791770045&FNodeNo=1&FNodeName=%E6%8F%90%E4%BA%A4%E8%AE%BE%E8%AE%A1&FHandlerId={userId}&FpreNodeNo=1&FpreNodeName=%E6%8F%90%E4%BA%A4%E8%AE%BE%E8%AE%A1&FIsAgreed=1&FOpinion=&FOperate=%E8%BD%AC%E5%8F%91&FObjStatus=%E6%8F%90%E4%BA%A4&FOpinion={fOpinion}";
		requestParam = requestParam.replace("{bugId}", bugId).replace("{userId}", forwardUserId).replace("{fOpinion}", URLEncoder.encode(fOpinion, "UTF-8"));
		requestUrl = "http://192.168.16.18//DMPV2.0/JSPSource/workflow/h_SaveHandle.jsp?state=2";
		requestUrl += "&" + requestParam;

		HttpClientUtil.post(requestUrl, currentUserId, currentUserName, null);

	}

	private void fixBug(String bugId, String handleId, String userId, String userName, String submitId, String fOpinion) throws ClientProtocolException, IOException {
		// 第一步 下推给设计者
		pushToDesigner(bugId, handleId, userId, userName);
		// 第二步 下推开发者
		pushToDeveloper(bugId, handleId, userId, userName);
		// 第三步 下推代码检查
		pushToReviewer(bugId, submitId, userId, userName, fOpinion);

	}

	/**
	 * 
	 * <p>
	 * 根据用户名称查询所有bug
	 * </p>
	 * 
	 * @param userName
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws DocumentException
	 *
	 */
	public List<Bug> queryBugList(String userName, String userId) throws ClientProtocolException, IOException, DocumentException {

		// 查询bugId
		String requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/xmlLedger_Data.jsp?url=buglist.jsp&wt=a~a1=1a~aanda~a(a~aFwf_curHandlerIDa~a=a~af~f{userId}f~f)&status=id";
		userId = userId == null ? this.getUserIdByUserName(userName) : userId;
		requestUrl = requestUrl.replace("{userId}", userId);
		List<String> lines = HttpClientUtil.get(requestUrl, null);
		String bugIds = "";
		for (String line : lines) {
			if (!StringUtils.isEmpty(line.trim())) {
				bugIds = XMLParse.parseXMLGetProjectTaskIds(line);
				break;
			}
		}
		// 根据id查询bug
		requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/xmlLedger_Data.jsp?url=buglist.jsp&status=data&wt={idParam}";
		requestUrl = requestUrl.replace("{idParam}", bugIds);
		lines = HttpClientUtil.get(requestUrl, "GBK");

		String xml = "";
		for (String line : lines) {
			if (!line.isEmpty() && !line.trim().isEmpty()) {
				xml += line;
			}
		}

		List<Bug> bugList = XMLParse.parseXMLGetBugList(xml);

		return bugList;
	}

	/**
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param bugNo
	 * @param userName
	 * @param userId
	 * @return
	 * @throws BusinessException
	 * @throws ClientProtocolException
	 * @throws IOException
	 *
	 */
	public Bug getBugByBugNo(String bugNo, String userName, String userId) {

		Bug bug = null;
		try {
			String baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/Message/Message_in_Data.jsp?wt=Bug_d~dd~d_0_d~dd~d_{userName}_d~dd~d_{userId}_d~dd~d_a~aanda~adbo.T_BugRecord.FBugRecord_Codea~alikea~af~fj~j{bugNo}j~jf~f_d~dd~d_&status=&startrow=1&endrow=100&rndstr=.333201";
			// String baseUrl =
			// "http://192.168.16.18/DMPV2.0/JSPSource/Message/Message_in_Data.jsp?wt=Bug_d~dd~d_a~aanda~adbo.T_BugRecord.FBugRecord_Codea~alikea~af~fj~j{bugNo}j~jf~f_d~dd~d_&status=&startrow=1&endrow=100&rndstr=.333201";
			baseUrl = baseUrl.replace("{bugNo}", bugNo).replace("{userName}", URLEncoder.encode(userName, "UTF-8")).replace("{userId}", userId);

			List<String> lines = HttpClientUtil.get(baseUrl, null);
			for (String line : lines) {
				bug = transformBug(line);
				if (bug != null) {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("通过bugNo:{},userName:{},userId:{}查询对应bug出错:{}", bugNo, userName, userId, e);
			throw new RuntimeException("系统异常，请稍后重试");
		}

		return bug;
	}

	/**
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param bugId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 *
	 */
	public String getCurrentHandleIdByBugId(String bugId) throws ClientProtocolException, IOException {
		String baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_FlowHandle.jsp?objType=wf_bug&formId={bugId}";
		baseUrl = baseUrl.replace("{bugId}", bugId);
		List<String> lines = HttpClientUtil.get(baseUrl, null);
		String handleId = null;

		for (String line : lines) {
			if (!StringUtils.isEmpty(line)) {
				handleId = getHandleId(line);
			}
			if (handleId != null && !handleId.isEmpty()) {
				break;
			}
		}

		// lines.stream().forEach(line -> getHandleId(line, handleId));
		return handleId;
	}

	/**
	 * 
	 * <p>
	 * 
	 * </p>
	 * 
	 * @param bugId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 *
	 */
	public String getBugSubmitterByBugId(String bugId) throws ClientProtocolException, IOException {

		String baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_Flow_Data.jsp?state=1&formId={bugId}";
		baseUrl = baseUrl.replace("{bugId}", bugId);
		List<String> lines = HttpClientUtil.get(baseUrl, "GBK");

		String xml = this.sumLines(lines);
		String submitterName = getSubmitterName(xml);

		return getUserIdByUserName(submitterName);

	}

	private String sumLines(List<String> lines) {
		StringBuffer sb = new StringBuffer();
		for (String line : lines) {
			sb.append(line);
		}
		return sb.toString();
	}

	/**
	 * 
	 * <p>
	 * 根据bugNo,bugId获得bug详情
	 * </p>
	 * 
	 * @param bugNo
	 * @param bugId
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws DocumentException
	 *
	 */
	public Bug getBugInfoByBugNoAndBugId(String bugNo, String bugId) throws ClientProtocolException, IOException, DocumentException {
		String baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/BugManage/bugEdit.jsp?state=2&bugcode={bugNo}&id={bugId}";
		baseUrl = baseUrl.replace("{bugId}", bugId).replace("{bugNo}", bugNo);

		List<String> lines = HttpClientUtil.get(baseUrl, null);
		String html = StringUtil.readLinesToString(lines);
		Bug bug = HtmlParse.parseHtmlGetBug(html);

		baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_Flow_Data.jsp?state=1&formId={bugId}";
		baseUrl = baseUrl.replace("{bugId}", bugId);
		lines = HttpClientUtil.get(baseUrl, "GBK");
		html = StringUtil.readLinesToString(lines).replace("\n", "");
		List<HandleDetail> handleList = XMLParse.parseHtmlGetHandleList(html);
		bug.setHandleList(handleList);

		// 处理外网来的bug，图片看不到。
		String imageUrl = handle(bug.getImageUrl());
		bug.setImageUrl(imageUrl);
		String imageUrl2 = handle(bug.getImageUrl2());
		bug.setImageUrl2(imageUrl2);
		String imageUrl3 = handle(bug.getImageUrl3());
		bug.setImageUrl3(imageUrl3);
		String imageUrl4 = handle(bug.getImageUrl4());
		bug.setImageUrl4(imageUrl4);

		return bug;
	}

	private String handle(String imageUrl) throws ClientProtocolException, IOException {
		if (StringUtils.isEmpty(imageUrl)) {
			return imageUrl;
		}
		HttpGet httpGet = new HttpGet(imageUrl);
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse response = httpClient.execute(httpGet);
		String returnImageUrl = imageUrl;
		if (response.getStatusLine().getStatusCode() == 404) {
			returnImageUrl = imageUrl.replace("192.168.16.18:80", "dmp.kingdee.com");
		}
		return returnImageUrl;
	}

	private static void updateBugStatus(String bugId, String userId, String userName) throws ClientProtocolException, IOException {
		String baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/BugManage/BugInWF_Data.jsp?id={bugId}&tostatus=%E5%B7%B2%E6%94%B9&direction=1";
		baseUrl = baseUrl.replace("{bugId}", bugId);
		HttpClientUtil.post(baseUrl, userId, userName, null);
	}

	private String getSubmitterName(String line) {

		String submitterName = null;

		if (!line.isEmpty()) {
			try {
				submitterName = XMLParse.parseXMLGetSubmitterId(line);
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}

		return submitterName;
	}

	private String getHandleId(String line) {
		String handleId = null;
		if (!line.isEmpty() && line.startsWith("	      <input type=\"text\" id=\"handler\" name=\"handler\" style=\"width:150px;height=25\" readonly value")) {
			try {
				String userName = line.substring(line.indexOf("value=") + 6, line.indexOf(">")).replace("\"", "");
				handleId = getUserIdByUserName(userName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return handleId;
	}

	public String getUserIdByUserName(String userName) throws ClientProtocolException, IOException {

		if (StringUtils.equals(RequestContext.get("userName"), userName)) {
			return RequestContext.get("dmpUserId");
		}

		List<User> users = null;
		try {
			String baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/public/GESelect_Data.jsp?wt=usery~ya~aanda~aFUser_Namea~alikea~a~~~j~j{userName}j~j~~~a~ay~y&status=&startrow=1&endrow=100&rndstr=5.228388E-02";
			baseUrl = baseUrl.replace("{userName}", URLEncoder.encode(userName, "UTF-8"));
			List<String> lines = HttpClientUtil.get(baseUrl, null);

			for (String line : lines) {
				users = getUserId(line);
				if (users.size() > 0) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String userId = null;
		if (users == null) {
			logger.error("not found user by userName :{}", userName);
			// throw new BusinessException("not found user by userName " +
			// userName);
		} else if (users.size() > 1) {
			logger.warn("found repeat naem by userName :{}", userName);
			userId = users.get(0).getDmpUserId();
		} else {
			userId = users.get(0).getDmpUserId();
		}
		logger.info("查询用户：" + userName + " 的 userId:" + userId);
		return userId;
	}

	public List<User> getUsersByUserName(String userName) {
		List<User> users = null;
		try {
			String baseUrl = "http://192.168.16.18/DMPV2.0/JSPSource/public/GESelect_Data.jsp?wt=usery~ya~aanda~aFUser_Namea~alikea~a~~~j~j{userName}j~j~~~a~ay~y&status=&startrow=1&endrow=100&rndstr=5.228388E-02";
			baseUrl = baseUrl.replace("{userName}", URLEncoder.encode(userName, "UTF-8"));
			List<String> lines = HttpClientUtil.get(baseUrl, null);
			for (String line : lines) {
				users = getUserId(line);
				if (users.size() > 0) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	private List<User> getUserId(String line) {

		List<User> users = Lists.newArrayList();
		if (!line.isEmpty() && !line.trim().isEmpty()) {
			try {
				users = XMLParse.parseXMLGetUserId(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return users;
	}

	private Bug transformBug(String line) {
		Bug bug = null;
		if (!line.isEmpty()) {
			try {
				bug = XMLParse.parseXMLGetBug(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bug;
	}

	public static void pushToReviewer(String bugId, String handleId, String userId, String userName, String fOpinion) throws ClientProtocolException, IOException {
		String requestParam = "objType=wf_bug&StatusType=99&FFormId={bugId}&FFlowId=45580270699443157042898068791770045&FNodeNo=4&FNodeName=%E5%B7%B2%E6%94%B9%E9%AA%8C%E8%AF%81&FHandlerId={handleId}&FpreNodeNo=3&FpreNodeName=%E4%BB%A3%E7%A0%81%E6%A3%80%E6%9F%A5&FIsAgreed=1&FOpinion={fOpinion}&FOperate=%E6%8F%90%E4%BA%A4&FObjStatus=%E7%A1%AE%E8%AE%A4%E5%B7%B2%E6%94%B9";
		requestParam = requestParam.replace("{bugId}", bugId).replace("{handleId}", handleId).replace("{fOpinion}", URLEncoder.encode(fOpinion, "UTF-8"));
		String requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_SaveHandle.jsp?state=2";
		requestUrl += "&" + requestParam;

		HttpClientUtil.post(requestUrl, userId, userName, null);

	}

	private static void pushToDeveloper(String bugId, String handleId, String userId, String userName) throws ClientProtocolException, IOException {
		String requestParam = "objType=wf_bug&StatusType=99&FFormId={bugId}&FFlowId=45580270699443157042898068791770045&FNodeNo=3&FNodeName=%E4%BB%A3%E7%A0%81%E6%A3%80%E6%9F%A5&FHandlerId={handleId}&FpreNodeNo=2&FpreNodeName=%E6%8F%90%E4%BA%A4%E5%BC%80%E5%8F%91&FIsAgreed=1&FOpinion=&FOperate=%E6%8F%90%E4%BA%A4&FObjStatus=%E5%B7%B2%E6%94%B9";
		requestParam = requestParam.replace("{bugId}", bugId).replace("{handleId}", handleId);
		String requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_SaveHandle.jsp?state=2";
		requestUrl += "&" + requestParam;

		HttpClientUtil.post(requestUrl, userId, userName, null);
	}

	private static void pushToDesigner(String bugId, String handleId, String userId, String userName) throws ClientProtocolException, IOException {
		String requestUrl;
		String requestParam = "objType=wf_bug&StatusType=99&FFormId={bugId}&FFlowId=45580270699443157042898068791770045&FNodeNo=2&FNodeName=%E6%8F%90%E4%BA%A4%E5%BC%80%E5%8F%91&FHandlerId={handleId}&FpreNodeNo=1&FpreNodeName=%E6%8F%90%E4%BA%A4%E8%AE%BE%E8%AE%A1&FIsAgreed=1&FOpinion=&FOperate=%E6%8F%90%E4%BA%A4&FObjStatus=%E5%B7%B2%E8%AF%BB";
		requestParam = requestParam.replace("{bugId}", bugId).replace("{handleId}", handleId);
		requestUrl = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_SaveHandle.jsp?state=2";
		requestUrl += "&" + requestParam;

		// 修改bug状态
		updateBugStatus(bugId, userId, userName);

		HttpClientUtil.post(requestUrl, userId, userName, null);

	}

	public static void main(String[] args) throws ClientProtocolException, IOException, DocumentException {
		BugService service = new BugService();
		// String userId = service.getUserIdByUserName("王鸿浩");
		service.getBugByBugNo("BT10001", "王鸿浩", "1E7FD416-F853-40A7-BB11-9EEA29A141CC");

		// System.out.println(userId);

		// 同步dmp userId,useraccount

	}

	public void passBug(String userName, String bugNo, String fOpinion) throws ClientProtocolException, IOException {
		String userId = this.getUserIdByUserName(userName);
		String bugId = this.getBugByBugNo(bugNo, userName, userId).getBugId();

		// 修改状态为验证通过
		String url = "http://192.168.16.18/DMPV2.0/JSPSource/workflow/h_SaveHandle.jsp?state=2&objType=wf_bug&StatusType=100&FFormId={bugId}&FFlowId=45580270699443157042898068791770045&FNodeNo=100&FNodeName=%E5%B7%A5%E4%BD%9C%E6%B5%81%E7%BB%93%E6%9D%9F&FHandlerId=&FpreNodeNo=4&FpreNodeName=%E5%B7%B2%E6%94%B9%E9%AA%8C%E8%AF%81&FIsAgreed=1&FOpinion={fOpinion}&FOperate=%E7%BB%93%E6%9D%9F&FObjStatus=%E9%AA%8C%E8%AF%81%E9%80%9A%E8%BF%87";
		url = url.replace("{bugId}", bugId).replace("{fOpinion}", URLEncoder.encode(fOpinion, "UTF-8"));
		HttpClientUtil.post(url, userId, userName, null);
		// 提交工作流
		url = "http://192.168.16.18/DMPV2.0/JSPSource/BugManage/BugInWF_Data.jsp?id={bugId}&tostatus=%E9%AA%8C%E8%AF%81%E9%80%9A%E8%BF%87&direction=1";
		url = url.replace("{bugId}", bugId);
		HttpClientUtil.post(url, userId, userName, null);

	}
}
