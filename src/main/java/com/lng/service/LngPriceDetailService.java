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
	
	/**
	 * @description 获取指定液厂、指定价格、指定日期的记录列表（同一个液厂同一价格一天只能录入一次）
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月12日 上午9:47:19
	 * @param gfId 液厂
	 * @param price 价格
	 * @param priceDate 价格日期
	 * @return
	 */
	List<LngPriceDetail> listInfoByOpt(String gfId,Integer price,String priceDate);
	
	/**
	 * @description 根据条件获取最近三天的价格行情列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月12日 上午11:55:47
	 * @param provPy 省份拼音(""不查询)
	 * @param gsId 液厂编号(""不查询)
	 * @param gsNamePy 液厂拼音(""不查询)
	 * @param orderStr 排序规则（asc,desc,""）
	 * @param sDate 开始时间
	 * @param sDate 结束时间
	 * @return
	 */
	List<LngPriceDetail> listInfoByOpt(String provPy,String gsId,String gsNamePy,String sDate,String eDate,String orderStr);
	
	/**
	 * @description 根据主键获取价格行情详情
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月14日 上午11:55:47
	 * @param lpdId 价格行情编号
	 * @return
	 */
	LngPriceDetail getEntityById(String lpdId);
}
