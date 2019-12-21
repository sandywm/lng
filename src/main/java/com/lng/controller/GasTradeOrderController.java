package com.lng.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.Company;
import com.lng.pojo.GasTrade;
import com.lng.pojo.GasTradeOrder;
import com.lng.pojo.GasTradeOrderLog;
import com.lng.pojo.User;
import com.lng.service.CompanyService;
import com.lng.service.GasTradeOrderLogService;
import com.lng.service.GasTradeOrderService;
import com.lng.service.GasTradeService;
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
@Api(tags = "燃气交易订单相关接口")
@RequestMapping("/gtOrder")
public class GasTradeOrderController {

	@Autowired
	private GasTradeOrderService gtoSeriver;
	@Autowired
	private GasTradeService gtService;
	@Autowired
	private CompanyService cpyService;
	@Autowired
	private UserService uService;
	@Autowired
	private GasTradeOrderLogService gtolService;

	@PostMapping("/addGasTraderOrder")
	@ApiOperation(value = "添加燃气交易订单", notes = "添加燃气交易订单信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gtId", value = "燃气交易编号", required = true),
			@ApiImplicitParam(name = "userId", value = "买气人编号", required = true),
			@ApiImplicitParam(name = "cpyId", value = "买气人所在公司", required = true),
			@ApiImplicitParam(name = "lxMobile", value = "购买人电话", required = true),
			@ApiImplicitParam(name = "price", value = "出价", required = true),
			@ApiImplicitParam(name = "remark", value = "备注"),
			@ApiImplicitParam(name = "lxrProv", value = "购买人省份", required = true),
			@ApiImplicitParam(name = "lxrCity", value = "城市", required = true),
			@ApiImplicitParam(name = "lxrAddress", value = "详细地址", required = true),
			@ApiImplicitParam(name = "lxrGpsInfo", value = "地理位置GPS信息"),
			@ApiImplicitParam(name = "distance", value = "距离位置(气源距离目的地距离)"), })
	public GenericResponse addGasTraderOrder(HttpServletRequest request) {
		String gtId = CommonTools.getFinalStr("gtId", request);
		String userId = CommonTools.getFinalStr("userId", request);
		String cpyId = CommonTools.getFinalStr("cpyId", request);
		String lxMobile = CommonTools.getFinalStr("lxMobile", request);
		Double price = CommonTools.getFinalDouble("price", request);
		String remark = CommonTools.getFinalStr("remark", request);
		String lxrProv = CommonTools.getFinalStr("lxrProv", request);
		String lxrCity = CommonTools.getFinalStr("lxrCity", request);
		String lxrAddress = CommonTools.getFinalStr("lxrAddress", request);
		String lxrGpsInfo = CommonTools.getFinalStr("lxrGpsInfo", request);
		Integer distance = CommonTools.getFinalInteger("distance", request);
		Integer status = 200;
		String gtoId = "";
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		try {
			if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.ADD_GTO)) {

			} else if (cilentInfo.equals("wxApp")) {

			} else {
				status = 70001;
			}

			if (status.equals(200)) {
				GasTradeOrder gto = new GasTradeOrder();
				GasTrade gasTrade = gtService.getEntityById(gtId);
				gto.setGasTrade(gasTrade);
				User user = uService.getEntityById(userId);
				gto.setUser(user);
				Company company = cpyService.getEntityById(cpyId);
				gto.setCompany(company);
				gto.setLxMobile(lxMobile);
				gto.setPrice(price);
				gto.setRemark(remark);
				gto.setLxrProv(lxrProv);
				gto.setLxrCity(lxrCity);
				gto.setLxrAddress(lxrAddress);
				gto.setLxrGpsInfo(lxrGpsInfo);
				gto.setDistance(distance);
				gto.setAddTime(CurrentTime.getCurrentTime());
				gto.setOrderStatus(0);
				gtoId = gtoSeriver.addOrUpdate(gto);
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gtoId);
	}

	@PostMapping("/addGasTraderOrderLog")
	@ApiOperation(value = "添加燃气交易订单日志", notes = "添加燃气交易订单日志信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gtoId", value = "燃气交易订单编号", required = true),
			@ApiImplicitParam(name = "orderSta", value = "订单状态", required = true),
			@ApiImplicitParam(name = "oImgDetail", value = "详情图片"),
			@ApiImplicitParam(name = "oDetailTxt", value = "订单详情文字", required = true) })
	public GenericResponse addGasTraderOrderLog(HttpServletRequest request) {
		String gtoId = CommonTools.getFinalStr("gtoId", request);
		String oImgDetail = CommonTools.getFinalStr("oImgDetail", request);
		String oDetailTxt = CommonTools.getFinalStr("oDetailTxt", request);
		Integer orderSta = CommonTools.getFinalInteger("orderSta", request);
		Integer status = 200;
		String gtolId = "";
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		try {
			if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.ADD_GTO)) {

			} else if (cilentInfo.equals("wxApp")) {

			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				GasTradeOrderLog gtoLog = new GasTradeOrderLog();
				GasTradeOrder gasTradeOrder = gtoSeriver.getEntityById(gtoId);
				gtoLog.setGasTradeOrder(gasTradeOrder);
				gtoLog.setOrderStatus(orderSta);
				gtoLog.setOrderImgDetail(oImgDetail);
				gtoLog.setOrderDetailTxt(oDetailTxt);
				gtoLog.setAddTime(CurrentTime.getCurrentTime());
				gtolId = gtolService.addOrUpdate(gtoLog);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gtolId);
	}

	@PutMapping("/updateGtOrderByStatus")
	@ApiOperation(value = "更新燃气交易订单状态", notes = "更新燃气交易订单状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气交易订单编号", required = true),
			@ApiImplicitParam(name = "orderSta", value = "订单状态") })
	public GenericResponse updatePotTradeByStatus(HttpServletRequest request, String id, Integer orderSta) {
		id = CommonTools.getFinalStr(id);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;

		try {
			if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.UP_GTO)) {

			} else if (cilentInfo.equals("wxApp")) {

			} else {
				status = 70001;
			}

			if (status.equals(200)) {
				GasTradeOrder gto = gtoSeriver.getEntityById(id);
				if (gto == null) {
					status = 50001;
				} else {
					if (orderSta != null && !orderSta.equals(gto.getOrderStatus())) {
						gto.setOrderStatus(orderSta);
					}
					gtoSeriver.addOrUpdate(gto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateGtOrderByPj")
	@ApiOperation(value = "更新燃气交易订单评价", notes = "更新燃气交易订单评价")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气交易订单编号", required = true),
			@ApiImplicitParam(name = "pjNum", value = "1-3星（好，中，差评，未评价时为0）"),
			@ApiImplicitParam(name = "pjDetail", value = "评价内容"), })
	public GenericResponse updateGtOrderByPj(HttpServletRequest request, String id, Integer pjNum, String pjDetail) {
		id = CommonTools.getFinalStr(id);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;

		try {
			if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.UP_GTO)) {

			} else if (cilentInfo.equals("wxApp")) {

			} else {
				status = 70001;
			}

			if (status.equals(200)) {
				GasTradeOrder gto = gtoSeriver.getEntityById(id);
				if (gto == null) {
					status = 50001;
				} else {
					if (pjNum != null && !pjNum.equals(gto.getOrderPjNumber())) {
						gto.setOrderPjNumber(pjNum);
					}
					if (!pjDetail.isEmpty() && !pjDetail.equals(gto.getOrderPjDetail())) {
						gto.setOrderPjDetail(pjDetail);
					}
					gtoSeriver.addOrUpdate(gto);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/queryGtOrder")
	@ApiOperation(value = "获取燃气交易订单分页信息", notes = "获取燃气交易订单分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号"),
			@ApiImplicitParam(name = "cpyId", value = "公司编号"), @ApiImplicitParam(name = "addtime", value = "添加时间"),
			@ApiImplicitParam(name = "orderSta", value = "订单状态"), @ApiImplicitParam(name = "page", value = "第几页"),
			@ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse queryGtOrder(String userId, String cpyId, String addtime, Integer orderSta, Integer page,
			Integer limit) {
		Integer status = 200;
		Page<GasTradeOrder> gto = null;
		try {
			userId = CommonTools.getFinalStr(userId);
			cpyId = CommonTools.getFinalStr(cpyId);
			addtime = CommonTools.getFinalStr(addtime);
			if (page == null) {
				page = 1;
			}
			if (limit == null) {
				limit = 10;
			}
			if (orderSta == null) {
				orderSta = -1;
			}

			gto = gtoSeriver.listPageInfoByOpt(userId, cpyId, addtime, orderSta, page - 1, limit);
			if (gto.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, gto.getTotalElements(), status, gto.getContent());
	}

	@GetMapping("/queryGtOrderByGtId")
	@ApiOperation(value = "根据燃气交易订单编号获取燃气交易订单", notes = "根据燃气交易订单编号获取燃气交易订单信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gtId", value = "燃气交易编号"), })
	public GenericResponse queryGtOrderByGtId(String gtId) {
		Integer status = 200;
		List<GasTradeOrder> gtoList = null;
		try {
			gtoList = gtoSeriver.getInfoBygtId(gtId);
			if (gtoList.isEmpty()) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gtoList);
	}

	@GetMapping("/queryGtoLogByGtoId")
	@ApiOperation(value = "获取燃气交易订单日志信息", notes = "获取燃气交易订日志信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gtoId", value = "燃气交易订单编号"), })
	public GenericResponse queryGtoLogByGtoId(String gtoId) {
		Integer status = 200;
		List<GasTradeOrderLog> gtolList = null;
		try {
			gtolList = gtolService.getGtLogList(gtoId);
			if (gtolList.isEmpty()) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gtolList);
	}
}
