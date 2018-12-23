package com.kingdee.uranus.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kingdee.uranus.core.PageResult;
import com.kingdee.uranus.service.HomeListService;
import com.kingdee.uranus.util.DateUtil;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年8月21日 下午12:30:38
 * @version
 */
@Controller
@RequestMapping("/api/")
public class HomeListController {

	@Resource
	private HomeListService homeListService;

	@RequestMapping("getOneWeakCloser")
	@ResponseBody
	public PageResult<Map<String, Object>> getOneWeakCloser() {
		Date startDate = com.kingdee.uranus.util.DateUtil.getBeginDayOfWeek();
		Date endDate = com.kingdee.uranus.util.DateUtil.getEndDayOfWeek();
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		List<Map<String, Object>> closerList = homeListService.getCloserList(startDate, endDate);
		pageResult.setCode(0);
		pageResult.setCount(closerList.size());
		pageResult.setData(closerList);
		return pageResult;
	}

	@RequestMapping("getOneMonthCloser")
	@ResponseBody
	public PageResult<Map<String, Object>> getOneMonthCloser() {
		Date startDate = com.kingdee.uranus.util.DateUtil.getBeginDayOfMonth();
		Date endDate = com.kingdee.uranus.util.DateUtil.getEndDayOfMonth();
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		List<Map<String, Object>> closerList = homeListService.getCloserList(startDate, endDate);
		pageResult.setCode(0);
		pageResult.setCount(closerList.size());
		pageResult.setData(closerList);
		return pageResult;
	}

	@RequestMapping("getTodayCloser")
	@ResponseBody
	public PageResult<Map<String, Object>> getTodayCloser() throws ParseException {
		Date startDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		startDate = sdf.parse(sdf.format(startDate));
		Date endDate = DateUtil.getNextDate(new Date());
		PageResult<Map<String, Object>> pageResult = new PageResult<Map<String, Object>>();
		List<Map<String, Object>> closerList = homeListService.getCloserList(startDate, endDate);
		pageResult.setCode(0);
		pageResult.setCount(closerList.size());
		pageResult.setData(closerList);
		return pageResult;
	}
}
