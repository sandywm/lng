package com.lng.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.Company;
import com.lng.pojo.GasFactory;
import com.lng.pojo.GasFactoryCompany;
import com.lng.pojo.LngPriceDetail;
import com.lng.pojo.LngPriceRemark;
import com.lng.pojo.LngPriceSubDetail;
import com.lng.pojo.MessageCenter;
import com.lng.service.GasFactoryCompanyService;
import com.lng.service.GasFactoryService;
import com.lng.service.LngPriceDetailService;
import com.lng.service.LngPriceRemarkService;
import com.lng.service.LngPriceSubDetailService;
import com.lng.service.MessageCenterService;
import com.lng.tools.AutoCreateTable;
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
@Api(tags = "lng管理")
@RequestMapping(value = "/lng")
@SuppressWarnings("unchecked")
public class LngController {

	@Autowired
	private LngPriceDetailService lpds;
	@Autowired
	private GasFactoryService gfs;
	@Autowired
	private GasFactoryCompanyService gfcs;
	@Autowired
	private MessageCenterService mcs;
	@Autowired
	private LngPriceRemarkService lprs;
	@Autowired
	private LngPriceSubDetailService lpsds;
	
	/**
	 * 按照价格降序排序
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月15日 上午8:33:39
	 */
	private class SortClass implements Comparator<Object> {
		@Override
	    public int compare(Object obj0, Object obj1) {
	      Map<String, Integer> map0 = (Map<String, Integer>) obj0;
	      Map<String, Integer> map1 = (Map<String, Integer>) obj1;
	      int flag = map0.get("currPrice").compareTo(map1.get("currPrice"));
	      return -flag; // 不取反，则按正序排列
	    }
	 }
	
	@PostMapping("addInitBatchData")
	@ApiOperation(value = "不用--初始批量增加记录",notes = "初始批量增加记录用--增加完省份(海气、普通)后使用，不在页面体现出来")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	public GenericResponse addInitBatchData(HttpServletRequest request) {
		Integer status = 200;
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.ADD_LNG_PRICE)) {
				String crrentTime = CurrentTime.getCurrentTime();
				List<GasFactory> gfList = gfs.listInfoByOpt("", "", "", "", "", 1);
				List<LngPriceDetail> list = new ArrayList<LngPriceDetail>();
				for(GasFactory gf : gfList) {
					LngPriceDetail lpd = new LngPriceDetail(gf, 0, crrentTime, "", crrentTime);
					list.add(lpd);
				}
				lpds.saveBatch(list);
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	
	@PostMapping("checkRemainFlag")
	@ApiOperation(value = "判断指定日期余下未变动lng行情的记录",
	notes = "判断指定日期余下未变动lng行情的记录。true表示可以批量增加，false表示已有记录，不能批量。如果昨天价格为0-停产，那么复制是就不需要复制")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "priceDate", value = "价格时间(yyyy-mm-dd)")
	})
	public GenericResponse checkRemainFlag(HttpServletRequest request) {
		Integer status = 200;
		String priceDate = CommonTools.getFinalStr("priceDate", request);
		String preDate = CurrentTime.getFinalDate(priceDate, -1);
		List<String> list_add = new ArrayList<String>();
		boolean existFlag = false;
//		String msg = "";
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.ADD_LNG_PRICE)) {
				//获取全部气厂的记录(审核通过)
				List<GasFactory> gfList = gfs.listInfoByOpt("", "", "", "", "", 1);
//				for(GasFactory gf : gfList) {
//					//获取该气厂指定日期有无价格记录
//					if(lpds.listInfoByOpt(gf.getId(), null, priceDate).size() == 0) {//不存在记录
//						existFlag = true;
//						break;
//					}
//				}
				for(GasFactory gf : gfList) {
					//获取该气厂指定日期有无价格记录
					if(lpds.listInfoByOpt(gf.getId(), 0, priceDate).size() == 0) {//不存在当前的记录
						//获取指定日期前一天的记录
						List<LngPriceDetail> lpdList = lpds.listInfoByOpt("", gf.getId(), "", preDate, preDate, "desc");
						if(lpdList.size() > 0) {//存在昨天记录
							if(lpdList.get(0).getPrice() > 0) {//最新记录价格不是0
								list_add.add("ok");
								break;
							}
						}
					}
				}
				if(list_add.size() > 0) {
					existFlag = true;
				}
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, existFlag);
	}
	
	@PostMapping("addRemainBatchData")
	@ApiOperation(value = "批量增加指定日期余下未变动lng行情的记录",notes = "当增加完已变动的价格行情后，点击批量生成未变动的数据")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "priceDate", value = "价格时间(yyyy-mm-dd)")
	})
	public GenericResponse addRemainBatchData(HttpServletRequest request) {
		Integer status = 200;
		String currentTime = CurrentTime.getCurrentTime();
		String priceDate = CommonTools.getFinalStr("priceDate", request);
		String preDate = CurrentTime.getFinalDate(priceDate, -1);
		List<Object> list_add = new ArrayList<Object>();
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.ADD_LNG_PRICE)) {
				//获取全部气厂的记录(审核通过)
				List<GasFactory> gfList = gfs.listInfoByOpt("", "", "", "", "", 1);
				String timeStr = currentTime.substring(10);
				for(GasFactory gf : gfList) {
					Map<String,Object> map_add = new HashMap<String,Object>();
					//获取该气厂指定日期有无价格记录
					if(lpds.listInfoByOpt(gf.getId(), 0, priceDate).size() == 0) {//不存在记录
						//获取指定日期前一天的记录
						List<LngPriceDetail> lpdList = lpds.listInfoByOpt("", gf.getId(), "", preDate, preDate, "desc");
						if(lpdList.size() > 0) {//存在昨天记录
							if(lpdList.get(0).getPrice() > 0) {
								map_add.put("prov", gf.getProvince());
								map_add.put("gasTypeName", gf.getGasType().getName());
								map_add.put("gfName", gf.getName());
								map_add.put("priceTime", priceDate+timeStr);
								LngPriceDetail lpd_pre = lpdList.get(0);
								LngPriceDetail lpd = new LngPriceDetail(gf, lpd_pre.getPrice(), priceDate+timeStr , "", currentTime);
								map_add.put("price", lpd_pre.getPrice());
								list_add.add(map_add);
								String lpdId = lpds.addOrUpdate(lpd);
								if(!lpdId.equals("")) {
									lpsds.saveOrUpdate(new LngPriceSubDetail(lpds.getEntityById(lpdId), lpd_pre.getPrice(), priceDate+timeStr, "",
											currentTime));
									//批量余下时无需增加备注表
								}
							}
						}
					}
				}
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list_add);
	}
	
	@PostMapping("addBatchLngData")
	@ApiOperation(value = "增加单条或者多条lng行情记录",notes = "手动批量增加记录用,也适用于单个增加记录。succList为录入成功的记录，existList之前存在记录录入失败的记录")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号(多个逗号隔开)" , required = true),
		@ApiImplicitParam(name = "price", value = "价格(多个逗号隔开)", required = true),
		@ApiImplicitParam(name = "priceDate", value = "价格时间(yyyy-mm-dd)", required = true),
		@ApiImplicitParam(name = "remark", value = "备注")
	})
	public GenericResponse addBatchLngData(HttpServletRequest request) {
		String gfId = CommonTools.getFinalStr("gfId", request);
		String price = CommonTools.getFinalStr("price", request);
		String priceDate = CommonTools.getFinalStr("priceDate", request);
		String remark = CommonTools.getFinalStr("remark",request);
		Integer status = 200;
		List<Object> list_final = new ArrayList<Object>();
		List<Object> list_succ = new ArrayList<Object>();
		List<Object> list_exist = new ArrayList<Object>();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.ADD_LNG_PRICE)) {
				if(!gfId.equals("") && !price.equals("")) {
					String[] gfIdArr = gfId.split(",");
					String[] priceArr = price.split(",");
					String[] remarkArr = remark.split(",");
					String currentTime = CurrentTime.getCurrentTime();
					String timeStr = currentTime.substring(10);
//					List<LngPriceDetail> list = new ArrayList<LngPriceDetail>();
					for(int i = 0 ; i < gfIdArr.length ; i++) {
						Map<String,Object> map_d = new HashMap<String,Object>();
						GasFactory gf = gfs.getEntityById(gfIdArr[i]);
						Integer lngPrice = 0;
						if(!priceArr[i].equals("")) {
							lngPrice = Integer.parseInt(priceArr[i]);
						}
						String remark_tmp = "";
						if(!remarkArr[i].equals("")) {
							remark_tmp = priceDate+timeStr+" : "+remarkArr[i];
						}
						//获取指定液厂当天的所有价格明细
						List<LngPriceDetail> lpdList = lpds.listInfoByOpt(gfId, 0, priceDate);
						if(lpdList.size() == 0) {//当天没有任何记录
							LngPriceDetail lpd = new LngPriceDetail(gf, lngPrice, priceDate+timeStr, remark_tmp, currentTime);
							//增加价格表
							String lpdId = lpds.addOrUpdate(lpd);
							if(!lpdId.equals("")) {
								//增加价格明细表
								String lpsdId = lpsds.saveOrUpdate(new LngPriceSubDetail(lpds.getEntityById(lpdId), lngPrice, priceDate+timeStr, remark_tmp,
										currentTime));
								if(!lpsdId.equals("") && !remark_tmp.equals("")) {
									//增加备注表
									lprs.saveOrUpdate(new LngPriceRemark(gf, remark_tmp,priceDate+timeStr));
								}
							}
							map_d.put("gfName", gf.getName());
							map_d.put("price", lngPrice);
							map_d.put("priceTime", priceDate+timeStr);
							list_succ.add(map_d);
							//获取昨天最后最后一条记录
							List<LngPriceDetail> lpdList_pre = lpds.listInfoByOpt(gfId, 0, CurrentTime.getFinalDate(priceDate, -1));
							Integer diffPrice = 0;
							if(lpdList_pre.size() > 0) {//存在昨日价格记录
								Integer prePrice = lpdList_pre.get(0).getPrice();
								diffPrice = lngPrice - prePrice;
							}else {
								diffPrice = lngPrice;
							}
							if(lngPrice.equals(0)) {//停产
								MessageCenter mc = new MessageCenter(gf.getName()+"最新燃气价格变动", 
										gf.getName()+"已停产", 0, priceDate+timeStr, 4,
										"", "", "", "", 0);
								String mcsId = mcs.saveOrUpdate(mc);
								MessageCenter mc_1 = mcs.getEntityById(mcsId);
								mc_1.setPrimaryId(gf.getId());
								mcs.saveOrUpdate(mc_1);
							}else {
								MessageCenter mc = null;
								if(diffPrice > 0) {//价格上调变动
									//发送价格变动新闻
									mc = new MessageCenter(gf.getName()+"最新燃气价格变动", 
											gf.getName()+"最新燃气价格"+lngPrice+"元，每吨上调"+diffPrice+"元", 0, priceDate+timeStr, 4,
											"", "", "", "", 0);
								}else if(diffPrice < 0) {//价格下调变动
									mc = new MessageCenter(gf.getName()+"最新燃气价格变动", 
											gf.getName()+"最新燃气价格"+lngPrice+"元，每吨下调"+Math.abs(diffPrice)+"元", 0, priceDate+timeStr, 4,
											"", "", "", "", 0);
								}
								mcs.saveOrUpdate(mc);
								String mcsId = mcs.saveOrUpdate(mc);
								MessageCenter mc_1 = mcs.getEntityById(mcsId);
								mc_1.setPrimaryId(gf.getId());
								mcs.saveOrUpdate(mc_1);
							} 
						}else {//存在记录，获取最近一条记录
							if(lngPrice.equals(lpdList.get(0).getPrice())) {//如果和最近一条记录价格相同，不能添加
								map_d.put("gfName", gf.getName());
								map_d.put("price", lngPrice);
								map_d.put("priceTime", priceDate+timeStr);
								list_exist.add(map_d);
							}else {//如果不相同，可以修改价格表
								LngPriceDetail lpd = lpdList.get(0);
								lpd.setPrice(lngPrice);
								if(!remark_tmp.equals("")) {
									lpd.setRemark(remark_tmp);
								}
								lpd.setPriceTime(priceDate+timeStr);
								lpd.setAddTime(currentTime);
								lpds.addOrUpdate(lpd);
								
								//增加lng价格明细
								String lpsdId = lpsds.saveOrUpdate(new LngPriceSubDetail(lpd, lngPrice, priceDate+timeStr, remark_tmp,
										currentTime));
								if(!lpsdId.equals("") && !remark_tmp.equals("")) {
									//增加备注表
									lprs.saveOrUpdate(new LngPriceRemark(gf,remark_tmp,priceDate+timeStr));
								}
								
								map_d.put("gfName", gf.getName());
								map_d.put("price", lngPrice);
								map_d.put("priceTime", priceDate+timeStr);
								list_succ.add(map_d);
								
								Integer prePrice = lpdList.get(0).getPrice();
								Integer diffPrice = lngPrice - prePrice;
								if(lngPrice.equals(0)) {//停产
									//存在当天的价格变动新闻就修改
									List<MessageCenter> mcList = mcs.listMsgByOpt_1(4, gf.getId(), "", priceDate, priceDate);
									if(mcList.size() == 0) {
										MessageCenter mc = new MessageCenter(gf.getName()+"最新燃气价格变动", 
												gf.getName()+"已停产", 0, priceDate+timeStr, 4,
												"", "", "", "", 0);
										mcs.saveOrUpdate(mc);
										String mcsId = mcs.saveOrUpdate(mc);
										MessageCenter mc_1 = mcs.getEntityById(mcsId);
										mc_1.setPrimaryId(gf.getId());
										mcs.saveOrUpdate(mc_1);
									}else {
										MessageCenter mc_1 = mcList.get(0);
										mc_1.setContent(gf.getName()+"已停产");
										mcs.saveOrUpdate(mc_1);
									}
								}else {
									if(diffPrice > 0) {//价格上调变动
										//发送价格变动新闻
										//存在当天的价格变动新闻就修改
										List<MessageCenter> mcList = mcs.listMsgByOpt_1(4, gf.getId(), "", priceDate, priceDate);
										if(mcList.size() == 0) {
											MessageCenter mc = new MessageCenter(gf.getName()+"最新燃气价格变动", 
													gf.getName()+"最新燃气价格"+lngPrice+"元，每吨上调"+diffPrice+"元", 0, priceDate+timeStr, 4,
													"", "", "", "", 0);
											mcs.saveOrUpdate(mc);
											String mcsId = mcs.saveOrUpdate(mc);
											MessageCenter mc_1 = mcs.getEntityById(mcsId);
											mc_1.setPrimaryId(gf.getId());
											mcs.saveOrUpdate(mc_1);
										}else {
											MessageCenter mc_1 = mcList.get(0);
											mc_1.setContent(gf.getName()+"最新燃气价格"+lngPrice+"元，每吨上调"+diffPrice+"元");
											mcs.saveOrUpdate(mc_1);
										}
									}else if(diffPrice < 0) {//价格下调变动
										List<MessageCenter> mcList = mcs.listMsgByOpt_1(4, gf.getId(), "", priceDate, priceDate);
										if(mcList.size() == 0) {
											MessageCenter mc = new MessageCenter(gf.getName()+"最新燃气价格变动", 
													gf.getName()+"最新燃气价格"+lngPrice+"元，每吨下调"+Math.abs(diffPrice)+"元", 0, priceDate+timeStr, 4,
													"", "", "", "", 0);
											mcs.saveOrUpdate(mc);
											String mcsId = mcs.saveOrUpdate(mc);
											MessageCenter mc_1 = mcs.getEntityById(mcsId);
											mc_1.setPrimaryId(gf.getId());
											mcs.saveOrUpdate(mc_1);
										}else {
											MessageCenter mc_1 = mcList.get(0);
											mc_1.setContent(gf.getName()+"最新燃气价格"+lngPrice+"元，每吨下调"+Math.abs(diffPrice)+"元");
											mcs.saveOrUpdate(mc_1);
										}
									}
								}
							}
						}
					}
//					lpds.saveBatch(list);
				}else {
					status = 10002;
				}
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		map.put("succList", list_succ);
		map.put("existList", list_exist);
		list_final.add(map);
		return ResponseFormat.retParam(status, list_final);
	}
	
	@GetMapping("getSpecLngPriceDetail")
	@ApiOperation(value = "不用--获取指定液厂指定日期的价格明细",notes = "修改记录用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号",required = true),
		@ApiImplicitParam(name = "specDate", value = "指定日期",required = true)
	})
	
	public GenericResponse getSpecLngPriceDetail(HttpServletRequest request) {
		Integer status = 200;
		String gfId = CommonTools.getFinalStr("gfId", request);
		String specDate = CommonTools.getFinalStr("specDate", request);
		String preDate = CurrentTime.getFinalDate(specDate, -1);
		Map<String,Object> map = new HashMap<String,Object>();
		Integer ckPrice = 0;
		String ckDate = "";
		String gfName = "";
		try {
			if(!gfId.equals("") && !specDate.equals("")) {
				List<LngPriceDetail> lpdList = lpds.listInfoByOpt(gfId, 0, specDate);
				Integer count = lpdList.size();
				if(count > 0) {//指定日期有价格
					LngPriceDetail lpd = lpdList.get(0);//获取指定日期最后一条记录
					gfName = lpd.getGf().getName();
					Integer currPrice = lpd.getPrice();
					if(currPrice > 0) {//当前指定日期有价格
						ckPrice = currPrice;
						ckDate = specDate;
					}else {//无价格,获取昨天的价格
						ckDate = preDate;
						List<LngPriceDetail> lpdPreList = lpds.listInfoByOpt(gfId, 0, ckDate);
						if(lpdPreList.size() > 0) {//存在记录
							LngPriceDetail lpdCk = lpdPreList.get(0);//获取参考日期最后一条记录
							ckPrice = lpdCk.getPrice();
							gfName = lpdCk.getGf().getName();
						}else {
							GasFactory gf = gfs.getEntityById(gfId);
							if(gf != null) {
								gfName = gf.getName();
								ckPrice = 0;
							}else {
								status = 50001;
							}
						}
					}
				}else {//指定日期没价格
					ckDate = preDate;
					List<LngPriceDetail> lpdPreList = lpds.listInfoByOpt(gfId, 0, preDate);
					if(lpdPreList.size() > 0) {//存在记录
						LngPriceDetail lpdCk = lpdPreList.get(0);//获取指定日期前一天最后一条记录
						ckPrice = lpdCk.getPrice();
						gfName = lpdCk.getGf().getName();
					}else {
						GasFactory gf = gfs.getEntityById(gfId);
						if(gf != null) {
							gfName = gf.getName();
							ckPrice = 0;
						}else {
							status = 50001;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		map.put("ckDate", ckDate);
		map.put("ckPrice", ckPrice);
		map.put("gfName", gfName);
		map.put("specDate", specDate);
		List<Object> list = new ArrayList<Object>();
		list.add(map);
		return ResponseFormat.retParam(status, list);
	}
	
//	@PutMapping("upSpecLngData")
//	@ApiOperation(value = "不用--修改指定lng行情记录",notes = "修改指定lng行情记录")
//	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
//		@ApiResponse(code = 70001, message = "无权限访问"),
//		@ApiResponse(code = 10002, message = "参数为空"),
//		@ApiResponse(code = 50001, message = "数据未找到")
//	})
//	@ApiImplicitParams({
//		@ApiImplicitParam(name = "lpdId", value = "lng行情编号"),
//		@ApiImplicitParam(name = "price", value = "价格(多个逗号隔开)"),
//		@ApiImplicitParam(name = "priceDate", value = "价格时间(yyyy-mm-dd)"),
//		@ApiImplicitParam(name = "remark", value = "备注")
//	})
//	public GenericResponse upSpecLngData(HttpServletRequest request) {
//		String lpdId = CommonTools.getFinalStr("lpdId", request);
//		Integer price = CommonTools.getFinalInteger("price", request);
//		String remark = CommonTools.getFinalStr("remark");
//		Integer status = 200;
//		try {
//			if(!lpdId.equals("") && price >= 0) {
//				if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.UP_LNG_PRICE)) {
//					LngPriceDetail lpd = lpds.getEntityById(lpdId);
//					if(lpd != null) {
//						lpd.setPrice(price);
//						lpd.setRemark(remark);
//						lpds.addOrUpdate(lpd);
//					}else {
//						status = 50001;
//					}
//				}else {
//					status = 70001;
//				}
//			}else {
//				status = 10002;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			status = 1000;
//		}
//		return ResponseFormat.retParam(status, "");
//	}
	
	@GetMapping("getLngPriceData")
	@ApiOperation(value = "根据省、液厂编号、液厂首字母获取LNG价格行情--后台录入时显示",
	notes = "增加记录用，条件为空时表示全部。价格时间为最近三天（昨天、今天、明天），查询组合--1：省份编号，2：液厂编号，3：液厂名称首字母")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "provPy", value = "省份首字母"),
		@ApiImplicitParam(name = "gsId", value = "液厂编号"),
		@ApiImplicitParam(name = "gsNamePy", value = "液厂名称首字母"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质类型编号"),
		@ApiImplicitParam(name = "priceDate", value = "价格时间(yyyy-mm-dd)")
	})
	public GenericResponse getLngPriceData(HttpServletRequest request) {
		Integer status = 200;
		String provPy = CommonTools.getFinalStr("provPy", request);//有可能是海气，也可能是其他气
		String gsId = CommonTools.getFinalStr("gsId", request);
		String gasTypeId = CommonTools.getFinalStr("gasTypeId", request);
		String gsNamePy = CommonTools.getFinalStr("gsNamePy", request);
		String priceDate = CommonTools.getFinalStr("priceDate", request);
		if(priceDate.equals("")) {
			priceDate = CurrentTime.getStringDate();//默认为当天
		}
		String preDate = CurrentTime.getFinalDate(priceDate,-1);
		String nextDate = CurrentTime.getFinalDate(priceDate,1);
		List<Object> list_d = new ArrayList<Object>();
		long startTime=System.currentTimeMillis();
		try {
			List<GasFactory> gfList = gfs.listInfoByOpt(provPy, gsId, gasTypeId,gsNamePy, 1);
			long midTime=System.currentTimeMillis();
			float diffTime=(float)(midTime-startTime)/1000;
			System.out.println("查询所有液厂耗费时间--"+diffTime);
			if(gfList.size() > 0) {
				for(GasFactory gf : gfList) {
					Map<String,Object> map_d = new HashMap<String,Object>();
					String remark = "";
					String remark_pre = "";
					String remark_curr = "";
					String remark_next = "";
					map_d.put("gfId", gf.getId());
					map_d.put("gfName", gf.getName());
					map_d.put("province", gf.getProvince());
					map_d.put("gfType", gf.getGasType().getName());
					//获取昨天，今天，明天的价格
					Integer prePrice = 0;
					Integer currPrice = 0;
					Integer nextPrice = 0;
					
					List<LngPriceDetail> lpdList = lpds.listInfoByOpt("", gf.getId(), "", preDate, nextDate,"desc");
					if(lpdList.size() > 0) {
						for(LngPriceDetail lpd : lpdList) {
							Integer price = lpd.getPrice();
							if(lpd.getPriceTime().substring(0,10).equals(preDate)) {
								prePrice = price;
								remark_pre = lpd.getRemark();
							}else if(lpd.getPriceTime().substring(0,10).equals(priceDate)) {
								currPrice = price;
								remark_curr = lpd.getRemark();
							}else if(lpd.getPriceTime().substring(0,10).equals(nextDate)) {
								nextPrice = price;
								remark_next = lpd.getRemark();
							}
						}
					}
					map_d.put("prePrice", prePrice);
					map_d.put("currPrice", currPrice);
					map_d.put("nextPrice", nextPrice);
					if(currPrice > 0) {
						map_d.put("diffPrice_curr", currPrice - prePrice);
					}else {
						map_d.put("diffPrice_curr", 0);
					}
					if(nextPrice > 0) {
						map_d.put("diffPrice_next", nextPrice - currPrice);
					}else {
						map_d.put("diffPrice_next", 0);
					}
					if(!remark_next.equals("")) {
						remark = remark_next;
					}else if(!remark_curr.equals("")) {
						remark = remark_curr;
					}else if(!remark_pre.equals("")) {
						remark = remark_pre;
					}
					if(remark.equals("")) {
						List<LngPriceRemark> lprList = lprs.listInfoByGfId(gf.getId());
						if(lprList.size() > 0) {
							remark = lprList.get(0).getRemark();
						}
					}
					map_d.put("preDate", preDate);
					map_d.put("currDate", priceDate);
					map_d.put("nextDate", nextDate);
					map_d.put("remark", remark);
					list_d.add(map_d);
				}
			}else {
				status = 50001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		long endTime=System.currentTimeMillis();
		float excTime=(float)(endTime-startTime)/1000;
		System.out.println("查询所有液厂价格耗费时间--"+excTime);
		return ResponseFormat.retParam(status, list_d);
	}
	
	@GetMapping("getPageLngPriceData")
	@ApiOperation(value = "根据省、液厂首字母、液质类型、时间获取LNG价格行情--前台显示用",
	notes = "条件为空时表示全部。价格时间为最近三天（昨天、今天、明天），查询组合--1：液厂首字母，2：省份，3：液质类型编号，4：时间。先按照hot降序排列，再按照中间天的价格降序排列")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "provPy", value = "省份首字母（可单可组合）"),
		@ApiImplicitParam(name = "gtId", value = "液质类型编号"),
		@ApiImplicitParam(name = "gsNamePy", value = "液厂名称首字母"),
		@ApiImplicitParam(name = "priceDate", value = "价格时间(yyyy-mm-dd)"),
		@ApiImplicitParam(name = "page", value = "页码"),
		@ApiImplicitParam(name = "limit", value = "每页记录条数")
	})
	public PageResponse getPageLngPriceData(HttpServletRequest request) {
		Integer status = 200;
		String provPy = CommonTools.getFinalStr("provPy", request);
		String gtId = CommonTools.getFinalStr("gtId", request);
		String gsNamePy = CommonTools.getFinalStr("gsNamePy", request);
		String priceDate = CommonTools.getFinalStr("priceDate", request);
		if(priceDate.equals("")) {
			priceDate = CurrentTime.getStringDate();//默认为当天
		}
		String preDate = CurrentTime.getFinalDate(priceDate,-1);
		String nextDate = CurrentTime.getFinalDate(priceDate,1);
		Integer pageIndex = CommonTools.getFinalInteger("page", request);
		Integer pageSize = CommonTools.getFinalInteger("limit", request);
		if(pageIndex.equals(0)) {
			pageIndex = 1;
		}
		if(pageSize.equals(0)) {
			pageSize = 20;
		}
		long count = 0;
		List<Object> list_d = new ArrayList<Object>();
		try {
			Page<GasFactory> gfList = gfs.listInfoByOpt(provPy, gtId, gsNamePy, pageIndex, pageSize);
			count = gfList.getTotalElements();
			if(count > 0) {
				for(GasFactory gf : gfList) {
					Map<String,Object> map_d = new HashMap<String,Object>();
					String remark = "";
					String remark_pre = "";
					String remark_curr = "";
					String remark_next = "";
					map_d.put("gfId", gf.getId());
					map_d.put("gfName", gf.getName());
					map_d.put("province", gf.getProvince());
					map_d.put("gfType", gf.getGasType().getName());
					//获取昨天，今天，明天的价格
					Integer prePrice = 0;
					Integer currPrice = 0;
					Integer nextPrice = 0;
					
					List<LngPriceDetail> lpdList = lpds.listInfoByOpt("", gf.getId(), "", preDate, nextDate,"desc");
					if(lpdList.size() > 0) {
						for(LngPriceDetail lpd : lpdList) {
							Integer price = lpd.getPrice();
							if(lpd.getPriceTime().substring(0,10).equals(preDate)) {
								prePrice = price;
								remark_pre = lpd.getRemark();
							}else if(lpd.getPriceTime().substring(0,10).equals(priceDate)) {
								currPrice = price;
								remark_curr = lpd.getRemark();
							}else if(lpd.getPriceTime().substring(0,10).equals(nextDate)) {
								nextPrice = price;
								remark_next = lpd.getRemark();
							}
						}
					}
					map_d.put("prePrice", prePrice);
					map_d.put("currPrice", currPrice);
					map_d.put("nextPrice", nextPrice);
					if(currPrice > 0) {
						map_d.put("diffPrice_curr", currPrice - prePrice);
					}else {
						map_d.put("diffPrice_curr", 0);
					}
					if(nextPrice > 0) {
						map_d.put("diffPrice_next", nextPrice - currPrice);
					}else {
						map_d.put("diffPrice_next", 0);
					}
					if(!remark_next.equals("")) {
						remark = remark_next;
					}else if(!remark_curr.equals("")) {
						remark = remark_curr;
					}else if(!remark_pre.equals("")) {
						remark = remark_pre;
					}
					if(remark.equals("")) {
						List<LngPriceRemark> lprList = lprs.listInfoByGfId(gf.getId());
						if(lprList.size() > 0) {
							remark = lprList.get(0).getRemark();
						}
					}
					map_d.put("preDate", preDate);
					map_d.put("currDate", priceDate);
					map_d.put("nextDate", nextDate);
					map_d.put("remark", remark);
					list_d.add(map_d);
				}
				SortClass sort = new SortClass();
				Collections.sort(list_d, sort);//list_d中currPrice降序排列
			}else {
				status = 50001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize,pageIndex,count,status, list_d);
	}
	
	@GetMapping("getLngPriceDetail")
	@ApiOperation(value = "获取指定LNG价格行情明细--前台用",notes = "获取指定LNG价格行情明细--前台用,默认进来按照当前月的天数汇总")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "gfId"),
		@ApiImplicitParam(name = "specDate", value = "指定日期")
	})
	public GenericResponse getLngPriceDetail(HttpServletRequest request) {
		Integer status = 200;
		String gfId = CommonTools.getFinalStr("gfId", request);
		String specDate = CommonTools.getFinalStr("specDate", request);
		if(specDate.equals("")) {
			specDate = CurrentTime.getStringDate();
		}
		//获取当前日期的第一天和最后一天
		String sYearStr = specDate.substring(0, 4);
		String sMonthStr = specDate.substring(5, 7);
		String sDate = sYearStr + "-" + sMonthStr + "-01";
		String eDate = CurrentTime.getEndDayofMonth(Integer.parseInt(sYearStr), Integer.parseInt(sMonthStr));
		List<Object> list_d = new ArrayList<Object>();
		try {
			GasFactory gf = gfs.getEntityById(gfId);
			if(gf != null) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("gfId", gf.getId());
				map.put("gfName", gf.getName());
				map.put("prov", gf.getProvince());
				map.put("gsType", gf.getGasType().getName());
				String yzbgImg = gf.getYzbgImg();
				if(!yzbgImg.equals("")) {
					map.put("yzbgLength", yzbgImg.split(",").length);
				}else {
					map.put("yzbgLength", 0);
				}
				//获取统计图数据
				List<Object> hpdList = lpds.listTjMonthInfoByGfId(gfId, sDate, eDate);
				List<Object> list_tj = new ArrayList<Object>();
				for(Object obj : hpdList) {
					Object[] sub = (Object[]) obj;
					Map<String,Object> map_tj = new HashMap<String,Object>();
					map_tj.put("priceDate", sub[0].toString().substring(8));
					Integer price = Integer.parseInt(sub[1].toString().substring(0,sub[1].toString().indexOf(".")));
					map_tj.put("price", price);
					list_tj.add(map_tj);
				}
				map.put("specTjDate", sYearStr+"-"+sMonthStr);
				map.put("specDate", specDate);
				map.put("tjList", list_tj);
				//获取指定日期价格
				List<LngPriceDetail> lpdList = lpds.listInfoByOpt(gfId, 0, specDate);
				if(lpdList.size() > 0) {
					map.put("currPrice", lpdList.get(0).getPrice());
				}else {
					map.put("currPrice", 0);
				}
				//获取该液厂下所有的贸易商
				List<GasFactoryCompany> gfcList = gfcs.listCompanyByGfId(gfId, "", 1);
				map.put("cpyLenth", gfcList.size());
				list_d.add(map);
			}else {
				status = 50001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list_d);
	}
	
	@GetMapping("getReportLngPriceDate")
	@ApiOperation(value = "获取指定LNG价格统计图数据--前台用",notes = "获取指定LNG价格统计图数据--前台用(汇总按年、月、日)")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号"),
		@ApiImplicitParam(name = "specTjDate", value = "统计日期-格式(2019或者2019-01或者2019-01-01)")
	})
	public GenericResponse getReportLngPriceDate(HttpServletRequest request) {
		Integer status = 200;
		String gfId = CommonTools.getFinalStr("gfId", request);
		String specTjDate = CommonTools.getFinalStr("specTjDate", request);
		List<Object> list_tj = new ArrayList<Object>();
		if(specTjDate.equals("") || gfId.equals("")) {
			status = 10002;
		}else {
			try {
				//获取统计图数据
				String sDate = "";
				String eDate = "";
				List<Object> hpdList = new ArrayList<Object>();
				if(specTjDate.length() == 4) {//按年汇总
					//specTjDate=2019
					sDate = specTjDate + "-01";
					eDate = specTjDate + "-12";
					hpdList = lpds.listTjYearInfoByGfId(gfId, sDate, eDate);
				}else if(specTjDate.length() == 7) {//按月汇总
					//specTjDate=2019-12
					sDate = specTjDate +"-01";
					String sYearStr = specTjDate.substring(0, 4);
					String sMonthStr = specTjDate.substring(5, 7);
					eDate = CurrentTime.getEndDayofMonth(Integer.parseInt(sYearStr), Integer.parseInt(sMonthStr));
					hpdList = lpds.listTjMonthInfoByGfId(gfId, sDate, eDate);
				}else if(specTjDate.length() == 10) {//按天汇总
					//specTjDate=2019-12-05
					sDate = specTjDate +" 00:00:01";
					eDate = specTjDate +" 23:59:59";
					hpdList = lpds.listTjDaysInfoByGfId(gfId, sDate, eDate);
				}
				if(hpdList.size() > 0) {
					for(Object obj : hpdList) {
						Object[] sub = (Object[]) obj;
						Map<String,Object> map_tj = new HashMap<String,Object>();
						System.out.println(sub[0].toString());
						if(specTjDate.length() == 4){
							map_tj.put("priceDate", sub[0].toString().substring(0,7));//2019-12
						}else if(specTjDate.length() == 7) {
							map_tj.put("priceDate", sub[0].toString().substring(8));//25
						}else if(specTjDate.length() == 10) {
							map_tj.put("priceDate", sub[0].toString().substring(11));//12:35:45
						}
						Integer price = 0;
						Integer length = sub[1].toString().indexOf(".");
						if(length > -1) {
							price = Integer.parseInt(sub[1].toString().substring(0,length));
						}else {
							price = Integer.parseInt(sub[1].toString());
						}
						map_tj.put("price", price);
						list_tj.add(map_tj);
					}
				}else {
					status = 50001;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				status = 1000;
			}
		}
		return ResponseFormat.retParam(status, list_tj);
	}
	
	@GetMapping("getSpecGasFactoryZzImg")
	@ApiOperation(value = "获取指定液厂的资质报告图--前台用",notes = "获取指定液厂的资质报告图--前台用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号",required = true)
	})
	public GenericResponse getSpecGasFactoryZzImg(HttpServletRequest request) {
		Integer status = 200;
		String gfId = CommonTools.getFinalStr("gfId", request);
		List<Object> list = new ArrayList<Object>();
		if(gfId.equals("") ) {
			status = 10002;
		}else {
			try {
				GasFactory gf = gfs.getEntityById(gfId);
				//获取统计图数据
				if(gf != null) {
					String zzImg = gf.getYzbgImg();
					String[] zzImgArr = zzImg.split(",");
					for(int i = 0 ; i < zzImgArr.length ; i++) {
						Map<String,String> map = new HashMap<String,String>();
						map.put("imgPath", zzImgArr[i]);
						list.add(map);
					}
				}else {
					status = 50001;
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				status = 1000;
			}
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("getSpecGasFactoryCpy")
	@ApiOperation(value = "获取指定液厂的贸易商列表--前台用",notes = "获取指定液厂的贸易商列表--前台用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号",required = true),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)",required = true,dataType = "integer")
	})
	public GenericResponse getSpecGasFactoryCpy(HttpServletRequest request) {
		Integer status = 200;
		String gfId = CommonTools.getFinalStr("gfId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		List<Object> list = new ArrayList<Object>();
		if(gfId.equals("") ) {
			status = 10002;
		}else {
			try {
				GasFactory gf = gfs.getEntityById(gfId);
				//获取统计图数据
				if(gf != null) {
					List<GasFactoryCompany> gfcList = gfcs.listCompanyByGfId(gfId, "", checkStatus);
					for(GasFactoryCompany gfc : gfcList) {
						Map<String,Object> map_d = new HashMap<String,Object>();
						Company cpy = gfc.getCompany();
						map_d.put("cpyName", cpy.getName());
						map_d.put("lxName", cpy.getLxName());
						map_d.put("lxTel", cpy.getLxTel());
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
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@GetMapping("getMapData")
	@ApiOperation(value = "获取全国液源地统计",notes = "获取全国液源地统计")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "provName", value = "省份")
	})
	public GenericResponse getMapData(HttpServletRequest request) {
		Integer status = 200;
		String provName = CommonTools.getFinalStr("provName", request);
		List<Object> list = new ArrayList<Object>();
		try {
			if(provName.equals("")) {//全国按照省份统计
				List<Object> list_obj = gfs.getTjInfo();
				for(Object obj : list_obj) {
					Object[] sub = (Object[]) obj;
					Map<String,Object> map_tj = new HashMap<String,Object>();
					map_tj.put("provName", sub[0].toString());
					map_tj.put("gfNum", Integer.parseInt(sub[1].toString()));
					list.add(map_tj);
				}
			}else {
				List<GasFactory> gfList = gfs.listInfoByOpt("", "", "", provName, "", 1);
				String specDate = CurrentTime.getStringDate();
				for(GasFactory gf : gfList) {
					Map<String,Object> map = new HashMap<String,Object>();
					List<LngPriceDetail> lpdList = lpds.listInfoByOpt(gf.getId(), 0, specDate);
					Integer price = 0;
					if(lpdList.size() > 0) {
						price = lpdList.get(0).getPrice();
					}
					map.put("gfId", gf.getId());
					map.put("prov", provName);
					map.put("city", gf.getCity());
					map.put("county", gf.getCounty());
					map.put("price", price);
					map.put("specDate", specDate);
					list.add(map);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
}
