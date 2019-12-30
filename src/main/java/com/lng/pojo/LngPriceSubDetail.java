package com.lng.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "lng_price_sub_detail", catalog = "lng")
@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@ApiModel("燃气价格详情--LngPriceSubDetail")
public class LngPriceSubDetail  implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	@ApiModelProperty(value = "价格编号")
	private LngPriceDetail lpd;
	@ApiModelProperty(value = "价格")
	private int price;
	@ApiModelProperty(value = "价格时间")
	private String priceTime;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "添加时间")
	private String addTime;
	
	public LngPriceSubDetail() {
		
	}
	
	public LngPriceSubDetail(LngPriceDetail lpd, int price, String priceTime, String remark,
			String addTime) {
		this.lpd = lpd;
		this.price = price;
		this.priceTime = priceTime;
		this.remark = remark;
		this.addTime = addTime;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", unique = true, nullable = false, length = 100)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lpd_id", nullable = false, columnDefinition = "varchar(100) COMMENT '价格编号'")
	public LngPriceDetail getLpd() {
		return lpd;
	}

	public void setLpd(LngPriceDetail lpd) {
		this.lpd = lpd;
	}

	@Column(name = "price", nullable = false, columnDefinition = "int(11) COMMENT '价格'")
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Column(name = "price_time", nullable = false, columnDefinition = "varchar(50) COMMENT '价格时间'")
	public String getPriceTime() {
		return priceTime;
	}

	public void setPriceTime(String priceTime) {
		this.priceTime = priceTime;
	}

	@Column(name = "remark", columnDefinition = "varchar(100) COMMENT '备注'")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "add_time", nullable = false, columnDefinition = "varchar(50) COMMENT '添加时间'")
	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	
	
}
