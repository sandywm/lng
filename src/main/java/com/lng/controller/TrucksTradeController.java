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

import com.lng.pojo.Company;
import com.lng.pojo.Qualification;
import com.lng.pojo.TrucksHeadPp;
import com.lng.pojo.TrucksHeadType;
import com.lng.pojo.TrucksPotPp;
import com.lng.pojo.TrucksTrade;
import com.lng.pojo.TrucksTradeQualification;
import com.lng.pojo.TrucksType;
import com.lng.pojo.TructsTradeZz;
import com.lng.pojo.User;
import com.lng.pojo.UserFocus;
import com.lng.pojo.WqPfbz;
import com.lng.service.CompanyService;
import com.lng.service.QualificationService;
import com.lng.service.TrucksHeadPpService;
import com.lng.service.TrucksHeadTypeService;
import com.lng.service.TrucksPotPpService;
import com.lng.service.TrucksTradeQualService;
import com.lng.service.TrucksTradeService;
import com.lng.service.TrucksTypeService;
import com.lng.service.TructsTradeZzService;
import com.lng.service.UserFocusService;
import com.lng.service.UserService;
import com.lng.service.WqPfbzService;
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
@Api(tags = "货车租卖相关接口")
@RequestMapping("/trucksTrade")
public class TrucksTradeController {
	@Autowired
	private TrucksHeadTypeService headTypeService;
	@Autowired
	private TrucksHeadPpService headPpService;
	@Autowired
	private TrucksTypeService typeService;
	@Autowired
	private WqPfbzService wqPfBzService;
	@Autowired
	private TrucksTradeService trucksTradeService;
	@Autowired
	private CompanyService comService;
	@Autowired
	private QualificationService qualService;
	@Autowired
	private TrucksTradeQualService ttQualService;
	@Autowired
	private TructsTradeZzService ttzzService;
	@Autowired
	private UserFocusService ufService;
	@Autowired
	private TrucksPotPpService tpps;
	@Autowired
	private UserService us;

	@PostMapping("/addTrucksTrade")
	@ApiOperation(value = "添加货车租卖", notes = "添加货车租卖")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 20001, message = "用户未登录"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号"),
			@ApiImplicitParam(name = "mainImg", value = "车辆主图", required = true),
			@ApiImplicitParam(name = "trucksNo", value = "车头车牌号码", required = true),
			@ApiImplicitParam(name = "trucksGcNo", value = "挂车车牌号码（可选）"),
			@ApiImplicitParam(name = "spYear", value = "车头上牌年月", required = true),
			@ApiImplicitParam(name = "potPpId", value = "储罐品牌（危货）"),
			@ApiImplicitParam(name = "potVol", value = "储罐容积（危货）"),
			@ApiImplicitParam(name = "spYearPot", value = "储罐上牌年月（危货）"),
			@ApiImplicitParam(name = "buyYear", value = "购买年份"),
			@ApiImplicitParam(name = "headTypeId", value = "车头类型编号", required = true),
			@ApiImplicitParam(name = "headPpId", value = "车头品牌编号", required = true),
			@ApiImplicitParam(name = "trucksTypeId", value = "车辆种类编号", required = true),
			@ApiImplicitParam(name = "xsDistance", value = "车辆行驶里程", required = true),
			@ApiImplicitParam(name = "remark", value = "备注"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			// @ApiImplicitParam(name = "addUserId", value = "上传人员", required = true),
			@ApiImplicitParam(name = "qualId", value = "进港资质编号", required = true),
			@ApiImplicitParam(name = "ttImg", value = "槽车租卖详情图片"),
			// @ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）",
			// required = true),
			@ApiImplicitParam(name = "tradeType", value = "贸易类型（1：租赁，2：买卖）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "area", value = "运输范围（租赁）"), @ApiImplicitParam(name = "qyTypeId", value = "气源类型"),
			@ApiImplicitParam(name = "wqpfbzId", value = "尾气排放标准编号", required = true),
			@ApiImplicitParam(name = "accidentFlag", value = "是否发生事故（1：是,2：否，0：不填）"),
			@ApiImplicitParam(name = "tructsHeadxsz", value = "车头行驶证（危货）"),
			@ApiImplicitParam(name = "gcXsz", value = "罐车行驶证（危货）"), @ApiImplicitParam(name = "price", value = "车辆价格"),
			@ApiImplicitParam(name = "regPlace", value = "车辆注册地"),
			@ApiImplicitParam(name = "tructsYyz", value = "车辆运营证（危货）"),
			@ApiImplicitParam(name = "potJyz", value = "储罐检验合格证（危货）"),
			@ApiImplicitParam(name = "aqfbg", value = "安全阀校验报告（危货）") })
	public GenericResponse addTrucksTrade(HttpServletRequest request) {
		String compId = CommonTools.getFinalStr("compId", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String trucksNo = CommonTools.getFinalStr("trucksNo", request);
		String trucksGcNo = CommonTools.getFinalStr("trucksGcNo",request);
		String spYear = CommonTools.getFinalStr("spYear", request);
		String potPpId = CommonTools.getFinalStr("potPpId", request);
		Integer potVol = CommonTools.getFinalInteger("potVol", request);
		String spYearPot = CommonTools.getFinalStr("spYearPot", request);
		String buyYear = CommonTools.getFinalStr("buyYear", request);
		String headTypeId = CommonTools.getFinalStr("headTypeId", request);
		String headPpId = CommonTools.getFinalStr("headPpId", request);
		String trucksTypeId = CommonTools.getFinalStr("trucksTypeId", request);
		Integer xsDistance = CommonTools.getFinalInteger("xsDistance", request);
		Integer price = CommonTools.getFinalInteger("price", request);
		String regPlace = CommonTools.getFinalStr("regPlace", request);
		String remark = CommonTools.getFinalStr("remark", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		// String addUserId = CommonTools.getFinalStr("addUserId", request);
		String qualId = CommonTools.getFinalStr("qualId", request);
		// Integer userType = CommonTools.getFinalInteger("userType", request);
		Integer userType = 1;
		Integer tradeType = CommonTools.getFinalInteger("tradeType", request);
		String area = CommonTools.getFinalStr("area", request);
		String qyTypeId = CommonTools.getFinalStr("qyTypeId", request);
		String wqpfbzId = CommonTools.getFinalStr("wqpfbzId", request);
		Integer accidentFlag = CommonTools.getFinalInteger("accidentFlag", request);
		String tructsHeadxsz = CommonTools.getFinalStr("tructsHeadxsz", request);
		String gcXsz = CommonTools.getFinalStr("gcXsz", request);
		String tructsYyz = CommonTools.getFinalStr("tructsYyz", request);
		String potJyz = CommonTools.getFinalStr("potJyz", request);
		String aqfbg = CommonTools.getFinalStr("aqfbg", request);
		String ttImg = CommonTools.getFinalStr("ttImg", request);
		String loginUserId = CommonTools.getLoginUserId(request);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;
		String ttId = "";

		try {
			if (cilentInfo.equals("wxApp")) {
				userType = 2;
				loginUserId = CommonTools.getFinalStr("userId", request);
				if (loginUserId.isEmpty()) {
					status = 20001;
				}
			} else if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),
					Constants.ADD_TRTR)) {
			} else {
				status = 70001;
			}

			if (status.equals(200)) {
				TrucksTrade trtr = new TrucksTrade();

				trtr.setCompanyId(compId);
				if (!mainImg.equals("")) {
					trtr.setMainImg(CommonTools.dealUploadDetail(loginUserId, "", mainImg));
				}
				trtr.setTrucksNo(trucksNo);
				trtr.setTrucksGcNo(trucksGcNo);
				trtr.setSpYear(spYear);
				trtr.setSpYearPot(spYearPot);
				trtr.setPotPpId(potPpId);
				trtr.setPotVolume(potVol);
				trtr.setSpYearPot(spYearPot);
				trtr.setBuyYear(buyYear);
				TrucksHeadType trucksHeadType = headTypeService.findById(headTypeId);
				trtr.setTrucksHeadType(trucksHeadType);
				TrucksHeadPp trucksHeadPp = headPpService.findById(headPpId);
				trtr.setTrucksHeadPp(trucksHeadPp);
				TrucksType trucksType = typeService.findById(trucksTypeId);
				trtr.setTrucksType(trucksType);
				trtr.setXsDistance(xsDistance);
				trtr.setPrice(price);
				trtr.setRegPlace(regPlace);
				trtr.setRemark(remark);
				trtr.setLxName(lxName);
				trtr.setLxTel(lxTel);
				if (userType.equals(1)) {
					trtr.setCheckStatus(1);
					trtr.setCheckTime(CurrentTime.getCurrentTime());
				} else {
					trtr.setCheckStatus(0);
					trtr.setCheckTime("");
				}
				trtr.setShowStatus(showStatus);
				trtr.setAddUserId(loginUserId);
				trtr.setAddTime(CurrentTime.getCurrentTime());
				trtr.setUserType(userType);
				trtr.setHot(0);
				trtr.setTradeType(tradeType);
				trtr.setArea(area);
				trtr.setQyTypeId(qyTypeId);
				WqPfbz wqPfbz = wqPfBzService.findById(wqpfbzId);
				trtr.setWqPfbz(wqPfbz);
				trtr.setAccidentFlag(accidentFlag);
				if (!tructsHeadxsz.isEmpty()) {
					trtr.setTrucksHeadxsz(CommonTools.dealUploadDetail(loginUserId, "", tructsHeadxsz));
				}
				if (!gcXsz.isEmpty()) {
					trtr.setGcXsz(CommonTools.dealUploadDetail(loginUserId, "", gcXsz));
				}
				if (!tructsYyz.isEmpty()) {
					trtr.setTructsYyz(CommonTools.dealUploadDetail(loginUserId, "", tructsYyz));
				}
				if (!potJyz.isEmpty()) {
					trtr.setPotJyz(CommonTools.dealUploadDetail(loginUserId, "", potJyz));
				}
				if (!aqfbg.isEmpty()) {
					trtr.setAqfBg(CommonTools.dealUploadDetail(loginUserId, "", aqfbg));
				}

				ttId = trucksTradeService.saveOrUpdate(trtr);

				if (!ttId.isEmpty()) {
					if (!qualId.isEmpty()) {
						String[] qualArr = qualId.split(",");
						for (int i = 0; i < qualArr.length; i++) {
							Qualification qual = qualService.findById(qualArr[i]);
							TrucksTradeQualification ttQual = new TrucksTradeQualification();
							ttQual.setQualification(qual);
							ttQual.setTrucksTrade(trtr);
							ttQualService.addOrUpdate(ttQual);
						}

					}

					if (!ttImg.equals("")) {
						String ttImgs = CommonTools.dealUploadDetail(loginUserId, "", ttImg);
						String[] ttimgArr = ttImgs.split(",");
						List<TructsTradeZz> zzlist = new ArrayList<>();
						for (int i = 0; i < ttimgArr.length; i++) {
							TructsTradeZz ttzz = new TructsTradeZz();
							ttzz.setTrucksTrade(trtr);
							ttzz.setTructsTradeZz(ttimgArr[i]);
							zzlist.add(ttzz);
						}
						ttzzService.addOrUpdateBatch(zzlist);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}

		return ResponseFormat.retParam(status, ttId);
	}

	@PutMapping("/updateTrTrByStatus")
	@ApiOperation(value = "更新货车租卖审核状态", notes = "更新货车租卖审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "货车租卖主键", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）") })
	public GenericResponse updateTrTrByStatus(HttpServletRequest request, String id, Integer checkSta,
			Integer showSta) {
		id = CommonTools.getFinalStr(id);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;

		try {
			if (cilentInfo.equals("wxApp")) {

			} else if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.CHECK_TRTR)) {
			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				TrucksTrade tt = trucksTradeService.getEntityById(id);
				if (tt == null) {
					status = 50001;
				} else {
					if (checkSta != null && !checkSta.equals(tt.getCheckStatus())) {
						tt.setCheckStatus(checkSta);
						tt.setCheckTime(CurrentTime.getCurrentTime());
					}
					if (showSta != null && !showSta.equals(tt.getShowStatus())) {
						tt.setShowStatus(showSta);
					}
					trucksTradeService.saveOrUpdate(tt);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}

		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateTrTrByHot")
	@ApiOperation(value = "更新货车租卖热度", notes = "更新货车租卖热度")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "货车租卖主键", required = true),
			@ApiImplicitParam(name = "hot", value = "热度（默认为0）") })
	public GenericResponse updateTrTrByHot(HttpServletRequest request, String id, Integer hot) {
		id = CommonTools.getFinalStr(id);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;

		try {
			if (cilentInfo.equals("wxApp")) {

			} else if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.UP_TRTR)) {
			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				TrucksTrade tt = trucksTradeService.getEntityById(id);
				if (tt == null) {
					status = 50001;
				} else {
					if (hot != null && !hot.equals(tt.getHot())) {
						tt.setHot(hot);
					}
					trucksTradeService.saveOrUpdate(tt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateTrucksTrade")
	@ApiOperation(value = "修改货车租卖", notes = "修改货车租卖基本信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 80001, message = "审核通过不能修改"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "货车租卖主键", required = true),
			@ApiImplicitParam(name = "compId", value = "公司编号"),
			@ApiImplicitParam(name = "mainImg", value = "车辆主图", required = true),
			@ApiImplicitParam(name = "trucksNo", value = "车牌号码", required = true),
			@ApiImplicitParam(name = "spYear", value = "车头上牌年月", required = true),
			@ApiImplicitParam(name = "potPpId", value = "储罐品牌（危货）"),
			@ApiImplicitParam(name = "potVol", value = "储罐容积（危货）"),
			@ApiImplicitParam(name = "spYearPot", value = "储罐上牌年月（危货）"),
			@ApiImplicitParam(name = "buyYear", value = "购买年份"),
			@ApiImplicitParam(name = "headTypeId", value = "车头类型编号", required = true),
			@ApiImplicitParam(name = "headPpId", value = "车头品牌编号", required = true),
			@ApiImplicitParam(name = "trucksTypeId", value = "车辆种类编号", required = true),
			@ApiImplicitParam(name = "xsDistance", value = "车辆行驶里程", required = true),
			@ApiImplicitParam(name = "qualId", value = "进港资质编号", required = true),
			@ApiImplicitParam(name = "ttImg", value = "槽车租卖详情图片"), @ApiImplicitParam(name = "remark", value = "备注"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			// @ApiImplicitParam(name = "addUserId", value = "上传人员", required = true),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true),
			@ApiImplicitParam(name = "tradeType", value = "贸易类型（1：租赁，2：买卖）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "area", value = "运输范围（租赁）"), @ApiImplicitParam(name = "qyTypeId", value = "气源类型"),
			@ApiImplicitParam(name = "wqpfbzId", value = "尾气排放标准编号", required = true),
			@ApiImplicitParam(name = "accidentFlag", value = "是否发生事故（1：是,2：否，0：不填）"),
			@ApiImplicitParam(name = "tructsHeadxsz", value = "车头行驶证（危货）"),
			@ApiImplicitParam(name = "gcXsz", value = "罐车行驶证（危货）"),
			@ApiImplicitParam(name = "tructsYyz", value = "车辆运营证（危货）"),
			@ApiImplicitParam(name = "price", value = "车辆价格"), @ApiImplicitParam(name = "regPlace", value = "车辆注册地"),
			@ApiImplicitParam(name = "potJyz", value = "储罐检验合格证（危货）"),
			@ApiImplicitParam(name = "aqfbg", value = "安全阀校验报告（危货）") })
	public GenericResponse updateTrucksTrade(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id", request);
		String compId = CommonTools.getFinalStr("compId", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String trucksNo = CommonTools.getFinalStr("trucksNo", request);
		String spYear = CommonTools.getFinalStr("spYear", request);
		String potPpId = CommonTools.getFinalStr("potPpId", request);
		String qualId = CommonTools.getFinalStr("qualId", request);
		String ttImg = CommonTools.getFinalStr("ttImg", request);
		Integer potVol = CommonTools.getFinalInteger("potVol", request);
		String spYearPot = CommonTools.getFinalStr("spYearPot", request);
		String buyYear = CommonTools.getFinalStr("buyYear", request);
		String headTypeId = CommonTools.getFinalStr("headTypeId", request);
		String headPpId = CommonTools.getFinalStr("headPpId", request);
		String trucksTypeId = CommonTools.getFinalStr("trucksTypeId", request);
		Integer xsDistance = CommonTools.getFinalInteger("xsDistance", request);
		String remark = CommonTools.getFinalStr("remark", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		String regPlace = CommonTools.getFinalStr("regPlace", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		Integer price = CommonTools.getFinalInteger("price", request);
		// String addUserId = CommonTools.getFinalStr("addUserId", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);
		Integer tradeType = CommonTools.getFinalInteger("tradeType", request);
		String area = CommonTools.getFinalStr("area", request);
		String qyTypeId = CommonTools.getFinalStr("qyTypeId", request);
		String wqpfbzId = CommonTools.getFinalStr("wqpfbzId", request);
		Integer accidentFlag = CommonTools.getFinalInteger("accidentFlag", request);
		String tructsHeadxsz = CommonTools.getFinalStr("tructsHeadxsz", request);
		String gcXsz = CommonTools.getFinalStr("gcXsz", request);
		String tructsYyz = CommonTools.getFinalStr("tructsYyz", request);
		String potJyz = CommonTools.getFinalStr("potJyz", request);
		String aqfbg = CommonTools.getFinalStr("aqfbg", request);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;

		try {
		 if (cilentInfo.equals("wxApp")) {

			}else if(CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request), Constants.UP_TRTR)) {} else {
				status = 70001;
			}

			if (status.equals(200)) {
				TrucksTrade trtr = trucksTradeService.getEntityById(id);
				if (trtr == null) {
					status = 50001;
				} else if (trtr.getCheckStatus() == 1) {
					status = 80001;
				} else {
					if (!compId.isEmpty() && !compId.equals(trtr.getCompanyId())) {
						trtr.setCompanyId(compId);
					}

					if (!mainImg.equals("") && !compId.equals(trtr.getMainImg())) {
						trtr.setMainImg(CommonTools.dealUploadDetail(loginUserId, trtr.getMainImg(), mainImg));
					}
					if (!trucksNo.isEmpty() && !trucksNo.equals(trtr.getTrucksNo())) {
						trtr.setTrucksNo(trucksNo);
					}
					if (!spYear.isEmpty() && !spYear.equals(trtr.getSpYear())) {
						trtr.setSpYear(spYear);
					}
					if (!spYearPot.isEmpty() && !spYearPot.equals(trtr.getSpYearPot())) {
						trtr.setSpYearPot(spYearPot);
					}
					if (!potPpId.isEmpty() && !potPpId.equals(trtr.getPotPpId())) {
						trtr.setPotPpId(potPpId);
					}
					if (potVol != null && !potVol.equals(trtr.getPotVolume())) {
						trtr.setPotVolume(potVol);
					}
					if (!spYearPot.isEmpty() && !spYearPot.equals(trtr.getSpYearPot())) {
						trtr.setSpYearPot(spYearPot);
					}
					if (!buyYear.isEmpty() && !buyYear.equals(trtr.getBuyYear())) {
						trtr.setBuyYear(buyYear);
					}
					if (!headTypeId.isEmpty() && !headTypeId.equals(trtr.getTrucksHeadType().getId())) {
						TrucksHeadType trucksHeadType = headTypeService.findById(headTypeId);
						trtr.setTrucksHeadType(trucksHeadType);
					}
					if (!headPpId.isEmpty() && !headPpId.equals(trtr.getTrucksHeadPp().getId())) {
						TrucksHeadPp trucksHeadPp = headPpService.findById(headPpId);
						trtr.setTrucksHeadPp(trucksHeadPp);
					}
					if (!trucksTypeId.isEmpty() && !trucksTypeId.equals(trtr.getTrucksType().getId())) {
						TrucksType trucksType = typeService.findById(trucksTypeId);
						trtr.setTrucksType(trucksType);
					}
					if (xsDistance != null && !xsDistance.equals(trtr.getXsDistance())) {
						trtr.setXsDistance(xsDistance);
					}
					if (!remark.isEmpty() && !remark.equals(trtr.getRemark())) {
						trtr.setRemark(remark);
					}
					if (!lxName.isEmpty() && !lxName.equals(trtr.getLxName())) {
						trtr.setLxName(lxName);
					}
					if (!lxTel.isEmpty() && !lxTel.equals(trtr.getLxTel())) {
						trtr.setLxTel(lxTel);
					}
					if (!regPlace.isEmpty() && !regPlace.equals(trtr.getRegPlace())) {
						trtr.setRegPlace(regPlace);
					}
					if (price != null && !price.equals(trtr.getPrice())) {
						trtr.setPrice(price);
					}
					if (showStatus != null && !showStatus.equals(trtr.getShowStatus())) {
						trtr.setShowStatus(showStatus);
					}

					trtr.setAddUserId(loginUserId);

					if (userType != null && !userType.equals(trtr.getUserType())) {
						trtr.setUserType(userType);
					}
					if (tradeType != null && tradeType.equals(trtr.getTradeType())) {
						trtr.setTradeType(tradeType);
					}
					if (!area.isEmpty() && !area.equals(trtr.getArea())) {
						trtr.setArea(area);
					}
					if (!qyTypeId.isEmpty() && !qyTypeId.equals(trtr.getQyTypeId())) {
						trtr.setQyTypeId(qyTypeId);
					}
					if (!wqpfbzId.isEmpty() && !wqpfbzId.equals(trtr.getWqPfbz().getId())) {
						WqPfbz wqPfbz = wqPfBzService.findById(wqpfbzId);
						trtr.setWqPfbz(wqPfbz);
					}
					if (accidentFlag != null && !accidentFlag.equals(trtr.getAccidentFlag())) {
						trtr.setAccidentFlag(accidentFlag);
					}
					if (!tructsHeadxsz.isEmpty() && !tructsHeadxsz.equals(trtr.getTrucksHeadxsz())) {
						trtr.setTrucksHeadxsz(
								CommonTools.dealUploadDetail(loginUserId, trtr.getTrucksHeadxsz(), tructsHeadxsz));
					}
					if (!gcXsz.isEmpty() && !gcXsz.equals(trtr.getGcXsz())) {
						trtr.setGcXsz(CommonTools.dealUploadDetail(loginUserId, trtr.getGcXsz(), gcXsz));
					}
					if (!tructsYyz.isEmpty() && !tructsYyz.equals(trtr.getTructsYyz())) {
						trtr.setTructsYyz(CommonTools.dealUploadDetail(loginUserId, trtr.getTructsYyz(), tructsYyz));
					}
					if (!potJyz.isEmpty() && !potJyz.equals(trtr.getPotJyz())) {
						trtr.setPotJyz(CommonTools.dealUploadDetail(loginUserId, trtr.getPotJyz(), potJyz));
					}
					if (!aqfbg.isEmpty() && !aqfbg.equals(trtr.getAqfBg())) {
						trtr.setAqfBg(CommonTools.dealUploadDetail(loginUserId, trtr.getAqfBg(), aqfbg));
					}
					trucksTradeService.saveOrUpdate(trtr);

					List<TrucksTradeQualification> ttqs = ttQualService.getTrucksTradeQualList(id);
					if (!ttqs.isEmpty()) {
						ttQualService.deleteBatch(ttqs);
					}

					if (!qualId.isEmpty()) {

						String[] qualArr = qualId.split(",");
						for (int i = 0; i < qualArr.length; i++) {
							Qualification qual = qualService.findById(qualArr[i]);
							TrucksTradeQualification ttQual = new TrucksTradeQualification();
							ttQual.setQualification(qual);
							ttQual.setTrucksTrade(trtr);
							ttQualService.addOrUpdate(ttQual);
						}
					}

					List<TructsTradeZz> zzs = ttzzService.getTructsTradeZzByttId(id);
					String imgPath_db = "";
					if (!zzs.isEmpty()) {
						for (TructsTradeZz ttz : zzs) {
							imgPath_db += ttz.getTructsTradeZz() + ",";
						}
						imgPath_db = imgPath_db.substring(0, imgPath_db.length() - 1);
						ttzzService.deleteBatch(zzs);
					}
					if (!ttImg.equals("")) {
						String ttImgs = "";
						ttImgs = CommonTools.dealUploadDetail(loginUserId, imgPath_db, ttImg);
						String[] ttimgArr = ttImgs.split(",");
						List<TructsTradeZz> zzlist = new ArrayList<>();
						for (int i = 0; i < ttimgArr.length; i++) {
							TructsTradeZz ttzz = new TructsTradeZz();
							ttzz.setTrucksTrade(trtr);
							ttzz.setTructsTradeZz(ttimgArr[i]);
							zzlist.add(ttzz);
						}
						ttzzService.addOrUpdateBatch(zzlist);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}

		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/queryTrucksTrade")
	@ApiOperation(value = "获取货车租卖", notes = "获取货车租卖分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "userId", value = "上传人员"),
			@ApiImplicitParam(name = "tradeType", value = "贸易类型（1：租赁，2：买卖）"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "spYear", value = "上牌年份"),
			@ApiImplicitParam(name = "potPpId", value = "储罐品牌"),
			@ApiImplicitParam(name = "headPpId", value = "车头品牌"),
			@ApiImplicitParam(name = "page", value = "第几页"), 
			@ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse queryTrucksTrade(HttpServletRequest request) {
		Integer checkSta = CommonTools.getFinalInteger("checkSta", request);
		String addUserId = CommonTools.getFinalStr("userId", request);
		Integer tradeType = CommonTools.getFinalInteger("tradeType", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		String spYear = CommonTools.getFinalStr("spYear", request);
		String potPpId = CommonTools.getFinalStr("potPpId", request);
		String headPpId = CommonTools.getFinalStr("headPpId", request);
		Integer page = CommonTools.getFinalInteger("page", request);
		Integer limit = CommonTools.getFinalInteger("limit", request);
		Integer status = 200;
		Page<TrucksTrade> tts = null;
		long count = 0;
		List<Object> list = new ArrayList<Object>();
		try {
			if (page == 0) {
				page = 1;
			}
			if (limit == 0) {
				limit = 10;
			}
			if (checkSta == 0) {
				checkSta = -1;
			}
			if (tradeType == 0) {
				tradeType = -1;
			}
			if (showStatus == 0) {
				showStatus = -1;
			}
			tts = trucksTradeService.getTrucksTradeByOption(checkSta, addUserId, tradeType, showStatus,
					spYear,potPpId,headPpId,page - 1,limit);
			count = tts.getTotalElements();
			if (count == 0) {
				status = 50001;
			} else {
				List<TrucksTrade> ttList = tts.getContent();
				for (TrucksTrade tt : ttList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("mainImg", tt.getMainImg());
					map.put("ttId", tt.getId());
					Integer tadeType = tt.getTradeType();
					Integer price = tt.getPrice();
					if(price.equals(0)) {
						map.put("price", "面议");
					}else {
						if (tadeType.equals(1)) {
							map.put("TradeTypeName", "租赁");
							map.put("price", price);
						} else if (tadeType.equals(2)) {
							map.put("TradeTypeName", "买卖");
							map.put("price", price / 10000.0);
						}
					}
					if (!tt.getCompanyId().isEmpty()) {
						Company cpy = comService.getEntityById(tt.getCompanyId());
						map.put("CompanyName", cpy.getName());
					}else {
						map.put("CompanyName", "个人");
					}
					TrucksType trucksType = tt.getTrucksType();
					map.put("title", tt.getTrucksHeadPp().getName() + trucksType.getName());
					map.put("regPlace", tt.getRegPlace());
					map.put("trucksTypeName", trucksType.getName());
					if (trucksType.getType() == 1) {
						map.put("trucksTypes", "普货车");
					} else if (trucksType.getType() == 2) {
						map.put("trucksTypes", "危货车");
					}

					WqPfbz pfbz = wqPfBzService.findById(tt.getWqPfbz().getId());
					map.put("pfbz", pfbz.getName());
					map.put("xsDistance", tt.getXsDistance());
					map.put("spYear", tt.getSpYear());
					map.put("checkStatus", tt.getCheckStatus());
					map.put("showStatus", tt.getShowStatus());
					map.put("area", tt.getArea());
					list.add(map);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, count, status, list);
	}

	@GetMapping("/getSpecTrucksTrade")
	@ApiOperation(value = "根据主键获取货车租卖详细信息", notes = "根据主键获取货车租卖详细信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 10002, message = "参数为空"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "货车租卖编号", required = true),
			@ApiImplicitParam(name = "userId", value = "用户编号") })
	public GenericResponse getSpecTrucksTrade(HttpServletRequest request) {
		Integer status = 200;
		String ttId = CommonTools.getFinalStr("id", request);
		String userId = CommonTools.getFinalStr("userId", request);
		List<Object> list = new ArrayList<Object>();

		try {
			if (ttId.equals("")) {
				status = 10002;
			} else {
				TrucksTrade tt = trucksTradeService.getEntityById(ttId);
				if (tt == null) {
					status = 50001;
				} else {
					// 获取该租卖的进港资质
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("companyId", tt.getCompanyId());
					map.put("title", tt.getTrucksHeadPp().getName() + tt.getTrucksType().getName());
					map.put("mainImg", tt.getMainImg());
					map.put("trucksNo", tt.getTrucksNo());
					map.put("trucksGcNo", tt.getTrucksGcNo());
					map.put("spYear", tt.getSpYear());
					String potPpId = tt.getPotPpId();
					String potPpName = "";
					map.put("potPpId", potPpId);
					if(potPpId.equals("")) {
						TrucksPotPp tpp = tpps.findById(potPpId);
						if(tpp != null) {
							potPpName = tpp.getName();
						}
					}
					map.put("potPpName", potPpName);
					map.put("potVol", tt.getPotVolume());
					map.put("spYearPot", tt.getSpYearPot());
					map.put("buyYear", tt.getBuyYear());
					map.put("headTypeId", tt.getTrucksHeadType().getId());
					map.put("headTypeName", tt.getTrucksHeadType().getName());
					map.put("headPpId", tt.getTrucksHeadPp().getId());
					map.put("headPpName", tt.getTrucksHeadPp().getName());
					TrucksType trucksType  = tt.getTrucksType();
					map.put("trucksTypeId", trucksType.getId());
					map.put("trucksTypes", trucksType.getType());
					map.put("trucksTypeName", trucksType.getName());
					map.put("xsDistance", tt.getXsDistance());
					if (!tt.getCompanyId().isEmpty()) {
						Company cpy = comService.getEntityById(tt.getCompanyId());
						map.put("CompanyName", cpy.getName());
					}else {
						map.put("CompanyName", "个人");
					}
					Integer tadeType = tt.getTradeType();
					Integer price = tt.getPrice();
					if(price.equals(0)) {
						map.put("price", "面议");
					}else {
						if (tadeType.equals(1)) {
							map.put("TradeTypeName", "租赁");
							map.put("price", price);
						} else if (tadeType.equals(2)) {
							map.put("TradeTypeName", "买卖");
							map.put("price", price / 10000.0);
						}
					}
					map.put("regPlace", tt.getRegPlace());
					map.put("remark", tt.getRemark());
					map.put("lxName", tt.getLxName());
					map.put("lxTel", tt.getLxTel());
					String addUserId = tt.getAddUserId();
					String userHead = "";
					if(!addUserId.equals("")) {
						User user = us.getEntityById(addUserId);
						if(user != null) {
							userHead = user.getUserPortrait();
						}
					}
					map.put("userHead", userHead);
					map.put("checkStatus", tt.getCheckStatus());
					map.put("checkTime", tt.getCheckTime());
					map.put("showStatus", tt.getShowStatus());
					map.put("addUserId", addUserId);
					map.put("addTime", tt.getAddTime());
					map.put("userType", tt.getUserType());
					map.put("hot", tt.getHot());
					map.put("tradeType", tt.getTradeType());
					map.put("area", tt.getArea());
					map.put("qyTypeId", tt.getQyTypeId());
					map.put("pfbzId", tt.getWqPfbz().getId());
					map.put("pfbzName", tt.getWqPfbz().getName());
					map.put("accidentFlag", tt.getAccidentFlag());
					map.put("tructsHeadxsz", tt.getTrucksHeadxsz());
					map.put("gcXsz", tt.getGcXsz());
					map.put("tructsYyz", tt.getTructsYyz());
					map.put("potjyz", tt.getPotJyz());
					map.put("aqfBg", tt.getAqfBg());
					List<TructsTradeZz> zzs = ttzzService.getTructsTradeZzByttId(ttId);
					List<Object> zzlist = new ArrayList<Object>();
					if (!zzs.isEmpty()) {
						for (int i = 0; i < zzs.size(); i++) {
							Map<String, Object> zzmap = new HashMap<String, Object>();
							zzmap.put("ttImg", zzs.get(i).getTructsTradeZz());
							zzlist.add(zzmap);
						}
					}

					List<TrucksTradeQualification> ttqualList = ttQualService.getTrucksTradeQualList(ttId);
					List<Object> tQllist = new ArrayList<Object>();
					if (!ttqualList.isEmpty()) {
						for (int i = 0; i < ttqualList.size(); i++) {
							Map<String, Object> qualmap = new HashMap<String, Object>();
							qualmap.put("qualId", ttqualList.get(i).getQualification().getId());
							qualmap.put("qualName", ttqualList.get(i).getQualification().getName());
							tQllist.add(qualmap);
						}
					}
					String ufId = "";
					if (!userId.isEmpty()) {
						List<UserFocus> ufList = ufService.getUserFocusList(userId, ttId, "cczm");
						if(ufList.size() > 0) {
							ufId = ufList.get(0).getId();
						}
					}
					map.put("ufId", ufId);
					map.put("zzlist", zzlist);
					map.put("tqList", tQllist);
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

}
