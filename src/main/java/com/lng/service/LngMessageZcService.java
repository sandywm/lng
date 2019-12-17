package com.lng.service;

import java.util.List;

import com.lng.pojo.LngMessageUserZc;

public interface LngMessageZcService {
	/**
	 * 
	 * @description 添加LNG行情支持明细
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月16日 下午2:55:26
	 * @param lmZc LNG行情支持明细
	 * @return
	 */
	String  addLmZc(LngMessageUserZc lmZc);
	/**
	 * 
	 * @description 检查是否对当前LNG行情留言支持
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月16日 下午2:55:31
	 * @param userId 用户编号
	 * @param msgId LNG留言编号
	 * @return
	 */
	List<LngMessageUserZc> getLmZc(String userId,String msgId);
}
