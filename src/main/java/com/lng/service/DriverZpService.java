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
	 * @param jzYear 驾龄
	 * @param checkSta 审核状态
	 * @param showSta 上下架状态
	 * @param wage 薪资范围
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<DriverZp> getDriverQzByOption(String compId, String jzType, String jzYear,Integer checkSta, Integer showSta, String wage,
			Integer pageNo, Integer pageSize);
	
	/**
	 * @description 根据条件获取司机招聘信息
	 * @author wmk
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月13日 下午21:50:19
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @param checkSta 审核状态
	 * @param showSta 上下架状态
	 * @return
	 */
	List<DriverZp> listDriverZpByOpt(String sDate,String eDate,Integer checkSta, Integer showSta);
	
	/**
	 * @description 根据创建人，审核状态，上下架状态获取发布招聘信息列表
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年1月7日 下午1:28:25
	 * @param addUserId 发布人
	 * @param checkSta 审核状态（0:未审核,1:审核通过,2:审核未通过，-1：全部）
	 * @param showSta 上下架状态（0：上架，1：下架，-1：全部）
	 * @return
	 */
	List<DriverZp> listDriverZpByOpt(String addUserId,Integer checkSta, Integer showSta);
}
