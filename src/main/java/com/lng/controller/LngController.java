package com.lng.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.GasFactory;
import com.lng.pojo.LngPriceDetail;
import com.lng.pojo.SuperDep;
import com.lng.pojo.SuperUser;
import com.lng.service.GasFactoryService;
import com.lng.service.LngPriceDetailService;
import com.lng.service.LngService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.tools.MD5;
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
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号(多个用逗号隔开)", required = true)
	})
	public GenericResponse addInitBatchData(HttpServletRequest request) {
		Integer status = 200;
		String gfId = CommonTools.getFinalStr("gfId", request);
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_LNG_PRICE)) {
				if(!gfId.equals("")) {
					String[] gfIdArr = gfId.split(",");
					for(int i = 0 ; i < gfIdArr.length ; i++) {
						GasFactory gs = gfs.getEntityById(gfIdArr[i]);
//						LngPriceDetail lpd = new LngPriceDetail(gs, 0, CurrentTime.getCurrentTime(), String remark, String addTime);
					}
//					lpds.saveBatch(lpdList);
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
