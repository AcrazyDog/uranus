package com.kingdee.uranus.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年3月30日 下午3:25:23
 * @version
 */
@RestController
@RequestMapping("api/check/sql")
public class CheckSqlController {

	@RequestMapping("checkSql")
	public Object checkSql(String sql) {
//		try {
//			TransUtil.Translate(sql, DbType.MySQL);
//			return ResultMap.ok();
//		} catch (kd.bos.ksql.ParserException e) {
//			return ResultMap.error("SQL错误：").put("msg", e.getMessage());
//		} catch (SqlTranslateException e) {
//			return ResultMap.error("SQL错误：").put("msg", e.getMessage());
//		}
		return null;

	}
}
