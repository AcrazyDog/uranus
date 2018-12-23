package com.kingdee.uranus.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.kingdee.uranus.core.PageBean;
import com.kingdee.uranus.core.PaginationContext;
import com.kingdee.uranus.mapper.LogMapper;
import com.kingdee.uranus.model.Log;
import com.kingdee.uranus.service.LogService;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月11日 下午3:12:54
 * @version
 */
@Service("logService")
public class LogServiceImpl implements LogService {

	@Resource
	private LogMapper logMapper;

	@Override
	public void saveLog(Log log) {
		logMapper.insert(log);
	}

	@Override
	public PageBean<Log> getLogList(String startDate, String endDate, String userName) {
		PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
		List<Log> list = logMapper.selectLogList(startDate, endDate, userName);
		return new PageBean<Log>(list);
	}

}
