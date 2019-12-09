package com.lng.service;

import java.util.List;

import com.lng.pojo.RqDevType1;

public interface RqDevType1Service {
	
	/**
	 * 
	 * @description 添加和修改燃气设备类型
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:34:08
	 * @param rqDevType1 燃气设备类型实体
	 * @return
	 */
	public String saveOrUpdate(RqDevType1 rqDevType1);
	/**
	 * 
	 * @description 根据燃气设备类型查看类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:35:55
	 * @param id 燃气设备类型编号
	 * @return
	 */
	RqDevType1 findById(String id);
	/**
	 * 
	 * @description 根据燃气设备类型名称获取燃气设备类型信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月6日 下午2:36:53
	 * @param name
	 * @return
	 */
	List<RqDevType1> getRqDevType1ByNameList(String name);

}
