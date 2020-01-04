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

import com.lng.pojo.QyType;
import com.lng.service.QyTypeService;
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
@Api(tags = "气源类型相关接口")
@RequestMapping("/qyType")
public class QyTypeController {

	@Autowired
	private QyTypeService qytypeService;

	@PostMapping("/addqyType")
	@ApiOperation(value = "添加气源类型", notes = "添加气源类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "气源类型名称", defaultValue = "天然气", required = true) })
	public GenericResponse addQyType(HttpServletRequest request, String name) {
		Integer status = 200;
		String qyId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.QYLX_ABILITY)) {
			try {
				if (qytypeService.getQyTypeByNameList(name).size() == 0) {
					QyType qyType = new QyType();
					qyType.setName(CommonTools.getFinalStr(name));
					qyType.setAddTime(CurrentTime.getCurrentTime());
					qyId = qytypeService.saveOrUpdate(qyType);
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
		return ResponseFormat.retParam(status, qyId);
	}

	@PutMapping("/updateQyType")
	@ApiOperation(value = "修改气源类型", notes = "修改气源类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "气源类型编号", required = true),
			@ApiImplicitParam(name = "name", value = "气源类型名称", defaultValue = "海气", required = true) })
	public GenericResponse updateQyType(HttpServletRequest request, String id, String name) {
		id = CommonTools.getFinalStr(id);
		name = CommonTools.getFinalStr(name);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.QYLX_ABILITY)) {
			try {
				QyType qy = qytypeService.findById(id);
				if (qy == null) {
					status = 50001;
				} else {
					if(!name.equals(qy.getName())) {
						if (qytypeService.getQyTypeByNameList(name).size() == 0) {
							qy.setName(name);
							qytypeService.saveOrUpdate(qy);
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
	@ApiOperation(value = "获取气源类型", notes = "获取气源类型信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "气源类型编号") })
	public GenericResponse queryQyType(String id) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		List<QyType> qyList = new ArrayList<QyType>();
		try {
			if (id.equals("")) {
				qyList = qytypeService.getQyTypeByNameList("");
			} else {
				QyType qy = qytypeService.findById(id);
				if (qy == null) {
					status = 50001;
				} else {
					qyList.add(qy);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, qyList);
	}

}
