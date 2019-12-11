package com.lng.service;

import java.util.List;

import com.lng.pojo.LngInfo;

public interface LngService {

	/**
	 * @description 增加或者修改lng行情列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月11日 上午10:35:39
	 * @param lng
	 * @return
	 */
	String addOrUpdate(LngInfo lng);
	
	/**
	 * @description 根据条件获取lng行情列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月11日 上午10:36:01
	 * @param province 省份
	 * @param gsNamePy 液厂名称拼音
	 * @param specDate 指定时间(昨天，今天，明天三天)
	 * @return
	 */
	List<LngInfo> listInfoByOpt(String province,String gsNamePy,String specDate);
}
