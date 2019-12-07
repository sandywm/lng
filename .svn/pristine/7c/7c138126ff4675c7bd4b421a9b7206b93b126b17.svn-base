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
	 * @param depId 部门主键
	 * @return
	 */
	Department getEntityById(String depId);
	
	/**
	 * @description 增加或者修改部门信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午3:39:20
	 * @param dep 部门实体
	 * @return
	 */
	String addOrUpDepartment(Department dep);
	
	/**
	 * @description 根据部门名称获取部门信息(显示状态为正常)
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月4日 下午3:40:35
	 * @param depName 部门名称(为空时表示全部)
	 * @return
	 */
	List<Department> findSpecInfo(String depName);
}
