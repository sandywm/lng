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

import com.lng.pojo.TrucksHeadType;
import com.lng.service.TrucksHeadTypeService;
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
@Api(tags = "槽车车头类型相关接口")
@RequestMapping("/tHeadType")
public class TrucksHeadTypeController {
	@Autowired
	private TrucksHeadTypeService thtService;

	@PostMapping("/addTHeadType")
	@ApiOperation(value = "添加槽车车头类型", notes = "添加槽车车头类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "槽车车头类型名称", defaultValue = "槽车车头类型测试", required = true) })
	public GenericResponse addTHeadType(HttpServletRequest request, String name) {
		Integer status = 200;
		String thtId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_THT)) {
			try {
				if (thtService.getTrucksHeadTypeByNameList(name).size() == 0) {
					TrucksHeadType tht = new TrucksHeadType();
					tht.setName(CommonTools.getFinalStr(name));
					thtId = thtService.saveOrUpdate(tht);
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
		return ResponseFormat.retParam(status, thtId);
	}

	@PutMapping("/updateTHeadType")
	@ApiOperation(value = "修改槽车车头类型", notes = "修改槽车车头类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "槽车车头类型编号", required = true),
			@ApiImplicitParam(name = "name", value = "槽车车头类型名称", defaultValue = "槽车车头类型测试", required = true) })
	public GenericResponse updateTHeadType(HttpServletRequest request, String id, String name) {
		id = CommonTools.getFinalStr(id);
		name=CommonTools.getFinalStr(name);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_THT)) {
			try {
				TrucksHeadType tht = thtService.findById(id);
				if (tht== null) {
					status = 50001;
				} else {
					if (thtService.getTrucksHeadTypeByNameList(name).size() == 0) {
						if(!name.equals("") && name.equals(tht.getName())) {
							tht.setName(name);
						}
						thtService.saveOrUpdate(tht);
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

	@GetMapping("/queryTHeadType")
	@ApiOperation(value = "获取槽车车头类型", notes = "获取槽车车头类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "槽车车头类型编号") })
	public GenericResponse queryTHeadType(String id) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		List<TrucksHeadType> thtList = new ArrayList<TrucksHeadType>();
		try {
			if (id.equals("")) {
				thtList = thtService.getTrucksHeadTypeByNameList("");
			} else {
				TrucksHeadType tht = thtService.findById(id);
				if (tht == null) {
					status = 50001;
				} else {
					thtList.add(tht);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, thtList);
	}

}
