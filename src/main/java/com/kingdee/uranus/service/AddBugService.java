package com.kingdee.uranus.service;

import java.util.List;
import java.util.Map;

import com.kingdee.uranus.core.exception.BusinessException;
import com.kingdee.uranus.domain.AddBugParam;
import com.kingdee.uranus.domain.ProductTaskDomain;
import com.kingdee.uranus.domain.SubSystem;

/**
 * 增加BUG的相关服务，不写到BugService中，防止类太长
 * 
 * @author rd_kang_nie
 *
 */
public interface AddBugService {

	public List<Map<String, Object>> getStageByGroupId(String groupId);

	public List<Map<String, Object>> getRelateProjectTask(SubSystem subSystem);

	public void addBug(AddBugParam param, String userName) throws BusinessException;

	public List<ProductTaskDomain> getAllProductTask();

	public List<SubSystem> getAllSubSystem();

	public String getBugCode(String userName);

}
