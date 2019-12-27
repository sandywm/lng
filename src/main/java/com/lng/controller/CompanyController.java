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
import com.lng.pojo.CompanyPsr;
import com.lng.pojo.CompanyTructsGcCp;
import com.lng.pojo.CompanyTructsHeadCp;
import com.lng.pojo.CompanyType;
import com.lng.pojo.CompanyZz;
import com.lng.pojo.GasFactory;
import com.lng.pojo.GasFactoryCompany;
import com.lng.pojo.MessageCenter;
import com.lng.pojo.User;
import com.lng.pojo.UserCompany;
import com.lng.service.CompanyPsrService;
import com.lng.service.CompanyService;
import com.lng.service.CompanyTructsGcCpService;
import com.lng.service.CompanyTructsHeadCpService;
import com.lng.service.CompanyTypeService;
import com.lng.service.CompanyZzService;
import com.lng.service.GasFactoryCompanyService;
import com.lng.service.GasFactoryService;
import com.lng.service.MessageCenterService;
import com.lng.service.UserCompanyService;
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
@Api(tags = "公司相关接口")
@RequestMapping("/company")
public class CompanyController {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private UserService us;
	@Autowired
	private UserCompanyService ucs;
	@Autowired
	private CompanyTypeService ctService;
	@Autowired
	private CompanyPsrService psrService;
	@Autowired
	private CompanyTructsGcCpService tructsGcCpService;
	@Autowired
	private CompanyTructsHeadCpService tructsHeadCpService;
	@Autowired
	private CompanyZzService zzService;
	@Autowired
	private GasFactoryCompanyService gfcs;
	@Autowired
	private GasFactoryService gfs;
	@Autowired
	private MessageCenterService mcs;

	@PostMapping("/addCompany")
	@ApiOperation(value = "添加公司", notes = "添加公司")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "公司名称", defaultValue = "公司名称测试", required = true),
			@ApiImplicitParam(name = "typeId", value = "公司类型编号", defaultValue = "97ce2488-1027-4bfe-9eef-335cb1391d6", required = true),
			@ApiImplicitParam(name = "owerUserId", value = "公司所属人编号", defaultValue = "公司所属人编号", required = true),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南"),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳"),
			@ApiImplicitParam(name = "county", value = "县", defaultValue = "华龙区"),
			@ApiImplicitParam(name = "address", value = "地址", defaultValue = "大庆路110号"),
			@ApiImplicitParam(name = "lxname", value = "联系人", defaultValue = "小哈"),
			@ApiImplicitParam(name = "lxtel", value = "联系电话", defaultValue = "18795121221"),
			@ApiImplicitParam(name = "yyzzImg", value = "营业执照"),
			@ApiImplicitParam(name = "bankName", value = "公司银行名称", defaultValue = "华龙区银行"),
			@ApiImplicitParam(name = "bankNo", value = "公司银行卡号", defaultValue = "4565445445452218997"),
			@ApiImplicitParam(name = "bankAcc", value = "公司银行账户", defaultValue = "4565445445452218"),
			//@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）"),
			@ApiImplicitParam(name = "zzImg", value = "公司资质图片") })
	public GenericResponse addCompany(HttpServletRequest request, String name, String typeId, String owerUserId,
			String province, String city, String county, String address, String lxname, String lxtel, String yyzzImg,
			String bankName, String bankNo, String bankAcc, /* Integer userType, */ String zzImg) {
		Integer status = 200;
		String loginUserId = CommonTools.getLoginUserId(request);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		zzImg = CommonTools.getFinalStr(zzImg);
		String comId = "";
		Integer userType =1;
		try {
			if (!CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),
					Constants.ADD_COMPANY)) {
				
			} else if (cilentInfo.equals("wxApp")) {
				userType =2;
				loginUserId = CommonTools.getFinalStr("owerUserId", request);
			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				if (companyService.getCompanyByName(name).size() == 0) {
					Company comp = new Company();
					comp.setName(CommonTools.getFinalStr(name));
					comp.setAddTime(CurrentTime.getCurrentTime());
					CompanyType ct = ctService.findById(CommonTools.getFinalStr(typeId));
					comp.setCompanyType(ct);
					comp.setOwerUserId(CommonTools.getFinalStr(owerUserId));
					comp.setCheckStatus(0);
					comp.setProvince(CommonTools.getFinalStr(province));
					comp.setCity(CommonTools.getFinalStr(city));
					comp.setCounty(CommonTools.getFinalStr(county));
					comp.setAddress(CommonTools.getFinalStr(address));
					comp.setLxName(CommonTools.getFinalStr(lxname));
					comp.setLxTel(CommonTools.getFinalStr(lxtel));
					if (!yyzzImg.equals("")) {
						comp.setYyzzImg(CommonTools.dealUploadDetail(loginUserId, "", yyzzImg));
					}
					comp.setBankName(CommonTools.getFinalStr(bankName));
					comp.setBankNo(CommonTools.getFinalStr(bankNo));
					comp.setBankAccount(CommonTools.getFinalStr(bankAcc));
				
					comp.setUserType(userType);
					if (userType.equals(1)) {
						comp.setCheckStatus(1);
						comp.setCheckTime(CurrentTime.getCurrentTime());
					} else {
						comp.setCheckStatus(0);
						comp.setCheckTime("");
					}
					comId = companyService.saveOrUpdate(comp);
					if (!zzImg.equals("")) {
						String zzImgPath = CommonTools.dealUploadDetail(loginUserId, "", zzImg);
						String[] pathLen = zzImgPath.split(",");
						List<CompanyZz> zzList = new ArrayList<>();
						for (int i = 0; i < pathLen.length; i++) {
							CompanyZz zz = new CompanyZz();
							zz.setCompany(comp);
							String zzi = pathLen[i];
							zz.setCompanyZzImg(zzi);
							zzList.add(zz);
						}
						zzService.saveOrUpdateBatch(zzList);
					}
					//将自己加入到公司员工关联
					ucs.addOrUpdate(new UserCompany(comp, us.getEntityById(loginUserId), CurrentTime.getCurrentTime(),1,CurrentTime.getCurrentTime()));
				} else {
					status = 50003;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}

		return ResponseFormat.retParam(status, comId);
	}

	@PostMapping("/addCompanyPsr")
	@ApiOperation(value = "添加公司司机押运人", notes = "添加公司司机押运人")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "name", value = "姓名", defaultValue = "小哈哈", required = true),
			@ApiImplicitParam(name = "sex", value = "性别", defaultValue = "男", required = true),
			@ApiImplicitParam(name = "mobile", value = "电话", defaultValue = "0393-8563425", required = true) })
	public GenericResponse addCompanyPsr(HttpServletRequest request, String compId, String name, String sex,
			String mobile) {
		compId = CommonTools.getFinalStr(compId);
		Integer status = 200;
		String psrId = "";
		try {
			CompanyPsr psr = new CompanyPsr();
			Company company = companyService.getEntityById(compId);
			psr.setCompany(company);
			psr.setName(CommonTools.getFinalStr(name));
			psr.setSex(CommonTools.getFinalStr(sex));
			psr.setMobile(CommonTools.getFinalStr(mobile));
			psrId = psrService.saveOrUpdate(psr);
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, psrId);
	}

	@PostMapping("/addTrucksGcCp")
	@ApiOperation(value = "添加挂车车牌", notes = "添加挂车车牌")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "gch", value = "挂车车牌号", defaultValue = "豫jkl228", required = true) })
	public GenericResponse addTrucksGcCp(HttpServletRequest request, String compId, String gch) {
		compId = CommonTools.getFinalStr(compId);
		Integer status = 200;
		String gcId = "";
		try {
			gch = CommonTools.getFinalStr(gch);
			if (tructsGcCpService.getGcCpByName(gch.toUpperCase()).size() == 0) {
				CompanyTructsGcCp gccp = new CompanyTructsGcCp();
				Company company = companyService.getEntityById(compId);
				gccp.setCompany(company);
				if (!CommonTools.getFinalStr(gch).equals("")) {
					gccp.setTrucksGch(gch.toUpperCase());
				} else {
					gccp.setTrucksGch("");
				}
				gcId = tructsGcCpService.saveOrUpdate(gccp);
			} else {
				status = 50003;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gcId);
	}

	@PostMapping("/addTrucksHeadCp")
	@ApiOperation(value = "添加车头车牌", notes = "添加车头车牌")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "cp", value = "车头车牌号", defaultValue = "豫jkl258", required = true) })
	public GenericResponse addTrucksHeadCp(HttpServletRequest request, String compId, String cp) {
		compId = CommonTools.getFinalStr(compId);
		Integer status = 200;
		String cId = "";
		try {
			cp = CommonTools.getFinalStr(cp);
			if (tructsHeadCpService.getHeadCpList(cp.toUpperCase()).size() == 0) {
				CompanyTructsHeadCp headCp = new CompanyTructsHeadCp();
				Company company = companyService.getEntityById(compId);
				headCp.setCompany(company);
				if (!CommonTools.getFinalStr(cp).equals("")) {
					headCp.setTrucksCp(cp.toUpperCase());
				} else {
					headCp.setTrucksCp("");
				}
				cId = tructsHeadCpService.saveOrUpdate(headCp);
			} else {
				status = 50003;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, cId);
	}

	@PostMapping("/addOrUpdateCompanyZz")
	@ApiOperation(value = "添加公司资质", notes = "添加公司资质")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "zzimg", value = "公司资质图片(多张图片逗号分隔)", defaultValue = "haha.img,tt.img,", required = true) })
	public GenericResponse addOrUpdateCompanyZz(HttpServletRequest request, String compId, String zzimg) {
		compId = CommonTools.getFinalStr(compId);
		zzimg = CommonTools.getFinalStr(zzimg);
		String loginUserId = CommonTools.getLoginUserId(request);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;
		String zzId = "";
		String zzImgPath = "";

		try {
			if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),
					Constants.ADD_COMPANY)) {

			} else if (cilentInfo.equals("wxApp")) {

			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				List<CompanyZz> zzs = zzService.getCompanyZzList(compId);
				if (zzs != null) {
					zzService.deleteBatch(zzs);
				}
				Company company = companyService.getEntityById(compId);
				if (!zzimg.equals("")) {
					zzImgPath = CommonTools.dealUploadDetail(loginUserId, "", zzimg);
					String[] pathLen = zzImgPath.split(",");
					List<CompanyZz> zzList = new ArrayList<>();
					for (int i = 0; i < pathLen.length; i++) {
						CompanyZz zz = new CompanyZz();
						zz.setCompany(company);
						String zzi = pathLen[i];
						zz.setCompanyZzImg(zzi);
						zzList.add(zz);
					}
					zzService.saveOrUpdateBatch(zzList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}

		return ResponseFormat.retParam(status, zzId);
	}

	@GetMapping("/queryCompany")
	@ApiOperation(value = "获取公司", notes = "获取公司分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "typeId", value = "公司类型编号"),
			@ApiImplicitParam(name = "name", value = "公司名称"),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(-1 全部,0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "page", value = "第几页"), @ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse queryCompany(String typeId, String name, Integer checkSta, Integer page, Integer limit) {
		Integer status = 200;
		Page<Company> coms = null;
		long count = 0;
		List<Object> list = new ArrayList<Object>();
		try {
			if (page == null) {
				page = 1;
			}
			if (limit == null) {
				limit = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			coms = companyService.getCompanyList(CommonTools.getFinalStr(name), CommonTools.getFinalStr(typeId),
					checkSta, page - 1, limit);
			count = coms.getTotalElements();
			if (count == 0) {
				status = 50001;
			}else {
				for(Company cpy : coms) {
					Map<String,Object> map_d = new HashMap<String,Object>();
					map_d.put("id", cpy.getId());
					map_d.put("name", cpy.getName());
					map_d.put("checkStatus", cpy.getCheckStatus());
					map_d.put("companyType", cpy.getCompanyType().getName());
					map_d.put("lxName", cpy.getLxName());
					map_d.put("lxTel", cpy.getLxTel());
					map_d.put("address", cpy.getAddress());
					map_d.put("province", cpy.getProvince());
					map_d.put("city", cpy.getCity());
					map_d.put("county", cpy.getCounty());
					map_d.put("addTime", cpy.getAddTime());
					list.add(map_d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, count, status, list);
	}

	@GetMapping("/getCompanyList")
	@ApiOperation(value = "根据公司类型，公司人员获取公司列表", notes = "根据公司类型，公司人员获取公司列表，后台用户用的时候不需要传")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
		@ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "typeId", value = "公司类型编号"),
		@ApiImplicitParam(name = "typeName", value = "公司类型名称"),
		@ApiImplicitParam(name = "userId", value = "员工编号"),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态(,-1：全部,0:未审核,1:审核通过,2:审核未通过)"),
		@ApiImplicitParam(name = "opt", value = "状态（0：后台用，1：前台用）"),
	})
	public GenericResponse getCompanyList(HttpServletRequest request) {
		Integer status = 200;
		String cpyTypeId = CommonTools.getFinalStr("typeId", request);
		String cpyTypeName = CommonTools.getFinalStr("typeName", request);
		String userId = CommonTools.getFinalStr("userId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		Integer opt = CommonTools.getFinalInteger("opt", request);
		List<Object> list = new ArrayList<Object>();
		try {
			if(opt.equals(0)) {
				List<Company> cList = companyService.listSpecCpy(cpyTypeId, cpyTypeName);
				if(cList.size() > 0) {
					for(Company cpy : cList) {
						Map<String, String> map_d = new HashMap<String, String>();
						map_d.put("cpyId", cpy.getId());
						map_d.put("cpyName", cpy.getName());
						list.add(map_d);
					}
				}else {
					status = 50001;
				}
			}else {
				List<UserCompany> ucList = ucs.getUserCompanyListByOpt(cpyTypeId, cpyTypeName, checkStatus, userId);
				if(ucList.size() > 0) {
					for(UserCompany uc : ucList) {
						Map<String, String> map_d = new HashMap<String, String>();
						Company cpy = uc.getCompany();
						map_d.put("cpyId", cpy.getId());
						map_d.put("cpyName", cpy.getName());
						list.add(map_d);
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
	
	@GetMapping("/queryCompanyEmployee")
	@ApiOperation(value = "获取公司员工", notes = "获取公司员工")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号") })
	public GenericResponse queryCompanyEmployee(HttpServletRequest request) {
		Integer status = 200;
		String compId = CommonTools.getFinalStr("compId", request);
		List<Object> list = new ArrayList<Object>();
		try {
			List<UserCompany> ucList = ucs.getUserCompanyList(compId, "");
			if (ucList.size() == 0) {
				status = 50001;
			} else {
				for (UserCompany uc : ucList) {
					Map<String, String> map_d = new HashMap<String, String>();
					User user = uc.getUser();
					map_d.put("userId", user.getId());
					map_d.put("userHead", user.getUserPortrait());
					map_d.put("wxName", user.getWxName());
					map_d.put("sex", user.getSex());
					map_d.put("userName", user.getRealName());
					map_d.put("userMobile", user.getMobile());
					list.add(map_d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/queryCompanyPsr")
	@ApiOperation(value = "获取公司押运人", notes = "获取公司押运人信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号") })
	public GenericResponse queryCompanyPsr(HttpServletRequest request) {
		Integer status = 200;
		String compId = CommonTools.getFinalStr("compId", request);
		List<Object> list = new ArrayList<Object>();
		try {
			List<CompanyPsr> psrList = psrService.getCompanyPsrList(compId);
			if (psrList.size() == 0) {
				status = 50001;
			} else {
				for (CompanyPsr psr : psrList) {
					Map<String, String> map_d = new HashMap<String, String>();
					map_d.put("psrId", psr.getId());
					map_d.put("psrName", psr.getName());
					map_d.put("psrSex", psr.getSex());
					map_d.put("psrMobile", psr.getMobile());
					list.add(map_d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

	@GetMapping("/queryCompanyGcCP")
	@ApiOperation(value = "获取公司挂车车牌", notes = "获取公司挂车车牌信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号") })
	public GenericResponse queryCompanyGcCP(HttpServletRequest request) {
		Integer status = 200;
		String compId = CommonTools.getFinalStr("compId", request);
		List<Object> list = new ArrayList<Object>();
		try {
			List<CompanyTructsGcCp> cpyGccpList = tructsGcCpService.getTructsGcCpList(compId);
			if (cpyGccpList.size() == 0) {
				status = 50001;
			} else {
				for (CompanyTructsGcCp cp : cpyGccpList) {
					Map<String, String> map_d = new HashMap<String, String>();
					map_d.put("cph", cp.getTrucksGch());
					list.add(map_d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

	@GetMapping("/queryHeadCP")
	@ApiOperation(value = "获取公司车头车牌", notes = "获取公司车头车牌信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号") })
	public GenericResponse queryHeadCP(HttpServletRequest request) {
		Integer status = 200;
		String compId = CommonTools.getFinalStr("compId", request);
		List<Object> list = new ArrayList<Object>();
		try {
			List<CompanyTructsHeadCp> headCpList = tructsHeadCpService.getTructsHeadCpList(compId);
			if (headCpList.size() == 0) {
				status = 50001;
			} else {
				for (CompanyTructsHeadCp cp : headCpList) {
					Map<String, String> map_d = new HashMap<String, String>();
					map_d.put("cph", cp.getTrucksCp());
					list.add(map_d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

	@PutMapping("/updateCompByStatus")
	@ApiOperation(value = "更新公司审核状态", notes = "更新公司审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司编号", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)", required = true) })
	public GenericResponse updateCompByStatus(HttpServletRequest request, String id, Integer checkSta) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),
				Constants.CHECK_CPY_APPLY)) {
			try {
				Company comp = companyService.getEntityById(id);
				if (comp == null) {
					status = 50001;
				} else {
					if (checkSta != null && !checkSta.equals(comp.getCheckStatus())) {
						comp.setCheckStatus(checkSta);
						comp.setCheckTime(CurrentTime.getCurrentTime());
						String result = "未审核通过";
						if(checkSta.equals(1)) {
							result = "审核通过";
						}
						MessageCenter mc = new MessageCenter("您提交的"+comp.getName()+"公司的申请"+result, "您提交的"+comp.getName()+"公司的申请"+result, 0, CurrentTime.getCurrentTime(), 2,
								id, "addCpy", "", comp.getOwerUserId(), 0);
						mcs.saveOrUpdate(mc);
					}
					companyService.saveOrUpdate(comp);
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

	@PutMapping("/updateCompany")
	@ApiOperation(value = "更新公司", notes = "更新公司信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),@ApiResponse(code = 80001, message = "审核通过不能修改"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司编号", required = true),
			@ApiImplicitParam(name = "typeId", value = "公司类型编号", required = true),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南"),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳"),
			@ApiImplicitParam(name = "county", value = "县", defaultValue = "华龙区"),
			@ApiImplicitParam(name = "address", value = "地址", defaultValue = "大庆路110号"),
			@ApiImplicitParam(name = "lxname", value = "联系人", defaultValue = "小哈"),
			@ApiImplicitParam(name = "lxtel", value = "联系电话", defaultValue = "18795121221"),
			@ApiImplicitParam(name = "yyzzImg", value = "营业执照图"),
			@ApiImplicitParam(name = "bankName", value = "公司银行名称", defaultValue = "华龙区银行"),
			@ApiImplicitParam(name = "bankNo", value = "公司银行卡号", defaultValue = "4565445445452218997"),
			@ApiImplicitParam(name = "bankAcc", value = "公司银行账户", defaultValue = "4565445445452218"), })
	public GenericResponse updateCompany(HttpServletRequest request, String id, String typeId, String province,
			String city, String county, String address, String lxname, String lxtel, String yyzzImg, String bankName,
			String bankNo, String bankAcc) {
		id = CommonTools.getFinalStr(id);
		province = CommonTools.getFinalStr(province);
		city = CommonTools.getFinalStr(city);
		county = CommonTools.getFinalStr(county);
		address = CommonTools.getFinalStr(address);
		lxname = CommonTools.getFinalStr(lxname);
		lxtel = CommonTools.getFinalStr(lxtel);
		bankName = CommonTools.getFinalStr(bankName);
		bankNo = CommonTools.getFinalStr(bankNo);
		bankAcc = CommonTools.getFinalStr(bankAcc);
		String loginUserId = CommonTools.getLoginUserId(request);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;

		try {
			if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),
					Constants.UP_COMPANY)) {
			} else if (cilentInfo.equals("wxApp")) {

			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				Company comp = companyService.getEntityById(id);
				if (comp == null) {
					status = 50001;
				} else if(comp.getCheckStatus()==1){
					status = 80001;
				}else {
					CompanyType ct = ctService.findById(CommonTools.getFinalStr(typeId));
					comp.setCompanyType(ct);
					if (!province.equals("") && !province.equals(comp.getProvince())) {
						comp.setProvince(province);
					}
					if (!city.equals("") && !city.equals(comp.getCity())) {
						comp.setCity(city);
					}
					if (!county.equals("") && !county.equals(comp.getCounty())) {
						comp.setCounty(county);
					}
					if (!address.equals("") && !address.equals(comp.getAddress())) {
						comp.setAddress(address);
					}
					if (!lxname.equals("") && !lxname.equals(comp.getLxName())) {
						comp.setLxName(lxname);
					}
					if (!lxtel.equals("") && !lxtel.equals(comp.getLxTel())) {
						comp.setLxTel(lxtel);
					}
					if (!yyzzImg.isEmpty() && !yyzzImg.equals(comp.getYyzzImg()) && comp.getCheckStatus() == 1) {
						comp.setYyzzImg(CommonTools.dealUploadDetail(loginUserId, "", yyzzImg));
					}
					if (!bankName.equals("") && !bankName.equals(comp.getBankName())) {
						comp.setBankName(bankName);
					}
					if (!bankNo.equals("") && !bankNo.equals(comp.getBankNo())) {
						comp.setBankNo(bankNo);
					}
					if (!bankAcc.equals("") && !bankAcc.equals(comp.getBankAccount())) {
						comp.setBankAccount(bankAcc);
					}
					companyService.saveOrUpdate(comp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}

		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateCompanyPsr")
	@ApiOperation(value = "更新公司押运人", notes = "更新公司押运人信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司押运人主键"),
			@ApiImplicitParam(name = "name", value = "姓名", defaultValue = "小宝", required = true),
			@ApiImplicitParam(name = "sex", value = "性别", defaultValue = "女", required = true),
			@ApiImplicitParam(name = "mobile", value = "电话", defaultValue = "038310168", required = true) })
	public GenericResponse updateCompanyPsr(HttpServletRequest request, String id, String name, String sex,
			String mobile) {
		Integer status = 200;
		id = CommonTools.getFinalStr(id);
		name = CommonTools.getFinalStr(name);
		sex = CommonTools.getFinalStr(sex);
		mobile = CommonTools.getFinalStr(mobile);
		try {
			CompanyPsr psr = psrService.getEntityById(id);
			if (psr == null) {
				status = 50001;
			} else {
				if (!name.isEmpty() && !name.equals(psr.getName())) {
					psr.setName(name);
				}
				if (!sex.isEmpty() && !sex.equals(psr.getSex())) {
					psr.setSex(sex);
				}
				if (mobile.isEmpty() && !mobile.equals(psr.getMobile())) {
					psr.setMobile(mobile);
				}
				psrService.saveOrUpdate(psr);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateCompanyGcCp")
	@ApiOperation(value = "更新公司挂车车牌", notes = "更新公司挂车车牌")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司挂车车牌主键"),
			@ApiImplicitParam(name = "gch", value = "挂车车牌号", defaultValue = "鲁jdk226"),

	})
	public GenericResponse updateCompanyGcCp(HttpServletRequest request, String id, String gch) {
		Integer status = 200;
		gch = CommonTools.getFinalStr(gch);
		id = CommonTools.getFinalStr(id);
		try {
			CompanyTructsGcCp gccp = tructsGcCpService.getEntityById(id);
			if (gccp == null) {
				status = 50001;
			} else {
				if (!gch.isEmpty() && !gch.equals(gccp.getTrucksGch())) {
					gccp.setTrucksGch(gch);
				}
				tructsGcCpService.saveOrUpdate(gccp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateHeadCp")
	@ApiOperation(value = "更新公司车头车牌", notes = "更新公司车头车牌")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司车头车牌主键"),
			@ApiImplicitParam(name = "cp", value = "车头车牌", defaultValue = "鲁jdk229"),

	})
	public GenericResponse updateHeadCp(HttpServletRequest request, String id, String cp) {
		Integer status = 200;
		cp = CommonTools.getFinalStr(cp);
		id = CommonTools.getFinalStr(id);
		try {
			CompanyTructsHeadCp headCp = tructsHeadCpService.getEntityById(id);
			if (headCp == null) {
				status = 50001;
			} else {
				if (!cp.isEmpty() && !cp.equals(headCp.getTrucksCp())) {
					headCp.setTrucksCp(cp);
				}
				tructsHeadCpService.saveOrUpdate(headCp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateCompanyZz")
	@ApiOperation(value = "更新公司执照", notes = "更新公司执照")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司执照主键"),
			@ApiImplicitParam(name = "zzImg", value = "公司资质图片(多张图片逗号分隔)", defaultValue = "demo.img"),

	})
	public GenericResponse updateCompanyZz(HttpServletRequest request, String id, String zzImg) {
		Integer status = 200;
		zzImg = CommonTools.getFinalStr(zzImg);
		id = CommonTools.getFinalStr(id);
		String loginUserId = CommonTools.getLoginUserId(request);
		try {
			CompanyZz zz = zzService.getEntityById(id);
			if (zz == null) {
				status = 50001;
			} else {
				if (!zzImg.isEmpty() && !zzImg.equals(zz.getCompanyZzImg())) {
					zz.setCompanyZzImg(CommonTools.dealUploadDetail(loginUserId, zz.getCompanyZzImg(), zzImg));
				}
				zzService.saveOrUpdate(zz);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/getSpecCompanyDetail")
	@ApiOperation(value = "获取指定公司基本信息", notes = "获取指定公司基本信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号", required = true) })
	public GenericResponse getSpecCompanyDetail(HttpServletRequest request) {
		Integer status = 200;
		String compId = CommonTools.getFinalStr("compId", request);
		List<Object> list = new ArrayList<Object>();
		if (compId.equals("")) {
			status = 50001;
		} else {
			try {
				Company cpy = companyService.getEntityById(compId);
				if (cpy == null) {
					status = 50001;
				} else {
					Map<String, Object> map_d = new HashMap<String, Object>();
					map_d.put("compId", cpy.getId());
					map_d.put("cpyName", cpy.getName());
					map_d.put("cptTypeName", cpy.getCompanyType().getName());
					map_d.put("provName", cpy.getProvince());
					map_d.put("city", cpy.getCity());
					map_d.put("county", cpy.getCounty());
					map_d.put("address", cpy.getAddress());
					map_d.put("lxName", cpy.getLxName());
					map_d.put("lxTel", cpy.getLxTel());
					map_d.put("bankName", cpy.getBankName());
					map_d.put("bankNo", cpy.getBankNo());
					map_d.put("bankAccount", cpy.getBankAccount());
					map_d.put("yyzzImg", cpy.getYyzzImg());
					// 获取公司执照
					List<CompanyZz> czList = zzService.getCompanyZzList(compId);
					List<Object> list_d1 = new ArrayList<Object>();
					if (czList.size() > 0) {
						for (CompanyZz cz : czList) {
							Map<String, Object> map_d1 = new HashMap<String, Object>();
							map_d1.put("czId", cz.getId());
							map_d1.put("czImage", cz.getCompanyZzImg());
							list_d1.add(map_d1);
						}
					}
					map_d.put("zzImageList", list_d1);
					list.add(map_d);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		}
		return ResponseFormat.retParam(status, list);
	}

	@GetMapping("getSpecGasFactoryCpy")
	@ApiOperation(value = "获取指定主键的液厂的贸易商信息", notes = "获取指定主键的液厂详细信息(审核通过的)")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gfId", value = "液厂编号", required = true) })
	public GenericResponse getSpecGasFactoryCpy(HttpServletRequest request) {
		Integer status = 200;
		String gfId = CommonTools.getFinalStr("gfId", request);
		List<Object> list = new ArrayList<Object>();
		try {
			if (gfId.equals("")) {
				status = 50001;
			} else {
				List<GasFactoryCompany> gfcList = gfcs.listCompanyByGfId(gfId, "", 1);
				if (gfcList.size() > 0) {
					for (GasFactoryCompany gfCpy : gfcList) {
						Company cpy = gfCpy.getCompany();
						Map<String, Object> map_d = new HashMap<String, Object>();
						map_d.put("compId", cpy.getId());
						map_d.put("cpyName", cpy.getName());
						map_d.put("cptTypeName", cpy.getCompanyType().getName());
						map_d.put("provName", cpy.getProvince());
						map_d.put("city", cpy.getCity());
						map_d.put("county", cpy.getCounty());
						map_d.put("address", cpy.getAddress());
						map_d.put("lxName", cpy.getLxName());
						map_d.put("lxTel", cpy.getLxTel());
						map_d.put("bankName", cpy.getBankName());
						map_d.put("bankNo", cpy.getBankNo());
						map_d.put("bankAccount", cpy.getBankAccount());
						map_d.put("checkStatus", gfCpy.getCheckStatus());
						// 获取公司执照
//						List<CompanyZz> czList = zzService.getCompanyZzList(cpy.getId());
//						List<Object> list_d1 = new ArrayList<Object>();
//						if (czList.size() > 0) {
//							for (CompanyZz cz : czList) {
//								Map<String, Object> map_d1 = new HashMap<String, Object>();
//								map_d1.put("czId", cz.getId());
//								map_d1.put("czImage", cz.getCompanyZzImg());
//								list_d1.add(map_d1);
//							}
//						}
//						map_d.put("zzImageList", list_d1);
						list.add(map_d);
					}
				} else {
					status = 50001;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

	@GetMapping("getPageFactoryCpy")
	@ApiOperation(value = "获取液厂的贸易商关联信息--审核用", notes = "获取液厂的贸易商关联信息--审核用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gfName", value = "液厂名称"),
			@ApiImplicitParam(name = "gfNamePy", value = "液厂名称首字母"),
			@ApiImplicitParam(name = "checkStatus", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"), })
	public PageResponse getPageFactoryCpy(HttpServletRequest request) {
		Integer status = 200;
		String gfName = CommonTools.getFinalStr("gfName", request);
		String gfNamePy = CommonTools.getFinalStr("gfNamePy", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		Integer pageNo = CommonTools.getFinalInteger("pageNo", request);
		Integer pageSize = CommonTools.getFinalInteger("pageSize", request);
		if (pageNo.equals(0)) {
			pageNo = 1;
		}
		if (pageSize.equals(0)) {
			pageSize = 10;
		}
		long count = 0;
		List<Object> list = new ArrayList<Object>();
		try {
			Page<GasFactoryCompany> gfcList = gfcs.listPageCompanyByOpt(gfName, gfNamePy, checkStatus, pageNo,
					pageSize);
			count = gfcList.getTotalElements();
			if (count > 0) {
				for (GasFactoryCompany gfCpy : gfcList) {
					Company cpy = gfCpy.getCompany();
					Map<String, Object> map_d = new HashMap<String, Object>();
					map_d.put("gfcId", gfCpy.getId());
					map_d.put("cpyId", cpy.getId());
					map_d.put("cpyName", cpy.getName());
					map_d.put("gasFactoryName", gfCpy.getGasFactory().getName());
					map_d.put("applyTime", gfCpy.getAddTime());
					map_d.put("checkStatus", gfCpy.getCheckStatus());
					map_d.put("checkTime", gfCpy.getCheckTime());
					list.add(map_d);
				}
			} else {
				status = 50001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, count, status, list);
	}
	
	@PostMapping("/joinGasFactorApply")
	@ApiOperation(value = "贸易公司申请加入液厂贸易商", notes = "贸易公司申请加入液厂贸易商(必须是审核通过的贸易商和液厂才能进行申请)")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 10002, message = "参数为空"),
			@ApiResponse(code = 50003, message = "数据已存在")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "gfId", value = "液厂编号", required = true),
			@ApiImplicitParam(name = "userId", value = "申请人", required = true)
	})
	public GenericResponse joinGasFactorApply(HttpServletRequest request) {
		Integer status = 200;
		String gfcId = "";
		String compId = CommonTools.getFinalStr("compId", request);
		String gfId = CommonTools.getFinalStr("gfId", request);
		String userId = CommonTools.getFinalStr("userId", request);
		if(compId.equals("") || gfId.equals("")) {
			status = 10002;
		}else {
			try {
				if(gfcs.listCompanyByGfId(gfId, compId, 1).size() == 0) {
					Company c = companyService.getEntityById(compId);
					GasFactory gf = gfs.getEntityById(gfId);
					if(c != null || gf != null) {
						if(c.getCheckStatus() == 1 && gf.getCheckStatus() == 1) {
							gfcId = gfcs.saveOrUpdate(new GasFactoryCompany(c, gf, userId,CurrentTime.getCurrentTime(),0,""));
						}else {
							status = 50001;
						}
					}else {
						status = 50001;
					}
				}else {//已存在审核通过的申请，不能再进行申请
					status = 50003;
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		}
		return ResponseFormat.retParam(status, gfcId);
	}
}
