package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.GasTradeOrder;

public interface GasTradeOrderService {
	/**
	 * 
	 * @description 添加获取修改燃气交易订单
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月18日 下午2:19:48
	 * @param gto 燃气交易订单 实体
	 * @return
	 */
	String addOrUpdate(GasTradeOrder gto);
	/**
	 * 
	 * @description根据主键获取燃气交易订单实体 
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月18日 下午2:26:42
	 * @param id 主键
	 * @return
	 */
	GasTradeOrder getEntityById(String id);
	/**
	 * 
	 * @description 根据条件分页获取燃气交易订单
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月18日 下午2:27:11
	 * @param userId 用户编号
	 * @param compId 公司编号
	 * @param addTime 添加时间
	 * @param ordSta 订单状态(-3表示全部,-2表示未完成，7表示订单完成)
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<GasTradeOrder> listPageInfoByOpt(String userId,String compId,String sDate,String eDate,Integer ordSta,Integer pageNo,Integer pageSize);
	/**
	 * 
	 * @description 根据燃气交易编号获取信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月18日 下午2:28:23
	 * @param gtId
	 * @return
	 */
	List<GasTradeOrder> getInfoBygtId(String gtId);
	
	/**
	 * @description 根据卖气人所在公司获取所有已评价完成的燃气交易订单
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月20日 上午9:49:38
	 * @param cpyId 卖气人所在公司编号
	 * @return
	 */
	List<GasTradeOrder> listComInfoByCpyId(String cpyId);
	
	/**
	 * @description 根据用户编号、燃气买卖编号获取订单列表（防止重复提交）
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月24日 上午9:59:09
	 * @param userId 用户编号
	 * @param gtId 燃气交易编号
	 * @return
	 */
	List<GasTradeOrder> listComInfoByOpt(String userId,String gtId);
	

}
