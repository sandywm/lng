package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.Company;

public interface CompanyService {
	/**
	 * 
	 * @description 添加修改公司
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月9日 上午9:36:46
	 * @param company 公司实体
	 * @return
	 */
	public String saveOrUpdate(Company company);

	/**
	 * 
	 * @description 根据公司编号获取公司实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月9日 上午9:37:27
	 * @param id 公司编号
	 * @return
	 */
	Company getEntityById(String id);

	/**
	 * 
	 * @description 根据公司名称,公司类型分页获取公司列表
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月9日 上午9:50:37
	 * @param name     公司名称
	 * @param typeId   公司类型
	 * @param checkSta 审核状态
	 * @param pageNo   第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<Company> getCompanyList(String name, String typeId,int checkSta, Integer pageNo, Integer pageSize);
	/**
	 * 
	 * @description 根据公司名获取公司信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月9日 下午2:46:34
	 * @param name 公司名称
	 * @return
	 */
	List<Company> getCompanyByName(String name);
	
}
