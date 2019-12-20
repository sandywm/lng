package com.lng.service;

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
	  * @param toUserId 接收人
	  * @param readSta 已读状态
	  * @param pageNo 第几页
	  * @param pageSize 每页多少条
	  * @return
	  */
	 Page<MessageCenter> getMessageCenterByOption(String toUserId,Integer readSta,Integer pageNo,Integer pageSize);

}
