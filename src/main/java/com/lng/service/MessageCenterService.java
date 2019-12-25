package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.MessageCenter;

public interface MessageCenterService {
	/**
	 * 
	 * @description 添加修改消息中心
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月20日 上午9:06:49
	 * @param mc 消息中心实体
	 * @return
	 */
	 public String saveOrUpdate(MessageCenter mc);
	 /**
	  * 
	  * @description 根据消息主键获取消息中心实体
	  * @author zdf
	  * @Version : 1.0
	  * @ModifiedBy : 
	  * @date  2019年12月20日 上午9:07:58
	  * @param id 主键
	  * @return
	  */
	 MessageCenter getEntityById(String id);
	 /**
	  * 
	  * @description 根据接收人,已读状态获取消息中心
	  * @author zdf
	  * @Version : 1.0
	  * @ModifiedBy : 
	  * @date  2019年12月20日 上午9:09:09
	  * @param msgTypeId 消息类型（1：新闻资讯，2：系统通知，3：留言回复，4：价格变动）
	  * @param toUserId 接收人
	  * @param readSta 已读状态（0：未读，1：已读）
	  * @param showStatus 显示状态（0：默认显示，1：隐藏）
	  * @param pageNo 第几页
	  * @param pageSize 每页多少条
	  * @return
	  */
	 Page<MessageCenter> getMessageCenterByOption(Integer msgTypeId,String toUserId,Integer showStatus,Integer readSta,
			 Integer pageNo,Integer pageSize);
	 
	 /**
	  * @description 根据条件获取指定日期时间段的消息
	  * @author wm
	  * @Version : 1.0
	  * @ModifiedBy : 修改人
	  * @date  2019年12月25日 下午1:14:11
	  * @param msgTypeId 消息类型（1：新闻资讯，2：系统通知，3：留言回复，4：价格变动）
	  * @param sTime 开始时间
	  * @param eTime 结束时间
	  * @return
	  */
	 List<MessageCenter> listMsgByOpt(Integer msgTypeId,String sTime,String eTime);

}
