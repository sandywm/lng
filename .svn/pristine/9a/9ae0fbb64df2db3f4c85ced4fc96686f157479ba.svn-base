package com.lng.service;

import java.util.List;

import com.lng.pojo.Module;

public interface ModuleService {

	/**
	 * @description 获取平台所有模块列表（后台超管分配权限用）
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 上午8:51:59
	 * @return
	 */
	List<Module> listAllInfo();
	
	/**
	 * @description 增加或者修改模块
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 上午11:43:20
	 * @param mod
	 * @return
	 */
	String addOrUpMod(Module mod);
	
	/**
	 * @description 根据模块名称获取模块列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 上午11:47:26
	 * @param modName 模块名称
	 * @return
	 */
	List<Module> listSpecInfoByName(String modName);
	
	/**
	 * @description 根据主键获取模块实体信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 下午1:25:58
	 * @param modId 主键
	 * @return
	 */
	Module getEntityById(String modId);
}
