package com.yizhilu.entity;

import java.io.Serializable;

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-12 上午10:43:17
 * 类说明:
 */
public class PageEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private int totalResultSize;
	private int totalPageSize;
	private int pageSize;
	private int currentPage;
	private int startRow;
	private boolean first;
	private boolean last;
	public int getTotalResultSize() {
		return totalResultSize;
	}
	public void setTotalResultSize(int totalResultSize) {
		this.totalResultSize = totalResultSize;
	}
	public int getTotalPageSize() {
		return totalPageSize;
	}
	public void setTotalPageSize(int totalPageSize) {
		this.totalPageSize = totalPageSize;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getStartRow() {
		return startRow;
	}
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	public boolean isFirst() {
		return first;
	}
	public void setFirst(boolean first) {
		this.first = first;
	}
	public boolean isLast() {
		return last;
	}
	public void setLast(boolean last) {
		this.last = last;
	}
}
