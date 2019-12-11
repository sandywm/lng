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

import com.lng.pojo.TrucksType;
import com.lng.service.TrucksTypeService;
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
@Api(tags = "槽车类型相关接口")
@RequestMapping("/trucksType")
public class TrucksTypeController {
	@Autowired
	private TrucksTypeService ttService;

	@PostMapping("/addTrucksType")
	@ApiOperation(value = "添加槽车类型", notes = "添加槽车类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "车辆种类（油罐车...）", defaultValue = "槽车类型测试", required = true),
			@ApiImplicitParam(name = "type", value = "车辆类型（1：普货车，2：危货车）", defaultValue = "2", required = true) })
	public GenericResponse addTrucksType(HttpServletRequest request, String name, int type) {
		Integer status = 200;
		String ttId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_TT)) {
			try {
				if (ttService.getTrucksTypeByNameList(name).size() == 0) {
					TrucksType tt = new TrucksType();
					tt.setName(CommonTools.getFinalStr(name));
					tt.setType(CommonTools.getFinalInteger(type));
					ttId = ttService.saveOrUpdate(tt);
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
		return ResponseFormat.retParam(status, ttId);
	}

	@PutMapping("/updateTrucksType")
	@ApiOperation(value = "修改槽车类型", notes = "修改槽车类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "槽车类型编号", required = true),
			@ApiImplicitParam(name = "name", value = "车辆种类（油罐车...）", defaultValue = "槽车类型测试", required = true),
			@ApiImplicitParam(name = "type", value = "车辆类型（1：普货车，2：危货车）", defaultValue = "2", required = true) })
	public GenericResponse updateTrucksType(HttpServletRequest request, String id, String name, Integer type) {
		Integer status = 200;
		id = CommonTools.getFinalStr(id);
		name=CommonTools.getFinalStr(name);
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_TT)) {
			try {
				TrucksType tt = ttService.findById(id);
				if (tt == null) {
					status = 50001;
				} else {
					if (ttService.getTrucksTypeByNameList(name).size() == 0) {
						if (!name.equals("") && ! name.equals(tt.getName())) {
							tt.setName(name);
						}
						if (type != null) {
							tt.setType(type);
						}
						
						ttService.saveOrUpdate(tt);
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

	@GetMapping("/queryTrucksType")
	@ApiOperation(value = "获取槽车类型", notes = "获取槽车类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "槽车类型编号") })
	public GenericResponse queryTrucksType(String id) {
		Integer status = 200;
		List<TrucksType> ttList = new ArrayList<TrucksType>();
		id = CommonTools.getFinalStr(id);
		try {
			if (id.equals("")) {
				ttList = ttService.getTrucksTypeByNameList("");
			} else {
				TrucksType tt = ttService.findById(id);
				if (tt == null) {
					status = 50001;
				} else {
					ttList.add(tt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, ttList);
	}

}
