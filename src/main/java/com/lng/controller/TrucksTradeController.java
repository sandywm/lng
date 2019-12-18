package com.lng.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.TrucksHeadPp;
import com.lng.pojo.TrucksHeadType;
import com.lng.pojo.TrucksTrade;
import com.lng.pojo.TrucksType;
import com.lng.pojo.WqPfbz;
import com.lng.service.TrucksHeadPpService;
import com.lng.service.TrucksHeadTypeService;
import com.lng.service.TrucksTradeService;
import com.lng.service.TrucksTypeService;
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

	@PostMapping("/addTrucksTrade")
	@ApiOperation(value = "添加货车租卖", notes = "添加货车租卖")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号"),
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
			@ApiImplicitParam(name = "remark", value = "备注"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "addUserId", value = "上传人员", required = true),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true),
			@ApiImplicitParam(name = "tradeType", value = "贸易类型（1：租赁，2：买卖）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "area", value = "运输范围（租赁）"), @ApiImplicitParam(name = "qyTypeId", value = "气源类型"),
			@ApiImplicitParam(name = "wqpfbzId", value = "尾气排放标准编号", required = true),
			@ApiImplicitParam(name = "accidentFlag", value = "是否发生事故（1：是,2：否，0：不填）"),
			@ApiImplicitParam(name = "tructsHeadxsz", value = "车头行驶证（危货）"),
			@ApiImplicitParam(name = "gcXsz", value = "罐车行驶证（危货）"),
			@ApiImplicitParam(name = "tructsYyz", value = "车辆运营证（危货）"),
			@ApiImplicitParam(name = "potJyz", value = "储罐检验合格证（危货）"),
			@ApiImplicitParam(name = "aqfbg", value = "安全阀校验报告（危货）") })
	public GenericResponse addTrucksTrade(HttpServletRequest request) {
		String compId = CommonTools.getFinalStr("compId", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String trucksNo = CommonTools.getFinalStr("trucksNo", request);
		String spYear = CommonTools.getFinalStr("spYear", request);
		String potPpId = CommonTools.getFinalStr("potPpId", request);
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
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		String addUserId = CommonTools.getFinalStr("addUserId", request);
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

		String loginUserId = CommonTools.getLoginUserId(request);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;
		String ttId = "";

		try {
			if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),
					Constants.ADD_TRTR)) {

			} else if (cilentInfo.equals("wxApp")) {
				loginUserId = CommonTools.getFinalStr("userId", request);
			} else {
				status = 70001;
			}

			if (status.equals(200)) {
				TrucksTrade trtr = new TrucksTrade();

				trtr.setCompanyId(compId);
				if (!mainImg.equals("")) {
					trtr.setMainImg(CommonTools.dealUploadDetail(addUserId, "", mainImg));
				}
				trtr.setTrucksNo(trucksNo);
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
				trtr.setAddUserId(addUserId);
				trtr.setAddTime(CurrentTime.getCurrentTime());
				trtr.setUserType(userType);
				trtr.setHot(0);
				trtr.setTradeType(tradeType);
				trtr.setArea(area);
				trtr.setQyTypeId(qyTypeId);
				WqPfbz wqPfbz = wqPfBzService.findById(wqpfbzId);
				trtr.setWqPfbz(wqPfbz);
				trtr.setAccidentFlag(accidentFlag);
				trtr.setTrucksHeadxsz(tructsHeadxsz);
				trtr.setGcXsz(gcXsz);
				trtr.setTructsYyz(tructsYyz);
				trtr.setPotJyz(potJyz);
				trtr.setAqfBg(aqfbg);
				ttId = trucksTradeService.saveOrUpdate(trtr);
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
			if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.UP_TRTR)) {

			} else if (cilentInfo.equals("wxApp")) {

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
			if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.UP_TRTR)) {

			} else if (cilentInfo.equals("wxApp")) {

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
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
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
			@ApiImplicitParam(name = "remark", value = "备注"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "addUserId", value = "上传人员", required = true),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true),
			@ApiImplicitParam(name = "tradeType", value = "贸易类型（1：租赁，2：买卖）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "area", value = "运输范围（租赁）"), @ApiImplicitParam(name = "qyTypeId", value = "气源类型"),
			@ApiImplicitParam(name = "wqpfbzId", value = "尾气排放标准编号", required = true),
			@ApiImplicitParam(name = "accidentFlag", value = "是否发生事故（1：是,2：否，0：不填）"),
			@ApiImplicitParam(name = "tructsHeadxsz", value = "车头行驶证（危货）"),
			@ApiImplicitParam(name = "gcXsz", value = "罐车行驶证（危货）"),
			@ApiImplicitParam(name = "tructsYyz", value = "车辆运营证（危货）"),
			@ApiImplicitParam(name = "potJyz", value = "储罐检验合格证（危货）"),
			@ApiImplicitParam(name = "aqfbg", value = "安全阀校验报告（危货）") })
	public GenericResponse updateTrucksTrade(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id", request);
		String compId = CommonTools.getFinalStr("compId", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String trucksNo = CommonTools.getFinalStr("trucksNo", request);
		String spYear = CommonTools.getFinalStr("spYear", request);
		String potPpId = CommonTools.getFinalStr("potPpId", request);
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
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		String addUserId = CommonTools.getFinalStr("addUserId", request);
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
			if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request), Constants.UP_TRTR)) {

			} else if (cilentInfo.equals("wxApp")) {

			} else {
				status = 70001;
			}

			if (status.equals("")) {
				TrucksTrade trtr = trucksTradeService.getEntityById(id);
				if (trtr == null) {
					status = 50001;
				} else {
					if (!compId.isEmpty() && !compId.equals(trtr.getCompanyId())) {
						trtr.setCompanyId(compId);
					}

					if (!mainImg.equals("") && !compId.equals(trtr.getMainImg())) {
						trtr.setMainImg(CommonTools.dealUploadDetail(addUserId, trtr.getMainImg(), mainImg));
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
					if (xsDistance != null && !xsDistance.equals(xsDistance.equals(trtr.getXsDistance()))) {
						trtr.setXsDistance(xsDistance);
					}
					if (!remark.isEmpty() && !remark.equals(remark.equals(trtr.getRemark()))) {
						trtr.setRemark(remark);
					}
					if (!lxName.isEmpty() && !lxName.equals(trtr.getLxName())) {
						trtr.setLxName(lxName);
					}
					if (!lxTel.isEmpty() && !lxTel.equals(trtr.getLxTel())) {
						trtr.setLxTel(lxTel);
					}
					if (showStatus != null && !showStatus.equals(trtr.getShowStatus())) {
						trtr.setShowStatus(showStatus);
					}
					if (!addUserId.isEmpty() && !addUserId.equals(trtr.getAddUserId())) {
						trtr.setAddUserId(addUserId);
					}
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
						trtr.setTrucksHeadxsz(tructsHeadxsz);
					}
					if (!gcXsz.isEmpty() && !gcXsz.equals(trtr.getGcXsz())) {
						trtr.setGcXsz(gcXsz);
					}
					if (!tructsYyz.isEmpty() && !tructsYyz.equals(trtr.getTructsYyz())) {
						trtr.setTructsYyz(tructsYyz);
					}
					if (!potJyz.isEmpty() && !potJyz.equals(trtr.getPotJyz())) {
						trtr.setPotJyz(potJyz);
					}
					if (!aqfbg.isEmpty() && !aqfbg.equals(trtr.getAqfBg())) {
						trtr.setAqfBg(aqfbg);
					}
					trucksTradeService.saveOrUpdate(trtr);
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
			@ApiImplicitParam(name = "addUserId", value = "上传人员"),
			@ApiImplicitParam(name = "tradeType", value = "贸易类型（1：租赁，2：买卖）"),
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryTrucksTrade(Integer checkSta, String addUserId, Integer tradeType, Integer pageNo,
			Integer pageSize) {
		Integer status = 200;
		Page<TrucksTrade> tts = null;
		try {
			addUserId = CommonTools.getFinalStr(addUserId);

			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			if (tradeType == null) {
				tradeType = -1;
			}
			tts = trucksTradeService.getTrucksTradeByOption(checkSta, addUserId, tradeType, pageNo, pageSize);
			if (tts.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, tts.getTotalElements(), status, tts.getContent());
	}

}
