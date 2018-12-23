package com.kingdee.uranus.filter;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kingdee.uranus.constant.AuthConstant;
import com.kingdee.uranus.controller.BugController;
import com.kingdee.uranus.core.RequestContext;
import com.kingdee.uranus.core.utils.DesUtil;
import com.kingdee.uranus.model.User;
import com.kingdee.uranus.service.UserService;
import com.kingdee.uranus.util.SessionUtil;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年3月27日 下午5:31:13
 * @version
 */
@Component
@WebFilter(filterName = "loginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(BugController.class);

	private static final String[] PASS_URL = { ".jsp", "login", ".js", ".css", ".jpg", ".png", ".woff", ".ico",
			"/image/captcha", "file/MetaDataLockFilter.class", "file/web.xml" };

	@Value("${login.forward.url}")
	private String URL;

	@Resource
	private UserService userService;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		String url = servletRequest.getRequestURI();

		if (checkUrl(url)) {
			chain.doFilter(servletRequest, servletResponse);
		} else {
			User user = SessionUtil.getUser(servletRequest);
			if (user != null) {
				try {
					// 将username，userId放入 RequestContext
					RequestContext.set("userName", user.getUserNickname());
					RequestContext.set("dmpUserId", user.getDmpUserId());

					chain.doFilter(servletRequest, servletResponse);
				} finally {
					RequestContext.remove();
				}
			} else {

				String userId = null;
				// 从cookie中获得加密的userId
				Cookie[] cookies = servletRequest.getCookies();
				if (cookies == null) {
					servletResponse.sendRedirect(URL);
					return;
				}
				for (Cookie cookie : cookies) {
					if (cookie != null && AuthConstant.USER_ID.equals(cookie.getName())) {
						userId = cookie.getValue();
						break;
					}
				}

				if (userId == null) {
					servletResponse.sendRedirect(URL);
					return;
				} else {
					// 去数据库查询user
					try {
						userId = DesUtil.decrypt(userId, DesUtil.KEY);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					user = userService.getUserById(userId);

					if (user == null) {
						servletResponse.sendRedirect(URL);
						return;
					} else {
						try {
							servletRequest.getSession().setAttribute(AuthConstant.USER, user);
							// 将username，userId放入 RequestContext
							RequestContext.set("userName", user.getUserNickname());
							RequestContext.set("dmpUserId", user.getDmpUserId());

							chain.doFilter(servletRequest, servletResponse);
						} finally {
							RequestContext.remove();
						}
					}
				}
			}
		}
	}

	private boolean checkUrl(String requestURI) {
		boolean result = false;
		for (int i = 0; i < PASS_URL.length; i++) {
			if (requestURI.contains(PASS_URL[i])) {
				result = true;
			}
		}

		return result;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
