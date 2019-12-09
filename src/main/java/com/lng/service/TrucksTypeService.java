package com.lng.service;

import java.util.List;

import com.lng.pojo.TrucksType;

public interface TrucksTypeService {
	/**
	 * 
	 * @description 添加和修改槽车类型
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:34:08
	 * @param trucksType 槽车类型实体
	 * @return
	 */
	public String saveOrUpdate(TrucksType trucksType);
	/**
	 * 
	 * @description 根据槽车类型查看类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:35:55
	 * @param id 槽车类型编号
	 * @return
	 */
	TrucksType findById(String id);
	/**
	 * 
	 * @description 根据槽车类型名称获取槽车类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:36:53
	 * @param name 槽车类型名称
	 * @return
	 */
	List<TrucksType> getTrucksTypeByNameList(String name);


}
