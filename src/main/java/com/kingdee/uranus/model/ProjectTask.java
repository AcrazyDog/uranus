package com.kingdee.uranus.model;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年4月27日 下午1:05:03
 * @version
 */
public class ProjectTask {

	private String projectTaskId;

	private String projectTaskCode;

	private String taskType;

	private String projectTask;

	private String taskName;

	private String preTime;

	private String adviceName;

	private String adviceTime;

	private String group;

	// prodtName产品任务
	private String prodtName;

	// 助记码
	private String taskFlagCode;

	// 详细描述
	private String desc;

	// 工作流状态
	private String workflowStatus;

	public String getProdtName() {
		return prodtName;
	}

	public void setProdtName(String prodtName) {
		this.prodtName = prodtName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTaskFlagCode() {
		return taskFlagCode;
	}

	public void setTaskFlagCode(String taskFlagCode) {
		this.taskFlagCode = taskFlagCode;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getProjectTaskId() {
		return projectTaskId;
	}

	public void setProjectTaskId(String projectTaskId) {
		this.projectTaskId = projectTaskId;
	}

	public String getProjectTaskCode() {
		return projectTaskCode;
	}

	public void setProjectTaskCode(String projectTaskCode) {
		this.projectTaskCode = projectTaskCode;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getProjectTask() {
		return projectTask;
	}

	public void setProjectTask(String projectTask) {
		this.projectTask = projectTask;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getPreTime() {
		return preTime;
	}

	public void setPreTime(String preTime) {
		this.preTime = preTime;
	}

	public String getAdviceName() {
		return adviceName;
	}

	public void setAdviceName(String adviceName) {
		this.adviceName = adviceName;
	}

	public String getAdviceTime() {
		return adviceTime;
	}

	public void setAdviceTime(String adviceTime) {
		this.adviceTime = adviceTime;
	}

}
