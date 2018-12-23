package com.kingdee.uranus.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdee.uranus.core.PageBean;
import com.kingdee.uranus.core.utils.StringUtil;
import com.kingdee.uranus.model.Log;
import com.kingdee.uranus.service.LogService;
import com.kingdee.uranus.util.SessionUtil;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月11日 下午3:39:40
 * @version
 */
@RestController
@RequestMapping("/api/log")
public class LogController {

	@Resource
	private LogService logService;

	/**
	 * 查询所有登录日志
	 * 
	 * @return
	 */
	@GetMapping()
	public PageBean<Log> list(HttpServletRequest request, String startDate, String endDate) {
		String userName = SessionUtil.getUser(request).getUserNickname();

		if ("聂康".equals(userName)) {
			userName = null;
		}

		if (StringUtil.isBlank(startDate)) {
			startDate = null;
		} else {
			startDate += " 00:00:00";
		}
		if (StringUtil.isBlank(endDate)) {
			endDate = null;
		} else {
			endDate += " 23:59:59";
		}
		return logService.getLogList(startDate, endDate, userName);
	}
}
