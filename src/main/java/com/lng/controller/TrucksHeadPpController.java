package com.lng.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.TrucksHeadPp;
import com.lng.service.TrucksHeadPpService;
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
@Api(tags = "槽车车头品牌相关接口")
@RequestMapping("/tHeadPp")
public class TrucksHeadPpController {
	@Autowired
	private TrucksHeadPpService thppService;

	@PostMapping("/addTHeadPp")
	@ApiOperation(value = "添加槽车车头品牌", notes = "添加槽车车头品牌信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "槽车车头品牌名称", defaultValue = "槽车车头品牌测试", required = true) })
	public GenericResponse addTHeadPp(HttpServletRequest request, String name) {
		Integer status = 200;
		String thppId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.CCCTPP_ABILITY)) {
			try {
				if (thppService.getTrucksHeadPpByNameList(name).size() == 0) {
					TrucksHeadPp thpp = new TrucksHeadPp();
					thpp.setName(CommonTools.getFinalStr(name));
					thpp.setAddTime(CurrentTime.getCurrentTime());
					thppId = thppService.saveOrUpdate(thpp);
				} else {
					status = 50003;
				}

			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, thppId);
	}

	@PutMapping("/updateTHeadPp")
	@ApiOperation(value = "修改槽车车头品牌", notes = "修改槽车车头品牌信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "槽车车头品牌编号", required = true),
			@ApiImplicitParam(name = "name", value = "槽车车头品牌名称", defaultValue = "槽车车头品牌测试", required = true) })
	public GenericResponse updateTHeadPp(HttpServletRequest request, String id, String name) {
		id = CommonTools.getFinalStr(id);
		name=CommonTools.getFinalStr(name);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),CommonTools.getLoginRoleName(request), Constants.CCCTPP_ABILITY)) {
			try {
				TrucksHeadPp thpp = thppService.findById(id);
				if (thpp== null) {
					status = 50001;
				} else {
					if(!name.equals("") && !name.equals(thpp.getName())) {
						if (thppService.getTrucksHeadPpByNameList(name).size() == 0) {
							thpp.setName(name);
							thppService.saveOrUpdate(thpp);
						} else {
							status = 50003;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		} else {
			status = 70001;
		}
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/queryTHeadPp")
	@ApiOperation(value = "获取槽车车头品牌", notes = "获取槽车车头品牌信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "槽车车头品牌编号") })
	public GenericResponse queryTHeadPp(String id) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		List<TrucksHeadPp> thppList = new ArrayList<TrucksHeadPp>();
		try {
			if (id.equals("")) {
				thppList = thppService.getTrucksHeadPpByNameList("");
			} else {
				TrucksHeadPp thpp = thppService.findById(id);
				if (thpp == null) {
					status = 50001;
				} else {
					thppList.add(thpp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, thppList);
	}

}
