package com.lng.service;

import java.util.List;

import com.lng.pojo.SuperDep;

public interface SuperDepService {

	/**
	 * @description 根据用户编号获取用户身份关联列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 上午9:24:34
	 * @param userId 后台用户编号
	 * @return
	 */
	List<SuperDep> listSpecInfoByUserId(String userId);
	
	/**
	 * @description 根据身份编号获取用户身份关联列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月6日 下午2:15:33
	 * @param roleId 身份编号（""时不查询）
	 * @return
	 */
	List<SuperDep> listInfoByDepId(String roleId);
	
	/**
	 * @description 批量删除用户身份关联
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月7日 下午1:20:35
	 * @param sdList 用户身份关联列表
	 */
	void delBatch(List<SuperDep> sdList);
	
	
	/**
	 * @description 添加或者修改后台人员部门关联信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午4:24:08
	 * @param sp
	 * @return
	 */
	String addOrUpSuperDep(SuperDep sp);
	
	/**
	 * @description 根据用户编号，用户角色编号获取用户角色关联信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月7日 下午1:59:42
	 * @param userId 用户编号
	 * @param roleId 角色编号
	 * @return
	 */
	List<SuperDep> listInfoByOpt(String userId,String roleId);
}
