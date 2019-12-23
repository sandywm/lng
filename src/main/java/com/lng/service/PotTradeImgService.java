package com.lng.service;

import java.util.List;

import com.lng.pojo.PotTradeImg;

public interface PotTradeImgService {
	/**
	 * 
	 * @description 添加修改储罐租卖详情图
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月23日 上午9:25:21
	 * @param ptImg 储罐租卖详情图实体
	 * @return
	 */
	String addOrUpdate(PotTradeImg ptImg);

	/**
	 * 
	 * @description 通过主键获取储罐租卖详情图
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月23日 上午9:25:45
	 * @param id
	 * @return
	 */
	PotTradeImg getEntityById(String id);

	/**
	 * 
	 * @description 通过主键储罐租卖详情图
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月23日 上午9:26:26
	 * @param id 主键
	 */
	public void delete(String id);
	/**
	 * 
	 * @description   通过储罐买卖编号获取储罐租卖详情图
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午9:27:10
	 * @param PtId  储罐买卖编号
	 * @return
	 */
	List<PotTradeImg> getPotTradeImgByPtId(String PtId);
	/**
	 * 
	 * @description 批量添加储罐租卖详情图
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 下午1:23:59
	 * @param ptImg 储罐租卖详情图实体
	 */
	void addOrUpdateBatch(List<PotTradeImg> ptImg);
	/**
	 * 
	 * @description 批量删除储罐租卖详情图
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 下午1:24:30
	 * @param ptImg 储罐租卖详情图实体
	 */
	void deleteBatch(List<PotTradeImg> ptImg);

}
