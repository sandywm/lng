package com.lng.tools;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.baidu.aip.contentcensor.AipContentCensor;
import com.lng.pojo.SuperDep;
import com.lng.pojo.SystemInfo;
import com.lng.service.ActSuperService;
import com.lng.service.SuperDepService;
import com.lng.service.SysConfigService;
import com.lng.util.Constants;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

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
	 * @description 自定义字符型变量值--页面传递
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
	 * @description 自定义字符型变量值--后台重组
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月9日 上午11:23:08
	 * @param inputValue
	 * @return
	 */
	public static String getFinalStr(String inputValue) {
		inputValue = String.valueOf(inputValue);
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
	 * 
	 * @description 自定义整型变量值--后台重组
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月9日 下午1:58:56
	 * @param inputValue 传的值
	 * @return
	 */

	public static Integer getFinalInteger(Integer inputValue) {
		if(inputValue==null){
			return 0;
		}
	  return inputValue;
	}
	
	/**
	 * @description 自定义double变量值--后台重组
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月11日 下午2:03:16
	 * @param inputData
	 * @param request
	 * @return
	 */
	public static Double getFinalDouble(String inputData,HttpServletRequest request){
		inputData = String.valueOf(request.getParameter(inputData));
		if(inputData.equals("") || inputData.equals("null")  || inputData.equals("undefined")){
			return 0.0;
		}else{
			return Double.parseDouble(inputData);
		}
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
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		String userId = "";
		if(cilentInfo.equals("pc")) {
			userId = (String)request.getSession(false).getAttribute(Constants.LOGIN_USER_ID);
	        if(userId == null){
	        	return "";
	        }
		}else if(cilentInfo.equals("wxApp")) {
			userId = CommonTools.getFinalStr("userId", request);
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
	public static boolean checkAuthorization(String userId,String roleName,String actNameEng) {
		//获取用户所在部门
		boolean abilityFlag = false;
		List<SuperDep> sdList = ct.sds.listSpecInfoByUserId(userId);
		if(sdList.size() > 0) {
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
	
	/**
	 * @description 获取中文首字母
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月9日 上午10:37:41
	 * @param inputStr_chi
	 * @return
	 */
	public static String getFirstSpell(String inputStr_chi){
		StringBuffer pybf = new StringBuffer();   
        char[] arr = inputStr_chi.toCharArray();   
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();   
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);   
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  
        for (int i = 0; i < arr.length; i++) {   
            if (arr[i] > 128) {   
                    try {   
                            String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);   
                            if (temp != null) {   
                                    pybf.append(temp[0].charAt(0));   
                            }   
                    } catch (BadHanyuPinyinOutputFormatCombination e) {   
                            e.printStackTrace();   
                    }   
            } else {   
                    pybf.append(arr[i]);   
            }   
        }   
        return pybf.toString().replaceAll("\\W", "").trim(); 
	}
	
	/**
	 * @description 保留2位小数四舍五入
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月11日 下午1:26:05
	 * @param inputD
	 * @return
	 */
	public static String convertInputNumber(double inputD){
		DecimalFormat    df   = new DecimalFormat("######0.00");   
		return df.format(inputD);
	}
	
	
	/**
	 * @description 保存时处理之前上传的图片(0.5压缩率)
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月11日 下午2:46:22
	 * @param upFileStr 上传的图片地址
	 * @param upFileDb 已存数据库的地址(增加时为空，修改时为数据库字段信息)
	 * @throws Exception 
	 */
	public static String dealUploadDetail(String loingUserId,String upFileDb,String upFileStr) throws Exception {
		String finalPath = "";
		if(!upFileStr.equals("") && !loingUserId.equals("")) {
			String[] upFileArr = upFileStr.split(",");
			String imagePath = Constants.UPLOAD_PATH + loingUserId;
			File filePath = new File(imagePath);
			if(!filePath.exists()) {
				filePath.mkdirs();
			}
			if(upFileDb.equals("")) {//新增时上传
				for(int i = 0 ; i < upFileArr.length ; i++) {
					//复制原图
					FileOpration.copyFile(Constants.UPLOAD_PATH+"temp/"+upFileArr[i], imagePath+"/"+upFileArr[i]);
					Integer lastIndex = upFileArr[i].lastIndexOf(".");
					String suffix = upFileArr[i].substring(lastIndex+1);
					String newFileNamePre = upFileArr[i].substring(0, lastIndex);
					String formatName = FileOpration.getImageFormat(suffix);
					if(!formatName.equals("")) {
						String newFileName = newFileNamePre+"_small."+suffix;
						String newUrl = imagePath+"/"+newFileName;
						FileOpration.makeImage(imagePath+"/"+upFileArr[i], 0.5, newUrl, formatName);
						finalPath += loingUserId + "/" + newFileName + ",";
					}
					//删除临时文件夹图片
					FileOpration.deleteFile(Constants.UPLOAD_PATH+"temp/"+upFileArr[i]);
				}
			}else {//编辑时上传
				for(int i = 0 ; i < upFileArr.length ; i++) {
					//复制原图
					upFileDb = upFileDb.replaceAll("_small", "");
					if(upFileDb.contains(upFileArr[i])) {//旧图
						Integer lastIndex = upFileArr[i].lastIndexOf(".");
						String suffix = upFileArr[i].substring(lastIndex+1);
						String newFileNamePre = upFileArr[i].substring(0, lastIndex);
						finalPath += newFileNamePre+"_small."+suffix + ",";
					}else {//新增加的图
						FileOpration.copyFile(Constants.UPLOAD_PATH+"temp/"+upFileArr[i], imagePath+"/"+upFileArr[i]);
						Integer lastIndex = upFileArr[i].lastIndexOf(".");
						String suffix = upFileArr[i].substring(lastIndex+1);
						String newFileNamePre = upFileArr[i].substring(0, lastIndex);
						String formatName = FileOpration.getImageFormat(suffix);
						if(!formatName.equals("")) {
							String newFileName = newFileNamePre+"_small."+suffix;
							String newUrl = imagePath+"/"+newFileName;
							FileOpration.makeImage(imagePath+"/"+upFileArr[i], 0.5, newUrl, formatName);
							finalPath += loingUserId + "/" + newFileName + ",";
						}
						//删除临时文件夹图片
						FileOpration.deleteFile(Constants.UPLOAD_PATH+"temp/"+upFileArr[i]);
					}
				}
			}
			if(!finalPath.equals("")) {
				finalPath = finalPath.substring(0, finalPath.length() - 1);
			}
		}
		return finalPath;
	}
	
	public static Integer autoCheckMsg(String msg) {
//		AipContentCensor client = new AipContentCensor(Constants.APP_ID, Constants.APP_KEY, Constants.SECRET_KEY);
//		String content = "操你妈";
//		org.json.JSONObject response = client.antiSpam(content, null);
//	    System.out.println(response.toString());
		return null;
	}
	
	public static void main(String[] args) {
		String a = "河南省,安徽省,山西省,安徽省,黑龙江省";
		String b = "台湾省,安徽省";
		System.out.println(a.indexOf(b));
		System.out.println(CommonTools.getFirstSpell("我们的家"));
//		CommonTools.autoCheckMsg("");
	}
}
