package com.lng.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.lng.pojo.SuperDep;
import com.lng.service.SuperDepService;
import com.lng.tools.CommonTools;
import com.lng.util.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "跳转管理")
@RequestMapping(value = "/goPage")
public class JumpPageController {

	@Autowired
	private SuperDepService sds;
	
	@ApiOperation("登陆后跳转到首页")
	@GetMapping("goWelcomePage")
	public ModelAndView goWelcomePage(HttpServletRequest request){
		String roleId = CommonTools.getFinalStr("roleId",request);
		if(!roleId.equals("")) {
			List<SuperDep> spList = sds.listInfoByOpt(CommonTools.getLoginUserId(request), roleId);
			if(spList.size() > 0) {
				String roleName = spList.get(0).getDepartment().getDepName();
				request.getSession(false).setAttribute(Constants.LOGIN_USER_ROLE_NAME, roleName);
				if(roleName.equals("超级管理员")) {
					return new ModelAndView("welcome_spu");
				}else {
					return new ModelAndView("welcome");
				}
			}
		}
		return null;
	}
	
	@ApiOperation("模块权限设置")
	@GetMapping("goSpuModPage")
	public ModelAndView goSpuModPage(){
		return new ModelAndView("modManager/spuModPage");
	}
	
	@ApiOperation("用户权限管理")
	@GetMapping("goUserPermPage")
	public ModelAndView goModPage(){
		return new ModelAndView("modManager/userPermisson");
	}
	
	@ApiOperation("用户管理")
	@GetMapping("goUserManagerPage")
	public ModelAndView goUserManagerPage(){
		return new ModelAndView("userManager/userManager");
	}
	
	@ApiOperation("角色管理")
	@GetMapping("goRolePage")
	public ModelAndView goRolePage(){
		return new ModelAndView("roleManager/roleManager");
	}
	
	@ApiOperation("个人资料设置")
	@GetMapping("goPerInfoPage")
	public ModelAndView goPerInfoPage(){
		return new ModelAndView("personalInfoSet");
	}
}
