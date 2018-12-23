package com.kingdee.uranus.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.kingdee.uranus.domain.Bug;
import com.kingdee.uranus.model.ProjectTask;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年3月28日 上午10:32:19
 * @version
 */
public class HtmlParse {

	public static Bug parseHtmlGetBug(String html) {
		Document doc = Jsoup.parse(html);

		String nextHandleUserName = doc.getElementById("txtTasker").val();
		String keyWord = doc.getElementById("txtQsnKeyWord").val();
		String operateStep = doc.getElementById("txtQsnOpeStep").val();
		String environment = doc.getElementById("txtQsnDataEnv").val();
		String detail = doc.getElementById("txtQsnDetailDesc").val();
		String imageUrl = doc.getElementById("imgBug1").attr("src");
		String imageUrl2 = doc.getElementById("imgBug2").attr("src");
		String imageUrl3 = doc.getElementById("imgBug3").attr("src");
		String imageUrl4 = doc.getElementById("imgBug4").attr("src");

		Bug bug = new Bug();
		bug.setNextHandleUserName(nextHandleUserName);
		bug.setKeyWord(keyWord);
		bug.setOperateStep(operateStep);
		bug.setEnvironment(environment);
		bug.setDetail(detail);
		bug.setImageUrl(imageUrl);
		bug.setImageUrl2(imageUrl2);
		bug.setImageUrl3(imageUrl3);
		bug.setImageUrl4(imageUrl4);
		return bug;
	}

	public static ProjectTask parseHtmlGetTask(String html) {
		Document doc = Jsoup.parse(html);

		ProjectTask task = new ProjectTask();
		String desc = doc.getElementById("desc").val();
		String projectTaskCode = doc.getElementById("code").val();
		String prodtName = doc.getElementById("prodtName").val();
		String taskName = doc.getElementById("projtaskname").val();

		task.setDesc(desc);
		task.setProjectTaskCode(projectTaskCode);
		task.setProdtName(prodtName);
		task.setTaskName(taskName);

		return task;
	}

	public static String parseHtmlGetBugCode(String html) {
		Document doc = Jsoup.parse(html);
		if (doc.getElementById("txtBugCode") == null) {
			return null;
		}
		return doc.getElementById("txtBugCode").val();
	}

	public static String parseHtmlGetBugId(String html) {
		Document doc = Jsoup.parse(html);
		return doc.getElementById("txtBugCode").val();
	}

}
