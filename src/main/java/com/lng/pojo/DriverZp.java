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
 * DriverZp generated by hbm2java
 */
@Entity
@Table(name = "driver_zp", catalog = "lng")
@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@ApiModel("司机招聘--DriverZp")
public class DriverZp implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	@ApiModelProperty(value = "公司编号")
	private Company company;
	@ApiModelProperty(value = "驾照类型")
	private String jzType;
	@ApiModelProperty(value = "司机年龄范围")
	private String sjAgeRange;
	@ApiModelProperty(value = "司机驾龄范围")
	private String jlYearRange;
	@ApiModelProperty(value = "薪资")
	private String wage;
	@ApiModelProperty(value = "省")
	private String province;
	@ApiModelProperty(value = "市")
	private String city;
	@ApiModelProperty(value = "公司地址")
	private String address;
	@ApiModelProperty(value = "学历")
	private String education;
	@ApiModelProperty(value = "工作年限")
	private String workYear;
	@ApiModelProperty(value = "人数")
	private int num;
	@ApiModelProperty(value = "福利待遇")
	private String welfare;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "审核状态")
	private int checkStatus;
	@ApiModelProperty(value = "审核时间")
	private String checkTime;
	@ApiModelProperty(value = "上/下架状态")
	private int showStatus;
	@ApiModelProperty(value = "添加时间")
	private String addTime;
	@ApiModelProperty(value = "上传人员类型")
	private int userType;
	@ApiModelProperty(value = "上传人员编号")
	private String addUserId;
	@ApiModelProperty(value = "热度")
	private int hot;
	@ApiModelProperty(value = "联系人")
	private String lxName;
	@ApiModelProperty(value = "联系电话")
	private String lxTel;

	public DriverZp() {
	}

	

	public DriverZp(Company company, String jzType, String sjAgeRange, String jlYearRange, String wage, String province,
			String city, String address, String education, String workYear, int num, String welfare, String remark,
			int checkStatus, String checkTime, int showStatus, String addTime, int userType, String addUserId, int hot,
			String lxName, String lxTel) {
		this.company = company;
		this.jzType = jzType;
		this.sjAgeRange = sjAgeRange;
		this.jlYearRange = jlYearRange;
		this.wage = wage;
		this.province = province;
		this.city = city;
		this.address = address;
		this.education = education;
		this.workYear = workYear;
		this.num = num;
		this.welfare = welfare;
		this.remark = remark;
		this.checkStatus = checkStatus;
		this.checkTime = checkTime;
		this.showStatus = showStatus;
		this.addTime = addTime;
		this.userType = userType;
		this.addUserId = addUserId;
		this.hot = hot;
		this.lxName = lxName;
		this.lxTel = lxTel;
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

	@ManyToOne(fetch = FetchType.EAGER) // 等同于lazy="false"
	@JoinColumn(name = "company_id", nullable = false, columnDefinition = "varchar(100) COMMENT '公司编号'")
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Column(name = "jz_type", nullable = false, columnDefinition = "varchar(30) COMMENT '驾照类型（C2,C1,B）'")
	public String getJzType() {
		return this.jzType;
	}

	public void setJzType(String jzType) {
		this.jzType = jzType;
	}

	@Column(name = "sj_age_range", nullable = false, columnDefinition = "varchar(30) COMMENT '司机年龄范围'")
	public String getSjAgeRange() {
		return this.sjAgeRange;
	}

	public void setSjAgeRange(String sjAgeRange) {
		this.sjAgeRange = sjAgeRange;
	}

	@Column(name = "jl_year_range", nullable = false, columnDefinition = "varchar(30) COMMENT '司机驾龄范围'")
	public String getJlYearRange() {
		return this.jlYearRange;
	}

	public void setJlYearRange(String jlYearRange) {
		this.jlYearRange = jlYearRange;
	}

	@Column(name = "wage", nullable = false, columnDefinition = "varchar(100) COMMENT '薪资'")
	public String getWage() {
		return this.wage;
	}

	public void setWage(String  wage) {
		this.wage = wage;
	}

	@Column(name = "province", nullable = false, columnDefinition = "varchar(50) COMMENT '省'")
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "city", nullable = false, columnDefinition = "varchar(50) COMMENT '市'")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "address", nullable = false, columnDefinition = "varchar(200) COMMENT '公司地址'")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "education",  columnDefinition = "varchar(100) COMMENT '学历'")
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	@Column(name = "work_year",  columnDefinition = "varchar(100) COMMENT '工作年限'")
	public String getWorkYear() {
		return workYear;
	}
	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}
	@Column(name = "num",  columnDefinition = "int(11) COMMENT '人数'")
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Column(name = "welfare",  columnDefinition = "varchar(100) COMMENT '福利待遇'")
	public String getWelfare() {
		return welfare;
	}
	public void setWelfare(String welfare) {
		this.welfare = welfare;
	}
	@Column(name = "remark", columnDefinition = "varchar(255) COMMENT '备注'")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@Column(name = "show_status", nullable = false, columnDefinition = "int(11) COMMENT '上/下架状态（0：上架，1：下架'")
	public int getShowStatus() {
		return this.showStatus;
	}

	public void setShowStatus(int showStatus) {
		this.showStatus = showStatus;
	}

	@Column(name = "add_time", nullable = false, columnDefinition = "varchar(50) COMMENT '添加时间'")
	public String getAddTime() {
		return this.addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

	@Column(name = "user_type", nullable = false, columnDefinition = "int(11) COMMENT '上传人员类型（1：后台管理人员，2：普通用户）'")
	public int getUserType() {
		return this.userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	@Column(name = "add_user_id", nullable = false, columnDefinition = "varchar(100) COMMENT '上传人员编号'")
	public String getAddUserId() {
		return this.addUserId;
	}

	public void setAddUserId(String addUserId) {
		this.addUserId = addUserId;
	}

	@Column(name = "hot", nullable = false, columnDefinition = "int(11) COMMENT '热度（默认为0）'")
	public int getHot() {
		return this.hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	@Column(name = "lx_name", nullable = false, columnDefinition = "varchar(30) COMMENT '联系人'")
	public String getLxName() {
		return this.lxName;
	}

	public void setLxName(String lxName) {
		this.lxName = lxName;
	}

	@Column(name = "lx_tel", nullable = false, columnDefinition = "varchar(30) COMMENT '联系电话'")
	public String getLxTel() {
		return this.lxTel;
	}

	public void setLxTel(String lxTel) {
		this.lxTel = lxTel;
	}

}
