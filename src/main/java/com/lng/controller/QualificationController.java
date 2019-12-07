package com.lng.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.Qualification;
import com.lng.service.QualificationService;
import com.lng.tools.CurrentTime;
import com.lng.util.GenericResponse;
import com.lng.util.ResponseFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "进港资质相关接口")
public class QualificationController {
	@Autowired
	private QualificationService quaService;

	@PostMapping("/addQual")
	@ApiOperation(value = "添加进港资质", notes = "添加进港资质信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "进港资质名称", defaultValue = "进港资质测试", required = true),
			@ApiImplicitParam(name = "validStatus", value = "有效状态(0:有效,1:无效)", defaultValue = "1", required = true) })
	public GenericResponse addQual(String name, Integer validStatus) {
		Integer status = 200;
		String qId = "";
		try {
			if (quaService.getQualByNameList(name).size() == 0) {
				Qualification qua = new Qualification();
				qua.setName(name);
				qua.setValidStatus(validStatus);
				qua.setAddTime(CurrentTime.getCurrentTime());
				qId = quaService.save(qua);
			} else {
				status = 50003;
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, qId);
	}

	@PutMapping("/updateQual")
	@ApiOperation(value = "修改进港资质", notes = "修改进港资质信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), @ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "qId", value = "进港资质编号", required = true),
			@ApiImplicitParam(name = "name", value = "进港资质名称", defaultValue = "进港资质测试", required = true),
			@ApiImplicitParam(name = "validstatus", value = "有效状态(0:有效,1:无效)", defaultValue = "1", required = true) })
	public GenericResponse updateQual(String qId, String name, Integer validstatus) {
		Integer status = 200;
		try {
			Qualification qual = quaService.findById(qId);
			if (qual == null) {
				status = 50001;
			} else {
				if (name.equals(qual.getName())) {
					qual.setValidStatus(validstatus);
					quaService.edit(qual);
				} else {
					if (quaService.getQualByNameList(name).size() == 0) {
						qual.setName(name);
						qual.setValidStatus(validstatus);
						quaService.edit(qual);
					} else {
						status = 50003;
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}

	@GetMapping("/queryQual")
	@ApiOperation(value = "分页获取所有进港资质", notes = "分页获取所有进港资质信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "进港资质编号") })
	public GenericResponse queryQual(String id) {
		Integer status = 200;
		List<Qualification> qualList = new ArrayList<Qualification>();
		try {
			if (id.equals("")) {
				qualList = quaService.getQualificationList();
			} else {
				Qualification qual = quaService.findById(id);
				if (qual == null) {
					status = 50001;
				} else {
					qualList.add(qual);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, qualList);
	}

	@DeleteMapping("/delQualById")
	@ApiOperation(value = "删除指定编号进港资质", notes = "删除指定编号进港资质信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "qId", value = "进港资质编号", required = true) })
	public GenericResponse delQualById(String qId) {
		Integer status = 200;
		try {
			quaService.delete(qId);
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");

	}
}
