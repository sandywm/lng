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
				String roleName = "";
				for(SuperDep sd : spList) {
					if(sd.getDepartment().getId().equals(roleId)) {
						roleName = sd.getDepartment().getDepName();
						if(!roleName.equals("超级管理员")) {
							roleName = "平台管理员";
						}
						break;
					}
				}
				if(!roleName.equals("")) {
					request.getSession(false).setAttribute(Constants.LOGIN_USER_ROLE_NAME, roleName);
					if(roleName.equals("超级管理员")) {
						return new ModelAndView("welcome_spu");
					}else {
						return new ModelAndView("welcome");
					}
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
	
	@ApiOperation("密码设置")
	@GetMapping("goUpPassPage")
	public ModelAndView goUpPassPage(){
		return new ModelAndView("upPassword");
	}
	
	@ApiOperation("气源类型设置")
	@GetMapping("goGasTypePage")
	public ModelAndView goGasTypePage(){
		return new ModelAndView("gasTypeManager/gasTypeList");
	}
	
	@ApiOperation("液质类型设置")
	@GetMapping("goLqTypePage")
	public ModelAndView goLqTypePage(){
		return new ModelAndView("lqTypeManager/lqTypeList");
	}
	
	@ApiOperation("进港资质管理")
	@GetMapping("goJgZzPage")
	public ModelAndView goJgZzPage(){
		return new ModelAndView("jgzzManager/jgzzManager");
	}
	
	@ApiOperation("槽车类型管理")
	@GetMapping("goTrucksTypePage")
	public ModelAndView goTrucksTypePage(){
		return new ModelAndView("trucksTypeManager/trucksTypeList");
	}
	
	@ApiOperation("槽车车头品牌管理")
	@GetMapping("goTHeadBrandPage")
	public ModelAndView goTHeadBrandPage(){
		return new ModelAndView("trucksHeadBrand/trucksHeadList");
	}
	
	@ApiOperation("槽车车头类型管理")
	@GetMapping("goTHeadTypePage")
	public ModelAndView goTHeadTypePage(){
		return new ModelAndView("trucksHeadType/trucksHeadType");
	}
	
	@ApiOperation("槽车储罐品牌管理")
	@GetMapping("goTankBrandPage")
	public ModelAndView goTankBrandPage(){
		return new ModelAndView("storageTankBrand/tankBrandList");
	}
	
	@ApiOperation("装载介质类型管理")
	@GetMapping("goZzjzPage")
	public ModelAndView goZzjzPage(){
		return new ModelAndView("zzjzTypeManager/zzjzTypeList");
	}
	
	@ApiOperation("燃气设备类型管理")
	@GetMapping("goGasEquipTypePage")
	public ModelAndView goGasEquipPage(){
		return new ModelAndView("gasEquipmentType/gasEquipList");
	}
	
	@ApiOperation("燃气设备类目管理")
	@GetMapping("goGasEquipCategPage")
	public ModelAndView goGasEquipCategPage(){
		return new ModelAndView("gasEquipCategory/gasEquipCategList");
	}
	
	@ApiOperation("尾气排放标准管理")
	@GetMapping("goWqPfbzPage")
	public ModelAndView goWqPfbzPage(){
		return new ModelAndView("wqPfbz/wqPfbzList");
	}
	
	@ApiOperation("公司类型管理")
	@GetMapping("goCompTypePage")
	public ModelAndView goCompTypePage(){
		return new ModelAndView("compTypeManager/compTypeList");
	}
	
	@ApiOperation("公司管理")
	@GetMapping("gomCompPage")
	public ModelAndView gomCompPage(){
		return new ModelAndView("compManager/compManager");
	}
	
	@ApiOperation("公司列表")
	@GetMapping("gomCompListPage")
	public ModelAndView gomCompListPage(){
		return new ModelAndView("compManager/companyList");
	}
	
	@ApiOperation("液厂管理")
	@GetMapping("goLqFactoryPage")
	public ModelAndView goLqFactoryPage(){
		return new ModelAndView("lqFactoryManager/lqFactoryList");
	}
	
	@ApiOperation("省份管理")
	@GetMapping("goProvManagerPage")
	public ModelAndView goProvManagerPage(){
		return new ModelAndView("provManager/provList");
	}
	
	@ApiOperation("贸易商审核管理")
	@GetMapping("goCpyJoinFcyCheckPage")
	public ModelAndView goCpyJoinFcyCheckPage(){
		return new ModelAndView("applyManager/tradeApply");
	}
	
	@ApiOperation("LNG行情管理")
	@GetMapping("goLngPage") 
	public ModelAndView goLngPage(){
		return new ModelAndView("LngManager/lngManager");
	}
	
	@ApiOperation("槽车租卖管理")
	@GetMapping("goTrucksTradePage") 
	public ModelAndView goTrucksTradePage(){
		return new ModelAndView("trucksTrade/trucksTradeList");
	}
	
}
