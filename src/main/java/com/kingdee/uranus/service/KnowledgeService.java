package com.kingdee.uranus.service;

import com.kingdee.uranus.controller.param.KnowledgeSearchParam;
import com.kingdee.uranus.core.PageBean;
import com.kingdee.uranus.model.Knowledge;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月19日 下午5:30:32
 * @version
 */
public interface KnowledgeService {

	public Long save(Knowledge knowledge);

	public void update(Knowledge knowledge);

	public PageBean<Knowledge> getKnowledgeList(KnowledgeSearchParam param);

	public Knowledge getBugDetail(Long id);

	public void deleteKnowledge(Long id);
}
