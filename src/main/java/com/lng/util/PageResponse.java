package com.lng.util;

/**
 * 分页返回参数定义
 * @author Administrator
 *
 */
public class PageResponse {

	private Integer limit;//每页记录条数
	
	private Integer page;//当前页数
	
	private Long count;//返回数据总条数
	
    private int code;//状态码
    
    private String message;//提示信息
    
    private Object datas;//业务数据

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getDatas() {
		return datas;
	}

	public void setDatas(Object datas) {
		this.datas = datas;
	}

	public PageResponse(Integer limit, Integer page, Long count, int code, String message, Object datas) {
		this.limit = limit;
		this.page = page;
		this.count = count;
		this.code = code;
		this.message = message;
		this.datas = datas;
	}
    
    
}
