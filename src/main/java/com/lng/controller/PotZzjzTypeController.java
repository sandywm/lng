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

import com.lng.pojo.PotZzjzType;
import com.lng.service.PotZzjzTypeService;
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
@Api(tags = "装载介质类型相关接口")
@RequestMapping("/potZzjzType")
public class PotZzjzTypeController {

	@Autowired
	private PotZzjzTypeService potZzjztypeService;

	@PostMapping("/addPotZzjzType")
	@ApiOperation(value = "添加装载介质类型", notes = "添加装载介质类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "装载介质类型名称", defaultValue = "液氮", required = true) })
	public GenericResponse addPotZzjzType(HttpServletRequest request, String name) {
		Integer status = 200;
		String jzId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),CommonTools.getLoginRoleName(request), Constants.ZZJZ_ABILITY)) {
			try {
				if (potZzjztypeService.getPotZzjzTypeByNameList(name).size() == 0) {
					PotZzjzType jzType = new PotZzjzType();
					jzType.setName(CommonTools.getFinalStr(name));
					jzId = potZzjztypeService.saveOrUpdate(jzType);
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
		return ResponseFormat.retParam(status, jzId);
	}

	@PutMapping("/updatePotZzjzType")
	@ApiOperation(value = "修改装载介质类型", notes = "修改装载介质类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "装载介质类型编号", required = true),
			@ApiImplicitParam(name = "name", value = "装载介质类型名称", defaultValue = "酒精", required = true) })
	public GenericResponse updatePotZzjzType(HttpServletRequest request, String id, String name) {
		id = CommonTools.getFinalStr(id);
		name = CommonTools.getFinalStr(name);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.ZZJZ_ABILITY)) {
			try {
				PotZzjzType jz = potZzjztypeService.findById(id);
				if (jz == null) {
					status = 50001;
				} else {
					if (potZzjztypeService.getPotZzjzTypeByNameList(name).size() == 0) {
						if (!(name.equals("") && !name.equals(jz.getName()))) {
							jz.setName(name);
						}
						potZzjztypeService.saveOrUpdate(jz);
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

	@GetMapping("/queryPotZzjzType")
	@ApiOperation(value = "获取装载介质类型", notes = "获取装载介质类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "装载介质类型编号") })
	public GenericResponse queryPotZzjzType(String id) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		List<PotZzjzType> jzList = new ArrayList<PotZzjzType>();
		try {
			if (id.equals("")) {
				jzList = potZzjztypeService.getPotZzjzTypeByNameList("");
			} else {
				PotZzjzType jz = potZzjztypeService.findById(id);
				if (jz == null) {
					status = 50001;
				} else {
					jzList.add(jz);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, jzList);
	}
}
