package com.lng.controller;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.lng.pojo.SystemInfo;
import com.lng.service.SysConfigService;
import com.lng.tools.ContantsProperties;
import com.lng.tools.CurrentTime;
import com.lng.tools.FileOpration;
import com.lng.tools.JunitImage;
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

	@Autowired
	private SysConfigService scs;
	@Autowired
	private ContantsProperties cp;
	
	@ApiOperation("单个图片上传接口")
	@PostMapping("uploadSingle")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误")})
	public GenericResponse upload(MultipartFile file,HttpServletRequest request) throws Exception {
		long startTime=System.currentTimeMillis();
		String newFileNamePre = "";
		String newFileNamePre_small = "";
		Integer status = 200;
		File filePath = new File(cp.getWeburl() + "temp");
		if(!filePath.exists()) {
			filePath.mkdirs();
		}
		try {
			String fileName = file.getOriginalFilename();
			Integer lastIndex = fileName.lastIndexOf(".");
			String suffix = fileName.substring(lastIndex+1);
			String currentTime = CurrentTime.getRadomTime();
			newFileNamePre = currentTime + "." + suffix;
			newFileNamePre_small = currentTime + "_small." + suffix;
			String newFilePath = filePath+File.separator+newFileNamePre;
			String newSmallFilePath = filePath+File.separator+newFileNamePre_small;
			file.transferTo(new File(newFilePath));
			String formatName = FileOpration.getImageFormat(suffix);
//			List<SystemInfo> sList = scs.findInfo();
//			String waterMark = "";
//			if(sList.size() > 0) {
//				waterMark = sList.get(0).getWaterMark();
//			}
//			JunitImage.markImageByText(waterMark,newFilePath,newFilePath,45,new Color(0,0,0),formatName);
			FileOpration.makeImage(newFilePath, 0.5, newSmallFilePath, formatName);
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = 1000;
		}
		long endTime=System.currentTimeMillis();
		float excTime=(float)(endTime-startTime)/1000;
		System.out.println("上传单个文件耗费时间--"+excTime);
		return ResponseFormat.retParam(status, newFileNamePre);
	}
	
	@ApiOperation("多个图片上传接口")
	@PostMapping("uploadMuti")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误")})
	public GenericResponse uploadMuti(HttpServletRequest request) throws Exception {
		Integer status = 200;
		List<String> list_d = new ArrayList<String>();
		File filePath = new File(cp.getWeburl() + "temp");
		if(!filePath.exists()) {
			filePath.mkdirs();
		}
		try {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> multipartFiles = multipartHttpServletRequest.getFiles("file");
	        for (MultipartFile file : multipartFiles) {
	        	String fileName = file.getOriginalFilename();
				Integer lastIndex = fileName.lastIndexOf(".");
				String suffix = fileName.substring(lastIndex+1);
				String currentTime = CurrentTime.getRadomTime();
				String newFileNamePre = currentTime + "." + suffix;
				String newFileNamePre_small = currentTime + "_small." + suffix;
				String newFilePath = filePath+File.separator+newFileNamePre;
				String newSmallFilePath = filePath+File.separator+newFileNamePre_small;
				file.transferTo(new File(newFilePath));
				String formatName = FileOpration.getImageFormat(suffix);
//				List<SystemInfo> sList = scs.findInfo();
//				String waterMark = "";
//				if(sList.size() > 0) {
//					waterMark = sList.get(0).getWaterMark();
//				}
//				JunitImage.markImageByText(waterMark,newFilePath,newFilePath,45,new Color(0,0,0),formatName);
				FileOpration.makeImage(newFilePath, 0.5, newSmallFilePath, formatName);
	        	list_d.add(newFileNamePre);
	        }
		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list_d);
	}
}
