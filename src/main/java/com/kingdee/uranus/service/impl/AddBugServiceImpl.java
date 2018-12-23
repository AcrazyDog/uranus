package com.kingdee.uranus.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.domain.AddBugParam;
import com.kingdee.uranus.domain.ProductTaskDomain;
import com.kingdee.uranus.domain.SubSystem;
import com.kingdee.uranus.search.DocumentSearch;
import com.kingdee.uranus.service.AddBugService;
import com.kingdee.uranus.service.BugService;
import com.kingdee.uranus.util.HtmlParse;
import com.kingdee.uranus.util.HttpClientUtil;
import com.kingdee.uranus.util.StringUtil;
import com.kingdee.uranus.util.XMLParse;

@Service
public class AddBugServiceImpl implements AddBugService {

	private static final Logger logger = LoggerFactory.getLogger(AddBugService.class);

	@Value("${dmp.host}")
	private String dmpHost;

	@Autowired
	private BugService bugService;

	@Autowired
	private DocumentSearch documentSearch;

	@Override
	public List<Map<String, Object>> getStageByGroupId(String groupId) {

		String url = dmpHost + "/DMPV2.0/JSPSource/prjmanager/ItStage_Data.jsp?action=getItStageByWt&wt=a~aANDa~aFPT_ID=f~f{groupId}f~f";
		url = url.replace("{groupId}", groupId);
		List<Map<String, Object>> list = Lists.newArrayList();

		List<String> lines;
		try {
			lines = HttpClientUtil.get(url, "GBK");
			String xml = "";
			for (String line : lines) {
				if (!line.isEmpty() && !line.trim().isEmpty()) {
					xml += line;
				}
			}

			list = XMLParse.pareXmlGetStage(xml);

		} catch (IOException e) {
			logger.error("get State error , id:{},reson:{}", groupId, e);
		} catch (DocumentException e) {
			logger.error("get State error , id:{},reson:{}", groupId, e);
		}

		return list;
	}

	@Override
	public List<Map<String, Object>> getRelateProjectTask(SubSystem subSystem) {

		String url = dmpHost + "/DMPV2.0/JSPSource/public/GESelect_Data.jsp?wt=projtasksely~ya~aanda~aFPT_ID=f~f{groupId}f~fa~aanda~a(FProjT_TestStatus=f~f%E6%98%AFf~fa~aora~aa~aFProjT_IsSubmitToValidate=f~f%E6%98%AFf~f)a~aanda~aFProdT_ID=f~f{projectTaskId}f~fa~aanda~aFSubSystem_ID=f~f{subSystemId}f~fa~aanda~aFPT_ItStageIDa~a=a~af~f{stageId}f~fy~yFProjT_ID,FProjT_Name,FProjTask_Code,FPreTaskName&status=&startrow=1&endrow=100&rndstr=.6463674";
		url = url.replace("{groupId}", subSystem.getGroupId()).replace("{projectTaskId}", subSystem.getProjectTaskId()).replace("{subSystemId}", subSystem.getId()).replace("{stageId}", subSystem.getStageId());
		List<Map<String, Object>> list = Lists.newArrayList();

		List<String> lines;
		try {
			lines = HttpClientUtil.get(url, "UTF-8");
			String xml = "";
			for (String line : lines) {
				if (!line.isEmpty() && !line.trim().isEmpty()) {
					xml += line;
				}
			}

			list = XMLParse.pareXmlGetRelateProjectTask(xml);

		} catch (IOException e) {
			logger.error("get State error ,reson:{}", e);
		} catch (DocumentException e) {
			logger.error("get State error ,reson:{}", e);
		}

		return list;
	}

	@Override
	public void addBug(AddBugParam param, String userName) throws BusinessException {

		String url = dmpHost + "/DMPV2.0/JSPSource/SaveCatch.jsp?filename=bugEdit.jsp?state=1&data=prodtName${productName}~ddlProdTask${projectTaskId}~txtSubSys${subSystemName}~txtSubSysID${subSystemId}~txtProjTeam${group}~txtProjTeamID${groupId}~ddlDefectProp${ddlDefectProp}~ddlDefectType${ddlDefectType}~ddlTestAngle$~ddlDetectStage${ddlDetectStage}~ddlPonderance$737DA4C8-37B5-4D12-B18D-640829AC76BA~ddlInstancy$E7181B40-BF14-44D3-877F-7FEA96B5BB23~ddlTestMethod$AA899435-8246-4BA8-960B-DE5C84C1CDB2~ddlOS$4311B5FD-E212-40CE-8DC0-26B56A9BF4EC~ddlDB$CA271099-E611-492E-91E4-EF3670E8224F~ddlTestEnv1$~ddlTestEnv2$~txtQsnDataEnv${txtQsnDataEnv}~ItStage${ItStage}~txtProjTask${relateProjectTaskName}~txtProjTaskID${relateProjectTaskId}~txtPreTask$~txtQsnKeyWord${txtQsnKeyWord}~txtQsnOpeStep${txtQsnOpeStep}~ddlBugPri${ddlBugPri}~FLanguageVersion$%e7%ae%80%e4%bd%93&userid={userId}";

		try {
			url = replacePlaceholder(url, param);
		} catch (Exception e) {
			logger.error("replacePlaceholder error :{}", e);
		}

		String currentUserId = null;
		try {
			currentUserId = bugService.getUserIdByUserName(userName);
		} catch (IOException e) {
			logger.error("getUserIdByUserName:{}, error :{}", userName, e);
			throw new BusinessException("getUserIdByUserName error");
		}
		url = url.replace("{userId}", currentUserId);

		// 增加一些参数
		try {
			url += "&FIsRepeated=" + URLEncoder.encode(param.getFIsRepeated(), "utf-8");
			url += "&FIsRecurred=" + URLEncoder.encode(param.getFIsRecurred(), "utf-8");
			url += "&txtTestor=" + URLEncoder.encode(param.getTxtTestor(), "utf-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("URLEncoder.encode error:{}", e1);
			e1.printStackTrace();
		}
		url += "&txtTestDate=" + param.getTxtTestDate();
		url += "&txtBugCode=" + param.getTxtBugCode();

		try {
			HttpClientUtil.post(url, currentUserId, userName, null);
		} catch (IOException e) {
			logger.error("add bug  error :{}", e);
			throw new BusinessException("add bug  error");
		}

		String bugId = null;
		try {
			// 为什么要跑这个
			url = dmpHost + "/DMPV2.0/JSPSource/BugManage/bugHideFrame.jsp";
			List<String> lines = HttpClientUtil.postMultipartParam(url, currentUserId, userName, "Content-Type: multipart/form-data; boundary=---------------------------7e2e17ee1024", param);

			// 解析Html获得bugId;
			String result = StringUtil.readLinesToString(lines);

			String tmpResult = result.substring(result.indexOf("txtBugID"));
			bugId = tmpResult.substring(tmpResult.indexOf("value=\"") + 7, tmpResult.indexOf("\";"));

		} catch (Exception e) {
			logger.error("post url bugHideFrame URL：{}  error :{}", url, e);
			throw new BusinessException("post url bugHideFrame  error");
		}
		try {
			// 提交工作流
			url = dmpHost + "/DMPV2.0/JSPSource/workflow/h_SaveHandle.jsp?state=0&objType=wf_bug&FFormId={bugId}&FFlowId=45580270699443157042898068791770045&FNodeNo=1&FNodeName=%E6%8F%90%E4%BA%A4%E8%AE%BE%E8%AE%A1&FHandlerId={handlerId}&FOpinion=";

			url = url.replace("{bugId}", bugId);
			url = url.replace("{handlerId}", bugService.getUserIdByUserName(param.getNextHandlePerson()));
			HttpClientUtil.post(url, currentUserId, userName, null);

		} catch (Exception e) {
			logger.error("submit workflow error :{}", e);
			throw new BusinessException("submit workflow error ");
		}
		// 保存Bug
		// TODO
		// documentSearch.addDocument(ConfigConstant.ELASTIC_BUG_INDEX, obj);

	}

	private String replacePlaceholder(String url, AddBugParam param) throws Exception {

		Field[] declaredFields = param.getClass().getDeclaredFields();

		for (Field field : declaredFields) {
			String name = field.getName();
			Method method = param.getClass().getMethod("get" + name.toUpperCase().substring(0, 1) + name.substring(1));

			String result = method.invoke(param) == null ? "" : method.invoke(param).toString();
			if (StringUtil.checkStringContainChinese(result)) {
				result = URLEncoder.encode(result, "UTF-8");
			}

			url = url.replace("{" + field.getName() + "}", result);
		}

		return url;
	}

	public static void main(String[] args) {
	}

	@Override
	@Caching
	public List<ProductTaskDomain> getAllProductTask() {
		String url = dmpHost + "/DMPV2.0/JSPSource/public/GESelect_Data.jsp?wt=prodtasky~yy~yFProdT_ID,FProdT_Name,FProduct_Name&status=&startrow=1&endrow=1000&rndstr=.6214803";

		List<ProductTaskDomain> taskList = Lists.newArrayList();
		try {
			List<String> lines = HttpClientUtil.get(url, "UTF-8");

			String xml = "";
			for (String line : lines) {
				if (!line.isEmpty() && !line.trim().isEmpty()) {
					xml += line;
				}
			}

			taskList = XMLParse.pareXmlGetAllProjectTask(xml);
		} catch (IOException e) {
			logger.error("loadProductTask IOException error:{}", e);
		} catch (DocumentException e) {
			logger.error("loadProductTask DocumentException error:{}", e);
		}

		return taskList;
	}

	@Override
	@Caching
	public List<SubSystem> getAllSubSystem() {
		// 查询条件产品任务为下一代的Id
		String url = dmpHost + "/DMPV2.0/JSPSource/public/GESelect_Data.jsp?wt=prodtsubsysy~ya~aanda~aFSubSystem_Status=f~f1f~fa~aanda~aFProdT_ID=f~f236012601610386847648913483649384966f~fy~yFSubSystem_ID,FSubSystem_Name,FT_ID,FT_Name&status=&startrow=1&endrow=1000&rndstr=.213445";
		List<SubSystem> subSystemList = Lists.newArrayList();
		try {
			List<String> lines = HttpClientUtil.get(url, "UTF-8");
			String xml = "";
			for (String line : lines) {
				if (!line.isEmpty() && !line.trim().isEmpty()) {
					xml += line;
				}
			}
			subSystemList = XMLParse.pareXmlGetAllSubSystem(xml);
		} catch (IOException e) {
			logger.error("loadProductTask IOException error:{}", e);
		} catch (DocumentException e) {
			logger.error("loadProductTask DocumentException error:{}", e);
		}
		return subSystemList;
	}

	@Override
	public String getBugCode(String userName) {

		String userId = null;
		try {
			userId = bugService.getUserIdByUserName(userName);
		} catch (Exception e) {
			logger.error("getUserIdByUserName error:{}", e);
		}

		// 登录
		String url = dmpHost + "/DMPV2.0/JSPSource/OpenWindows.jsp?adsf=98&state=1&url=BugManage/bugEdit.jsp?state=1&title=%E6%96%B0%E5%A2%9Ebug%E8%AE%B0%E5%BD%95";
		HttpResponse repsonse = null;
		try {
			repsonse = HttpClientUtil.getRepsonse(url, "UTF-8", userId, userName);
		} catch (IOException e) {
			logger.error("getBugCode IOException error:{}", e);
		}
		Header firstHeader = repsonse.getFirstHeader("Set-Cookie");

		if (firstHeader != null) {
			String firstHeaderValue = firstHeader.getValue();
			if (firstHeaderValue != null) {
				String sessionId = firstHeaderValue.split(";")[0].split("=")[1];
				HttpClientUtil.setSessionId(sessionId);
			}
		}

		String bugCode = null;
		try {
			bugCode = this.tryGetBugCode(userId, userName);
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bugCode;
	}

	private String tryGetBugCode(String userId, String userName) throws UnsupportedOperationException, IOException {

		String url = dmpHost + "DMPV2.0/JSPSource/BugManage/bugEdit.jsp?state=1";
		HttpResponse repsonse = null;
		try {
			repsonse = HttpClientUtil.getRepsonse(url, "UTF-8", userId, userName);
		} catch (IOException e) {
			logger.error("getBugCode IOException error:{}", e);
		}
		InputStream io = repsonse.getEntity().getContent();
		String html = StringUtil.readLinesToString(IOUtils.readLines(io, "UTF-8"));
		String bugCode = HtmlParse.parseHtmlGetBugCode(html);
		return bugCode;
	}
}
