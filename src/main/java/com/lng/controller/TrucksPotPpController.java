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

import com.lng.pojo.TrucksPotPp;
import com.lng.service.TrucksPotPpService;
import com.lng.tools.CommonTools;
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
@Api(tags = "槽车储罐品牌相关接口")
@RequestMapping("/tPotPp")
public class TrucksPotPpController {
	@Autowired
	private TrucksPotPpService tpppService;

	@PostMapping("/addTPotPp")
	@ApiOperation(value = "添加槽车储罐品牌", notes = "添加槽车储罐品牌信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "槽车储罐品牌名称", defaultValue = "槽车储罐品牌测试", required = true) })
	public GenericResponse addTPotPp(HttpServletRequest request, String name) {
		Integer status = 200;
		String tpppId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.CGPP_ABILITY)) {
			try {
				if (tpppService.getTrucksPotPpByNameList(name).size() == 0) {
					TrucksPotPp tppp = new TrucksPotPp();
					tppp.setName(CommonTools.getFinalStr(name));
					tpppId = tpppService.saveOrUpdate(tppp);
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
		return ResponseFormat.retParam(status, tpppId);
	}

	@PutMapping("/updateTPotPp")
	@ApiOperation(value = "修改槽车储罐品牌", notes = "修改槽车储罐品牌信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "槽车储罐品牌编号", required = true),
			@ApiImplicitParam(name = "name", value = "槽车储罐品牌名称", defaultValue = "槽车储罐品牌测试", required = true) })
	public GenericResponse updateTPotPp(HttpServletRequest request, String id, String name) {
		Integer status = 200;
		id = CommonTools.getFinalStr(id);
		name=CommonTools.getFinalStr(name);
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.CGPP_ABILITY)) {
			try {
				TrucksPotPp tppp = tpppService.findById(id);
				if (tppp== null) {
					status = 50001;
				} else {
					if (tpppService.getTrucksPotPpByNameList(name).size() == 0) {
						if(!name.equals("") && ! name.equals(tppp.getName())) {
							tppp.setName(name);
						}
						tpppService.saveOrUpdate(tppp);
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

	@GetMapping("/queryTPotPp")
	@ApiOperation(value = "获取槽车储罐品牌", notes = "获取槽车储罐品牌信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "槽车储罐品牌编号") })
	public GenericResponse queryTPotPp(String id) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		List<TrucksPotPp> tpppList = new ArrayList<TrucksPotPp>();
		try {
			if (id.equals("")) {
				tpppList = tpppService.getTrucksPotPpByNameList("");
			} else {
				TrucksPotPp tppp= tpppService.findById(id);
				if (tppp == null) {
					status = 50001;
				} else {
					tpppList.add(tppp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, tpppList);
	}


}
