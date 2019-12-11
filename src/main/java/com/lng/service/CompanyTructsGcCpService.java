package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.CompanyTructsGcCp;

public interface CompanyTructsGcCpService {
	/**
	 * 
	 * @description 添加或修改挂车车牌
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:35:32
	 * @param comPsr 挂车车牌实体
	 * @return
	 */
	public String saveOrUpdate(CompanyTructsGcCp tGcCp);

	/**
	 * 
	 * @description 通过主键加载挂车车牌实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:36:14
	 * @param id 挂车车牌主键
	 * @return
	 */
	CompanyTructsGcCp getEntityById(String id);

	/**
	 * 
	 * @description 通过公司名称分页查询挂车车牌信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 上午9:38:00
	 * @param compId 公司编号
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<CompanyTructsGcCp> getTructsGcCpList(String compId,  Integer pageNo, Integer pageSize);
	/**
	 * 
	 * @description 根据车牌获取挂车车牌信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月10日 下午2:21:55
	 * @param cp 挂车车牌
	 * @return
	 */
	List<CompanyTructsGcCp> getGcCpByName(String cp);
	
	


}
