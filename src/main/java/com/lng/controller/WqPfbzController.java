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

import com.lng.pojo.WqPfbz;
import com.lng.service.WqPfbzService;
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
@Api(tags = "尾气排放标准相关接口")
@RequestMapping("/wqPfbz")
public class WqPfbzController {
	@Autowired
	private WqPfbzService pfbzService;

	@PostMapping("/addWqPfbz")
	@ApiOperation(value = "添加尾气排放标准", notes = "添加尾气排放标准信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问"),
			@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "尾气排放标准名称", defaultValue = "尾气排放标准测试", required = true) })
	public GenericResponse addWqPfbz(HttpServletRequest request, String name) {
		Integer status = 200;
		String pfbzId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.WQPP_ABILITY)) {
			try {
				if(!name.equals("")) {
					if (pfbzService.getWqPfbzByNameList(name).size() == 0) {
						WqPfbz pfbz = new WqPfbz();
						pfbz.setName(CommonTools.getFinalStr(name));
						pfbz.setAddTime(CurrentTime.getCurrentTime());
						pfbzId = pfbzService.saveOrUpdate(pfbz);
					} else {
						status = 50003;
					}
				}else {
					status = 10002;
				}

			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, pfbzId);
	}

	@PutMapping("/updateWqPfbz")
	@ApiOperation(value = "修改尾气排放标准", notes = "修改尾气排放标准信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "尾气排放标准编号", required = true),
			@ApiImplicitParam(name = "name", value = "尾气排放标准名称", defaultValue = "尾气排放标准测试", required = true) })
	public GenericResponse updateWqPfbz(HttpServletRequest request, String id, String name) {
		Integer status = 200;
		name=CommonTools.getFinalStr(name);
		
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.WQPP_ABILITY)) {
			try {
				WqPfbz pfbz = pfbzService.findById(CommonTools.getFinalStr(id));
				if (pfbz== null) {
					status = 50001;
				} else {
					if(!name.equals(pfbz.getName())) {
						if (pfbzService.getWqPfbzByNameList(name).size() == 0) {
							pfbz.setName(name);
							pfbzService.saveOrUpdate(pfbz);
						} else {
							status = 50003;
						}
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

	@GetMapping("/queryWqPfbz")
	@ApiOperation(value = "获取尾气排放标准", notes = "获取尾气排放标准信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "尾气排放标准编号") })
	public GenericResponse queryWqPfbz(String id) {
		Integer status = 200;
		List<WqPfbz> pfbzList = new ArrayList<WqPfbz>();
		id= CommonTools.getFinalStr(id);
		try {
			if (id.equals("")) {
				pfbzList = pfbzService.getWqPfbzByNameList("");
			} else {
				WqPfbz pfbz= pfbzService.findById(id);
				if (pfbz == null) {
					status = 50001;
				} else {
					pfbzList.add(pfbz);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, pfbzList);
	}

}
