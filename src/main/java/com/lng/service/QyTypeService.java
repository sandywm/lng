package com.lng.service;

import java.util.List;

import com.lng.pojo.QyType;

public interface QyTypeService {
	/**
	 * 
	 * @description 添加和修改气源类型
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:34:08
	 * @param qyType 气源类型实体
	 * @return
	 */
	public String saveOrUpdate(QyType qyType);
	/**
	 * 
	 * @description 根据气源类型查看类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:35:55
	 * @param id 气源类型编号
	 * @return
	 */
	QyType findById(String id);
	/**
	 * 
	 * @description 根据气源类型名称获取起源类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:36:53
	 * @param name
	 * @return
	 */
	List<QyType> getQyTypeByNameList(String name);

}
