package com.kingdee.uranus.core;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.Page;

/**
 * <p>
 * 
 * </p>
 * 
 * @author RD_kang_nie
 * @date 2018年4月19日 下午4:10:55
 * @version
 */
public class PageBean<T> implements Serializable {
	private static final long serialVersionUID = 8656597559014685635L;
	private long count; // 总记录数
	private List<T> data; // 结果集
	private int pageNum; // 第几页
	private int pageSize; // 每页记录数
	private int pages; // 总页数
	private int size; // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性

	private int code; // 状态码, 0表示成功

	private String msg; // 提示信息

	/**
	 * 包装Page对象，因为直接返回Page对象，在JSON处理以及其他情况下会被当成List来处理， 而出现一些问题。
	 * 
	 * @param list
	 *            page结果
	 * @param navigatePages
	 *            页码数量
	 */
	public PageBean(List<T> list) {
		if (list instanceof Page) {
			Page<T> page = (Page<T>) list;
			this.pageNum = page.getPageNum();
			this.pageSize = page.getPageSize();
			this.count = page.getTotal();
			this.pages = page.getPages();
			this.data = page;
			this.size = page.size();
			this.code = 0;
		}
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
