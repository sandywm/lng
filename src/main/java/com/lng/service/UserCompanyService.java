package com.lng.service;

import java.util.List;

import com.lng.pojo.UserCompany;

public interface UserCompanyService {
	/**
	 * 
	 * @description 添加修改用户公司关联信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月16日 下午3:56:05
	 * @param uc 用户公司关联实体
	 * @return
	 */
	String addOrUpdate(UserCompany uc);
	/**
	 * 
	 * @description 根据主键获取用户公司关联实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月16日 下午3:56:54
	 * @param id 主键
	 * @return
	 */
	UserCompany getEntityId(String id);
	/**
	 * 
	 * @description 根据公司编号获取用户公司关联信息列表
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月16日 下午3:57:35
	 * @param compId 公司编号
	 * @param userId 用户编号
	 * @return
	 */
	List<UserCompany> getUserCompanyList(String compId,String userId);
}
