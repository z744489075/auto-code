package com.zengtengpeng.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.io.Serializable;
import java.util.List;

/**
 * 分页控件 DTO
 * 
 * @author zengtp
 * 
 */
public class Page implements Serializable {

	@ApiModelProperty(hidden = true)
	private Integer total;// 总条数

	@ApiModelProperty(hidden = true)
	private List rows; // 返回页面的数据

	private Integer page = 1;

	private Integer pageSize = 10;



	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private String startDate;//开始时间


	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private String endDate;//结束时间


	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private String orderByString;//排序


	public String getOrderByString() {
		return orderByString;
	}

	public void setOrderByString(String orderByString) {
		this.orderByString = orderByString;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
