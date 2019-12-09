package com.lng.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lng.tools.CommonTools;
import com.lng.util.Constants;
import com.lng.util.GenericResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "上传相关接口")
@RequestMapping(value = "/upload")
public class UploadController {

	@ApiOperation("单个文件/图片上传接口")
	@PostMapping("uploadSingle")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误")})
	public GenericResponse upload(MultipartFile file,HttpServletRequest request) {
		String cilent_info = CommonTools.getCilentInfo_new(request);
		String userId = "";
		if(cilent_info.equals("wxApp")) {//微信端
			
		}else if(cilent_info.equals("pc")) {
			userId = CommonTools.getLoginUserId(request);
			
		}
		Integer status = 200;
		File filePath = new File(Constants.UPLOAD_PATH + userId);
		if(!filePath.exists()) {
			filePath.mkdirs();
		}
		try {
			file.transferTo(new File(filePath+File.separator+file.getOriginalFilename()));
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, null);
	}
	
	@ApiOperation("多个文件/图片上传接口")
	@PostMapping("uploadMuti")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误")})
	public GenericResponse uploadMuti(HttpServletRequest request) {
		Integer status = 200;
		List<String> list_d = new ArrayList<String>();
		String cilent_info = CommonTools.getCilentInfo_new(request);
		String userId = "";
		if(cilent_info.equals("wxApp")) {//微信端
			
		}else if(cilent_info.equals("pc")) {
			userId = CommonTools.getLoginUserId(request);
			
		}
		File filePath = new File(Constants.UPLOAD_PATH + userId);
		if(!filePath.exists()) {
			filePath.mkdirs();
		}
		try {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> multipartFiles = multipartHttpServletRequest.getFiles("file");
	        for (MultipartFile file : multipartFiles) {
	        	file.transferTo(new File(filePath+File.separator+file.getOriginalFilename()));
	        	list_d.add(file.getOriginalFilename());
	        	System.out.println(file.getOriginalFilename());
	        }
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list_d);
	}
}
