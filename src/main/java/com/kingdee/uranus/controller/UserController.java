package com.kingdee.uranus.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

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
import com.kingdee.uranus.core.utils.Md5Util;
import com.kingdee.uranus.model.User;
import com.kingdee.uranus.service.UserService;

/**
 * 用户管理
 * 
 * @author wangfan
 * @date 2017-3-24 下午3:56:37
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	/**
	 * 查询所有用户
	 */
	@GetMapping()
	public PageResult<User> list(Integer page, Integer limit, Integer status, String searchKey, String searchValue) throws UnsupportedEncodingException {
		if (searchValue != null) {
			searchValue = new String(searchValue.getBytes("ISO-8859-1"), "UTF-8");
		}
		if (page == null) {
			page = 0;
			limit = 0;
		}
		return userService.getUsers(page, limit, status, searchKey, searchValue);
	}

	/**
	 * 添加用户
	 */
	@PostMapping()
	public ResultMap add(User user) throws BusinessException {
		if (userService.addUser(user)) {
			return ResultMap.ok("添加成功");
		} else {
			return ResultMap.error("添加失败，请重试");
		}
	}

	/**
	 * 修改用户
	 */
	@PutMapping()
	public ResultMap update(User user) {
		if (userService.updateUser(user)) {
			// SubjectUtil.getInstance().updateCacheRoles(user.getUserId());
			return ResultMap.ok("修改成功");
		} else {
			return ResultMap.error("修改失败");
		}
	}

	/**
	 * 修改用户状态
	 */
	@PutMapping("status")
	public ResultMap updateStatus(String userId, int status) throws ParameterException {
		if (userService.updateUserStatus(userId, status)) {
			// SubjectUtil.getInstance().expireToken(userId);
			return ResultMap.ok();
		} else {
			return ResultMap.error();
		}
	}

	/**
	 * 修改自己密码
	 */
	@PutMapping("psw")
	public ResultMap updatePsw(String oldPsw, String newPsw, HttpServletRequest request) {
		String userId = getUserId(request);
		String encryPsw = Md5Util.MD5(oldPsw);
		User tempUser = userService.getUserById(userId);
		if (tempUser == null || !encryPsw.equals(tempUser.getUserPassword())) {
			return ResultMap.error("旧密码输入不正确");
		}
		if (userService.updateUserPsw(userId, newPsw)) {
			// SubjectUtil.getInstance().expireToken(userId);
			return ResultMap.ok();
		} else {
			return ResultMap.error();
		}
	}

	/**
	 * 删除用户
	 */
	@DeleteMapping("/{userId}")
	public ResultMap delete(@PathVariable("userId") String userId) throws BusinessException {
		if (userService.deleteUser(userId)) {
			// SubjectUtil.getInstance().expireToken(userId);
			return ResultMap.ok("删除成功");
		} else {
			return ResultMap.error("删除失败");
		}
	}

	/**
	 * 重置密码
	 */
	// @RequiresRoles("admin")
	@PutMapping("psw/{userId}")
	public ResultMap resetPsw(@PathVariable("userId") String userId, HttpServletRequest request) {
		if (userService.updateUserPsw(userId, "123456")) {
			// SubjectUtil.getInstance().expireToken(userId);
			return ResultMap.ok();
		} else {
			return ResultMap.error();
		}
	}
}