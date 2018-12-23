package com.kingdee.uranus.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kingdee.uranus.core.ResultMap;

/**
 * 测试RESTful接口
 * 
 * @author wangfan
 * @date 2017-7-14 下午4:39:51
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

	@GetMapping
	public ResultMap list(HttpServletRequest request) {
		return ResultMap.ok();
	}

	/**
	 * 根据id获取资源
	 */
	@GetMapping("{id}")
	public ResultMap getById(@PathVariable("id") String id) {
		return ResultMap.ok().put("id", id);
	}
}
