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
import com.lng.pojo.CompanyType;
import com.lng.pojo.RqDevType1;
import com.lng.service.CompanyService;
import com.lng.service.CompanyTypeService;
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

	@GetMapping("/queryCompany")
	@ApiOperation(value = "获取公司", notes = "获取公司分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "typeId", value = "公司类型编号"),
			@ApiImplicitParam(name = "name", value = "公司名称"), 
			@ApiImplicitParam(name = "checkSta", value = "审核状态(-1 全部,0:未审核,1:审核通过,2:审核未通过)"), 
			@ApiImplicitParam(name = "pageNo", value = "第几页"),
			@ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryCompany(String typeId, String name, Integer checkSta, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<Company> coms = null;
		try {
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 2;
			}
			if(checkSta == null) {
				checkSta = -1;
			}
			coms = companyService.getCompanyList(CommonTools.getFinalStr(name), CommonTools.getFinalStr(typeId),checkSta,
					pageNo - 1, pageSize);
			if (coms.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, coms.getTotalElements(), status, coms.getContent());
	}

	@PutMapping("/updateCompByStatus")
	@ApiOperation(value = "更新公司审核状态", notes = "更新公司审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司编号", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态", required = true) })
	public GenericResponse updateCompByStatus(HttpServletRequest request, String id, int checkSta) {

		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_COMPANY)) {
			try {
				Company comp = companyService.getEntityById(id);
				if (comp == null) {
					status = 50001;
				} else {
					comp.setCheckStatus(CommonTools.getFinalInteger(checkSta));
					comp.setCheckTime(CurrentTime.getCurrentTime());
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
	@ApiImplicitParams({ 
		    @ApiImplicitParam(name = "id", value = "公司编号",  required = true),
			@ApiImplicitParam(name = "typeId", value = "公司类型编号",  required = true),
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
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_COMPANY)) {
			try {
				Company comp = companyService.getEntityById(id);
				if (comp == null) {
					status = 50001;
				} else {
					CompanyType ct = ctService.findById(CommonTools.getFinalStr(typeId));
					comp.setCompanyType(ct);
					comp.setProvince(CommonTools.getFinalStr(province));
					comp.setCity(CommonTools.getFinalStr(city));
					comp.setCounty(CommonTools.getFinalStr(county));
					comp.setAddress(CommonTools.getFinalStr(address));
					comp.setLxName(CommonTools.getFinalStr(lxname));
					comp.setLxTel(CommonTools.getFinalStr(lxtel));
					comp.setBankName(CommonTools.getFinalStr(bankName));
					comp.setBankNo(CommonTools.getFinalStr(bankNo));
					comp.setBankAccount(CommonTools.getFinalStr(bankAcc));
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

}
