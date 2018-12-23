/**
 * Copyright (c) 2006-2015 Kingdee Ltd. All Rights Reserved. 
 *  
 * This code is the confidential and proprietary information of   
 * Kingdee. You shall not disclose such Confidential Information   
 * and shall use it only in accordance with the terms of the agreements   
 * you entered into with Kingdee,http://www.Kingdee.com.
 *  
 */
package com.kingdee.uranus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * 
 * </p>
 * 
 * @author Administrator
 * @date 2018年3月26日 上午10:23:58
 * @version
 */
@Controller
public class IndexController {

	@GetMapping("/system/buglist")
	public String buglist() {
		return "/system/buglist";
	}
}
