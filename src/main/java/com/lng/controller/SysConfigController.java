package com.lng.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.SystemInfo;
import com.lng.service.SysConfigService;
import com.lng.tools.CommonTools;
import com.lng.util.GenericResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
	
	@PutMapping("upConfigData")
	@ApiOperation(value = "修改系统配置信息",notes = "修改系统配置信息")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),@ApiResponse(code = 50001, message = "数据未找到")})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cId", value = "配置编号",  required = true),
		@ApiImplicitParam(name = "appId", value = "APPID", required = true),
		@ApiImplicitParam(name = "appSecret", value = "APPSECRET", required = true),
		@ApiImplicitParam(name = "waterMark", value = "水印")
	})
	public GenericResponse upConfigData(HttpServletRequest request) {
		String cId = CommonTools.getFinalStr("cId", request);
		String appId = CommonTools.getFinalStr("appId", request);
		String appSecret = CommonTools.getFinalStr("appSecret", request);
		String waterMark = CommonTools.getFinalStr("waterMark", request);
		Integer status = 200;
		List<SystemInfo> scList = new ArrayList<SystemInfo>();
		try {
			if(cId.equals("") || appSecret.equals("") || appId.equals("")) {
				status = 50001;
			}else {
				scList = scs.findInfo();
				if(scList.size() == 0) {
					status = 50001;
				}else {
					SystemInfo sys = scList.get(0);
					sys.setAppId(appId);
					sys.setAppSecret(appSecret);
					sys.setWaterMark(waterMark);
					scs.addOrUpdate(sys);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
}
