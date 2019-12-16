package com.lng.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.Company;
import com.lng.pojo.DriverQz;
import com.lng.pojo.DriverZp;
import com.lng.service.CompanyService;
import com.lng.service.DriverQzService;
import com.lng.service.DriverZpService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.util.Constants;
import com.lng.util.GenericResponse;
import com.lng.util.PageResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "司机招聘相关接口")
@RequestMapping("/driverZp")
public class DriverZpController {
	@Autowired
	private CompanyService cpService;
	@Autowired
	private DriverQzService qzService;
	@Autowired
	private DriverZpService zpService;

	@PostMapping("/addDriverQz")
	@ApiOperation(value = "添加司机求职", notes = "添加司机求职")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "userName", value = "用户姓名", required = true),
			@ApiImplicitParam(name = "userMobile", value = "联系电话", required = true),
			@ApiImplicitParam(name = "userHead", value = "用户头像"),
			@ApiImplicitParam(name = "jzYear", value = "驾龄", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）", required = true, defaultValue = "C1"),
			@ApiImplicitParam(name = "wage", value = "薪资", required = true, defaultValue = "1000"),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "remark", value = "备注", defaultValue = " "),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true),
			@ApiImplicitParam(name = "hot", value = "热度", defaultValue = "0") })
	public GenericResponse addDriverQz(HttpServletRequest request) {
		String userId = CommonTools.getFinalStr("userId", request);
		String userName = CommonTools.getFinalStr("userName", request);
		String userMobile = CommonTools.getFinalStr("userMobile", request);
		String userHead = CommonTools.getFinalStr("userHead", request);
		Integer jzYear = CommonTools.getFinalInteger("jzYear", request);
		String jzType = CommonTools.getFinalStr("jzType", request);
		Integer wage = CommonTools.getFinalInteger("wage", request);
		String province = CommonTools.getFinalStr("province", request);
		String city = CommonTools.getFinalStr("city", request);
		String remark = CommonTools.getFinalStr("remark", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);
		Integer hot = CommonTools.getFinalInteger("hot", request);

		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;
		String qzId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_QZ)) {
			try {
				DriverQz qz = new DriverQz();
				qz.setUserId(userId);
				qz.setUserName(userName);
				qz.setUserMobile(userMobile);
				if (!userHead.equals("")) {
					qz.setUserHead(CommonTools.dealUploadDetail(loginUserId, userHead));
				}
				// qz.setUserHead(userHead);
				qz.setJzYear(jzYear);
				qz.setJzType(jzType);
				qz.setWage(wage);
				qz.setProvince(province);
				qz.setCity(city);
				qz.setRemark(remark);
				qz.setShowStatus(showStatus);
				qz.setUserType(userType);
				if (userType.equals(1)) {
					qz.setCheckStatus(1);
					qz.setCheckTime(CurrentTime.getCurrentTime());
				} else {
					qz.setCheckStatus(0);
					qz.setCheckTime("");
				}
				qz.setAddTime(CurrentTime.getCurrentTime());
				qz.setHot(hot);
				qzId = qzService.saveOrUpdate(qz);

			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, qzId);
	}

	@PutMapping("/updateQzByStatus")
	@ApiOperation(value = "更新司机求职审核状态", notes = "更新司机求职审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机求职主键", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）") })
	public GenericResponse updateQzByStatus(HttpServletRequest request, String id, Integer checkSta, Integer showSta) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_QZ)) {
			try {
				DriverQz qz = qzService.getEntityById(id);
				if (qz == null) {
					status = 50001;
				} else {
					if (checkSta != null && !checkSta.equals(qz.getCheckStatus())) {
						qz.setCheckStatus(checkSta);
						qz.setCheckTime(CurrentTime.getCurrentTime());
					}
					if (showSta != null && !showSta.equals(qz.getShowStatus())) {
						qz.setShowStatus(showSta);
					}
					qzService.saveOrUpdate(qz);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateQzByHot")
	@ApiOperation(value = "更新司机求职热度", notes = "更新司机求职热度")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机求职主键", required = true),
			@ApiImplicitParam(name = "hot", value = "热度（默认为0）") })
	public GenericResponse updateQzByHot(HttpServletRequest request, String id, Integer hot) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_QZ)) {
			try {
				DriverQz qz = qzService.getEntityById(id);
				if (qz == null) {
					status = 50001;
				} else {
					if (hot != null && !hot.equals(qz.getHot())) {
						qz.setHot(hot);
					}
					qzService.saveOrUpdate(qz);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateDriverQz")
	@ApiOperation(value = "更新司机求职", notes = "更新司机求职基本信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机求职主键", required = true),
			@ApiImplicitParam(name = "userName", value = "用户姓名", required = true),
			@ApiImplicitParam(name = "userMobile", value = "联系电话", required = true),
			@ApiImplicitParam(name = "userHead", value = "用户头像"),
			@ApiImplicitParam(name = "jzYear", value = "驾龄", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）", required = true, defaultValue = "C1"),
			@ApiImplicitParam(name = "wage", value = "薪资", required = true, defaultValue = "1000"),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "remark", value = "备注", defaultValue = " "),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true) })
	public GenericResponse updateDriverQz(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id", request);
		String userName = CommonTools.getFinalStr("userName", request);
		String userMobile = CommonTools.getFinalStr("userMobile", request);
		String userHead = CommonTools.getFinalStr("userHead", request);
		Integer jzYear = CommonTools.getFinalInteger("jzYear", request);
		String jzType = CommonTools.getFinalStr("jzType", request);
		Integer wage = CommonTools.getFinalInteger("wage", request);
		String province = CommonTools.getFinalStr("province", request);
		String city = CommonTools.getFinalStr("city", request);
		String remark = CommonTools.getFinalStr("remark", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_QZ)) {
			try {
				DriverQz qz = qzService.getEntityById(id);
				if (qz == null) {
					status = 50001;
				} else {
					if (!userName.isEmpty() && !userName.equals(qz.getUserName())) {
						qz.setUserName(userName);
					}
					if (!userMobile.isEmpty() && !userMobile.equals(qz.getUserMobile())) {
						qz.setUserMobile(userMobile);
					}
					if (!userHead.isEmpty() && !userHead.equals(qz.getUserHead())) {
						qz.setUserHead(userHead);
					}
					if (jzYear != null && !jzYear.equals(qz.getJzYear())) {
						qz.setJzYear(jzYear);
					}
					if (!jzType.isEmpty() && jzType.equals(qz.getJzType())) {
						qz.setJzType(jzType);
					}
					if (wage != null && !wage.equals(qz.getWage())) {
						qz.setWage(wage);
					}
					if (!province.isEmpty() && !province.equals(qz.getProvince())) {
						qz.setProvince(province);
					}
					if (!city.isEmpty() && !city.equals(qz.getCity())) {
						qz.setCity(city);
					}
					if (!remark.isEmpty() && !remark.equals(qz.getRemark())) {
						qz.setRemark(remark);
					}
					if (userType != null && !userType.equals(qz.getUserType())) {
						qz.setUserType(userType);
					}
					qzService.saveOrUpdate(qz);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}
	@GetMapping("/queryDriverQz")
	@ApiOperation(value = "获取司机求职", notes = "获取司机求职分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ 
		    @ApiImplicitParam(name = "userId", value = "用户编号"),
		    @ApiImplicitParam(name = "jzYear", value = "驾龄"),
		    @ApiImplicitParam(name = "jz_type", value = "驾照类型（C2,C1,B...）"),
		    @ApiImplicitParam(name = "wage", value = "薪资"),
		    @ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
		    @ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryDriverQz(String userId,Integer jzYear,String jzType,String wage, Integer checkSta,Integer showSta, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<DriverQz> qzs = null;
		try {
			userId = CommonTools.getFinalStr(userId);
			jzType = CommonTools.getFinalStr(jzType);
			wage = CommonTools.getFinalStr(wage);
	
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			if (showSta == null) {
				showSta = -1;
			}
			if (jzYear == null) {
				jzYear = -1;
			}
			qzs = qzService.getDriverQzByOption(userId, jzYear, jzType, wage, checkSta, showSta, pageNo, pageSize);
			if (qzs.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, qzs.getTotalElements(), status, qzs.getContent());
	}
	
	@PostMapping("/addDriverZp")
	@ApiOperation(value = "添加司机招聘", notes = "添加司机招聘")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({
		    @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）", required = true, defaultValue = "C1"),
			@ApiImplicitParam(name = "wage", value = "薪资范围", required = true, defaultValue = "1000-2000"),
			@ApiImplicitParam(name = "sjAgeRange", value = "司机年龄范围", required = true),
			@ApiImplicitParam(name = "jlYearRange", value = "司机驾龄范围", required = true),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "address", value = "公司地址", defaultValue = "大庆路200号", required = true),
			@ApiImplicitParam(name = "remark", value = "备注", defaultValue = " "),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true),
			@ApiImplicitParam(name = "addUserId", value = "上传人员编号", required = true), 
			@ApiImplicitParam(name = "hot", value = "热度", defaultValue = "0",required = true) ,
			@ApiImplicitParam(name = "lxName", value = "联系人",required = true) ,
			@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true) 
		    })
	public GenericResponse addDriverZp(HttpServletRequest request) {
		String compId = CommonTools.getFinalStr("compId", request);
		String jzType = CommonTools.getFinalStr("jzType", request);
		Integer wage = CommonTools.getFinalInteger("wage", request);
		String sjAgeRange = CommonTools.getFinalStr("sjAgeRange", request);
		String jlYearRange = CommonTools.getFinalStr("jlYearRange", request);
		String province = CommonTools.getFinalStr("province", request);
		String address = CommonTools.getFinalStr("address", request);
		String city = CommonTools.getFinalStr("city", request);
		String remark = CommonTools.getFinalStr("remark", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);
		String addUserId = CommonTools.getFinalStr("addUserId", request);
		Integer hot = CommonTools.getFinalInteger("hot", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);

		Integer status = 200;
		String zpId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_ZP)) {
			try {
				DriverZp zp = new DriverZp();
				Company company = cpService.getEntityById(compId);
				zp.setCompany(company );
				zp.setJzType(jzType);
				zp.setWage(wage);
				zp.setSjAgeRange(sjAgeRange);
				zp.setJlYearRange(jlYearRange);
				zp.setProvince(province);
				zp.setCity(city);
				zp.setAddress(address);
				zp.setRemark(remark);
				zp.setShowStatus(showStatus);
				zp.setUserType(userType);
				zp.setAddUserId(addUserId);
				if (userType.equals(1)) {
					zp.setCheckStatus(1);
					zp.setCheckTime(CurrentTime.getCurrentTime());
				} else {
					zp.setCheckStatus(0);
					zp.setCheckTime("");
				}
				zp.setAddTime(CurrentTime.getCurrentTime());
				zp.setHot(hot);
				zp.setLxName(lxName);
				zp.setLxTel(lxTel);
				zpId = zpService.saveOrUpdate(zp);

			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, zpId);
	}
	@PutMapping("/updateZpByStatus")
	@ApiOperation(value = "更新司机招聘审核状态", notes = "更新司机招聘审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机招聘主键", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）") })
	public GenericResponse updateZpByStatus(HttpServletRequest request, String id, Integer checkSta, Integer showSta) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_ZP)) {
			try {
				DriverZp zp = zpService.getEntityById(id);
				if (zp == null) {
					status = 50001;
				} else {
					if (checkSta != null && !checkSta.equals(zp.getCheckStatus())) {
						zp.setCheckStatus(checkSta);
						zp.setCheckTime(CurrentTime.getCurrentTime());
					}
					if (showSta != null && !showSta.equals(zp.getShowStatus())) {
						zp.setShowStatus(showSta);
					}
					zpService.saveOrUpdate(zp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateZpByHot")
	@ApiOperation(value = "更新司机招聘热度", notes = "更新司机招聘热度")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机招聘主键", required = true),
			@ApiImplicitParam(name = "hot", value = "热度（默认为0）") })
	public GenericResponse updateZpByHot(HttpServletRequest request, String id, Integer hot) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_ZP)) {
			try {
				DriverZp zp = zpService.getEntityById(id);
				if (zp == null) {
					status = 50001;
				} else {
					if (hot != null && !hot.equals(zp.getHot())) {
						zp.setHot(hot);
					}
					zpService.saveOrUpdate(zp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}
	@PutMapping("/updateDriverZp")
	@ApiOperation(value = "更新司机招聘", notes = "更新司机招聘")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({
		    @ApiImplicitParam(name = "id", value = "司机招聘主键", required = true),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）", required = true, defaultValue = "C1"),
			@ApiImplicitParam(name = "wage", value = "薪资范围", required = true, defaultValue = "1000-2000"),
			@ApiImplicitParam(name = "sjAgeRange", value = "司机年龄范围", required = true),
			@ApiImplicitParam(name = "jlYearRange", value = "司机驾龄范围", required = true),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "address", value = "公司地址", defaultValue = "大庆路200号", required = true),
			@ApiImplicitParam(name = "remark", value = "备注", defaultValue = " "),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true),
			@ApiImplicitParam(name = "addUserId", value = "上传人员编号", required = true), 
			@ApiImplicitParam(name = "lxName", value = "联系人",required = true) ,
			@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true) 
		    })
	public GenericResponse updateDriverZp(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id", request);
		String jzType = CommonTools.getFinalStr("jzType", request);
		Integer wage = CommonTools.getFinalInteger("wage", request);
		String sjAgeRange = CommonTools.getFinalStr("sjAgeRange", request);
		String jlYearRange = CommonTools.getFinalStr("jlYearRange", request);
		String province = CommonTools.getFinalStr("province", request);
		String address = CommonTools.getFinalStr("address", request);
		String city = CommonTools.getFinalStr("city", request);
		String remark = CommonTools.getFinalStr("remark", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);
		String addUserId = CommonTools.getFinalStr("addUserId", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);

		Integer status = 200;
		String zpId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_ZP)) {
			try {
				DriverZp zp = zpService.getEntityById(id);
				if (zp == null) {
					status = 50001;
				}else {
					if (!jzType.isEmpty() && jzType.equals(zp.getJzType())) {
						zp.setJzType(jzType);
					}
					if (wage != null && !wage.equals(zp.getWage())) {
						zp.setWage(wage);
					}
					if(!sjAgeRange.isEmpty() && ! sjAgeRange.equals(zp.getSjAgeRange())) {
						zp.setSjAgeRange(sjAgeRange);
					}
					if(!jlYearRange.isEmpty() && ! jlYearRange.equals(zp.getJlYearRange())) {
						zp.setJlYearRange(jlYearRange);
					}
					if (!province.isEmpty() && !province.equals(zp.getProvince())) {
						zp.setProvince(province);
					}
					if (!city.isEmpty() && !city.equals(zp.getCity())) {
						zp.setCity(city);
					}
					if(!address.isEmpty() && !address.equals(zp.getAddress())) {
						zp.setAddress(address);
					}
					if (!remark.isEmpty() && !remark.equals(zp.getRemark())) {
						zp.setRemark(remark);
					}
					if (userType != null && !userType.equals(zp.getUserType())) {
						zp.setUserType(userType);
					}
					if(!addUserId.isEmpty() && ! addUserId.equals(zp.getAddUserId())) {
						zp.setAddUserId(addUserId);
					}
					if(!lxName.isEmpty() && ! lxName.equals(zp.getLxName())) {
						zp.setLxName(lxName);
					}
					if(!lxTel.isEmpty() && ! lxTel.equals(zp.getLxTel())) {
						zp.setLxTel(lxTel);
					}
					zpId = zpService.saveOrUpdate(zp);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, zpId);
	}
	
	@GetMapping("/queryDriverZp")
	@ApiOperation(value = "获取司机招聘", notes = "获取司机招聘分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ 
		    @ApiImplicitParam(name = "compId", value = " 公司编号"),
		    @ApiImplicitParam(name = "jz_type", value = "驾照类型（C2,C1,B...）"),
		    @ApiImplicitParam(name = "wage", value = "薪资"),
		    @ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
		    @ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryDriverZp(String compId,String jzType,String wage, Integer checkSta,Integer showSta, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<DriverZp> zps = null;
		try {
			compId = CommonTools.getFinalStr(compId);
			jzType = CommonTools.getFinalStr(jzType);
			wage = CommonTools.getFinalStr(wage);
	
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			if (showSta == null) {
				showSta = -1;
			}
			zps = zpService.getDriverQzByOption(compId, jzType, checkSta, showSta, wage, pageNo, pageSize);
			if (zps.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, zps.getTotalElements(), status, zps.getContent());
	}
	
}
