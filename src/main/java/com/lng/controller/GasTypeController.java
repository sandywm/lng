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

import com.lng.pojo.GasType;
import com.lng.service.GasTypeService;
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
@Api(tags = "液质类型相关接口")
@RequestMapping("/gasType")
public class GasTypeController {
	@Autowired
	private GasTypeService gTypeService;

	@PostMapping("/addGasType")
	@ApiOperation(value = "添加液质类型", notes = "添加液质类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "液质类型名称", defaultValue = "天然气", required = true),
			@ApiImplicitParam(name = "yzImg", value = "液质图片", required = true) })
	public GenericResponse addGasType(HttpServletRequest request) {
		Integer status = 200;
		String gtId = "";
		String name = CommonTools.getFinalStr("name", request);
		String yzImg = CommonTools.getFinalStr("yzImg", request);
		String loginUserId = CommonTools.getLoginUserId(request);
		if (CommonTools.checkAuthorization(loginUserId, Constants.YZLX_ABILITY)) {
			try {
				if (gTypeService.getGasTypeByNameList(name).size() == 0) {
					GasType gType = new GasType();
					gType.setName(name);
					if(!yzImg.equals("")) {//上传图
						gType.setYz_img(CommonTools.dealUploadDetail(loginUserId, yzImg));
					}
					gtId = gTypeService.saveOrUpdate(gType);
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
		return ResponseFormat.retParam(status, gtId);
	}

	@PutMapping("/updateGasType")
	@ApiOperation(value = "修改液质类型", notes = "修改液质类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "液质类型编号", required = true),
			@ApiImplicitParam(name = "name", value = "液质类型名称", defaultValue = "天然气", required = true),
			@ApiImplicitParam(name = "yzImg", value = "液质图片", required = true) })
	public GenericResponse updateGasType(HttpServletRequest request, String id, String name, String yzImg) {
		id = CommonTools.getFinalStr(id);
		name = CommonTools.getFinalStr(name);
		yzImg = CommonTools.getFinalStr(yzImg);
		Integer status = 200;
		String loginUserId = CommonTools.getLoginUserId(request);
		if (CommonTools.checkAuthorization(loginUserId, Constants.YZLX_ABILITY)) {
			try {
				GasType gt = gTypeService.findById(id);
				if (gt == null) {
					status = 50001;
				} else {
					if(gt.getName().equals(name)) {
						if (!yzImg.equals("") && !yzImg.equals(gt.getYz_img())) {
							gt.setYz_img(CommonTools.dealUploadDetail(loginUserId, yzImg));
							gTypeService.saveOrUpdate(gt);
						}
					}else {
						if (gTypeService.getGasTypeByNameList(name).size() == 0) {
							if (!name.equals("") && !name.equals(gt.getName())) {
								gt.setName(name);
							}
							if (!yzImg.equals("") && !yzImg.equals(gt.getYz_img())) {
								gt.setYz_img(CommonTools.dealUploadDetail(loginUserId, yzImg));
							}
							gTypeService.saveOrUpdate(gt);
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

	@GetMapping("/queryGasType")
	@ApiOperation(value = "获取液质类型", notes = "获取液质类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "液质类型编号") })
	public GenericResponse queryGasType(String id) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		List<GasType> gtList = new ArrayList<GasType>();
		try {
			if (id.equals("")) {
				gtList = gTypeService.getGasTypeList();
			} else {
				GasType gt = gTypeService.findById(id);
				if (gt == null) {
					status = 50001;
				} else {
					gtList.add(gt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gtList);
	}
}
