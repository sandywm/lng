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
 * ModuleAct generated by hbm2java
 */
@Entity
@Table(name = "module_act", catalog = "lng")
@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@ApiModel("模块动作--ModuleAct")
public class ModuleAct implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	@ApiModelProperty(value = "模块编号")
	private Module module;
	@ApiModelProperty(value = "动作名称中文")
	private String actNameChi;
	@ApiModelProperty(value = "动作名称英文")
	private String actNameEng;
	@ApiModelProperty(value = "动作排序")
	private int actOrder;

	public ModuleAct() {
	}

	public ModuleAct(Module module, String actNameChi, String actNameEng, int actOrder) {
		this.module = module;
		this.actNameChi = actNameChi;
		this.actNameEng = actNameEng;
		this.actOrder = actOrder;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mod_id", nullable = false, columnDefinition = "varchar(100) COMMENT '模块编号'")
	public Module getModule() {
		return this.module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	@Column(name = "act_name_chi", nullable = false, columnDefinition = "varchar(30) COMMENT '动作名称中文'")
	public String getActNameChi() {
		return this.actNameChi;
	}

	public void setActNameChi(String actNameChi) {
		this.actNameChi = actNameChi;
	}

	@Column(name = "act_name_eng", nullable = false, columnDefinition = "varchar(30) COMMENT '动作名称英文'")
	public String getActNameEng() {
		return this.actNameEng;
	}

	public void setActNameEng(String actNameEng) {
		this.actNameEng = actNameEng;
	}

	@Column(name = "act_order", nullable = false, columnDefinition = "int(11) COMMENT '动作排序(每个模块下从1开始)'")
	public int getActOrder() {
		return this.actOrder;
	}

	public void setActOrder(int actOrder) {
		this.actOrder = actOrder;
	}

}
