package com.lng.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.lng.pojo.User;
import com.lng.pojo.UserFocus;
import com.lng.service.CompanyService;
import com.lng.service.DriverQzService;
import com.lng.service.DriverZpService;
import com.lng.service.SuperService;
import com.lng.service.UserFocusService;
import com.lng.service.UserService;
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
	@Autowired
	private UserFocusService ufService;
	@Autowired
	private UserService us;

	@GetMapping("/getPubNum")
	@ApiOperation(value = "获取发布求职/招聘信息数量", notes = "获取发布求职/招聘数量")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 10002, message = "参数为空"), 
			@ApiResponse(code = 50005, message = "超过最大数量")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号（公司发布招聘信息时用）"),
			@ApiImplicitParam(name = "userId", value = "用户编号",required = true),
			@ApiImplicitParam(name = "opt", value = "参数变量（0：获取求职简历数量，1：获取公司招聘信息数量）",required = true)
	})
	public GenericResponse getPubNum(HttpServletRequest request) {
		Integer status = 200;
		String compId = CommonTools.getFinalStr("compId", request);
		String userId = CommonTools.getFinalStr("userId", request);
		Integer opt = CommonTools.getFinalInteger("opt", request);
		status = 1000;
		try {
			if (userId.equals("")) {
				status = 10002;
			}else if(opt.equals(1) && compId.equals("")) {
				status = 10002;
			} else {
				if(opt.equals(0)) {
					if (qzService.getDriverQzByUserId(userId).size() > 0) {
						status = 50005;
					}else {
						status = 200;
					}
				}else {
					if (zpService.getDriverZpList(compId).size() >= 5) {
						status = 50005;
					}else {
						status = 200;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	
	@PostMapping("/addDriverQz")
	@ApiOperation(value = "添加司机求职", notes = "添加司机求职")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50005, message = "超过最大数量") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号"),
			@ApiImplicitParam(name = "userName", value = "用户姓名", required = true),
			@ApiImplicitParam(name = "userMobile", value = "联系电话", required = true),
			@ApiImplicitParam(name = "userHead", value = "用户头像"),
			@ApiImplicitParam(name = "jzYear", value = "驾龄", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）", required = true, defaultValue = "C1"),
			@ApiImplicitParam(name = "wage", value = "薪资", required = true, defaultValue = "1000"),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "education", value = "学历", defaultValue = "高中"),
			@ApiImplicitParam(name = "age", value = "年龄", defaultValue = "25"),
			@ApiImplicitParam(name = "sex", value = "性别", defaultValue = "男"),
			@ApiImplicitParam(name = "workExp", value = "工作经验", defaultValue = "无"),
			@ApiImplicitParam(name = "workYear", value = "工作年限", defaultValue = "8"),
			@ApiImplicitParam(name = "colleges", value = "院校", defaultValue = " "),
			@ApiImplicitParam(name = "marriage", value = "婚否", defaultValue = "未婚"),
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
		Integer age = CommonTools.getFinalInteger("age", request);
		String province = CommonTools.getFinalStr("province", request);
		String city = CommonTools.getFinalStr("city", request);
		String education = CommonTools.getFinalStr("education", request);
		String sex = CommonTools.getFinalStr("sex", request);
		String workExp = CommonTools.getFinalStr("workExp", request);
		String workYear = CommonTools.getFinalStr("workYear", request);
		String colleges = CommonTools.getFinalStr("colleges", request);
		String marriage = CommonTools.getFinalStr("marriage", request);
		String remark = CommonTools.getFinalStr("remark", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);
		Integer hot = CommonTools.getFinalInteger("hot", request);
		Integer status = 200;
		String qzId = "";
		try {
			if (qzService.getDriverQzByUserId(userId).size() == 0) {
				DriverQz qz = new DriverQz();
				qz.setUserId(userId);
				qz.setUserName(userName);
				qz.setUserMobile(userMobile);
				if (!userHead.equals("")) {
					qz.setUserHead(CommonTools.dealUploadDetail(userId, "", userHead));
				}
				qz.setJzYear(jzYear);
				qz.setJzType(jzType);
				qz.setWage(wage);
				qz.setProvince(province);
				qz.setCity(city);
				qz.setEducation(education);
				qz.setAge(age);
				qz.setSex(sex);
				qz.setWorkExp(workExp);
				qz.setWorkYear(workYear);
				qz.setColleges(colleges);
				qz.setMarriage(marriage);
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
			} else {
				status = 50005;
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, qzId);
	}

	@PutMapping("/updateQzByStatus")
	@ApiOperation(value = "更新司机求职审核状态", notes = "更新司机求职审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
					@ApiResponse(code = 200, message = "成功"),
					@ApiResponse(code = 50001, message = "数据未找到"),
					@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机求职主键", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "opt", value = "0:审核，1：显示/隐藏",required = true,dataType = "integer")
	})
	public GenericResponse updateQzByStatus(HttpServletRequest request) {
		String  id = CommonTools.getFinalStr("id",request);
		Integer checkSta = CommonTools.getFinalInteger("checkSta", request);
		Integer showSta = CommonTools.getFinalInteger("showSta", request);
		Integer status = 200;
		Integer opt = CommonTools.getFinalInteger("opt", request);
		try {
			DriverQz qz = qzService.getEntityById(id);
			if (qz == null) {
				status = 50001;
			} else {
				if(opt.equals(0)) {
					if(checkSta >= 0 && !checkSta.equals(qz.getCheckStatus())) {
						if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),CommonTools.getLoginRoleName(request), Constants.CHECK_QZ)) {
							qz.setCheckStatus(checkSta);
							qz.setCheckTime(CurrentTime.getCurrentTime());
							qzService.saveOrUpdate(qz);
						}else {
							status = 70001;
						}
					}
				}else {
					if (showSta >=0 && !showSta.equals(qz.getShowStatus())) {
						qz.setShowStatus(showSta);
						if(qz.getShowStatus() == 0) {//只有正常显示的才能进行隐藏
							if(showSta.equals(1)) {//由正常显示到 隐藏
								//重置审核状态为未审核
								qz.setCheckStatus(0);
							}
						}
						qzService.saveOrUpdate(qz);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateQzByHot")
	@ApiOperation(value = "更新司机求职热度", notes = "更新司机求职热度")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机求职主键", required = true),
			@ApiImplicitParam(name = "hot", value = "热度（默认为0）") })
	public GenericResponse updateQzByHot(HttpServletRequest request, String id, Integer hot) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;

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
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateDriverQz")
	@ApiOperation(value = "更新司机求职", notes = "更新司机求职基本信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 80001, message = "审核通过不能修改"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机求职主键", required = true),
			@ApiImplicitParam(name = "userName", value = "用户姓名", required = true),
			@ApiImplicitParam(name = "userMobile", value = "联系电话", required = true),
			@ApiImplicitParam(name = "userHead", value = "用户头像"),
			@ApiImplicitParam(name = "jzYear", value = "驾龄", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）", required = true, defaultValue = "C1"),
			@ApiImplicitParam(name = "wage", value = "薪资", required = true, defaultValue = "1000"),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "education", value = "学历", defaultValue = "高中"),
			@ApiImplicitParam(name = "age", value = "年龄", defaultValue = "25"),
			@ApiImplicitParam(name = "sex", value = "性别", defaultValue = "男"),
			@ApiImplicitParam(name = "workExp", value = "工作经验", defaultValue = "无"),
			@ApiImplicitParam(name = "workYear", value = "工作年限", defaultValue = "8"),
			@ApiImplicitParam(name = "colleges", value = "院校", defaultValue = " "),
			@ApiImplicitParam(name = "marriage", value = "婚否", defaultValue = "未婚"),
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
		Integer age = CommonTools.getFinalInteger("age", request);
		String education = CommonTools.getFinalStr("education", request);
		String sex = CommonTools.getFinalStr("sex", request);
		String workExp = CommonTools.getFinalStr("workExp", request);
		String workYear = CommonTools.getFinalStr("workYear", request);
		String colleges = CommonTools.getFinalStr("colleges", request);
		String marriage = CommonTools.getFinalStr("marriage", request);
		String remark = CommonTools.getFinalStr("remark", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);
		Integer status = 200;

		try {
			DriverQz qz = qzService.getEntityById(id);
			if (qz == null) {
				status = 50001;
			} else if (qz.getCheckStatus() == 1) {
				status = 80001;
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
				if (age != null && !age.equals(qz.getAge())) {
					qz.setAge(age);
				}
				if (!education.isEmpty() && !education.equals(qz.getEducation())) {
					qz.setEducation(education);
				}
				if (!sex.isEmpty() && !sex.equals(qz.getSex())) {
					qz.setSex(sex);
				}
				if (!workExp.isEmpty() && !workExp.equals(qz.getWorkExp())) {
					qz.setWorkExp(workExp);
				}
				if (!workYear.isEmpty() && !workYear.equals(qz.getWorkYear())) {
					qz.setWorkYear(workYear);
				}
				if (!colleges.isEmpty() && !colleges.equals(qz.getColleges())) {
					qz.setColleges(colleges);
				}
				if (!marriage.isEmpty() && !marriage.equals(qz.getMarriage())) {
					qz.setMarriage(marriage);
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
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/getDriverQzById")
	@ApiOperation(value = "根据主键获取司机求职详细信息", notes = "根据主键获取司机求职详细信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 10002, message = "参数为空"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机求职编号"),
			@ApiImplicitParam(name = "userId", value = "用户编号") })
	public GenericResponse getDriverQzById(HttpServletRequest request) {
		Integer status = 200;
		String qzId = CommonTools.getFinalStr("id", request);
		String userId = CommonTools.getFinalStr("userId", request);
		List<Object> list = new ArrayList<Object>();
		try {
			if (qzId.equals("") && userId.equals("")) {
				status = 10002;
			} else {
				DriverQz qz = null;
				if (!qzId.isEmpty()) {
					qz = qzService.getEntityById(qzId);
				} else if (!userId.isEmpty()) {
					List<DriverQz> qzList = qzService.getDriverQzByUserId(userId);
					if (qzList.size() > 0) {
						qz = qzList.get(0);
					}
				}

				if (qz == null) {
					status = 10002;
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", qz.getId());
					map.put("userName", qz.getUserName());
					map.put("userMobile", qz.getUserMobile());
					map.put("userHead", qz.getUserHead());
					map.put("jzYear", qz.getJzYear());
					map.put("jzType", qz.getJzType());
					map.put("wage", qz.getWage());
					map.put("privince", qz.getProvince());
					map.put("city", qz.getCity());
					map.put("education", qz.getEducation());
					map.put("age", qz.getAge());
					map.put("sex", qz.getSex());
					map.put("workExp", qz.getWorkExp());
					map.put("workYear", qz.getWorkYear());
					map.put("colleges", qz.getColleges());
					map.put("marriage", qz.getMarriage());
					map.put("remark", qz.getRemark());
					map.put("showStatus", qz.getShowStatus());
					String ufId = "";
					if (!userId.isEmpty()) {
						List<UserFocus> ufList = ufService.getUserFocusList(userId, qzId, "sjqz");
						if(ufList.size() > 0) {
							ufId = ufList.get(0).getId();
						}
					}
					map.put("ufId", ufId);
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

	@GetMapping("/queryDriverQz")
	@ApiOperation(value = "获取司机求职", notes = "获取司机求职分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号"),
			@ApiImplicitParam(name = "jzYear", value = "驾龄"),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）"),
			@ApiImplicitParam(name = "wage", value = "薪资"),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "page", value = "第几页"), @ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse queryDriverQz(String userId, String jzYear, String jzType, String wage, Integer checkSta,
			Integer showSta, Integer page, Integer limit) {
		Integer status = 200;
		Page<DriverQz> qzs = null;
		List<Object> list = new ArrayList<Object>();
		try {
			userId = CommonTools.getFinalStr(userId);
			jzType = CommonTools.getFinalStr(jzType);
			wage = CommonTools.getFinalStr(wage);
			jzYear = CommonTools.getFinalStr(jzYear);
			if (page == null) {
				page = 1;
			}
			if (limit == null) {
				limit = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			if (showSta == null) {
				showSta = -1;
			}
			qzs = qzService.getDriverQzByOption(userId, jzYear, jzType, wage, checkSta, showSta, page - 1, limit);
			if (qzs.getTotalElements() == 0) {
				status = 50001;
			} else {
				List<DriverQz> qzList = qzs.getContent();
				for (DriverQz qz : qzList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("qzId", qz.getId());
					map.put("userName", qz.getUserName());
					map.put("userMobile", qz.getUserMobile());
					map.put("userHead", qz.getUserHead());
					map.put("jzYear", qz.getJzYear());
					map.put("jzType", qz.getJzType());
					map.put("checkStatus", qz.getCheckStatus());
					map.put("showStatus", qz.getShowStatus());
					map.put("education", qz.getEducation());
					map.put("sex", qz.getSex());
					map.put("age", qz.getAge());
					map.put("prov", qz.getProvince());
					map.put("city", qz.getCity());
					map.put("addTime", qz.getAddTime());
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, qzs.getTotalElements(), status, list);
	}

	@PostMapping("/addDriverZp")
	@ApiOperation(value = "添加司机招聘", notes = "添加司机招聘")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50005, message = "超过最大数量") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）", required = true, defaultValue = "C1"),
			@ApiImplicitParam(name = "wage", value = "薪资范围", required = true, defaultValue = "1000-2000"),
			@ApiImplicitParam(name = "sjAgeRange", value = "司机年龄范围", required = true),
			@ApiImplicitParam(name = "jlYearRange", value = "司机驾龄范围", required = true),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "address", value = "公司地址", defaultValue = "大庆路200号", required = true),
			@ApiImplicitParam(name = "remark", value = "备注", defaultValue = " "),
			@ApiImplicitParam(name = "education", value = "学历", defaultValue = " "),
			@ApiImplicitParam(name = "workYear", value = "工作年限", defaultValue = " "),
			@ApiImplicitParam(name = "num", value = "人数", defaultValue = " "),
			@ApiImplicitParam(name = "welfare", value = "福利待遇", defaultValue = " "),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true),
			@ApiImplicitParam(name = "addUserId", value = "上传人员编号", required = true),
			@ApiImplicitParam(name = "hot", value = "热度", defaultValue = "0"),
			@ApiImplicitParam(name = "lxName", value = "联系人", required = true),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true) })
	public GenericResponse addDriverZp(HttpServletRequest request) {
		String compId = CommonTools.getFinalStr("compId", request);
		String jzType = CommonTools.getFinalStr("jzType", request);
		String wage = CommonTools.getFinalStr("wage", request);
		Integer num = CommonTools.getFinalInteger("num", request);
		String sjAgeRange = CommonTools.getFinalStr("sjAgeRange", request);
		String education = CommonTools.getFinalStr("education", request);
		String workYear = CommonTools.getFinalStr("workYear", request);
		String welfare = CommonTools.getFinalStr("welfare", request);
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
		try {
			if (zpService.getDriverZpList(compId).size() < 5) {
				DriverZp zp = new DriverZp();
				Company company = cpService.getEntityById(compId);
				zp.setCompany(company);
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
				zp.setEducation(education);
				zp.setWorkYear(workYear);
				zp.setNum(num);
				zp.setWelfare(welfare);
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
			} else {
				status = 50005;
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}

		return ResponseFormat.retParam(status, zpId);
	}

	@PutMapping("/updateZpByStatus")
	@ApiOperation(value = "更新司机招聘审核状态", notes = "更新司机招聘审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"),
					@ApiResponse(code = 200, message = "成功"),
					@ApiResponse(code = 50001, message = "数据未找到"),
					@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机招聘主键", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "opt", value = "0:审核，1：显示/隐藏",required = true,dataType = "integer")
	})
	public GenericResponse updateZpByStatus(HttpServletRequest request) {
		String  id = CommonTools.getFinalStr("id",request);
		Integer checkSta = CommonTools.getFinalInteger("checkSta", request);
		Integer showSta = CommonTools.getFinalInteger("showSta", request);
		Integer opt = CommonTools.getFinalInteger("opt", request);
		Integer status = 200;
		try {
			DriverZp zp = zpService.getEntityById(id);
			if (zp == null) {
				status = 50001;
			} else {
				if(opt.equals(0)) {
					if(checkSta >= 0 && !checkSta.equals(zp.getCheckStatus())) {
						if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),CommonTools.getLoginRoleName(request), Constants.CHECK_ZP)) {
							zp.setCheckStatus(checkSta);
							zp.setCheckTime(CurrentTime.getCurrentTime());
							zpService.saveOrUpdate(zp);
						}else {
							status = 70001;
						}
					}
				}else {
					if (showSta >=0 && !showSta.equals(zp.getShowStatus())) {
						zp.setShowStatus(showSta);
						if(zp.getShowStatus() == 0) {//只有正常显示的才能进行隐藏
							if(showSta.equals(1)) {//由正常显示到 隐藏
								//重置审核状态为未审核
								zp.setCheckStatus(0);
							}
						}
						zpService.saveOrUpdate(zp);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateZpByHot")
	@ApiOperation(value = "更新司机招聘热度", notes = "更新司机招聘热度")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机招聘主键", required = true),
			@ApiImplicitParam(name = "hot", value = "热度（默认为0）") })
	public GenericResponse updateZpByHot(HttpServletRequest request, String id, Integer hot) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
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
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateDriverZp")
	@ApiOperation(value = "更新司机招聘", notes = "更新司机招聘")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 80001, message = "审核通过不能修改"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机招聘主键", required = true),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）", required = true, defaultValue = "C1"),
			@ApiImplicitParam(name = "wage", value = "薪资范围", required = true, defaultValue = "1000-2000"),
			@ApiImplicitParam(name = "sjAgeRange", value = "司机年龄范围", required = true),
			@ApiImplicitParam(name = "jlYearRange", value = "司机驾龄范围", required = true),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "address", value = "公司地址", defaultValue = "大庆路200号", required = true),
			@ApiImplicitParam(name = "remark", value = "备注", defaultValue = " "),
			@ApiImplicitParam(name = "education", value = "学历", defaultValue = " "),
			@ApiImplicitParam(name = "workYear", value = "工作年限", defaultValue = " "),
			@ApiImplicitParam(name = "num", value = "人数", defaultValue = " "),
			@ApiImplicitParam(name = "welfare", value = "福利待遇", defaultValue = " "),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true),
			@ApiImplicitParam(name = "addUserId", value = "上传人员编号", required = true),
			@ApiImplicitParam(name = "lxName", value = "联系人", required = true),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true) })
	public GenericResponse updateDriverZp(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id", request);
		String jzType = CommonTools.getFinalStr("jzType", request);
		String wage = CommonTools.getFinalStr("wage", request);
		Integer num = CommonTools.getFinalInteger("num", request);
		String sjAgeRange = CommonTools.getFinalStr("sjAgeRange", request);
		String jlYearRange = CommonTools.getFinalStr("jlYearRange", request);
		String province = CommonTools.getFinalStr("province", request);
		String address = CommonTools.getFinalStr("address", request);
		String city = CommonTools.getFinalStr("city", request);
		String remark = CommonTools.getFinalStr("remark", request);
		String education = CommonTools.getFinalStr("education", request);
		String workYear = CommonTools.getFinalStr("workYear", request);
		String welfare = CommonTools.getFinalStr("welfare", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);
		String addUserId = CommonTools.getFinalStr("addUserId", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);

		Integer status = 200;
		String zpId = "";
		try {
			DriverZp zp = zpService.getEntityById(id);
			if (zp == null) {
				status = 50001;
			} else if (zp.getCheckStatus() == 1) {
				status = 80001;
			} else {
				if (!jzType.isEmpty() && jzType.equals(zp.getJzType())) {
					zp.setJzType(jzType);
				}
				if (!wage.isEmpty() && !wage.equals(zp.getWage())) {
					zp.setWage(wage);
				}
				if (num != null && !num.equals(zp.getNum())) {
					zp.setNum(num);
				}
				if (!education.isEmpty() && !education.equals(zp.getEducation())) {
					zp.setEducation(education);
				}
				if (!workYear.isEmpty() && !workYear.equals(zp.getWorkYear())) {
					zp.setWorkYear(workYear);
				}
				if (!welfare.isEmpty() && !welfare.equals(zp.getWelfare())) {
					zp.setWelfare(welfare);
				}
				if (!sjAgeRange.isEmpty() && !sjAgeRange.equals(zp.getSjAgeRange())) {
					zp.setSjAgeRange(sjAgeRange);
				}
				if (!jlYearRange.isEmpty() && !jlYearRange.equals(zp.getJlYearRange())) {
					zp.setJlYearRange(jlYearRange);
				}
				if (!province.isEmpty() && !province.equals(zp.getProvince())) {
					zp.setProvince(province);
				}
				if (!city.isEmpty() && !city.equals(zp.getCity())) {
					zp.setCity(city);
				}
				if (!address.isEmpty() && !address.equals(zp.getAddress())) {
					zp.setAddress(address);
				}
				if (!remark.isEmpty() && !remark.equals(zp.getRemark())) {
					zp.setRemark(remark);
				}
				if (userType != null && !userType.equals(zp.getUserType())) {
					zp.setUserType(userType);
				}
				if (!addUserId.isEmpty() && !addUserId.equals(zp.getAddUserId())) {
					zp.setAddUserId(addUserId);
				}
				if (!lxName.isEmpty() && !lxName.equals(zp.getLxName())) {
					zp.setLxName(lxName);
				}
				if (!lxTel.isEmpty() && !lxTel.equals(zp.getLxTel())) {
					zp.setLxTel(lxTel);
				}
				zpId = zpService.saveOrUpdate(zp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, zpId);
	}

	@GetMapping("/queryDriverZp")
	@ApiOperation(value = "获取司机招聘", notes = "获取司机招聘分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = " 公司编号"),
			@ApiImplicitParam(name = "jzYear", value = "驾龄"),
			@ApiImplicitParam(name = "jzType", value = "驾照类型（C2,C1,B...）"),
			@ApiImplicitParam(name = "wage", value = "薪资"),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "page", value = "第几页"), @ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse queryDriverZp(String compId, String jzType, String wage, String jzYear,Integer checkSta, Integer showSta,
			Integer page, Integer limit) {
		Integer status = 200;
		Page<DriverZp> zps = null;
		List<Object> list = new ArrayList<Object>();
		try {
			compId = CommonTools.getFinalStr(compId);
			jzType = CommonTools.getFinalStr(jzType);
			wage = CommonTools.getFinalStr(wage);
			jzYear = CommonTools.getFinalStr(jzYear);
			if (page == null) {
				page = 1;
			}
			if (limit == null) {
				limit = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			if (showSta == null) {
				showSta = -1;
			}
			zps = zpService.getDriverQzByOption(compId, jzType, jzYear,checkSta, showSta, wage, page - 1, limit);
			if (zps.getTotalElements() == 0) {
				status = 50001;
			} else {
				List<DriverZp> zpList = zps.getContent();
				for (DriverZp zp : zpList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("zpId", zp.getId());
					map.put("companyName", zp.getCompany().getName());
					map.put("age", zp.getSjAgeRange());
					map.put("jlYear", zp.getJlYearRange());
					map.put("province", zp.getProvince());
					map.put("jzType", zp.getJzType());
					map.put("city", zp.getCity());
					map.put("education", zp.getEducation());
					map.put("welfare", zp.getWelfare().split(","));
					map.put("wage", zp.getWage());
					map.put("addTime", zp.getAddTime());
					map.put("checkStatus", zp.getCheckStatus());
					map.put("showStatus", zp.getShowStatus());
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, zps.getTotalElements(), status, list);
	}

	@GetMapping("/getSelfDriverZp")
	@ApiOperation(value = "获取自己发布的招聘信息", notes = "获取自己发布的招聘信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "当前登录用编号"),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过,-1：全部)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架，-1：全部）"),
	})
	public GenericResponse getSelfDriverZp(HttpServletRequest request) {
		String userId = CommonTools.getFinalStr("userId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkSta", request);
		Integer showSta = CommonTools.getFinalInteger("showSta", request);
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			if(userId.equals("")) {
				status = 10002;
			}else {
				List<DriverZp> zpList = zpService.listDriverZpByOpt(userId, checkStatus, showSta);
				if(zpList.size() > 0) {
					for (DriverZp zp : zpList) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("zpId", zp.getId());
						map.put("companyName", zp.getCompany().getName());
						map.put("age", zp.getSjAgeRange());
						map.put("jlYear", zp.getJlYearRange());
						map.put("province", zp.getProvince());
						map.put("num", zp.getNum());
						map.put("lxName", zp.getLxName());
						map.put("lxTel", zp.getLxTel());
						map.put("addTime", zp.getAddTime());
						map.put("checkStatus", zp.getCheckStatus());
						map.put("showStatus", zp.getShowStatus());
						list.add(map);
					}
				}else {
					status = 50001;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/getSelfDriverQz")
	@ApiOperation(value = "获取自己发布的求职信息", notes = "获取自己发布的求职信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "当前登录用编号")
	})
	public GenericResponse getSelfDriverQz(HttpServletRequest request) {
		String userId = CommonTools.getFinalStr("userId", request);
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			if(userId.equals("")) {
				status = 10002;
			}else {
				List<DriverQz> qzList = qzService.getDriverQzByUserId(userId);
				if(qzList.size() > 0) {
					for (DriverQz qz : qzList) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", qz.getId());
						map.put("userName", qz.getUserName());
						map.put("userMobile", qz.getUserMobile());
						map.put("userHead", qz.getUserHead());
						map.put("jzYear", qz.getJzYear());
						map.put("jzType", qz.getJzType());
						map.put("wage", qz.getWage());
						map.put("privince", qz.getProvince());
						map.put("city", qz.getCity());
						map.put("education", qz.getEducation());
						map.put("age", qz.getAge());
						map.put("sex", qz.getSex());
						map.put("workExp", qz.getWorkExp());
						map.put("workYear", qz.getWorkYear());
						map.put("colleges", qz.getColleges());
						map.put("marriage", qz.getMarriage());
						map.put("remark", qz.getRemark());
						map.put("showStatus", qz.getShowStatus());
						map.put("checkStatus", qz.getCheckStatus());
						list.add(map);
					}
				}else {
					status = 50001;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/getDriverZpById")
	@ApiOperation(value = "根据主键获取司机招聘详细信息", notes = "根据主键获取司机招聘详细信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 10002, message = "参数为空"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "司机招聘编号"),
			@ApiImplicitParam(name = "userId", value = "用户编号编号"), })
	public GenericResponse getDriverZpById(HttpServletRequest request) {
		Integer status = 200;
		String zpId = CommonTools.getFinalStr("id", request);
		String userId = CommonTools.getFinalStr("userId", request);
		List<Object> list = new ArrayList<Object>();
		try {
			if (zpId.equals("")) {
				status = 10002;
			} else {
				DriverZp zp = zpService.getEntityById(zpId);
				if (zp == null) {
					status = 10002;
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", zp.getId());
					map.put("companyName", zp.getCompany().getName());
					map.put("jzType", zp.getJzType());
					map.put("sjAge",zp.getSjAgeRange());
					map.put("jlYear",zp.getJlYearRange());
					map.put("province", zp.getProvince());
					map.put("city", zp.getCity());
					map.put("address", zp.getAddress());
					map.put("checkStatus", zp.getCheckStatus());
					map.put("checkTime", zp.getCheckTime());
					map.put("showStatus", zp.getShowStatus());
					map.put("userType", zp.getUserType());
					map.put("hot", zp.getHot());
					map.put("lxName", zp.getLxName());
					map.put("lxTel", zp.getLxTel());
					map.put("education",zp.getEducation());
					map.put("workYear", zp.getWorkYear());
					map.put("num", zp.getNum());
					map.put("welftare", zp.getWelfare().split(","));
					map.put("wage", zp.getWage());
					map.put("remark", zp.getRemark());
					String addUserId = zp.getAddUserId();
					map.put("adduserId", addUserId);
					if(!addUserId.equals("") && zp.getUserType() == 2) {
						User user = us.getEntityById(addUserId);
						if(user != null) {
							map.put("userHead", user.getUserPortrait());
						}
					}else {
						map.put("userHead", "");
					}
					String ufId = "";
					if (!userId.isEmpty()) {
						List<UserFocus> ufList = ufService.getUserFocusList(userId, zpId, "sjzp");
						if(ufList.size() > 0) {
							ufId = ufList.get(0).getId();
						}
					}
					map.put("ufId", ufId);
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

}
