package com.lng.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
public class LngController {

	@Autowired
	private LngPriceDetailService lpds;
	@Autowired
	private GasFactoryService gfs;
	
	@PostMapping("addInitBatchData")
	@ApiOperation(value = "初始批量增加记录",notes = "初始批量增加记录用--增加完省份(海气、普通)后使用，不在页面体现出来")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	public GenericResponse addInitBatchData(HttpServletRequest request) {
		Integer status = 200;
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_LNG_PRICE)) {
				String crrentTime = CurrentTime.getCurrentTime();
				List<GasFactory> gfList = gfs.listInfoByOpt("", "", "", "", "", "", -1);
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
	
	@PostMapping("addRemainBatchData")
	@ApiOperation(value = "批量增加余下未变动的记录",notes = "当增加完已变动的价格行情后，点击批量生成未变动的数据")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	public GenericResponse addRemainBatchData(HttpServletRequest request) {
		Integer status = 200;
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_LNG_PRICE)) {
				String crrentTime = CurrentTime.getCurrentTime();
				List<GasFactory> gfList = gfs.listInfoByOpt("", "", "", "", "", "", -1);
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
	
	@PostMapping("addBatchLngData")
	@ApiOperation(value = "增加记录",notes = "手动批量增加记录用,也适用于单个增加记录。succList为录入成功的记录，existList之前存在记录录入失败的记录")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号(多个逗号隔开)"),
		@ApiImplicitParam(name = "price", value = "价格(多个逗号隔开)"),
		@ApiImplicitParam(name = "priceDate", value = "价格时间(yyyy-mm-dd)")
	})
	public GenericResponse addBatchLngData(HttpServletRequest request) {
		String gfId = CommonTools.getFinalStr("gfId", request);
		String price = CommonTools.getFinalStr("price", request);
		String priceDate = CommonTools.getFinalStr("priceDate", request);
		Integer status = 200;
		List<Object> list_final = new ArrayList<Object>();
		List<Object> list_succ = new ArrayList<Object>();
		List<Object> list_exist = new ArrayList<Object>();
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_LNG_PRICE)) {
				if(!gfId.equals("") && !price.equals("")) {
					String[] gfIdArr = gfId.split(",");
					String[] priceArr = price.split(",");
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
						//同一个液厂同一价格一天只能录入一次
						if(lpds.listInfoByOpt(gfId, lngPrice, priceDate).size() == 0) {
							LngPriceDetail lpd = new LngPriceDetail(gf, lngPrice, priceDate+timeStr, "", currentTime);
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
	
	@GetMapping("getLngPriceData")
	@ApiOperation(value = "根据省、液厂首字母获取LNG价格行情",
	notes = "增加记录用，条件为空时表示全部。价格时间为最近三天（昨天、今天、明天），查询组合--1：省份编号，2：液厂编号，3：液厂名称首字母")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "provPy", value = "省份首字母（单独查询）"),
		@ApiImplicitParam(name = "gsId", value = "液厂编号（单独查询）"),
		@ApiImplicitParam(name = "gsNamePy", value = "液厂名称首字母（单独查询）"),
		@ApiImplicitParam(name = "priceDate", value = "价格时间(yyyy-mm-dd)")
	})
	public GenericResponse getLngPriceData(HttpServletRequest request) {
		Integer status = 200;
		String provPy = CommonTools.getFinalStr("provPy", request);//有可能是海气，也可能是其他气
		String gsId = CommonTools.getFinalStr("gsId", request);
		String gsNamePy = CommonTools.getFinalStr("gsNamePy", request);
		String priceDate = CommonTools.getFinalStr("priceDate", request);
		if(priceDate.equals("")) {
			priceDate = CurrentTime.getStringDate();//默认为当天
		}
		String preDate = CurrentTime.getFinalDate(priceDate,-1);
		String nextDate = CurrentTime.getFinalDate(priceDate,1);
		List<Object> list_d = new ArrayList<Object>();
		try {
			List<GasFactory> gfList = gfs.listInfoByOpt(provPy, gsId, gsNamePy, 1);
			if(gfList.size() > 0) {
				for(GasFactory gf : gfList) {
					Map<String,Object> map_d = new HashMap<String,Object>();
					String remark = "";
					map_d.put("gfId", gf.getId());
					map_d.put("gfName", gf.getName());
					map_d.put("province", gf.getProvince());
					map_d.put("gfType", gf.getGasType().getName());
					//获取昨天，今天，明天的价格
					List<LngPriceDetail> lpdList_pre = lpds.listInfoByOpt("", gf.getId(), "", preDate, preDate);
					if(lpdList_pre.size() > 0) {
						map_d.put("prePrice", lpdList_pre.get(lpdList_pre.size() - 1).getPrice());
					}else {
						map_d.put("prePrice", "");
					}
					List<LngPriceDetail> lpdList_curr = lpds.listInfoByOpt("", gf.getId(), "", priceDate, priceDate);
					if(lpdList_curr.size() > 0) {
						LngPriceDetail lpd = lpdList_curr.get(lpdList_curr.size() - 1);
						map_d.put("currPrice", lpd.getPrice());
						remark = lpd.getRemark();
					}else {
						map_d.put("currPrice", "");
					}
					List<LngPriceDetail> lpdList_next = lpds.listInfoByOpt("", gf.getId(), "", nextDate, nextDate);
					if(lpdList_next.size() > 0) {
						map_d.put("nextPrice", lpdList_next.get(lpdList_next.size() - 1).getPrice());
					}else {
						map_d.put("nextPrice", "");
					}
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
}
