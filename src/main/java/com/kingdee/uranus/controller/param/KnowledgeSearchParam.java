package com.kingdee.uranus.controller.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.kingdee.uranus.util.DateUtil;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年6月14日 下午2:12:23
 * @version
 */
public class KnowledgeSearchParam {

	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date endDate;

	private String createName;

	private String title;

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return DateUtil.getNextDate(endDate);
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
