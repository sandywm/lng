package com.lng.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "wq_pfbz", catalog = "lng")
@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@ApiModel("尾气排放标准--WqPfbz")
public class WqPfbz implements java.io.Serializable {
	private String id;
	@ApiModelProperty(value = "尾气排放类型")
	private String name;

	public WqPfbz() {
	}

	public WqPfbz(String id, String name) {
		this.id = id;
		this.name = name;
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

	@Column(name = "name", nullable = false, columnDefinition = "varchar(50) COMMENT '尾气排放类型'")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
