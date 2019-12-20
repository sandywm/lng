package com.lng.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.MessageCenter;
import com.lng.service.MessageCenterService;
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
@Api(tags = "消息中心相关接口")
@RequestMapping("/MsgCenter")
public class MessageCenterController {

	@Autowired
	private MessageCenterService mcService;

	@GetMapping("/getMsgCenterPage")
	@ApiOperation(value = "分页获取消息中心列表", notes = "分页获取消息中心列表")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "toUserId", value = "接收人编号", required = true),
			@ApiImplicitParam(name = "readSta", value = "已读状态", dataType = "integer"),
			@ApiImplicitParam(name = "pageNo", value = "页码", dataType = "integer"),
			@ApiImplicitParam(name = "pageSize", value = "每页记录条数", dataType = "integer") })
	public PageResponse getMsgCenterPageList(String toUserId, Integer readSta, Integer pageNo, Integer pageSize) {
		toUserId = CommonTools.getFinalStr(toUserId);
		pageNo = CommonTools.getFinalInteger(pageNo);
		pageSize = CommonTools.getFinalInteger(pageSize);
		Integer status = 200;
		if (pageNo.equals(0)) {
			pageNo = 1;
		}
		if (pageSize.equals(0)) {
			pageSize = 10;
		}
		if (readSta==null) {
			readSta = -1;
		}
		Page<MessageCenter> mcs = null;
		try {
			mcs = mcService.getMessageCenterByOption(toUserId, readSta, pageNo - 1, pageSize);
			if (mcs.getTotalElements() == 0) {
				status = 50001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(pageSize, pageNo, mcs.getTotalElements(), status, mcs.getContent());
	}

	@PostMapping("/addMessageCenter")
	@ApiOperation(value = "添加消息中心信息", notes = "添加燃气交易订单日志信息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "title", value = "标题", required = true),
			@ApiImplicitParam(name = "content", value = "内容", required = true),
			@ApiImplicitParam(name = "showSta", value = "显示状态（0：默认显示，1：隐藏）", required = true),
			@ApiImplicitParam(name = "msgType", value = "消息类型（1：新闻资讯，2：系统通知，3：留言回复）", required = true),
			@ApiImplicitParam(name = "primaryId", value = "主键类型"),
			@ApiImplicitParam(name = "primaryType", value = "主键类型"),
			@ApiImplicitParam(name = "addUserId", value = "发布人编号"),
			@ApiImplicitParam(name = "toUserId", value = "接收人编号") })
	public GenericResponse addMessageCenter(HttpServletRequest request) {
		String title = CommonTools.getFinalStr("title", request);
		String content = CommonTools.getFinalStr("content", request);
		String primaryId = CommonTools.getFinalStr("primaryId", request);
		String primaryType = CommonTools.getFinalStr("primaryType", request);
		String addUserId = CommonTools.getFinalStr("addUserId", request);
		String toUserId = CommonTools.getFinalStr("toUserId", request);
		Integer showSta = CommonTools.getFinalInteger("showSta", request);
		Integer msgType = CommonTools.getFinalInteger("msgType", request);
		Integer status = 200;
		String mcId = "";
		try {
			MessageCenter mc = new MessageCenter();
			mc.setTitle(title);
			mc.setContent(content);
			mc.setShowStatus(showSta);
			mc.setAddTime(CurrentTime.getCurrentTime());
			mc.setMessageType(msgType);
			mc.setPrimaryId(primaryId);
			mc.setPrimaryType(primaryType);
			mc.setAddUserId(addUserId);
			mc.setToUserId(toUserId);
			mc.setReadStatus(0);
			mcId = mcService.saveOrUpdate(mc);
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, mcId);
	}
	
	@PutMapping("/updateByReadSta")
	@ApiOperation(value = "修改消息中心已读状态", notes = "修改消息中心已读状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			 @ApiResponse(code = 50001, message = "数据未找到")
		 })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "消息中心编号", required = true),
			@ApiImplicitParam(name = "readSta", value = "已读状态（0：未读，1：已读）", defaultValue = "1", required = true),
		 })
	public GenericResponse updateByReadSta(String id, Integer readSta) {
		id = CommonTools.getFinalStr(id);
		readSta = CommonTools.getFinalInteger(readSta);
		Integer status = 200;
			try {
				MessageCenter mc = mcService.getEntityById(id);
				if (mc == null) {
					status = 50001;
				} else {
					mc.setReadStatus(readSta);
					mcService.saveOrUpdate(mc);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		
		return ResponseFormat.retParam(status, "");
	}

}
