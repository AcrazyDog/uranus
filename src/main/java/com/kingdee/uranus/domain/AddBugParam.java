package com.kingdee.uranus.domain;

import lombok.Data;

@Data
public class AddBugParam {

	private String txtBugCode;

	private String productName;

	private String projectTaskId;

	private String subSystemName;

	private String subSystemId;

	private String group;
	private String groupId;
	private String ddlDefectProp;

	private String ddlDefectType;

	private String ddlDetectStage;
	private String txtQsnDataEnv;
	private String ItStage;

	private String relateProjectTaskName;

	private String relateProjectTaskId;

	private String txtQsnKeyWord;

	private String txtQsnOpeStep;
	private String ddlBugPri;

	private String imageUrls;

	private String FIsRepeated;

	private String FIsRecurred;

	// 测试人员
	private String txtTestor;
	// 测试时间
	private String txtTestDate;

	private String nextHandlePerson;

	/**
	 * 产品版本
	 */
	private String txtProdVer;

	private String txtProdName;

	private String ddlTestAngle;

	private String ddlPonderance;

	private String ddlInstancy;

	private String ddlTestMethod;

	private String ddlOS;

	private String txtStatus;

	private String FLanguageVersion;

	private String FIsHistory;

	private String txtQsnDetailDesc;

	// TODO
	// 未加入到页面
	// private Date fRequireDealDate;

	public String getTxtProdVer() {
		return txtProdVer;
	}

	public String getTxtQsnDetailDesc() {
		return txtQsnDetailDesc;
	}

	public void setTxtQsnDetailDesc(String txtQsnDetailDesc) {
		this.txtQsnDetailDesc = txtQsnDetailDesc;
	}

	public void setTxtProdVer(String txtProdVer) {
		this.txtProdVer = txtProdVer;
	}

	public String getTxtProdName() {
		return txtProdName;
	}

	public void setTxtProdName(String txtProdName) {
		this.txtProdName = txtProdName;
	}

	public String getDdlTestAngle() {
		return ddlTestAngle;
	}

	public void setDdlTestAngle(String ddlTestAngle) {
		this.ddlTestAngle = ddlTestAngle;
	}

	public String getDdlPonderance() {
		return ddlPonderance;
	}

	public void setDdlPonderance(String ddlPonderance) {
		this.ddlPonderance = ddlPonderance;
	}

	public String getDdlInstancy() {
		return ddlInstancy;
	}

	public void setDdlInstancy(String ddlInstancy) {
		this.ddlInstancy = ddlInstancy;
	}

	public String getDdlTestMethod() {
		return ddlTestMethod;
	}

	public void setDdlTestMethod(String ddlTestMethod) {
		this.ddlTestMethod = ddlTestMethod;
	}

	public String getDdlOS() {
		return ddlOS;
	}

	public void setDdlOS(String ddlOS) {
		this.ddlOS = ddlOS;
	}

	public String getTxtStatus() {
		return txtStatus;
	}

	public void setTxtStatus(String txtStatus) {
		this.txtStatus = txtStatus;
	}

	public String getFLanguageVersion() {
		return FLanguageVersion;
	}

	public void setFLanguageVersion(String fLanguageVersion) {
		FLanguageVersion = fLanguageVersion;
	}

	public String getFIsHistory() {
		return FIsHistory;
	}

	public void setFIsHistory(String fIsHistory) {
		FIsHistory = fIsHistory;
	}

	public String getTxtTestor() {
		return txtTestor;
	}

	public String getNextHandlePerson() {
		return nextHandlePerson;
	}

	public void setNextHandlePerson(String nextHandlePerson) {
		this.nextHandlePerson = nextHandlePerson;
	}

	public String getTxtBugCode() {
		return txtBugCode;
	}

	public void setTxtBugCode(String txtBugCode) {
		this.txtBugCode = txtBugCode;
	}

	public void setTxtTestor(String txtTestor) {
		this.txtTestor = txtTestor;
	}

	public String getTxtTestDate() {
		return txtTestDate;
	}

	public void setTxtTestDate(String txtTestDate) {
		this.txtTestDate = txtTestDate;
	}

	public String getFIsRepeated() {
		return FIsRepeated;
	}

	public void setFIsRepeated(String fIsRepeated) {
		FIsRepeated = fIsRepeated;
	}

	public String getFIsRecurred() {
		return FIsRecurred;
	}

	public void setFIsRecurred(String fIsRecurred) {
		FIsRecurred = fIsRecurred;
	}

	// public Date getfRequireDealDate() {
	// return fRequireDealDate;
	// }
	//
	// public void setfRequireDealDate(Date fRequireDealDate) {
	// this.fRequireDealDate = fRequireDealDate;
	// }

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProjectTaskId() {
		return projectTaskId;
	}

	public void setProjectTaskId(String projectTaskId) {
		this.projectTaskId = projectTaskId;
	}

	public String getSubSystemName() {
		return subSystemName;
	}

	public void setSubSystemName(String subSystemName) {
		this.subSystemName = subSystemName;
	}

	public String getSubSystemId() {
		return subSystemId;
	}

	public void setSubSystemId(String subSystemId) {
		this.subSystemId = subSystemId;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDdlDefectProp() {
		return ddlDefectProp;
	}

	public void setDdlDefectProp(String ddlDefectProp) {
		this.ddlDefectProp = ddlDefectProp;
	}

	public String getDdlDefectType() {
		return ddlDefectType;
	}

	public void setDdlDefectType(String ddlDefectType) {
		this.ddlDefectType = ddlDefectType;
	}

	public String getDdlDetectStage() {
		return ddlDetectStage;
	}

	public void setDdlDetectStage(String ddlDetectStage) {
		this.ddlDetectStage = ddlDetectStage;
	}

	public String getTxtQsnDataEnv() {
		return txtQsnDataEnv;
	}

	public void setTxtQsnDataEnv(String txtQsnDataEnv) {
		this.txtQsnDataEnv = txtQsnDataEnv;
	}

	public String getItStage() {
		return ItStage;
	}

	public void setItStage(String itStage) {
		ItStage = itStage;
	}

	public String getRelateProjectTaskName() {
		return relateProjectTaskName;
	}

	public void setRelateProjectTaskName(String relateProjectTaskName) {
		this.relateProjectTaskName = relateProjectTaskName;
	}

	public String getRelateProjectTaskId() {
		return relateProjectTaskId == null ? "" : relateProjectTaskId;
	}

	public void setRelateProjectTaskId(String relateProjectTaskId) {
		this.relateProjectTaskId = relateProjectTaskId;
	}

	public String getTxtQsnKeyWord() {
		return txtQsnKeyWord;
	}

	public void setTxtQsnKeyWord(String txtQsnKeyWord) {
		this.txtQsnKeyWord = txtQsnKeyWord;
	}

	public String getTxtQsnOpeStep() {
		return txtQsnOpeStep;
	}

	public void setTxtQsnOpeStep(String txtQsnOpeStep) {
		this.txtQsnOpeStep = txtQsnOpeStep;
	}

	public String getDdlBugPri() {
		return ddlBugPri;
	}

	public void setDdlBugPri(String ddlBugPri) {
		this.ddlBugPri = ddlBugPri;
	}

	public String getImageUrls() {
		return imageUrls == null ? "" : imageUrls;
	}

	public void setImageUrls(String imageUrls) {
		this.imageUrls = imageUrls;
	}

}
