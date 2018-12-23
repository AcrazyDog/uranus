package com.kingdee.uranus.domain;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年3月21日 下午4:53:34
 * @version
 */
public class Bug {

	private String bugId;

	private String bugType;

	private String bugDesc;

	private String bugNo;

	private String group;

	private String toDate;

	private String toUserName;

	/**
	 * 子系统
	 */
	private String txtSubSys;

	/**
	 * 发现阶段
	 */
	private String discoveryTime;

	/**
	 * 测试员
	 */
	private String tester;

	/**
	 * 测试日期
	 */
	private String testTime;

	/**
	 * 下一步处理人
	 */
	private String nextHandleUserName;

	/**
	 * 优先级
	 */
	private String priority;

	/**
	 * 关键词
	 */
	private String keyWord;

	/**
	 * 操作步骤
	 */
	private String operateStep;

	/**
	 * 详细描述
	 */
	private String detail;

	/**
	 * 环境
	 */
	private String environment;

	/**
	 * 错误图片
	 */
	private String imageUrl;

	/**
	 * 错误图片2
	 */
	private String imageUrl2;

	/**
	 * 错误图片3
	 */
	private String imageUrl3;

	/**
	 * 错误图片4
	 */
	private String imageUrl4;

	/**
	 * 处理详情
	 */
	private List<HandleDetail> handleList;

	public String getImageUrl2() {
		return imageUrl2;
	}

	public void setImageUrl2(String imageUrl2) {
		this.imageUrl2 = imageUrl2;
	}

	public String getImageUrl3() {
		return imageUrl3;
	}

	public void setImageUrl3(String imageUrl3) {
		this.imageUrl3 = imageUrl3;
	}

	public String getImageUrl4() {
		return imageUrl4;
	}

	public void setImageUrl4(String imageUrl4) {
		this.imageUrl4 = imageUrl4;
	}

	public List<HandleDetail> getHandleList() {
		return handleList;
	}

	public void setHandleList(List<HandleDetail> handleList) {
		this.handleList = handleList;
	}

	public String getTxtSubSys() {
		return txtSubSys;
	}

	public void setTxtSubSys(String txtSubSys) {
		this.txtSubSys = txtSubSys;
	}

	public String getDiscoveryTime() {
		return discoveryTime;
	}

	public void setDiscoveryTime(String discoveryTime) {
		this.discoveryTime = discoveryTime;
	}

	public String getTester() {
		return tester;
	}

	public void setTester(String tester) {
		this.tester = tester;
	}

	public String getTestTime() {
		return testTime;
	}

	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getNextHandleUserName() {
		return nextHandleUserName;
	}

	public void setNextHandleUserName(String nextHandleUserName) {
		this.nextHandleUserName = nextHandleUserName;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getOperateStep() {
		return operateStep;
	}

	public void setOperateStep(String operateStep) {
		this.operateStep = operateStep;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getBugId() {
		return bugId;
	}

	public void setBugId(String bugId) {
		this.bugId = bugId;
	}

	public String getBugType() {
		return bugType;
	}

	public void setBugType(String bugType) {
		this.bugType = bugType;
	}

	public String getBugDesc() {
		return bugDesc;
	}

	public void setBugDesc(String bugDesc) {
		this.bugDesc = bugDesc;
	}

	public String getBugNo() {
		return bugNo;
	}

	public void setBugNo(String bugNo) {
		this.bugNo = bugNo;
	}

}
