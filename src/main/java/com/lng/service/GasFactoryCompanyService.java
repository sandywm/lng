package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.GasFactoryCompany;

public interface GasFactoryCompanyService {

	/**
	 * @description 增加或者修改液厂贸易商关联
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 上午11:01:57
	 * @param gfc 实体
	 * @return
	 */
	String saveOrUpdate(GasFactoryCompany gfc);
	
	/**
	 * @description 根据指定液厂的贸易商列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月15日 下午1:43:23
	 * @param gfId 液厂编号（""不查询）
	 * @param cpyId 贸易商编号（""不查询）
	 * @param checkStatus 审核状态(0:未审核,1:审核通过,2:审核未通过)
	 * @return
	 */
	List<GasFactoryCompany> listCompanyByGfId(String gfId,String cpyId, Integer checkStatus);
	
	/**
	 * @description 根据主键获取实体信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 上午11:08:26
	 * @param id
	 * @return
	 */
	GasFactoryCompany getEntityById(String id);
	
	/**
	 * @description 根据条件分页获取液厂贸易商关联信息列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月18日 上午10:10:40
	 * @param gfName 液厂名称
	 * @param gfNamePy 液厂名称首字母
	 * @param checkStatus 审核状态(0:未审核,1:审核通过,2:审核未通过)
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<GasFactoryCompany> listPageCompanyByOpt(String gfName,String gfNamePy, Integer checkStatus,Integer pageNo,Integer pageSize);
	
	/**
	 * @description 根据条件获取液厂贸易商关联信息列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2020年01月31日 上午10:10:40
	 * @param gfId 液厂编号
	 * @param cpyId 贸易商公司编号
	 * @param checkStatus 审核状态(0:未审核,1:审核通过,2:审核未通过)
	 * @param applyUserId 申请人编号
	 * @return
	 */
	List<GasFactoryCompany> listCompanyByOpt(String gfId,String cpyId, Integer checkStatus,String applyUserId);
}
