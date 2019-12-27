package com.lng.config;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lng.tools.Ability;
import com.lng.tools.CommonTools;
import com.lng.util.Constants;

//启用AOP做登录拦截
@Component//把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>
@Aspect//作用是把当前类标识为一个切面供容器读取
public class LoginIntercept {
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
	        if(session != null) {
	        	userAcc = String.valueOf(session.getAttribute(Constants.LOGIN_ACCOUNT));
	        }
	        if (userAcc.equals("null") || userAcc.equals("")) {
	        	System.out.println("用户未登陆开始拦截" + uri);
	        	if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
	        		// 如果是 ajax 请求，则设置 session 状态 、CONTEXTPATH 的路径值
	             	// 如果是ajax请求响应头会有，x-requested-with
	        		System.out.println("session丢失，拦截(ajax请求)---" + uri);
	        		response.setHeader("SESSIONSTATUS", "TIMEOUT");
	        		response.setHeader("CONTEXTPATH", "/loginOut");
	                // FORBIDDEN，forbidden。也就是禁止、403
	        		response.setStatus(HttpServletResponse.SC_FORBIDDEN); 
	        		String url = "window.top.location.href = '"+CommonTools.getWebAddress(request)+"/loginOut'";
					String authorizeScript = "由于您60分钟内没上线，系统已强制您下线，请重新登录！";
					Ability.PrintAuthorizeScript(url,authorizeScript, response);
	            }else {
	            	// 如果不是 ajax 请求，则直接跳转即可
	            	System.out.println("session丢失，拦截(不是ajax请求)---" + uri);
	            	String url = "window.top.location.href = '"+CommonTools.getWebAddress(request)+"/loginOut'";
					String authorizeScript = "由于您60分钟内没上线，系统已强制您下线，请重新登录！";
					Ability.PrintAuthorizeScript(url,authorizeScript, response);
	            }
	        	return null;
	        }else {
	        	System.out.println("存在session，放过拦截---" + uri);
	        }
		}
    	return pjp.proceed();
    }
}
