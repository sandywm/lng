package com.lng.service;

import java.util.List;

import com.lng.pojo.GasTradePsArea;

public interface GasTradePsAreaService {

	/**
	 * @description 增加或者编辑燃气买卖配送区域
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月18日 下午3:45:31
	 * @param gtpa
	 * @return
	 */
	String saveAndUpdate(GasTradePsArea gtpa);
	
	/**
	 * @description 根据燃气买卖编号获取配送区域
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月18日 下午3:45:48
	 * @param gtId 燃气买卖编号
	 * @return
	 */
	List<GasTradePsArea> listInfoByGtId(String gtId);
	
	/**
	 * @description 批量增加燃气买卖配送区域
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月18日 下午3:51:10
	 * @param list
	 */
	void addBatchInfo(List<GasTradePsArea> list);
	
	/**
	 * @description 批量删除燃气买卖配送区域
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月18日 下午3:51:35
	 * @param list
	 */
	void delBatchInfo(List<GasTradePsArea> list);
}
