package com.hg.util;

import java.math.BigInteger;
import java.util.List;


/**
 * 
 * <p>Title: PageResponse</p>
 * <p>Description: 返回 datagrid json 对象</p>
 * <p>Company: 骄语软件</p> 
 * @author 陈明星
 * @date 下午10:05:03
 */
public class PageResponse {

	private Object rows;//表格list数据
	
	
	private Object total;//总条数
	
	
	public Object getTotal() {
		return total;
	}

	public void setTotal(Object total) {
		this.total = total;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}
}
