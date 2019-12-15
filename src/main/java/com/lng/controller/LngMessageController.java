package com.lng.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.LngMessage;
import com.lng.service.LngMessageService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
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
@Api(tags = "lng留言相关接口")
@RequestMapping("/lngMsg")
public class LngMessageController {

	@Autowired
	private LngMessageService lms;
	
	@GetMapping("/getLngMsgPageList")
	@ApiOperation(value = "分页获取lng留言列表", notes = "分页获取lng留言列表")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "integer"),
		@ApiImplicitParam(name = "pageSize", value = "每页记录条数", required = true, dataType = "integer")
	})
	public PageResponse getLngMsgPageList(HttpServletRequest request) {
		Integer pageNo = CommonTools.getFinalInteger("pageNo", request);
		Integer pageSize = CommonTools.getFinalInteger("pageSize", request);
		List<Object> list = new ArrayList<Object>();
		Integer status = 200;
		long count = 0;
		if(pageNo.equals(0)) {
			pageNo = 1;
		}
		if(pageSize.equals(0)) {
			pageSize = 20;
		}
		try {
			Page<LngMessage> page = lms.listPageMsgInfoByOpt("", 1, 0, pageNo, pageSize);
			count = page.getTotalElements();
			if(count > 0) {
				for(LngMessage lm : page) {
					Map<String,Object> map_d = new HashMap<String,Object>();
					map_d.put("id", lm.getId());
					map_d.put("content", lm.getContent());
					map_d.put("addTime", lm.getAddTime());
					map_d.put("zcTimes", lm.getZcTimes());
					map_d.put("replyNumber", lms.listReplyMsgByMsdId(lm.getId(), 1, 0).size());
					list.add(map_d);
				}
			} else {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, count, status, list);
	}
	
	@GetMapping("/addLngMsg")
	@ApiOperation(value = "发布lng行情留言主题--未完成", notes = "发布lng行情留言主题")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			@ApiResponse(code = 200, message = "成功")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "液质类型编号") })
	public GenericResponse addLngMsg(HttpServletRequest request) {
		String content = CommonTools.getFinalStr("content", request);
		List<Object> list = new ArrayList<Object>();
		Integer status = 200;
		String id  = "";
		try {
			//调用百度自然语言处理
			LngMessage lmsg = new LngMessage(null,content, CurrentTime.getCurrentTime(), 1, "",0,0);
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, id);
	}
}
