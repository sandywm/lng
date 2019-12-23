package com.lng.service;

import java.util.List;

import com.lng.pojo.TrucksTradeQualification;

public interface TrucksTradeQualService {
	/**
	 * 
	 * @description 添加删除槽车租卖进港资质关联
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:23:47
	 * @param TTQual 槽车租卖进港资质关联实体
	 * @return
	 */
	String  addOrUpdate(TrucksTradeQualification TTQual);
	/**
	 * 
	 * @description 根据主键获取槽车租卖进港资质关联
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:24:20
	 * @param id 主键
	 * @return
	 */
	TrucksTradeQualification getEntityById(String id);
	/**
	 * 
	 * @description 根据主键删除槽车租卖进港资质关联信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:24:49
	 * @param id 主键
	 */
	public void delete(String id);
	/**
	 * 
	 * @description 根据槽车租卖编号获取槽车租卖进港资质关联信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:25:13
	 * @param ttId 槽车租卖编号
	 * @return
	 */
	List<TrucksTradeQualification> getTrucksTradeQualList(String ttId);
	/**
	 * 
	 * @description  批量添加租卖进港资质关联
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:38:19
	 * @param TTQual 租卖进港资质关联列表
	 */
	void addOrUpdateBatch(List<TrucksTradeQualification> TTQual);
	/**
	 * 
	 * @description 批量删除租卖进港资质关联
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:40:37
	 * @param TTQual
	 */
	void deleteBatch(List<TrucksTradeQualification> TTQual);

}
