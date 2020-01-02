package com.lng.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.ActionEnter;
import com.lng.tools.CommonTools;
import com.lng.tools.ContantsProperties;
import com.lng.tools.CurrentTime;

import io.swagger.annotations.ApiOperation;

@Controller
public class UeditorController {

	@Autowired
	private ContantsProperties cp;
	
	@ApiOperation("百度富文本配置")
	@RequestMapping("ueditorConfig")
	public void ueditorConfig(HttpServletRequest request,HttpServletResponse response){
		String action = CommonTools.getFinalStr("action", request);
		try {
			response.setContentType("application/json");
			request.setCharacterEncoding( "utf-8" );
			response.setHeader("Content-Type" , "text/html");
			String rootPath = System.getProperty("user.dir") + "\\src\\main\\resources";//开发电脑用
//			String rootPath = request.getServletContext().getRealPath("/WEB-INF/classes");//window服务器上用
			if(action.equals("config")) {//初始配置
				response.getWriter().write( new ActionEnter( request, rootPath ).exec() );
			}else if("uploadimage".equals(action) || "uploadvideo".equals(action) || "uploadfile".equals(action)){//如果是上传图片、视频、和其他文件
				 try {
	                    MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
	        			List<MultipartFile> multipartFiles = multipartHttpServletRequest.getFiles("upfile");
	        			String filePath_tmp = cp.getWeburl() + "file/ueditor/";
	        			File filePath = new File(filePath_tmp);
	        			if(!filePath.exists()) {
	        				filePath.mkdirs();
	        			}
	        	        for (MultipartFile pic : multipartFiles) {
	        	        	JSONObject jo = new JSONObject();
	                        long size = pic.getSize();    //文件大小
	                        String originalFilename = pic.getOriginalFilename();  //原来的文件名
	                        
	                        String fileName = pic.getOriginalFilename();
	        				Integer lastIndex = fileName.lastIndexOf(".");
	        				String suffix = fileName.substring(lastIndex+1);
	        				String currentTime = CurrentTime.getRadomTime();
	        				String newFileNamePre = currentTime + "." + suffix;
	        				String newFilePath = filePath_tmp+newFileNamePre;
	        				
	        				pic.transferTo(new File(newFilePath));
	        				jo.put("state", "SUCCESS");
                            jo.put("original", originalFilename);//原来的文件名
                            jo.put("size", size);//文件大小
                            jo.put("title", fileName);//随意，代表的是鼠标经过图片时显示的文字
                            jo.put("type", FilenameUtils.getExtension(pic.getOriginalFilename()));//文件后缀名
                            jo.put("url", "/file/ueditor/"+newFileNamePre);//这里的url字段表示的是上传后的图片在图片服务器的完整地址（http://ip:端口/***/***/***.jpg）
	                        response.getWriter().write(jo.toString());
	        	        }
	                }catch (Exception e) {
	                    e.printStackTrace();
	                }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
