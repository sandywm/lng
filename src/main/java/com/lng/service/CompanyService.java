package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
	
	/**
	 * @description 根据公司类型编号、公司类型名称、公司创建人获取公司列表
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月24日 上午8:18:24
	 * @param typeId 公司类型（""不查询）
	 * @param typeName 公司类型名称（""不查询）
	 * @param owerUserId 公司创建人
	 * @param checkStatus 审核状态
	 * @return
	 */
	List<Company> listSpecCpy(String typeId,String typeName,String owerUserId,Integer checkStatus);
	
	/**
	 * @description 加入公司时获取的公司列表（审核通过）
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年1月17日 下午5:06:24
	 * @param cpyTypeId 公司类型编号
	 * @param userId 用户编号
	 * @param pageable 分页
	 * @return
	 */
	Page<Company> getPageCpyList(String cpyTypeId,String userId,Pageable pageable);
}
