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
 * TrucksTradeQualification generated by hbm2java
 */
@Entity
@Table(name = "trucks_trade_qualification", catalog = "lng")
@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@ApiModel("槽车租卖进港资质关联--TrucksTradeQualification")
public class TrucksTradeQualification implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	@ApiModelProperty(value = "槽车租卖编号")
	private Qualification qualification;
	@ApiModelProperty(value = "进港资质编号")
	private TrucksTrade trucksTrade;

	public TrucksTradeQualification() {
	}

	public TrucksTradeQualification(Qualification qualification, TrucksTrade trucksTrade) {
		this.qualification = qualification;
		this.trucksTrade = trucksTrade;
	}

	@Id
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", unique = true, nullable = false, length = 100)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)//等同于lazy="false"
	@JoinColumn(name = "qualification_id", nullable = false,columnDefinition = "varchar(100) COMMENT '槽车租卖编号'" )
	public Qualification getQualification() {
		return this.qualification;
	}

	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}

	@ManyToOne(fetch = FetchType.EAGER)//等同于lazy="false"
	@JoinColumn(name = "trucks_trade_id", nullable = false,columnDefinition = "varchar(100) COMMENT '进港资质编号'")
	public TrucksTrade getTrucksTrade() {
		return this.trucksTrade;
	}

	public void setTrucksTrade(TrucksTrade trucksTrade) {
		this.trucksTrade = trucksTrade;
	}

}
