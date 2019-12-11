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

import com.lng.pojo.Department;
import com.lng.pojo.SuperDep;
import com.lng.pojo.SuperUser;
import com.lng.service.SuperDepService;
import com.lng.service.SuperService;
import com.lng.tools.AuthImg;
import com.lng.tools.CurrentTime;
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
						//修改登录次数和最后登录时间
						su.setLastLoginTime(CurrentTime.getCurrentTime());
						Integer loginTimes = su.getLoginTimes();
						if(loginTimes < 50) {
							loginTimes++;
						}else {
							loginTimes = 0;
						}
						su.setLoginTimes(loginTimes);
						ss.addOrUpUser(su);
						//获取该用户所有身份列表
						List<SuperDep> sdList = sds.listSpecInfoByUserId(su.getId());
						if(sdList.size() > 0) {
							for(SuperDep sd : sdList) {
								Department role = sd.getDepartment();
								Map<String,Object> map_d = new HashMap<String,Object>();
								map_d.put("roleId", role.getId());
								map_d.put("roleName", role.getDepName());
								list_d.add(map_d);
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
}
