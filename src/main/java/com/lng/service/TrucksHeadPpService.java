package com.lng.service;

import java.util.List;

import com.lng.pojo.TrucksHeadPp;

public interface TrucksHeadPpService {
	/**
	 * 
	 * @description 添加和修改槽车车头品牌
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:34:08
	 * @param trucksHeadPp 槽车车头品牌实体
	 * @return
	 */
	public String saveOrUpdate(TrucksHeadPp trucksHeadPp);
	/**
	 * 
	 * @description 根据槽车车头品牌查看类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:35:55
	 * @param id 槽车车头品牌编号
	 * @return
	 */
	TrucksHeadPp findById(String id);
	/**
	 * 
	 * @description 根据槽车车头品牌名称获取槽车车头品牌信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:36:53
	 * @param name 槽车车头品牌名称
	 * @return
	 */
	List<TrucksHeadPp> getTrucksHeadPpByNameList(String name);

}
