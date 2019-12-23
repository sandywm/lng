package com.lng.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.lng.pojo.LngPriceDetail;

public interface LngPriceDetailDao   extends BaseDao<LngPriceDetail, Object> {

	/**
	 * @description 获取指定月份下每天的记录统计
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月23日 上午8:56:40
	 * @param gfId 液厂编号
	 * @param sDate 开始天
	 * @param eDate 结束天
	 * @return
	 */
	@Query(value = "select substring(lpd.priceTime,1,10),avg(lpd.price) from LngPriceDetail lpd where"
			+ " lpd.gf.id = ?1 and substring(lpd.priceTime,1,10) >= ?2 and substring(lpd.priceTime,1,10) <= ?3"
			+ " and lpd.price > 0 group by substring(lpd.priceTime,1,10) order by substring(lpd.priceTime,1,10) asc",
		nativeQuery = false
		)
	List<Object> findTjMonthInfoByGfId(String gfId,String sDate,String eDate);
	
	/**
	 * @description 按照指定年份下每月的记录统计
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月23日 上午8:57:22
	 * @param gfId 液厂编号
	 * @param sMonth 开始月
	 * @param eMonth 结束月
	 * @return
	 */
	@Query(value = "select substring(lpd.priceTime,1,7),avg(lpd.price) from LngPriceDetail lpd where"
			+ " lpd.gf.id = ?1 and substring(lpd.priceTime,1,7) >= ?2 and substring(lpd.priceTime,1,7) <= ?3"
			+ " and lpd.price > 0 group by substring(lpd.priceTime,1,7) order by substring(lpd.priceTime,1,7) asc",
		nativeQuery = false
		)
	List<Object> findTjYearInfoByGfId(String gfId,String sMonth,String eMonth);

	/**
	 * @description 根据指定日期一天的记录
	 * @author wm
	 * @Version : 1.0
	 * @ModifiedBy : 修改人
	 * @date  2019年12月23日 上午10:02:28
	 * @param gfId 液厂编号
	 * @param sDay 开始日
	 * @param eDay 结束日
	 * @return
	 */
	@Query(value = "select lpd.priceTime,lpd.price from LngPriceDetail lpd where"
			+ " lpd.gf.id = ?1 and lpd.priceTime >= ?2 and lpd.priceTime <= ?3"
			+ " order by lpd.priceTime asc",
		nativeQuery = false
		)
	List<Object> findTjDayInfoByGfId(String gfId,String sDay,String eDay);
}
