package com.lng.util;

public class Constants {

	public static final String LAST_LOGIN_DATE = "last_login_date";
	public static final String LOGIN_STATUS = "login_status";
	public static final String LOGIN_USER_ID = "login_user_id";
	public static final String LOGIN_ACCOUNT = "login_account";
	public static final String LOGIN_REAL_NAME = "login_real_name";
	public static final String LOGIN_USER_ROLE_NAME = "login_user_role_name";
	public static final String SUPER_DEP_ABILITY = "超级管理员";
	
	public static final String APP_ID = "18029109";
	public static final String APP_KEY = "UlEHwsaCkkPWZl3Tf4VIwGbn";
	public static final String SECRET_KEY = "pnE5G7xTGntfUSnpTdzVXM3I2f6mcOsL";
	
	public static final String UPLOAD_PATH = "D:/lngWeb/resources/";
	
	/*--------------------------hql语句---------------------------------------------------------*/
	public static final String GAS_TRADE_HQL = "gas_trade_hql";

//	系统配置管理
	/**
	 * 进港资质操作
	 */
	public static final String JGZZ_ABILITY = "goJgZzPage";
	/**
	 * 气源类型操作
	 */
	public static final String QYLX_ABILITY = "goGasTypePage";
	/**
	 * 液质类型操作
	 */
	public static final String YZLX_ABILITY = "goLqTypePage";
	/**
	 * 燃气设备类目操作
	 */
	public static final String RQSBLM_ABILITY = "dealRqsblm";
	/**
	 * 燃气类型操作
	 */
	public static final String RQLX_ABILITY = "dealRqlx";
	/**
	 * 货车类型操作
	 */
	public static final String CCLX_ABILITY = "goTrucksTypePage";
	/**
	 * 货车储罐品牌操作
	 */
	public static final String CGPP_ABILITY = "dealHccgpp";
	/**
	 * 货车车头类型操作
	 */
	public static final String CCCTLX_ABILITY = "dealHcctlx";
	/**
	 * 货车车头品牌操作
	 */
	public static final String CCCTPP_ABILITY = "dealHcctpp";
	/**
	 * 装载介质操作
	 */
	public static final String ZZJZ_ABILITY = "dealZzjz";
	/**
	 * 职位操作
	 */
	public static final String ZW_ABILITY = "dealZw";
	/**
	 * 尾气排放操作
	 */
	public static final String WQPP_ABILITY = "dealWqpp";
	/**
	 * 公司类型操作
	 */
	public static final String GSLX_ABILITY = "dealGslx";
	
	
	public static final String ADD_ROLE = "addRole";// 角色
	public static final String LIST_ROLE = "listRole";
	public static final String UP_ROLE = "upRole";

	public static final String LIST_SJZP = "listSjzp";// 司机招聘
	public static final String CHECK_SJZP = "checkSjzp";

	public static final String LIST_RQSB = "listRqsb";// 燃气设备
	public static final String CHECK_RQSB = "checkRqsb";

	public static final String SET_ABILITY = "setAbility";// 设置权限

	public static final String ADD_MOD = "addMod";// 增加模块
	public static final String UP_MOD = "upMod";// 修改模块

	public static final String ADD_USER = "addUser";// 用户
	public static final String UP_USER = "upUser";

//	public static final String ADD_COMT = "add_comt";// 添加公司类型
//	public static final String UP_COMT = "up_comt"; // 修改公司类型

//	public static final String ADD_GAST = "add_gast";// 添加液质类型
//	public static final String UP_GAST = "up_gast"; // 修改液质类型

//	public static final String ADD_ZZJZ = "add_zzjz";// 添加装载介质类型
//	public static final String UP_ZZJZ = "up_zzjz"; // 修改装载介质类型

//	public static final String ADD_QUAL = "add_qual";// 添加进港资质
//	public static final String UP_QUAL = "up_qual"; // 修改进港资质

//	public static final String ADD_QYT = "add_qyt";// 添加气源类型
//	public static final String UP_QYT = "up_qyt"; // 修改气源类型

//	public static final String ADD_RQDEVTYPE = "add_rqdevtype";// 添加燃气设备类目
//	public static final String UP_RQDEVTYPE = "up_rqdevtype"; // 修改燃气设备类目

//	public static final String ADD_RQTYPE = "add_rqtype";// 添加燃气设备类型
//	public static final String UP_RQTYPE = "up_rqtype"; // 修改燃气设备类型

//	public static final String ADD_THPP = "add_thpp";// 添加槽车车头品牌
//	public static final String UP_THPP = "up_thpp"; // 修改槽车车头品牌

//	public static final String ADD_THT = "add_tht";// 添加槽车车头类型
//	public static final String UP_THT = "up_tht"; // 修改槽车车头类型

//	public static final String ADD_TPPP = "add_tppp";// 添加槽车储罐品牌
//	public static final String UP_TPPP = "up_tppp"; // 修改槽车储罐品牌

//	public static final String ADD_PFBZ = "add_pfbz";// 添加尾气排放标准
//	public static final String UP_PFBZ = "up_pfbz"; // 修改尾气排放标准

//	public static final String ADD_TT = "add_tt";// 添加槽车类型
//	public static final String UP_TT = "up_tt"; // 修改槽车类型
	
	public static final String ADD_USER_ROLE = "addUserRole";//用户角色
	public static final String UP_USER_ROLE = "UPUserRole";
	
	public static final String ADD_YC = "addYc";//添加液厂
	public static final String UP_YC = "upyc";//修改液厂
	
	public static final String ADD_COMPANY = "addCompany";//添加公司
	public static final String UP_COMPANY = "upCompany";//修改公司
	
	public static final String ADD_PSR = "addPsr";//添加公司司机押运人
	public static final String UP_PSR = "upPsr";//修改公司司机押运人
	
	public static final String ADD_ZZ = "addZz";//添加公司执照
	public static final String UP_ZZ = "upZz";//修改公司执照
	
	public static final String ADD_HEADCP = "addHeadcp";//添加公司车头车牌
	public static final String UP_HEADCP = "upHeadcp";//修改公司车头车牌
	
	public static final String ADD_GCCP = "addGccp";//添加公司挂车车牌
	public static final String UP_GCCP = "upGccp";//修改公司挂车车牌
	
	public static final String ADD_PROV = "addProv";//添加省份
	public static final String UP_PROV = "upProv";//修改省份
	
	public static final String ADD_PT = "addPt";//添加储罐租卖
	public static final String UP_PT = "upPt";//修改储罐租卖
	
	public static final String ADD_RDT = "addRdt";//添加燃气设备买卖
	public static final String UP_RDT = "upRdt";//修改燃气设备买卖
	
	public static final String ADD_TRTR = "addTrtr";//添加货车租卖
	public static final String UP_TRTR = "upTrtr";//修改货车租卖
	public static final String ADD_QZ = "addQz";//添加司机求职
	public static final String UP_QZ = "upQz";//修改司机求职
	public static final String ADD_ZP = "addZp";//添加司机招聘
	public static final String UP_ZP = "upZp";//修改司机招聘
	
	public static final String ADD_LNG_PRICE = "addLngPrice";//添加lng价格
	public static final String UP_LNG_PRICE = "upLngPrice";//修改lng价格
	
	public static final String ADD_GAS_TRADE = "addGasTrade";//添加燃气买卖
	public static final String UP_GAS_TRADE = "upGasTrade";//修改燃气买卖
	
	public static final String ADD_GTO = "addGto";//添加燃气交易订单
	public static final String UP_GTO = "upGto";//修改燃气交易订单
	
	public static final String CHECK_GAS_FCY_CPY_APPLY = "goCpyJoinFcyCheckPage";//贸易商加入液厂的申请审核
	public static final String CHECK_USER_JOIN_CPY_APPLY = "goUserJoinCpyCheckPage";//用户加入公司的申请审核
	public static final String CHECK_CPY_APPLY = "goCpyCheckPage";//审核公司信息
	public static final String CHECK_TRUCK_PUB = "goTruckCheckPage";//审核货车租卖发布信息
	public static final String CHECK_GAS_TRADE_PUB = "goGasTradeCheckPage";//审核燃气买卖发布信息
	public static final String CHECK_GAS_DEV_TRADE_PUB = "goGasDevTradePage";//审核燃气设备买卖发布信息
	public static final String CHECK_POT_TRADE_PUB = "goPotTradePage";//审核储罐租卖发布信息
	public static final String CHECK_DRIVER_QZ_PUB = "goDriverQzPage";//审核司机求职发布信息
	public static final String CHECK_DRIVER_ZP_PUB = "goDriverZpPage";//审核司机招聘发布信息
	public static final String CHECK_GAS_FACTORY_PUB = "goGasFactotyPage";//审核液厂
	
	
	
	
}

