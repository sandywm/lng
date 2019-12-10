package com.lng.service;

import java.util.List;

import com.lng.pojo.Department;

public interface DepartmentService {

	/**
	 * @description 根据主键获取实体信息(显示状态为正常)
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午3:39:04
	 * @param depId 身份主键
	 * @return
	 */
	Department getEntityById(String depId);
	
	/**
	 * @description 增加或者修改身份信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午3:39:20
	 * @param dep 身份实体
	 * @return
	 */
	String addOrUpDepartment(Department dep);
	
	/**
	 * @description 根据身份名称获取身份信息(显示状态为正常)
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午3:40:35
	 * @param depName 身份名称(为空时表示全部)
	 * @return
	 */
	List<Department> findSpecInfo(String depName);
	
	/**
	 * @description 根据主键删除身份信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月10日 下午2:24:21
	 * @param id 主键
	 */
	void delInfoById(String id);
}
