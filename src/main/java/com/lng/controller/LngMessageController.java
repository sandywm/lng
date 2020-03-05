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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lng.pojo.LngMessage;
import com.lng.pojo.LngMessageReply;
import com.lng.pojo.LngMessageUserZc;
import com.lng.pojo.User;
import com.lng.service.LngMessageService;
import com.lng.service.LngMessageZcService;
import com.lng.service.UserService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.tools.Review;
import com.lng.tools.EmojiDealUtil;
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
	@Autowired
	private UserService userService;
	@Autowired
	private LngMessageZcService zcService;

	@GetMapping("/getLngMsgPageList")
	@ApiOperation(value = "分页获取lng留言列表", notes = "分页获取lng留言列表")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "page", value = "页码", dataType = "integer"),
			@ApiImplicitParam(name = "limit", value = "每页记录条数", dataType = "integer") })
	public PageResponse getLngMsgPageList(HttpServletRequest request) {
		Integer pageNo = CommonTools.getFinalInteger("page", request);
		Integer pageSize = CommonTools.getFinalInteger("limit", request);
		List<Object> list = new ArrayList<Object>();
		Integer status = 200;
		long count = 0;
		if (pageNo.equals(0)) {
			pageNo = 1;
		}
		if (pageSize.equals(0)) {
			pageSize = 50;
		}
		try {
			Page<LngMessage> page = lms.listPageMsgInfoByOpt("", 1, 0, pageNo, pageSize);
			count = page.getTotalElements();
			if (count > 0) {
				for (LngMessage lm : page) {
					Map<String, Object> map_d = new HashMap<String, Object>();
					map_d.put("id", lm.getId());
					User user = lm.getUser();
					map_d.put("headImg", user.getUserPortrait());
					map_d.put("userName", user.getRealName());
					map_d.put("content", EmojiDealUtil.changeStrToEmoji(lm.getContent()));
					map_d.put("addTime", lm.getAddTime());
					map_d.put("zcTimes", lm.getZcTimes());
					map_d.put("replyNumber", lms.listReplyMsgByMsdId(lm.getId(), 1, 0, 1, 100000).getTotalElements());
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

	@PostMapping("/addLngMsg")
	@ApiOperation(value = "发布lng行情留言主题", notes = "发布lng行情留言主题")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			@ApiResponse(code = 20001, message = "用户未登录"),
			@ApiResponse(code = 50001, message = "数据未找到"), 
			@ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "content", value = "内容", required = true),
			@ApiImplicitParam(name = "userId", value = "用户编号", required = true) })
	public GenericResponse addLngMsg(HttpServletRequest request) {
		String content = CommonTools.getFinalStr("content", request);
		String userId = CommonTools.getFinalStr("userId", request);
		Integer status = 200;
		String id = "";
		try {
			if (userId.isEmpty()) {
				status = 20001;
			}else if(content.equals("")) {
				status = 10002;
			} else {
				User user = userService.getEntityById(userId);
				if (user == null) {
					status = 50001;
				} else {
					content = EmojiDealUtil.changeEmojiToHtml(content);
					LngMessage lmsg = new LngMessage(user, content, CurrentTime.getCurrentTime(), 1,
							"", 0, 0);
					lms.addOrUpdateLngMsg(lmsg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, id);
	}

	@PostMapping("/addLngMsgRep")
	@ApiOperation(value = "发布LNG行情留言回复", notes = "发布LNG行情留言回复")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
		@ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 20001, message = "用户未登录"),
		@ApiResponse(code = 10002, message = "参数为空"),
		@ApiResponse(code = 50001, message = "数据未找到")})
	@ApiImplicitParams({ @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
			@ApiImplicitParam(name = "msgId", value = "消息编号"),
			@ApiImplicitParam(name = "content", value = "内容", required = true) })
	public GenericResponse addLngMsgRep(HttpServletRequest request) {
		String content = CommonTools.getFinalStr("content", request);
		String userId = CommonTools.getFinalStr("userId", request);
		String msgId = CommonTools.getFinalStr("msgId", request);
		Integer status = 200;
		String id = "";
		try {
			if (userId.isEmpty()) {
				status = 20001;
			} else if(msgId.equals("") || content.equals("")){
				status = 10002;
			}else {
				User user = userService.getEntityById(userId);
				if (user == null) {
					status = 50001;
				} else {
					content = EmojiDealUtil.changeEmojiToHtml(content);
					LngMessage lmsg = lms.getEntityById(msgId);
					LngMessageReply lmr = new LngMessageReply(lmsg, user, content,
							CurrentTime.getCurrentTime(), 1, "", 0);
					lms.addOrUpdateLngMsg(lmr);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, id);
	}

	@GetMapping("/getLngMsgRepPageList")
	@ApiOperation(value = "分页获取lng留言回复列表", notes = "分页获取lng留言回复列表")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "msgId", value = "LNG留言编号", required = true),
//		@ApiImplicitParam(name = "checkStatus", value = "审核状态(0:未审核,1:审核通过,2:审核未通过)"),
//		@ApiImplicitParam(name = "showStatus", value = "显示状态（0：显示，1：隐藏）"),
			@ApiImplicitParam(name = "limit", value = "显示记录条数"), @ApiImplicitParam(name = "page", value = "页码") })
	public GenericResponse getLngMsgRepPageList(HttpServletRequest request) {
		String msgId = CommonTools.getFinalStr("msgId", request);
//		Integer checkStatus = CommonTools.getFinalInteger("checkStatus", request);
//		Integer showStatus = CommonTools.getFinalInteger("showStatus", request);
		Integer pageNo = CommonTools.getFinalInteger("page", request);
		Integer pageSize = CommonTools.getFinalInteger("limit", request);
		List<Object> list = new ArrayList<Object>();
		Integer status = 200;
		long count = 0;
		try {
			if (pageNo.equals(0)) {
				pageNo = 1;
			}
			if (pageSize.equals(0)) {
				pageSize = 50;
			}
			List<Object> list_main = new ArrayList<Object>();
			Map<String, Object> map_main = new HashMap<String, Object>();
			LngMessage lsg = lms.getEntityById(msgId);
			Map<String, Object> map = new HashMap<String, Object>();
			User user_tmp = lsg.getUser();
			map.put("headImg", user_tmp.getUserPortrait());
			map.put("userName", user_tmp.getRealName());
			map.put("content", EmojiDealUtil.changeStrToEmoji(lsg.getContent()));
			map.put("addTime", lsg.getAddTime());
			map.put("zcTimes", lsg.getZcTimes());
			long lsgLen = lms.listReplyMsgByMsdId(msgId, 1, 0, 1, 100000).getTotalElements();
			map.put("replyNumber", lsgLen);
			list_main.add(map);
			map_main.put("mainList", list_main);

			if (lsgLen > 0) {
				List<Object> list_reply = new ArrayList<Object>();
				Page<LngMessageReply> lmList = lms.listReplyMsgByMsdId(msgId, 1, 0, pageNo, pageSize);
				count = lmList.getTotalElements();
				if (count > 0) {
					for (LngMessageReply lm : lmList) {
						Map<String, String> map_d = new HashMap<String, String>();
						User user = lm.getUser();
						map_d.put("headImg", user.getUserPortrait());
						map_d.put("userName", user.getWxName());
						map_d.put("content", lm.getContent());
						map_d.put("addTime", lm.getAddTime());
						list_reply.add(map_d);
					}
					map_main.put("replyList", list_reply);
				} else {
					status = 50001;
				}
			} else {
				status = 50001;
			}
			list.add(map_main);
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}

	@PostMapping("/addLmZc")
	@ApiOperation(value = "LNG行情支持", notes = "LNG行情支持明细,返回接口数据为消息编号")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50003, message = "数据已存在"), })
	@ApiImplicitParams({ @ApiImplicitParam(name = "msgId", value = "LNG消息编号", required = true),
			@ApiImplicitParam(name = "userId", value = "用户编号", required = true) })
	public GenericResponse addLmZc(HttpServletRequest request) {
		String msgId = CommonTools.getFinalStr("msgId", request);
		String userId = CommonTools.getFinalStr("userId", request);
		Integer status = 200;
		String id = "";
		try {
			if (zcService.getLmZc(userId, msgId).size() == 0) {
				User user = userService.getEntityById(userId);
				LngMessage lngMessage = lms.getEntityById(msgId);
				if (lngMessage == null || user == null) {

				} else {
					LngMessageUserZc lmZc = new LngMessageUserZc(lngMessage, user, CurrentTime.getCurrentTime());
					id = zcService.addLmZc(lmZc);
					if (!id.equals("")) {
						lngMessage.setZcTimes(lngMessage.getZcTimes() + 1);
						lms.addOrUpdateLngMsg(lngMessage);
					}
				}
			} else {
				status = 50003;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, "");
	}
}
