package com.kingdee.uranus.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kingdee.uranus.model.LoginRecord;
import com.kingdee.uranus.model.LoginRecordExample;

@Mapper
public interface LoginRecordMapper {

	int deleteByPrimaryKey(String id);

	int insert(LoginRecord record);

	int insertSelective(LoginRecord record);

	LoginRecord selectByPrimaryKey(String id);

	int updateByExampleSelective(@Param("record") LoginRecord record, @Param("example") LoginRecordExample example);

	int updateByExample(@Param("record") LoginRecord record, @Param("example") LoginRecordExample example);

	int updateByPrimaryKeySelective(LoginRecord record);

	int updateByPrimaryKey(LoginRecord record);

	public List<LoginRecord> selectLoginRecords(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("searchAccount") String searchAccount);
}