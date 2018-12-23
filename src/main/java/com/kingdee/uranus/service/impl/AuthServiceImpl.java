package com.kingdee.uranus.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingdee.uranus.core.utils.UUIDUtil;
import com.kingdee.uranus.mapper.PermissionMapper;
import com.kingdee.uranus.mapper.RolePermissionMapper;
import com.kingdee.uranus.model.MenuTree;
import com.kingdee.uranus.model.Permission;
import com.kingdee.uranus.model.PermissionExample;
import com.kingdee.uranus.model.RolePermission;
import com.kingdee.uranus.model.RolePermissionExample;
import com.kingdee.uranus.service.AuthService;

/**
 * 权限
 * 
 * @author wangfan
 * @date 2017-4-27 下午5:19:43
 */
@Service(value = "authService")
public class AuthServiceImpl implements AuthService {

	@Resource
	private RolePermissionMapper rolePermissionMapper;

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public List<MenuTree> getPermissionTree(String roleId) {
		List<MenuTree> listMenuTree = new ArrayList<MenuTree>();
		PermissionExample permissionExample = new PermissionExample();
		permissionExample.setOrderByClause("order_number ASC");
		PermissionExample.Criteria permissionCriteria = permissionExample.createCriteria();
		permissionCriteria.andIsDeleteEqualTo(0);
		List<Permission> allPermissions = permissionMapper.selectByExample(permissionExample);
		RolePermissionExample rolePermissionExample = new RolePermissionExample();
		RolePermissionExample.Criteria rolePermissionCriteria = rolePermissionExample.createCriteria();
		rolePermissionCriteria.andRoleIdEqualTo(roleId);
		List<RolePermission> rolePermissions = rolePermissionMapper.selectByExample(rolePermissionExample);
		for (Permission one : allPermissions) {
			MenuTree menuTree = new MenuTree();
			menuTree.setId(one.getPermissionId());
			menuTree.setName(one.getPermissionName());
			menuTree.setpId(one.getParentId());
			menuTree.setOpen(true);
			menuTree.setChecked(false);
			for (RolePermission temp : rolePermissions) {
				if (temp.getPermissionId().equals(one.getPermissionId())) {
					menuTree.setChecked(true);
					break;
				}
			}
			listMenuTree.add(menuTree);
		}
		return listMenuTree;
	}

	@Override
	public boolean updateRolePermission(String roleId, List<String> authIds) {
		RolePermissionExample example = new RolePermissionExample();
		RolePermissionExample.Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		rolePermissionMapper.deleteByExample(example);
		if (authIds != null && authIds.size() > 0) {
			for (String authId : authIds) {
				RolePermission record = new RolePermission();
				record.setId(UUIDUtil.randomUUID8());
				record.setRoleId(roleId);
				record.setPermissionId(authId);
				rolePermissionMapper.insert(record);
			}
		}
		return true;
	}
}
