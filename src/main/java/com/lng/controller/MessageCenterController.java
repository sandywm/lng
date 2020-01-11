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

import com.lng.pojo.MessageCenter;
import com.lng.pojo.SuperUser;
import com.lng.pojo.User;
import com.lng.service.MessageCenterService;
import com.lng.service.SuperService;
import com.lng.service.UserService;
import com.lng.tools.CommonTools;
import com.lng.tools.ContantsProperties;
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
@Api(tags = "消息中心相关接口")
@RequestMapping("/MsgCenter")
public class MessageCenterController {

	@Autowired
	private MessageCenterService mcService;
	@Autowired
	private UserService us;
	@Autowired
	private SuperService sus;
	@Autowired
	private ContantsProperties cp;

	@GetMapping("/getMsgCenterPageList")
	@ApiOperation(value = "分页获取消息中心列表", notes = "分页获取消息中心列表")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "toUserId", value = "接收人编号"),
			@ApiImplicitParam(name = "msgTypeId", value = "消息类型（1：新闻资讯，2：系统通知，3：留言回复，4：价格变动通知）", dataType = "integer"),
			@ApiImplicitParam(name = "showStatus", value = "显示状态（-1:全部，0：默认显示，1：隐藏）", dataType = "integer"),
			@ApiImplicitParam(name = "readSta", value = "已读状态（-1:全部，0：未读，1：已读）", dataType = "integer"),
			@ApiImplicitParam(name = "page", value = "页码", dataType = "integer"),
			@ApiImplicitParam(name = "limit", value = "每页记录条数", dataType = "integer") })
	public PageResponse getMsgCenterPageList(HttpServletRequest request) {
		Integer msgTypeId =  CommonTools.getFinalInteger("msgTypeId", request);
		Integer showStatus =  CommonTools.getFinalInteger("showStatus", request);
		String toUserId = CommonTools.getFinalStr("toUserId",request);
		Integer readSta = CommonTools.getFinalInteger("readSta", request);
		Integer page = CommonTools.getFinalInteger("page", request);
		Integer limit = CommonTools.getFinalInteger("limit", request);
		Integer status = 200;
		long count = 0;
		if (page.equals(0)) {
			page = 1;
		}
		if (limit.equals(0)) {
			limit = 10;
		}
		Page<MessageCenter> mcs = null;
		List<Object> list = new ArrayList<Object>();
		try {
			mcs = mcService.getMessageCenterByOption(msgTypeId,toUserId, showStatus, readSta, page - 1, limit);
			count = mcs.getTotalElements();
			if (count == 0) {
				status = 50001;
			}else {
				for(MessageCenter mc : mcs) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", mc.getId());
					map.put("title", mc.getTitle());
					map.put("content", mc.getContent());
					map.put("addTime", mc.getAddTime());
					Integer msgType = mc.getMessageType();
					map.put("msgType", msgType);
					map.put("primaryId", mc.getPrimaryId());
					map.put("primaryType", mc.getPrimaryType());
					map.put("showStatus", mc.getShowStatus());
					map.put("readSta", mc.getReadStatus());
					String addUserId = mc.getAddUserId();
					String toUserId_tmp = mc.getToUserId();
					String addUserName = "";
					String toUserName = "";
					if(msgType.equals(2) || msgType.equals(3)) {
						map.put("addUserId", addUserId);
						User user = us.getEntityById(addUserId);
						if(user != null) {
							addUserName = user.getRealName();
						}
						User toUser = us.getEntityById(toUserId_tmp);
						if(toUser != null) {
							toUserName = toUser.getRealName();
						}
					}else if(msgType.equals(1)) {
						SuperUser user = sus.getEntityById(addUserId);
						if(user != null) {
							addUserName = user.getRealName();
						}
					}
					map.put("addUserId", addUserId);
					map.put("toUserId", toUserId);
					map.put("addUserName", addUserName);
					map.put("toUserName", toUserName);
					list.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.getPageJson(limit, page, count, status, list);
	}

	@GetMapping("/getSpecMsgDetail")
	@ApiOperation(value = "获取指定消息详情", notes = "获取指定消息详情")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			@ApiResponse(code = 50001, message = "数据未找到") })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "消息编号")})
	public GenericResponse getSpecMsgDetail(HttpServletRequest request) {
		String id =  CommonTools.getFinalStr("id", request);
		Integer status = 200;
		List<Object> list = new ArrayList<Object>();
		try {
			MessageCenter mc = mcService.getEntityById(id);
			if (mc == null) {
				status = 50001;
			}else {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", id);
				String mainImg = mc.getMainImg();
				if(mc.getMessageType() == 1) {
					if(mainImg.equals("")) {
						map.put("mainImg", cp.getDefaultNewsImg());
					}else {
						map.put("mainImg", mc.getMainImg());
					}
				}
				map.put("title", mc.getTitle());
				map.put("content", mc.getContent());
				map.put("addTime", mc.getAddTime());
				Integer msgType = mc.getMessageType();
				map.put("msgType", msgType);
				map.put("primaryId", mc.getPrimaryId());
				map.put("primaryType", mc.getPrimaryType());
				String addUserId = mc.getAddUserId();
				String toUserId = mc.getToUserId();
				String addUserName = "";
				String toUserName = "";
				if(msgType.equals(2) || msgType.equals(3)) {
					map.put("addUserId", addUserId);
					User user = us.getEntityById(addUserId);
					if(user != null) {
						addUserName = user.getRealName();
					}
					User toUser = us.getEntityById(toUserId);
					if(toUser != null) {
						toUserName = toUser.getRealName();
					}
				}
				map.put("addUserId", addUserId);
				map.put("toUserId", toUserId);
				map.put("addUserName", addUserName);
				map.put("toUserName", toUserName);
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, list);
	}
	
	@PostMapping("/sendMessage")
	@ApiOperation(value = "发送消息", notes = "发送消息")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
		@ApiResponse(code = 200, message = "成功"),
		@ApiResponse(code = 70001, message = "无权限访问")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "title", value = "标题", required = true),
			@ApiImplicitParam(name = "content", value = "内容", required = true),
			@ApiImplicitParam(name = "msgType", value = "消息类型（1：新闻资讯，2：系统通知，3：留言回复，4：价格变动通知）", required = true),
			@ApiImplicitParam(name = "primaryId", value = "主键类型-新闻资讯时无需传递"),
			@ApiImplicitParam(name = "primaryType", value = "主键类型-新闻资讯时无需传递"),
			@ApiImplicitParam(name = "addUserId", value = "发布人员编号-留言回复时传递"),
			@ApiImplicitParam(name = "toUserId", value = "接收人编号-新闻资讯时无需传递"),
			@ApiImplicitParam(name = "mainImg", value = "新闻封面图-新闻资讯时需传递")
	})
	public GenericResponse sendMessage(HttpServletRequest request) {
		String title = CommonTools.getFinalStr("title", request);
		String content = CommonTools.getFinalStr("content", request);
		String primaryId = CommonTools.getFinalStr("primaryId", request);
		String primaryType = CommonTools.getFinalStr("primaryType", request);
		String addUserId = "";
		String toUserId = CommonTools.getFinalStr("toUserId", request);
		Integer msgType = CommonTools.getFinalInteger("msgType", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		Integer status = 200;
		String mcId = "";
		boolean flag = true;
		try {
			if(msgType.equals(1)) {
				addUserId = CommonTools.getLoginUserId(request);
				//需要判断权限
				if(!CommonTools.checkAuthorization(addUserId, CommonTools.getLoginRoleName(request), Constants.ADD_MSG)) {
					flag = false;
				}
			}else if(msgType.equals(3)) {
				addUserId = CommonTools.getFinalStr("addUserId", request);
			}
			if(flag) {
				MessageCenter mc = new MessageCenter();
				if(msgType.equals(1) && !mainImg.equals("")) {
					mc.setMainImg(CommonTools.dealUploadDetail(addUserId, "", mainImg));
				}else {
					mc.setMainImg("");
				}
				mc.setTitle(title);
				mc.setContent(content);
				mc.setShowStatus(0);
				mc.setAddTime(CurrentTime.getCurrentTime());
				mc.setMessageType(msgType);
				mc.setPrimaryId(primaryId);
				mc.setPrimaryType(primaryType);
				mc.setAddUserId(addUserId);
				mc.setToUserId(toUserId);
				mc.setReadStatus(0);
				mcId = mcService.saveOrUpdate(mc);
			}else {
				status = 70001;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status = 1000;
		}
		return ResponseFormat.retParam(status, mcId);
	}
	
	@PutMapping("/updateByReadSta")
	@ApiOperation(value = "修改消息中心已读状态--针对系统通知和留言回复用", notes = "修改消息中心已读状态")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
			 @ApiResponse(code = 50001, message = "数据未找到")
		 })
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "消息中心编号", required = true),
			@ApiImplicitParam(name = "readSta", value = "已读状态（0：未读，1：已读）", defaultValue = "1", required = true),
		 })
	public GenericResponse updateByReadSta(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id",request);
		Integer readSta = CommonTools.getFinalInteger("readSta",request);
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
	
	@PutMapping("/updateByshowSta")
	@ApiOperation(value = "修改消息中心显示状态--新闻资讯用", notes = "修改消息中心显示状态--新闻资讯用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			 @ApiResponse(code = 200, message = "成功"),
			 @ApiResponse(code = 50001, message = "数据未找到"),
			 @ApiResponse(code = 70001, message = "无权限访问"),
			 @ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "消息中心编号", required = true),
			@ApiImplicitParam(name = "showSta", value = "显示状态（0：默认显示，1：隐藏）", required = true)
		 })
	public GenericResponse updateByshowSta(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id",request);
		Integer showStatus =  CommonTools.getFinalInteger("showSta", request);
		Integer status = 200;
			try {
				if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request), Constants.UP_MSG)) {
					MessageCenter mc = mcService.getEntityById(id);
					if (mc == null) {
						status = 50001;
					} else {
						mc.setShowStatus(showStatus);
						mcService.saveOrUpdate(mc);
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
	
	@PutMapping("/updateDetailById")
	@ApiOperation(value = "修改消息中心明细", notes = "修改消息中心明细")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			 @ApiResponse(code = 200, message = "成功"),
			 @ApiResponse(code = 50001, message = "数据未找到"),
			 @ApiResponse(code = 70001, message = "无权限访问"),
			 @ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "消息中心编号", required = true),
			@ApiImplicitParam(name = "title", value = "标题）", required = true),
			@ApiImplicitParam(name = "content", value = "标题）", required = true),
			@ApiImplicitParam(name = "mainImg", value = "新闻封面图-新闻资讯时需传递")
	})
	public GenericResponse updateDetailById(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id",request);
		String title = CommonTools.getFinalStr("title", request);
		String content =  CommonTools.getFinalStr("content", request);
		String mainImg = CommonTools.getFinalStr("mainImg", request);
		Integer status = 200;
			try {
				if(title.equals("") || content.equals("")) {
					status = 10002;
				}else {
					//需要判断权限
					if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request), Constants.UP_MSG)) {
						MessageCenter mc = mcService.getEntityById(id);
						if (mc == null) {
							status = 50001;
						} else {
							if(mc.getMessageType() == 1) {
								if(mainImg.equals("")) {
									mc.setMainImg("");
								}else if(!mainImg.equals(mc.getMainImg())) {//图片不相同
									mc.setMainImg(CommonTools.dealUploadDetail(CommonTools.getLoginUserId(request), mc.getMainImg(), mainImg));
								}
							}
							mc.setContent(content);
							mc.setTitle(title);
							mcService.saveOrUpdate(mc);
						}
					}else {
						status = 70001;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		return ResponseFormat.retParam(status, "");
	}
	
	@PutMapping("/delSpecMsg")
	@ApiOperation(value = "删除指定新闻--新闻资讯用", notes = "删除指定新闻--新闻资讯用")
	@ApiResponses({ @ApiResponse(code = 1000, message = "服务器错误"), 
			 @ApiResponse(code = 200, message = "成功"),
			 @ApiResponse(code = 50001, message = "数据未找到"),
			 @ApiResponse(code = 70001, message = "无权限访问"),
			 @ApiResponse(code = 10002, message = "参数为空")
	})
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "消息中心编号", required = true)})
	public GenericResponse delSpecMsg(HttpServletRequest request) {
		String id = CommonTools.getFinalStr("id",request);
		Integer status = 200;
			try {
				if(id.equals("") ) {
					status = 10002;
				}else {
					//需要判断权限
					if(CommonTools.checkAuthorization(CommonTools.getLoginUserId(request), CommonTools.getLoginRoleName(request), Constants.DEL_MSG)) {
						MessageCenter mc = mcService.getEntityById(id);
						if (mc == null) {
							status = 50001;
						} else {
							mcService.delMsgById(id);
						}
					}else {
						status = 70001;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = 1000;
			}
		return ResponseFormat.retParam(status, "");
	}
}
