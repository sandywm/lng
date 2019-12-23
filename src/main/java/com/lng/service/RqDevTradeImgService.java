package com.lng.service;

import java.util.List;

import com.lng.pojo.RqDevTradeImg;

public interface RqDevTradeImgService {
	/**
	 * 
	 * @description 添加修改燃气设备买卖线图
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午9:18:18
	 * @param rdtImg 燃气设备买卖详图实体
	 * @return
	 */
	String addOrUpdate(RqDevTradeImg rdtImg);
	/**
	 * 
	 * @description 通过主键获取燃气设备买卖详图
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午9:19:23
	 * @param id
	 * @return
	 */
	RqDevTradeImg getEntityById(String id);
	
	/**
	 * 
	 * @description 删除指定的详图信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月4日 上午10:28:06
	 * @param id 详图编号
	 */
	public void delete(String id);
	/**
	 * 
	 * @description 通过燃气设备交易编号获取详图信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月23日 上午9:20:53
	 * @param rdtId 燃气设备交易编号
	 * @return
	 */
   List<RqDevTradeImg> getRdtImgByRdtId(String rdtId);
   /**
    * 
    * @description 批量添加修改燃气设备买卖线图
    * @author zdf
    * @Version : 1.0
    * @ModifiedBy : 
    * @date  2019年12月23日 上午11:59:49
    * @param rdtImg  燃气设备买卖线图列表
    */
   void addOrUpdateBatch( List<RqDevTradeImg> rdtImg);
   /**
    * 
    * @description批量删除燃气设备买卖线图
    * @author zdf
    * @Version : 1.0
    * @ModifiedBy : 
    * @date  2019年12月23日 上午11:59:53
    * @param rdtImg 燃气设备买卖线图列表
    */
   void deleteBatch( List<RqDevTradeImg> rdtImg);

}
