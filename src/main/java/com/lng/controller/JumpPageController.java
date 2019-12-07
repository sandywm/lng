package com.lng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "跳转管理")
public class JumpPageController {

	@ApiOperation("登陆后跳转到首页")
	@GetMapping("welcome")
	public ModelAndView welcome1(){
		return new ModelAndView("welcome");
	}
}
