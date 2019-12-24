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

}
