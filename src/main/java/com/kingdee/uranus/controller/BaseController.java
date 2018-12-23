package com.kingdee.uranus.controller;

import javax.servlet.http.HttpServletRequest;

import com.kingdee.uranus.constant.AuthConstant;
import com.kingdee.uranus.model.User;

/**
 * Controller基类
 * 
 * @author wangfan
 * @date 2017-6-10 上午10:38:16
 */
public class BaseController {

	/**
	 * 获取登录的User
	 */
	public String getUserId(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(AuthConstant.USER);
		return user.getUserId();
	}

	/**
	 * 获取登录的User
	 */
	public User getUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(AuthConstant.USER);
		return user;
	}

	/**
	 * 获取当前请求的token
	 */
	public String getToken(HttpServletRequest request) {
		String token = request.getHeader("token");
		if (token == null) {
			token = request.getParameter("token");
		}
		return token;
	}

}
