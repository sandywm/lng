package com.lng.service;

import java.util.List;

import com.lng.pojo.CompanyType;

public interface CompanyTypeService {
	/**
	 * 
	 * @description 添加或修改公司类型
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 上午11:51:13
	 * @param ctype 公司类型实体
	 * @return
	 */
	String saveOrUpdate(CompanyType ctype);
	/**
	 * 
	 * @description 根据公司类型编号查看公司类型
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 上午11:54:51
	 * @param id 公司类型编号
	 * @return
	 */
	CompanyType findById(String id);
	/**
	 * 
	 * @description 获取所有的公司类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 上午11:55:27
	 * @param status 状态（0：全部，1：加入公司展示）
	 * @return  公司列表
	 */
	List<CompanyType> getCompanyTypeList(Integer status);
	/**
	 * 
	 * @description 根据公司类型名称获取公司类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 上午11:56:07
	 * @param name  公司类型名称
	 * @return
	 */
	List<CompanyType> getCompanyTypeByNameList(String name);
}
