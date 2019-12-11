package com.lng.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.CompanyType;
import com.lng.service.CompanyTypeService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.util.Constants;
import com.lng.util.GenericResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "公司类型相关接口")
@RequestMapping("/comType")
public class CompanyTypeController {
	@Autowired
	private CompanyTypeService cTypeService;

	@PostMapping("/addComType")
	@ApiOperation(value = "添加公司类型", notes = "添加公司类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "公司类型名称", defaultValue = "LNG贸易商", required = true) })
	public GenericResponse addComType(HttpServletRequest request, String name) {
		Integer status = 200;
		String ctId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_COMT)) {
			try {
				if (cTypeService.getCompanyTypeByNameList(name).size() == 0) {
					CompanyType cType = new CompanyType();
					cType.setName(CommonTools.getFinalStr(name));
					cType.setAddTime(CurrentTime.getCurrentTime());
					ctId = cTypeService.saveOrUpdate(cType);
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
		return ResponseFormat.retParam(status, ctId);
	}

	@PutMapping("/updateComType")
	@ApiOperation(value = "修改公司类型", notes = "修改公司类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司类型编号", required = true),
			@ApiImplicitParam(name = "name", value = "公司类型名称", defaultValue = "LNG贸易商", required = true) })
	public GenericResponse updateComType(HttpServletRequest request, String id, String name) {
		id = CommonTools.getFinalStr(id);
		name = CommonTools.getFinalStr(name);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_COMT)) {
			try {
				CompanyType ct = cTypeService.findById(id);
				if (ct == null) {
					status = 50001;
				} else {
					if (cTypeService.getCompanyTypeByNameList(name).size() == 0) {
						if (!name.equals("") && !name.equals(ct.getName())) {
							ct.setName(name);
							ct.setAddTime(CurrentTime.getCurrentTime());
						}
						cTypeService.saveOrUpdate(ct);
					} else {
						status = 50003;
					}
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

	@GetMapping("/queryComType")
	@ApiOperation(value = "获取公司类型", notes = "获取公司类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "公司类型编号") })
	public GenericResponse queryComType(String id) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		List<CompanyType> ctList = new ArrayList<CompanyType>();
		try {
			if (id.equals("")) {
				ctList = cTypeService.getCompanyTypeList();
			} else {
				CompanyType ct = cTypeService.findById(id);
				if (ct == null) {
					status = 50001;
				} else {
					ctList.add(ct);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, ctList);
	}
}
