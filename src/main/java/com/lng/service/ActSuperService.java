package com.lng.service;

import java.util.List;

import com.lng.pojo.ActSuper;

public interface ActSuperService {

	/**
	 * @description 根据用户编号、指定动作模块编号获取模块动作列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 上午9:51:32
	 * @param userId 后台用户编号
	 * @param maId 动作模块编号（可为空）
	 * @return
	 */
	List<ActSuper> listSpecInfoByUserId(String userId,String maId);
	
	/**
	 * @description 根据用户编号、指定动作模块获取模块动作列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 上午11:55:59
	 * @param userId 后台用户编号
	 * @param actNameEng 块动作ENG 
	 * @return
	 */
	List<ActSuper> listSpecInfoByOpt(String userId,String actNameEng);
	
	/**
	 * @description 批量删除指定编号的信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 下午1:55:11
	 * @param asList 用户模块关联
	 */
	void delBatchInfo(List<ActSuper> asList);
	
	/**
	 * @description 为指定用户批量增加权限
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 下午2:05:53
	 * @param selMaIdStr 权限动作（多个逗号隔开）
	 */
	void addBatchInfo(String userId,String selMaIdStr);
}
