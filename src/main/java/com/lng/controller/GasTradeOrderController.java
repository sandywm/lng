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
import com.lng.pojo.GasFactory;
import com.lng.pojo.GasTrade;
import com.lng.pojo.GasTradeImg;
import com.lng.pojo.GasTradeOrder;
import com.lng.pojo.GasTradeOrderLog;
import com.lng.pojo.GasType;
import com.lng.pojo.MessageCenter;
import com.lng.pojo.User;
import com.lng.service.CompanyService;
import com.lng.service.GasTradeOrderLogService;
import com.lng.service.GasTradeOrderService;
import com.lng.service.GasTradeService;
import com.lng.service.MessageCenterService;
import com.lng.service.UserService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.tools.EmojiDealUtil;
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
	@Autowired
	private MessageCenterService mcs;
	
	@PostMapping("/addGasTraderOrder")
	@ApiOperation(value = "添加燃气交易订单", notes = "添加燃气交易订单信息并增加订单日志")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 30003, message = "该交易审核未通过或已下架，不能进行操作"),
			@ApiResponse(code = 30004, message = "该产品正在交易中，不能进行下单"),
			@ApiResponse(code = 30005, message = "您已下过单，不能重复下单"),
			@ApiResponse(code = 30007, message = "不能购买自己的产品")
	})
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
		try {
			GasTrade gt = gtService.getEntityById(gtId);
			if(gt.getCheckStatus() == 1 && gt.getShowStatus() == 0){//审核通过且上架的交易才能增加订单
				if(!gt.getTradeOrderId().equals("")) {//存在确认订单
					status = 30003;
				}else {
					List<GasTradeOrder> gtoList = gtoSeriver.listComInfoByOpt(userId, gtId);
					if(gtoList.size() > 0) {//当前用户没下过订单
						status = 30005;
					}else {
						if(!gt.getAddUserId().equals(userId)) {
							GasTradeOrder gto = new GasTradeOrder();
							gto.setOrderNo(CurrentTime.getRadomTime().substring(2));
							GasTrade gasTrade = gtService.getEntityById(gtId);
							gto.setGasTrade(gasTrade);
							User user = uService.getEntityById(userId);
							gto.setUser(user);
							Company company = cpyService.getEntityById(cpyId);
							gto.setCompany(company);
							gto.setLxMobile(lxMobile);
							gto.setPrice(price);
							gto.setRemark(EmojiDealUtil.changeEmojiToHtml(remark));
							gto.setLxrProv(lxrProv);
							gto.setLxrCity(lxrCity);
							gto.setLxrAddress(EmojiDealUtil.changeEmojiToHtml(lxrAddress));
							gto.setLxrGpsInfo(lxrGpsInfo);
							gto.setDistance(distance);
							gto.setAddTime(CurrentTime.getCurrentTime());
							gto.setOrderStatus(0);
							gto.setOrderPjNumber(-1);
							gto.setOrderPjDetail("");
							gtoId = gtoSeriver.addOrUpdate(gto);
							if(!gtoId.equals("")) {
								//增加日志
								GasTradeOrderLog gtoLog = new GasTradeOrderLog();
								GasTradeOrder gasTradeOrder = gtoSeriver.getEntityById(gtoId);
								gtoLog.setGasTradeOrder(gasTradeOrder);
								gtoLog.setOrderStatus(0);
								gtoLog.setOrderImgDetail("");
								gtoLog.setOrderDetailTxt("");
								gtoLog.setAddTime(CurrentTime.getCurrentTime());
								gtolService.addOrUpdate(gtoLog);
								MessageCenter mc = new MessageCenter("",user.getRealName()+"想购买您发布的"+gt.getGasFactory().getName()+"燃气", user.getRealName()+"想购买您发布的"+gt.getGasFactory().getName()+"燃气", 0, CurrentTime.getCurrentTime(), 2,
										gtId, "gasTrade", "", gt.getAddUserId(), 0);
								mcs.saveOrUpdate(mc);
							}
						}else {
							status = 30007;
						}
					}
				}
			}else {
				status = 30003;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gtoId);
	}

	@PostMapping("/dealGasTraderOrderLog")
	@ApiOperation(value = "燃气交易中各种业务逻辑处理", notes = "订单中的动作处理")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
		@ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 30001, message = "某业务出现问题"),
		@ApiResponse(code = 30002, message = "该交易已存在确认订单，不能再进行确认操作"),
		@ApiResponse(code = 30003, message = "该交易审核未通过或已下架，不能进行操作"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "gtoId", value = "燃气交易订单编号", required = true),
			@ApiImplicitParam(name = "oImgDetail", value = "详情图片(买家上传缴费凭证或者上传磅单时传递或者上传余款凭证)"),
			@ApiImplicitParam(name = "pjScore", value = "订单完成时评价分数(评价时传递)1-3星（好，中，差评，未评价时为0）"),
			@ApiImplicitParam(name = "pjContent", value = "评价内容(评价时传递)"),
			@ApiImplicitParam(name = "oTxtDetail", value = "备注(买家上传缴费凭证或者上传磅单时传递或者上传余款凭证)"),
			@ApiImplicitParam(name = "orderSta", value = "订单状态（-2-7）"),
			@ApiImplicitParam(name = "userId", value = "用户编号")
	})
	public GenericResponse dealGasTraderOrderLog(HttpServletRequest request) {
		String gtoId = CommonTools.getFinalStr("gtoId", request);
		String oImgDetail = CommonTools.getFinalStr("oImgDetail", request);
		String oTxtDetail = CommonTools.getFinalStr("oTxtDetail", request);
		String pjContent = CommonTools.getFinalStr("pjContent", request);
		Integer pjScore = CommonTools.getFinalInteger("pjScore", request);
		String userId = CommonTools.getFinalStr("userId", request);
		Integer orderSta = CommonTools.getFinalInteger("orderSta", request);
		Integer status = 200;
		String gtolId = "";
		String currentTime = CurrentTime.getCurrentTime();
		try {
			if(userId.equals("") && gtoId.equals("")) {
				status = 10002;
			}else {
				//获取订单
				GasTradeOrder gasTradeOrder = gtoSeriver.getEntityById(gtoId);
				if(gasTradeOrder != null) {
					//获取燃气贸易
					GasTrade gt = gasTradeOrder.getGasTrade();
					if(gt != null) {
						String gtoId_qr = gt.getTradeOrderId();//确认订单编号
						if(gt.getCheckStatus() == 1 && gt.getShowStatus() == 0) {//审核通过且上架的才能进行操作
							String pubUserId = gasTradeOrder.getGasTrade().getAddUserId();//商家
							String buyUserId = gasTradeOrder.getUser().getId();//买家
							if(orderSta.equals(-2)) {//用户取消订单时
								if(userId.equals(buyUserId)) {//该阶段为用户操作环节
									//只有订单状态为0或者1时用户才能取消订单
									if(gasTradeOrder.getOrderStatus() == 0 || gasTradeOrder.getOrderStatus() == 1) {
										gasTradeOrder.setOrderStatus(orderSta);
										gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
										//修改其他订单为取消状态
										gtoSeriver.addOrUpdate(gasTradeOrder);
										//增加订单日志
										gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, "","", currentTime));
										//当是商家已确认订单、用户主动取消订单时，需要重置确认订单为空
										if(gtoId_qr.equals(gtoId)) {
											gt.setTradeOrderId("");
											gtService.saveOrUpdate(gt);
//											//商家确认订单后取消
//											//将之前自动拒绝或者手动拒绝的商户订单重置为0
//											List<GasTradeOrder> gtoList = gtoSeriver.listGtInfoByOpt1(gt.getId(), "", "", "", "");
//											for(GasTradeOrder gto : gtoList) {
//												if(!gto.getId().equals(gtoId)) {
//													if(gto.getOrderStatus() == -1) {
//														gto.setOrderStatus(-1);
//														gtoSeriver.addOrUpdate(gasTradeOrder);
//													}
//												}
//											}
										}
									}else {
										status = 30001;
									}
								}else {
									status = 30001;
								}
							}else if(orderSta.equals(-1)) {//商家主动拒绝订单时
								if(userId.equals(pubUserId)) {//该阶段为商户操作环节
									gasTradeOrder.setOrderStatus(orderSta);
									gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
									//修改其他订单为取消状态
									gtoSeriver.addOrUpdate(gasTradeOrder);
									//增加订单日志
									gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, "","", currentTime));
								}else {
									status = 30001;
								}
							}else if(orderSta.equals(1)) {//商家确认订单时
								if(userId.equals(pubUserId)) {//该阶段为商户操作环节
									//判断该燃气交易有无确认订单
									if(gtoId_qr.equals("")) {//无确认订单
										//并设置其他订单为取消状态
										List<GasTradeOrder> gtoList = gtoSeriver.getInfoBygtId(gt.getId());
										Integer gtoLen = gtoList.size();
										if(gtoLen > 0) {
											if(gasTradeOrder.getOrderStatus() == 0) {//确认订单时，用户的订单状态必须为0-下单状态
												gasTradeOrder.setOrderStatus(orderSta);
												gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
												gtoSeriver.addOrUpdate(gasTradeOrder);
												//设置燃气交易确认订单
												gt.setTradeOrderId(gtoId);
												gtService.saveOrUpdate(gt);
												//增加订单日志
												gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, "","", currentTime));
												if(gtoLen > 1) {//多个订单时
													for(GasTradeOrder gto : gtoList) {
														if(!gto.getId().equals(gasTradeOrder.getId())) {
															if(gto.getOrderStatus() == 0) {//当最近一次不是用户自行拒绝时
																gto.setOrderStatus(-1);
																//修改其他订单为取消状态
																gtoSeriver.addOrUpdate(gto);
																//增加订单日志
																gtolService.addOrUpdate(new GasTradeOrderLog(gto, -1, "","", currentTime));
															}
														}
													}
												}
											}else {
												status = 30001;
											}
										}else {
											status = 50001;
										}
									}else {
										//获取该用户在该贸易的所有订单，可能是上传了预付款，商家未到账，需要重新上传付款凭证
										List<GasTradeOrderLog> gtolList = gtolService.getGtLogList(gtoId);
										if(gtolList.size() > 0) {
											//获取最后一条订单日志
											GasTradeOrderLog gtol = gtolList.get(gtolList.size() - 1);
											if(gtol.getOrderStatus() == 2) {
												gasTradeOrder.setOrderStatus(orderSta);
												gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
												gtoSeriver.addOrUpdate(gasTradeOrder);
												//增加订单日志
												gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, 
														gtol.getOrderImgDetail(),"商户未收到首款或首款数目有出入", currentTime));
											}else {//已存在确认订单，不能进行操作
												status = 30002;
											}
										}else {
											status = 30001;
										}
									}
								}else {
									status = 30001;
								}
							}else if(orderSta.equals(2)) {//用户上传首款付款凭证
								//用户已付预付款，等待商家确认
								if(userId.equals(buyUserId)) {//该阶段为用户操作环节
									gasTradeOrder.setOrderStatus(orderSta);
									gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
									gtoSeriver.addOrUpdate(gasTradeOrder);
									//增加订单日志
									gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, 
											CommonTools.dealUploadDetail(userId, "", oImgDetail),oTxtDetail, currentTime));
								}else {
									status = 30001;
								}
							}else if(orderSta.equals(3)) {//商户点击确认首款到账时
								if(userId.equals(pubUserId)) {//该阶段为商户操作环节
									gasTradeOrder.setOrderStatus(orderSta);
									gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
									gtoSeriver.addOrUpdate(gasTradeOrder);
									//增加订单日志
									gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, 
											"","", currentTime));
								}else {
									status = 30001;
								}
							}else if(orderSta.equals(4)) {//用户上传磅单确认收货时
								//用户已确认收货，等待用户上传余款缴费凭证
								if(userId.equals(buyUserId)) {//该阶段为用户操作环节
									gasTradeOrder.setOrderStatus(orderSta);
									gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
									gtoSeriver.addOrUpdate(gasTradeOrder);
									//增加订单日志
									gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, 
											CommonTools.dealUploadDetail(userId, "", oImgDetail),oTxtDetail, currentTime));
								}else if(userId.equals(pubUserId)){//商户拒绝余款凭证时
									List<GasTradeOrderLog> gtolList = gtolService.getGtLogList(gtoId_qr);
									if(gtolList.size() > 0) {
										//获取最后一条订单日志
										GasTradeOrderLog gtol = gtolList.get(gtolList.size() - 1);
										if(gtol.getOrderStatus() == 5) {
											gasTradeOrder.setOrderStatus(orderSta);
											gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
											gtoSeriver.addOrUpdate(gasTradeOrder);
											//增加订单日志
											gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, 
													gtol.getOrderImgDetail(),"商户未收到余款或余款数目有出入", currentTime));
										}
									}
								}else {
									status = 30001;
								}
							}else if(orderSta.equals(5)) {//用户上传余款付款凭证时
								//用户已付余款，等待商家确认
								if(userId.equals(buyUserId)) {//该阶段为用户操作环节
									gasTradeOrder.setOrderStatus(orderSta);
									gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
									gtoSeriver.addOrUpdate(gasTradeOrder);
									//增加订单日志
									gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, 
											CommonTools.dealUploadDetail(userId, "", oImgDetail),oTxtDetail, currentTime));
								}else {
									status = 30001;
								}
							}else if(orderSta.equals(6)) {//商户确认余款到账
								if(userId.equals(pubUserId)) {//该阶段为商户操作环节
									gasTradeOrder.setOrderStatus(orderSta);
									gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
									gtoSeriver.addOrUpdate(gasTradeOrder);
									//增加订单日志
									gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, 
											"","", currentTime));
								}else {
									status = 30001;
								}
							}else if(orderSta.equals(7)) {//用户评价完成时
								if(userId.equals(buyUserId)) {//该阶段为用户操作环节
									if (pjScore > 0 && pjScore <= 5) {
										gasTradeOrder.setOrderStatus(orderSta);
										gasTradeOrder.setAddTime(CurrentTime.getCurrentTime());
										gasTradeOrder.setOrderPjNumber(pjScore);
										gasTradeOrder.setOrderPjDetail(EmojiDealUtil.changeEmojiToHtml(pjContent));
										gtoSeriver.addOrUpdate(gasTradeOrder);
										//增加订单日志
										gtolService.addOrUpdate(new GasTradeOrderLog(gasTradeOrder, orderSta, 
												"","", currentTime));
									}else {
										status = 10002;
									}
								}else {
									status = 30001;
								}
							}
						}else {
							status = 30003;
						}
					}else {
						status = 50001;
					}
				}else {
					status = 50001;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gtolId);
	}

	@GetMapping("/getPageGtOrder")
	@ApiOperation(value = "获取燃气交易订单分页信息--后台", notes = "获取燃气交易订单分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号"),
			@ApiImplicitParam(name = "cpyId", value = "公司编号"), 
			@ApiImplicitParam(name = "sDate", value = "开始时间"),
			@ApiImplicitParam(name = "eDate", value = "结束时间"),
			@ApiImplicitParam(name = "orderSta", value = "订单状态（-2到7）"), 
			@ApiImplicitParam(name = "page", value = "第几页"),
			@ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse getPageGtOrder(HttpServletRequest request) {
		Integer status = 200;
		Page<GasTradeOrder> gtoList = null;
		String userId = CommonTools.getFinalStr("userId",request);
		String cpyId = CommonTools.getFinalStr("cpyId",request);
		String sDate = CommonTools.getFinalStr("sDate",request);
		String eDate = CommonTools.getFinalStr("eDate",request);
		Integer orderSta = CommonTools.getFinalInteger("orderSta", request);
		Integer page = CommonTools.getFinalInteger("page", request);
		Integer limit = CommonTools.getFinalInteger("limit", request);
		long count = 0;
		List<Object> list = new ArrayList<Object>();
		try {
			if (page == 0) {
				page = 1;
			}
			if (limit == 0) {
				limit = 10;
			}
			if (orderSta == 0) {
				orderSta = -1;
			}
			gtoList = gtoSeriver.listPageInfoByOpt(userId, cpyId, sDate,eDate, orderSta, page - 1, limit);
			count = gtoList.getTotalElements();
			if (count == 0) {
				status = 50001;
			}else {
				for(GasTradeOrder gto : gtoList) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", gto.getId());
					GasTrade gt = gto.getGasTrade();
					GasFactory gf = gt.getGasFactory();
					GasType gasType = gt.getGasType();
					map.put("gasTradeId", gt.getId());
					String headImg = gt.getHeadImg();
					if(headImg.equals("")) {
						//系统默认图
						headImg = gt.getGasType().getYzImg();
					}
					map.put("headImg", headImg);
					map.put("orderNo", gto.getOrderNo());
					map.put("title", gf.getProvince()+gf.getName()+gasType.getName());
					map.put("psArea", gt.getPsArea());
					map.put("sellPrice", gt.getGasPrice());
					map.put("sellCpyName", gt.getCompany().getName());
					map.put("buyCpyName", gto.getCompany().getName());
					Integer oStatus = gto.getOrderStatus();
					String orderStatusChi = "";
					map.put("orderStatus", oStatus);
					if(oStatus.equals(-1)) {
						orderStatusChi = "已取消";//商户取消
					}else if(oStatus.equals(0)) {
						orderStatusChi = "待商家确认";//下单等待商家确认0
					}else if(oStatus.equals(1)) {
						orderStatusChi = "待付款";//付款凭证上传后等待商家确认后状态修改为1
					}else if(oStatus.equals(2)) {
						orderStatusChi = "待发货";//商家确认无误后等待商家发货状态修改为2，确认有误时状态修改为0，直到确认完成
					}else if(oStatus.equals(3)) {
						orderStatusChi = "待收货";//商家发货后状态修改为3
					}else if(oStatus.equals(4)) {
						orderStatusChi = "待付款";//买家点击收货时上传磅单，状态修改为4
					}else if(oStatus.equals(5)) {
						orderStatusChi = "待商家确认";//买家上传尾款凭证后，状态修改为5
					}else if(oStatus.equals(6)) {
						orderStatusChi = "待买家评价";//商家确认后状态修改为6，确认有误时状态修改为4
					}else if(oStatus.equals(7)) {
						orderStatusChi = "订单完成";//买家评价后状态修改为7，订单完成
					}
					map.put("orderStatusChi", orderStatusChi);
					map.put("addTime", gto.getAddTime());
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, count, status, list);
	}

	@GetMapping("/getGtOrderList")
	@ApiOperation(value = "获取燃气交易订单信息--前台", notes = "获取燃气交易订单分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户编号"), 
			@ApiImplicitParam(name = "sDate", value = "开始时间"),
			@ApiImplicitParam(name = "eDate", value = "结束时间"),
			@ApiImplicitParam(name = "orderSta", value = "订单状态区间--用逗号隔开（-2到7），比如-2,1"), 
			@ApiImplicitParam(name = "opt", value = "参数(0:用户,1:商户)"),
	})
	public GenericResponse getGtOrderList(HttpServletRequest request) {
		Integer status = 200;
		String userId = CommonTools.getFinalStr("userId", request);
		String sDate = CommonTools.getFinalStr("sDate",request);
		String eDate = CommonTools.getFinalStr("eDate",request);
		String orderSta = CommonTools.getFinalStr("orderSta", request);
		Integer opt = CommonTools.getFinalInteger("opt", request);
		List<Object> list = new ArrayList<Object>();
		try {
			String buyUserId = "";
			String pubUserId = "";
			if(opt.equals(0)) {//用户
				buyUserId = userId;
				List<GasTradeOrder> gtoList = gtoSeriver.listGtInfoByOpt1("",buyUserId, orderSta, sDate, eDate);
				if(gtoList.size() > 0) {
					for(GasTradeOrder gto : gtoList) {
						Map<String,Object> map = new HashMap<String,Object>();
						map.put("gtoId", gto.getId());
						GasTrade gt = gto.getGasTrade();
						GasFactory gf = gt.getGasFactory();
						GasType gasType = gt.getGasType();
						map.put("gtId", gt.getId());
						String headImg = gt.getHeadImg();
						if(headImg.equals("")) {
							headImg = gt.getGasType().getYzImg();
						}
						map.put("headImg", headImg);
						map.put("addDate", gt.getAddTime());
						map.put("title", gf.getProvince()+gf.getName()+gasType.getName());
						map.put("psArea", gt.getPsArea());
						map.put("gsTypeName", gasType.getName());
						map.put("yyd", gf.getProvince());
						map.put("sellPrice", gt.getGasPrice());
						map.put("volume", gt.getGasVolume());
						map.put("orderNo", gto.getOrderNo());
						Integer oStatus = gto.getOrderStatus();
						String orderStatusChi = "";
						map.put("orderStatus", oStatus);
						String tipsTxt = "";
						List<Object> list_pj = new ArrayList<Object>();
						if(oStatus.equals(-2)) {
							orderStatusChi = "已取消";//用户主动取消
							tipsTxt = "用户已取消订单";
						}else if(oStatus.equals(-1)) {
							orderStatusChi = "已拒绝";//商户取消
							tipsTxt = "商家已拒绝";
						}else if(oStatus.equals(0)) {
							orderStatusChi = "待商家确认";//下单等待商家确认0
							tipsTxt = "买家已下单，等待商家确认";
						}else if(oStatus.equals(1)) {
							orderStatusChi = "待付款";//商家确认后等待用户上传付款凭证状态修改为1
							tipsTxt = "商家已确认，等待用户付款并上传缴费凭证";
							List<GasTradeOrderLog> gtolList = gtolService.getGtLogList(gto.getId(), 1);
							if(gtolList.size() > 0) {
								if(gtolList.get(0).getOrderDetailTxt().equals("商户未收到首款或首款数目有出入")) {
									//表示是商户拒绝
									tipsTxt = "商户已拒绝，等待 用户重新上传首款缴费凭证";
								}
							}
						}else if(oStatus.equals(2)) {
							orderStatusChi = "待发货";//商家确认无误后等待商家发货状态修改为2，确认有误时状态修改1，直到确认完成
							tipsTxt = "用户已付预付款，等待商家确认";
						}else if(oStatus.equals(3)) {
							orderStatusChi = "待收货";//商家发货后状态修改为3
							tipsTxt = "商家已发货，等待用户确认收货";
						}else if(oStatus.equals(4)) {
							orderStatusChi = "待付款";//买家点击收货时上传磅单，状态修改为4
							tipsTxt = "用户已确认收货，等待用户上传余款缴费凭证";
							List<GasTradeOrderLog> gtolList = gtolService.getGtLogList(gto.getId(), 4);
							if(gtolList.size() > 0) {
								if(gtolList.get(0).getOrderDetailTxt().equals("商户未收到余款或余款数目有出入")) {
									//表示是商户拒绝
									tipsTxt = "商户已拒绝，等待用户重新上传余款缴费凭证";
								}
							}
						}else if(oStatus.equals(5)) {
							orderStatusChi = "待商家确认";//买家上传尾款凭证后，状态修改为5
							tipsTxt = "用户已付余款，等待商家确认";
						}else if(oStatus.equals(6)) {
							orderStatusChi = "待买家评价";//商家确认后状态修改为6，确认有误时状态修改为4
							tipsTxt = "商家已确认收款，等待用户评价";
						}else if(oStatus.equals(7)) {
							orderStatusChi = "订单完成";//买家评价后状态修改为7，订单完成
							tipsTxt = "订单完成";
							//获取用户评价
							//获取评价内容
							String pjTime  = gto.getAddTime();
							if(!pjTime.equals("")) {
								Map<String,Object> map_pj = new HashMap<String,Object>();
								map_pj.put("pjUserHead", gto.getUser().getUserPortrait());
								map_pj.put("pjUserName", gto.getUser().getRealName());
								map_pj.put("pjScore", gto.getOrderPjNumber());
								map_pj.put("pjDetail", gto.getOrderPjDetail());
								map_pj.put("pjDate", gto.getAddTime());
								list_pj.add(map_pj);
							}
						}
						map.put("pjList", list_pj);
						List<GasTradeOrderLog> gtolList = gtolService.getGtLogList(gto.getId(),2);//用户上传的首款
						if(gtolList.size() > 0) {
							GasTradeOrderLog gtol = gtolList.get(0);
							map.put("feeImg1", gtol.getOrderImgDetail());
							map.put("feeRemark", gtol.getOrderDetailTxt());
						}else {
							map.put("feeImg1", "");
							map.put("feeRemark", "");
						}
						List<GasTradeOrderLog> gtolList1 = gtolService.getGtLogList(gto.getId(),4);//用户上传的磅单
						if(gtolList1.size() > 0) {
							GasTradeOrderLog gtol = gtolList1.get(0);
							map.put("bdImg", gtol.getOrderImgDetail());
							map.put("bdRemark", gtol.getOrderDetailTxt());
						}else {
							map.put("bdImg", "");
							map.put("bdRemark", "");
						}
						List<GasTradeOrderLog> gtolList2 = gtolService.getGtLogList(gto.getId(),5);//用户上传的尾款
						if(gtolList2.size() > 0) {
							GasTradeOrderLog gtol = gtolList2.get(0);
							map.put("feeImg2", gtol.getOrderImgDetail());
							map.put("feeRemark2", gtol.getOrderDetailTxt());
						}else {
							map.put("feeImg2", "");
							map.put("feeRemark2", "");
						}
						map.put("orderStatusChi", orderStatusChi);
						map.put("tipsTxt", tipsTxt);
						//当前用户出价信息
						Map<String,Object> map_d = new HashMap<String,Object>();
						List<Object> list_d = new ArrayList<Object>();
						User buyUser = gto.getUser();
						map_d.put("buyUserId", buyUser.getId());
						map_d.put("buyUserHead", buyUser.getUserPortrait());
						map_d.put("buyUserName", buyUser.getWxName());
						map_d.put("buyPrice", gto.getPrice());
						map_d.put("psAddress", gto.getLxrProv() + gto.getLxrCity() + EmojiDealUtil.changeStrToEmoji(gto.getLxrAddress()));
						list_d.add(map_d);
						map.put("buyUserList", list_d);
						list.add(map);
					}
				}else {
					status = 50001;
				}
			}else {//商户
				pubUserId = userId;
				List<GasTrade> gtList = gtService.listTradingInfoByOpt(sDate, eDate, pubUserId);
				if(gtList.size() > 0) {
					for(GasTrade gt : gtList) {
						Integer oStatus = -3;
						Map<String,Object> map = new HashMap<String,Object>();
						List<Object> list_d = new ArrayList<Object>();
						String gtoId_qr = gt.getTradeOrderId();//确认订单
						Integer orderNum = 0;
						GasTradeOrder gto = null;
						String unBuyInfo = "";
						if(gtoId_qr.equals("")) {//订单未确认
							List<GasTradeOrder> gtoList = gtoSeriver.getInfoBygtId(gt.getId());
							orderNum = gtoList.size();
							oStatus = 0;
							if(orderNum > 0) {
								unBuyInfo = "deal";//表示已被拒绝或者被取消
								for(GasTradeOrder gto_tmp : gtoList) {
									if(gto_tmp.getOrderStatus() == 0) {
										unBuyInfo = "unDeal";//表示存在未确认的订单
										break;
									}
								}
							}
						}else {//订单已确认
							gto = gtoSeriver.getEntityById(gtoId_qr);
							if(gto != null) {
								orderNum = 1;
								//获取下单人员信息
								Map<String,Object> map_d = new HashMap<String,Object>();
								User buyUser = gto.getUser();
								map_d.put("gtoId", gto.getId());
								map_d.put("buyUserId", buyUser.getId());
								map_d.put("buyUserHead", buyUser.getUserPortrait());
								map_d.put("buyUserName", buyUser.getWxName());
								map_d.put("buyPrice", gto.getPrice());
								map_d.put("psAddress", gto.getLxrProv() + gto.getLxrCity() + EmojiDealUtil.changeStrToEmoji(gto.getLxrAddress()));
								oStatus = gto.getOrderStatus();
								map_d.put("orderStatus", gto.getOrderStatus());
								map_d.put("orderNo", gto.getOrderNo());
								list_d.add(map_d);
							}
						}
						if(orderNum > 0 && oStatus > -3) {
							String[] orderStaArr = orderSta.split(",");
							boolean showFlag = false;
							if(gtoId_qr.equals("") ) {//订单未确认,且存在已下单记录
								if(oStatus >= Integer.parseInt(orderStaArr[0]) && oStatus <= Integer.parseInt(orderStaArr[1])) {
									showFlag = true;
								}
							}else {//存在确认订单，需要判断订单状态和状态标签数字相同
								if(oStatus >= Integer.parseInt(orderStaArr[0]) && oStatus <= Integer.parseInt(orderStaArr[1])) {
									showFlag = true;
								}
							}
							if(showFlag) {
								if(gtoId_qr.equals("") ) {//订单未确认
									map.put("orderNum", orderNum);
								}else {
									map.put("buyUserList", list_d);
								}
								GasFactory gf = gt.getGasFactory();
								GasType gasType = gt.getGasType();
								map.put("gtId", gt.getId());
								String headImg = gt.getHeadImg();
								if(headImg.equals("")) {
									//系统默认图
									headImg = gt.getGasType().getYzImg();
								}
								map.put("headImg", headImg);
								map.put("addDate", gt.getAddTime());
								map.put("title", gf.getProvince()+gf.getName()+gasType.getName());
								map.put("psArea", gt.getPsArea());
								map.put("gsTypeName", gasType.getName());
								map.put("yyd", gf.getProvince());
								map.put("sellPrice", gt.getGasPrice());
								map.put("volume", gt.getGasVolume());
								String orderStatusChi = "";
								map.put("orderStatus", oStatus);
								String tipsTxt = "";
								List<Object> list_pj = new ArrayList<Object>();
								if(oStatus.equals(-2)) {
									orderStatusChi = "已取消";//用户主动取消
									tipsTxt = "用户已取消订单";
								}else if(oStatus.equals(-1)) {
									orderStatusChi = "已拒绝";//商户取消
									tipsTxt = "商家已拒绝";
								}else if(oStatus.equals(0)) {
									orderStatusChi = "待商家确认";//下单等待商家确认0
									tipsTxt = "买家已下单，等待商家确认";
									if(unBuyInfo.equals("deal")) {
										orderStatusChi = "订单已全被拒绝或者取消，等待新用户下单";//下单等待商家确认0
										tipsTxt = "订单已全被拒绝或者取消，等待新用户下单";
									}
								}else if(oStatus.equals(1)) {
									orderStatusChi = "待付款";//商家确认后等待用户上传付款凭证状态修改为1
									tipsTxt = "商家已确认，等待用户付款并上传缴费凭证";
									List<GasTradeOrderLog> gtolList = gtolService.getGtLogList(gto.getId(), 1);
									if(gtolList.size() > 0) {
										if(gtolList.get(0).getOrderDetailTxt().equals("商户未收到首款或首款数目有出入")) {
											//表示是商户拒绝
											tipsTxt = "商户已拒绝，等待用户重新上传首款缴费凭证";
										}
									}
								}else if(oStatus.equals(2)) {
									orderStatusChi = "待发货";//商家确认无误后等待商家发货状态修改为2，确认有误时状态修改1，直到确认完成
									tipsTxt = "用户已付预付款，等待商家确认";
								}else if(oStatus.equals(3)) {
									orderStatusChi = "待收货";//商家发货后状态修改为3
									tipsTxt = "商家已发货，等待用户确认收货";
								}else if(oStatus.equals(4)) {
									orderStatusChi = "待付款";//买家点击收货时上传磅单，状态修改为4
									tipsTxt = "用户已确认收货，等待用户上传余款缴费凭证";
									List<GasTradeOrderLog> gtolList = gtolService.getGtLogList(gto.getId(), 4);
									if(gtolList.size() > 0) {
										if(gtolList.get(0).getOrderDetailTxt().equals("商户未收到余款或余款数目有出入")) {
											//表示是商户拒绝
											tipsTxt = "商户已拒绝，等待用户重新上传余款缴费凭证";
										}
									}
								}else if(oStatus.equals(5)) {
									orderStatusChi = "待商家确认";//买家上传尾款凭证后，状态修改为5
									tipsTxt = "用户已付余款，等待商家确认";
								}else if(oStatus.equals(6)) {
									orderStatusChi = "待买家评价";//商家确认后状态修改为6，确认有误时状态修改为4
									tipsTxt = "商家已确认收款，等待用户评价";
								}else if(oStatus.equals(7)) {
									orderStatusChi = "订单完成";//买家评价后状态修改为7，订单完成
									tipsTxt = "订单完成";
									//获取评价内容
									String pjTime  = gto.getAddTime();
									if(!pjTime.equals("")) {
										Map<String,Object> map_pj = new HashMap<String,Object>();
										map_pj.put("pjUserHead", gto.getUser().getUserPortrait());
										map_pj.put("pjUserName", gto.getUser().getRealName());
										map_pj.put("pjScore", gto.getOrderPjNumber());
										map_pj.put("pjDetail", gto.getOrderPjDetail());
										map_pj.put("pjDate", gto.getAddTime());
										list_pj.add(map_pj);
									}
								}
								map.put("pjList", list_pj);
								if(!gtoId_qr.equals("")) {
									List<GasTradeOrderLog> gtolList_f = gtolService.getGtLogList(gtoId_qr,2);//用户上传的首款
									if(gtolList_f.size() > 0) {
										GasTradeOrderLog gtol = gtolList_f.get(0);
										map.put("feeImg1", gtol.getOrderImgDetail());
										map.put("feeRemark", gtol.getOrderDetailTxt());
									}else {
										map.put("feeImg1", "");
										map.put("feeRemark", "");
									}
									List<GasTradeOrderLog> gtolList1 = gtolService.getGtLogList(gto.getId(),4);//用户上传的磅单
									if(gtolList1.size() > 0) {
										GasTradeOrderLog gtol = gtolList1.get(0);
										map.put("bdImg", gtol.getOrderImgDetail());
										map.put("bdRemark", gtol.getOrderDetailTxt());
									}else {
										map.put("bdImg", "");
										map.put("bdRemark", "");
									}
									List<GasTradeOrderLog> gtolList2 = gtolService.getGtLogList(gto.getId(),5);//用户上传的尾款
									if(gtolList2.size() > 0) {
										GasTradeOrderLog gtol = gtolList2.get(0);
										map.put("feeImg2", gtol.getOrderImgDetail());
										map.put("feeRemark2", gtol.getOrderDetailTxt());
									}else {
										map.put("feeImg2", "");
										map.put("feeRemark2", "");
									}
								}
								map.put("orderStatusChi", orderStatusChi);
								map.put("tipsTxt", tipsTxt);
								list.add(map);
							}
						}
					}
					if(list.size() == 0) {
						status = 50001;
					}
				}else {
					status = 50001;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/getConfirmOrderList")
	@ApiOperation(value = "获取指定燃气交易编号需要确认的订单信息--前台", notes = "确认页面点击下单人数后出现的订单列表")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户编号"), 
			@ApiImplicitParam(name = "gtId", value = "燃气贸易编号")
	})
	public GenericResponse getConfirmOrderList(HttpServletRequest request) {
		Integer status = 200;
		String gtId = CommonTools.getFinalStr("gtId", request);
		String userId = CommonTools.getFinalStr("userId", request);
		List<Object> list = new ArrayList<Object>();
		GasTrade gt = gtService.getEntityById(gtId);
		if(gt != null) {
			if(gt.getAddUserId().equals(userId)) {
				//获取该燃气贸易的订单(商户拒绝,用户取消，未确认，已同意)
				List<GasTradeOrder> gtoList = gtoSeriver.listGtInfoByOpt(gtId, "", 1, "", "");
				if(gtoList.size() > 0) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("gtId", gt.getId());
					String headImg = gt.getHeadImg();
					if(headImg.equals("")) {
						//系统默认图
						headImg = gt.getGasType().getYzImg();
					}
					map.put("headImg", headImg);
					map.put("addDate", gt.getAddTime());
					GasFactory gf = gt.getGasFactory();
					GasType gasType = gt.getGasType();
					map.put("title", gf.getProvince()+gf.getName()+gasType.getName());
					map.put("psArea", gt.getPsArea());
					map.put("gsTypeName", gasType.getName());
					map.put("yyd", gf.getProvince());
					map.put("sellPrice", gt.getGasPrice());
					map.put("volume", gt.getGasVolume());
					List<Object> list_d = new ArrayList<Object>();
					for(GasTradeOrder gto : gtoList) {
						Map<String,Object> map_d = new HashMap<String,Object>();
						User buyUser = gto.getUser();
						map_d.put("gtoId", gto.getId());
						map_d.put("buyUserId", buyUser.getId());
						map_d.put("buyUserHead", buyUser.getUserPortrait());
						map_d.put("buyUserName", buyUser.getWxName());
						map_d.put("buyPrice", gto.getPrice());
						map_d.put("psAddress", gto.getLxrProv() + gto.getLxrCity() + EmojiDealUtil.changeStrToEmoji(gto.getLxrAddress()));
						map_d.put("orderStatus", gto.getOrderStatus());
						list_d.add(map_d);
					}
					map.put("buyUserList", list_d);
					list.add(map);
				}else {
					status = 50001;
				}
			}else {
				status = 50001;
			}
		}else {
			status = 50001;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/getSpecGtInfo")
	@ApiOperation(value = "获取指定燃气交易编号头部信息--前台", notes = "获取指定燃气交易编号头部信息订单列表")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "gtId", value = "燃气贸易编号")
	})
	public GenericResponse getSpecGtInfo(HttpServletRequest request) {
		Integer status = 200;
		String gtId = CommonTools.getFinalStr("gtId", request);
		List<Object> list = new ArrayList<Object>();
		GasTrade gt = gtService.getEntityById(gtId);
		if(gt != null) {
			String gtoId_qr = gt.getTradeOrderId();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("gtId", gt.getId());
			String headImg = gt.getHeadImg();
			if(headImg.equals("")) {
				//系统默认图
				headImg = gt.getGasType().getYzImg();
			}
			map.put("headImg", headImg);
			map.put("addDate", gt.getAddTime());
			GasFactory gf = gt.getGasFactory();
			GasType gasType = gt.getGasType();
			map.put("title", gf.getProvince()+gf.getName()+gasType.getName());
			map.put("cpyName", gt.getCompany().getName());
			map.put("psArea", gt.getPsArea());
			map.put("gsTypeName", gasType.getName());
			map.put("yyd", gf.getProvince());
			map.put("sellPrice", gt.getGasPrice());
			map.put("volume", gt.getGasVolume());
			map.put("gtoId", gtoId_qr);
			String orderNo = "";
			if(!gtoId_qr.equals("")) {
				GasTradeOrder gto = gtoSeriver.getEntityById(gtoId_qr);
				if(gto != null) {
					orderNo = gto.getOrderNo();
				}
			}
			map.put("orderNo", orderNo);
			list.add(map);
		}else {
			status = 50001;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/queryGtOrderByGtId")
	@ApiOperation(value = "根据燃气交易编号获取燃气交易订单详情", notes = "根据燃气交易编号获取燃气交易订单详情")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gtId", value = "燃气交易编号"), 
			@ApiImplicitParam(name = "gtId", value = "燃气交易编号")
	})
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

	@GetMapping("/getGtOrderDetailByGtId")
	@ApiOperation(value = "根据燃气交易订单编号获取燃气交易订单详情--前台用", notes = "根据燃气交易订单编号获取燃气交易订单详情--前台用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gtId", value = "燃气交易订单编号"),
		 @ApiImplicitParam(name = "userId", value = "用户编号")
	})
	public GenericResponse getGtOrderDetailByGtId(HttpServletRequest request) {
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		String gtId = CommonTools.getFinalStr("gtId", request);
		String userId = CommonTools.getFinalStr("userId", request);
		try {
			if(!gtId.equals("") && !userId.equals("")) {
				GasTrade gt = gtService.getEntityById(gtId);
				if(gt != null) {
					String gtoId_qr = gt.getTradeOrderId();//确认订单
					String gtoId = "";
					Map<String,Object> map = new HashMap<String,Object>();
					String userType = "";
					//贸易信息
					map.put("gtId", gt.getId());
					String headImg = gt.getHeadImg();
					if(headImg.equals("")) {
						//系统默认图
						headImg = gt.getGasType().getYzImg();
					}
					map.put("headImg", headImg);
					map.put("addDate", gt.getAddTime());
					map.put("psArea", gt.getPsArea());
					map.put("gsTypeName", gt.getGasType().getName());
					map.put("yyd", gt.getGasFactory().getProvince());
					map.put("sellPrice", gt.getGasPrice());
					map.put("volume", gt.getGasVolume());
					String pubUserId = gt.getAddUserId();
					Integer oStatus = -3;
					if(pubUserId.equals(userId)) {//商家
						//显示确认订单的信息
						gtoId = gtoId_qr;
						userType = "pubUser";
						//商家看的是确认用户的信息
						List<GasTradeOrderLog> gtolList = gtolService.getGtLogList(gtoId);
						if(gtolList.size() > 0) {
							User buyUser = gtolList.get(0).getGasTradeOrder().getUser();
							map.put("userHead", buyUser.getUserPortrait());
							map.put("lxName", buyUser.getWxName());
							map.put("lxTel", buyUser.getMobile());
						}
					}else {//用户
						userType = "buyUser";
						//用户查看的是商家的联系 信息
						User user = uService.getEntityById(pubUserId);
						if(user != null) {
							map.put("userHead", user.getUserPortrait());
						}else {
							map.put("userHead", "");
						}
						map.put("lxName", EmojiDealUtil.changeStrToEmoji(gt.getLxName()));
						map.put("lxTel", gt.getLxTel());
						//获取指定用户指定燃气贸易的订单编号
						List<GasTradeOrder> gtoList = gtoSeriver.listComInfoByOpt(userId, gtId);
						if(gtoList.size() > 0) {
							gtoId = gtoList.get(0).getId();
						}
					}
					map.put("userType", userType);
					//订单信息
					//首款凭证
					if(!gtoId.equals("")) {
						List<GasTradeOrderLog> gtolList0 = gtolService.getGtLogList(gtoId,2);//用户上传的首款
						if(gtolList0.size() > 0) {
							GasTradeOrderLog gtol = gtolList0.get(0);
							map.put("feeImg1", gtol.getOrderImgDetail());
							map.put("feeRemark", gtol.getOrderDetailTxt());
						}else {
							map.put("feeImg1", "");
							map.put("feeRemark", "");
						}
						List<GasTradeOrderLog> gtolList1 = gtolService.getGtLogList(gtoId,4);//用户上传的磅单
						if(gtolList1.size() > 0) {
							GasTradeOrderLog gtol = gtolList1.get(0);
							map.put("bdImg", gtol.getOrderImgDetail());
							map.put("bdRemark", gtol.getOrderDetailTxt());
						}else {
							map.put("bdImg", "");
							map.put("bdRemark", "");
						}
						List<GasTradeOrderLog> gtolList2 = gtolService.getGtLogList(gtoId,5);//用户上传的尾款
						if(gtolList2.size() > 0) {
							GasTradeOrderLog gtol = gtolList2.get(0);
							map.put("feeImg2", gtol.getOrderImgDetail());
							map.put("feeRemark2", gtol.getOrderDetailTxt());
						}else {
							map.put("feeImg2", "");
							map.put("feeRemark2", "");
						}
					}else {
						map.put("feeImg1", "");
						map.put("feeImg2", "");
						map.put("bdImg", "");
						map.put("feeRemark", "");
						map.put("bdRemark", "");
						map.put("feeRemark2", "");
					}
					GasTradeOrder gto = gtoSeriver.getEntityById(gtoId);
					String orderRemark = "";
					String addOrderTime = "";
					String orderNo = "";
					if(gto != null) {
						oStatus = gto.getOrderStatus();
						addOrderTime = gto.getAddTime();
						orderNo = gto.getOrderNo();
						orderRemark = EmojiDealUtil.changeStrToEmoji(gto.getRemark());//订单备注
					}
					map.put("orderNo", orderNo);
					map.put("orderRemark", orderRemark);
					map.put("addOrderTime", addOrderTime);
					//配送信息
					List<Object> list_ps = new ArrayList<Object>();
					if(oStatus >= 1 || userType.equals("pubUser")) {//商户确认订单后才能显示配送信息或者商户下
						Map<String,String> map_ps = new HashMap<String,String>();
						map_ps.put("cpNo", gt.getCpNo());
						map_ps.put("gcNo", gt.getGcNo());
						map_ps.put("jsrName", gt.getJsyName());
						map_ps.put("jsrMobile", gt.getJsyMobile());
						map_ps.put("yyrName", gt.getYyrName());
						map_ps.put("yyrMobile", gt.getYyrMobile());
						list_ps.add(map_ps);
					}
					map.put("psList", list_ps);
					//铅封信息
					map.put("qfTxt1", gt.getQfText1());
					map.put("qfTxt2", gt.getQfText2());
					map.put("qfTxt3", gt.getQfText3());
					map.put("qfImg1", gt.getQfImg1());
					map.put("qfImg2", gt.getQfImg2());
					map.put("qfImg3", gt.getQfImg3());
					//磅单信息
					map.put("bdImgPub", gt.getBdImg());
					//危化品信息
					map.put("whpImg", gt.getWhpImg());
					//
					map.put("tructsImg", gt.getTructsImg());
					//获取燃气买卖其他详图
					List<GasTradeImg> gtiList = gtService.listInfoByGtId(gtId);
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
					String tipsTxt = "";
					String orderStatusChi = "";
					//评价list
					List<Object> list_pj = new ArrayList<Object>();
					if(oStatus.equals(-2)) {
						orderStatusChi = "已取消";//用户主动取消
						tipsTxt = "用户已取消订单";
					}else if(oStatus.equals(-1)) {
						orderStatusChi = "已拒绝";//商户取消
						tipsTxt = "商家已拒绝";
					}else if(oStatus.equals(0)) {
						orderStatusChi = "待商家确认";//下单等待商家确认0
						tipsTxt = "买家已下单，等待商家确认";
					}else if(oStatus.equals(1)) {
						orderStatusChi = "待付款";//商家确认后等待用户上传付款凭证状态修改为1
						tipsTxt = "商家已确认，等待用户付款并上传缴费凭证";
					}else if(oStatus.equals(2)) {
						orderStatusChi = "待发货";//商家确认无误后等待商家发货状态修改为2，确认有误时状态修改1，直到确认完成
						tipsTxt = "用户已付预付款，等待商家确认";
					}else if(oStatus.equals(3)) {
						orderStatusChi = "待收货";//商家发货后状态修改为3
						tipsTxt = "商家已发货，等待用户确认收货";
					}else if(oStatus.equals(4)) {
						orderStatusChi = "待付款";//买家点击收货时上传磅单，状态修改为4
						tipsTxt = "用户已确认收货，等待用户上传余款缴费凭证";
					}else if(oStatus.equals(5)) {
						orderStatusChi = "待商家确认";//买家上传尾款凭证后，状态修改为5
						tipsTxt = "用户已付余款，等待商家确认";
					}else if(oStatus.equals(6)) {
						orderStatusChi = "待买家评价";//商家确认后状态修改为6，确认有误时状态修改为4
						tipsTxt = "商家已确认收款，等待用户评价";
					}else if(oStatus.equals(7)) {
						orderStatusChi = "订单完成";//买家评价后状态修改为7，订单完成
						tipsTxt = "订单完成";
						//评价信息
						Map<String,Object> map_pj = new HashMap<String,Object>();
						if(gto != null) {
							map_pj.put("pjUserHead", gto.getUser().getUserPortrait());
							map_pj.put("pjUserName", gto.getUser().getRealName());
							map_pj.put("pjDate", gto.getAddTime());
							map_pj.put("pjScore", gto.getOrderPjNumber());
							map_pj.put("pjDetail", gto.getOrderPjDetail());
							list_pj.add(map_pj);
						}
					}
					map.put("pjList", list_pj);
					map.put("oStatus", oStatus);
					map.put("orderStatusChi", orderStatusChi);
					map.put("tipsTxt", tipsTxt);
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/getSpecGtOrderPjDetail")
	@ApiOperation(value = "获取指定燃气贸易的评价详情--前台用", notes = "获取指定燃气贸易的评价详情--前台用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "gtId", value = "燃气交易订单编号")
	})
	public GenericResponse getSpecGtOrderPjDetail(HttpServletRequest request) {
		Integer status = 50001;
		List<Object> list = new ArrayList<Object>();
		String gtId = CommonTools.getFinalStr("gtId", request);
		try {
			if(!gtId.equals("")) {
				GasTrade gt = gtService.getEntityById(gtId);
				if(gt != null) {
					String gtoId_qr = gt.getTradeOrderId();//确认订单
					Map<String,Object> map_pj = new HashMap<String,Object>();
					//订单信息
					//首款凭证
					if(!gtoId_qr.equals("")) {
						GasTradeOrder gto = gtoSeriver.getEntityById(gtoId_qr);
						if(gto != null) {
							if(gto.getOrderStatus() == 7) {
								status = 200;
								map_pj.put("pjUserHead", gto.getUser().getUserPortrait());
								map_pj.put("pjUserName", gto.getUser().getRealName());
								map_pj.put("pjDate", gto.getAddTime());
								map_pj.put("pjScore", gto.getOrderPjNumber());
								map_pj.put("pjDetail", gto.getOrderPjDetail());
								list.add(map_pj);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/queryGtOrderByGtoId")
	@ApiOperation(value = "根据燃气交易订单编号获取燃气交易订单详情", notes = "根据燃气交易订单编号获取燃气交易订单详情")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gtoId", value = "燃气交易订单编号"), 
			@ApiImplicitParam(name = "gtoId", value = "燃气交易订单编号")
	})
	public GenericResponse queryGtOrderByGtoId(String gtoId) {
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			GasTradeOrder gt = gtoSeriver.getEntityById(gtoId);
			if (gt == null) {
				status = 50001;
			}else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", gt.getId());
				map.put("orderNo", gt.getOrderNo());
				GasTrade gasTrade = gt.getGasTrade();
				GasFactory gf = gasTrade.getGasFactory();
				map.put("title", gf.getProvince()+gf.getName()+gf.getGasType().getName());
				String headImg = gasTrade.getHeadImg();
				if(headImg.equals("")) {
					headImg = gf.getGasType().getYzImg();
				}
				map.put("headImg", headImg);
				map.put("gasTradeId", gasTrade.getId());
				map.put("sellCpyName", gasTrade.getCompany().getName());
				map.put("buyUserName", gt.getUser().getRealName());
				map.put("buyCpyName", gt.getCompany().getName());
				map.put("buyUserMobile", gt.getLxMobile());
				map.put("price", gt.getPrice());
				map.put("remark", gt.getRemark());
				map.put("userProv", gt.getLxrProv());
				map.put("userCity", gt.getLxrCity());
				map.put("userAddress", gt.getLxrProv() + gt.getLxrCity() + EmojiDealUtil.changeStrToEmoji(gt.getLxrAddress()));
				map.put("gpsInfo", gt.getLxrGpsInfo());
				map.put("distance", gt.getDistance());
				map.put("addTime", gt.getAddTime());
				Integer oStatus = gt.getOrderStatus();
				String orderStatusChi = "";
				map.put("orderStatus", oStatus);
				if(oStatus.equals(-1)) {
					orderStatusChi = "已取消";//商户取消
				}else if(oStatus.equals(0)) {
					orderStatusChi = "待商家确认";//下单等待商家确认0
				}else if(oStatus.equals(1)) {
					orderStatusChi = "待付款";//付款凭证上传后等待商家确认后状态修改为1
				}else if(oStatus.equals(2)) {
					orderStatusChi = "待发货";//商家确认无误后等待商家发货状态修改为2，确认有误时状态修改为0，直到确认完成
				}else if(oStatus.equals(3)) {
					orderStatusChi = "待收货";//商家发货后状态修改为3
				}else if(oStatus.equals(4)) {
					orderStatusChi = "待付款";//买家点击收货时上传磅单，状态修改为4
				}else if(oStatus.equals(5)) {
					orderStatusChi = "待商家确认";//买家上传尾款凭证后，状态修改为5
				}else if(oStatus.equals(6)) {
					orderStatusChi = "待买家评价";//商家确认后状态修改为6，确认有误时状态修改为4
				}else if(oStatus.equals(7)) {
					orderStatusChi = "订单完成";//买家评价后状态修改为7，订单完成
				}
				map.put("orderStatusChi", orderStatusChi);
				map.put("pjNumber", gt.getOrderPjNumber());
				map.put("pjDetail", gt.getOrderPjDetail());
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/queryGtoLogByGtoId")
	@ApiOperation(value = "根据订单编号获取燃气交易订单日志信息", notes = "根据订单编号获取燃气交易订单日志信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "gtoId", value = "燃气交易订单编号")})
	public GenericResponse queryGtoLogByGtoId(String gtoId) {
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			List<GasTradeOrderLog> gtolList = gtolService.getGtLogList(gtoId);
			if (gtolList.isEmpty()) {
				status = 50001;
			}else {
				for(GasTradeOrderLog gtol : gtolList) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("detailTxt", gtol.getOrderDetailTxt());
					map.put("detailImg", gtol.getOrderImgDetail());
					map.put("addTime", gtol.getAddTime());
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
