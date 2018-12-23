package com.kingdee.uranus.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdee.uranus.constant.AuthConstant;
import com.kingdee.uranus.core.ResultMap;
import com.kingdee.uranus.core.utils.DesUtil;
import com.kingdee.uranus.core.utils.Md5Util;
import com.kingdee.uranus.core.utils.UserAgentGetter;
import com.kingdee.uranus.model.LoginRecord;
import com.kingdee.uranus.model.User;
import com.kingdee.uranus.service.LoginRecordService;
import com.kingdee.uranus.service.PermissionService;
import com.kingdee.uranus.service.UserService;

/**
 * 登录控制器
 * 
 * @author wangfan
 * @date 2017-3-24 下午3:56:37
 */
@RestController
@RequestMapping("/api")
public class LoginController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private LoginRecordService loginRecordService;

	@Autowired
	private PermissionService permissionService;

	/**
	 * 登录
	 */
	@PostMapping("/login")
	public ResultMap login(String account, String password, String vercode, String verkey, HttpServletRequest request,
			HttpServletResponse response) {

		User loginUser = userService.getUserByDmpUserAccount(account);

		if (loginUser == null) {
			// 根据用户账号查询
			List<User> users = userService.getUserByAccount(account);
			if (users == null || users.size() == 0) {
				return ResultMap.error("账号不存在");
			} else if (users.size() == 1) {
				loginUser = users.get(0);
			} else if (users.size() > 1) {
				return ResultMap.error("存在相同的账号，请使用DMP账号登录");
			}
		}
		if (loginUser.getUserStatus() != 0) {
			return ResultMap.error("账号被锁定");
		}
		String encryPsw = Md5Util.MD5(password);
		if (!loginUser.getUserPassword().equals(encryPsw)) {
			return ResultMap.error("密码错误");
		}
		// 添加到登录日志
		addLoginRecord(request, loginUser.getUserId());

		request.getSession().setAttribute(AuthConstant.USER, loginUser);
		String userId = null;
		try {
			userId = DesUtil.encrypt(loginUser.getUserId().getBytes(), DesUtil.KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 放入cookies
		Cookie cookie = new Cookie(AuthConstant.USER_ID, userId);
		cookie.setPath("/");
		response.addCookie(cookie);

		// String token =
		// SubjectUtil.getInstance().createToken(loginUser.getUserId(),
		// DateUtil.getAppointDate(new Date(), 30));
		loginUser.setUserPassword(null);
		return ResultMap.ok("登录成功").put(AuthConstant.USER, loginUser).put("token", "");
	}

	/**
	 * 获取用户的菜单
	 */
	@GetMapping("/menu")
	public ResultMap navMenu(HttpServletRequest request) {
		return ResultMap.ok().put("menus", permissionService.getMenusByUser(getUserId(request)));
	}

	/**
	 * 退出登录
	 */
	@DeleteMapping("/login")
	public ResultMap loginOut(HttpServletRequest request) {
		// SubjectUtil.getInstance().expireToken(getUserId(request),
		// getToken(request));
		request.getSession().removeAttribute(AuthConstant.USER);
		return ResultMap.ok();
	}

	/**
	 * 添加登录日志
	 */
	private void addLoginRecord(HttpServletRequest request, String userId) {
		UserAgentGetter agentGetter = new UserAgentGetter(request);
		// 添加到登录日志
		LoginRecord loginRecord = new LoginRecord();
		loginRecord.setUserId(userId);
		loginRecord.setIpAddress(agentGetter.getIpAddr());
		loginRecord.setDevice(agentGetter.getDevice());
		loginRecord.setBrowserType(agentGetter.getBrowser());
		loginRecord.setOsName(agentGetter.getOS());
		loginRecordService.addLoginRecord(loginRecord);
	}

}