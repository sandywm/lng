package com.lng.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
	@ApiOperation(value = "初始批量增加记录",notes = "初始批量增加记录用")
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
	
	@PostMapping("addBatchLngData")
	@ApiOperation(value = "增加记录",notes = "手动批量增加记录用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号"),
		@ApiImplicitParam(name = "price", value = "价格"),
		@ApiImplicitParam(name = "priceTime", value = "价格时间")
	})
	public GenericResponse addBatchLngData(HttpServletRequest request) {
		String gfId = CommonTools.getFinalStr("gfId", request);
		String price = CommonTools.getFinalStr("price", request);
		String priceTime = CommonTools.getFinalStr("priceTime", request);
		Integer status = 200;
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_LNG_PRICE)) {
				if(!gfId.equals("") && !price.equals("")) {
					String[] gfIdArr = gfId.split(",");
					String[] priceArr = price.split(",");
					String currentTime = CurrentTime.getCurrentTime();
					List<LngPriceDetail> list = new ArrayList<LngPriceDetail>();
					for(int i = 0 ; i < gfIdArr.length ; i++) {
						GasFactory gf = gfs.getEntityById(gfIdArr[i]);
						Integer lngPrice = 0;
						if(!priceArr[i].equals("")) {
							lngPrice = Integer.parseInt(priceArr[i]);
						}
						LngPriceDetail lpd = new LngPriceDetail(gf, lngPrice, priceTime+currentTime.substring(10), "", currentTime);
						list.add(lpd);
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
		return ResponseFormat.retParam(status, "");
	}
}
