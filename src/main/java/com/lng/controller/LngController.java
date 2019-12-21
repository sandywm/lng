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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.GasFactory;
import com.lng.pojo.LngPriceDetail;
import com.lng.service.GasFactoryService;
import com.lng.service.LngPriceDetailService;
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
	@ApiOperation(value = "初始批量增加记录",notes = "初始批量增加记录用--增加完省份(海气、普通)后使用，不在页面体现出来")
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
	@ApiOperation(value = "判断指定日期余下未变动lng行情的记录",notes = "判断指定日期余下未变动lng行情的记录。true表示可以批量增加，false表示已有记录，不能批量")
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
					if(lpds.listInfoByOpt(gf.getId(), null, priceDate).size() == 0) {//不存在记录
						//获取指定日期前一天的记录
						List<LngPriceDetail> lpdList = lpds.listInfoByOpt("", gf.getId(), "", preDate, preDate, "desc");
						if(lpdList.size() > 0) {//存在昨天记录
							list_add.add("ok");
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
				List<LngPriceDetail> list = new ArrayList<LngPriceDetail>();
				String timeStr = currentTime.substring(10);
				for(GasFactory gf : gfList) {
					Map<String,Object> map_add = new HashMap<String,Object>();
					//获取该气厂指定日期有无价格记录
					if(lpds.listInfoByOpt(gf.getId(), null, priceDate).size() == 0) {//不存在记录
						//获取指定日期前一天的记录
						List<LngPriceDetail> lpdList = lpds.listInfoByOpt("", gf.getId(), "", preDate, preDate, "desc");
						if(lpdList.size() > 0) {//存在昨天记录
							map_add.put("prov", gf.getProvince());
							map_add.put("gasTypeName", gf.getGasType().getName());
							map_add.put("gfName", gf.getName());
							map_add.put("priceTime", priceDate+timeStr);
							LngPriceDetail lpd_pre = lpdList.get(0);
							LngPriceDetail lpd = new LngPriceDetail(gf, lpd_pre.getPrice(), priceDate+timeStr , "", currentTime);
							map_add.put("price", lpd_pre.getPrice());
							list.add(lpd);
							list_add.add(map_add);
						}else {
//							LngPriceDetail lpd = new LngPriceDetail(gf, 0, priceDate+timeStr , "", currentTime);
//							map_add.put("price", 0);
//							list.add(lpd);
						}
					}
				}
				if(list.size() > 0) {
					lpds.saveBatch(list);
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
					List<LngPriceDetail> list = new ArrayList<LngPriceDetail>();
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
						//同一个液厂同一价格一天只能录入一次
						if(lpds.listInfoByOpt(gfId, lngPrice, priceDate).size() == 0) {
							LngPriceDetail lpd = new LngPriceDetail(gf, lngPrice, priceDate+timeStr, remark_tmp, currentTime);
							list.add(lpd);
							map_d.put("gfName", gf.getName());
							map_d.put("price", lngPrice);
							map_d.put("priceTime", priceDate+timeStr);
							list_succ.add(map_d);
						}else {
							map_d.put("gfName", gf.getName());
							map_d.put("price", lngPrice);
							map_d.put("priceTime", priceDate+timeStr);
							list_exist.add(map_d);
						}
					}
					lpds.saveBatch(list);
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
	@ApiOperation(value = "获取指定液厂指定日期的价格明细",notes = "修改记录用")
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
				List<LngPriceDetail> lpdList = lpds.listInfoByOpt(gfId, null, specDate);
				Integer count = lpdList.size();
				if(count > 0) {//指定日期有价格
					LngPriceDetail lpd = lpdList.get(count - 1);//获取指定日期最后一条记录
					gfName = lpd.getGf().getName();
					Integer currPrice = lpd.getPrice();
					if(currPrice > 0) {//当前指定日期有价格
						ckPrice = currPrice;
						ckDate = specDate;
//						if(count > 1) {//当天存在多个价格变动，获取上一次的价格为参考
//							LngPriceDetail lpdCk = lpdList.get(count - 2);
//							ckPrice = lpdCk.getPrice();
//							ckDate = lpdCk.getPriceTime().substring(0, 10);
//						}else {//当前只存在一个价格
//							//按照昨天最后一次的价格为主
//							ckDate = preDate;
//							List<LngPriceDetail> lpdPreList = lpds.listInfoByOpt(gfId, null, ckDate);
//							if(lpdPreList.size() > 0) {//存在记录
//								ckPrice = lpdPreList.get(lpdPreList.size() - 1).getPrice();
//							}else {
//								ckPrice = 0;
//							}
//						}
					}else {//无价格,获取昨天的价格
						ckDate = preDate;
						List<LngPriceDetail> lpdPreList = lpds.listInfoByOpt(gfId, null, ckDate);
						if(lpdPreList.size() > 0) {//存在记录
							LngPriceDetail lpdCk = lpdPreList.get(lpdPreList.size() - 1);
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
					List<LngPriceDetail> lpdPreList = lpds.listInfoByOpt(gfId, null, preDate);
					if(lpdPreList.size() > 0) {//存在记录
						LngPriceDetail lpdCk = lpdPreList.get(lpdPreList.size() - 1);
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
	
	@GetMapping("getLngPriceDetail")
	@ApiOperation(value = "获取指定LNG价格行情",notes = "修改记录用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "lpdId", value = "lng行情编号")
	})
	public GenericResponse getLngPriceDetail(HttpServletRequest request) {
		Integer status = 200;
		String lpdId = CommonTools.getFinalStr("lpdId", request);
		List<Object> list_d = new ArrayList<Object>();
		try {
			LngPriceDetail lpd = lpds.getEntityById(lpdId);
			if(lpd != null) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("lpdId", lpd.getId());
				map.put("gfName", lpd.getGf().getName());
				map.put("price", lpd.getPrice());
				map.put("priceTime", lpd.getPriceTime());
				map.put("remark", lpd.getRemark());
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
	
	@PutMapping("upSpecLngData")
	@ApiOperation(value = "修改指定lng行情记录",notes = "修改指定lng行情记录")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "lpdId", value = "lng行情编号"),
		@ApiImplicitParam(name = "price", value = "价格(多个逗号隔开)"),
		@ApiImplicitParam(name = "priceDate", value = "价格时间(yyyy-mm-dd)"),
		@ApiImplicitParam(name = "remark", value = "备注")
	})
	public GenericResponse upSpecLngData(HttpServletRequest request) {
		String lpdId = CommonTools.getFinalStr("lpdId", request);
		Integer price = CommonTools.getFinalInteger("price", request);
		String remark = CommonTools.getFinalStr("remark");
		Integer status = 200;
		try {
			if(!lpdId.equals("") && price >= 0) {
				if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.UP_LNG_PRICE)) {
					LngPriceDetail lpd = lpds.getEntityById(lpdId);
					if(lpd != null) {
						lpd.setPrice(price);
						lpd.setRemark(remark);
						lpds.addOrUpdate(lpd);
					}else {
						status = 50001;
					}
				}else {
					status = 70001;
				}
			}else {
				status = 10002;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
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
		try {
			List<GasFactory> gfList = gfs.listInfoByOpt(provPy, gsId, gasTypeId,gsNamePy, 1);
			if(gfList.size() > 0) {
				for(GasFactory gf : gfList) {
					Map<String,Object> map_d = new HashMap<String,Object>();
					String remark = "";
					map_d.put("gfId", gf.getId());
					map_d.put("gfName", gf.getName());
					map_d.put("province", gf.getProvince());
					map_d.put("gfType", gf.getGasType().getName());
					//获取昨天，今天，明天的价格
					Integer prePrice = 0;
					Integer currPrice = 0;
					Integer nextPrice = 0;
					
					List<LngPriceDetail> lpdList_pre = lpds.listInfoByOpt("", gf.getId(), "", preDate, preDate,"desc");
					if(lpdList_pre.size() > 0) {
						prePrice = lpdList_pre.get(0).getPrice();
						map_d.put("prePrice", prePrice);
					}else {
						map_d.put("prePrice", prePrice);
					}
					map_d.put("preDate", preDate);
					List<LngPriceDetail> lpdList_curr = lpds.listInfoByOpt("", gf.getId(), "", priceDate, priceDate,"desc");
					if(lpdList_curr.size() > 0) {
						LngPriceDetail lpd = lpdList_curr.get(0);
						currPrice = lpd.getPrice();
						map_d.put("currPrice", currPrice);
						if(currPrice > 0) {
							map_d.put("diffPrice_curr", currPrice - prePrice);
						}else {
							map_d.put("diffPrice_curr", 0);
						}
					}else {
						map_d.put("currPrice", currPrice);
						map_d.put("diffPrice_curr", 0);
					}
					map_d.put("currDate", priceDate);
					List<LngPriceDetail> lpdList_next = lpds.listInfoByOpt("", gf.getId(), "", nextDate, nextDate,"desc");
					if(lpdList_next.size() > 0) {
						nextPrice = lpdList_next.get(0).getPrice();
						map_d.put("nextPrice", nextPrice);
						if(nextPrice > 0) {
							map_d.put("diffPrice_next", nextPrice - currPrice);
						}else {
							map_d.put("diffPrice_next", 0);
						}
					}else {
						map_d.put("nextPrice", nextPrice);
						map_d.put("diffPrice_next", 0);
					}
					List<LngPriceDetail> lpdList_all = lpds.listInfoByOpt("", gf.getId(), "", "", "","desc");
					for(LngPriceDetail lpd : lpdList_all) {
						String remark_tmp = lpd.getRemark();
						if(!remark_tmp.equals("")) {
							remark = remark_tmp;
							break;
						}
					}
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
					map_d.put("gfId", gf.getId());
					map_d.put("gfName", gf.getName());
					map_d.put("province", gf.getProvince());
					map_d.put("gfType", gf.getGasType().getName());
					//获取昨天，今天，明天的价格
					Integer prePrice = 0;
					Integer currPrice = 0;
					Integer nextPrice = 0;
					List<LngPriceDetail> lpdList_pre = lpds.listInfoByOpt("", gf.getId(), "", preDate, preDate,"desc");
					if(lpdList_pre.size() > 0) {
						prePrice = lpdList_pre.get(0).getPrice();
					}
					map_d.put("prePrice", prePrice);
					List<LngPriceDetail> lpdList_curr = lpds.listInfoByOpt("", gf.getId(), "", priceDate, priceDate,"desc");
					if(lpdList_curr.size() > 0) {
						currPrice = lpdList_curr.get(0).getPrice();
						map_d.put("diffPrice_curr", currPrice - prePrice);
						map_d.put("priceTime", lpdList_curr.get(0).getPriceTime());
					}else {
						map_d.put("diffPrice_curr", 0);
						map_d.put("priceTime", "");
					}
					map_d.put("currPrice", currPrice);
					List<LngPriceDetail> lpdList_next = lpds.listInfoByOpt("", gf.getId(), "", nextDate, nextDate,"desc");
					if(lpdList_next.size() > 0) {
						nextPrice = lpdList_next.get(0).getPrice();
						if(nextPrice > 0) {
							map_d.put("diffPrice_next", nextPrice - currPrice);
						}else {
							map_d.put("diffPrice_next", 0);
						}
					}else {
						map_d.put("diffPrice_next", 0);
					}
					map_d.put("nextPrice", nextPrice);
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
	
	@GetMapping("getPageLngMesasageData")
	@ApiOperation(value = "获取lng行情留言",
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
	public PageResponse getPageLngMesasageData(HttpServletRequest request) {
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
					map_d.put("gfId", gf.getId());
					map_d.put("gfName", gf.getName());
					map_d.put("province", gf.getProvince());
					map_d.put("gfType", gf.getGasType().getName());
					//获取昨天，今天，明天的价格
					Integer prePrice = 0;
					Integer currPrice = 0;
					Integer nextPrice = 0;
					List<LngPriceDetail> lpdList_pre = lpds.listInfoByOpt("", gf.getId(), "", preDate, preDate,"desc");
					if(lpdList_pre.size() > 0) {
						prePrice = lpdList_pre.get(0).getPrice();
					}
					map_d.put("prePrice", prePrice);
					List<LngPriceDetail> lpdList_curr = lpds.listInfoByOpt("", gf.getId(), "", priceDate, priceDate,"desc");
					if(lpdList_curr.size() > 0) {
						currPrice = lpdList_curr.get(0).getPrice();
						map_d.put("diffPrice_curr", currPrice - prePrice);
						map_d.put("priceTime", lpdList_curr.get(0).getPriceTime());
					}else {
						map_d.put("priceTime", "");
					}
					map_d.put("currPrice", currPrice);
					List<LngPriceDetail> lpdList_next = lpds.listInfoByOpt("", gf.getId(), "", nextDate, nextDate,"desc");
					if(lpdList_next.size() > 0) {
						nextPrice = lpdList_next.get(0).getPrice();
						map_d.put("diffPrice_next", nextPrice - currPrice);
					}
					map_d.put("nextPrice", nextPrice);
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
}
