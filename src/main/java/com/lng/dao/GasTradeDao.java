package com.lng.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.lng.pojo.GasTrade;

public interface GasTradeDao  extends BaseDao<GasTrade, Object> {

	//sql原生写法
	@Query(value = "from GasTrade gt where 1=1"
		+ " and (?1 = '' or gt.company.id = ?1 )"
		+ " and gt.addUserId = ?2 "
		+ " and (?3 = '' or gt.gasType.id = ?3)"
		+ " and (?4 = '' or gt.gasFactory.id = ?4) "
		+ " and gt.checkStatus = ?5 "
		//+ " and (?4 = '' or exists(select gtp.id from GasTradePsArea gtp where gtp.gasTrade.id = gt.id and gtp.prov = ?4)) "
		+ " order by gt.addTime desc",
	nativeQuery = false
	)
	List<GasTrade> findPageInfoByOpt(String cpyId,String addUserId,String gasTypeId,String gfId,Integer checkStatus);

}
