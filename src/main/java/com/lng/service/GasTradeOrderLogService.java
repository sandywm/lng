package com.lng.service;

import java.util.List;

import com.lng.pojo.GasTradeOrderLog;

public interface GasTradeOrderLogService {
	/**
	 * 
	 * @description 添加更新燃气交易订单日志
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月18日 下午2:35:45
	 * @param gtoLog 燃气交易订单日志实体
	 * @return
	 */
	String addOrUpdate(GasTradeOrderLog gtoLog);
	/**
	 * 
	 * @description 根据主键获取燃气交易订单日志实体
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月18日 下午2:38:05
	 * @param id 主键
	 * @return
	 */
	GasTradeOrderLog getEntityById(String id);
    /**
     * 	
     * @description 根据燃气交易订单编号获取日志信息(升序排列)
     * @author zdf
     * @Version : 1.0
     * @ModifiedBy : 
     * @date  2019年12月18日 下午2:38:43
     * @param gtoId 燃气交易订单编号
     * @return
     */
	List<GasTradeOrderLog> getGtLogList(String gtoId);
	
	/**
	 * @description 批量删除指定订单的日志
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月29日 上午10:57:47
	 * @param logList
	 */
	void delBatchLog(List<GasTradeOrderLog> logList);
	
	/**
     * 	
     * @description 根据燃气交易订单编号、订单状态获取日志信息(降序排列)
     * @author wm
     * @Version : 1.0
     * @ModifiedBy : 
     * @date  2019年12月18日 下午2:38:43
     * @param gtoId 燃气交易订单编号
     * @param orderStatus 订单编号（-2-7）(已取消，已拒绝，确认订单，待付款，确认收款，待收货，待付款，确认收款，待评价，已完成)
     * @return
     */
	List<GasTradeOrderLog> getGtLogList(String gtoId,Integer orderStatus);

}
