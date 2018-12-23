package com.kingdee.uranus.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kingdee.uranus.model.Permission;
import com.kingdee.uranus.model.PermissionExample;

public interface PermissionMapper {

	int countByExample(PermissionExample example);

	int deleteByExample(PermissionExample example);

	int deleteByPrimaryKey(String permissionId);

	int insert(Permission record);

	int insertSelective(Permission record);

	List<Permission> selectByExample(PermissionExample example);

	Permission selectByPrimaryKey(String permissionId);

	int updateByExampleSelective(@Param("record") Permission record, @Param("example") PermissionExample example);

	int updateByExample(@Param("record") Permission record, @Param("example") PermissionExample example);

	int updateByPrimaryKeySelective(Permission record);

	int updateByPrimaryKey(Permission record);

	// 根据角色id查询所有权限
	public List<Permission> selectPermissionByRoleId(@Param("roleId") String roleId);

	public List<Permission> selectPermissions(@Param("searchKey") String searchKey, @Param("searchValue") String searchValue);

	public List<Permission> selectPermissionsByType(Integer permissionType);

	public List<Permission> selectPermissionByUserId(String userId);

	public List<Permission> selectButtonParent();

}