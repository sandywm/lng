package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.UserFocus;

public interface UserFocusService {
	/**
	 * 
	 * @description  添加修改用户关注
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月16日 下午3:54:22
	 * @param uf 用户关注实体
	 * @return
	 */
	String addOrUpdage(UserFocus uf);
	/**
	 * 
	 * @description 根据主键获取用户关注实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月16日 下午3:54:50
	 * @param id 主键
	 * @return
	 */
	UserFocus getEntityId(String id);
	/**
	 * 
	 * @description 根据用户编号获取用户关注列表
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月16日 下午3:55:24
	 * @param userId 用户编号
	 * @param focusId 关注编号
	 * @param focusType 关注类型
	 * @return
	 */
	List<UserFocus> getUserFocusList(String userId,String focusId, String focusType);
	/**
	 * 
	 * @description 获取我的关注
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月24日 下午2:05:51
	 * @param userId 用户编号
	 * @param focusType 关注类型
	 * @param pageNo 第几页
	 * @param  pageSize 每页多少条
	 * @return
	 */
	Page<UserFocus> userFocusList(String userId, String focusType,Integer pageNo,Integer pageSize);
	/**
	 * 
	 * @description 取消关注
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月17日 上午9:56:00
	 * @param id 主键
	 */
	public void delete(String id);
}
