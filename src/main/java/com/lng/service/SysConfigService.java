package com.lng.service;

import java.util.List;

import com.lng.pojo.SystemInfo;

public interface SysConfigService {

	/**
	 * @description 获取系统配置信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月6日 下午5:16:44
	 * @return
	 */
	List<SystemInfo> findInfo();
	
	/**
	 * @description 增加或者修改配置信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月11日 上午10:59:23
	 * @param sys
	 * @return
	 */
	String addOrUpdate(SystemInfo sys);
}
