package com.lng.service;

import org.springframework.data.domain.Page;

import com.lng.pojo.DriverQz;

public interface DriverQzService {
	/**
	 * 
	 * @description 添加修改司机求职
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月13日 下午4:46:19
	 * @param driverQz 司机求职实体
	 * @return
	 */
	public String saveOrUpdate(DriverQz driverQz);

	/**
	 * 
	 * @description 根据主键获取司机求职实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月13日 下午4:46:49
	 * @param id 主键
	 * @return
	 */
	DriverQz getEntityById(String id);

	/**
	 * 
	 * @description 根据条件获取司机求职信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月13日 下午4:47:17
	 * @param userId 用户编号
	 * @param jzYear 驾龄
	 * @param jzType 驾照类型
	 * @param wage 薪资
	 * @param checkSta 审核状态
	 * @param showSta 上下架状态
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<DriverQz> getDriverQzByOption(String userId, Integer jzYear, String jzType, String wage, Integer checkSta,
			Integer showSta, Integer pageNo, Integer pageSize);

}
