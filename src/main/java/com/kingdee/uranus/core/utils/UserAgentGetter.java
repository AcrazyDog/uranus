package com.kingdee.uranus.core.utils;

import javax.servlet.http.HttpServletRequest;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 获取客户端设备信息
 * 
 * @author wangfan
 * @date 2017-7-24 下午3:47:39
 */
public class UserAgentGetter {
	private UserAgent userAgent;
	private String userAgentString;
	private HttpServletRequest request;

	public UserAgentGetter(HttpServletRequest request) {
		this.request = request;
		userAgentString = request.getHeader("User-Agent");
		userAgent = UserAgent.parseUserAgentString(userAgentString);
	}

	/**
	 * 获取浏览器类型
	 */
	public String getBrowser() {
		if (null == userAgent) {
			return "";
		}
		return userAgent.getBrowser().getName();
	}

	/**
	 * 获取操作系统
	 */
	public String getOS() {
		if (null == userAgent) {
			return "未知设备";
		}
		return userAgent.getOperatingSystem().getName();
	}

	/**
	 * 获取设备型号
	 */
	public String getDevice() {
		if (null == userAgentString) {
			return "未知设备";
		}
		if (userAgentString.contains("Android")) {
			String[] str = userAgentString.split("[()]+");
			str = str[1].split("[;]");
			String[] res = str[str.length - 1].split("Build/");
			return res[0].trim();
		} else if (userAgentString.contains("iPhone")) {
			String[] str = userAgentString.split("[()]+");
			String res = "iphone" + str[1].split("OS")[1].split("like")[0];
			return res.trim();
		} else if (userAgentString.contains("iPad")) {
			return "iPad";
		} else {
			return getOS().trim();
		}
	}

	/**
	 * 获取ip地址
	 */
	public String getIpAddr() {
		String ip = null;
		try {
			ip = request.getHeader("x-forwarded-for");
			if (isBlankIp(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (isBlankIp(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (isBlankIp(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (isBlankIp(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (isBlankIp(ip)) {
				ip = request.getRemoteAddr();
			}
			// 使用代理，则获取第一个IP地址
			if (!isBlankIp(ip) && ip.length() > 15) {
				ip = ip.split(",")[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	private boolean isBlankIp(String str) {
		return str == null || str.trim().isEmpty() || "unknown".equalsIgnoreCase(str);
	}
}
