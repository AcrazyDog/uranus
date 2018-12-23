package com.kingdee.uranus.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年8月21日 下午12:33:46
 * @version
 */
public interface HomeListService {

	public List<Map<String, Object>> getCloserList(Date startDate, Date endDate);
}
