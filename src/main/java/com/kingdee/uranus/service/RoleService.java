package com.kingdee.uranus.service;

import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.core.exception.ParameterException;
import com.kingdee.uranus.model.Role;

/**
 * 角色操作相关的service
 * 
 * @author wangfan
 * @date 2017-4-27 下午5:37:20
 */
public interface RoleService {
	
	/**
	 * 查询所有角色
	 */
	public PageResult<Role> getRoles(int pageNum, int pageSize, String searchKey, String searchValue, Integer isDelete);

	/**
	 * 根据id查询角色
	 */
	public Role getRoleById(String roleId);

	/**
	 * 添加角色
	 */
	public boolean addRole(Role role);

	/**
	 * 修改角色
	 */
	public boolean updateRole(Role role);

	/**
	 * 修改角色状态
	 */
	public boolean updateRoleStatus(String roleId, int isDelete) throws ParameterException;

	/**
	 * 删除角色
	 */
	public boolean deleteRole(String roleId) throws BusinessException ;

}
