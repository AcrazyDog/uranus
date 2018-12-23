package com.kingdee.uranus.util;

import javax.servlet.http.HttpServletRequest;

import com.kingdee.uranus.constant.AuthConstant;
import com.kingdee.uranus.model.User;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月19日 下午4:43:58
 * @version
 */
public class SessionUtil {

	public static User getUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(AuthConstant.USER);
		return user;
	}
}
