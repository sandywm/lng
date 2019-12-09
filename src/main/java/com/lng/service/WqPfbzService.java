package com.lng.service;

import java.util.List;

import com.lng.pojo.WqPfbz;

public interface WqPfbzService {
	/**
	 * 
	 * @description 添加和修改尾气排放标准
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:34:08
	 * @param wqPfbz 尾气排放标准实体
	 * @return
	 */
	public String saveOrUpdate(WqPfbz wqPfbz);
	/**
	 * 
	 * @description 根据尾气排放标准查看类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:35:55
	 * @param id 尾气排放标准编号
	 * @return
	 */
	WqPfbz findById(String id);
	/**
	 * 
	 * @description 根据尾气排放标准名称获取尾气排放标准信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:36:53
	 * @param name 尾气排放标准名称
	 * @return
	 */
	List<WqPfbz> getWqPfbzByNameList(String name);


}
