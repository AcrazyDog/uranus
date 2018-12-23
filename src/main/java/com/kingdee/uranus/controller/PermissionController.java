package com.kingdee.uranus.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.core.ResultMap;
import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.core.exception.ParameterException;
import com.kingdee.uranus.core.utils.JSONUtil;
import com.kingdee.uranus.model.MenuTree;
import com.kingdee.uranus.model.Permission;
import com.kingdee.uranus.service.AuthService;
import com.kingdee.uranus.service.PermissionService;

/**
 * 菜单管理
 * @author wangfan
 * @date 2017-3-24 下午3:56:21
 */
@RestController
@RequestMapping("/api/permission")
public class PermissionController {
	@Autowired
	private PermissionService menuService;
	@Autowired
	private AuthService authService;
	
	/**
	 * 查询所有菜单
	 */
	@GetMapping()
	public PageResult<Permission> list(Integer page, Integer limit, String searchKey, String searchValue) throws UnsupportedEncodingException{
		if(searchValue != null){
			searchValue = new String(searchValue.getBytes("ISO-8859-1"), "UTF-8");
		}
		if(page == null) {
			page = 0;
			limit = 0;
		}
		return menuService.getPermissions(page, limit, searchKey, searchValue);
	}
	
	/**
	 * 角色权限菜单树
	 */
	@GetMapping("/tree/{roleId}")
	public List<MenuTree> listPermTree(@PathVariable("roleId") String roleId){
		return authService.getPermissionTree(roleId);
	}
	
	/**
	 * 修改角色权限
	 */
	@PutMapping("/tree")
	public ResultMap updatePermTree(String roleId, String permIds){
		List<String> permissionIds = JSONUtil.parseArray(permIds);
		if(authService.updateRolePermission(roleId, permissionIds)){
//			SubjectUtil.getInstance().updateCachePermission();
			return ResultMap.ok("修改成功");
		}else{
			return ResultMap.error("修改失败");
		}
	}
	
	/**
	 * 查询所有的父菜单
	 */
	@GetMapping("/parent/{type}")
	public List<Permission> listParent(@PathVariable("type") int type){
		return menuService.getParentPermissions(type);
	}
	
	/**
	 * 添加菜单
	 */
	@PostMapping()
	public ResultMap add(Permission permission){
		if(menuService.addPermission(permission)){
			return ResultMap.ok("添加成功！");
		}else{
			return ResultMap.error("添加失败！");
		}
	}
	
	/**
	 * 修改菜单
	 */
	@PutMapping()
	public ResultMap update(Permission permission){
		if(menuService.updatePermission(permission)){
			return ResultMap.ok("修改成功！");
		}else{
			return ResultMap.error("修改失败！");
		}
	}
	
	/**
	 * 修改状态
	 */
	@PutMapping("status")
	public ResultMap updateStatus(String permissionId, int status) throws ParameterException {
		if(menuService.updatePermissionStatus(permissionId, status)){
			return ResultMap.ok();
		}
		return ResultMap.error();
	}
	
	/**
	 * 删除
	 */
	@DeleteMapping("/{permissionId}")
	public ResultMap delete(@PathVariable("permissionId") String permissionId) throws BusinessException {
		if(menuService.deletePermission(permissionId)){
			return ResultMap.ok("删除成功");
		}
		return ResultMap.error("删除失败");
	}
	
}
