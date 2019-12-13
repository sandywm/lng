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
import com.lng.pojo.CompanyPsr;
import com.lng.pojo.CompanyTructsGcCp;
import com.lng.pojo.CompanyTructsHeadCp;
import com.lng.pojo.CompanyType;
import com.lng.pojo.CompanyZz;
import com.lng.service.CompanyPsrService;
import com.lng.service.CompanyService;
import com.lng.service.CompanyTructsGcCpService;
import com.lng.service.CompanyTructsHeadCpService;
import com.lng.service.CompanyTypeService;
import com.lng.service.CompanyZzService;
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
	private CompanyTypeService ctService;
	@Autowired
	private CompanyPsrService psrService;
	@Autowired
	private CompanyTructsGcCpService tructsGcCpService;
	@Autowired
	private CompanyTructsHeadCpService tructsHeadCpService;
	@Autowired
	private CompanyZzService zzService;

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
			@ApiImplicitParam(name = "bankName", value = "公司银行名称", defaultValue = "华龙区银行"),
			@ApiImplicitParam(name = "bankNo", value = "公司银行卡号", defaultValue = "4565445445452218997"),
			@ApiImplicitParam(name = "bankAcc", value = "公司银行账户", defaultValue = "4565445445452218"), })
	public GenericResponse addCompany(HttpServletRequest request, String name, String typeId, String owerUserId,
			String province, String city, String county, String address, String lxname, String lxtel, String bankName,
			String bankNo, String bankAcc) {
		Integer status = 200;
		String comId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_COMPANY)) {
			try {
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
					comp.setBankName(CommonTools.getFinalStr(bankName));
					comp.setBankNo(CommonTools.getFinalStr(bankNo));
					comp.setBankAccount(CommonTools.getFinalStr(bankAcc));
					comId = companyService.saveOrUpdate(comp);
				} else {
					status = 50003;
				}

			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
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
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_PSR)) {
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
		} else {
			status = 70001;
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
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_GCCP)) {
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
		} else {
			status = 70001;
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
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_HEADCP)) {
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
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, cId);
	}

	@PostMapping("/addCompanyZz")
	@ApiOperation(value = "添加公司执照", notes = "添加公司执照")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "zzimg", value = "公司资质图片", defaultValue = "haha.img", required = true) })
	public GenericResponse addCompanyZz(HttpServletRequest request, String compId, String zzimg) {
		compId = CommonTools.getFinalStr(compId);
		zzimg = CommonTools.getFinalStr(zzimg);
		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;
		String zzId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_ZZ)) {
			try {
				CompanyZz zz = new CompanyZz();
				Company company = companyService.getEntityById(compId);
				zz.setCompany(company);
				if(!zzimg.equals("")) {//上传图
					zz.setCompanyZzImg(CommonTools.dealUploadDetail(loginUserId, zzimg));
				}
				zzId = zzService.saveOrUpdate(zz);
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
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
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryCompany(String typeId, String name, Integer checkSta, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<Company> coms = null;
		try {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			coms = companyService.getCompanyList(CommonTools.getFinalStr(name), CommonTools.getFinalStr(typeId),
					checkSta, pageNo - 1, pageSize);
			if (coms.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, coms.getTotalElements(), status, coms.getContent());
	}

	@GetMapping("/queryCompanyPsr")
	@ApiOperation(value = "获取公司押运人", notes = "获取公司押运人分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号"),
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryCompanyPsr(String compId, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<CompanyPsr> psrPage = null;
		try {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			psrPage = psrService.getCompanyPsrList(CommonTools.getFinalStr(compId), pageNo - 1, pageSize);
			if (psrPage.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, psrPage.getTotalElements(), status, psrPage.getContent());
	}

	@GetMapping("/queryCompanyGcCP")
	@ApiOperation(value = "获取公司挂车车牌", notes = "获取公司挂车车牌分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号"),
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryCompanyGcCP(String compId, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<CompanyTructsGcCp> gccpPage = null;
		try {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			gccpPage = tructsGcCpService.getTructsGcCpList(CommonTools.getFinalStr(compId), pageNo - 1, pageSize);
			if (gccpPage.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, gccpPage.getTotalElements(), status, gccpPage.getContent());
	}

	@GetMapping("/queryHeadCP")
	@ApiOperation(value = "获取公司车头车牌", notes = "获取公司车头车牌分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号"),
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryHeadCP(String compId, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<CompanyTructsHeadCp> hcpPage = null;
		try {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			hcpPage = tructsHeadCpService.getTructsHeadCpList(CommonTools.getFinalStr(compId), pageNo - 1, pageSize);
			if (hcpPage.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, hcpPage.getTotalElements(), status, hcpPage.getContent());
	}

	@GetMapping("/queryCompanyZz")
	@ApiOperation(value = "获取公司执照", notes = "获取公司执照分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号"),
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryCompanyZz(String compId, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<CompanyZz> zzPage = null;
		try {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			zzPage = zzService.getCompanyZzList(CommonTools.getFinalStr(compId), pageNo - 1, pageSize);
			if (zzPage.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, zzPage.getTotalElements(), status, zzPage.getContent());
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
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_COMPANY)) {
			try {
				Company comp = companyService.getEntityById(id);
				if (comp == null) {
					status = 50001;
				} else {
					if(checkSta!= null && !checkSta.equals(comp.getCheckStatus())) {
						comp.setCheckStatus(checkSta);
						comp.setCheckTime(CurrentTime.getCurrentTime());
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
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司编号", required = true),
			@ApiImplicitParam(name = "typeId", value = "公司类型编号", required = true),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南"),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳"),
			@ApiImplicitParam(name = "county", value = "县", defaultValue = "华龙区"),
			@ApiImplicitParam(name = "address", value = "地址", defaultValue = "大庆路110号"),
			@ApiImplicitParam(name = "lxname", value = "联系人", defaultValue = "小哈"),
			@ApiImplicitParam(name = "lxtel", value = "联系电话", defaultValue = "18795121221"),
			@ApiImplicitParam(name = "bankName", value = "公司银行名称", defaultValue = "华龙区银行"),
			@ApiImplicitParam(name = "bankNo", value = "公司银行卡号", defaultValue = "4565445445452218997"),
			@ApiImplicitParam(name = "bankAcc", value = "公司银行账户", defaultValue = "4565445445452218"), })
	public GenericResponse updateCompany(HttpServletRequest request, String id, String typeId, String province,
			String city, String county, String address, String lxname, String lxtel, String bankName, String bankNo,
			String bankAcc) {
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
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_COMPANY)) {
			try {
				Company comp = companyService.getEntityById(id);
				if (comp == null) {
					status = 50001;
				} else {
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
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
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
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_PSR)) {
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
		} else {
			status = 70001;
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
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_GCCP)) {
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
		} else {
			status = 70001;
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
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_HEADCP)) {
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
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateCompanyZz")
	@ApiOperation(value = "更新公司执照", notes = "更新公司执照")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司执照主键"),
			@ApiImplicitParam(name = "zzImg", value = "公司资质图片", defaultValue = "demo.img"),

	})
	public GenericResponse updateCompanyZz(HttpServletRequest request, String id, String zzImg) {
		Integer status = 200;
		zzImg = CommonTools.getFinalStr(zzImg);
		id = CommonTools.getFinalStr(id);
		String loginUserId = CommonTools.getLoginUserId(request);
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_ZZ)) {
			try {
				CompanyZz zz = zzService.getEntityById(id);
				if (zz == null) {
					status = 50001;
				} else {
					if (!zzImg.isEmpty() && !zzImg.equals(zz.getCompanyZzImg())) {
						zz.setCompanyZzImg(CommonTools.dealUploadDetail(loginUserId, zzImg));
					}
					zzService.saveOrUpdate(zz);
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

}
