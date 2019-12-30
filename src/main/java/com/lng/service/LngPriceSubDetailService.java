package com.lng.service;

import java.util.List;

import com.lng.pojo.LngPriceSubDetail;

public interface LngPriceSubDetailService {

	/**
	 * @description 根据价格编号,指定时间段获取价格明细列表
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月30日 下午4:34:42
	 * @param lpdId 价格编号
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @return
	 */
	List<LngPriceSubDetail> listInfoByLpdId(String lpdId,String sDate,String eDate);
	
	/**
	 * @description 增加或者修改
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月30日 下午5:06:24
	 * @param lpsd
	 * @return
	 */
	String saveOrUpdate(LngPriceSubDetail lpsd);
}
