package com.kingdee.uranus.service;

import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.model.LoginRecord;

/**
 * 登录日志Service
 * 
 * @author wangfan
 * @date 2017-7-24 下午1:16:02
 */
public interface LoginRecordService {

	/**
	 * 添加登录日志
	 * 
	 * @param loginRecord
	 * @return
	 */
	public boolean addLoginRecord(LoginRecord loginRecord);

	/**
	 * 查询登录日志
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @param startDate
	 * @param endDate
	 * @param searchAccount
	 * @return
	 */
	public PageResult<LoginRecord> getLoginRecords(int pageIndex, int pageSize, String startDate, String endDate, String searchAccount);
}
