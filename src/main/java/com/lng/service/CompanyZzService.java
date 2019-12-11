package com.lng.service;

import org.springframework.data.domain.Page;

import com.lng.pojo.CompanyZz;

public interface CompanyZzService {
	/**
	 * 
	 * @description 添加或修改公司执照
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:35:32
	 * @param comPsr 公司执照实体
	 * @return
	 */
	public String saveOrUpdate(CompanyZz compZz);

	/**
	 * 
	 * @description 通过主键加载公司执照实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:36:14
	 * @param id 公司执照主键
	 * @return
	 */
	CompanyZz getEntityById(String id);

	/**
	 * 
	 * @description 通过公司名称分页查询公司执照信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:38:00
	 * @param compId 公司编号
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<CompanyZz> getCompanyZzList(String compId,  Integer pageNo, Integer pageSize);
	

}
