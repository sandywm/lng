package com.lng.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "common_province_order", catalog = "lng")
@ApiModel("省份排序规则--CommonProvinceOrder")
public class CommonProvinceOrder implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	@ApiModelProperty(value = "省份")
	private String province;
	@ApiModelProperty(value = "排序")
	private Integer orderNo;
	
	public CommonProvinceOrder() {
		
	}
	
	public CommonProvinceOrder(String province, Integer orderNo) {
		this.province = province;
		this.orderNo = orderNo;
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "province", nullable = false, columnDefinition = "varchar(100) COMMENT '省份'")
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	@Column(name = "order_no", nullable = false, columnDefinition = "int(11) COMMENT '点击率'")
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
