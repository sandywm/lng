package com.lng.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.lng.pojo.LngPriceDetail;

public interface LngPriceDetailDao   extends BaseDao<LngPriceDetail, Object> {

	@Query(value = "select substring(lpd.priceTime,1,10),avg(lpd.price) from LngPriceDetail lpd where"
			+ " lpd.gf.id = ?1 and substring(lpd.priceTime,1,10) >= ?2 and substring(lpd.priceTime,1,10) <= ?3"
			+ " group by substring(lpd.priceTime,1,10) order by substring(lpd.priceTime,1,10) asc",
		nativeQuery = false
		)
	List findTjInfoByGfId(String gfId,String sDate,String eDate);
}
