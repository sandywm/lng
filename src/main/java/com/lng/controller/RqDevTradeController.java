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

import com.lng.pojo.Company;
import com.lng.pojo.RqDevTrade;
import com.lng.pojo.RqDevTradeImg;
import com.lng.pojo.RqDevType;
import com.lng.pojo.RqDevType1;
import com.lng.pojo.User;
import com.lng.pojo.UserFocus;
import com.lng.service.CompanyService;
import com.lng.service.RqDevTradeImgService;
import com.lng.service.RqDevTradeService;
import com.lng.service.RqDevType1Service;
import com.lng.service.RqDevTypeService;
import com.lng.service.UserFocusService;
import com.lng.service.UserService;
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
@Api(tags = "燃气设备买卖相关接口")
@RequestMapping("/rqDevTrade")
public class RqDevTradeController {
	@Autowired
	private CompanyService companyService;
	@Autowired
	private RqDevType1Service zlService;
	@Autowired
	private RqDevTypeService lmService;
	@Autowired
	private RqDevTradeService rdtService;
	@Autowired
	private RqDevTradeImgService rdtiService;
	@Autowired
	private UserFocusService ufService;
	@Autowired
	private UserService us;

	@PostMapping("/addRqDevTrade")
	@ApiOperation(value = "添加燃气设备买卖", notes = "添加燃气设备买卖")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司主键", required = true),
			@ApiImplicitParam(name = "mainImg", value = "燃气设备主图", required = true, defaultValue = "燃气设备图.img"),
			@ApiImplicitParam(name = "devName", value = "设备名称", required = true, defaultValue = "马哈"),
			@ApiImplicitParam(name = "devNo", value = "设备型号", required = true, defaultValue = "NoAlo909"),
			@ApiImplicitParam(name = "devPp", value = "生产厂家", required = true, defaultValue = "其他牌"),
			@ApiImplicitParam(name = "devPrice", value = "官方报价", required = true, defaultValue = "2345.00"),
			@ApiImplicitParam(name = "lmId", value = "设备类目主键", required = true),
			@ApiImplicitParam(name = "zlId", value = "设备种类主键", required = true),
			@ApiImplicitParam(name = "description", value = "描述", defaultValue = "这是一个描述"),
			@ApiImplicitParam(name = "detailImg", value = "燃气设备详情图"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"),
			@ApiImplicitParam(name = "userId", value = "上传人员", required = true),
			/*
			 * @ApiImplicitParam(name = "userType", value = "上传人员类型（1：后台管理人员，2：普通用户）",
			 * required = true)
			 */
	})
	public GenericResponse addRqDevTrade(HttpServletRequest request) {
		String compId = CommonTools.getFinalStr("compId", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String devName = CommonTools.getFinalStr("devName", request);
		String devNo = CommonTools.getFinalStr("devNo", request);
		String devPp = CommonTools.getFinalStr("devPp", request);
		Double devPrice = CommonTools.getFinalDouble("devPrice", request);
		String lmId = CommonTools.getFinalStr("lmId", request);
		String zlId = CommonTools.getFinalStr("zlId", request);
		String description = CommonTools.getFinalStr("description", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		String detailImg = CommonTools.getFinalStr("detailImg", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		// String addUserId = CommonTools.getFinalStr("addUserId", request);
		// Integer userType = CommonTools.getFinalInteger("userType", request);
		Integer userType = 1;
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;
		String rdtId = "";

		try {
			if (cilentInfo.equals("wxApp")) {
				userType = 2;
			} else if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),
					Constants.ADD_RDT)) {

			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				RqDevTrade rdt = new RqDevTrade();
				Company company = companyService.getEntityById(compId);
				rdt.setCompany(company);
				if (!mainImg.equals("")) {
					rdt.setMainImg(CommonTools.dealUploadDetail(loginUserId, "", mainImg));
				}
				rdt.setDevName(devName);
				rdt.setDevNo(devNo);
				rdt.setDevPp(devPp);
				rdt.setDevPrice(devPrice);
				RqDevType rqDevType = lmService.findById(lmId);
				rdt.setRqDevType(rqDevType);
				RqDevType1 rqDevType1 = zlService.findById(zlId);
				rdt.setRqDevType1(rqDevType1);
				rdt.setDescription(description);
				rdt.setLxName(lxName);
				rdt.setLxTel(lxTel);
				rdt.setShowStatus(showStatus);
				rdt.setAddUserId(loginUserId);
				rdt.setUserType(userType);
				rdt.setAddTime(CurrentTime.getCurrentTime());
				if (userType.equals(1)) {
					rdt.setCheckStatus(1);
					rdt.setCheckTime(CurrentTime.getCurrentTime());
				} else {
					rdt.setCheckStatus(0);
					rdt.setCheckTime("");
				}
				rdt.setHot(0);
				rdtId = rdtService.saveOrUpdate(rdt);

				if (!detailImg.isEmpty()) {
					String dImgs = CommonTools.dealUploadDetail(loginUserId, "", detailImg);
					String[] dImgArr = dImgs.split(",");
					List<RqDevTradeImg> rdtiList = new ArrayList<>();
					for (int i = 0; i < dImgArr.length; i++) {
						RqDevTradeImg rdti = new RqDevTradeImg();
						rdti.setRqDevTrade(rdt);
						rdti.setReDevDetailImg(dImgArr[i]);
						rdti.setOrderNum(i);
						rdtiList.add(rdti);
					}
					rdtiService.addOrUpdateBatch(rdtiList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, rdtId);
	}

	@PutMapping("/updateRqDevTradeByStatus")
	@ApiOperation(value = "更新燃气设备买卖审核状态", notes = "更新燃气设备买卖状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气设备买卖主键", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "opt", value = "0:审核，1：上下架",required = true,dataType = "integer")
	})
	public GenericResponse updateRqDevTradeByStatus(HttpServletRequest request, String id, Integer checkSta,
			Integer showSta) {
		id = CommonTools.getFinalStr(id);
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer status = 200;
		Integer opt = CommonTools.getFinalInteger("opt", request);
		try {
			if (cilentInfo.equals("wxApp")) {

			} else if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.CHECK_RDT)) {

			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				RqDevTrade rdt = rdtService.getEntityById(id);
				if (rdt == null) {
					status = 50001;
				} else {
					if(opt.equals(0)) {
						if (checkSta != null && !checkSta.equals(rdt.getCheckStatus())) {
							rdt.setCheckStatus(checkSta);
							rdt.setCheckTime(CurrentTime.getCurrentTime());
							rdtService.saveOrUpdate(rdt);
						}
					}else {
						if (showSta != null && !showSta.equals(rdt.getShowStatus())) {
							rdt.setShowStatus(showSta);
							if(showSta.equals(1)) {//由上架设置为未审核
								//审核状态设置为未审核
								rdt.setCheckStatus(0);
								rdt.setCheckTime("");
							}else {//上架
								rdt.setAddTime(CurrentTime.getCurrentTime());
							}
							rdtService.saveOrUpdate(rdt);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateRqDevTradeByHot")
	@ApiOperation(value = "更新储罐租卖热度", notes = "更新储罐租卖热度")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气设备买卖主键", required = true),
			@ApiImplicitParam(name = "hot", value = "热度（默认为0）") })
	public GenericResponse updateRqDevTradeByHot(HttpServletRequest request, String id, Integer hot) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		try {
			if (cilentInfo.equals("wxApp")) {

			} else if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),
					CommonTools.getLoginRoleName(request), Constants.UP_RDT)) {

			} else {
				status = 70001;
			}

			if (status.equals(200)) {
				RqDevTrade rdt = rdtService.getEntityById(id);
				if (rdt == null) {
					status = 50001;
				} else {
					if (hot != null && !hot.equals(rdt.getHot())) {
						rdt.setHot(hot);
					}
					rdtService.saveOrUpdate(rdt);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}

		return ResponseFormat.retParam(status, "");
	}

	@PutMapping("/updateRqDevTrade")
	@ApiOperation(value = "更新燃气设备买卖", notes = "更新燃气设备买卖信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 80001, message = "审核通过不能修改"), @ApiResponse(code = 50001, message = "数据未找到"),
			@ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "燃气设备买卖主键", required = true),
			@ApiImplicitParam(name = "mainImg", value = "燃气设备主图", required = true, defaultValue = "燃气设备图.img"),
			@ApiImplicitParam(name = "compId", value = "公司编号", required = true),
			@ApiImplicitParam(name = "devName", value = "设备名称", required = true, defaultValue = "马哈"),
			@ApiImplicitParam(name = "devNo", value = "设备型号", required = true, defaultValue = "NoAlo909"),
			@ApiImplicitParam(name = "devPp", value = "生产厂家", required = true, defaultValue = "其他牌"),
			@ApiImplicitParam(name = "devPrice", value = "官方报价", required = true, defaultValue = "2345.00"),
			@ApiImplicitParam(name = "lmId", value = "设备类目主键", required = true),
			@ApiImplicitParam(name = "zlId", value = "设备种类主键", required = true),
			@ApiImplicitParam(name = "description", value = "描述", defaultValue = "这是一个描述"),
			@ApiImplicitParam(name = "lxName", value = "联系人", defaultValue = "小黑"),
			@ApiImplicitParam(name = "detailImg", value = "燃气设备详情图"),
			@ApiImplicitParam(name = "lxTel", value = "联系电话", defaultValue = "13956487523"),
			@ApiImplicitParam(name = "showStatus", value = "上/下架状态（0：上架，1：下架）", required = true, defaultValue = "0"), 
			@ApiImplicitParam(name = "userId", value = "用户编号", required = true)
	})
	public GenericResponse updateRqDevTrade(HttpServletRequest request) {

		String id = CommonTools.getFinalStr("id", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		String compId = CommonTools.getFinalStr("compId", request);
		String devName = CommonTools.getFinalStr("devName", request);
		String devNo = CommonTools.getFinalStr("devNo", request);
		String devPp = CommonTools.getFinalStr("devPp", request);
		Double devPrice = CommonTools.getFinalDouble("devPrice", request);
		String lmId = CommonTools.getFinalStr("lmId", request);
		String zlId = CommonTools.getFinalStr("zlId", request);
		String description = CommonTools.getFinalStr("description", request);
		String lxName = CommonTools.getFinalStr("lxName", request);
		String lxTel = CommonTools.getFinalStr("lxTel", request);
		String detailImg = CommonTools.getFinalStr("detailImg", request);
		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);

		String loginUserId = CommonTools.getLoginUserId(request);
		Integer status = 200;
		String cilentInfo = CommonTools.getCilentInfo_new(request);
		Integer userType = 1;
		try {
			if (cilentInfo.equals("wxApp")) {
				userType = 2;
			} else if (CommonTools.checkAuthorization(loginUserId, CommonTools.getLoginRoleName(request),
					Constants.UP_RDT)) {

			} else {
				status = 70001;
			}
			if (status.equals(200)) {
				RqDevTrade rdt = rdtService.getEntityById(id);
				if (rdt == null) {
					status = 50001;
				} else if (rdt.getCheckStatus() == 1) {
					status = 80001;
				} else {
					if(userType.equals(2)) {
						rdt.setUserType(2);
					}
					if (!mainImg.equals(rdt.getMainImg())) {
						rdt.setMainImg(CommonTools.dealUploadDetail(loginUserId, rdt.getMainImg(), mainImg));
					}
					if (!compId.isEmpty() && !compId.equals(rdt.getCompany().getId())) {
						Company company = companyService.getEntityById(compId);
						rdt.setCompany(company);
					}
					if (!devName.equals(rdt.getDevName())) {
						rdt.setDevName(devName);
					}
					if (!devNo.equals(rdt.getDevNo())) {
						rdt.setDevNo(devNo);
					}
					if (!devPp.equals(rdt.getDevPp())) {
						rdt.setDevPp(devPp);
					}
					if (!devPrice.equals(rdt.getDevPrice())) {
						rdt.setDevPrice(devPrice);
					}
					if (!lmId.equals(rdt.getRqDevType().getId())) {
						RqDevType rqDevType = lmService.findById(lmId);
						rdt.setRqDevType(rqDevType);
					}
					if (!zlId.equals(rdt.getRqDevType1().getId())) {
						RqDevType1 rqDevType1 = zlService.findById(zlId);
						rdt.setRqDevType1(rqDevType1);
					}
					if (!description.equals(rdt.getDescription())) {
						rdt.setDescription(description);
					}
					if (!lxName.equals(rdt.getLxName())) {
						rdt.setLxName(lxName);
					}
					if (!lxTel.equals(rdt.getLxTel())) {
						rdt.setLxTel(lxTel);
					}
					if (!showStatus.equals(rdt.getShowStatus())) {
						rdt.setShowStatus(showStatus);
					}
					rdtService.saveOrUpdate(rdt);

					List<RqDevTradeImg> riList = rdtiService.getRdtImgByRdtId(id);
					String imgPath_db = "";
					if (!riList.isEmpty()) {
						for (RqDevTradeImg ri : riList) {
							imgPath_db += ri.getReDevDetailImg() + ",";
						}
						imgPath_db = imgPath_db.substring(0, imgPath_db.length() - 1);
						rdtiService.deleteBatch(riList);
					}

					if (!detailImg.isEmpty()) {
						String dImgs = CommonTools.dealUploadDetail(loginUserId, imgPath_db, detailImg);
						String[] dImgArr = dImgs.split(",");
						List<RqDevTradeImg> rdtiList = new ArrayList<>();
						for (int i = 0; i < dImgArr.length; i++) {
							RqDevTradeImg rdti = new RqDevTradeImg();
							rdti.setRqDevTrade(rdt);
							rdti.setReDevDetailImg(dImgArr[i]);
							rdti.setOrderNum(i);
							rdtiList.add(rdti);
						}
						rdtiService.addOrUpdateBatch(rdtiList);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/queryRqDevTrade")
	@ApiOperation(value = "燃气设备买卖", notes = "获取燃气设备买卖分页信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司主键"),
			@ApiImplicitParam(name = "lmId", value = "设备类目主键"), @ApiImplicitParam(name = "zlId", value = "设备种类主键"),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
			@ApiImplicitParam(name = "showSta", value = "上/下架状态（0：上架，1：下架）"),
			@ApiImplicitParam(name = "addUserId", value = "上传人员"), @ApiImplicitParam(name = "page", value = "第几页"),
			@ApiImplicitParam(name = "limit", value = "每页多少条") })
	public PageResponse queryRqDevTrade(String compId, String lmId, String zlId, Integer checkSta, Integer showSta,
			String addUserId, Integer page, Integer limit) {
		Integer status = 200;
		Page<RqDevTrade> rdt = null;
		List<Object> list = new ArrayList<Object>();
		try {
			compId = CommonTools.getFinalStr(compId);
			lmId = CommonTools.getFinalStr(lmId);
			zlId = CommonTools.getFinalStr(zlId);
			addUserId = CommonTools.getFinalStr(addUserId);
			if (page == null) {
				page = 1;
			}
			if (limit == null) {
				limit = 10;
			}
			if (checkSta == null) {
				checkSta = -1;
			}
			if (showSta == null) {
				showSta = -1;
			}
			rdt = rdtService.getRqDevTradeList(compId, lmId, zlId, checkSta, showSta, addUserId, page - 1, limit);
			if (rdt.getTotalElements() == 0) {
				status = 50001;
			} else {
				List<RqDevTrade> rdtList = rdt.getContent();
				for (RqDevTrade rdts : rdtList) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("rdtId", rdts.getId());
					map.put("cpyName", rdts.getCompany().getName());
					map.put("devName", rdts.getDevName());
					map.put("lmName", rdts.getRqDevType().getName());
					map.put("zlName", rdts.getRqDevType1().getName());
					map.put("checkStatus", rdts.getCheckStatus());
					map.put("showStatus", rdts.getShowStatus());
					map.put("devPrice", rdts.getDevPrice());
					map.put("factory", rdts.getDevPp());
					map.put("devNo", rdts.getDevNo());
					map.put("mainImg", rdts.getMainImg());
					map.put("addTime", rdts.getAddTime());
					map.put("userType", rdts.getUserType());
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, rdt.getTotalElements(), status, list);
	}

	@GetMapping("/getRqDevTradeById")
	@ApiOperation(value = "根据主键获取燃气设备买卖详细信息", notes = "根据主键获取燃气设备买卖详细信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 10002, message = "参数为空"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号"),
			@ApiImplicitParam(name = "id", value = "燃气设备买卖编号", required = true) })
	public GenericResponse getRqDevTradeById(HttpServletRequest request) {
		Integer status = 200;
		String rdtId = CommonTools.getFinalStr("id", request);
		String userId = CommonTools.getFinalStr("userId", request);
		List<Object> list = new ArrayList<Object>();
		try {
			if (rdtId.equals("")) {
				status = 10002;
			} else {
				RqDevTrade rdt = rdtService.getEntityById(rdtId);
				if (rdt == null) {
					status = 10002;
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("cpyId", rdt.getCompany().getId());
					map.put("cpyName", rdt.getCompany().getName());
					map.put("mainImg", rdt.getMainImg());
					map.put("devName", rdt.getDevName());
					map.put("devNo", rdt.getDevNo());
					map.put("devPp", rdt.getDevPp());
					map.put("devPrice", rdt.getDevPrice());
					map.put("devTypeId1", rdt.getRqDevType().getId());
					map.put("devTypeName1", rdt.getRqDevType().getName());
					map.put("devTypeId2", rdt.getRqDevType1().getId());
					map.put("devTypeName2", rdt.getRqDevType1().getName());
					map.put("description", rdt.getDescription());
					map.put("lxName", rdt.getLxName());
					map.put("lxTel", rdt.getLxTel());
					map.put("checkStatus", rdt.getCheckStatus());
					map.put("checkTime", rdt.getCheckTime());
					map.put("showStatus", rdt.getShowStatus());
					map.put("addUserId", rdt.getAddUserId());
					map.put("addTime", rdt.getAddTime());
					map.put("userType", rdt.getUserType());
					map.put("hot", rdt.getHot());
					User user = us.getEntityById(rdt.getAddUserId());
					if(user != null) {
						map.put("userHead", user.getUserPortrait());
					}else {
						map.put("userHead", "");
					}
					List<RqDevTradeImg> rdti = rdtiService.getRdtImgByRdtId(rdtId);
					List<Object> rdtilist = new ArrayList<Object>();
					if (!rdti.isEmpty()) {
						for (int i = 0; i < rdti.size(); i++) {
							Map<String, Object> rdtimap = new HashMap<String, Object>();
							rdtimap.put("pdiImg", rdti.get(i).getReDevDetailImg());
							rdtilist.add(rdtimap);
						}
					}
					String ufId = "";
					if (!userId.isEmpty()) {
						List<UserFocus> ufList = ufService.getUserFocusList(userId, rdtId, "rqsb");
						if(ufList.size() > 0) {
							ufId = ufList.get(0).getId();
						}
					}
					map.put("ufId", ufId);
					map.put("detailImg", rdtilist);
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

}
