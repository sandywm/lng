package com.lng.config;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lng.pojo.SuperUser;
import com.lng.pojo.User;
import com.lng.service.SuperService;
import com.lng.tools.Ability;
import com.lng.tools.CommonTools;
import com.lng.util.Constants;

//启用AOP做登录拦截
@Component//把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>
@Aspect//作用是把当前类标识为一个切面供容器读取
public class LoginIntercept {
	
	@Autowired
	private SuperService ss;
	
	//!within必须要和within同时出现，不然会报错
	@Pointcut("within(com.lng.controller..*) && !within(com.lng.controller.Login)")
	
    public void pointCut() {}
	
    @Around("pointCut()")//环绕通知
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		//获取当前设备信息
		String clientInfo = CommonTools.getCilentInfo_new(request);
		if(clientInfo.equals("wxApp")){//微信小程序不检验
			
		}else{
			HttpSession session= request.getSession(false);
			String uri = request.getRequestURI();
			String userAcc = "";
			Integer loginTimes = -1;
	        if(session != null) {
	        	userAcc = String.valueOf(session.getAttribute(Constants.LOGIN_ACCOUNT));
	        	loginTimes = Integer.parseInt(String.valueOf(session.getAttribute(Constants.LOGIN_TIMES)));
	        }
	        if (userAcc.equals("null") || userAcc.equals("")) {
//	        	System.out.println("用户未登陆开始拦截" + uri);
	        	if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
	        		// 如果是 ajax 请求，则设置 session 状态 、CONTEXTPATH 的路径值
	             	// 如果是ajax请求响应头会有，x-requested-with
//	        		System.out.println("session丢失，拦截(ajax请求)---" + uri);
	        		response.setHeader("SESSIONSTATUS", "TIMEOUT");
	        		response.setHeader("CONTEXTPATH", "/loginOut");
	                // FORBIDDEN，forbidden。也就是禁止、403
	        		response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
	        		String url = "window.top.location.href = '"+CommonTools.getWebAddress(request)+"/loginOut'";
					String authorizeScript = "由于您60分钟内没上线，系统已强制您下线，请重新登录！";
					Ability.PrintAuthorizeScript(url,authorizeScript, response);
	            }else {
	            	// 如果不是 ajax 请求，则直接跳转即可
//	            	System.out.println("session丢失，拦截(不是ajax请求)---" + uri);
	            	String url = "window.top.location.href = '"+CommonTools.getWebAddress(request)+"/loginOut'";
					String authorizeScript = "由于您60分钟内没上线，系统已强制您下线，请重新登录！";
					Ability.PrintAuthorizeScript(url,authorizeScript, response);
	            }
	        	return null;
	        }else {
//	        	System.out.println("存在session，放过拦截---" + uri);
	        	List<SuperUser> userList = ss.findInfoByOpt(userAcc, "");
	        	if(userList.size() > 0) {
	        		Integer loginTimesDb = userList.get(0).getLoginTimes();
	        		if(!loginTimes.equals(loginTimesDb)) {
	        			String url = "window.top.location.href = '"+CommonTools.getWebAddress(request)+"/loginOut'";
	        			String authorizeScript = "该账号已在别处登录，请重新登录！";
	        			if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
	        				response.setHeader("SESSIONSTATUS", "TIMEOUT");
	    	        		response.setHeader("CONTEXTPATH", "/loginOut");
	    	        		response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
	        			}
	        			Ability.PrintAuthorizeScript(url,authorizeScript, response);
	        			return null;
	        		}
	        	}
	        }
		}
    	return pjp.proceed();
    }
}
