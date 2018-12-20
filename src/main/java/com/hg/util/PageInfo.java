package com.hg.util;


/**
 * 
 * <p>Title: PageRequest</p>
 * <p>Description: 接受前端传来的分页信息</p>
 * <p>Company: 骄语软件</p> 
 * @author 陈明星
 * @date 下午10:06:11
 */
public class PageInfo {
	
	
	private Integer rows;//每页显示条数
	
	private Integer page;//当前页数
	
	private String sort;//排序字段
	
	private String order;//排序方式，desc，asc

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
