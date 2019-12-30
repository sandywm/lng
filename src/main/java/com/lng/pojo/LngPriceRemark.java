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
@Table(name = "lng_price_remark", catalog = "lng")
@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@ApiModel("燃气价格详情--LngPriceRemark")
public class LngPriceRemark  implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	@ApiModelProperty(value = "液厂编号")
	private GasFactory gf;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "添加时间")
	private String addTime;
	
	public LngPriceRemark() {
		
	}
	
	public LngPriceRemark(GasFactory gf, int price, String priceTime, String remark,
			String addTime) {
		this.gf = gf;
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
	@JoinColumn(name = "gf_id", nullable = false, columnDefinition = "varchar(100) COMMENT '液厂编号'")
	public GasFactory getGf() {
		return gf;
	}
	
	public void setGf(GasFactory gf) {
		this.gf = gf;
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
