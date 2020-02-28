package com.lng.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.lng.pojo.Department;
import com.lng.pojo.SuperDep;
import com.lng.pojo.SuperUser;
import com.lng.pojo.User;
import com.lng.service.SuperDepService;
import com.lng.service.SuperService;
import com.lng.service.UserService;
import com.lng.tools.AuthImg;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.tools.EmojiDealUtil;
import com.lng.tools.WxTools;
import com.lng.util.Constants;
import com.lng.util.GenericResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * GET     获取一个资源 
 * POST    添加一个资源 
 * PUT     修改一个资源 
 * DELETE  删除一个资源 
 * @author wm
 * @Version : 1.0
 * @ModifiedBy : 修改人
 * @date  2019年12月4日 下午4:35:25
 */

@RestController
@Api(tags = "1--用户登录之前相关接口")
public class Login {

	@Autowired
	public SuperService ss;
	
	@Autowired
	public SuperDepService sds;
	
	@Autowired
	public UserService us;
	
	@ApiOperation("后台用户登录首页")
	@GetMapping("goLoginPage")
	public ModelAndView welcome(){
		return new ModelAndView("index");
	}
	
	@ApiOperation("后台用户登出系统")
	@GetMapping("loginOut")
	public ModelAndView loginOut(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		return new ModelAndView("index");
	}
	
	@ApiOperation("生成随机验证码")
	@GetMapping("getVerify")
	public void getVerify(HttpServletRequest request,HttpServletResponse response){
		AuthImg au = new AuthImg();
		try {
			au.getRandcode(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@ApiOperation("后台用户登录动作接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "account", value = "用户账号", defaultValue = "wmk", required = true),
		@ApiImplicitParam(name = "password", value = "用户密码", defaultValue = "123456", required = true),
		@ApiImplicitParam(name = "inputCode", value = "验证码", required = true),
	})
	@GetMapping("superLogin")
	@ApiResponses({@ApiResponse(code = 20002, message = "账号不存在或密码错误"),
				   @ApiResponse(code = 10002, message = "参数为空"),
				   @ApiResponse(code = 40001, message = "系统繁忙，请稍后重试"),
				   @ApiResponse(code = 20006, message = "验证码错误"),
				   @ApiResponse(code = 20003, message = "账号已被禁用")
	})
	public GenericResponse superLogin(HttpServletRequest request,String account,String password,String inputCode) {
		Integer status = 40001;
		List<Object> list_d = new ArrayList<Object>();
		HttpSession sess = request.getSession(true);
		if(account == null || password == null || inputCode == null) {
			status = 10002;
		}else {
			String sessionCode = String.valueOf(sess.getAttribute("rand"));
			if(sessionCode.equals(inputCode)) {
				List<SuperUser> sList = ss.findInfoByOpt(account, password);
				if(sList.size() > 0) {
					SuperUser su = sList.get(0);
					if(su.getAccountStatus().equals(1)) {
						sess.setAttribute(Constants.LOGIN_ACCOUNT, account);
						sess.setAttribute(Constants.LOGIN_USER_ID, su.getId());
						sess.setAttribute(Constants.LOGIN_REAL_NAME, su.getRealName());
						//修改登录次数和最后登录时间
						su.setLastLoginTime(CurrentTime.getCurrentTime());
						Integer loginTimes = su.getLoginTimes();
						if(loginTimes < 50) {
							loginTimes++;
						}else {
							loginTimes = 0;
						}
						su.setLoginTimes(loginTimes);
						sess.setAttribute(Constants.LOGIN_TIMES, loginTimes);
						ss.addOrUpUser(su);
						//获取该用户所有身份列表
						List<SuperDep> sdList = sds.listSpecInfoByUserId(su.getId());
						if(sdList.size() > 0) {
							Integer i = 0;
							for(SuperDep sd : sdList) {
								Department role = sd.getDepartment();
								Map<String,Object> map_d = new HashMap<String,Object>();
								map_d.put("roleId", role.getId());
								if(!role.getDepName().equals("超级管理员")) {
									if(i.equals(0)) {
										map_d.put("roleName", "平台管理员");
										list_d.add(map_d);
									}
									i++;
								}else {
									map_d.put("roleName", role.getDepName());
									list_d.add(map_d);
								}
							}
						}
						status = 200;
					}else{
						status = 20003;
					}
				}else {
					status = 20002;
				}
			}else {
				status = 20006;
			}
		}
		return ResponseFormat.retParam(status, list_d);
	}	
	
	@ApiOperation("微信小程序授权接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "临时登录凭证", required = true)
	})
	@GetMapping("wxAuth")
	@ApiResponses({@ApiResponse(code = 10002, message = "参数为空"),
			@ApiResponse(code = 1000, message = "服务器错误"),
			@ApiResponse(code = 20008, message = "授权失败")
	})
	public GenericResponse wxAuth(HttpServletRequest request) {
		Integer status = 40001;
		String code = CommonTools.getFinalStr("code", request);
		List<Object> list_d = new ArrayList<Object>();
		if(code.equals("")) {
			status = 10002;
		}else {
			try {
				JSONObject jsonObject = WxTools.code2sessionKey(code);
				String openId = jsonObject.getString("openid");// 用户唯一标识
				String sessionKey = jsonObject.getString("session_key");// 密钥
				// 满足UnionID下发条件的情况下，返回
				String unionId = jsonObject.getString("unionid");
				if(openId == null) {
					status = 20008;
				}else {
					Map<String,String> map = new HashMap<String,String>();
					map.put("openId", openId);
					map.put("sessionKey", sessionKey);
					//关于unionId,这里需要说明一下,如果应用只限于小程序内则不需要unionId,直接通过openId可以确定用户身份,
					//但是如果需要跨应用 如:网页应用,app应用时则需要使用到unionId作为身份标识
					map.put("unionId", unionId);
					list_d.add(map);
					status = 200;
				}
				status = 200;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				status = 1000;
			}			
		}
		return ResponseFormat.retParam(status, list_d);
	}	
	
	@ApiOperation("微信用户登录动作接口")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "code", value = "临时登录凭证", required = true),
		@ApiImplicitParam(name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据(头像、昵称等)", required = true),
		@ApiImplicitParam(name = "iv", value = "加密算法的初始向量", required = true),
		@ApiImplicitParam(name = "sessionKey", value = "sessionKey"),
		@ApiImplicitParam(name = "openId", value = "openId")
	})
	@GetMapping("wxUserLogin")
	@ApiResponses({@ApiResponse(code = 20002, message = "账号不存在错误"),
				   @ApiResponse(code = 10002, message = "参数为空"),
				   @ApiResponse(code = 40001, message = "系统繁忙，请稍后重试"),
				   @ApiResponse(code = 20003, message = "账号已被禁用"),
				   @ApiResponse(code = 20008, message = "授权失败")
	})
	public GenericResponse wxUserLogin(HttpServletRequest request) {
		Integer status = 40001;
		String code = CommonTools.getFinalStr("code", request);
		String encryptedData = CommonTools.getFinalStr("encryptedData", request);
		String iv = CommonTools.getFinalStr("iv", request);
		String sessionKey = CommonTools.getFinalStr("sessionKey", request);
		String wxOpenId = CommonTools.getFinalStr("openId", request);
		List<Object> list_d = new ArrayList<Object>();
		if(code.equals("") || encryptedData.equals("") || iv.equals("")) {
			status = 10002;
		}else {
				try {
					String currTime = CurrentTime.getCurrentTime();
					// 解密encryptedData,获取unionId相关信息
					JSONObject json = WxTools.decryptionUserInfo(encryptedData, sessionKey, iv);
					if(json == null) {
						status = 20008;
					}else {
						String nickName = json.getString("nickName");//昵称
						String sex = json.getString("gender");//性别
//						String prov = json.getString("province");//省
//						String city = json.getString("country");//市
						String headImg = json.getString("avatarUrl");//头像
						User user = us.getEntityByWxOpenId(wxOpenId);
						if(user != null) {
							if(user.getAccountStatus().equals(1)) {
								//修改登录次数和最后登录时间
								user.setUserPortrait(headImg);
								user.setLastLoginTime(currTime);
								Integer loginStatus = user.getLoginStatus();
								if(loginStatus < 50) {
									loginStatus++;
								}else {
									loginStatus = 0;
								}
								user.setLoginTimes(user.getLoginStatus()+1);
								user.setLoginStatus(loginStatus);
								us.saveAndUpdate(user);
								Map<String,String> map = new HashMap<String,String>();
								map.put("userId", user.getId());
								map.put("wxOpenId", user.getAccount());
								map.put("wxName", EmojiDealUtil.changeStrToEmoji(user.getWxName()));
								list_d.add(map);
								status = 200;
							}else{
								status = 20003;
							}
						}else {
							//首次授权，注册账号
							if(sex.equals("1")) {
								sex = "男";
							}else {
								sex = "女";
							}
							String wxName = EmojiDealUtil.changeEmojiToHtml(nickName);
							String userId = us.saveAndUpdate(new User(wxOpenId, "E10ADC3949BA59ABBE56E057F20F883E", wxName, wxName, sex, "",
									currTime, "",currTime, 1, 1, 1,"wx", headImg, ""));
							Map<String,String> map = new HashMap<String,String>();
							map.put("userId", userId);
							map.put("wxOpenId", wxOpenId);
							map.put("wxName", nickName);
							list_d.add(map);
							status = 200;
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					status = 40001;
				}			
		}
		return ResponseFormat.retParam(status, list_d);
	}	
}
