package com.kingdee.uranus.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingdee.uranus.model.Role;
import com.kingdee.uranus.model.RoleExample;

public interface RoleMapper {

	int countByExample(RoleExample example);

	int deleteByExample(RoleExample example);

	int deleteByPrimaryKey(String roleId);

	int insert(Role record);

	int insertSelective(Role record);

	List<Role> selectByExample(RoleExample example);

	Role selectByPrimaryKey(String roleId);

	int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

	int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

	int updateByPrimaryKeySelective(Role record);

	int updateByPrimaryKey(Role record);
}