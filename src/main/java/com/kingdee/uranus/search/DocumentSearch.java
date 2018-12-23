package com.kingdee.uranus.search;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;

import com.kingdee.uranus.search.param.SearchParam;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年7月5日 下午8:27:58
 * @version
 */
public interface DocumentSearch {

	public SearchResponse searchDocument(SearchParam searchParam);

	public void addDocument(String index, Object obj);

	public <T> void updateDocument(String index, T t, Map<String, Object> queryParams);

}
