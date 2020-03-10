package com.lng.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.User;
import com.lng.service.UserService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.tools.EmojiDealUtil;
import com.lng.util.Constants;
import com.lng.util.GenericResponse;
import com.lng.util.PageResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "前台用户相关接口")
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/addUser")
	@ApiOperation(value = "添加前台用户", notes = "添加前台用户")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "account", value = "账号", required = true),
			@ApiImplicitParam(name = "wxName", value = "微信名称"), @ApiImplicitParam(name = "realName", value = "真实姓名"),
			@ApiImplicitParam(name = "sex", value = "性别"), @ApiImplicitParam(name = "mobile", value = "手机号"),
			@ApiImplicitParam(name = "userPortrait", value = "用户头像"), })
	public GenericResponse addUser(HttpServletRequest request) {
		String account = CommonTools.getFinalStr("account", request);
		String wxName = CommonTools.getFinalStr("wxName", request);
		String realName = CommonTools.getFinalStr("realName", request);
		String sex = CommonTools.getFinalStr("sex", request);
		String mobile = CommonTools.getFinalStr("mobile", request);
		String userPortrait = CommonTools.getFinalStr("userPortrait", request);
		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;
		String uId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.ADD_USER)) {
			try {
				User user = new User();
				user.setAccount(account);
				user.setPassword("");
				user.setWxName(EmojiDealUtil.changeEmojiToHtml(wxName));
				user.setRealName(EmojiDealUtil.changeEmojiToHtml(realName));
				user.setSex(sex);
				user.setMobile(mobile);
				user.setSignDate(CurrentTime.getCurrentTime());
				user.setLastLoginTime(CurrentTime.getCurrentTime());
				user.setLoginTimes(0);
				user.setAccountStatus(1);
				user.setAccountType("wx");
				// user.setUserPortrait(userPortrait);
				if (!userPortrait.equals("")) {
					user.setUserPortrait(CommonTools.dealUploadDetail(loginUserId,"", userPortrait));
				}
				user.setSpecFun("");

				uId = userService.saveAndUpdate(user);

			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, uId);
	}

	@GetMapping("/queryUserByWxName")
	@ApiOperation(value = "获取用户", notes = "获取用户分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "wxName", value = "微信名称"),
			@ApiImplicitParam(name = "page", value = "第几页"), @ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse queryUserByWxName(String wxName, Integer page, Integer limit) {
		Integer status = 200;
		Page<User> users = null;
		long count = 0;
		List<Object> list = new ArrayList<Object>();
		try {
			wxName = CommonTools.getFinalStr(wxName);
			if (page == null) {
				page = 1;
			}
			if (limit == null) {
				limit = 10;
			}
			users = userService.listPageInfoByWxName(wxName, page, limit);
			count = users.getTotalElements();
			if (count == 0) {
				status = 50001;
			}else {
				for(User user : users) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", user.getId());
					map.put("account", user.getAccount());
					map.put("wxName", EmojiDealUtil.changeStrToEmoji(user.getWxName()));
					map.put("realName", EmojiDealUtil.changeStrToEmoji(user.getRealName()));
					map.put("sex", user.getSex());
					map.put("mobile", user.getMobile());
					map.put("birthday", user.getBirthday());
					map.put("loginStatus", user.getLoginStatus());
					map.put("accountStatus", user.getAccountStatus());
					map.put("accountType", user.getAccountType());
					map.put("userPortrait", user.getUserPortrait());
					map.put("specFun", user.getSpecFun());
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, count, status, list);
	}
	
	@GetMapping("/getSpecUserDetail")
	@ApiOperation(value = "获取指定前台用户明细", notes = "获取指定前台用户明细")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "前台用户编号")})
	public GenericResponse getSpecUserDetail(HttpServletRequest request) {
		String userId = CommonTools.getFinalStr("userId", request);
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			User user = userService.getEntityById(userId);
			if (user == null) {
				status = 50001;
			}else {
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", userId);
				map.put("account", user.getAccount());
				map.put("password", user.getPassword());
				map.put("wxName", EmojiDealUtil.changeStrToEmoji(user.getWxName()));
				map.put("realName", EmojiDealUtil.changeStrToEmoji(user.getRealName()));
				map.put("sex", user.getSex());
				map.put("mobile", user.getMobile());
				map.put("birthday", user.getBirthday());
				map.put("userHead", user.getUserPortrait());
				map.put("specFun", user.getSpecFun());
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@PutMapping("/updateUser")
	@ApiOperation(value = "更新用户自己基本信息", notes = "更新用户自己基本信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到")})
	@ApiImplicitParams({
		    @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "wxName", value = "微信名称"),
			@ApiImplicitParam(name = "realName", value = "真实姓名"),
			@ApiImplicitParam(name = "mobile", value = "手机号"),
			@ApiImplicitParam(name = "sex", value = "性别"),
//			@ApiImplicitParam(name = "userPortrait", value = "用户头像"),
			@ApiImplicitParam(name = "birthday", value = "生日")
	})
	public GenericResponse updateUser(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("userId", request);
		String wxName = CommonTools.getFinalStr("wxName", request);
		String realName = CommonTools.getFinalStr("realName", request);
		String mobile = CommonTools.getFinalStr("mobile", request);
		String sex = CommonTools.getFinalStr("sex", request);
//		String userPortrait = CommonTools.getFinalStr("userPortrait", request);
		String birthday = CommonTools.getFinalStr("birthday", request);
		Integer status = 200;
		try {
			User user = userService.getEntityById(id);
			if (user == null) {
				status = 50001;
			} else {
				if(!wxName.isEmpty()) {
					user.setWxName(EmojiDealUtil.changeEmojiToHtml(wxName));
				}
				if(!realName.isEmpty()) {
					user.setRealName(EmojiDealUtil.changeEmojiToHtml(realName));
				}
				if(!mobile.isEmpty()&& ! mobile.equals(user.getMobile())) {
					user.setMobile(mobile);
				}
				if(!sex.isEmpty() && ! sex.equals(user.getSex())) {
					user.setSex(sex);
				}
//				if (!userPortrait.isEmpty() && !userPortrait.equals(user.getUserPortrait())) {
////					user.setUserPortrait(CommonTools.dealUploadDetail(loginUserId,user.getUserPortrait(), userPortrait));
//					user.setUserPortrait(userPortrait);
//				}
				if(!birthday.equals("")) {
					user.setBirthday(birthday);
				}
				userService.saveAndUpdate(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	@PutMapping("/updateUserBySta")
	@ApiOperation(value = "更新用户审核--审核微信用户", notes = "更新用户审核信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({
		    @ApiImplicitParam(name = "id", value = "用户编号", required = true),
			@ApiImplicitParam(name = "accSta", value = "账号状态0:无效,1:有效")
	})
	public GenericResponse updateUserBySta(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id", request);
		Integer accSta = CommonTools.getFinalInteger("accSta", request);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.UP_USER)) {
			try {
				User user = userService.getEntityById(id);
				if (user == null) {
					status = 50001;
				} else {
					if(accSta != null && !accSta.equals(user.getAccountStatus())) {
						user.setAccountStatus(accSta);
					}
					userService.saveAndUpdate(user);
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
}
