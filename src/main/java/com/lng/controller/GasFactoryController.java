package com.lng.controller;

import java.util.ArrayList;
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
import com.lng.service.GasFactoryService;
import com.lng.service.GasTypeService;
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
@Api(tags = "液厂管理")
@RequestMapping(value = "/gsf")
public class GasFactoryController {

	@Autowired
	private GasFactoryService gfs;
	@Autowired
	private GasTypeService gts;
	
	
	@PostMapping("addGasFactory")
	@ApiOperation(value = "增加液厂--后台人员",notes = "后台人员增加液厂信息时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "液厂名称", required = true),
		@ApiImplicitParam(name = "facImage", value = "液厂图片"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质类型编号", required = true),
		@ApiImplicitParam(name = "province", value = "省", required = true),
		@ApiImplicitParam(name = "city", value = "市", required = true),
		@ApiImplicitParam(name = "county", value = "县", required = true),
		@ApiImplicitParam(name = "address", value = "详细地址", required = true),
		@ApiImplicitParam(name = "lxName", value = "联系人", required = true),
		@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true),
		@ApiImplicitParam(name = "yzbgImg", value = "液质报告图", required = true)
	})
	public GenericResponse addGasFactory(HttpServletRequest request,String name,String facImage,String gasTypeId,
			String province,String city,String county,String address,String lxName,String lxTel,String yzbgImg) {
		Integer status = 200;
		String uId = "";
		String userId = CommonTools.getLoginUserId(request);
		try {
			if(CommonTools.checkAuthorization(userId, Constants.ADD_YC)) {
				if(gfs.listInfoByOpt(name, "", "", "", "", "", -1).size() == 0) {
					String namePy = CommonTools.getFirstSpell(name);
					GasFactory gf = new GasFactory(gts.findById(gasTypeId), name, namePy, facImage, province, city,
							county, address, lxName, lxTel, CurrentTime.getCurrentTime(), yzbgImg, 1,
							CurrentTime.getCurrentTime(),userId);
					uId = gfs.addOrUpGasFactory(gf);
				}else {
					status = 50003;
				}
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
	
	@PutMapping("upGasFactory")
	@ApiOperation(value = "修改液厂--后台人员",notes = "后台人员修改液厂信息时使用")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到"),
		@ApiResponse(code = 50003, message = "数据已存在"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号", required = true),
		@ApiImplicitParam(name = "name", value = "液厂名称", required = true),
		@ApiImplicitParam(name = "facImage", value = "液厂图片", required = true),
		@ApiImplicitParam(name = "gasTypeId", value = "液质类型编号", required = true),
		@ApiImplicitParam(name = "province", value = "省", required = true),
		@ApiImplicitParam(name = "city", value = "市", required = true),
		@ApiImplicitParam(name = "county", value = "县", required = true),
		@ApiImplicitParam(name = "address", value = "详细地址", required = true),
		@ApiImplicitParam(name = "lxName", value = "联系人", required = true),
		@ApiImplicitParam(name = "lxTel", value = "联系电话", required = true),
		@ApiImplicitParam(name = "yzbgImg", value = "液质报告图")
	})
	public GenericResponse upGasFactory(HttpServletRequest request,String gfId,String name,String facImage,String gasTypeId,
			String province,String city,String county,String address,String lxName,String lxTel,String yzbgImg) {
		Integer status = 200;
		String uId = "";
		String userId = CommonTools.getLoginUserId(request);
		try {
			if(CommonTools.checkAuthorization(userId, Constants.ADD_YC)) {
				GasFactory gf = gfs.getEntityById(gfId);
				if(gf != null) {
					name = CommonTools.getFinalStr(name);
					facImage = CommonTools.getFinalStr(facImage);
					gasTypeId = CommonTools.getFinalStr(gasTypeId);
					province = CommonTools.getFinalStr(province);
					city = CommonTools.getFinalStr(city);
					county = CommonTools.getFinalStr(county);
					address = CommonTools.getFinalStr(address);
					lxName = CommonTools.getFinalStr(lxName);
					lxTel = CommonTools.getFinalStr(lxTel);
					yzbgImg = CommonTools.getFinalStr(yzbgImg);
					if(!name.equals("")) {
						if(!name.equals(gf.getName())) {
							//需要检查是否重复
							if(gfs.listInfoByOpt(name, "", "", "", "", "", -1).size() == 0) {
								gf.setName(name);
								gf.setNamePy(CommonTools.getFirstSpell(name));
							}else {
								status = 50003;
							}
						}else {
							gf.setName(name);
							gf.setNamePy(CommonTools.getFirstSpell(name));
						}
					}
					if(status.equals(200)) {
						gf.setFacImage(facImage);
						if(!gasTypeId.equals("") && gasTypeId.equals(gf.getGasType().getId())) {
							gf.setGasType(gts.findById(gasTypeId));
						}
						gf.setProvince(province);
						gf.setCity(city);
						gf.setCounty(county);
						gf.setAddress(address);
						gf.setLxName(lxName);
						gf.setLxTel(lxTel);
						gf.setYzbgImg(yzbgImg);
						uId = gfs.addOrUpGasFactory(gf);
					}
				}else {
					status = 50001;
				}
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
	
	@GetMapping("getGasFactoryDetail")
	@ApiOperation(value = "获取指定主键的液厂详细信息",notes = "获取指定主键的液厂详细信息")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "gfId", value = "液厂编号", required = true)
	})
	public GenericResponse getGasFactoryDetail(HttpServletRequest request,String gfId) {
		Integer status = 200;
		List<GasFactory> gfList = new ArrayList<GasFactory>();
		try {
			GasFactory gf = gfs.getEntityById(gfId);
			if(gf != null) {
				gfList.add(gf);
			}else {
				status = 50001;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, gfList);
	}
	
	@GetMapping("getPageGasFactoryData")
	@ApiOperation(value = "分页获取主键的液厂信息列表",notes = "为空时不查询，审核状态为-1时不查询")
	@ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
		@ApiResponse(code = 50001, message = "数据未找到")
	})
	@ApiImplicitParams({
		@ApiImplicitParam(name = "name", value = "液厂名称"),
		@ApiImplicitParam(name = "namePy", value = "液厂名称首字母"),
		@ApiImplicitParam(name = "gasTypeId", value = "液质类型编号"),
		@ApiImplicitParam(name = "province", value = "省"),
		@ApiImplicitParam(name = "city", value = "市"),
		@ApiImplicitParam(name = "county", value = "县"),
		@ApiImplicitParam(name = "checkStatus", value = "审核状态(-1:全部,0:未审核,1:审核通过,2:审核未通过)"),
		@ApiImplicitParam(name = "owerUserId", value = "液厂拥有人"),
		@ApiImplicitParam(name = "pageIndex", value = "页码"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录条数")
	})
	public PageResponse getPageGasFactoryData(HttpServletRequest request,String name,String namePy,String gasTypeId,
			String province,String city,String county,Integer checkStatus,String owerUserId,Integer pageIndex,Integer pageSize ) {
		Integer status = 200;
		long count = 0;
		List<Object> list = new ArrayList<Object>();
		try {
			if(pageIndex == null) {
				pageIndex = 1;
			}
			if(pageSize == null) {
				pageSize = 10;
			}
			if(checkStatus == null) {
				checkStatus = -1;
			}
			Page<GasFactory> page = gfs.listPageInfoByOpt(name, namePy, gasTypeId, province, city, county, 
					checkStatus, owerUserId, pageIndex, pageSize);
			count = page.getTotalElements();
			if(count == 0) {
				status = 50001;
			}else {
				for(GasFactory gf : page) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("name", gf.getName());
					map.put("namePy", gf.getNamePy());
					map.put("facImage", gf.getFacImage());
					map.put("gasTypeName", gf.getGasType().getName());
					map.put("province", gf.getProvince());
					map.put("city", gf.getCity());
					map.put("county", gf.getCounty());
					map.put("address", gf.getAddress());
					map.put("lxName", gf.getLxName());
					map.put("addTime", gf.getAddTime());
					map.put("yzbg", gf.getYzbgImg());
					map.put("checkStatus", gf.getCheckStatus());
					list.add(map);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize,pageIndex,count,status, list);
	}
}
