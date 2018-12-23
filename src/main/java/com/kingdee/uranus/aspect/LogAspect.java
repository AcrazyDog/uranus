package com.kingdee.uranus.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.kingdee.uranus.service.LogService;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年5月10日 下午5:25:22
 * @version
 */
@Aspect
@Component
public class LogAspect {

	private LogService service;

	@After(value = "execution(* com.kingdee.close.bug.controller.*.*(..))")
	public void after(JoinPoint joinPoint) {

		// 这样实现不好。

		/*
		 * String methodName = joinPoint.getSignature().getName(); Object[]
		 * objs= joinPoint.getArgs(); HttpServletRequest req =
		 * (HttpServletRequest)objs[0]; Log log = new Log(); if
		 * ("closeBug".equals(methodName)) { log.setLogType(LogType.CLOSE);
		 * String bugNo = objs[1].toString(); log.setBugNo(bugNo); String
		 * fOpinion = objs[2].toString(); log.setReason(fOpinion); }else
		 * if("fowardBug".equals(methodName)){ log.setLogType(LogType.FORWARD);
		 * String bugNo = objs[1].toString(); log.setBugNo(bugNo);
		 * log.setReason("转让BUG"); }else if("closeManyBugs".equals(methodName)){
		 * log.setLogType(LogType.CLOSE); String bugNo = objs[1].toString();
		 * log.setBugNo(bugNo); String fOpinion = objs[2].toString();
		 * log.setReason(fOpinion); } joinPoint.getArgs();
		 * 
		 * System.out.println("最终通知....");
		 */
	}
}
