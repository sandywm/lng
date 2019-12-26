package com.lng.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.lng.pojo.RqDevTrade;

public interface RqDevTradeService {
	/**
	 * 
	 * @description 添加修改燃气设备买卖
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月12日 上午10:43:25
	 * @param rqDevTrade 燃气设备买卖实体
	 * @return
	 */
	public String saveOrUpdate(RqDevTrade rqDevTrade);

	/**
	 * 
	 * @description 根据主键获取燃气设备买卖
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月12日 上午11:07:30
	 * @param id  主键
	 * @return
	 */
	RqDevTrade getEntityById(String id);

	/**
	 * 
	 * @description 根据条件查询燃气设备买卖
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月12日 上午11:09:39
	 * @param compId 公司编号
	 * @param lmId 设备类目编号
	 * @param zlId 设备种类编号
	 * @param checkSta 审核状态
	 * @param showSta 上 下架状态
	 * @param addUserId 上传人员
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<RqDevTrade> getRqDevTradeList(String compId,String lmId,String zlId,Integer checkSta,Integer showSta,String addUserId, Integer pageNo, Integer pageSize);
	/**
	 * 
	 * @description 根据用户编号获取发布信息
	 * @author zdf
	 * @Version : 1.0
	 * @ModifiedBy : 
	 * @date  2019年12月26日 下午1:59:11
	 * @param userId 用户编号
	 * @param showStatus 上下架状态
	 * @param pageNo 第几页
	 * @param pageSize 每页多少条
	 * @return
	 */
	Page<RqDevTrade> rqDevTradeOnPublish(String userId,Integer showStatus, Integer pageNo, Integer pageSize);
	
	/**
	 * @description 根据条件获取设备租卖记录
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月25日 下午3:33:30
	 * @param sDate 开始时间
	 * @param eDate 结束时间
	 * @param checkSta 审核状态
	 * @param showSta 上下架状态
	 * @return
	 */
	List<RqDevTrade> listInfoByOpt(String sDate,String eDate,Integer checkSta,Integer showSta);
}
