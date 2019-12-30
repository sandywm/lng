package com.lng.service;

import org.springframework.data.domain.Page;

import com.lng.pojo.User;

public interface UserService {

	/**
	 * @description 增加或者编辑用户信息列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 上午9:00:35
	 * @param user 用户实体
	 * @return
	 */
	String saveAndUpdate(User user);
	
	/**
	 * @description 根据主键获取用户信息实体
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 上午9:00:56
	 * @param userId 用户编号
	 * @return
	 */
	User getEntityById(String userId);
	
	/**
	 * @description 根据微信号获取用户信息实体
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 上午9:27:02
	 * @param wxOpenId 微信授权号
	 * @return
	 */
	User getEntityByWxOpenId(String wxOpenId);
	
	/**
	 * @description 根据用户姓名分页获取用户信息列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 上午9:01:11
	 * @param wxName 微信授权名字
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<User> listPageInfoByWxName(String wxName,Integer pageNo,Integer pageSize);
}
