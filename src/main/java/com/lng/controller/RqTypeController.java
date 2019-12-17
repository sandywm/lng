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

import com.lng.pojo.RqDevType1;
import com.lng.service.RqDevType1Service;
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
@Api(tags = "燃气设备类型相关接口")
@RequestMapping("/rqType")
public class RqTypeController {

	@Autowired
	private RqDevType1Service rqTypeService;

	@PostMapping("/addRqType")
	@ApiOperation(value = "添加燃气设备类型", notes = "添加燃气设备类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "name", value = "燃气设备类型名称", defaultValue = "燃气设备类型测试", required = true) })
	public GenericResponse addRqType(HttpServletRequest request, String name) {
		Integer status = 200;
		String rqId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.RQLX_ABILITY)) {
			try {
				if (rqTypeService.getRqDevType1ByNameList(name).size() == 0) {
					RqDevType1 rqType = new RqDevType1();
					rqType.setName(CommonTools.getFinalStr(name));
					rqId = rqTypeService.saveOrUpdate(rqType);
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

	@PutMapping("/updateRqType")
	@ApiOperation(value = "修改燃气设备类型", notes = "修改燃气设备类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气设备类型编号", required = true),
			@ApiImplicitParam(name = "name", value = "燃气设备类型名称", defaultValue = "燃气设备类型测试", required = true) })
	public GenericResponse updateRqType(HttpServletRequest request, String id, String name) {
		id = CommonTools.getFinalStr(id);
		name = CommonTools.getFinalStr(name);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.RQLX_ABILITY)) {
			try {
				RqDevType1 rq = rqTypeService.findById(id);
				if (rq == null) {
					status = 50001;
				} else {
					if (rqTypeService.getRqDevType1ByNameList(name).size() == 0) {
						if (!name.equals("") && !name.equals(rq.getName())) {
							rq.setName(name);
						}
						rqTypeService.saveOrUpdate(rq);
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

	@GetMapping("/queryRqType")
	@ApiOperation(value = "获取燃气设备类型", notes = "获取燃气设备类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气设备类型编号") })
	public GenericResponse queryRqType(String id) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		List<RqDevType1> rqList = new ArrayList<RqDevType1>();
		try {
			if (id.equals("")) {
				rqList = rqTypeService.getRqDevType1ByNameList("");
			} else {
				RqDevType1 rq = rqTypeService.findById(id);
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
