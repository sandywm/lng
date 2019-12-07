package com.lng.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.GasFactory;
import com.lng.pojo.GasType;
import com.lng.pojo.SuperDep;
import com.lng.pojo.SuperUser;
import com.lng.service.GasFactoryService;
import com.lng.service.GasTypeService;
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
@Api(tags = "液厂用户管理")
@RequestMapping(value = "/gsf")
public class GasFactoryController {

	@Autowired
	private GasFactoryService gfs;
	@Autowired
	private GasTypeService gts;
	
	@PostMapping("addGasFactory")
	@ApiOperation(value = "增加液厂",notes = "增加液厂接口信息")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "液厂名称", required = true),
		@ApiImplicitParam(name = "facImage", value = "液厂图片"),
		@ApiImplicitParam(name = "realName", value = "用户姓名", required = true),
		@ApiImplicitParam(name = "gasTypeId", value = "液质类型编号", required = true),
		@ApiImplicitParam(name = "province", value = "省", required = true),
		@ApiImplicitParam(name = "city", value = "市", required = true),
		@ApiImplicitParam(name = "county", value = "县", required = true),
		@ApiImplicitParam(name = "address", value = "详细地址", required = true),
		@ApiImplicitParam(name = "lxName", value = "联系人", required = true),
		@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true),
		@ApiImplicitParam(name = "yzbgImg", value = "液质报告图", required = true),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态", dataType = "Long",required = true),
		@ApiImplicitParam(name = "checkTime", value = "审核时间"),
		@ApiImplicitParam(name = "owerUserId", value = "液厂所属人员编号")
	})
	public GenericResponse addGasFactory(HttpServletRequest request,String name,String facImage,String realName,String gasTypeId,
			String province,String city,String county,String address,String lxName,String lxTel,String yzbgImg,Integer checkStatus,
			String checkTime,String owerUserId) {
		Integer status = 200;
		String uId = "";
		String userId = CommonTools.getLoginUserId(request);
		try {
			if(CommonTools.checkAuthorization(userId, Constants.ADD_YC)) {
//				String namePy = 
//				GasFactory gf = new GasFactory(gts.findById(gasTypeId), name, String namePy, facImage, province, city,
//						county, address, lxName, lxTel, CurrentTime.getCurrentTime(), yzbgImg, checkStatus,
//						checkTime,userId);
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, uId);
	}
}
