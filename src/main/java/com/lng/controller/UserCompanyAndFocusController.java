package com.lng.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.Company;
import com.lng.pojo.DriverQz;
import com.lng.pojo.DriverZp;
import com.lng.pojo.GasTrade;
import com.lng.pojo.GasTradeOrder;
import com.lng.pojo.MessageCenter;
import com.lng.pojo.PotTrade;
import com.lng.pojo.RqDevTrade;
import com.lng.pojo.SuperUser;
import com.lng.pojo.TrucksTrade;
import com.lng.pojo.TrucksType;
import com.lng.pojo.User;
import com.lng.pojo.UserCompany;
import com.lng.pojo.UserFocus;
import com.lng.pojo.WqPfbz;
import com.lng.service.CompanyService;
import com.lng.service.DriverQzService;
import com.lng.service.DriverZpService;
import com.lng.service.GasTradeOrderService;
import com.lng.service.GasTradeService;
import com.lng.service.MessageCenterService;
import com.lng.service.PotTradeService;
import com.lng.service.RqDevTradeService;
import com.lng.service.SuperService;
import com.lng.service.TrucksTradeService;
import com.lng.service.UserCompanyService;
import com.lng.service.UserFocusService;
import com.lng.service.UserService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
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
@Api(tags = "用户公司和关注相关接口")
@RequestMapping("/userCompany")
public class UserCompanyAndFocusController {
	@Autowired
	private UserCompanyService ucService;
	@Autowired
	private UserFocusService ufService;
	@Autowired
	private CompanyService cService;
	@Autowired
	private UserService uService;
	@Autowired
	private MessageCenterService mcs;
	@Autowired
	private TrucksTradeService trucksTradeService;
	@Autowired
	private PotTradeService potTradeService;
	@Autowired
	private RqDevTradeService rdtService;
	@Autowired
	private GasTradeService gts;
	@Autowired
	private GasTradeOrderService gtos;
	@Autowired
	private SuperService ss;
	@Autowired
	private DriverQzService dqzs;
	@Autowired
	private DriverZpService dzps;
	@PostMapping("/addUserCompany")
	@ApiOperation(value = "添加用户公司关联", notes = "添加用户公司关联信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"),
			@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "compId", value = "公司编号", required = true) })
	public GenericResponse addUserCompany(HttpServletRequest request, String userId, String compId) {
		userId = CommonTools.getFinalStr(userId);
		compId = CommonTools.getFinalStr(compId);
		Integer status = 200;
		String ucId = "";
		try {
			Company company = cService.getEntityById(compId);
			if(company != null) {
				if(company.getCheckStatus() == 1) {
					List<UserCompany> ucList =  ucService.getUserCompanyList(compId, userId,-1);
					if (ucList.size() == 0) {//没申请记录
						UserCompany uc = new UserCompany();
						User user = uService.getEntityById(userId);
						uc.setUser(user);
						uc.setCompany(company);
						uc.setAddTime(CurrentTime.getCurrentTime());
						uc.setCheckStatus(0);
						uc.setCheckTime("");
						ucId = ucService.addOrUpdate(uc);
						MessageCenter mc = new MessageCenter("",user.getRealName()+"申请加入"+company.getName()+"您的公司", user.getRealName()+"申请加入"+company.getName()+"您的公司", 0, CurrentTime.getCurrentTime(), 2,
								ucId, "joinCpy", "", uc.getUser().getId(), 0);
						mcs.saveOrUpdate(mc);
					}else {
						UserCompany uc = ucList.get(0);
						if(uc.getCheckStatus() != 1) {
							User user = uService.getEntityById(userId);
							uc.setUser(user);
							uc.setCompany(company);
							uc.setAddTime(CurrentTime.getCurrentTime());
							uc.setCheckStatus(0);
							uc.setCheckTime("");
							ucId = ucService.addOrUpdate(uc);
							MessageCenter mc = new MessageCenter("",user.getRealName()+"申请加入"+company.getName()+"您的公司", user.getRealName()+"申请加入"+company.getName()+"您的公司", 0, CurrentTime.getCurrentTime(), 2,
									ucId, "joinCpy", "", uc.getUser().getId(), 0);
							mcs.saveOrUpdate(mc);
						}else {
							status = 50001;
						}
					}
				}else {
					status = 50001;
				}
			}else {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, ucId);
	}

	@PostMapping("/addUserFocus")
	@ApiOperation(value = "添加用户关注", notes = "添加用户关注信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在")})
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "focusId", value = "关注编号", required = true),
			@ApiImplicitParam(name = "focusType", value = "关注类型(cczm,rqsb,cgzm,rqmm,sjqz,sjzp)", required = true) })
	public GenericResponse addUserFocus(HttpServletRequest request, String userId, String focusId, String focusType) {
		userId = CommonTools.getFinalStr(userId);
		focusId = CommonTools.getFinalStr(focusId);
		focusType = CommonTools.getFinalStr(focusType);
		Integer status = 200;
		String ufId = "";
		try {
			if (ufService.getUserFocusList(userId,focusId,focusType).size() == 0) {
				UserFocus uf = new UserFocus();
				User user = uService.getEntityById(userId);
				uf.setUser(user);
				uf.setFocusId(focusId);
				uf.setFocusType(focusType);
				uf.setFocusTime(CurrentTime.getCurrentTime());
				ufId = ufService.addOrUpdage(uf);
			} else {
				status = 50003;
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, ufId);
	}

	@PutMapping("/updateUserCompanyBySta")
	@ApiOperation(value = "用户公司关联信息审核", notes = "用户公司关联信息审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户公司关联编号", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)", required = true) })
	public GenericResponse updateUserCompanyBySta(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id",request);
		Integer checkSta = CommonTools.getFinalInteger("checkSta", request);
		Integer status = 200;
		try {
			UserCompany uc = ucService.getEntityId(id);
			if (uc == null) {
				status = 50001;
			} else {
				if (checkSta != null && !checkSta.equals(uc.getCheckStatus())) {
					uc.setCheckStatus(checkSta);
					uc.setCheckTime(CurrentTime.getCurrentTime());
					String result = "未审核通过";
					if(checkSta.equals(1)) {
						result = "审核通过";
					}
					Company cpy = uc.getCompany();
					MessageCenter mc = new MessageCenter("","您提交的加入"+cpy.getName()+"公司的申请"+result, "您提交的加入"+cpy.getName()+"公司的申请"+result, 0, CurrentTime.getCurrentTime(), 2,
							id, "joinCpy", "", uc.getUser().getId(), 0);
					mcs.saveOrUpdate(mc);
				}
				ucService.addOrUpdate(uc);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	@DeleteMapping("/delSpecUserCompany")
	@ApiOperation(value = "用户公司关联信息审核", notes = "用户公司关联信息审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户公司关联编号", required = true),
			@ApiImplicitParam(name = "userId", value = "操作用户编号", required = true)
	})
	public GenericResponse delSpecUserCompany(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id",request);
		String currUserId = CommonTools.getFinalStr("userId", request);
		Integer status = 200;
		try {
			UserCompany uc = ucService.getEntityId(id);
			if (uc == null) {
				status = 50001;
			} else {
				if(uc.getCompany().getOwerUserId().equals(currUserId)) {//只有公司创立人才能踢人
					Integer checkSta = uc.getCheckStatus();
					if (checkSta.equals(1)) {//审核通过的员工才能被踢出
						mcs.delMsgById(id);
					}else {
						status = 50001;
					}
				}else {
					status = 70001;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/queryUserCompany")
	@ApiOperation(value = "获取用户公司关联", notes = "获取用户公司关联信息，dealNum大于等于0时表示公司创建人需要处理的申请，-1时表示公司员工")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号"),
			@ApiImplicitParam(name = "userId", value = "用户编号"),
			@ApiImplicitParam(name = "checkStatus", value = "用户审核状态（0:未审核,1:审核通过,2:审核未通过,-1为全部）")
	})
	public GenericResponse queryUserCompany(HttpServletRequest request) {
		String compId = CommonTools.getFinalStr("compId", request);
		String userId = CommonTools.getFinalStr("userId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			List<UserCompany> ucs = ucService.getUserCompanyList(compId, "",checkStatus);
			if (ucs.size() == 0) {
				status = 50001;
			}else {
				for (UserCompany uc : ucs) {
					Map<String, Object> map_d = new HashMap<String, Object>();
					Company cpy = uc.getCompany();
					map_d.put("id", uc.getId());
					map_d.put("cpyId", cpy.getId());
					map_d.put("cpyName", cpy.getName());
					User user = uc.getUser();
					map_d.put("userId", user.getId());
					map_d.put("userHead", user.getUserPortrait());
					map_d.put("userName", user.getRealName());
					map_d.put("userMobile", user.getMobile());
					map_d.put("addTime", uc.getAddTime());
					map_d.put("checkStatus",uc.getCheckStatus());
					if(userId.equals(cpy.getOwerUserId())) {
						map_d.put("selfCpyFlag", true);
					}else {
						map_d.put("selfCpyFlag", false);
					}
					list.add(map_d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

	@GetMapping("/getSelfJoinCompanyList")
	@ApiOperation(value = "获取我加入的公司列表", notes = "获取我加入的公司列表")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({
			@ApiImplicitParam(name = "userId", value = "用户编号"),
			@ApiImplicitParam(name = "checkStatus", value = "用户审核状态（0:未审核,1:审核通过,2:审核未通过,-1为全部）")
	})
	public GenericResponse getSelfJoinCompanyList(HttpServletRequest request) {
		String userId = CommonTools.getFinalStr("userId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			List<UserCompany> ucs = ucService.getUserCompanyList("", userId,checkStatus);
			if (ucs.size() == 0) {
				status = 50001;
			}else {
				for (UserCompany uc : ucs) {
					User user = uc.getUser();
					Company cpy = uc.getCompany();
					if(!user.getId().equals(cpy.getOwerUserId())) {
						Map<String, Object> map_d = new HashMap<String, Object>();
						map_d.put("cpyId", cpy.getId());
						map_d.put("cpyName", cpy.getName());
						map_d.put("addTime", uc.getAddTime());
						map_d.put("checkStatus",uc.getCheckStatus());
						list.add(map_d);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("/queryUserFocus")
	@ApiOperation(value = "获取用户关注", notes = "获取用户关注")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "focusType", value = "关注类型(cczm,rqsb,cgzm,rqmm,sjqz-个人简历,sjzp-招聘信息)"), @ApiImplicitParam(name = "page", value = "第几页"),
			@ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse queryUserFocus(String userId, String focusType, Integer page, Integer limit) {
		userId = CommonTools.getFinalStr(userId);
		focusType = CommonTools.getFinalStr(focusType);
		Integer status = 200;
		if (page == null) {
			page = 1;
		}
		if (limit == null) {
			limit = 10;
		}
		Page<UserFocus> ufs = null;
		List<Object> list = new ArrayList<Object>();
		try {
			ufs = ufService.userFocusList(userId, focusType, page - 1, limit);
			if (ufs.getTotalElements() == 0) {
				status = 50001;
			} else {
				List<UserFocus> ufsList = ufs.getContent();
				for (UserFocus uf : ufsList) {
					String ufId = uf.getFocusId();
					focusType = uf.getFocusType();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("ufId", uf.getId());
					if (focusType.equalsIgnoreCase("cczm")) {
						TrucksTrade tt = trucksTradeService.getEntityById(ufId);
						String cId = tt.getCompanyId();
						if (!cId.isEmpty()) {
							Company cpy = cService.getEntityById(cId);
							map.put("cpyName", cpy.getName());
						} else {
							map.put("cpyName", "");
						}
//						map.put("title", tt.getTrucksHeadPp().getName() + tt.getTrucksType().getName());
//						map.put("ttId", tt.getId());
//						map.put("mainImg", tt.getMainImg());
//						map.put("spYear", tt.getSpYear());
//						map.put("buyYear", tt.getBuyYear());
//						map.put("headTypeName", tt.getTrucksHeadType().getName());
//						map.put("headPpName", tt.getTrucksHeadPp().getName());
//						map.put("trucksTypeName", tt.getTrucksType().getName());
//						map.put("trucksTypes", tt.getTrucksType().getType());
//						map.put("xsDistance", tt.getXsDistance());
//						map.put("price", tt.getPrice());
//						map.put("regPlace", tt.getRegPlace());
//						map.put("hot", tt.getHot());
//						map.put("tradeType", tt.getTradeType());
//						map.put("area", tt.getArea());
						
						map.put("mainImg", tt.getMainImg());
						map.put("id", tt.getId());
						Integer tadeType = tt.getTradeType();
						Integer price = tt.getPrice();
						if(price.equals(0)) {
							map.put("price", "面议");
						}
						if (tadeType.equals(1)) {
							map.put("TradeTypeName", "租赁");
							if(price.equals(0)) {
								map.put("price", "面议");
							}else {
								map.put("price", price);
							}
						} else if (tadeType.equals(2)) {
							map.put("TradeTypeName", "买卖");
							if(price.equals(0)) {
								map.put("price", "面议");
							}else {
								map.put("price", price / 10000.0);
							}
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
						map.put("xsDistance", tt.getXsDistance());
						map.put("spYear", tt.getSpYear());
						map.put("checkStatus", tt.getCheckStatus());
						map.put("showStatus", tt.getShowStatus());
						map.put("area", tt.getArea());
						map.put("userType", tt.getUserType());
						Integer userType = tt.getUserType();
						if(userType.equals(2)) {
							User user = uService.getEntityById(tt.getAddUserId());
							if(user != null) {
								map.put("pubUserHead", user.getUserPortrait());
								map.put("pubUserName", user.getRealName());
							}
						}else {
							SuperUser su = ss.getEntityById(tt.getAddUserId());
							map.put("pubUserHead", "");
							map.put("pubUserName", su.getRealName());
						}
						map.put("pubDate", tt.getAddTime());
					} else if (focusType.equalsIgnoreCase("cgzm")) {
						PotTrade pt = potTradeService.getEntityById(ufId);
						map.put("id", pt.getId());
						map.put("mainImg", pt.getMainImg());
						map.put("potPpName", pt.getTrucksPotPp().getName());
						map.put("potVolume", pt.getPotVolume());
						map.put("sxInfo", pt.getSxInfo());
						map.put("buyYear", pt.getBuyYear());
						map.put("zzJzTypeName", pt.getPotZzjzType().getName());
						map.put("leasePrice", pt.getLeasePrice());
						map.put("sellPrice", pt.getSellPrice());
						Integer userType = pt.getUserType();
						if(userType.equals(2)) {
							User user = uService.getEntityById(pt.getAddUserId());
							if(user != null) {
								map.put("pubUserHead", user.getUserPortrait());
								map.put("pubUserName", user.getRealName());
							}
						}else {
							SuperUser su = ss.getEntityById(pt.getAddUserId());
							map.put("pubUserHead", "");
							map.put("pubUserName", su.getRealName());
						}
						map.put("pubDate", pt.getAddTime());
						map.put("userType", pt.getUserType());
						map.put("checkStatus", pt.getCheckStatus());
						map.put("showStatus", pt.getShowStatus());
					} else if (focusType.equalsIgnoreCase("rqsb")) {
						RqDevTrade rdt = rdtService.getEntityById(ufId);
						map.put("id", rdt.getId());
						map.put("mainImg", rdt.getMainImg());
						map.put("devName", rdt.getDevName());
						map.put("devNo", rdt.getDevNo());
						map.put("devPp", rdt.getDevPp());
						map.put("devPrice", rdt.getDevPrice());
						Integer userType = rdt.getUserType();
						if(userType.equals(2)) {
							User user = uService.getEntityById(rdt.getAddUserId());
							if(user != null) {
								map.put("pubUserHead", user.getUserPortrait());
								map.put("pubUserName", user.getRealName());
							}
						}else {
							SuperUser su = ss.getEntityById(rdt.getAddUserId());
							map.put("pubUserHead", "");
							map.put("pubUserName", su.getRealName());
						}
						map.put("pubDate", rdt.getAddTime());
						map.put("userType", userType);
						map.put("checkStatus", rdt.getCheckStatus());
						map.put("showStatus", rdt.getShowStatus());
					} else if (focusType.equalsIgnoreCase("rqmm")) {
						GasTrade gt = gts.getEntityById(ufId);
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
						Integer userType = gt.getUserType();
						map.put("userType", userType);
						if(userType.equals(2)) {
							User user = uService.getEntityById(gt.getAddUserId());
							if(user != null) {
								map.put("pubUserHead", user.getUserPortrait());
								map.put("pubUserName", user.getRealName());
							}
						}else {
							SuperUser su = ss.getEntityById(gt.getAddUserId());
							map.put("pubUserHead", "");
							map.put("pubUserName", su.getRealName());
						}
						map.put("pubDate", gt.getAddTime());
						map.put("checkStatus", gt.getCheckStatus());
						map.put("showStatus", gt.getShowStatus());
					}else if (focusType.equalsIgnoreCase("sjqz")) {
						DriverQz qz = dqzs.getEntityById(ufId);
						if(qz != null) {
							map.put("qzId", qz.getId());
							map.put("userName", qz.getUserName());
							map.put("userMobile", qz.getUserMobile());
							map.put("userHead", qz.getUserHead());
							map.put("jzYear", qz.getJzYear());
							map.put("jzType", qz.getJzType());
							map.put("checkStatus", qz.getCheckStatus());
							map.put("showStatus", qz.getShowStatus());
							map.put("education", qz.getEducation());
							map.put("sex", qz.getSex());
							map.put("age", qz.getAge());
							map.put("prov", qz.getProvince());
							map.put("city", qz.getCity());
							Integer userType = qz.getUserType();
							map.put("userType", userType);
							if(userType.equals(2)) {
								User user = uService.getEntityById(qz.getUserId());
								if(user != null) {
									map.put("pubUserHead", user.getUserPortrait());
									map.put("pubUserName", user.getRealName());
								}
							}else {
								map.put("pubUserHead", "");
								map.put("pubUserName", "");
							}
							map.put("pubDate", qz.getAddTime());
						}
					}else if (focusType.equalsIgnoreCase("sjzp")) {
						DriverZp zp = dzps.getEntityById(ufId);
						if(zp != null) {
							map.put("zpId", zp.getId());
							map.put("companyName", zp.getCompany().getName());
							map.put("age", zp.getSjAgeRange());
							map.put("jlYear", zp.getJlYearRange());
							map.put("province", zp.getProvince());
							map.put("jzType", zp.getJzType());
							map.put("city", zp.getCity());
							map.put("education", zp.getEducation());
							map.put("welfare", zp.getWelfare().split(","));
							map.put("wage", zp.getWage());
							map.put("addTime", zp.getAddTime());
							map.put("checkStatus", zp.getCheckStatus());
							map.put("showStatus", zp.getShowStatus());
							Integer userType = zp.getUserType();
							map.put("userType", userType);
							if(userType.equals(2)) {
								User user = uService.getEntityById(zp.getAddUserId());
								if(user != null) {
									map.put("pubUserHead", user.getUserPortrait());
									map.put("pubUserName", user.getRealName());
								}
							}else {
								SuperUser su = ss.getEntityById(zp.getAddUserId());
								map.put("pubUserHead", "");
								map.put("pubUserName", su.getRealName());
							}
							map.put("pubDate", zp.getAddTime());
						}
					}
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, ufs.getTotalElements(), status, list);
	}

	@DeleteMapping("/delUserFocusById")
	@ApiOperation(value = "取消用户关注", notes = "取消用户关注信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50002, message = "数据有误") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "ufId", value = "用户关注编号", required = true) })
	public GenericResponse delUserFocusById(String ufId) {
		ufId = CommonTools.getFinalStr(ufId);
		Integer status = 200;
		try {
			if (ufId.isEmpty()) {
				status = 50002;
			} else {
				ufService.delete(ufId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");

	}
}
