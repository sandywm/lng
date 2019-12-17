package com.lng.service;


import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.LngMessage;
import com.lng.pojo.LngMessageReply;

public interface LngMessageService {

	/**
	 * @description 增加lng行情留言
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月15日 下午1:31:16
	 * @param lm 实体
	 * @return
	 */
	String addOrUpdateLngMsg(LngMessage lm);
	/**
	 * 
	 * @description 根据主键获取LNG行情留言
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月16日 下午1:46:55
	 * @param id 主键
	 * @return
	 */
	LngMessage getEntityById(String id);
	
	/**
	 * @description 根据条件分页获取lng行情留言
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月15日 下午1:33:03
	 * @param userId 发布人
	 * @param content 内容
	 * @param checkStatus 审核状态(0:未审核,1:审核通过,2:审核未通过)
	 * @param showStatus 显示状态（0：显示，1：隐藏）
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	Page<LngMessage> listPageMsgInfoByOpt(String userId,Integer checkStatus,Integer showStatus,Integer pageIndex,Integer pageSize);
	
	/**
	 * @description 增加lng行情留言回复
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月15日 下午4:51:51
	 * @param lmr
	 * @return
	 */
	String addOrUpdateLngMsg(LngMessageReply lmr);
	
	/**
	 * @description 获取指定留言的回复内容
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月15日 下午4:53:18
	 * @param msgId 留言编号
	 * @param checkStatus 审核状态(0:未审核,1:审核通过,2:审核未通过)
	 * @param showStatus 显示状态（0：显示，1：隐藏）
	 * @return
	 */
	List<LngMessageReply> listReplyMsgByMsdId(String msgId,Integer checkStatus,Integer showStatus);
}
