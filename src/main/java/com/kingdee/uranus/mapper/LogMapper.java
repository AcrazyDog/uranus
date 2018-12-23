package com.kingdee.uranus.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.kingdee.uranus.model.Log;

public interface LogMapper {

	int deleteByPrimaryKey(Long id);

	int insert(Log record);

	int insertSelective(Log record);

	Log selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Log record);

	int updateByPrimaryKey(Log record);

	Page<Log> selectLogList(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("userName") String userName);

	int selectLogCount(@Param("startDate") String startDate, @Param("endDate") String endDate,
			@Param("userName") String userName);

	List<Map<String, Object>> selectCloserList(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}