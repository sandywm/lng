package com.lng.service;

import java.util.List;

import com.lng.pojo.TrucksPotPp;

public interface TrucksPotPpService {
	/**
	 * 
	 * @description 添加和修改槽车储罐品牌
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:34:08
	 * @param trucksPotPp 槽车储罐品牌实体
	 * @return
	 */
	public String saveOrUpdate(TrucksPotPp trucksPotPp);
	/**
	 * 
	 * @description 根据槽车储罐品牌查看类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:35:55
	 * @param id 槽车储罐品牌编号
	 * @return
	 */
	TrucksPotPp findById(String id);
	/**
	 * 
	 * @description 根据槽车储罐品牌名称获取槽车储罐品牌信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:36:53
	 * @param name 槽车储罐品牌名称
	 * @return
	 */
	List<TrucksPotPp> getTrucksPotPpByNameList(String name);

}
