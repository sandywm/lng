package com.lng.service;

import java.util.List;

import com.lng.pojo.ModuleAct;

public interface ModuleActService {

	/**
	 * @description 获取指定模块下的子模块列表（升序排列）
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 上午8:59:45
	 * @param modId 模块编号
	 * @return
	 */
	List<ModuleAct> listInfoByModId(String modId);
	
	/**
	 * @description 根据动作名称获取模块动作列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 下午1:19:37
	 * @param modId 模块编号
	 * @param actNameChi 模块动作中文
	 * @param actNameEng 模块动作英文
	 * @return
	 */
	List<ModuleAct> listInfoByOpt(String modId,String actNameChi,String actNameEng);
	
	/**
	 * @description 增加或者保存模块动作
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 下午1:23:17
	 * @param ma
	 * @return
	 */
	String addOrUpModuleAct(ModuleAct ma);
	
	/**
	 * @description 根据
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月5日 下午2:16:22
	 * @param maId
	 * @return
	 */
	ModuleAct getEntityById(String maId);
}
