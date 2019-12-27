package com.lng.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.CommonProvinceOrder;
import com.lng.pojo.Company;
import com.lng.pojo.CompanyPsr;
import com.lng.pojo.GasFactory;
import com.lng.pojo.GasFactoryCompany;
import com.lng.pojo.GasTrade;
import com.lng.pojo.GasTradeImg;
import com.lng.pojo.GasTradeOrder;
import com.lng.pojo.GasType;
import com.lng.pojo.LngPriceDetail;
import com.lng.pojo.MessageCenter;
import com.lng.pojo.User;
import com.lng.pojo.UserCompany;
import com.lng.service.CommonProvinceOrderService;
import com.lng.service.CompanyPsrService;
import com.lng.service.CompanyService;
import com.lng.service.GasFactoryCompanyService;
import com.lng.service.GasFactoryService;
import com.lng.service.GasTradeOrderService;
import com.lng.service.GasTradeService;
import com.lng.service.GasTypeService;
import com.lng.service.MessageCenterService;
import com.lng.service.UserCompanyService;
import com.lng.service.UserService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
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
@Api(tags = "燃气买卖管理")
@RequestMapping(value = "/gasTrade")
public class GasTradeController {

	@Autowired
	private GasTradeService gts;
	@Autowired
	private GasTradeOrderService gtos;
	@Autowired
	private UserService us;
	@Autowired
	private GasTypeService gtypes;
	@Autowired
	private GasFactoryService gfs;
	@Autowired
	private GasFactoryCompanyService gfcs;
	@Autowired
	private CommonProvinceOrderService cpos;
	@Autowired
	private CompanyService cs;
	@Autowired
	private UserCompanyService ucs;
	@Autowired
	private CompanyPsrService cps;
	@Autowired
	private MessageCenterService mcs;
	
	@PostMapping("addGasTrade")
	@ApiOperation(value = "增加燃气买卖记录",notes = "发布燃气买卖记录,配送区域最多能选五个")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cpyId", value = "公司编号)" , required = true),
		@ApiImplicitParam(name = "headImg", value = "主图"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质编号", required = true),
		@ApiImplicitParam(name = "gasFactotyId", value = "液厂编号", required = true),
		@ApiImplicitParam(name = "gasVolume", value = "装载量（吨）", required = true),
		@ApiImplicitParam(name = "gasPrice", value = "单价（吨）"),
		@ApiImplicitParam(name = "zcDate", value = "装车日期"),
		@ApiImplicitParam(name = "psArea", value = "配送区域"),
		@ApiImplicitParam(name = "lxName", value = "联系人", required = true),
		@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true),
		@ApiImplicitParam(name = "addUserId", value = "上传人"),
		@ApiImplicitParam(name = "cpNo", value = "槽车车牌号"),
		@ApiImplicitParam(name = "jsyName", value = "驾驶员姓名"),
		@ApiImplicitParam(name = "jsyMobile", value = "驾驶员电话"),
		@ApiImplicitParam(name = "yyrName", value = "押运员姓名"),
		@ApiImplicitParam(name = "yyrMobile", value = "押运员电话"),
		@ApiImplicitParam(name = "qfTxt1", value = "铅封文字信息一"),
		@ApiImplicitParam(name = "qfTxt2", value = "铅封文字信息二"),
		@ApiImplicitParam(name = "qfTxt3", value = "铅封文字信息三"),
		@ApiImplicitParam(name = "qfImg1", value = "铅封图片信息一"),
		@ApiImplicitParam(name = "qfImg2", value = "铅封图片信息二"),
		@ApiImplicitParam(name = "qfImg3", value = "铅封图片信息三"),
		@ApiImplicitParam(name = "remark", value = "备注"),
		@ApiImplicitParam(name = "gpsInfo", value = "gps信息"),
		@ApiImplicitParam(name = "bdImg", value = "磅单图片"),
		@ApiImplicitParam(name = "whpImg", value = "危化品许可证"),
		@ApiImplicitParam(name = "tructsImg", value = "车辆照片"),
		@ApiImplicitParam(name = "otherImg", value = "车辆详图")
	})
	public GenericResponse addGasTrade(HttpServletRequest request) {
		Integer status = 200;
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer userType = 1;
		Integer checkStatus = 0;
		String cpyId = CommonTools.getFinalStr("cpyId", request);
		String headImg = CommonTools.getFinalStr("headImg", request);
		String gasTypeId = CommonTools.getFinalStr("gasTypeId", request);
		String gasFactotyId = CommonTools.getFinalStr("gasFactotyId", request);
		Integer gasVolume = CommonTools.getFinalInteger("gasVolume", request);
		String gasPrice = CommonTools.getFinalStr("gasPrice", request);
		String zcDate = CommonTools.getFinalStr("zcDate", request);
		String psArea = CommonTools.getFinalStr("psArea", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		String cpNo = CommonTools.getFinalStr("cpNo", request);
		String jsyName = CommonTools.getFinalStr("jsyName", request);
		String jsyMobile = CommonTools.getFinalStr("jsyMobile", request);
		String yyrName = CommonTools.getFinalStr("yyrName", request);
		String yyrMobile = CommonTools.getFinalStr("yyrMobile", request);
		String qfTxt1 = CommonTools.getFinalStr("qfTxt1", request);
		String qfTxt2 = CommonTools.getFinalStr("qfTxt2", request);
		String qfTxt3 = CommonTools.getFinalStr("qfTxt3", request);
		String qfImg1 = CommonTools.getFinalStr("qfImg1", request);
		String qfImg2 = CommonTools.getFinalStr("qfImg2", request);
		String qfImg3 = CommonTools.getFinalStr("qfImg3", request);
		String remark = CommonTools.getFinalStr("remark", request);
		String gpsInfo = CommonTools.getFinalStr("gpsInfo", request);
		String bdImg = CommonTools.getFinalStr("bdImg", request);
		String whpImg = CommonTools.getFinalStr("whpImg", request);
		String otherImg = CommonTools.getFinalStr("otherImg", request);
		String tructsImg = CommonTools.getFinalStr("tructsImg", request);
		String userId = CommonTools.getLoginUserId(request);
		String currentTime = CurrentTime.getCurrentTime();
		String gasTradeId = "";
		try {
			if(cpyId.equals("") || gasTypeId.equals("") || gasFactotyId.equals("") || gasVolume.equals(0)) {
				status = 10002;
			}else {
				if(CommonTools.checkAuthorization(userId, CommonTools.getLoginRoleName(request),Constants.ADD_GAS_TRADE)) {
					checkStatus = 1;
				}else if(cilentInfo.equals("wxApp")){
					userType = 2;
				}else {
					status = 70001;
				}
				if(status.equals(200)) {
					GasType gasType = gtypes.findById(gasTypeId);
					Company cpy = cs.getEntityById(cpyId);
					GasFactory gf = gfs.getEntityById(gasFactotyId);
					if(gasType != null && cpy != null && gf != null) {
						List<GasType> gtList = gtypes.getGasTypeList();
						for(GasType gt : gtList) {
							if(gt.getYzImg().replace("_small", "").equals(headImg)) {
								headImg = "";
								break;
							}
						}
						GasTrade gt = new GasTrade(cpy, gf, gasType, 
								CommonTools.dealUploadDetail(userId, "", headImg), gasVolume,
								Double.parseDouble(gasPrice), zcDate,lxName, lxTel, psArea,checkStatus,
								currentTime, 0, userId, currentTime, userType, 0, cpNo,
								jsyName, jsyMobile, yyrName, yyrMobile, qfTxt1, qfTxt2,
								qfTxt3, CommonTools.dealUploadDetail(userId, "", qfImg1), CommonTools.dealUploadDetail(userId, "", qfImg2)
								, CommonTools.dealUploadDetail(userId, "", qfImg3), remark, gpsInfo, CommonTools.dealUploadDetail(userId, "", bdImg),
								CommonTools.dealUploadDetail(userId, "", whpImg), CommonTools.dealUploadDetail(userId, "", tructsImg), "");
						gasTradeId = gts.saveOrUpdate(gt);
						if(!gasTradeId.equals("")) {
							if(!otherImg.equals("")) {//有其他详图
								String[] otherImg_new = CommonTools.dealUploadDetail(userId, "", otherImg).split(",");
								List<GasTradeImg> gtiList_new = new ArrayList<GasTradeImg>();
								for(int i = 0 ; i < otherImg_new.length ; i++) {
									GasTradeImg gti = new GasTradeImg(gt, otherImg_new[i]);
									gtiList_new.add(gti);
								}
								gts.addBatchInfo(gtiList_new);
							}
						}
					}else {
						status = 10002;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gasTradeId);
	}
	
	@GetMapping("getPageGasTradeList")
	@ApiOperation(value = "根据条件分页获取燃气买卖记录",notes = "根据条件分页获取燃气买卖记录-后台前台公用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cpyId", value = "发布人员所在公司编号)"),
		@ApiImplicitParam(name = "addUserId", value = "发布人员"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质编号"),
		@ApiImplicitParam(name = "gfId", value = "液厂编号"),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态(-1:全部,0:未审核,1:审核通过,2:审核未通过)",dataType = "integer"),
		@ApiImplicitParam(name = "showStatus", value = "上/下架状态（-1:全部,0：上架，1：下架)",dataType = "integer"),
		@ApiImplicitParam(name = "sPrice", value = "价格一",dataType = "integer"),
		@ApiImplicitParam(name = "ePrice", value = "价格二",dataType = "integer"),
		@ApiImplicitParam(name = "psArea", value = "配送区域"),
		@ApiImplicitParam(name = "page", value = "页码",dataType = "integer"),
		@ApiImplicitParam(name = "limit", value = "每页记录条数",dataType = "integer")
	})
	public PageResponse getPageGasTradeList(HttpServletRequest request) {
		Integer status = 200;
		String cpyId = CommonTools.getFinalStr("cpyId", request);
		String addUserId = CommonTools.getFinalStr("addUserId", request);
		String gasTypeId = CommonTools.getFinalStr("gasTypeId", request);//有可能是海气，也可能是其他气
		String gfId = CommonTools.getFinalStr("gfId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		Integer sPrice = CommonTools.getFinalInteger("sPrice", request);
		Integer ePrice = CommonTools.getFinalInteger("ePrice", request);
		String psArea = CommonTools.getFinalStr("psArea", request);
		Integer pageNo = CommonTools.getFinalInteger("page", request);
		Integer pageSize = CommonTools.getFinalInteger("limit", request);
		long count = 0;
		List<Object> list = new ArrayList<Object>();
		try {
			Page<GasTrade> gtList = gts.listPageInfoByOpt(cpyId, addUserId, gasTypeId, gfId, checkStatus, showStatus, sPrice, ePrice, psArea, pageNo, pageSize);
			count = gtList.getTotalElements();
			if(count > 0) {
				for(GasTrade gt : gtList) {
					Map<String,Object> map_d = new HashMap<String,Object>();
					map_d.put("id", gt.getId());
					map_d.put("cpyName", gt.getCompany().getName());
					map_d.put("headImg", gt.getHeadImg());
					map_d.put("gasTypeName", gt.getGasType().getName());
					String pubUserId = gt.getAddUserId();
					Integer userType = gt.getUserType();
					if(userType.equals(2)) {
						User user = us.getEntityById(pubUserId);
						if(user != null) {
							map_d.put("pubUserName", user.getRealName());
						}else {
							map_d.put("pubUserName", "");
						}
					}else {
						map_d.put("pubUserName", "");
					}
					map_d.put("psArea", gt.getPsArea());
					GasFactory gf = gt.getGasFactory();
					map_d.put("gfName", gf.getName());
					map_d.put("yyd", gf.getProvince());
					map_d.put("price", gt.getGasPrice());
					map_d.put("volume", gt.getGasVolume());
					map_d.put("addTime", gt.getAddTime());
					map_d.put("checkStatus", gt.getCheckStatus());
					map_d.put("showStatus",gt.getShowStatus());
					//从燃气交易中获取好评度
					Integer pjScore = 0;
					List<GasTradeOrder> gtoList = gtos.listComInfoByCpyId(cpyId);
					Integer tradeNum = gtoList.size();
					for(GasTradeOrder gto : gtoList) {
						pjScore += gto.getOrderPjNumber();
					}
					if(tradeNum > 0) {
						map_d.put("hpRate",CommonTools.convertInputNumber(pjScore * 100.0 / tradeNum));
					}else {
						map_d.put("hpRate","暂无");
					}
					map_d.put("tradeNum", tradeNum);
					list.add(map_d);
				}
			}else {
				status = 50001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, count, status, list);
	}
	
	@GetMapping("getSpecGasTradeDetail")
	@ApiOperation(value = "根据指定燃气买卖详情",notes = "根据指定燃气买卖详情--后台审核和前台客户浏览公用和发布人修改时用,配送区域最多能选五个")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 10002, message = "参数为空"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gasTradeId", value = "燃气买卖编号)",required = true),
		@ApiImplicitParam(name = "opt", value = "用途（0：审核用，1：前台使用）", dataType = "integer"),
		@ApiImplicitParam(name = "userId", value = "前台用户编号（前台时传递）",required = true)
	})
	public GenericResponse getSpecGasTradeDetail(HttpServletRequest request) {
		Integer status = 200;
		String gasTradeId = CommonTools.getFinalStr("gasTradeId", request);
		Integer opt = CommonTools.getFinalInteger("opt", request);//用途（0：审核用，1：前台使用）
		String userId = "";
		List<Object> list = new ArrayList<Object>();
		if(gasTradeId.equals("")) {
			status = 10002;
		}else {
			try {
				GasTrade gt = gts.getEntityById(gasTradeId);
				if(gt == null) {
					status = 50001;
				}else {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", gt.getId());
					Company cpy = gt.getCompany();
					String cpyId = cpy.getId();
					List<Object> list_cpy = new ArrayList<Object>();
					if(opt.equals(1)) {//前台
						userId = CommonTools.getFinalStr("userId", request);
						//获取自己的所有贸易公司
						List<UserCompany> ucList = ucs.getUserCompanyListByOpt("", "LNG贸易商", 1, userId);
						for(UserCompany uc : ucList) {
							Map<String, Object> map_d = new HashMap<String, Object>();
							Company cpy_tmp = uc.getCompany();
							map_d.put("cpyId", cpy_tmp.getId());
							map_d.put("cpyName", cpy_tmp.getName());
							if(cpy_tmp.getId().equals(cpyId)) {
								map_d.put("selFlag", true);
							}else {
								map_d.put("selFlag", false);
							}
							list_cpy.add(map_d);
						}
					}else {//后台--获取所有贸易商公司
						List<Company> cList = cs.listSpecCpy("","LNG贸易商");
						for(Company cpy_tmp : cList) {
							Map<String, Object> map_d = new HashMap<String, Object>();
							map_d.put("cpyId", cpy_tmp.getId());
							map_d.put("cpyName", cpy_tmp.getName());
							if(cpy_tmp.getId().equals(cpyId)) {
								map_d.put("selFlag", true);
							}else {
								map_d.put("selFlag", false);
							}
							list_cpy.add(map_d);
						}
					}
					map.put("cpyList", list_cpy);
					//液质类型是通过液厂获取出来
					GasType gasType = gt.getGasType();
					map.put("gasTypeId", gasType.getId());
					map.put("gasTypeName", gasType.getName());
					List<Object> list_gf = new ArrayList<Object>();
					GasFactory gf = gt.getGasFactory();
					List<GasFactoryCompany>  gfcList = gfcs.listCompanyByGfId("", cpyId, 1);
					String headImg = gt.getHeadImg();
					for(GasFactoryCompany gfc:gfcList) {
						Map<String,Object> map_gfc = new HashMap<String,Object>();
						GasFactory gf_tmp = gfc.getGasFactory();
						GasType gt_tmp = gf_tmp.getGasType();
						map_gfc.put("gfId", gf_tmp.getId());
						map_gfc.put("gfName", gf_tmp.getName());
						map_gfc.put("headImg", gt_tmp.getYzImg());
						map_gfc.put("gasTypeId", gt_tmp.getId());
						map_gfc.put("gasTypeName", gt_tmp.getName());
						if(gf_tmp.getId().equals(gf.getId())) {
							map_gfc.put("selFlag", true);
							if(headImg.equals("")) {
								headImg = gt_tmp.getYzImg();
							}
						}else {
							map_gfc.put("selFlag", false);
						}
						list_gf.add(map_gfc);
					}
					map.put("gasFacotryList", list_gf);
					map.put("headImg", headImg);
					map.put("yyd", gf.getProvince());
					map.put("gasVolume", gt.getGasVolume());
					map.put("gasPrice", gt.getGasPrice());
					map.put("zcDate", gt.getZcDate());
					map.put("lxName", gt.getLxName());
					map.put("lxTel", gt.getLxTel());
					//获取省份列表
					List<CommonProvinceOrder> cpoList = cpos.listAllInfo("asc");
					String psArea = gt.getPsArea();
					List<Object> list_prov = new ArrayList<Object>();
					Map<String,Object> map_all_prov = new HashMap<String,Object>();
					map_all_prov.put("provName", "全国");
					if(!psArea.equals("")) {
						if(!psArea.equals("全国")) {
							map_all_prov.put("selFlag", false);
							map_all_prov.put("disableFlag", true);
							list_prov.add(map_all_prov);
							String[] psAreaArr = psArea.split(",");
							for(CommonProvinceOrder cpo : cpoList) {
								Map<String,Object> map_prov = new HashMap<String,Object>();
								map_prov.put("disableFlag", false);
								map_prov.put("provName", cpo.getProvince());
								for(int i = 0 ; i < psAreaArr.length ; i++) {
									if(psAreaArr[i].equals(cpo.getProvince())) {
										map_prov.put("selFlag", true);
										break;
									}else {
										map_prov.put("selFlag", false);
									}
								}
								list_prov.add(map_prov);
							}
						}else {
							map_all_prov.put("selFlag", true);
							map_all_prov.put("disableFlag", false);
							list_prov.add(map_all_prov);
							Map<String,Object> map_prov1 = new HashMap<String,Object>();
							map_prov1.put("psArea", gt.getPsArea());
							map_prov1.put("selFlag", true);
							for(CommonProvinceOrder cpo : cpoList) {
								Map<String,Object> map_prov = new HashMap<String,Object>();
								map_prov.put("provName", cpo.getProvince());
								map_prov.put("selFlag", false);
								map_prov.put("disableFlag", true);
								list_prov.add(map_prov);
							}
						}
					}
					map.put("psArea", list_prov);
					map.put("checkStatus", gt.getCheckStatus());
					map.put("checkTime", gt.getCheckTime());
					map.put("showStatus", gt.getShowStatus());
					map.put("addTime", gt.getAddTime());
					map.put("userType", gt.getUserType());
					map.put("cpNo", gt.getCpNo());
					//获取公司驾驶员押运人
					List<CompanyPsr> cpyPsrList = cps.getCompanyPsrList(cpyId);
					List<Object> list_jsr = new ArrayList<Object>();
					List<Object> list_yyr = new ArrayList<Object>();
					String jsyName = gt.getJsyName();
					String jsyMobile = gt.getJsyMobile();
					String yyrName = gt.getYyrName();
					String yyrMobile = gt.getYyrMobile();
					for(CompanyPsr psr : cpyPsrList) {
						Map<String,Object> map_d = new HashMap<String,Object>();
						String driverName = psr.getName();
						String driverMobile = psr.getMobile();
						map_d.put("jsyName", driverName);
						map_d.put("jsyMobile", driverMobile);
						if(driverName.equals(jsyName) && driverMobile.equals(jsyMobile)) {
							map_d.put("selFlag", true);
						}else {
							map_d.put("selFlag", false);
						}
						list_jsr.add(map_d);
						Map<String,Object> map_d1 = new HashMap<String,Object>();
						map_d1.put("yyrName", driverName);
						map_d1.put("yyrMobile", driverMobile);
						if(driverName.equals(yyrName) && driverMobile.equals(yyrMobile)) {
							map_d1.put("selFlag", true);
						}else {
							map_d1.put("selFlag", false);
						}
						list_yyr.add(map_d1);
					}
					map.put("jsrList", list_jsr);
					map.put("yyrList", list_yyr);
					map.put("qfTxt1", gt.getQfText1());
					map.put("qfTxt2", gt.getQfText2());
					map.put("qfTxt3", gt.getQfText3());
					map.put("qfImg1", gt.getQfImg1());
					map.put("qfImg2", gt.getQfImg2());
					map.put("qfImg3", gt.getQfImg3());
					map.put("remark", gt.getRemark());
					map.put("gpsInfo", gt.getGpsInfo());
					map.put("bdImg", gt.getBdImg());
					map.put("whpImg", gt.getWhpImg());
					map.put("tructsImg", gt.getTructsImg());
					map.put("tradeOrderId", gt.getTradeOrderId());
					//获取燃气买卖其他详图
					List<GasTradeImg> gtiList = gts.listInfoByGtId(gasTradeId);
					List<Object> list_d = new ArrayList<Object>();
					if(gtiList.size() > 0) {
						for(GasTradeImg gti : gtiList) {
							Map<String,String> map_d = new HashMap<String,String>();
							map_d.put("gtiId", gti.getId());
							map_d.put("gtiImg", gti.getOtherImg());
							list_d.add(map_d);
						}
					}
					map.put("otherImgList", list_d);
					List<Object> list_d1 = new ArrayList<Object>();
					if(opt.equals(1)) {
						//获取燃气买卖订单列表
						List<GasTradeOrder> gtoList = gtos.getInfoBygtId(gasTradeId);
						if(gtoList.size() > 0) {
							for(GasTradeOrder gto : gtoList) {
								Map<String,Object> map_d = new HashMap<String,Object>();
								User user = gto.getUser();
								map_d.put("gtoId", gto.getId());
								map_d.put("buyUserName", user.getRealName());
								map_d.put("buyPrice", gto.getPrice());
								list_d1.add(map_d);
							}
						}
					}
					//从燃气交易中获取好评度
					Integer pjScore = 0;
					List<GasTradeOrder> gtoList = gtos.listComInfoByCpyId(cpyId);
					Integer tradeNum = gtoList.size();
					for(GasTradeOrder gto : gtoList) {
						pjScore += gto.getOrderPjNumber();
					}
					if(tradeNum > 0) {
						map.put("hpRate",CommonTools.convertInputNumber(pjScore * 100.0 / tradeNum));
					}else {
						map.put("hpRate","暂无");
					}
					map.put("tradeNum", tradeNum);
					map.put("tradeOrderList", list_d1);
					list.add(map);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				status = 1000;
			}
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@PutMapping("setGasTrade")
	@ApiOperation(value = "后台审核/前台设置上下架燃气买卖发布记录",notes = "后台审核/前台设置上下架燃气买卖发布记录")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gasTradeId", value = "燃气买卖编号)",required = true),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态（1:审核通过,2:审核未通过）",dataType = "integer"),
		@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）",dataType = "integer"),
		@ApiImplicitParam(name = "opt", value = "0:审核，1：上下架",required = true,dataType = "integer")
	})
	public GenericResponse setGasTrade(HttpServletRequest request) {
		Integer status = 200;
		String gasTradeId = CommonTools.getFinalStr("gasTradeId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		Integer opt = CommonTools.getFinalInteger("opt", request);
		String userId = CommonTools.getLoginUserId(request);
		try {
			if(!gasTradeId.equals("")){
				if(opt.equals(0)) {//审核
					if(checkStatus.equals(1) || checkStatus.equals(2)) {
						if(CommonTools.checkAuthorization(userId, CommonTools.getLoginRoleName(request),Constants.CHECK_GAS_TRADE)) {
							GasTrade gt = gts.getEntityById(gasTradeId);
							if(gt == null) {
								status = 50001;
							}else {
								gt.setCheckStatus(checkStatus);
								gt.setCheckTime(CurrentTime.getCurrentTime());
								gts.saveOrUpdate(gt);
								//给发布人发送一条消息
								String result = "";
								if(opt.equals(1)) {
									result = "审核通过";
								}else if(opt.equals(2)) {
									result = "审核未通过";
								}
								MessageCenter mc = new MessageCenter("您发布的"+gt.getGasFactory().getName()+"燃气"+result, "您发布的"+gt.getGasFactory().getName()+"燃气"+result, 0, CurrentTime.getCurrentTime(), 2,
										gasTradeId, "gasTrade", "", gt.getAddUserId(), 0);
								mcs.saveOrUpdate(mc);
							}
						}else {
							status = 70001;
						}
					}else {
						status = 10002;
					}
				}else {//上下架
					if(checkStatus.equals(0) || checkStatus.equals(1)) {
						GasTrade gt = gts.getEntityById(gasTradeId);
						if(gt == null) {
							status = 50001;
						}else {
							gt.setShowStatus(showStatus);
							gts.saveOrUpdate(gt);
						}
					}else {
						status = 10002;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	@PutMapping("upGasTrade")
	@ApiOperation(value = "发布人修改燃气记录的基本信息",notes = "发布人修改燃气记录的基本信息（审核通过后不能进行修改）")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空"),
		@ApiResponse(code = 80001, message = "审核通过不能修改"),
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gasTradeId", value = "燃气买卖编号)",required = true),
		@ApiImplicitParam(name = "cpyId", value = "公司编号)" , required = true),
		@ApiImplicitParam(name = "headImg", value = "主图"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质编号", required = true),
		@ApiImplicitParam(name = "gasFactotyId", value = "液厂编号", required = true),
		@ApiImplicitParam(name = "gasVolume", value = "装载量（吨）", dataType = "integer",required = true),
		@ApiImplicitParam(name = "gasPrice", value = "单价（吨）", required = true),
		@ApiImplicitParam(name = "zcDate", value = "装车日期"),
		@ApiImplicitParam(name = "psArea", value = "配送区域" , required = true),
		@ApiImplicitParam(name = "lxName", value = "联系人", required = true),
		@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true),
		@ApiImplicitParam(name = "cpNo", value = "槽车车牌号"),
		@ApiImplicitParam(name = "jsyName", value = "驾驶员姓名"),
		@ApiImplicitParam(name = "jsyMobile", value = "驾驶员电话"),
		@ApiImplicitParam(name = "yyrName", value = "押运员姓名"),
		@ApiImplicitParam(name = "yyrMobile", value = "押运员电话"),
		@ApiImplicitParam(name = "qfTxt1", value = "铅封文字信息一"),
		@ApiImplicitParam(name = "qfTxt2", value = "铅封文字信息二"),
		@ApiImplicitParam(name = "qfTxt3", value = "铅封文字信息三"),
		@ApiImplicitParam(name = "qfImg1", value = "铅封图片信息一"),
		@ApiImplicitParam(name = "qfImg2", value = "铅封图片信息二"),
		@ApiImplicitParam(name = "qfImg3", value = "铅封图片信息三"),
		@ApiImplicitParam(name = "remark", value = "备注"),
		@ApiImplicitParam(name = "gpsInfo", value = "gps信息"),
		@ApiImplicitParam(name = "bdImg", value = "磅单图片",required = true),
		@ApiImplicitParam(name = "whpImg", value = "危化品许可证",required = true),
		@ApiImplicitParam(name = "otherImg", value = "详情图"),
		@ApiImplicitParam(name = "tructsImg", value = "车辆照片",required = true),
		@ApiImplicitParam(name = "userId", value = "当前人员编号--电脑端不用传递")
	})
	public GenericResponse upGasTrade(HttpServletRequest request) {
		Integer status = 200;
		String gasTradeId = CommonTools.getFinalStr("gasTradeId", request);
		String cpyId = CommonTools.getFinalStr("cpyId", request);
		String headImg = CommonTools.getFinalStr("headImg", request);
		String gasTypeId = CommonTools.getFinalStr("gasTypeId", request);
		String gasFactotyId = CommonTools.getFinalStr("gasFactotyId", request);
		Integer gasVolume = CommonTools.getFinalInteger("gasVolume", request);
		String gasPrice = CommonTools.getFinalStr("gasPrice", request);
		String zcDate = CommonTools.getFinalStr("zcDate", request);
		String psArea = CommonTools.getFinalStr("psArea", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		String cpNo = CommonTools.getFinalStr("cpNo", request);
		String jsyName = CommonTools.getFinalStr("jsyName", request);
		String jsyMobile = CommonTools.getFinalStr("jsyMobile", request);
		String yyrName = CommonTools.getFinalStr("yyrName", request);
		String yyrMobile = CommonTools.getFinalStr("yyrMobile", request);
		String qfTxt1 = CommonTools.getFinalStr("qfTxt1", request);
		String qfTxt2 = CommonTools.getFinalStr("qfTxt2", request);
		String qfTxt3 = CommonTools.getFinalStr("qfTxt3", request);
		String qfImg1 = CommonTools.getFinalStr("qfImg1", request);
		String qfImg2 = CommonTools.getFinalStr("qfImg2", request);
		String qfImg3 = CommonTools.getFinalStr("qfImg3", request);
		String remark = CommonTools.getFinalStr("remark", request);
		String gpsInfo = CommonTools.getFinalStr("gpsInfo", request);
		String bdImg = CommonTools.getFinalStr("bdImg", request);
		String whpImg = CommonTools.getFinalStr("whpImg", request);
		String otherImg = CommonTools.getFinalStr("otherImg", request);
		String tructsImg = CommonTools.getFinalStr("tructsImg", request);
		String userId = CommonTools.getLoginUserId(request);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		try {
			if(!gasTradeId.equals("")){
				if(CommonTools.checkAuthorization(userId, CommonTools.getLoginRoleName(request),Constants.UP_GAS_TRADE)) {
					
				}else if(cilentInfo.equals("wxApp")){
					
				}else {
					status = 70001;
				}
				if(status.equals(200)) {
					GasTrade gt = gts.getEntityById(gasTradeId);
					if(gt == null) {
						status = 50001;
					}else {
						if(gt.getCheckStatus() == 1) {//审核通过的不能进行修改
							status = 80001;
						}else {
							GasFactory gf = gt.getGasFactory();
							GasType gasType = gt.getGasType();
							if(!cpyId.equals(gt.getCompany().getId())) {
								gt.setCompany(cs.getEntityById(cpyId));
							}
							if(!gasTypeId.equals(gasType.getId())) {//液质类型发生变化
								gasType = gtypes.findById(gasTypeId);
								gt.setGasType(gasType);
							}
							if(!gasFactotyId.equals(gf.getId())) {
								gt.setGasFactory(gfs.getEntityById(gasFactotyId));
							}
							if(!headImg.equals(gasType.getYzImg().replace("_small", ""))) {//说明是自己上传
								gt.setHeadImg(CommonTools.dealUploadDetail(userId, gt.getHeadImg(), headImg));
							}
							//如果图片不是液厂默认图片--上传
							gt.setGasVolume(gasVolume);
							gt.setGasPrice(Double.parseDouble(gasPrice));
							gt.setZcDate(zcDate);
							gt.setPsArea(psArea);
							gt.setLxName(lxName);
							gt.setLxTel(lxTel);
							gt.setCpNo(cpNo);
							gt.setJsyName(jsyName);
							gt.setJsyMobile(jsyMobile);
							gt.setYyrName(yyrName);
							gt.setYyrMobile(yyrMobile);
							gt.setQfText1(qfTxt1);
							gt.setQfText2(qfTxt2);
							gt.setQfText3(qfTxt3);
							gt.setQfImg1(qfImg1);
							gt.setQfImg2(qfImg2);
							gt.setQfImg3(qfImg3);
							gt.setRemark(remark);
							gt.setGpsInfo(gpsInfo);
							gt.setBdImg(CommonTools.dealUploadDetail(userId, gt.getBdImg(), bdImg));
							gt.setWhpImg(CommonTools.dealUploadDetail(userId, gt.getWhpImg(), whpImg));
							gt.setTructsImg(CommonTools.dealUploadDetail(userId, gt.getTructsImg(), tructsImg));
							gts.saveOrUpdate(gt);
							//获取详情图
							String otherImg_db = "";
							List<GasTradeImg> gtiList = gts.listInfoByGtId(gasTradeId);
							if(gtiList.size() == 0) {//数据库没图
								if(!otherImg.equals("")) {//编辑时增加图
									String[] otherImg_new = CommonTools.dealUploadDetail(userId, "", otherImg).split(",");
									List<GasTradeImg> gtiList_new = new ArrayList<GasTradeImg>();
									for(int i = 0 ; i < otherImg_new.length ; i++) {
										GasTradeImg gti = new GasTradeImg(gt, otherImg_new[i]);
										gtiList_new.add(gti);
									}
									gts.addBatchInfo(gtiList_new);
								}
							}else {//数据库有图
								if(!otherImg.equals("")) {//编辑时有图
									for(GasTradeImg gti : gtiList) {
										otherImg_db += gti.getOtherImg() + ",";
									}
									otherImg_db = otherImg_db.substring(0, otherImg_db.length() - 1);
									String[] otherImg_new = CommonTools.dealUploadDetail(userId, otherImg_db, otherImg).split(",");
									//删除原来数据
									gts.delBatchByGtId(gtiList);
									List<GasTradeImg> gtiList_new = new ArrayList<GasTradeImg>();
									for(int i = 0 ; i < otherImg_new.length ; i++) {
										GasTradeImg gti = new GasTradeImg(gt, otherImg_new[i]);
										gtiList_new.add(gti);
									}
									gts.addBatchInfo(gtiList_new);
								}else {//编辑时无图
									//删除原来数据
									gts.delBatchByGtId(gtiList);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
}
