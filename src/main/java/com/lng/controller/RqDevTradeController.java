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
import com.lng.pojo.RqDevTrade;
import com.lng.pojo.RqDevType;
import com.lng.pojo.RqDevType1;
import com.lng.service.CompanyService;
import com.lng.service.RqDevTradeService;
import com.lng.service.RqDevType1Service;
import com.lng.service.RqDevTypeService;
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
@Api(tags = "燃气设备买卖相关接口")
@RequestMapping("/rqDevTrade")
public class RqDevTradeController {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private RqDevType1Service zlService;
	@Autowired
	private RqDevTypeService lmService;
	@Autowired
	private RqDevTradeService rdtService;

	@PostMapping("/addRqDevTrade")
	@ApiOperation(value = "添加燃气设备买卖", notes = "添加燃气设备买卖")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司主键", required = true),
			@ApiImplicitParam(name = "mainImg", value = "燃气设备主图", required = true, defaultValue = "燃气设备图.img"),
			@ApiImplicitParam(name = "devName", value = "设备名称", required = true, defaultValue = "马哈"),
			@ApiImplicitParam(name = "devNo", value = "设备型号", required = true, defaultValue = "NoAlo909"),
			@ApiImplicitParam(name = "devPp", value = "生产厂家", required = true, defaultValue = "其他牌"),
			@ApiImplicitParam(name = "devPrice", value = "官方报价", required = true, defaultValue = "2345.00"),
			@ApiImplicitParam(name = "lmId", value = "设备类目主键", required = true),
			@ApiImplicitParam(name = "zlId", value = "设备种类主键", required = true),
			@ApiImplicitParam(name = "description", value = "描述", defaultValue = "这是一个描述"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "addUserId", value = "上传人员", required = true),
			@ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）", required = true) })
	public GenericResponse addRqDevTrade(HttpServletRequest request) {
		String compId = CommonTools.getFinalStr("compId", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String devName = CommonTools.getFinalStr("devName", request);
		String devNo = CommonTools.getFinalStr("devNo", request);
		String devPp = CommonTools.getFinalStr("devPp", request);
		Double devPrice = CommonTools.getFinalDouble("devPrice", request);
		String lmId = CommonTools.getFinalStr("lmId", request);
		String zlId = CommonTools.getFinalStr("zlId", request);
		String description = CommonTools.getFinalStr("description", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		String addUserId = CommonTools.getFinalStr("addUserId", request);
		Integer userType = CommonTools.getFinalInteger("userType", request);

		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;
		String rdtId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.ADD_RDT)) {
			try {
				RqDevTrade rdt = new RqDevTrade();
				Company company = companyService.getEntityById(compId);
				rdt.setCompany(company);
				if (!mainImg.equals("")) {
					rdt.setMainImg(CommonTools.dealUploadDetail(loginUserId, "", mainImg));
				}
				rdt.setDevName(devName);
				rdt.setDevNo(devNo);
				rdt.setDevPp(devPp);
				rdt.setDevPrice(devPrice);
				RqDevType rqDevType = lmService.findById(lmId);
				rdt.setRqDevType(rqDevType);
				RqDevType1 rqDevType1 = zlService.findById(zlId);
				rdt.setRqDevType1(rqDevType1);
				rdt.setDescription(description);
				rdt.setLxName(lxName);
				rdt.setLxTel(lxTel);
				rdt.setShowStatus(showStatus);
				rdt.setAddUserId(addUserId);
				rdt.setUserType(userType);
				rdt.setAddTime(CurrentTime.getCurrentTime());
				if (userType.equals(1)) {
					rdt.setCheckStatus(1);
					rdt.setCheckTime(CurrentTime.getCurrentTime());
				} else {
					rdt.setCheckStatus(0);
					rdt.setCheckTime("");
				}
				rdt.setHot(0);
				rdtId = rdtService.saveOrUpdate(rdt);

			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, rdtId);
	}

	@PutMapping("/updateRqDevTradeByStatus")
	@ApiOperation(value = "更新燃气设备买卖审核状态", notes = "更新燃气设备买卖状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气设备买卖主键", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）") })
	public GenericResponse updateRqDevTradeByStatus(HttpServletRequest request, String id, Integer checkSta,
			Integer showSta) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.UP_RDT)) {
			try {
				RqDevTrade rdt = rdtService.getEntityById(id);
				if (rdt == null) {
					status = 50001;
				} else {
					if (checkSta != null && !checkSta.equals(rdt.getCheckStatus())) {
						rdt.setCheckStatus(checkSta);
						rdt.setCheckTime(CurrentTime.getCurrentTime());
					}
					if (showSta != null && !showSta.equals(rdt.getShowStatus())) {
						rdt.setShowStatus(showSta);
					}
					rdtService.saveOrUpdate(rdt);
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

	@PutMapping("/updateRqDevTradeByHot")
	@ApiOperation(value = "更新储罐租卖热度", notes = "更新储罐租卖热度")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气设备买卖主键", required = true),
			@ApiImplicitParam(name = "hot", value = "热度（默认为0）") })
	public GenericResponse updateRqDevTradeByHot(HttpServletRequest request, String id, Integer hot) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.UP_RDT)) {
			try {
				RqDevTrade rdt = rdtService.getEntityById(id);
				if (rdt == null) {
					status = 50001;
				} else {
					if (hot != null && !hot.equals(rdt.getHot())) {
						rdt.setHot(hot);
					}
					rdtService.saveOrUpdate(rdt);
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

	@PutMapping("/updateRqDevTrade")
	@ApiOperation(value = "更新燃气设备买卖", notes = "更新燃气设备买卖信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({
		    @ApiImplicitParam(name = "id", value = "燃气设备买卖主键", required = true),
			@ApiImplicitParam(name = "mainImg", value = "燃气设备主图", required = true, defaultValue = "燃气设备图.img"),
			@ApiImplicitParam(name = "devName", value = "设备名称", required = true, defaultValue = "马哈"),
			@ApiImplicitParam(name = "devNo", value = "设备型号", required = true, defaultValue = "NoAlo909"),
			@ApiImplicitParam(name = "devPp", value = "生产厂家", required = true, defaultValue = "其他牌"),
			@ApiImplicitParam(name = "devPrice", value = "官方报价", required = true, defaultValue = "2345.00"),
			@ApiImplicitParam(name = "lmId", value = "设备类目主键", required = true),
			@ApiImplicitParam(name = "zlId", value = "设备种类主键", required = true),
			@ApiImplicitParam(name = "description", value = "描述", defaultValue = "这是一个描述"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"), })
	public GenericResponse updateRqDevTrade(HttpServletRequest request) {
		
		String id = CommonTools.getFinalStr("id", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String devName = CommonTools.getFinalStr("devName", request);
		String devNo = CommonTools.getFinalStr("devNo", request);
		String devPp = CommonTools.getFinalStr("devPp", request);
		Double devPrice = CommonTools.getFinalDouble("devPrice", request);
		String lmId = CommonTools.getFinalStr("lmId", request);
		String zlId = CommonTools.getFinalStr("zlId", request);
		String description = CommonTools.getFinalStr("description", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);

		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;
		if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),Constants.UP_RDT)) {
			try {
				RqDevTrade rdt = rdtService.getEntityById(id);
				if (!mainImg.equals(rdt.getMainImg())) {
					rdt.setMainImg(CommonTools.dealUploadDetail(loginUserId, rdt.getMainImg(), mainImg));
				}
				if (!devName.equals(rdt.getDevName())) {
					rdt.setDevName(devName);
				}
				if (!devNo.equals(rdt.getDevNo())) {
					rdt.setDevNo(devNo);
				}
				if (!devPp.equals(rdt.getDevPp())) {
					rdt.setDevPp(devPp);
				}
				if (!devPrice.equals(rdt.getDevPrice())) {
					rdt.setDevPrice(devPrice);
				}
				if (!lmId.equals(rdt.getRqDevType().getId())) {
					RqDevType rqDevType = lmService.findById(lmId);
					rdt.setRqDevType(rqDevType);
				}
				if (!zlId.equals(rdt.getRqDevType1().getId())) {
					RqDevType1 rqDevType1 = zlService.findById(zlId);
					rdt.setRqDevType1(rqDevType1);
				}
				if (!description.equals(rdt.getDescription())) {
					rdt.setDescription(description);
				}
				if (!lxName.equals(rdt.getLxName())) {
					rdt.setLxName(lxName);
				}
				if (!lxTel.equals(rdt.getLxTel())) {
					rdt.setLxTel(lxTel);
				}
				if (!showStatus.equals(rdt.getShowStatus())) {
					rdt.setShowStatus(showStatus);
				}
				rdtService.saveOrUpdate(rdt);

			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/queryRqDevTrade")
	@ApiOperation(value = "燃气设备买卖", notes = "获取燃气设备买卖分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司主键"),
			@ApiImplicitParam(name = "lmId", value = "设备类目主键"), @ApiImplicitParam(name = "zlId", value = "设备种类主键"),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "addUserId", value = "上传人员"),
			@ApiImplicitParam(name = "pageNo", value = "第几页"), @ApiImplicitParam(name = "pageSize", value = "每页多少条") })
	public PageResponse queryRqDevTrade(String compId, String lmId, String zlId, Integer checkSta, Integer showSta,
			String addUserId, Integer pageNo, Integer pageSize) {
		Integer status = 200;
		Page<RqDevTrade> rdt = null;
		try {
			compId = CommonTools.getFinalStr(compId);
			lmId = CommonTools.getFinalStr(lmId);
			zlId = CommonTools.getFinalStr(zlId);
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
			if (showSta == null) {
				showSta = -1;
			}
			rdt = rdtService.getRqDevTradeList(compId, lmId, zlId, checkSta, showSta, addUserId, pageNo, pageSize);
			if (rdt.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, rdt.getTotalElements(), status, rdt.getContent());
	}

}
