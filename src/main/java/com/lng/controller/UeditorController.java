package com.lng.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baidu.ueditor.ActionEnter;

import io.swagger.annotations.ApiOperation;

@Controller
public class UeditorController {

	@ApiOperation("百度富文本配置")
	@GetMapping("ueditorConfig")
	public void ueditorConfig(HttpServletRequest request,HttpServletResponse response){
		try {
			request.setCharacterEncoding( "utf-8" );
			response.setHeader("Content-Type" , "text/html");
			String rootPath = request.getServletContext().getRealPath(File.separator);
			response.getWriter().write( new ActionEnter( request, rootPath ).exec() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
