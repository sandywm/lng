package com.lng.service;

import java.util.List;

import com.lng.pojo.LngMessage;

public interface LngMessageService {

	/**
	 * @description 增加lng行情留言
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月15日 下午1:31:16
	 * @param userId 发布人
	 * @param content 内容
	 * @param checkStatus 审核状态(0:未审核,1:审核通过,2:审核未通过)
	 * @param showStatus 显示状态（0：显示，1：隐藏）
	 * @return
	 */
	String addOrUpdateLngMsg(String userId,String content,Integer checkStatus,Integer showStatus);
	
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
	List<LngMessage> listPageMsgInfoByOpt(String userId,Integer checkStatus,Integer showStatus,Integer pageIndex,Integer pageSize);
}
