package com.lng.service;

import java.util.List;

import com.lng.pojo.LngPriceRemark;

public interface LngPriceRemarkService {

	/**
	 * @description 根据液厂编号获取液厂备注列表
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月30日 下午4:36:15
	 * @param gfId
	 * @return
	 */
	List<LngPriceRemark> listInfoByGfId(String gfId);
	
	/**
	 * @description 增加或者修改
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月30日 下午5:21:24
	 * @param lpr
	 * @return
	 */
	String saveOrUpdate(LngPriceRemark lpr);
}
