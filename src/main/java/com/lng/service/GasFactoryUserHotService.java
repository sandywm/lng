package com.lng.service;

import java.util.List;

import com.lng.pojo.GasFactoryUserHot;

public interface GasFactoryUserHotService {

	/**
	 * @description 增加或者修改用户 点击 液厂支持率
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2020年01月19日 上午9:51:32
	 * @param gfuh 液厂用户点击实体
	 * @return
	 */
	String addOrUpdate(GasFactoryUserHot gfuh);
	
	/**
	 * @description 根据用户编号、指定液厂编号、指定日期获取用户 点击 液厂列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2020年01月19日 上午9:51:32
	 * @param userId 用户 编号
	 * @param addTime 点击事件
	 * @param gfId 液厂编号
	 * @return
	 */
	List<GasFactoryUserHot> listInfoByOpt(String userId,String addTime,String gfId);
}
