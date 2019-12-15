package com.lng.service;

import java.util.List;

import com.lng.pojo.CompanyTructsHeadCp;

public interface CompanyTructsHeadCpService {
	/**
	 * 
	 * @description 添加或修改车头车牌
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:35:32
	 * @param comPsr 车头车牌实体
	 * @return
	 */
	public String saveOrUpdate(CompanyTructsHeadCp tHeadCp);

	/**
	 * 
	 * @description 通过主键加载车头车牌实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:36:14
	 * @param id 车头车牌主键
	 * @return
	 */
	CompanyTructsHeadCp getEntityById(String id);

	/**
	 * 
	 * @description 通过公司名称分页查询车头车牌信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:38:00
	 * @param compId 公司编号
	 * @return
	 */
	List<CompanyTructsHeadCp> getTructsHeadCpList(String compId);
	/**
	 * 
	 * @description 根据车头车牌获取车头车牌信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 下午2:23:33
	 * @param cp 车牌
	 * @return
	 */
	List<CompanyTructsHeadCp> getHeadCpList(String cp);

}
