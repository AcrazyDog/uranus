package com.kingdee.uranus.mapper;

import java.util.List;

import com.kingdee.uranus.controller.param.KnowledgeSearchParam;
import com.kingdee.uranus.model.Knowledge;

public interface KnowledgeMapper {

	int deleteByPrimaryKey(Long id);

	int insert(Knowledge record);

	int insertSelective(Knowledge record);

	Knowledge selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Knowledge record);

	int updateByPrimaryKeyWithBLOBs(Knowledge record);

	int updateByPrimaryKey(Knowledge record);

	List<Knowledge> selectKnowledgeList(KnowledgeSearchParam param);
}