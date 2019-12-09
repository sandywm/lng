package com.lng.service;

import java.util.List;

import com.lng.pojo.RqDevType;

public interface RqDevTypeService {
	/**
	 * 
	 * @description 添加和修改燃气设备类目
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:34:08
	 * @param rqDevType 燃气设备类目实体
	 * @return
	 */
	public String saveOrUpdate(RqDevType rqDevType);
	/**
	 * 
	 * @description 根据燃气设备类目查看类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:35:55
	 * @param id 燃气设备类目编号
	 * @return
	 */
	RqDevType findById(String id);
	/**
	 * 
	 * @description 根据燃气设备类目名称获取燃气设备类目信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:36:53
	 * @param name
	 * @return
	 */
	List<RqDevType> getRqDevTypeByNameList(String name);
}
