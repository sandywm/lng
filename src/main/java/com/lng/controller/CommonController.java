package com.lng.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.lng.pojo.DriverQz;
import com.lng.pojo.DriverZp;
import com.lng.pojo.GasFactory;
import com.lng.pojo.GasTrade;
import com.lng.pojo.GasTradeOrder;
import com.lng.pojo.GasType;
import com.lng.pojo.HqProvinceOrder;
import com.lng.pojo.MessageCenter;
import com.lng.pojo.PotTrade;
import com.lng.pojo.RqDevTrade;
import com.lng.pojo.TrucksTrade;
import com.lng.pojo.TrucksType;
import com.lng.pojo.User;
import com.lng.pojo.UserCompany;
import com.lng.service.CommonProvinceOrderService;
import com.lng.service.CompanyService;
import com.lng.service.DriverQzService;
import com.lng.service.DriverZpService;
import com.lng.service.GasFactoryService;
import com.lng.service.GasTradeOrderService;
import com.lng.service.GasTradeService;
import com.lng.service.GasTypeService;
import com.lng.service.HqProvinceOrderService;
import com.lng.service.MessageCenterService;
import com.lng.service.PotTradeService;
import com.lng.service.RqDevTradeService;
import com.lng.service.TrucksTradeService;
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
import com.baidu.ueditor.ActionEnter;

@RestController
@Api(tags = "通用相关接口")
@RequestMapping("/common")
public class CommonController {

	@Autowired
	private CommonProvinceOrderService cpos;
	@Autowired
	private HqProvinceOrderService hpos;
	@Autowired
	private GasFactoryService gfs;
	@Autowired
	private GasTypeService gts;
	@Autowired
	private MessageCenterService mcs;
	@Autowired
	private GasTradeService gasTrades;
	@Autowired
	private TrucksTradeService tts;
	@Autowired
	private RqDevTradeService rdts;
	@Autowired
	private PotTradeService pts;
	@Autowired
	private DriverQzService dqzs;
	@Autowired
	private DriverZpService dzps;
	@Autowired
	private UserService us;
	@Autowired
	private TrucksTradeService trucksTradeService;
	@Autowired
	private PotTradeService potTradeService;
	@Autowired
	private RqDevTradeService rdtService;
	@Autowired
	private CompanyService cService;
	@Autowired
	private GasTradeOrderService gtos;

	@GetMapping("/getProvinceList")
	@ApiOperation(value = "获取省份排序列表", notes = "lng液厂显示顺序用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "gsType", value = "液质类型(0:其他,1:海气)", dataType = "integer", required = true) })
	public GenericResponse getProvinceList(Integer gsType) {
		Integer status = 200;
		List<CommonProvinceOrder> cpList = new ArrayList<CommonProvinceOrder>();
		List<HqProvinceOrder> hpList = new ArrayList<HqProvinceOrder>();
		List<Object> list = new ArrayList<Object>();
		try {
			if (gsType.equals(0)) {
				cpList = cpos.listAllInfo("asc");
				for(CommonProvinceOrder cp : cpList) {
					Map<String, Object> map_d = new HashMap<String, Object>();
					map_d.put("id", cp.getId());
					map_d.put("province", cp.getProvince());
					map_d.put("provincePy", cp.getProvincePy());
					map_d.put("orderNo", cp.getOrderNo());
					map_d.put("state",0);
					list.add(map_d);
				}
				
			} else {
				hpList = hpos.listAllInfo("asc");
				for(HqProvinceOrder hp : hpList) {
					Map<String, Object> map_d = new HashMap<String, Object>();
					map_d.put("id", hp.getId());
					map_d.put("province", hp.getProvince());
					map_d.put("provincePy", hp.getProvincePy());
					map_d.put("orderNo", hp.getOrderNo());
					map_d.put("state",0);
					list.add(map_d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		if (gsType.equals(0)) {
			return ResponseFormat.retParam(status, list);
		} else {
			return ResponseFormat.retParam(status, list);
		}
	}

	@PostMapping("/addProvince")
	@ApiOperation(value = "增加省份排序列表", notes = "lng液厂显示顺序用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "province", value = "省份", required = true),
			@ApiImplicitParam(name = "gsType", value = "液质类型(0:其他,1:海气)", dataType = "integer", required = true) })
	public GenericResponse addProvince(HttpServletRequest request, String province, Integer gsType) {
		Integer status = 200;
		Integer id = 0;
		List<CommonProvinceOrder> cpList = new ArrayList<CommonProvinceOrder>();
		List<HqProvinceOrder> hpList = new ArrayList<HqProvinceOrder>();
		try {
			if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.ADD_PROV)) {
				if (gsType.equals(0)) {
					cpList = cpos.listAllInfo("desc");
					boolean flag = false;
					Integer orderNo = 0;
					if (cpList.size() == 0) {
						flag = true;
					} else {
						// 先判断有无重复
						if (cpos.getEntityByOpt(0, province) == null) {
							flag = true;
							orderNo = cpList.get(0).getOrderNo() + 1;
						}
					}
					if (flag) {
						id = cpos.saveOrUpdate(
								new CommonProvinceOrder(province, CommonTools.getFirstSpell(province), orderNo));
					} else {
						status = 50003;
					}
				} else {
					hpList = hpos.listAllInfo("desc");
					boolean flag = false;
					Integer orderNo = 100000;
					if (hpList.size() == 0) {
						flag = true;
					} else {
						// 先判断有无重复
						if (hpos.getEntityByOpt(0, province) == null) {
							flag = true;
							orderNo = hpList.get(0).getOrderNo() + 1;
						}
					}
					if (flag) {
						id = hpos.saveOrUpdate(
								new HqProvinceOrder(province, CommonTools.getFirstSpell(province), orderNo));
					} else {
						status = 50003;
					}
				}
			} else {
				status = 70001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, id);
	}

	@PutMapping("/upProvName")
	@ApiOperation(value = "修改省份名称", notes = "修改省份名称")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问"), @ApiResponse(code = 10002, message = "参数为空"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "proId", value = "省份ID", required = true),
			@ApiImplicitParam(name = "provName", value = "身份名称", required = true),
			@ApiImplicitParam(name = "gsType", value = "液质类型(0:其他,1:海气)", dataType = "integer", required = true)

	})
	public GenericResponse upProvName(HttpServletRequest request) {
		Integer status = 200;
		Integer provId = CommonTools.getFinalInteger("provId", request);
		String provName = CommonTools.getFinalStr("provName", request);
		Integer gsType = CommonTools.getFinalInteger("gsType", request);
		try {
			if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.ADD_PROV)) {
				if (provId.equals(0) || provName.equals("")) {
					status = 10002;
				} else {
					if (gsType.equals(0)) {
						CommonProvinceOrder cpo = cpos.getEntityByOpt(provId, "");
						if (cpo != null) {
							if (!provName.equals(cpo.getProvince())) {
								cpo.setProvince(provName);
								cpo.setProvincePy(CommonTools.getFirstSpell(provName));
								cpos.saveOrUpdate(cpo);
							}
						} else {
							status = 50001;
						}
					} else if (gsType.equals(1)) {
						HqProvinceOrder hpo = hpos.getEntityByOpt(provId, "");
						if (hpo != null) {
							if (!provName.equals(hpo.getProvince())) {
								hpo.setProvince(provName);
								hpo.setProvincePy(CommonTools.getFirstSpell(provName));
								hpos.saveOrUpdate(hpo);
							}
						} else {
							status = 50001;
						}
					}
				}
			} else {
				status = 70001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/setProvOrder")
	@ApiOperation(value = "修改省份排序列表", notes = "lng液厂显示顺序用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问"), @ApiResponse(code = 10002, message = "参数为空") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "proIdStr", value = "省份ID组合", required = true),
			@ApiImplicitParam(name = "gsType", value = "液质类型(0:其他,1:海气)", dataType = "integer", required = true) })
	public GenericResponse setProvOrder(HttpServletRequest request, String proIdStr, Integer gsType) {
		Integer status = 200;
		try {
			if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.UP_YC)) {
				if (proIdStr == null || proIdStr == "") {
					status = 10002;
				} else {
					String[] provIdArr = proIdStr.split(",");
					if (gsType.equals(0)) {
						for (int i = 0; i < provIdArr.length; i++) {
							Integer provId = Integer.parseInt(provIdArr[i]);
							CommonProvinceOrder cpo = cpos.getEntityByOpt(provId, "");
							if (cpo != null) {
								cpo.setOrderNo(i);
								cpos.saveOrUpdate(cpo);
								// 批量修改普通类型的液厂
								Integer orderNo = cpo.getOrderNo();
								List<GasFactory> gfList = gfs.listInfoByOpt("", "", "", cpo.getProvince(), "", -1,"");
								for (GasFactory gf : gfList) {
									if (gf.getGasType().getName().equals("海气")) {
										continue;
									} else {
										gf.setOrderNo(orderNo);
										gfs.addOrUpGasFactory(gf);
									}
								}
							}
						}
					} else {
						for (int i = 0; i < provIdArr.length; i++) {
							Integer provId = Integer.parseInt(provIdArr[i]);
							HqProvinceOrder hpo = hpos.getEntityByOpt(provId, "");
							if (hpo != null) {
								hpo.setOrderNo(i);
								hpos.saveOrUpdate(hpo);
								// 批量修改海气类型的液厂
								Integer orderNo = hpo.getOrderNo();
								// 获取海气的编号
								List<GasType> gtList = gts.getGasTypeByNameList("海气");
								String gtId = "";
								if (gtList.size() > 0) {
									gtId = gtList.get(0).getId();
									List<GasFactory> gfList = gfs.listInfoByOpt("", "", gtId, hpo.getProvince(), "",
											-1,"");
									for (GasFactory gf : gfList) {
										gf.setOrderNo(orderNo);
										gfs.addOrUpGasFactory(gf);
									}
								}
							}
						}
					}
				}
			} else {
				status = 70001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/getWelcomeData")
	@ApiOperation(value = "获取微信首页数据", notes = "获取微信首页数据--价格变动消息为当天的消息，燃气贸易为最新的一条消息，行业资讯为最新的一条消息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "days", value = "最近几天的数据(0-30)-货车买卖，设备买卖，储罐租卖，司机招聘/求职"),
		@ApiImplicitParam(name = "limit", value = "行业资讯显示条数（不传默认为2）")
	})
	public GenericResponse getWelcomeData(HttpServletRequest request) {
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		String currentDate = CurrentTime.getStringDate();
		Integer days = CommonTools.getFinalInteger("days", request);
		Integer xwLimit = CommonTools.getFinalInteger("limit", request);
		String sDate = CurrentTime.getFinalDate(currentDate, -days);
		try {
			// 获取当天最新的价格变动消息
			List<MessageCenter> mcList = mcs.listMsgByOpt(4, currentDate, currentDate);
			// 获取燃气贸易最新的一条记录
			Page<GasTrade> gtList = gasTrades.listInfoByOpt("", "", 1, -1, 1, 1);
			// 获取货车买卖最近一周最新记录条数
			Integer tructsTradeNum = tts.listTrucksTradeByOpt(1, -1, sDate, currentDate).size();
			// 获取燃气设备买卖最近一周最新记录条数
			Integer rqDevTradeNum = rdts.listInfoByOpt(sDate, currentDate, 1, -1).size();
			// 获取储罐租卖当天最新最近一周记录条数
			Integer potTradeNum = pts.listPotTradeByOpt(sDate, currentDate, 1, -1).size();
			// 获取最近2条行业资讯
			if(xwLimit.equals(0)) {
				xwLimit = 2;
			}
			Page<MessageCenter> mcList_xw = mcs.getMessageCenterByOption(1, "", 0, -1, 0, xwLimit);
			// 获取最近一周最近一条司机求职的记录
			List<DriverQz> qzList = dqzs.listQzInfoByOpt(sDate, currentDate, 1, -1);
			// 获取最近一周最近一条招聘司机的记录
			List<DriverZp> zpList = dzps.listDriverZpByOpt(sDate, currentDate, 1, -1);
			Map<String, Object> map = new HashMap<String, Object>();
			List<Object> list_mc = new ArrayList<Object>();// 燃气贸易
			for(MessageCenter mc : mcList) {
				Map<String, Object> map_d = new HashMap<String, Object>();
				map_d.put("title", "通知："+mc.getContent());
				list_mc.add(map_d);
			}
			map.put("topMsgList", list_mc);
			map.put("tructsTradeNum", tructsTradeNum);
			map.put("rqDevTradeNum", rqDevTradeNum);
			map.put("potTradeNum", potTradeNum);
			List<Object> list_1 = new ArrayList<Object>();// 燃气贸易
			for (GasTrade gt : gtList) {
				Map<String, Object> map_d = new HashMap<String, Object>();
				String headImg = gt.getHeadImg();
				if(headImg == null || headImg.equals("")) {
					headImg = gt.getGasType().getYzImg();
				}
				map_d.put("headImg", headImg);
				map_d.put("gasTradeId", gt.getId());
				String addUserId = gt.getAddUserId();
				User user = us.getEntityById(addUserId);
				String title = "";
				if (user != null) {
					title = user.getRealName() + "发布了一条卖气信息";
				}
				map_d.put("title", title);
				map_d.put("psArea", gt.getPsArea());
				map_d.put("gasTypeName", gt.getGasType().getName());
				map_d.put("yyd", gt.getGasFactory().getProvince());
				list_1.add(map_d);
			}
			map.put("gasTradeList", list_1);
			List<Object> list_news = new ArrayList<Object>();// 行业资讯
			for (MessageCenter mc : mcList_xw) {
				Map<String, Object> map_d = new HashMap<String, Object>();
				map_d.put("newId", mc.getId());
				map_d.put("mainImg", mc.getMainImg());
				map_d.put("newTitle", mc.getTitle());
				map_d.put("addTime", mc.getAddTime());
				list_news.add(map_d);
			}
			map.put("newsList", list_news);
			List<Object> list_qz = new ArrayList<Object>();// 求职
			if (qzList.size() > 0) {
				DriverQz qz = qzList.get(0);
				Map<String, Object> map_qz = new HashMap<String, Object>();
				map_qz.put("qzId", qz.getId());
				map_qz.put("qzUserName", qz.getUserName());
				map_qz.put("userAge", qz.getAge());
				map_qz.put("jzType", qz.getJzType());
				map_qz.put("jzYear", qz.getJzYear());
				map_qz.put("address", qz.getProvince() + qz.getCity());
				list_qz.add(map_qz);
			}
			map.put("qzList", list_qz);
			List<Object> list_zp = new ArrayList<Object>();// 招聘
			if (zpList.size() > 0) {
				DriverZp zp = zpList.get(0);
				Map<String, Object> map_zp = new HashMap<String, Object>();
				map_zp.put("qzId", zp.getId());
				map_zp.put("cpyName", zp.getCompany().getName());
				map_zp.put("address", zp.getProvince() + zp.getCity());
				map_zp.put("jzType", zp.getJzType());
				map_zp.put("jlYear", zp.getJlYearRange());
				map_zp.put("ageRange", zp.getSjAgeRange());
				list_zp.add(map_zp);
			}
			map.put("zpList", list_zp);
			list.add(map);
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

	@GetMapping("/myPublish")
	@ApiOperation(value = "获取我的发布", notes = "获取我的发布信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 10002, message = "参数为空"), })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true),
			@ApiImplicitParam(name = "pubType", value = "发布类型（tructTrade-槽车租卖,gasDev-燃气设备,potTrade-储罐租卖,gasTrade-燃气买卖）", required = true),
			@ApiImplicitParam(name = "page", value = "第几页"), @ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse myPublish(String userId, Integer showStatus, String pubType, Integer page, Integer limit) {
		userId = CommonTools.getFinalStr(userId);
		pubType = CommonTools.getFinalStr(pubType);
		Integer status = 200;
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		if (showStatus == null) {
			showStatus = -1;
		}
		Long count = null;
		List<Object> list = new ArrayList<Object>();
		try {
			if (userId.isEmpty() || pubType.isEmpty() || showStatus == -1) {
				status = 10002;
			} else {
				if (pubType.equalsIgnoreCase("tructTrade")) {
					Page<TrucksTrade> trucksTrades = trucksTradeService.trucksTradeOnPublish(userId, showStatus,
							page - 1, limit);
					count = trucksTrades.getTotalElements();
					if (count == 0) {
						status = 50001;
					} else {
						for (TrucksTrade tt : trucksTrades) {
							Map<String, Object> map = new HashMap<String, Object>();
							String cId = tt.getCompanyId();
							if (!cId.isEmpty()) {
								Company cpy = cService.getEntityById(cId);
								map.put("cpyName", cpy.getName());
							} else {
								map.put("cpyName", "");
							}
							TrucksType trucksType = tt.getTrucksType();
							map.put("title", tt.getTrucksHeadPp().getName() + trucksType.getName());
							map.put("id", tt.getId());
							map.put("mainImg", tt.getMainImg());
							map.put("spYear", tt.getSpYear());
							map.put("buyYear", tt.getBuyYear());
							map.put("headTypeName", tt.getTrucksHeadType().getName());
							map.put("headPpName", tt.getTrucksHeadPp().getName());
							map.put("trucksTypeName", tt.getTrucksType().getName());
							map.put("trucksTypes", tt.getTrucksType().getType());
							map.put("xsDistance", tt.getXsDistance());
							map.put("price", tt.getPrice());
							map.put("regPlace", tt.getRegPlace());
							map.put("hot", tt.getHot());
							map.put("tradeType", tt.getTradeType());
							map.put("area", tt.getArea());
							map.put("addTime", tt.getAddTime());
							list.add(map);
						}
					}
				} else if (pubType.equalsIgnoreCase("potTrade")) {
					Page<PotTrade> potTrades = potTradeService.potTradeOnPublish(userId, showStatus, page - 1, limit);
					count = potTrades.getTotalElements();
					if (count == 0) {
						status = 50001;
					} else {
						for (PotTrade pt : potTrades) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", pt.getId());
							map.put("mainImg", pt.getMainImg());
							map.put("potPpName", pt.getTrucksPotPp().getName());
							map.put("potVolume", pt.getPotVolume());
							map.put("sxInfo", pt.getSxInfo());
							map.put("buyYear", pt.getBuyYear());
							map.put("zzJzTypeName", pt.getPotZzjzType().getName());
							map.put("leasePrice", pt.getLeasePrice());
							map.put("sellPrice", pt.getSellPrice());
							map.put("addTime", pt.getAddTime());
							list.add(map);
						}

					}

				} else if (pubType.equalsIgnoreCase("gasDev")) {
					Page<RqDevTrade> rqDevTrades = rdtService.rqDevTradeOnPublish(userId, showStatus, page - 1, limit);
					count = rqDevTrades.getTotalElements();
					if (count == 0) {
						status = 50001;
					} else {
						for (RqDevTrade rdt : rqDevTrades) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", rdt.getId());
							map.put("mainImg", rdt.getMainImg());
							map.put("devName", rdt.getDevName());
							map.put("devNo", rdt.getDevNo());
							map.put("devPp", rdt.getDevPp());
							map.put("devPrice", rdt.getDevPrice());
							map.put("addTime", rdt.getAddTime());
							list.add(map);
						}

					}
				} else if (pubType.equalsIgnoreCase("gasTrade")) {
					Page<GasTrade> gasTradesList = gasTrades.gasTradeOnPublish(userId, showStatus, page - 1, limit);
					count = gasTradesList.getTotalElements();
					if (count == 0) {
						status = 50001;
					} else {
						for (GasTrade gt : gasTradesList) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("id", gt.getId());
							map.put("headImg", gt.getHeadImg());
							map.put("gasPrice", gt.getGasPrice());
							map.put("psArea", gt.getPsArea());
							map.put("gasName", gt.getGasType().getName());
							map.put("gasVolume", gt.getGasVolume());
							// 从燃气交易中获取好评度
							Integer pjScore = 0;
							List<GasTradeOrder> gtoList = gtos.listComInfoByCpyId(gt.getCompany().getId());
							Integer tradeNum = gtoList.size();
							for (GasTradeOrder gto : gtoList) {
								pjScore += gto.getOrderPjNumber();
							}
							if (tradeNum > 0) {
								map.put("hpRate", CommonTools.convertInputNumber(pjScore * 100.0 / tradeNum));
							} else {
								map.put("hpRate", "暂无");
							}
							map.put("addTime", gt.getAddTime());
							list.add(map);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}

		return ResponseFormat.getPageJson(limit, page, count, status, list);
	}
}
