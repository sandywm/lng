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

import com.lng.pojo.CommonProvinceOrder;
import com.lng.pojo.GasFactory;
import com.lng.pojo.GasType;
import com.lng.pojo.HqProvinceOrder;
import com.lng.service.CommonProvinceOrderService;
import com.lng.service.GasFactoryService;
import com.lng.service.GasTypeService;
import com.lng.service.HqProvinceOrderService;
import com.lng.tools.CommonTools;
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
	
	@GetMapping("/getProvinceList")
	@ApiOperation(value = "获取省份排序列表", notes = "lng液厂显示顺序用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
		@ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "gsType", value = "液质类型(0:其他,1:海气)", dataType = "integer",required = true)})
	public GenericResponse getProvinceList(Integer gsType) {
		Integer status = 200;
		List<CommonProvinceOrder> cpList = new ArrayList<CommonProvinceOrder>();
		List<HqProvinceOrder> hpList = new ArrayList<HqProvinceOrder>();
		try {
			if(gsType.equals(0)) {
				cpList = cpos.listAllInfo("asc");
			}else {
				hpList = hpos.listAllInfo("asc");
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		if(gsType.equals(0)) {
			return ResponseFormat.retParam(status, cpList);
		}else {
			return ResponseFormat.retParam(status, hpList);
		}
	}
	
	@PostMapping("/addProvince")
	@ApiOperation(value = "增加省份排序列表", notes = "lng液厂显示顺序用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
		@ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "province", value = "省份",required = true),
		@ApiImplicitParam(name = "gsType", value = "液质类型(0:其他,1:海气)", dataType = "integer",required = true)
	})
	public GenericResponse addProvince(HttpServletRequest request,String province,Integer gsType) {
		Integer status = 200;
		Integer id = 0;
		List<CommonProvinceOrder> cpList = new ArrayList<CommonProvinceOrder>();
		List<HqProvinceOrder> hpList = new ArrayList<HqProvinceOrder>();
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_YC)) {
				if(gsType.equals(0)) {
					cpList = cpos.listAllInfo("asc");
					boolean flag = false;
					Integer orderNo = cpList.size() + 1;
					if(cpList.size() == 0) {
						flag = true;
					}else {
						//先判断有无重复
						if(cpos.getEntityByOpt(0, province) == null) {
							flag = true;
						}
					}
					if(flag) {
						id = cpos.saveOrUpdate(new CommonProvinceOrder(province,orderNo));
					}else {
						status = 50003;
					}
				}else {
					hpList = hpos.listAllInfo("asc");
					boolean flag = false;
					Integer orderNo = hpList.size() + 100000;
					if(hpList.size() == 0) {
						flag = true;
					}else {
						//先判断有无重复
						if(hpos.getEntityByOpt(0, province) == null) {
							flag = true;
						}
					}
					if(flag) {
						id = hpos.saveOrUpdate(new HqProvinceOrder(province,orderNo));
					}else {
						status = 50003;
					}
				}
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, id);
	}
	
	@PutMapping("/setProvOrder")
	@ApiOperation(value = "修改省份排序列表", notes = "lng液厂显示顺序用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
		@ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 70001, message = "无权限访问"),
		@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "proIdStr", value = "省份ID组合",required = true),
		@ApiImplicitParam(name = "gsType", value = "液质类型(0:其他,1:海气)", dataType = "integer",required = true)
	})
	public GenericResponse setProvOrder(HttpServletRequest request,String proIdStr,Integer gsType) {
		Integer status = 200;
		try {
			if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), Constants.ADD_YC)) {
				if(proIdStr == null || proIdStr == "") {
					status = 10002;
				}else {
					String[] provIdArr = proIdStr.split(",");
					if(gsType.equals(0)) {
						for(int i = 0 ; i < provIdArr.length ; i++) {
							Integer provId = Integer.parseInt(provIdArr[i]);
							CommonProvinceOrder cpo = cpos.getEntityByOpt(provId, "");
							if(cpo != null) {
								cpo.setOrderNo(i);
								cpos.saveOrUpdate(cpo);
								//批量修改普通类型的液厂
								Integer orderNo = cpo.getOrderNo();
								List<GasFactory>  gfList = gfs.listInfoByOpt("", "", "", cpo.getProvince(), "", "", -1);
								for(GasFactory gf : gfList) {
									if(gf.getGasType().getName().equals("海气")) {
										continue;
									}else {
										gf.setOrderNo(orderNo);
										gfs.addOrUpGasFactory(gf);
									}
								}
							}
						}
					}else {
						for(int i = 0 ; i < provIdArr.length ; i++) {
							Integer provId = Integer.parseInt(provIdArr[i]);
							HqProvinceOrder hpo = hpos.getEntityByOpt(provId, "");
							if(hpo != null) {
								hpo.setOrderNo(i);
								hpos.saveOrUpdate(hpo);
								//批量修改海气类型的液厂
								Integer orderNo = hpo.getOrderNo();
								//获取海气的编号
								List<GasType> gtList = gts.getGasTypeByNameList("海气");
								String gtId = "";
								if(gtList.size() > 0) {
									gtId = gtList.get(0).getId();
									List<GasFactory>  gfList = gfs.listInfoByOpt("", "", gtId, hpo.getProvince(), "", "", -1);
									for(GasFactory gf : gfList) {
										gf.setOrderNo(orderNo);
										gfs.addOrUpGasFactory(gf);
									}
								}
							}
						}
					}
				}
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
}
