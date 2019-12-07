package com.lng.tools;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lng.pojo.SuperDep;
import com.lng.pojo.SystemInfo;
import com.lng.service.ActSuperService;
import com.lng.service.SuperDepService;
import com.lng.service.SysConfigService;
import com.lng.util.Constants;

@Component//申明为spring组件
public class CommonTools {

	@Autowired
	private SuperDepService sds;
	@Autowired
	private ActSuperService ass;
	@Autowired
	private SysConfigService scs;
	
	public static CommonTools ct;
	
	@PostConstruct//注解@PostConstruct，这样方法就会在Bean初始化之后被Spring容器执行
    public void init() {
		ct = this;
	}
	
	/**
	 * 获取当前网址
	 * @param request
	 * @return
	 */
	public static String getWebAddress(ServletRequest request){
		String xyType = request.getScheme();
		String ym = request.getServerName();
		Integer dkh = request.getServerPort();
		String dkhChi = dkh.equals(80) ? "" : ":" + dkh;
		return xyType + "://" +  ym + dkhChi;
	}
	
	/**
	 * 获取客户端信息（上述2种方法的整合）分清安卓、ios、pc、移动浏览器
	 * @param request
	 * @return
	 */
	public static String getCilentInfo_new(HttpServletRequest request){
		String clientInfo = request.getHeader("User-agent");
		String cilentQuip = "";
		if(clientInfo != null){
			if(clientInfo.indexOf("MicroMessenger") >= 0){//微信小程序
				cilentQuip = "wxApp";
			}else if(clientInfo.indexOf("Android") >= 0 || clientInfo.indexOf("iPad") >= 0 || clientInfo.indexOf("iPhone") >= 0){
				if(clientInfo.indexOf("AppleWebKit") > 0){//手机浏览器，手机app封装的html页面
					if(clientInfo.indexOf("Html5") > 0){
						cilentQuip = "commonApp";////手机app封装html页面
					}else{
						//cilentQuip = "pc";//移动端浏览器效果同于PC
						if(clientInfo.indexOf("Android") >= 0){//安卓手机浏览器
							cilentQuip = "andriodWeb";
						}else if(clientInfo.indexOf("iPad") >= 0 || clientInfo.indexOf("iPhone") >= 0){//苹果手机浏览器
							cilentQuip = "iphoneWeb";
						}
					}
				}else{
					if(clientInfo.indexOf("Android") >= 0){//移动端APP
						cilentQuip = "andriodApp";
					}else if(clientInfo.indexOf("iPad") >= 0 || clientInfo.indexOf("iPhone") >= 0){
						cilentQuip = "iphoneApp";
					}
				}
			}else if(clientInfo.indexOf("Lavf") >= 0){//手机端播放视频时
				cilentQuip = "commonApp";////手机app封装html页面
			}else{
				cilentQuip = "pc";//PC端
			}
		}else{
			cilentQuip = "";//无法获取客户端信息
		}
		return cilentQuip;
	}
	
	/**
	 * @description 自定义字符型变量值
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午4:58:07
	 * @param inputValue
	 * @return
	 */
	public static String getFinalStr(String inputValue,HttpServletRequest request) {
		inputValue = String.valueOf(request.getParameter(inputValue));
		if(inputValue.equals("") || inputValue.equals("null")){
			return "";
		}
		return inputValue;
	}
	
	/**
	 * @description 自定义整型变量值
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午4:58:25
	 * @param inputValue
	 * @return
	 */
	public static Integer getFinalInteger(String inputValue,HttpServletRequest request) {
		inputValue = String.valueOf(request.getParameter(inputValue));
		if(inputValue.equals("") || inputValue.equals("null")){
			return 0;
		}
		return Integer.parseInt(inputValue);
	}
	
	/**
	 * @description 获取session中的用户编号
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 上午10:48:52
	 * @param request
	 * @return
	 */
	public static String getLoginUserId(HttpServletRequest request){
		String userId = "";
		userId = (String)request.getSession(false).getAttribute(Constants.LOGIN_USER_ID);
        if(userId == null){
        	return "";
        }
        return userId;
	}
	
	/**
	 * @description 获取session中的用户身份
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月7日 上午11:49:16
	 * @param request
	 * @return
	 */
	public static String getLoginRoleName(HttpServletRequest request){
		String roleName = "";
		roleName = (String)request.getSession(false).getAttribute(Constants.LOGIN_USER_ROLE_NAME);
        if(roleName == null){
        	return "";
        }
        return roleName;
	}
	
	/**
	 * @description 获取当前用户是否具有该权限
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 下午5:39:25
	 * @param userId
	 * @param actNameEng
	 * @return
	 */
	public static boolean checkAuthorization(String userId,String actNameEng) {
		//获取用户所在部门
		boolean abilityFlag = false;
		List<SuperDep> sdList = ct.sds.listSpecInfoByUserId(userId);
		if(sdList.size() > 0) {
			String roleName = sdList.get(0).getDepartment().getDepName();
			if(Constants.SUPER_DEP_ABILITY.contains(roleName)) {//拥有全部权限
				abilityFlag = true;
			}else if(ct.ass.listSpecInfoByOpt(userId, actNameEng).size() > 0){
				abilityFlag = true;
			}
		}
		return abilityFlag;
	}
	
	/**
	 * @description 获取水印
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月7日 下午2:23:55
	 * @return
	 */
	public static String getWatermark() {
		List<SystemInfo> sysList = ct.scs.findInfo();
		if(sysList.size() > 0) {
			return sysList.get(0).getWaterMark();
		}
		return "";
	}
}
