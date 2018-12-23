package com.kingdee.uranus.service;

import com.kingdee.uranus.core.PageBean;
import com.kingdee.uranus.model.Log;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月11日 下午3:11:03
 * @version
 */
public interface LogService {

	void saveLog(Log log);

	PageBean<Log> getLogList(String startDate, String endDate, String userName);

}
