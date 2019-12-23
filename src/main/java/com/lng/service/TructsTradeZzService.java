package com.lng.service;

import java.util.List;

import com.lng.pojo.TructsTradeZz;

public interface TructsTradeZzService {
	/**
	 * 
	 * @description 添加修改槽车租卖图片
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午9:30:39
	 * @param ttzz 槽车租卖图片实体
	 * @return
	 */
	String addOrUpdate(TructsTradeZz ttzz);
	/**
	 * 
	 * @description 根据槽车租卖图片主键获取实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:16:34
	 * @param id 槽车租卖图片主键
	 * @return
	 */
	TructsTradeZz getEntityById(String id);
	/**
	 * 
	 * @description 根据主键删除
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:17:01
	 * @param id 主键
	 */
	public void delete(String id);
	/**
	 * 
	 * @description  通过槽车租卖编号获取列表
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:18:26
	 * @param zzId 槽车租卖编号
	 * @return
	 */
	List<TructsTradeZz> getTructsTradeZzByttId(String ttId);
	/**
	 * 
	 * @description  批量保存槽车租卖图片
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:50:23
	 * @param zz 槽车租卖图片列表
	 */
	void addOrUpdateBatch(List<TructsTradeZz> zz);
	/**
	 * 
	 * @description 批量删除槽车租卖图片
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午11:50:29
	 * @param zz 槽车租卖图片列表
	 */
	void deleteBatch(List<TructsTradeZz> zz);

}
