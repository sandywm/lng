package com.lng.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.SystemInfo;
import com.lng.service.SysConfigService;
import com.lng.util.GenericResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "系统配置管理")
@RequestMapping(value = "/superConfig")
public class SysConfigController {

	@Autowired
	private SysConfigService scs;
	
	@GetMapping("getSysConfigData")
	@ApiOperation(value = "获取系统配置信息",notes = "系统配置信息")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),@ApiResponse(code = 50001, message = "数据未找到")})
	public GenericResponse getSysConfigData() {
		Integer status = 200;
		List<SystemInfo> scList = new ArrayList<SystemInfo>();
		try {
			scList = scs.findInfo();
			if(scList.size() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, scList);
	}
}
