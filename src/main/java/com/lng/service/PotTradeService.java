package com.lng.service;

import org.springframework.data.domain.Page;

import com.lng.pojo.PotTrade;

public interface PotTradeService {
	/**
	 * 
	 * @description 添加修改储罐租卖
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月11日 下午1:50:14
	 * @param potTrade 储罐租卖 实体
	 * @return
	 */
  public String saveOrUpdate(PotTrade potTrade);
  /**
   * 
   * @description 根据主键获取储罐租卖
   * @author zdf
   * @Version : 1.0
   * @ModifiedBy : 
   * @date  2019年12月11日 下午1:50:58
   * @param id 储罐租卖 主键
   * @return
   */
  PotTrade getEntityById(String id);
  /**
   * 
   * @description 根据条件分页查看储罐租卖信息
   * @author zdf
   * @Version : 1.0
   * @ModifiedBy : 
   * @date  2019年12月11日 下午1:51:27
   * @param potPpId 储罐品牌编号
   * @param potVol 储罐容积
   * @param sxInfo 有无手续
   * @param zzjzType  装载介质类型编号
   * @param checkSta 审核状态
   * @param pageNo 第几页
   * @param pageSize 每页多少条
   * @return
   */
  Page<PotTrade> getPotTradeByOption(String potPpId,Integer potVol,String sxInfo,String zzjzTypeId,Integer checkSta,Integer pageNo,Integer pageSize);

}
