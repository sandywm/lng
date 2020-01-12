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
import com.lng.pojo.PotTrade;
import com.lng.pojo.PotTradeImg;
import com.lng.pojo.PotZzjzType;
import com.lng.pojo.TrucksPotPp;
import com.lng.pojo.User;
import com.lng.pojo.UserFocus;
import com.lng.service.CompanyService;
import com.lng.service.PotTradeImgService;
import com.lng.service.PotTradeService;
import com.lng.service.PotZzjzTypeService;
import com.lng.service.TrucksPotPpService;
import com.lng.service.UserFocusService;
import com.lng.service.UserService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.tools.EmojiDealUtil;
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
@Api(tags = "储罐租卖相关接口")
@RequestMapping("/potTrade")
public class PotTradeController {

	@Autowired
	private PotTradeService potTradeService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private TrucksPotPpService potPpService;
	@Autowired
	private PotZzjzTypeService zzjzTypeService;
	@Autowired
	private PotTradeImgService ptiService;
	@Autowired
	private UserFocusService ufService;
	@Autowired
	private UserService us;

	@PostMapping("/addPotTrade")
	@ApiOperation(value = "添加储罐租卖", notes = "添加储罐租卖")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 20001, message = "用户未登录"),
			@ApiResponse(code = 200, message = "成功"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "mainImg", value = "储罐主图", required = true),
			@ApiImplicitParam(name = "potPpId", value = "储罐品牌编号", required = true),
			@ApiImplicitParam(name = "potVol", value = "储罐容积", required = true),
			@ApiImplicitParam(name = "sxInfo", value = "有无手续(1：有，2：无)", required = true, defaultValue = "有"),
			@ApiImplicitParam(name = "buyYear", value = "购买年份", required = true),
			@ApiImplicitParam(name = "zzjzTypeId", value = "装载介质类型编号", required = true),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "leasePrice", value = "租赁价格", defaultValue = "3000", required = true),
			@ApiImplicitParam(name = "sellPrice", value = "官方价格", defaultValue = "35000", required = true),
			@ApiImplicitParam(name = "remark", value = "备注"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "userId", value = "用户编号(微信)"),
			@ApiImplicitParam(name = "potDetailImg", value = "储罐详情图"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			// @ApiImplicitParam(name = "addUserId", value = "上传人员", required = true),
			// @ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）",
			// required = true),
			@ApiImplicitParam(name = "tradeStatus", value = "租卖类型(0:可租可卖,1:租,2:卖)", required = true, defaultValue = "0") })
	public GenericResponse addPotTrade(HttpServletRequest request) {
		String compId = CommonTools.getFinalStr("compId", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String potPpId = CommonTools.getFinalStr("potPpId", request);
		Integer potVol = CommonTools.getFinalInteger("potVol", request);
		String sxInfo = CommonTools.getFinalStr("sxInfo", request);
		String buyYear = CommonTools.getFinalStr("buyYear", request);
		String zzjzTypeId = CommonTools.getFinalStr("zzjzTypeId", request);
		String province = CommonTools.getFinalStr("province", request);
		String city = CommonTools.getFinalStr("city", request);
		Double leasePrice = CommonTools.getFinalDouble("leasePrice", request);
		Double sellPrice = CommonTools.getFinalDouble("sellPrice", request);
		String remark = CommonTools.getFinalStr("remark", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		// String addUserId = CommonTools.getFinalStr("addUserId", request);
		String potDetailImg = CommonTools.getFinalStr("potDetailImg", request);
		// Integer userType = CommonTools.getFinalInteger("userType", request);
		Integer userType = 1;
		Integer tradeStatus = CommonTools.getFinalInteger("tradeStatus", request);
		String loginUserId = CommonTools.getLoginUserId(request);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;
		String ptId = "";

		try {
			if (cilentInfo.equals("wxApp")) {
				userType = 2;
				loginUserId = CommonTools.getFinalStr("userId", request);
				if (loginUserId.isEmpty()) {
					status = 20001;
				}
			} else if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),
					Constants.ADD_PT)) {

			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				PotTrade pt = new PotTrade();
				Company company = companyService.getEntityById(compId);
				pt.setCompany(company);
				if (!mainImg.equals("")) {
					pt.setMainImg(CommonTools.dealUploadDetail(loginUserId, "", mainImg));
				}
				TrucksPotPp trucksPotPp = potPpService.findById(potPpId);
				pt.setTrucksPotPp(trucksPotPp);
				pt.setPotVolume(potVol);
				pt.setSxInfo(sxInfo);
				pt.setBuyYear(buyYear);
				PotZzjzType potZzjzType = zzjzTypeService.findById(zzjzTypeId);
				pt.setPotZzjzType(potZzjzType);
				pt.setProvince(province);
				pt.setCity(city);
				pt.setLeasePrice(leasePrice);
				pt.setSellPrice(sellPrice);
				if (!remark.isEmpty()) {
					pt.setRemark(EmojiDealUtil.changeEmojiToHtml(remark));
				}else {
					pt.setRemark(remark);
				}
				pt.setLxName(lxName);
				pt.setLxTel(lxTel);
				if (userType.equals(1)) {
					pt.setCheckStatus(1);
					pt.setCheckTime(CurrentTime.getCurrentTime());
				} else {
					pt.setCheckStatus(0);
					pt.setCheckTime("");
				}
				pt.setShowStatus(showStatus);
				pt.setAddUserId(loginUserId);
				pt.setAddTime(CurrentTime.getCurrentTime());
				pt.setUserType(userType);
				pt.setHot(0);
				pt.setTradeStatus(tradeStatus);
				ptId = potTradeService.saveOrUpdate(pt);

				if (!potDetailImg.isEmpty()) {
					String ptImgs = CommonTools.dealUploadDetail(loginUserId, "", potDetailImg);
					String[] ptimgArr = ptImgs.split(",");
					List<PotTradeImg> ptList = new ArrayList<>();
					for (int i = 0; i < ptimgArr.length; i++) {
						PotTradeImg pti = new PotTradeImg();
						pti.setPotTrade(pt);
						pti.setPotDetailImg(ptimgArr[i]);
						ptList.add(pti);
					}
					ptiService.addOrUpdateBatch(ptList);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, ptId);
	}

	@PutMapping("/updatePotTradeByStatus")
	@ApiOperation(value = "更新储罐租卖审核状态", notes = "更新储罐租卖审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "储罐租卖编号", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）") })
	public GenericResponse updatePotTradeByStatus(HttpServletRequest request, String id, Integer checkSta,
			Integer showSta) {
		id = CommonTools.getFinalStr(id);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;

		try {
			if (cilentInfo.equals("wxApp")) {

			} else if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.CHECK_PT)) {

			} else {
				status = 70001;
			}

			if (status.equals(200)) {
				PotTrade pt = potTradeService.getEntityById(id);
				if (pt == null) {
					status = 50001;
				} else {
					if (checkSta != null && !checkSta.equals(pt.getCheckStatus())) {
						pt.setCheckStatus(checkSta);
						pt.setCheckTime(CurrentTime.getCurrentTime());
					}
					if (showSta != null && !showSta.equals(pt.getShowStatus())) {
						pt.setShowStatus(showSta);
					}
					potTradeService.saveOrUpdate(pt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updatePotTradeByHot")
	@ApiOperation(value = "更新储罐租卖热度", notes = "更新储罐租卖热度")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "储罐租卖编号", required = true),
			@ApiImplicitParam(name = "hot", value = "热度（默认为0）") })
	public GenericResponse updatePotTradeByHot(HttpServletRequest request, String id, Integer hot) {
		id = CommonTools.getFinalStr(id);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;

		try {
			if (cilentInfo.equals("wxApp")) {

			} else if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.UP_PT)) {

			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				PotTrade pt = potTradeService.getEntityById(id);
				if (pt == null) {
					status = 50001;
				} else {
					if (hot != null && !hot.equals(pt.getHot())) {
						pt.setHot(hot);
					}
					potTradeService.saveOrUpdate(pt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updatePotTrade")
	@ApiOperation(value = "更新储罐租卖基本信息", notes = "更新储罐租卖基本信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 20001, message = "用户未登录"), @ApiResponse(code = 80001, message = "审核通过不能修改"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "储罐租卖编号", required = true),
			@ApiImplicitParam(name = "mainImg", value = "储罐主图", required = true),
			@ApiImplicitParam(name = "potPpId", value = "储罐品牌编号", required = true),
			@ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "potVol", value = "储罐容积", required = true),
			@ApiImplicitParam(name = "sxInfo", value = "有无手续(1：有，2：无)", defaultValue = "有", required = true),
			@ApiImplicitParam(name = "buyYear", value = "购买年份", required = true),
			@ApiImplicitParam(name = "zzjzTypeId", value = "装载介质类型编号", required = true),
			@ApiImplicitParam(name = "province", value = "省", defaultValue = "河南", required = true),
			@ApiImplicitParam(name = "city", value = "市", defaultValue = "濮阳", required = true),
			@ApiImplicitParam(name = "leasePrice", value = "租赁价格", defaultValue = "3000", required = true),
			@ApiImplicitParam(name = "sellPrice", value = "官方价格", defaultValue = "35000", required = true),
			@ApiImplicitParam(name = "remark", value = "备注"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "userId", value = "用户编号(WX)"),
			@ApiImplicitParam(name = "potDetailImg", value = "储罐详情图"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", defaultValue = "0", required = true),
			@ApiImplicitParam(name = "tradeStatus", value = "租卖类型(0:可租可卖,1:租,2:卖)", defaultValue = "0", required = true) })
	public GenericResponse updatePotTrade(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String compId = CommonTools.getFinalStr("compId", request);
		String potPpId = CommonTools.getFinalStr("potPpId", request);
		Integer potVol = CommonTools.getFinalInteger("potVol", request);
		String sxInfo = CommonTools.getFinalStr("sxInfo", request);
		String buyYear = CommonTools.getFinalStr("buyYear", request);
		String zzjzTypeId = CommonTools.getFinalStr("zzjzTypeId", request);
		String province = CommonTools.getFinalStr("province", request);
		String city = CommonTools.getFinalStr("city", request);
		Double leasePrice = CommonTools.getFinalDouble("leasePrice", request);
		Double sellPrice = CommonTools.getFinalDouble("sellPrice", request);
		String remark = CommonTools.getFinalStr("remark", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		String potDetailImg = CommonTools.getFinalStr("potDetailImg", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		Integer tradeStatus = CommonTools.getFinalInteger("tradeStatus", request);
		String loginUserId = CommonTools.getLoginUserId(request);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;

		try {
			if (cilentInfo.equals("wxApp")) {
				loginUserId = CommonTools.getFinalStr("userId", request);
				if (loginUserId.isEmpty()) {
					status = 20001;
				}
			} else if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),
					Constants.UP_PT)) {

			} else {

			}
			if (status.equals(200)) {
				PotTrade pt = potTradeService.getEntityById(id);
				if (pt == null) {
					status = 50001;
				} else if (pt.getCheckStatus() == 1) {
					status = 80001;
				} else {

					if (!mainImg.isEmpty() && !mainImg.equals(pt.getMainImg())) {
						pt.setMainImg(CommonTools.dealUploadDetail(loginUserId, pt.getMainImg(), mainImg));
					}
					if (!compId.isEmpty() && !compId.equals(pt.getCompany().getId())) {
						Company company = companyService.getEntityById(compId);
						pt.setCompany(company);
					}

					if (!potPpId.isEmpty() && !potPpId.equals(pt.getTrucksPotPp().getId())) {
						TrucksPotPp trucksPotPp = potPpService.findById(potPpId);
						pt.setTrucksPotPp(trucksPotPp);
					}
					if (!zzjzTypeId.isEmpty() && !zzjzTypeId.equals(pt.getPotZzjzType().getId())) {
						PotZzjzType potZzjzType = zzjzTypeService.findById(zzjzTypeId);
						pt.setPotZzjzType(potZzjzType);
					}
					if (potVol != null && !potVol.equals(pt.getPotVolume())) {
						pt.setPotVolume(potVol);
					}
					if (!sxInfo.isEmpty() && !sxInfo.equals(pt.getSxInfo())) {
						pt.setSxInfo(sxInfo);
					}
					if (!buyYear.isEmpty() && !buyYear.equals(pt.getBuyYear())) {
						pt.setBuyYear(buyYear);
					}
					if (!province.isEmpty() && !province.equals(pt.getProvince())) {
						pt.setProvince(province);
					}
					if (!city.isEmpty() && !city.equals(pt.getCity())) {
						pt.setCity(city);
					}
					if (leasePrice != null && !leasePrice.equals(pt.getLeasePrice())) {
						pt.setLeasePrice(leasePrice);
					}
					if (sellPrice != null && !sellPrice.equals(pt.getSellPrice())) {
						pt.setSellPrice(sellPrice);
					}
					if (!remark.isEmpty() && !remark.equals(pt.getRemark())) {
						pt.setRemark(EmojiDealUtil.changeEmojiToHtml(remark));
					}
					if (!lxName.isEmpty() && !lxName.equals(pt.getLxName())) {
						pt.setLxName(EmojiDealUtil.changeEmojiToHtml(lxName));
					}
					if (!lxTel.isEmpty() && !lxTel.equals(pt.getLxTel())) {
						pt.setLxTel(lxTel);
					}
					if (showStatus != null && !showStatus.equals(pt.getShowStatus())) {
						pt.setShowStatus(showStatus);
					}
					if (tradeStatus != null && !tradeStatus.equals(pt.getTradeStatus())) {
						pt.setTradeStatus(tradeStatus);
					}
					potTradeService.saveOrUpdate(pt);

					List<PotTradeImg> ptiList = ptiService.getPotTradeImgByPtId(id);
					String imgPath_db = "";
					if (!ptiList.isEmpty()) {
						for (PotTradeImg pti : ptiList) {
							imgPath_db += pti.getPotDetailImg() + ",";
						}
						imgPath_db = imgPath_db.substring(0, imgPath_db.length() - 1);
						ptiService.deleteBatch(ptiList);
					}
					if (!potDetailImg.isEmpty()) {
						String ptImgs = CommonTools.dealUploadDetail(loginUserId, imgPath_db, potDetailImg);
						String[] ptimgArr = ptImgs.split(",");
						List<PotTradeImg> ptList = new ArrayList<>();
						for (int i = 0; i < ptimgArr.length; i++) {
							PotTradeImg pti = new PotTradeImg();
							pti.setPotTrade(pt);
							pti.setPotDetailImg(ptimgArr[i]);
							ptList.add(pti);
						}
						ptiService.addOrUpdateBatch(ptList);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/queryPotTrade")
	@ApiOperation(value = "获取储罐租卖", notes = "获取储罐租卖分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "potPpId", value = "储罐品牌编号"),
			@ApiImplicitParam(name = "potVol", value = "储罐容积"),
			@ApiImplicitParam(name = "sxInfo", value = "有无手续(1：有，2：无)"),
			@ApiImplicitParam(name = "zzjzTypeId", value = "装载介质类型编号"),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "tradeStatus", value = "租卖类型(0:可租可卖,1:租,2:卖)"),

			@ApiImplicitParam(name = "page", value = "第几页"), @ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse queryPotTrade(String potPpId, Integer potVol, String sxInfo, String zzjzTypeId,
			Integer checkSta, Integer showStatus, Integer tradeStatus, Integer page, Integer limit) {
		Integer status = 200;
		Page<PotTrade> pts = null;
		List<Object> list = new ArrayList<Object>();
		try {
			potPpId = CommonTools.getFinalStr(potPpId);
			sxInfo = CommonTools.getFinalStr(sxInfo);
			zzjzTypeId = CommonTools.getFinalStr(zzjzTypeId);
			if (page == null) {
				page = 1;
			}
			if (limit == null) {
				limit = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			if (showStatus == null) {
				showStatus = -1;
			}
			if (tradeStatus == null) {
				tradeStatus = -1;
			}
			if (potVol == null) {
				potVol = -1;
			}
			pts = potTradeService.getPotTradeByOption(potPpId, potVol, sxInfo, zzjzTypeId, checkSta, showStatus,
					tradeStatus, page - 1, limit);
			if (pts.getTotalElements() == 0) {
				status = 50001;
			} else {
				List<PotTrade> ttList = pts.getContent();
				for (PotTrade pt : ttList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ptId", pt.getId());
					map.put("tradeStatus", pt.getTradeStatus());
					map.put("cpyName", pt.getCompany().getName());
					map.put("potPpName", pt.getTrucksPotPp().getName());
					map.put("potVol", pt.getPotVolume());
					map.put("sxInfo", pt.getSxInfo());
					map.put("leasePrice", pt.getLeasePrice());
					map.put("sellPrice", pt.getSellPrice());
					map.put("checkStatus", pt.getCheckStatus());
					map.put("showStatus", pt.getShowStatus());
					map.put("mainImg", pt.getMainImg());
					map.put("buyYear", pt.getBuyYear());
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, pts.getTotalElements(), status, list);
	}

	@GetMapping("/getPotTradeById")
	@ApiOperation(value = "根据主键获取储罐租卖详细信息", notes = "根据主键获取储罐租卖详细信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 10002, message = "参数为空"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "储罐租卖编号", required = true),
			@ApiImplicitParam(name = "userId", value = "用户编号") })
	public GenericResponse getPotTradeById(HttpServletRequest request) {
		Integer status = 200;
		String ptId = CommonTools.getFinalStr("id", request);
		String userId = CommonTools.getFinalStr("userId", request);
		List<Object> list = new ArrayList<Object>();

		try {
			if (ptId.equals("")) {
				status = 10002;
			} else {
				PotTrade pt = potTradeService.getEntityById(ptId);
				if (pt == null) {
					status = 10002;
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("cpyId", pt.getCompany().getId());
					map.put("cpyName", pt.getCompany().getName());
					map.put("mainImg", pt.getMainImg());
					map.put("potPpId", pt.getTrucksPotPp().getId());
					map.put("potPpName", pt.getTrucksPotPp().getName());
					map.put("potVolume", pt.getPotVolume());
					map.put("sxInfo", pt.getSxInfo());
					map.put("buyYear", pt.getBuyYear());
					map.put("zzjzTypeId", pt.getPotZzjzType().getId());
					map.put("zzJzTypeName", pt.getPotZzjzType().getName());
					map.put("province", pt.getProvince());
					map.put("city", pt.getCity());
					map.put("leasePrice", pt.getLeasePrice());
					map.put("sellPrice", pt.getSellPrice());
					map.put("reMark", EmojiDealUtil.changeStrToEmoji(pt.getRemark()));
					map.put("lxName", EmojiDealUtil.changeStrToEmoji(pt.getLxName()));
					map.put("lxTel", pt.getLxTel());
					map.put("checkStatus", pt.getCheckStatus());
					map.put("checkTime", pt.getCheckTime());
					String addUserId = pt.getAddUserId();
					String userHead = "";
					if (!addUserId.equals("")) {
						User user = us.getEntityById(addUserId);
						if (user != null) {
							userHead = user.getUserPortrait();
						}
					}
					map.put("userHead", userHead);
					map.put("addTime", pt.getAddTime());
					map.put("userType", pt.getUserType());
					map.put("hot", pt.getHot());
					map.put("tradeStatus", pt.getTradeStatus());
					List<PotTradeImg> pti = ptiService.getPotTradeImgByPtId(ptId);
					List<Object> ptilist = new ArrayList<Object>();
					if (!pti.isEmpty()) {
						for (int i = 0; i < pti.size(); i++) {
							Map<String, Object> ptimap = new HashMap<String, Object>();
							ptimap.put("pdiImg", pti.get(i).getPotDetailImg());
							ptilist.add(ptimap);
						}
					}
					String ufId = "";
					map.put("ufId", ufId);
					map.put("detailImg", ptilist);
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
