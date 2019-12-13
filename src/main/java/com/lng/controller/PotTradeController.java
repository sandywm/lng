package com.lng.controller;

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
import com.lng.pojo.PotZzjzType;
import com.lng.pojo.TrucksPotPp;
import com.lng.service.CompanyService;
import com.lng.service.PotTradeService;
import com.lng.service.PotZzjzTypeService;
import com.lng.service.TrucksPotPpService;
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

	@PostMapping("/addPotTrade")
	@ApiOperation(value = "添加储罐租卖", notes = "添加储罐租卖")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ 
		    @ApiImplicitParam(name = "compId", value = "公司编号", required = true),
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
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "addUserId", value = "上传人员", required = true),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true),
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
		String addUserId = CommonTools.getFinalStr("addUserId", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);
		Integer tradeStatus = CommonTools.getFinalInteger("tradeStatus", request);
		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;
		String ptId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_PT)) {
			try {
				PotTrade pt = new PotTrade();
				Company company = companyService.getEntityById(compId);
				pt.setCompany(company);
				if(!mainImg.equals("")) {
					pt.setMainImg(CommonTools.dealUploadDetail(loginUserId, mainImg));
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
				pt.setRemark(remark);
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
				pt.setAddUserId(addUserId);
				pt.setAddTime(CurrentTime.getCurrentTime());
				pt.setUserType(userType);
				pt.setHot(0);
				pt.setTradeStatus(tradeStatus);
				ptId = potTradeService.saveOrUpdate(pt);

			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
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
	public GenericResponse updatePotTradeByStatus(HttpServletRequest request, String id, Integer checkSta,Integer showSta) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_PT)) {
			try {
				PotTrade pt = potTradeService.getEntityById(id);
				if (pt == null) {
					status = 50001;
				} else {
					if (checkSta != null && !checkSta.equals(pt.getCheckStatus())) {
						pt.setCheckStatus(checkSta);
						pt.setCheckTime(CurrentTime.getCurrentTime());
					}
					if(showSta != null && !showSta.equals(pt.getShowStatus())) {
						pt.setShowStatus(showSta);
					}
					potTradeService.saveOrUpdate(pt);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
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
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_PT)) {
			try {
				PotTrade pt = potTradeService.getEntityById(id);
				if (pt == null) {
					status = 50001;
				} else {
					if (hot != null && !hot.equals(pt.getHot())) {
						pt.setHot(hot);
					}
					potTradeService.saveOrUpdate(pt);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}
	@PutMapping("/updatePotTrade")
	@ApiOperation(value = "更新储罐租卖基本信息", notes = "更新储罐租卖基本信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "id", value = "储罐租卖编号", required = true),
		@ApiImplicitParam(name = "mainImg", value = "储罐主图" , required = true),
		@ApiImplicitParam(name = "potPpId", value = "储罐品牌编号", required = true),
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
		@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", defaultValue = "0", required = true),
		@ApiImplicitParam(name = "tradeStatus", value = "租卖类型(0:可租可卖,1:租,2:卖)", defaultValue = "0", required = true)
	})
	public GenericResponse updatePotTrade(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id", request);
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
		Integer tradeStatus = CommonTools.getFinalInteger("tradeStatus", request);
		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.UP_PT)) {
			try {
				PotTrade pt = potTradeService.getEntityById(id);
				if (pt == null) {
					status = 50001;
				} else {
					
				    if(!mainImg.equals(pt.getMainImg())) {
				    	pt.setMainImg(CommonTools.dealUploadDetail(loginUserId, mainImg));
				    }
				    if(!potPpId.equals(pt.getTrucksPotPp().getId())) {
				    	TrucksPotPp trucksPotPp =potPpService.findById(potPpId);
						pt.setTrucksPotPp(trucksPotPp);
				    }
				    if(!zzjzTypeId.equals(pt.getPotZzjzType().getId())) {
				    	PotZzjzType potZzjzType = zzjzTypeService.findById(zzjzTypeId);
						pt.setPotZzjzType(potZzjzType );
				    }
				    if(!potVol.equals(pt.getPotVolume())) {
				    	pt.setPotVolume(potVol);
				    }
				    if(!sxInfo.equals(pt.getSxInfo())) {
				    	pt.setSxInfo(sxInfo);
				    }
				    if(!buyYear.equals(pt.getBuyYear())) {
				    	pt.setBuyYear(buyYear);
				    }
				    if(!province.equals(pt.getProvince())) {
				    	pt.setProvince(province);
				    }
				    if(!city.equals(pt.getCity())) {
				    	pt.setCity(city);
				    }
				    if(!leasePrice.equals(pt.getLeasePrice())) {
				    	pt.setLeasePrice(leasePrice);
				    }
				    if(!sellPrice.equals(pt.getSellPrice())) {
				    	pt.setSellPrice(sellPrice);
				    }
				    if(!remark.equals(pt.getRemark())) {
				    	pt.setRemark(remark);
				    }
				    if(!lxName.equals(pt.getLxName())) {
				    	pt.setLxName(lxName);
				    }
				    if(!lxTel.equals(pt.getLxTel())) {
				    	pt.setLxTel(lxTel);
				    }
				    if(!showStatus.equals(pt.getShowStatus())) {
				    	pt.setShowStatus(showStatus);
				    }
				    if(!tradeStatus.equals(pt.getTradeStatus())) {
				    	pt.setTradeStatus(tradeStatus);
				    }
					potTradeService.saveOrUpdate(pt);
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	@GetMapping("/queryPotTrade")
	@ApiOperation(value = "储罐租卖", notes = "获取储罐租卖分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ 
		  @ApiImplicitParam(name = "potPpId", value = "储罐品牌编号"),
			@ApiImplicitParam(name = "potVol", value = "储罐容积"),
			@ApiImplicitParam(name = "sxInfo", value = "有无手续(1：有，2：无)"),
			@ApiImplicitParam(name = "zzjzTypeId", value = "装载介质类型编号"),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryPotTrade(String potPpId, Integer  potVol,String sxInfo,String zzjzTypeId,  Integer checkSta, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<PotTrade> pts = null;
		try {
			potPpId = CommonTools.getFinalStr(potPpId);
			sxInfo = CommonTools.getFinalStr(sxInfo);
			zzjzTypeId = CommonTools.getFinalStr(zzjzTypeId);
			if (pageNo == null) {
				pageNo = 1;
			}
			if (pageSize == null) {
				pageSize = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			if(potVol == null) {
				potVol = -1;
			}
			pts = potTradeService.getPotTradeByOption(potPpId, potVol, sxInfo, zzjzTypeId, checkSta, pageNo-1, pageSize);
			if (pts.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, pts.getTotalElements(), status, pts.getContent());
	}

}
