package com.lng.service;

import java.util.List;

import com.lng.pojo.PotZzjzType;

public interface PotZzjzTypeService {
	/**
	 * 
	 * @description 添加和修改装载介质类型
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:34:08
	 * @param qyType 装载介质类型实体
	 * @return
	 */
	public String saveOrUpdate(PotZzjzType potZzJzType);
	/**
	 * 
	 * @description 根据装载介质类型查看类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:35:55
	 * @param id 装载介质类型编号
	 * @return
	 */
	PotZzjzType findById(String id);
	/**
	 * 
	 * @description 根据装载介质类型名称获取起源类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:36:53
	 * @param name
	 * @return
	 */
	List<PotZzjzType> getPotZzjzTypeByNameList(String name);

}
