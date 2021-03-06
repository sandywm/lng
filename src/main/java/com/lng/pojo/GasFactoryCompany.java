package com.lng.pojo;
// Generated 2019-12-2 10:34:35 by Hibernate Tools 5.0.6.Final

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

/**
 * GasFactoryCompany generated by hbm2java
 */
@Entity
@Table(name = "gas_factory_company", catalog = "lng")
@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@ApiModel("液厂贸易商关联--GasFactoryCompany")
public class GasFactoryCompany implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	@ApiModelProperty(value = "贸易商公司编号")
	private Company company;
	@ApiModelProperty(value = "液厂编号")
	private GasFactory gasFactory;
	@ApiModelProperty(value = "申请人")
	private String addUserId;
	@ApiModelProperty(value = "添加时间")
	private String addTime;
	@ApiModelProperty(value = "审核状态")
	private int checkStatus;
	@ApiModelProperty(value = "审核时间")
	private String checkTime;

	public GasFactoryCompany() {
	}

	public GasFactoryCompany(Company company, GasFactory gasFactory, String addUserId,String addTime,int checkStatus,String checkTime) {
		this.company = company;
		this.gasFactory = gasFactory;
		this.addUserId = addUserId;
		this.addTime = addTime;
		this.checkStatus = checkStatus;
		this.checkTime = checkTime;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", unique = true, nullable = false,length = 100)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)//等同于lazy="false"
	@JoinColumn(name = "company_id", nullable = false, columnDefinition = "varchar(100) COMMENT '公司编号'")
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.EAGER)//等同于lazy="false"
	@JoinColumn(name = "gas_factory_id", nullable = false, columnDefinition = "varchar(100) COMMENT '液厂编号'")
	public GasFactory getGasFactory() {
		return this.gasFactory;
	}

	public void setGasFactory(GasFactory gasFactory) {
		this.gasFactory = gasFactory;
	}

	@Column(name = "add_user_id", columnDefinition = "varchar(100) COMMENT '申请人'")
	public String getAddUserId() {
		return addUserId;
	}

	public void setAddUserId(String addUserId) {
		this.addUserId = addUserId;
	}

	@Column(name = "add_time", columnDefinition = "varchar(50) COMMENT '添加时间'")
	public String getAddTime() {
		return this.addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	@Column(name = "check_status", nullable = false, columnDefinition = "int(11) COMMENT '审核状态(0:未审核,1:审核通过,2:审核未通过)'")
	public int getCheckStatus() {
		return this.checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Column(name = "check_time", columnDefinition = "varchar(50) COMMENT '审核时间'")
	public String getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
}
