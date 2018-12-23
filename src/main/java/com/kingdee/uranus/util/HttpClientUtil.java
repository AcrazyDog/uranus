package com.kingdee.uranus.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.common.collect.Maps;
import com.kingdee.uranus.domain.AddBugParam;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年2月8日 下午7:55:38
 * @version
 */
public class HttpClientUtil {

	private static HashMap<String, String> cookie = Maps.newHashMap();

	static {
		cookie.put("XDMP_usercode", "US18279");
		cookie.put("XDMP_useraccount", "kang_nie");
		cookie.put("XDMP_deptname", "%CF%C2%D2%BB%B4%FA%BD%F0%B5%FB%D4%C6%D2%B5%CE%F1%B2%FA%C6%B7%B2%BF");
		cookie.put("XDMP_deptid", "FBEDCE81-D83E-44DE-99E8-B963D3877D5F");
		cookie.put("XDMP_ftid", "538318527324793878597484270924965386%2C597806855988735674730210888250043659");
		cookie.put("allPage", "prjmanager%2Fprjlist.jsp");
		cookie.put("FAuthorityG_ID",
				"097063923673153231401930613295369895%2C106038592879015188752868888505340870%2CEE6577DA-E485-4CDF-A2AE-881F75F4261F%2C");
		cookie.put("allTitle",
				"%BA%C4%B7%D1%D6%D0%D0%C4%7E%CF%EE%C4%BF%C8%CE%CE%F1%C7%E5%B5%A5%7E%B2%E2%CA%D4%C7%E5%B5%A5");
		cookie.put("JSESSIONID", "7CD5134BBB4CE625E59B28FB8F1F031A");

	}

	private static final String keys = "testCaseTask_Code,tmpSubSystem_ID,txtAddOver,txtBugID,txtHide,txtSubSysID,txtProjTeamID,txtProjTaskID,txtIsLock,txtState,isOverlength,isEnableSave,txtPicOrigin1,txtPicNow1,txtPicOrigin2,txtPicNow2,txtPicOrigin3,txtPicNow3,txtPicOrigin4,txtPicNow4,txtPicOrigin5,txtPicNow5,txtBugCode,prodtName,ddlProdTask,txtProdName,txtProdVer,txtSubSys,ddlDefectType,txtProjTeam,ItStage,ddlDetectStage,ddlDefectProp,txtImportStageName,txtImportS,ddlDB,txtReasonAnalyse,ddlBugPri,FRequireDealDate,ddlTestAngle,ddlPonderance,ddlInstancy,ddlTestMethod,txtNotice,ddlOS,txtStatus,ddlTestEnv1,ddlTestEnv2,FLanguageVersion,FIsRecurred,FIsRepeated,FIsHistory,txtTasker,txtQsnKeyWord,txtQsnOpeStep,txtQsnDetailDesc,txtQsnDataEnv,txtProjTask,txtPreTask,txtBugMod,txtBugMod_Author,txtTestCaseID,txtTestReqID,testCaseTask_Name,testCaseTask_ID,oldAttachment,txtTestor,txtTestDate,txtSubmitDate,FLastDealDate";

	public static void setSessionId(String sessionId) {
		cookie.put("JSESSIONID", sessionId);
	}

	public static String getCookieStr() {
		String cookieStr = "";
		for (Entry<String, String> entry : cookie.entrySet()) {
			cookieStr += entry.getKey() + "=" + entry.getValue() + ";";
		}

		return cookieStr;
	}

	public static List<String> get(String url, String charset) throws ClientProtocolException, IOException {
		HttpResponse repsonse = getRepsonse(url, charset, null, null);
		InputStream io = repsonse.getEntity().getContent();

		return IOUtils.readLines(io, charset == null ? "UTF-8" : charset);
	}

	public static HttpResponse getRepsonse(String url, String charset, String userId, String userName)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();

		HttpGet httpGet = new HttpGet(url);

		httpGet.addHeader("Accept", "*/*");
		httpGet.addHeader("User-Agent",
				" Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; InfoPath.3; .NET4.0C)");
		httpGet.addHeader("Proxy-Connection", "keep-alive");
		httpGet.addHeader("Upgrade-Insecure-Requests", "1");
		httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");

		String cookieStr = getCookieStr();
		cookieStr += "XDMP_userid=" + (userId == null ? "20BFECC6-5157-4570-911C-D244B0E04251" : userId) + ";";
		cookieStr += "XDMP_username=" + URLEncoder.encode(userName == null ? "聂康" : userName, "GBK");
		httpGet.addHeader("Cookie", cookieStr);

		return httpClient.execute(httpGet);
	}

	public static List<String> post(String url, String userId, String userName, String contentType)
			throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url);

		httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpPost.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");

		httpPost.addHeader("Proxy-Connection", "keep-alive");
		httpPost.addHeader("Upgrade-Insecure-Requests", "1");
		httpPost.addHeader("Content-Type", contentType == null ? "application/x-www-form-urlencoded" : contentType);
		httpPost.addHeader("XDMP_userid", userId);
		httpPost.addHeader("XDMP_username=%C4%F4%BF%B5", userName);

		String cookieStr = getCookieStr();
		cookieStr += "XDMP_userid=" + userId + ";";
		cookieStr += "XDMP_username=" + URLEncoder.encode(userName, "GBK");

		httpPost.addHeader("Cookie", cookieStr);

		HttpResponse response = httpClient.execute(httpPost);
		InputStream io = response.getEntity().getContent();

		return IOUtils.readLines(io);
	}

	public static List<String> postMultipartParam(String url, String userId, String userName, String contentType,
			AddBugParam param) throws Exception {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url);

		httpPost.addHeader("Content-Type", "multipart/form-data;");
		httpPost.addHeader("Accept", "*/*");
		httpPost.addHeader("Referer", " http://192.168.16.18/DMPV2.0/JSPSource/BugManage/bugEdit.jsp?state=1");
		httpPost.addHeader("Accept-Language", "zh-CN");
		httpPost.addHeader("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; InfoPath.3; .NET4.0C)");
		httpPost.addHeader("Accept-Encoding", "gzip, deflate");
		httpPost.addHeader("Connection", "Keep-Alive");
		httpPost.addHeader("Pragma", "no-cache");

		String cookieStr = getCookieStr();
		cookieStr += "XDMP_userid=" + userId + ";";
		cookieStr += "XDMP_username=" + URLEncoder.encode(userName, "GBK");

		httpPost.addHeader("Cookie", cookieStr);
		HttpEntity httpEntity = getEntityBuilder(param).build();
		httpPost.setEntity(httpEntity);
		HttpResponse response = httpClient.execute(httpPost);

		InputStream io = response.getEntity().getContent();
		return IOUtils.readLines(io);

	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		String urlstr = "http://192.168.16.18/DMPV2.0/JSPSource/Message/Message_in_Data.jsp?wt=Bug_d~dd~d__d~dd~d_%E8%81%82%E5%BA%B7_d~dd~d_20BFECC6-5157-4570-911C-D244B0E04251_d~dd~d_a~aa~a_d~dd~d_&status=&startrow=1&endrow=100&rndstr=6.021625E-02";

		for (String line : get(urlstr, null)) {
			System.out.println(line);
		}

	}

	private static MultipartEntityBuilder getEntityBuilder(AddBugParam param) throws Exception {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody("testCaseTask_Code", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("tmpSubSystem_ID", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtAddOver", "1", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtBugID", "null", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtHide", "1", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtSubSysID", param.getSubSystemId(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtProjTeamID", param.getGroupId(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));

		// TODO
		// 这个值有疑问
		builder.addTextBody("txtProjTaskID", param.getProjectTaskId(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtIsLock", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtState", "1", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("isOverlength", "0", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("isEnableSave", "1", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicOrigin1", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicNow1", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicOrigin2", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicNow2", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicOrigin3", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicNow3", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicOrigin4", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicNow4", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicOrigin5", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPicNow5", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtBugCode", param.getTxtBugCode(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("prodtName", param.getProductName(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlProdTask", param.getProjectTaskId(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtProdName", param.getTxtProdName(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtProdVer", param.getTxtProdVer(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtSubSys", param.getSubSystemName(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlDefectType", param.getDdlDefectType(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtProjTeam", param.getGroup(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ItStage", param.getItStage(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlDetectStage", param.getDdlDetectStage(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlDefectProp", param.getDdlDefectProp(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtImportStageName", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtImportS", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlDB", "CA271099-E611-492E-91E4-EF3670E8224F",
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtReasonAnalyse", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlBugPri", param.getDdlBugPri(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("FRequireDealDate", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlTestAngle", "选择测试角度", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlPonderance", "737DA4C8-37B5-4D12-B18D-640829AC76BA",
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlInstancy", "E7181B40-BF14-44D3-877F-7FEA96B5BB23",
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlTestMethod", "AA899435-8246-4BA8-960B-DE5C84C1CDB2",
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtNotice", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlOS", "4311B5FD-E212-40CE-8DC0-26B56A9BF4EC",
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtStatus", "计划", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlTestEnv1", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("ddlTestEnv2", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("FLanguageVersion", "简体", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("FIsRecurred", param.getFIsRecurred(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("FIsRepeated", param.getFIsRepeated(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("FIsHistory", "否", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtTasker", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtQsnKeyWord", param.getTxtQsnKeyWord(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtQsnOpeStep", param.getTxtQsnOpeStep(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtQsnDetailDesc", param.getTxtQsnDetailDesc(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtQsnDataEnv", param.getTxtQsnDataEnv(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtProjTask", param.getRelateProjectTaskName(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtPreTask", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtBugMod", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtBugMod_Author", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtTestCaseID", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtTestReqID", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("testCaseTask_Name", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("testCaseTask_ID", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("oldAttachment", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addBinaryBody("attachment", new byte[] {}, ContentType.APPLICATION_OCTET_STREAM, "");
		builder.addTextBody("txtTestor", param.getTxtTestor(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtTestDate", param.getTxtTestDate(),
				ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("txtSubmitDate", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.addTextBody("FLastDealDate", "", ContentType.DEFAULT_TEXT.withCharset(Charset.forName("UTF-8")));
		builder.setBoundary("---------------------------7e2e17ee1024");
		return builder;
	}
}
