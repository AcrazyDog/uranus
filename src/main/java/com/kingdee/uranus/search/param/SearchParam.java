package com.kingdee.uranus.search.param;

import java.util.List;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年7月6日 下午6:41:44
 * @version
 */
public class SearchParam {

	private String queryStr;

	private List<String> highLightFields;

	private List<String> queryFileds;

	private int size;

	private int from;

	private String index;

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public List<String> getHighLightFields() {
		return highLightFields;
	}

	public void setHighLightFields(List<String> highLightFields) {
		this.highLightFields = highLightFields;
	}

	public List<String> getQueryFileds() {
		return queryFileds;
	}

	public void setQueryFileds(List<String> queryFileds) {
		this.queryFileds = queryFileds;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

	public int getSize() {
		return size == 0 ? 10 : size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

}
