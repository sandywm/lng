package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.DriverZp;

public interface DriverZpService {
	/**
	 * 
	 * @description 添加修改司机招聘
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月13日 下午4:49:21
	 * @param driverZp 司机招聘实体
	 * @return
	 */
	public String saveOrUpdate(DriverZp driverZp);
	/**
	 * 
	 * @description 根据主键获取司机招聘信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月13日 下午4:49:47
	 * @param id 主键
	 * @return
	 */
	DriverZp getEntityById(String id);
	/**
	 * 
	 * @description 根据司机招聘获取司机招聘
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月25日 上午11:09:51
	 * @param cpyId 公司编号
	 * @return
	 */
	List<DriverZp> getDriverZpList(String cpyId);
	/**
	 * 
	 * @description 根据条件分页获取司机招聘信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月13日 下午4:50:19
	 * @param compId 公司编号
	 * @param jzType 驾照类型
	 * @param checkSta 审核状态
	 * @param showSta 上下架状态
	 * @param wage 薪资范围
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<DriverZp> getDriverQzByOption(String compId, String jzType, Integer checkSta, Integer showSta, String wage,
			Integer pageNo, Integer pageSize);
}
