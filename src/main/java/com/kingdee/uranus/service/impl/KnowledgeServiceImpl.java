package com.kingdee.uranus.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.kingdee.uranus.constant.ConfigConstant;
import com.kingdee.uranus.controller.param.KnowledgeSearchParam;
import com.kingdee.uranus.core.PageBean;
import com.kingdee.uranus.core.PaginationContext;
import com.kingdee.uranus.mapper.KnowledgeMapper;
import com.kingdee.uranus.model.Knowledge;
import com.kingdee.uranus.search.DocumentSearch;
import com.kingdee.uranus.service.KnowledgeService;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月19日 下午5:31:24
 * @version
 */
@Service
public class KnowledgeServiceImpl implements KnowledgeService {

	@Resource
	private KnowledgeMapper knowledgeMapper;

	@Resource
	private DocumentSearch documentSearch;

	@Override
	public Long save(Knowledge knowledge) {
		knowledgeMapper.insert(knowledge);

		// 增加到索引
		documentSearch.addDocument(ConfigConstant.ELASTIC_KNOWLEDGE_INDEX, knowledge);
		return knowledge.getId();
	}

	@Override
	public void update(Knowledge knowledge) {
		knowledgeMapper.updateByPrimaryKeySelective(knowledge);

		// 修改索引
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", knowledge.getId());
		documentSearch.updateDocument(ConfigConstant.ELASTIC_KNOWLEDGE_INDEX, knowledge, params);

	}

	@Override
	public PageBean<Knowledge> getKnowledgeList(KnowledgeSearchParam param) {
		PageHelper.startPage(PaginationContext.getPageNum(), PaginationContext.getPageSize());
		List<Knowledge> list = knowledgeMapper.selectKnowledgeList(param);
		return new PageBean<Knowledge>(list);
	}

	@Override
	public Knowledge getBugDetail(Long id) {
		return knowledgeMapper.selectByPrimaryKey(id);
	}

	@Override
	public void deleteKnowledge(Long id) {
		knowledgeMapper.deleteByPrimaryKey(id);
	}

}
