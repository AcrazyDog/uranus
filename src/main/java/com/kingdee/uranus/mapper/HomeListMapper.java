package com.kingdee.uranus.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年8月21日 下午12:35:53
 * @version
 */
public interface HomeListMapper {

	List<Map<String, Integer>> selectCloserList(Date startDate, Date endDate);

}
