package com.kingdee.uranus.model;

import java.io.Serializable;
import java.util.Date;

public class Log implements Serializable {
	/**
	 * <p>
	 * 
	 * </p>
	 * 
	 * @author rd_kang_nie
	 * @date 2018年5月9日 下午2:21:19
	 * @version
	 */
	private static final long serialVersionUID = -4786504114381000591L;

	private Long id;

	private String bugNo;

	private String bugCloser;

	private String ip;

	private Date createTime;

	private String reason;

	private LogType logType;

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBugNo() {
		return bugNo;
	}

	public void setBugNo(String bugNo) {
		this.bugNo = bugNo == null ? null : bugNo.trim();
	}

	public String getBugCloser() {
		return bugCloser;
	}

	public void setBugCloser(String bugCloser) {
		this.bugCloser = bugCloser == null ? null : bugCloser.trim();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}

	public static void main(String[] args) {
		System.out.println(LogType.valueOf("1"));
	}
}