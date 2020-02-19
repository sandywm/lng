package com.lng.controller;

import com.lng.pojo.FeedBack;
import com.lng.pojo.User;
import com.lng.service.FeedBackService;
import com.lng.service.UserService;
import com.lng.tools.CommonTools;
import com.lng.tools.CurrentTime;
import com.lng.util.GenericResponse;
import com.lng.util.PageResponse;
import com.lng.util.ResponseFormat;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "意见反馈相关接口")
@RequestMapping("/feedBack")
public class FeedBackController {
    @Autowired
    private FeedBackService feedBackService;
    @Autowired
    private UserService userService;

    @PostMapping("/addFeedBack")
    @ApiOperation(value = "添加意见反馈", notes = "添加意见反馈")
    @ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "反馈人编号", required = true),
            @ApiImplicitParam(name = "typeName", value = "反馈类型 (1-功能异常,2-体验问题,3-新功能建议,4-其他)", required = true),
            @ApiImplicitParam(name = "content", value = "反馈内容", required = true),
            @ApiImplicitParam(name = "mobile", value = "联系电话", required = true),
    })
    public GenericResponse addFeedBack(HttpServletRequest request) {
        String userId = CommonTools.getFinalStr("userId", request);
        String typeName = CommonTools.getFinalStr("typeName", request);
        String content = CommonTools.getFinalStr("content", request);
        String mobile = CommonTools.getFinalStr("mobile", request);
        Integer status = 200;
        Integer fbId = 0;
        try {
            FeedBack fb = new FeedBack();
            User user = userService.getEntityById(userId);
            fb.setUser(user);
            fb.setTypeName(typeName);
            fb.setContent(content);
            fb.setMobile(mobile);
            fb.setReadStatus(0);
            fb.setAddTime(CurrentTime.getCurrentTime());
            fbId = feedBackService.saveOrUpdate(fb);
        } catch (Exception e) {
            e.printStackTrace();
            status = 1000;
        }
        return ResponseFormat.retParam(status, fbId);
    }

    @PutMapping("/updateReadStatus")
    @ApiOperation(value = "更新意见反馈已读状态", notes = "更新意见反馈已读状态")
    @ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 50001, message = "数据未找到")
    })
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "意见反馈编号", required = true),
            @ApiImplicitParam(name = "readStatus", value = "已读状态(0：未读，1：已读)", required = true)
    })
    public GenericResponse updateReadStatus(HttpServletRequest request) {
        Integer id = CommonTools.getFinalInteger("id", request);
        Integer readStatus = CommonTools.getFinalInteger("readStatus", request);
        Integer status = 200;
        try {
            FeedBack fb = feedBackService.getEntityById(id);
            if (fb == null) {
                status = 50001;
            } else {
                if (readStatus != null && !readStatus.equals(fb.getReadStatus())) {
                    fb.setReadStatus(readStatus);
                    feedBackService.saveOrUpdate(fb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = 1000;
        }
        return ResponseFormat.retParam(status, "");
    }

    @GetMapping("getFeedBackList")
    @ApiOperation(value = "分页获取意见反馈列表", notes = "分页获取意见反馈列表")
    @ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 50001, message = "数据未找到")})
    @ApiImplicitParams({@ApiImplicitParam(name = "readStatus", value = "已读状态(0：未读，1：已读)",defaultValue = "0"),
            @ApiImplicitParam(name = "sDate",value = "开始时间"),
            @ApiImplicitParam(name = "eDate",value = "结束时间"),
            @ApiImplicitParam(name = "page", value = "第几页"),
            @ApiImplicitParam(name = "limit", value = "每页多少条")
    })
    public PageResponse getFeedBackList(HttpServletRequest request) {
        Integer status = 200;
        Integer readStatus = CommonTools.getFinalInteger("readStatus", request);
        String sDate = CommonTools.getFinalStr("sDate",request);
        String eDate = CommonTools.getFinalStr("eDate",request);
        Integer page = CommonTools.getFinalInteger("page", request);
        Integer limit = CommonTools.getFinalInteger("limit", request);
        List<Object> list = new ArrayList<Object>();
        long count = 0;
        try {
            if (page.equals(0)) {
                page = 1;
            }
            if (limit.equals(0)) {
                limit = 20;
            }
            Page<FeedBack> feedBackPage = feedBackService.getFeedBackByOption(readStatus, sDate,eDate,page-1, limit);
            count = feedBackPage.getTotalElements();
            if (count > 0) {
                List<FeedBack> fbList = feedBackPage.getContent();
                for (FeedBack fb : fbList) {
                    Map<String, Object> map_d = new HashMap<String, Object>();
                    map_d.put("id", fb.getId());
                    map_d.put("typeName", fb.getTypeName());
                    map_d.put("content", fb.getContent());
                    map_d.put("mobile", fb.getMobile());
                    map_d.put("readStatus", fb.getReadStatus());
                    map_d.put("addTime", fb.getAddTime());
                    map_d.put("realName", fb.getUser().getRealName());
                    list.add(map_d);
                }
            } else {
                status = 50001;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = 1000;
        }

        return ResponseFormat.getPageJson(limit, page, count, status, list);
    }

    @GetMapping("getMyFeedBackList")
    @ApiOperation(value = "获取自己发布的意见反馈列表", notes = "获取自己发布的意见反馈列表")
    @ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"),
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 50001, message = "数据未找到")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "发布者编号")
    })
    public GenericResponse getMyFeedBackList(HttpServletRequest request) {
        Integer status = 200;
        String userId = CommonTools.getFinalStr("userId",request);
        List<Object> list = new ArrayList<Object>();
        try {
            List<FeedBack> fbList = feedBackService.listInfoByUserId(userId);
            if (fbList.size() > 0) {
                for (FeedBack fb : fbList) {
                    Map<String, Object> map_d = new HashMap<String, Object>();
                    map_d.put("id", fb.getId());
                    map_d.put("typeName", fb.getTypeName());
                    map_d.put("content", fb.getContent());
                    map_d.put("mobile", fb.getMobile());
                    map_d.put("readStatus", fb.getReadStatus());
                    map_d.put("addTime", fb.getAddTime());
                    map_d.put("realName", fb.getUser().getRealName());
                    list.add(map_d);
                }
            } else {
                status = 50001;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = 1000;
        }

        return ResponseFormat.retParam(status, list);
    }
    
    @GetMapping("/getFeedBackById")
    @ApiOperation(value = "根据主键获取意见反馈详细信息", notes = "根据主键获取意见反馈详细信息")
    @ApiResponses({@ApiResponse(code = 1000, message = "服务器错误"), @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 10002, message = "参数为空"), @ApiResponse(code = 50001, message = "数据未找到")})
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "意见反馈编号")
    })
    public GenericResponse getFeedBackById(HttpServletRequest request) {
        Integer status = 200;
        Integer fbId = CommonTools.getFinalInteger("id", request);
        List<Object> list = new ArrayList<Object>();
        try {
            if (fbId.equals(0)) {
                status = 10002;
            } else {
                FeedBack fb = feedBackService.getEntityById(fbId);
                if (fb == null) {
                    status = 50001;
                } else {
                    //修改意见反馈为已读
                    if (fb.getReadStatus().equals(0)){
                        fb.setReadStatus(1);
                        feedBackService.saveOrUpdate(fb);
                    }

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", fb.getId());
                    map.put("typeName", fb.getTypeName());
                    map.put("content", fb.getContent());
                    map.put("mobile", fb.getMobile());
                    map.put("readStatus", fb.getReadStatus());
                    map.put("addTime", fb.getAddTime());
                    map.put("realName", fb.getUser().getRealName());
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