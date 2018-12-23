package com.kingdee.uranus.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.core.utils.UUIDUtil;
import com.kingdee.uranus.mapper.PermissionMapper;
import com.kingdee.uranus.model.Permission;
import com.kingdee.uranus.model.PermissionExample;
import com.kingdee.uranus.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public List<Permission> getMenusByUser(String userId) {
		List<Permission> results = new ArrayList<Permission>();
		List<Permission> permissions = permissionMapper.selectPermissionByUserId(userId);
		for (Permission one1 : permissions) {
			if ("0".equals(one1.getParentId())) {
				List<Permission> subMenu = new ArrayList<Permission>();
				for (Permission one2 : permissions) {
					if (one1.getPermissionId().equals(one2.getParentId())) {
						subMenu.add(one2);
					}
				}
				one1.setSubMenus(subMenu);
				results.add(one1);
			}
		}
		return results;
	}

	@Override
	public PageResult<Permission> getPermissions(Integer page, Integer limit, String searchKey, String searchValue) {
		PageResult<Permission> results = new PageResult<Permission>();
		Page<Object> startPage = PageHelper.startPage(page, limit);
		List<Permission> permissions = permissionMapper.selectPermissions(searchKey, searchValue);
		results.setData(permissions);
		results.setCount(startPage.getTotal());
		return results;
	}

	@Override
	public List<Permission> getPermissionsByRole(String roleId) {
		return permissionMapper.selectPermissionByRoleId(roleId);
	}

	@Override
	public List<Permission> getParentPermissions(int type) {
		List<Permission> result;
		if (type == 0) {
			PermissionExample example = new PermissionExample();
			example.setOrderByClause("order_number asc");
			PermissionExample.Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo("0");
			criteria.andPermissionTypeEqualTo(0);
			result = permissionMapper.selectByExample(example);
		} else {
			result = permissionMapper.selectButtonParent();
		}
		return result;
	}

	@Override
	public boolean addPermission(Permission permission) {
		permission.setPermissionId(UUIDUtil.randomUUID8());
		permission.setCreateTime(new Date());
		if (com.kingdee.uranus.core.utils.StringUtil.isBlank(permission.getParentId())) {
			permission.setParentId("0");
		}
		return permissionMapper.insertSelective(permission) > 0;
	}

	@Override
	public boolean updatePermission(Permission permission) {
		if (com.kingdee.uranus.core.utils.StringUtil.isBlank(permission.getParentId())) {
			permission.setParentId("0");
		}
		return permissionMapper.updateByPrimaryKeySelective(permission) > 0;
	}

	@Override
	public boolean updatePermissionStatus(String permissionId, int isDelete) {
		Permission permission = new Permission();
		permission.setPermissionId(permissionId);
		permission.setIsDelete(isDelete);
		return permissionMapper.updateByPrimaryKeySelective(permission) > 0;
	}

	@Override
	public boolean deletePermission(String permissionId) throws BusinessException {
		try {
			return permissionMapper.deleteByPrimaryKey(permissionId) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("权限已被使用");
		}
	}
}
