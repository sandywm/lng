package com.lng.service;

import java.util.List;

import com.lng.pojo.LngPriceDetail;

public interface LngPriceDetailService {

	/**
	 * @description 增加或者修改燃气价格行情
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月11日 下午4:47:05
	 * @param lpd
	 * @return
	 */
	String addOrUpdate(LngPriceDetail lpd);
	
	/**
	 * @description 批量增加燃气行情
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月11日 下午4:49:41
	 * @param lpdList
	 */
	void saveBatch(List<LngPriceDetail> lpdList);
}
