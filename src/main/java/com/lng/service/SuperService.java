package com.lng.service;

import java.util.List;

import com.lng.pojo.SuperUser;

public interface SuperService {
	
	/**
	 * @description 根据账号、密码获取后台管理人员
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 上午9:18:30
	 * @param account 账号
	 * @param password 密码（可以为空）
	 * @return
	 */
	List<SuperUser> findInfoByOpt(String account,String password);
	
	/**
	 * @description 根据主键获取后台人员实体信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 上午9:20:03
	 * @param superId 主键
	 * @return
	 */
	SuperUser getEntityById(String superId);
	
	/**
	 * @description 修改或者增加用户
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午4:02:32
	 * @param user
	 * @return
	 */
	String addOrUpUser(SuperUser user);
	
	/**
	 * @description 获取所有后台管理人员列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午4:44:19
	 * @return
	 */
	List<SuperUser> findAllInfo();
	
	/**
	 * @description 根据角色编号获取用户列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月10日 上午11:55:34
	 * @param roleId 角色编号
	 * @return
	 */
	List<SuperUser> findInfoByRoleId(String roleId);
	
}
