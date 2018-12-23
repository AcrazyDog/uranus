package com.kingdee.uranus.search.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.kingdee.uranus.search.DocumentSearch;
import com.kingdee.uranus.search.SearchClient;
import com.kingdee.uranus.search.param.SearchParam;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年7月5日 下午8:31:20
 * @version
 */
@Service
public class DocumentSearchImpl implements DocumentSearch {

	private static Logger logger = LoggerFactory.getLogger(DocumentSearchImpl.class);

	@Autowired
	private SearchClient elasticSearchClient;

	@Override
	public SearchResponse searchDocument(SearchParam searchParam) {
		TransportClient client = null;
		SearchResponse searchResponse = null;
		try {
			client = elasticSearchClient.getClient();

			HighlightBuilder hiBuilder = new HighlightBuilder();
			hiBuilder.preTags("<font style='color:#c00'>");
			hiBuilder.postTags("</font>");

			// 设置高亮字段
			if (!CollectionUtils.isEmpty(searchParam.getHighLightFields())) {
				searchParam.getHighLightFields().forEach(v -> {
					hiBuilder.field(v);
				});
			}

			SearchRequestBuilder searchRequestBuilder = client.prepareSearch(searchParam.getIndex())
					.setSearchType(SearchType.QUERY_THEN_FETCH).addSort(SortBuilders.scoreSort().order(SortOrder.DESC));
			String[] fileds = {};
			searchRequestBuilder.setQuery(QueryBuilders.multiMatchQuery(searchParam.getQueryStr(),
					searchParam.getQueryFileds().toArray(fileds)));

			searchResponse = searchRequestBuilder.highlighter(hiBuilder).setSize(searchParam.getSize())
					.setFrom(searchParam.getFrom()).get();

		} catch (Exception e) {
			logger.error("查询异常，条件是：{}，原因：{}", JSONObject.toJSONString(searchParam), e);
		} finally {
			elasticSearchClient.close(client);
		}

		return searchResponse;
	}

	@Override
	public void addDocument(String index, Object obj) {
		TransportClient client = null;
		try {
			client = elasticSearchClient.getClient();
			IndexResponse response = client.prepareIndex(index, obj.getClass().getName())
					.setSource(JSONObject.toJSONString(obj), XContentType.JSON).execute().actionGet();
			logger.info("增加的索引id:" + response.getId());
		} catch (Exception e) {
			logger.error("创建索引失败，索引：" + index + "，对象：{}，原因：{}", JSONObject.toJSONString(obj), e);
		} finally {
			elasticSearchClient.close(client);
		}

	}

	@Override
	public <T> void updateDocument(String index, T obj, Map<String, Object> queryParams) {
		TransportClient client = null;
		try {
			client = elasticSearchClient.getClient();
			MatchQueryBuilder matchQuery = null;
			for (Entry<String, Object> entry : queryParams.entrySet()) {
				matchQuery = QueryBuilders.matchQuery(entry.getKey(), entry.getValue());
			}

			SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setQuery(matchQuery);
			SearchResponse searchResponse = searchRequestBuilder.get();

			SearchHit[] hits = searchResponse.getHits().getHits();

			if (hits != null && hits.length > 0) {

				String id = hits[0].getId();
				logger.info("修改的索引id:" + id);
				// 更新
				UpdateResponse updateResponse = client.prepareUpdate(index, obj.getClass().getName(), id)
						.setDoc(JSONObject.toJSONString(obj), XContentType.JSON).execute().get();
				updateResponse.getGetResult();
			} else {
				logger.info("索引库：{}未查询到对象，添加对象{}到索引库", index, JSONObject.toJSONString(obj));
				this.addDocument(index, obj);
			}
		} catch (Exception e) {
			logger.error("修改索引失败,索引：" + index + ",查询参数：{},原因：{}", JSONObject.toJSONString(queryParams), e);
		} finally {
			elasticSearchClient.close(client);
		}

	}

}
