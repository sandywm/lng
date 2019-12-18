package com.lng.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.GasFactory;
import com.lng.pojo.GasTrade;
import com.lng.pojo.LngPriceDetail;
import com.lng.service.GasTradeService;
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
@Api(tags = "燃气买卖管理")
@RequestMapping(value = "/gasTrade")
public class GasTradeController {

	@Autowired
	private GasTradeService gts;
	
	@PostMapping("addGasTrade")
	@ApiOperation(value = "增加燃气买卖记录",notes = "发布燃气买卖记录")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cpyId", value = "公司编号)" , required = true),
		@ApiImplicitParam(name = "headImg", value = "主图"),
		@ApiImplicitParam(name = "gtId", value = "液质编号", required = true),
		@ApiImplicitParam(name = "gfId", value = "液厂编号", required = true),
		@ApiImplicitParam(name = "gasVolume", value = "装载量（吨）", required = true),
		@ApiImplicitParam(name = "gasPrice", value = "单价（吨）"),
		@ApiImplicitParam(name = "zcDate", value = "装车日期"),
		@ApiImplicitParam(name = "psArea", value = "配送区域"),
		@ApiImplicitParam(name = "lxName", value = "联系人", required = true),
		@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true),
		@ApiImplicitParam(name = "addUserId", value = "上传人"),
		@ApiImplicitParam(name = "cpNo", value = "槽车车牌号"),
		@ApiImplicitParam(name = "jsyName", value = "驾驶员姓名"),
		@ApiImplicitParam(name = "jsyMobile", value = "驾驶员电话"),
		@ApiImplicitParam(name = "yyrName", value = "押运员姓名"),
		@ApiImplicitParam(name = "yyrMobile", value = "押运员电话"),
		@ApiImplicitParam(name = "qfTxt1", value = "铅封文字信息一"),
		@ApiImplicitParam(name = "qfTxt2", value = "铅封文字信息二"),
		@ApiImplicitParam(name = "qfTxt3", value = "铅封文字信息三"),
		@ApiImplicitParam(name = "qfImg1", value = "铅封图片信息一"),
		@ApiImplicitParam(name = "qfImg2", value = "铅封图片信息二"),
		@ApiImplicitParam(name = "qfImg3", value = "铅封图片信息三"),
		@ApiImplicitParam(name = "remark", value = "备注"),
		@ApiImplicitParam(name = "gpsInfo", value = "gps信息"),
		@ApiImplicitParam(name = "bdImg", value = "磅单图片"),
		@ApiImplicitParam(name = "whpImg", value = "危化品许可证"),
		@ApiImplicitParam(name = "tructsImg", value = "车辆照片"),
		@ApiImplicitParam(name = "tradeOrderId", value = "确认订单编号")
	})
	public GenericResponse addGasTrade(HttpServletRequest request) {
		Integer status = 200;
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer userType = 1;
		Integer checkStatus = 0;
		String currTime = CurrentTime.getCurrentTime();
		String userId = CommonTools.getLoginUserId(request);
		try {
			if(CommonTools.checkAuthorization(userId, CommonTools.getLoginRoleName(request),Constants.ADD_GAS_TRADE)) {
				
			}else if(cilentInfo.equals("wxApp")){
				userType = 2;
				userId = CommonTools.getFinalStr("userId", request);
			}else {
				status = 70001;
			}
			if(status.equals(200)) {
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
	
	@GetMapping("getPageGasTradeList")
	@ApiOperation(value = "根据条件分页获取燃气买卖记录",notes = "根据条件分页获取燃气买卖记录")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "cpyId", value = "公司编号)"),
		@ApiImplicitParam(name = "addUserId", value = "添加人员"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质编号"),
		@ApiImplicitParam(name = "gfId", value = "液厂编号"),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态(-1:全部,0:未审核,1:审核通过,2:审核未通过)",dataType = "integer"),
		@ApiImplicitParam(name = "pageNo", value = "页码",dataType = "integer"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录条数",dataType = "integer"),
	})
	public GenericResponse getPageGasTradeList(HttpServletRequest request) {
		Integer status = 200;
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer userType = 1;
		String currTime = CurrentTime.getCurrentTime();
		String cpyId = CommonTools.getFinalStr("cpyId", request);
		String addUserId = CommonTools.getFinalStr("addUserId", request);//有可能是海气，也可能是其他气
		String gasTypeId = CommonTools.getFinalStr("gasTypeId", request);
		String gfId = CommonTools.getFinalStr("gfId", request);
		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
		Integer pageNo = CommonTools.getFinalInteger("pageNo", request);
		Integer pageSize = CommonTools.getFinalInteger("pageSize", request);
		long count = 0;
		try {
			Page<GasTrade> gtList = gts.listPageInfoByOpt(cpyId, addUserId, gasTypeId, gfId, checkStatus, pageNo, pageSize);
			count = gtList.getTotalElements();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
}
