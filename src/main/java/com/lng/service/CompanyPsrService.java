package com.lng.service;

import java.util.List;


import com.lng.pojo.CompanyPsr;

public interface CompanyPsrService {

	/**
	 * 
	 * @description 添加或修改公司押运人
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:35:32
	 * @param comPsr 公司押运人实体
	 * @return
	 */
	public String saveOrUpdate(CompanyPsr comPsr);

	/**
	 * 
	 * @description 通过主键加载公司押运人实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:36:14
	 * @param id 公司押运人主键
	 * @return
	 */
	CompanyPsr getEntityById(String id);

	/**
	 * 
	 * @description 通过公司名称查询公司押运人信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:38:00
	 * @param compId 公司编号
	 * @return
	 */
	List<CompanyPsr> getCompanyPsrList(String compId);
	

}
