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

import com.lng.pojo.RqDevType;
import com.lng.service.RqDevTypeService;
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
@Api(tags = "燃气设备类目相关接口")
@RequestMapping("/rqDevType")
public class RqDevTypeController {
	@Autowired
	private RqDevTypeService rqDevTypeService;

	@PostMapping("/addRqDevType")
	@ApiOperation(value = "添加燃气设备类目", notes = "添加燃气设备类目信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "燃气设备类目名称", defaultValue = "燃气设备类目测试", required = true) })
	public GenericResponse addRqDevType(HttpServletRequest request, String name) {
		Integer status = 200;
		String rqId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_RQDEVTYPE)) {
			try {
				if (rqDevTypeService.getRqDevTypeByNameList(name).size() == 0) {
					RqDevType rqDevType = new RqDevType();
					rqDevType.setName(name);
					rqId = rqDevTypeService.saveOrUpdate(rqDevType);
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
		return ResponseFormat.retParam(status, rqId);
	}

	@PutMapping("/updateRqDevType")
	@ApiOperation(value = "修改燃气设备类目", notes = "修改燃气设备类目信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气设备类目编号", required = true),
			@ApiImplicitParam(name = "name", value = "燃气设备类目名称", defaultValue = "燃气设备类目测试", required = true) })
	public GenericResponse updateRqDevType(HttpServletRequest request, String id, String name) {
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_RQDEVTYPE)) {
			try {
				RqDevType rqdev = rqDevTypeService.findById(id);
				if (rqdev== null) {
					status = 50001;
				} else {
					if (rqDevTypeService.getRqDevTypeByNameList(name).size() == 0) {
						rqdev.setName(name);
						rqDevTypeService.saveOrUpdate(rqdev);
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

	@GetMapping("/queryRqDevType")
	@ApiOperation(value = "获取燃气设备类目", notes = "获取燃气设备类目信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气设备类目编号") })
	public GenericResponse queryRqDevType(String id) {
		Integer status = 200;
		List<RqDevType> rqList = new ArrayList<RqDevType>();
		try {
			if (id.equals("")) {
				rqList = rqDevTypeService.getRqDevTypeByNameList("");
			} else {
				RqDevType rq = rqDevTypeService.findById(id);
				if (rq == null) {
					status = 50001;
				} else {
					rqList.add(rq);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, rqList);
	}

}
