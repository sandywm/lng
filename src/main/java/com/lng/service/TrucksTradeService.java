package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.TrucksTrade;

public interface TrucksTradeService {

	/**
	 * 
	 * @description 添加 修改货车租卖
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy :
	 * @date  2019年12月13日 上午8:37:18
	 * @param potTrade 货车租卖实体
	 * @return
	 */
	public String saveOrUpdate(TrucksTrade trucksTrade);
	/**
	 * 
	 * @description 根据主键查询货车租卖
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月13日 上午8:40:58
	 * @param id 主题
	 * @return
	 */
	TrucksTrade getEntityById(String id);
	/**
	 * 
	 * @description 根据条件分页查看货车租卖
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月13日 上午8:41:50
	 * @param checkSta 审核状态
	 * @param addUserId 上传人员
	 * @param tradeType 贸易类型
	 * @param showStatus  上/下架状态
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<TrucksTrade> getTrucksTradeByOption(Integer checkSta, String addUserId, Integer tradeType,Integer showStatus, Integer pageNo,
			Integer pageSize);
	
	/**
	 * @description 根据条件获取货车租卖记录列表
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月25日 下午2:26:12
	 * @param checkSta 审核状态
	 * @param showStatus 上下架状态
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @return
	 */
	List<TrucksTrade> listTrucksTradeByOpt(Integer checkSta,Integer showStatus,String sDate,String eDate);

}
