package com.lng.pojo;
// Generated 2019-12-2 10:34:35 by Hibernate Tools 5.0.6.Final

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * DriverQz generated by hbm2java
 */
@Entity
@Table(name = "driver_qz", catalog = "lng")
@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
@ApiModel("司机求职--DriverQz")
public class DriverQz implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	@ApiModelProperty(value = "用户编号")
	private String userId;
	@ApiModelProperty(value = "用户姓名")
	private String userName;
	@ApiModelProperty(value = "联系电话")
	private String userMobile;
	@ApiModelProperty(value = "用户头像")
	private String userHead;
	@ApiModelProperty(value = "驾龄")
	private int jzYear;
	@ApiModelProperty(value = "驾照类型")
	private String jzType;
	@ApiModelProperty(value = "薪资")
	private int wage;
	@ApiModelProperty(value = "省")
	private String province;
	@ApiModelProperty(value = "市")
	private String city;
	@ApiModelProperty(value = "学历")
	private String education;
	@ApiModelProperty(value = "年龄")
	private int age;
	@ApiModelProperty(value = "性别")
	private String sex;
	@ApiModelProperty(value = "工作经验")
	private String workExp;
	@ApiModelProperty(value = "工作年限")
	private String workYear;
	@ApiModelProperty(value = "毕业院校")
	private String colleges;
	@ApiModelProperty(value = "婚否")
	private String marriage;
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
	@ApiModelProperty(value = "热度")
	private int hot;

	public DriverQz() {
	}

	

	public DriverQz(String userId, String userName, String userMobile, String userHead, int jzYear, String jzType,
			int wage, String province, String city, String education, int age, String sex, String workExp,
			String workYear, String colleges, String marriage, String remark, int checkStatus, String checkTime,
			int showStatus, String addTime, int userType, int hot) {
		this.userId = userId;
		this.userName = userName;
		this.userMobile = userMobile;
		this.userHead = userHead;
		this.jzYear = jzYear;
		this.jzType = jzType;
		this.wage = wage;
		this.province = province;
		this.city = city;
		this.education = education;
		this.age = age;
		this.sex = sex;
		this.workExp = workExp;
		this.workYear = workYear;
		this.colleges = colleges;
		this.marriage = marriage;
		this.remark = remark;
		this.checkStatus = checkStatus;
		this.checkTime = checkTime;
		this.showStatus = showStatus;
		this.addTime = addTime;
		this.userType = userType;
		this.hot = hot;
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

	@Column(name = "user_id", nullable = false, columnDefinition = "varchar(100) COMMENT '用户编号'")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "user_name", nullable = false, columnDefinition = "varchar(30) COMMENT '用户姓名'")
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "user_mobile", nullable = false, columnDefinition = "varchar(30) COMMENT '联系电话'")
	public String getUserMobile() {
		return this.userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	@Column(name = "user_head", columnDefinition = "varchar(100) COMMENT '用户头像'")
	public String getUserHead() {
		return this.userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	@Column(name = "jz_year", nullable = false, columnDefinition = "int(11) COMMENT '驾龄'")
	public int getJzYear() {
		return this.jzYear;
	}

	public void setJzYear(int jzYear) {
		this.jzYear = jzYear;
	}

	@Column(name = "jz_type", nullable = false, columnDefinition = "varchar(30) COMMENT '驾照类型（C2,C1,B...）'")
	public String getJzType() {
		return this.jzType;
	}

	public void setJzType(String jzType) {
		this.jzType = jzType;
	}

	@Column(name = "wage", nullable = false, columnDefinition = "int(11) COMMENT '薪资'")
	public int getWage() {
		return this.wage;
	}

	public void setWage(int wage) {
		this.wage = wage;
	}

	@Column(name = "province", nullable = false, columnDefinition = "varchar(100) COMMENT '省'")
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "city", nullable = false, columnDefinition = "varchar(100) COMMENT '市'")
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	@Column(name = "education",  columnDefinition = "varchar(100) COMMENT '学历'")
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}
	@Column(name = "age",  columnDefinition = "int(11) COMMENT '年龄'")
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	@Column(name = "sex",  columnDefinition = "varchar(50) COMMENT '性别'")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	@Column(name = "work_exp",  columnDefinition = "varchar(100) COMMENT '工作经验'")
	public String getWorkExp() {
		return workExp;
	}

	public void setWorkExp(String workExp) {
		this.workExp = workExp;
	}
	
	@Column(name = "work_year",  columnDefinition = "varchar(100) COMMENT '工作年限'")
	public String getWorkYear() {
		return workYear;
	}
	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}
	@Column(name = "collages",  columnDefinition = "varchar(100) COMMENT '院校'")
	public String getColleges() {
		return colleges;
	}

	public void setColleges(String colleges) {
		this.colleges = colleges;
	}
	@Column(name = "marriage",  columnDefinition = "varchar(50) COMMENT '婚否'")
	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
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

	@Column(name = "check_time", nullable = false, columnDefinition = "varchar(50) COMMENT '审核时间'")
	public String getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	@Column(name = "show_status", nullable = false, columnDefinition = "int(11) COMMENT '上/下架状态（0：上架，1：下架）'")
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

	@Column(name = "hot", nullable = false, columnDefinition = "int(11) COMMENT '热度（默认为0）'")
	public int getHot() {
		return this.hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

}
