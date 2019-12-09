package com.lng.service;

import java.util.List;

import com.lng.pojo.TrucksHeadType;

public interface TrucksHeadTypeService {
	/**
	 * 
	 * @description 添加和修改槽车车头类型
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:34:08
	 * @param trucksHeadType 槽车车头类型实体
	 * @return
	 */
	public String saveOrUpdate(TrucksHeadType trucksHeadType);
	/**
	 * 
	 * @description 根据槽车车头类型查看类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:35:55
	 * @param id 槽车车头类型编号
	 * @return
	 */
	TrucksHeadType findById(String id);
	/**
	 * 
	 * @description 根据槽车车头类型名称获取槽车车头类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:36:53
	 * @param name 槽车车头类型名称
	 * @return
	 */
	List<TrucksHeadType> getTrucksHeadTypeByNameList(String name);

}
