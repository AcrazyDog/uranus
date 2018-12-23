package com.kingdee.uranus.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kingdee.uranus.mapper.LogMapper;
import com.kingdee.uranus.service.HomeListService;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年8月21日 下午12:34:43
 * @version
 */
@Service
public class HomeListServiceImpl implements HomeListService {

	@Resource
	private LogMapper logMapper;

	@Override
	public List<Map<String, Object>> getCloserList(Date startDate, Date endDate) {
		return logMapper.selectCloserList(startDate, endDate);
	}

}
