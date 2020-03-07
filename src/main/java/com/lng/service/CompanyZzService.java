package com.lng.service;

import java.util.List;

import com.lng.pojo.CompanyZz;

public interface CompanyZzService {
	/**
	 * 
	 * @description 添加或修改公司资质
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:35:32
	 * @param comPsr 公司资质实体
	 * @return
	 */
	public String saveOrUpdate(CompanyZz compZz);
	/**
	 * 
	 * @description 批量更新或修改公司资质
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月17日 下午2:51:34
	 * @param zzList
	 */
	public void saveOrUpdateBatch(List<CompanyZz> zzList);
	/**
	 * 
	 * @description 批量 删除公司资质
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月17日 下午3:48:06
	 * @param zzList
	 */
	public void deleteBatch(List<CompanyZz> zzList);

	/**
	 * 
	 * @description 通过主键加载公司资质实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:36:14
	 * @param id 公司资质主键
	 * @return
	 */
	CompanyZz getEntityById(String id);

	/**
	 * 
	 * @description 通过公司编号查询公司资质信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : wm
	 * @date  2019年12月10日 上午9:38:00
	 * @param compId 公司编号
	 * @return
	 */
	List<CompanyZz> getCompanyZzList(String compId);
	

}
