package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.GasTrade;
import com.lng.pojo.GasTradeImg;

public interface GasTradeService {

	/**
	 * @description 增加或者修改燃气信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午2:58:03
	 * @param gt
	 * @return
	 */
	String saveOrUpdate(GasTrade gt);
	
	/**
	 * @description 根据主键获取燃气买卖详细信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午2:58:59
	 * @param id
	 * @return
	 */
	GasTrade getEntityById(String id);
	
	/**
	 * @description 根据条件分页获取燃气买卖列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午3:08:13
	 * @param cpyId 发布人所在的公司（后台人员发布时选择公司）
	 * @param addUserId 发布人
	 * @param gtId 液质类型编号
	 * @param gfId 液厂编号
	 * @param checkStatus 审核状态(0:未审核,1:审核通过,2:审核未通过)
	 * @param showStatus 上/下架状态（0：上架，1：下架）
	 * @param sPrice 价格1
	 * @param ePrice 价格2
	 * @param psArea 配送区域（多个用逗号隔开）
	 * @param pageNo 页码
	 * @param pageSize 每页记录条数
	 * @return
	 */
	Page<GasTrade> listPageInfoByOpt(String cpyId,String addUserId,String gtId,String gfId,Integer checkStatus,
			Integer showStatus,Integer sPrice,Integer ePrice,String psArea,Integer pageNo,Integer pageSize);
	/**
	 * 
	 * @description 根据用户编号获取燃气发布信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月26日 下午2:07:56
	 * @param userId 用户编号
	 * @param showStatus 上下架状态
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<GasTrade> gasTradeOnPublish(String userId,Integer showStatus,Integer pageNo,Integer pageSize);
	
	/**
	 * @description 增加或修改燃气买卖其他详图
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午4:51:14
	 * @param gti
	 * @return
	 */
	String saveOrUpdate(GasTradeImg gti);
	
	/**
	 * @description 根据主键获取燃气买卖详图实体信息
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午4:54:33
	 * @param id
	 * @return
	 */
	GasTradeImg getEntityById_1(String id);
	
	/**
	 * @description 获取指定燃气买卖的详图列表
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午4:51:57
	 * @param gtId 燃气买卖编号
	 * @return
	 */
	List<GasTradeImg> listInfoByGtId(String gtId);
	
	/**
	 * @description 批量删除所有燃气买卖详图
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午4:58:38
	 * @param gtiList
	 */
	void delBatchByGtId(List<GasTradeImg> gtiList);
	
	/**
	 * @description 批量添加燃气买卖详图
	 * @author wm
	 * @Version : 版本
	 * @ModifiedBy : 修改人
	 * @date  2019年12月16日 下午5:00:41
	 * @param gtiList
	 */
	void addBatchInfo(List<GasTradeImg> gtiList);
	
	/**
	 * @description 获取指定日期时间段的燃气贸易列表（时间降序排列）
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月25日 下午1:49:13
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @param checkStatus 审核状态(0:未审核,1:审核通过,2:审核未通过)
	 * @param showStatus 上/下架状态（0：上架，1：下架）
	 * @return
	 */
	Page<GasTrade> listInfoByOpt(String sDate,String eDate,Integer checkStatus,
			Integer showStatus, Integer pageNo,Integer pageSize);
	
	/**
	 * @description 获取指定日期时间段自己发布的燃气贸易列表（时间降序排列）--审核通过且上架
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2020年02月11日 下午1:49:13
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @param pubUserId 商户
	 * @return
	 */
	List<GasTrade> listTradingInfoByOpt(String sDate,String eDate,String pubUserId);
}
