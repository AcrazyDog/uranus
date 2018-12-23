package com.kingdee.uranus.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.core.utils.UUIDUtil;
import com.kingdee.uranus.mapper.LoginRecordMapper;
import com.kingdee.uranus.model.LoginRecord;
import com.kingdee.uranus.service.LoginRecordService;

@Service
public class LoginRecordServiceImpl implements LoginRecordService {
	@Autowired
	private LoginRecordMapper loginRecordMapper;

	@Override
	public boolean addLoginRecord(LoginRecord object) {
		object.setId(UUIDUtil.randomUUID8());
		object.setCreateTime(new Date());
		return loginRecordMapper.insert(object)>0;
	}

	@Override
	public PageResult<LoginRecord> getLoginRecords(int pageIndex, int pageSize, String startDate, String endDate, String searchAccount) {
		Page<Object> startPage = PageHelper.startPage(pageIndex, pageSize);
		List<LoginRecord> list = loginRecordMapper.selectLoginRecords(startDate,endDate,searchAccount);
		return new PageResult<LoginRecord>(startPage.getTotal(), list);
	}
}