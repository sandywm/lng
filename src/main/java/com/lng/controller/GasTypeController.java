package com.lng.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@Api(tags = "液质类型相关接口")
@RequestMapping("/gasType")
public class GasTypeController {
	@Autowired
	private GasTypeService gTypeService;

	@PostMapping("/addGasType")
	@ApiOperation(value = "添加液质类型", notes = "添加液质类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问"),
			@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "液质类型名称", defaultValue = "天然气", required = true),
			@ApiImplicitParam(name = "yzImg", value = "液质图片", required = true) })
	public GenericResponse addGasType(HttpServletRequest request) {
		Integer status = 200;
		String gtId = "";
		String name = CommonTools.getFinalStr("name", request);
		String yzImg = CommonTools.getFinalStr("yzImg", request);
		String loginUserId = CommonTools.getLoginUserId(request);
		if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),Constants.YZLX_ABILITY)) {
			try {
				if(name.equals("") || yzImg.equals("")) {
					status = 10002;
				}else {
					if (gTypeService.getGasTypeByNameList(name).size() == 0) {
						GasType gType = new GasType();
						gType.setName(name);
						gType.setAddTime(CurrentTime.getCurrentTime());
						if(!yzImg.equals("")) {//上传图
							gType.setYzImg(CommonTools.dealUploadDetail(loginUserId,"", yzImg));
						}
						gtId = gTypeService.saveOrUpdate(gType);
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
		if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),Constants.YZLX_ABILITY)) {
			try {
				GasType gt = gTypeService.findById(id);
				if (gt == null) {
					status = 50001;
				} else {
					if(gt.getName().equals(name)) {
						if (!yzImg.equals("") && !yzImg.equals(gt.getYzImg())) {
							gt.setYzImg(CommonTools.dealUploadDetail(loginUserId, gt.getYzImg(), yzImg));
							gTypeService.saveOrUpdate(gt);
						}
					}else {
						if (gTypeService.getGasTypeByNameList(name).size() == 0) {
							gt.setName(name);
							if (!yzImg.equals("") && !yzImg.equals(gt.getYzImg())) {
								gt.setYzImg(CommonTools.dealUploadDetail(loginUserId, gt.getYzImg(), yzImg));
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
		List<Object> list = new ArrayList<Object>();
		try {
			if (id.equals("")) {
				List<GasType> gtList = gTypeService.getGasTypeList();
				for(GasType gt : gtList) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", gt.getId());
					map.put("name", gt.getName());
					map.put("yzImg", gt.getYzImg());
					map.put("addTime", gt.getAddTime());
					map.put("state", 0);
					list.add(map);
				}
			} else {
				GasType gt = gTypeService.findById(id);
				if (gt == null) {
					status = 50001;
				} else {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", gt.getId());
					map.put("name", gt.getName());
					map.put("yzImg", gt.getYzImg());
					map.put("addTime", gt.getAddTime());
					map.put("state", 0);
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
