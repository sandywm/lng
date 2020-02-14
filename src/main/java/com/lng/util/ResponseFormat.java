package com.lng.util;

import java.util.HashMap;
import java.util.Map;


/**
 * 返回code代码
 * @author Administrator
 *
 */
public class ResponseFormat {
	private static Map<Integer,String> messageMap = new HashMap<Integer,String>();
	
    //初始化状态码与文字说明
    static {
        /* 成功状态码 */
        messageMap.put(200, "成功");

        /* 服务器错误 */
        messageMap.put(1000,"服务器错误");

        /* 参数错误：10001-19999 */
        messageMap.put(10001, "参数无效");
        messageMap.put(10002, "参数为空");
        messageMap.put(10003, "参数类型错误");
        messageMap.put(10004, "参数缺失");

        /* 用户错误：20001-29999*/
        messageMap.put(20001, "用户未登录");
        messageMap.put(20002, "账号不存在或密码错误");
        messageMap.put(20003, "账号已被禁用");
        messageMap.put(20004, "用户不存在");
        messageMap.put(20005, "用户已存在");
        messageMap.put(20006, "验证码错误");
        messageMap.put(20007, "密码错误");
        messageMap.put(20008, "授权失败");

        /* 业务错误：30001-39999 */
        messageMap.put(30001, "某业务出现问题");
        messageMap.put(30002, "该交易已存在确认订单，不能再进行确认操作");
        messageMap.put(30003, "该交易审核未通过或已下架，不能进行操作");
        messageMap.put(30004, "该产品正在交易中，不能进行下单");
        messageMap.put(30005, "您已下过单，不能重复下单");
        messageMap.put(30006, "不能删除已确认订单");
        messageMap.put(30007, "不能购买自己的产品");

        /* 系统错误：40001-49999 */
        messageMap.put(40001, "系统繁忙，请稍后重试");

        /* 数据错误：50001-599999 */
        messageMap.put(50001, "数据未找到");
        messageMap.put(50002, "数据有误");
        messageMap.put(50003, "数据已存在");
        messageMap.put(50004,"查询出错");
        messageMap.put(50005,"超过最大数量");

        /* 接口错误：60001-69999 */
        messageMap.put(60001, "内部系统接口调用异常");
        messageMap.put(60002, "外部系统接口调用异常");
        messageMap.put(60003, "该接口禁止访问");
        messageMap.put(60004, "接口地址无效");
        messageMap.put(60005, "接口请求超时");
        messageMap.put(60006, "接口负载过高");

        /* 权限错误：70001-79999 */
        messageMap.put(70001, "无权限访问");
        
        /* 修改错误：80001-89999 */
        messageMap.put(80001, "审核通过不能修改");
        messageMap.put(80002, "审核通过才能进行上/下架");
        messageMap.put(80003, "存在交易订单，不能进行上/下架");
    }
    
    public static String getMessage(Integer code) {
    	return messageMap.get(code);
    }
    
    /**
     * 不带分页的返回封装
     * @param <T>
     * @param status
     * @param data
     * @return
     */
    public static GenericResponse retParam(Integer status,Object data) {
        GenericResponse json = new GenericResponse(status, ResponseFormat.getMessage(status), data);
        return json;
    }

    /**
     * 带分页的数据返回封装
     * @param limit
     * @param page
     * @param count
     * @param status
     * @param data
     * @return
     */
    public static PageResponse getPageJson(Integer limit, Integer page, Long count,Integer status,Object data) {
    	PageResponse json = new PageResponse(limit, page,count ,status, ResponseFormat.getMessage(status), data);
        return json;
    }
    
}
