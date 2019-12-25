package com.lng.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.Company;
import com.lng.pojo.User;
import com.lng.pojo.UserCompany;
import com.lng.pojo.UserFocus;
import com.lng.service.CompanyService;
import com.lng.service.UserCompanyService;
import com.lng.service.UserFocusService;
import com.lng.service.UserService;
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
@Api(tags = "用户公司和关注相关接口")
@RequestMapping("/company")
public class UserCompanyAndFocusController {
	@Autowired
	private UserCompanyService ucService;
	@Autowired
	private UserFocusService ufService;
	@Autowired
	private CompanyService cService;
	@Autowired
	private UserService uService;

	@PostMapping("/addUserCompany")
	@ApiOperation(value = "添加用户公司关联", notes = "添加用户公司关联信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "compId", value = "公司编号", required = true) })
	public GenericResponse addUserCompany(HttpServletRequest request, String userId, String compId) {
		userId = CommonTools.getFinalStr(userId);
		compId = CommonTools.getFinalStr(compId);
		Integer status = 200;
		String ucId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request),CommonTools.getLoginRoleName(request), Constants.CHECK_USER_JOIN_CPY_APPLY)) {
			try {
				if (ucService.getUserCompanyList(compId, userId).size() == 0) {
					UserCompany uc = new UserCompany();
					User user = uService.getEntityById(userId);
					uc.setUser(user);
					Company company = cService.getEntityById(compId);
					uc.setCompany(company);
					uc.setAddTime(CurrentTime.getCurrentTime());
					uc.setCheckStatus(0);
					uc.setCheckTime("");
					ucId = ucService.addOrUpdate(uc);
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
		return ResponseFormat.retParam(status, ucId);
	}

	@PostMapping("/addUserFocus")
	@ApiOperation(value = "添加用户关注", notes = "添加用户关注信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "focusId", value = "关注编号", required = true),
			@ApiImplicitParam(name = "focusType", value = "关注类型(cczm,rqsb,cgzm,rqmm,sjqz,sjzp)", required = true) })
	public GenericResponse addUserFocus(HttpServletRequest request, String userId, String focusId, String focusType) {
		userId = CommonTools.getFinalStr(userId);
		focusId = CommonTools.getFinalStr(focusId);
		focusType = CommonTools.getFinalStr(focusType);
		Integer status = 200;
		String ufId = "";
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.ADD_USER)) {
			try {
				if (ufService.getUserFocusList(userId,focusId,focusType).size() == 0) {
					UserFocus uf = new UserFocus();
					User user = uService.getEntityById(userId);
					uf.setUser(user);
					uf.setFocusId(focusId);
					uf.setFocusType(focusType);
					uf.setFocusTime(CurrentTime.getCurrentTime());
					ufId = ufService.addOrUpdage(uf);
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
		return ResponseFormat.retParam(status, ufId);
	}

	@PutMapping("/updateUserCompanyBySta")
	@ApiOperation(value = "用户公司关联信息审核", notes = "用户公司关联信息审核状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到"), @ApiResponse(code = 70001, message = "无权限访问") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "用户公司关联编号", required = true),
			@ApiImplicitParam(name = "checkSta", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)", required = true) })
	public GenericResponse updateUserCompanyBySta(HttpServletRequest request, String id, Integer checkSta) {
		id = CommonTools.getFinalStr(id);
		Integer status = 200;
		if (CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request),Constants.CHECK_USER_JOIN_CPY_APPLY)) {
			try {
				UserCompany uc = ucService.getEntityId(id);
				if (uc == null) {
					status = 50001;
				} else {
					if (checkSta != null && !checkSta.equals(uc.getCheckStatus())) {
						uc.setCheckStatus(checkSta);
						uc.setCheckTime(CurrentTime.getCurrentTime());
					}
					ucService.addOrUpdate(uc);
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

	@GetMapping("/queryUserCompany")
	@ApiOperation(value = "获取用户公司关联", notes = "获取用户公司关联信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "compId", value = "公司编号"),
			@ApiImplicitParam(name = "userId", value = "用户编号") })
	public GenericResponse queryUserCompany(String compId, String userId) {
		compId = CommonTools.getFinalStr(compId);
		userId = CommonTools.getFinalStr(userId);
		Integer status = 200;
		List<UserCompany> ucs = null;
		try {
			ucs = ucService.getUserCompanyList(compId, userId);
			if (ucs == null) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, ucs);
	}

	@GetMapping("/queryUserFocus")
	@ApiOperation(value = "获取用户关注", notes = "获取用户关注")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "focusType", value = "关注类型") })
	public GenericResponse queryUserFocus(String userId, String focusType) {
		userId = CommonTools.getFinalStr(userId);
		focusType = CommonTools.getFinalStr(focusType);
		Integer status = 200;
		List<UserFocus> ufs = null;
		try {
			ufs = ufService.userFocusList(userId, focusType);
			if (ufs == null) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, ufs);
	}

	@DeleteMapping("/delUserFocusById")
	@ApiOperation(value = "取消用户关注", notes = "取消用户关注信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50002, message = "数据有误") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "ufId", value = "用户关注编号", required = true) })
	public GenericResponse delUserFocusById(String ufId) {
		ufId = CommonTools.getFinalStr(ufId);
		Integer status = 200;
		try {
			if (ufId.isEmpty()) {
				status = 50002;
			} else {
				ufService.delete(ufId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");

	}
}
